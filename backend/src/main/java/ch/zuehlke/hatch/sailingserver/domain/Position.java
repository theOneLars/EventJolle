package ch.zuehlke.hatch.sailingserver.domain;

import java.time.LocalDateTime;

public class Position {

    private final LocalDateTime timestamp;
    private final String longitude;
    private final String latitude;

    public Position(LocalDateTime timestamp, String longitude, String latitude) {
        this.timestamp = timestamp;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return "Position{" +
                "timestamp=" + timestamp +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
