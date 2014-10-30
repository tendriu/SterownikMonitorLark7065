package com.example.app.Sterownik;

import com.example.app.Sterownik.BinarySerialization.BinarySerializer;
import com.example.app.Sterownik.BinarySerialization.SerializableStruct;

import java.io.BufferedInputStream;
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
    public BufferedInputStream InStream;
    public String IP;
    public int Port;
    public boolean Connected;
    public boolean Connecting = false;

    public SterownikConnection(String ip, int port)
    {
        IP = ip;
        Port = port;
    }

    public void Connect()
    {

            (new Thread(new Runnable() {
                @Override
                public void run() {
                    try
                {
                    Connecting = true;
                    Client = new Socket(IP, Port);
                    Client.setSoTimeout(4000);

                    OutStream = new DataOutputStream(Client.getOutputStream());
                    InStream = new BufferedInputStream (Client.getInputStream());

                    Connected = true;
                    Connecting = false;
                    OnConnected();
                }
                catch (Exception ex)
                {
                    Connected = false;
                    Connecting = false;
                    OnConnectionError(ex.getMessage());
                }
            }})).start();
    }

    public void OnConnected() throws Exception {

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
        Connected = false;
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
        byte[] buff = new byte[length];
        InStream.read(buff, 0, length);

        return buff;
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
        String val = new String(buff, "UTF8");
        if(val.indexOf(0)>=0)
            return  val.substring(0,val.indexOf(0));
        return val;
    }

    public byte[] Procedure(short procID, int returnLength, byte... param) throws IOException
    {
        WriteProcedureHeader(procID, param);
        return ReadArray(returnLength);
    }

    public SerializableStruct GetProcedureData(int id) throws Exception
    {
        SerializableStruct ser = null;
        synchronized (Client)
        {
            OutStream.writeByte(id);
            int res = ReadInt08();
            if (res == 1)
            {
                ser = BinarySerializer.Deserialize(InStream,OutStream);
                ReadInt08();
            }
        }
        return ser;
    }
}
