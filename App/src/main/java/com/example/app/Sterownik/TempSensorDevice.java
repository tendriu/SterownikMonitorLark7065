package com.example.app.Sterownik;

/**
 * Created by nayk on 10.12.13.
 */

public class TempSensorDevice {

    public String Name;
    public String Description;
    public double Temperature;
    public TempSensorDeviceStatus Status;

    public TempSensorDevice() {
        Status = TempSensorDeviceStatus.OK;
    }

    public TempSensorDevice(String name) {
        Name = name;
    }

}
