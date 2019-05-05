package ch.zuehlke.hatch.sailingserver;

import org.apache.commons.cli.*;

import java.util.HashMap;
import java.util.Map;

public class SimulatorRunner {

    private static final String CLI_PARAMETER_FILE = "f";
    private static final String CLI_PARAMETER_THROTTLE = "it";

    private static final String DEFAULT_FILE = "/logs/eigeneLogs/signalk-rawdata.log.2017-07-12T04";

    public static void main(String... args) {
        System.out.println("Starting Simulator....");

        Map<String, String> parameter = parseParameter(args);

        String filePath = parameter.getOrDefault(CLI_PARAMETER_FILE, DEFAULT_FILE);
        String throttle = parameter.getOrDefault(CLI_PARAMETER_THROTTLE, "-1");


        sendFile(filePath, Integer.parseInt(throttle));
    }

    private static void sendFile(String filePath, int throttle) {
        Simulator simulator = new Simulator();
        simulator.sendFile(filePath, throttle);
        simulator.closeSocket();
    }


    /**
     * Imput paramter get parsed and returned as map.
     *
     * The parameter name is the key its value. E.g. -file some/file.txt has
     * key "file" and value "some/file.txt".
     */
    static Map<String, String> parseParameter(String[] args) {
        Map<String, String> parameter = new HashMap<>();

        CommandLine commandLine;
        Option optionFileToRead = Option.builder(CLI_PARAMETER_FILE)
                .required(false)
                .numberOfArgs(1)
                .desc("File to read for simulation")
                .longOpt("file")
                .build();

        Option optionInputThrottle = Option.builder(CLI_PARAMETER_THROTTLE)
                .required(false)
                .numberOfArgs(1)
                .desc("Input Throttle wil be uses as timing parameter to push messages")
                .longOpt("inputThrottle")
                .type(Boolean.class)
                .build();

        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        options.addOption(optionFileToRead);

        try {
            commandLine = parser.parse(options, args);

            if (commandLine.hasOption(CLI_PARAMETER_FILE)) {
                parameter.put(CLI_PARAMETER_FILE, commandLine.getOptionValue(CLI_PARAMETER_FILE));
            }

            if (commandLine.hasOption(CLI_PARAMETER_THROTTLE)) {
                parameter.put(CLI_PARAMETER_THROTTLE, commandLine.getOptionValue(CLI_PARAMETER_THROTTLE));
            }

        } catch (ParseException exception) {
            System.out.print("Parse error: ");
            System.out.println(exception.getMessage());
        }
        return parameter;
    }
}
