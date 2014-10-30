package com.example.app.Sterownik.BinarySerialization;

/**
 * Created by nayk on 2014-10-26.
 */
public enum DataID {
    BEGIN(254), END(255);

    private int value;

    private DataID(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
