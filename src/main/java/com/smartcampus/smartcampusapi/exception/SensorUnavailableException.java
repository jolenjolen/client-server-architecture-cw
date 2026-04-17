package com.smartcampus.smartcampusapi.exception;

public class SensorUnavailableException extends RuntimeException {
    public SensorUnavailableException(String sensorId) {
        super("Sensor " + sensorId + " is under maintenance and cannot accept new readings.");
    }
}