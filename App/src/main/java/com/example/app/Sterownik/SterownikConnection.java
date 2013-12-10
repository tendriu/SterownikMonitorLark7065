package com.example.app.Sterownik;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Created by nayk on 10.12.13.
 */
public class SterownikConnection
{
    public Socket Client;
    public DataOutputStream OutStream;
    public BufferedReader InStream;
    public String IP;
    public int Port;
    public boolean Connected;

    public SterownikConnection(String ip, int port)
    {
        IP = ip;
        Port = port;
    }

    public void Connect()
    {
        try
        {
            Client = new Socket(IP, Port);

            OutStream = new DataOutputStream(Client.getOutputStream());
            InStream = new BufferedReader(new InputStreamReader(Client.getInputStream()));

            Connected = true;
        }
        catch (Exception ex)
        {
            OnConnectionError(ex.getMessage());
        }
    }

    void OnConnectionError(String message)
    {
        Connected = false;
    }

    public void Disconnect() throws IOException {
        if (Client != null && Client.isConnected())
        {
            Client.close();
        }
    }

    protected void WriteProcedureHeader(short procID, byte... param) throws IOException {
        //  Stream.Write(new byte[] { (byte)procID, (byte)(procID >> 8), (byte)param.Length, 0 }, 0, 4);
        OutStream.write(new byte[]{(byte) procID, (byte) (procID >> 8), (byte) param.length, 0}, 0, 4);
        if (param.length > 0)
        {
            // Stream.Write(param, 0, param.Length);
            OutStream.write(param, 0, param.length);
        }
    }

    public byte[] ReadArray(int length) throws IOException
    {
        if (length == 0) return null;
        char[] buff = new char[length];
        InStream.read(buff, 0, length);
        return Charset.forName("UTF-8").encode(CharBuffer.wrap(buff)).array();
    }

    public int ReadInt08() throws IOException
    {
        byte[] val = ReadArray(1);

        return (val[0]);
    }

    public int ReadInt16() throws IOException
    {
        byte[] val = ReadArray(2);

        return ((val[0]) | (val[1] << 8));
    }

    public int ReadInt32() throws IOException
    {
        byte[] val = ReadArray(4);

        return ((val[0]) | (val[1] << 8) | (val[2] << 16) | (val[3] << 24));
    }

    public String ReadString(int length) throws UnsupportedEncodingException,IOException {
        byte[] buff = ReadArray(length);
        return new String(buff, "UTF8");
    }

    public byte[] Procedure(short procID, int returnLength, byte... param) throws IOException
    {
        WriteProcedureHeader(procID, param);
        return ReadArray(returnLength);
    }
}
