package com.example.app.Sterownik;

/**
 * Created by nayk on 2014-10-26.
 */
public enum TempSensorDeviceStatus {
    OK(0), CRC_ERROR(1), NOT_FOUND(2);

    private int value;

    private TempSensorDeviceStatus(int value) {
        this.value = value;
    }

    public static TempSensorDeviceStatus FromInt(int value)
    {
        switch (value)
        {
            case 0:
                return OK;
            case 1:
                return CRC_ERROR;
            case 2:
                return NOT_FOUND;
            default:
                return OK;
        }
    }

    public int getValue() {
        return value;
    }
}
