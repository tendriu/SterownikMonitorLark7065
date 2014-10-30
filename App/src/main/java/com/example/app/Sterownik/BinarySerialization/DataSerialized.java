package com.example.app.Sterownik.BinarySerialization;

/**
 * Created by nayk on 2014-10-26.
 */
public class DataSerialized {
    public DataHeader Header;
    public Object Value;

    public <T> T Cast()
    {
        return (T)Value;
    }
}
