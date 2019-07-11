package ch.zuehlke.hatch.sailingserver;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

public class Simulator {

    private DatagramSocket socket;

    public static final int DEFAULT_PORT = 4123;

    public Simulator() {

        try {
            this.socket = new DatagramSocket();
        } catch (IOException e) {
            System.out.println("Could not init Simulator: " + e.getMessage());
        }
    }

    private void sendMessage(String message) {

        try {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, message.length(), InetAddress.getLocalHost(), DEFAULT_PORT);
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("Could not sent message: " + message + "\nERROR:\n" + e.getMessage());
        }
    }

    public boolean closeSocket() {
        socket.close();
        return socket.isClosed();
    }

    public void sendFile(String filePath, int inputThrottle) {

        File file = new File(this.getClass().getResource(filePath).getPath());

        if (!file.exists() || !file.canRead()) {
            System.err.println("File does not exist or is not readable");
        }

        List<File> files = new ArrayList<>();
        if (file.isDirectory()) {
            files = Arrays.asList(file.listFiles());
        } else {
            files.add(file);
        }

        files.sort((file1, file2) -> file2.getName().compareTo(file1.getName()));

        for (File singleFile : files) {
            System.out.println("Processing file: " + singleFile.getName());
            try (BufferedReader reader = new BufferedReader(new FileReader(singleFile))) {
                long timePreviousLine = 0;

                String line;

                do {
                    line = reader.readLine();
                    if (StringUtils.isBlank(line)) {
                        continue;
                    }

                    Pair<Long, String> timeJson = extractTimeJson(line);
//                    System.out.println(timeJson.getValue());
                    sendMessage(timeJson.getValue());

                    if (inputThrottle == -1) {
                        Long timeCurrentLine = timeJson.getKey();
                        timePreviousLine = (timePreviousLine == 0 ? timeCurrentLine : timePreviousLine);
                        try {
                            Thread.sleep(calculateWaitTime(timePreviousLine, timeCurrentLine));
                        } catch (InterruptedException e) {
                            System.err.println("Something went wrong while sleeping: " + e.getMessage());
                        }
                        timePreviousLine = timeCurrentLine;
                    } else {
                        try {
                            Thread.sleep(inputThrottle);
                        } catch (InterruptedException e) {
                            System.err.println("Something went wrong while sleeping: " + e.getMessage());
                        }
                    }
                } while(StringUtils.isNotBlank(line));
            } catch (IOException e) {
                System.err.println("Something went wrong while reading file: " + e.getMessage());
            }
        }
    }

    private long calculateWaitTime(long timePreviousLine, Long timeCurrentLine) {
        long timeDiff = timeCurrentLine - timePreviousLine;
        return timeDiff > 0 ? timeDiff : 0;
    }

    /**
     * Returns a {@link Pair} with <timestamp of line, string of line>.
     *
     * Format of limes:
     *     timestamp; [I]; json of data
     *     e.g.: 1498581040596;I;{some: json}
     * @param line
     * @return
     */
    private Pair<Long, String> extractTimeJson(String line) {
        long time = 0;
        String json = "";
        Pair<Long, String> timeJson = new Pair<>(time, json);

        Scanner sc = new Scanner(line).useDelimiter(";");
        if (sc.hasNextLong()) {
            time = sc.nextLong();
            sc.next();
            json = sc.next();
            timeJson = new Pair<>(time, json);
        } else {
            System.out.println("No timestamp found in line");
        }

        return timeJson;
    }
}
