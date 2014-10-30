package com.example.app.Sterownik.BinarySerialization;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Created by nayk on 2014-10-26.
 */
public class BinarySerializer
{
    public BufferedInputStream StrIn;
    public DataOutputStream StrOut;

    public BinarySerializer(BufferedInputStream streamIn, DataOutputStream streamOut)
    {
        StrIn = streamIn;
        StrOut = streamOut;
    }

    public static BinarySerializer Create(BufferedInputStream streamIn, DataOutputStream streamOut)
    {
        return new BinarySerializer(streamIn, streamOut);
    }

    public static SerializableStruct Deserialize(BufferedInputStream streamIn, DataOutputStream streamOut) throws  Exception
    {
        return BinarySerializer.Create(streamIn, streamOut).Deserialize();
    }

    public byte[] ReadArray(int length) throws IOException
    {
        if (length == 0) return null;
        byte[] buff = new byte[length];
        StrIn.read(buff, 0, length);

        return buff;
    }

    public int ReadInt08() throws IOException
    {
        byte[] val = ReadArray(1);

        return 0xFF & val[0];
    }

    public int ReadInt16() throws IOException
    {
        byte[] val = ReadArray(2);
        int val0 = 0xFF & val[0];
        int val1 = 0xFF & val[1];

        return (val0 | (val1 << 8));
    }

    public int ReadInt32() throws IOException
    {
        byte[] val = ReadArray(4);
        int val0 = 0xFF & val[0];
        int val1 = 0xFF & val[1];
        int val2 = 0xFF & val[2];
        int val3 = 0xFF & val[3];

        return ((val0) | (val1 << 8) | (val2 << 16) | (val3 << 24));
    }

    public DataHeader ReadHeader() throws IOException {
        DataHeader head = new DataHeader();
        head.Type = DataType.FromValue(ReadInt08());
        head.ID = ReadInt08();
        return head;
    }

    public int ReadDataU8() throws IOException
    {
        return ReadInt08();
    }

    public int ReadDataU16() throws IOException
    {
        return ReadInt16();
    }

    public int ReadDataU32() throws IOException
    {
        return ReadInt32();
    }

    public boolean ReadDataBool() throws IOException
    {
        int val = ReadInt08();
        return (val != 0);
    }

    public String ReadDataStringASCI() throws IOException
    {
        int length = ReadInt16();
        if (length > 0)
        {
            byte[] dataString = ReadArray(length);
            return new String(dataString, "UTF8");
        }
        return "";
    }

    public String ReadDataStringUNICODE()
    {
       /* var length = ReadDataU8();
        if (length > 0)
        {
            byte[] dataString = Str.ReadBuff(length);
            return Encoding.Unicode.GetString(dataString);
        }*/
        return "";
    }

    //public double ReadDataDouble()
    //{
    //    byte[] buff = new byte[4];
    //    Str.ReadBuff(buff,4);

    //    return MathEx.IEEE_754_to_float(buff);
    //}

    public double ReadDataDouble() throws IOException {
        byte[] buff = ReadArray(8);

        double val = ByteBuffer.wrap(buff).order(ByteOrder.LITTLE_ENDIAN).getDouble();

        return val;
    }

    public byte[] ReadDataU8Array() throws IOException
    {
        int length = ReadInt16();
        return ReadArray(length);
    }

    public Object ReadData(DataType dt) throws IOException
    {
        if (dt.getValue() < 128)
        {
            switch (dt)
            {
                case U8:
                    return ReadDataU8();
                case U16:
                    return ReadDataU16();
                case U32:
                    return ReadDataU32();
                case S8:
                    return (byte)ReadDataU8();
                case S16:
                    return (short)ReadDataU16();
                case S32:
                    return (int)ReadDataU32();
                case BOOL:
                    return ReadDataBool();
                case DOUBLE:
                    return ReadDataDouble();
                case STRING_ACSI:
                    return ReadDataStringASCI();
                case STRING_UNICODE:
                    return ReadDataStringUNICODE();
                //    case DataType.DOUBLE_PC:
                //       return ReadDataDoublePC();
                default:
                    return null;
            }
        }
        else
        {
            DataType arrayType = DataType.FromValue(dt.getValue() - 128);
            switch (arrayType)
            {
                case U8:
                    return ReadDataU8Array();
                  /*  case DataType.U16:
                        return ReadDataU16();
                    case DataType.U32:
                        return ReadDataU32();
                    case DataType.BOOL:
                        return ReadDataBool();
                    case DataType.DOUBLE:
                        return ReadDataDouble();
                    case DataType.STRING_ACSI:
                        return ReadDataStringASCI();
                    case DataType.STRING_UNICODE:
                        return ReadDataStringUNICODE();*/
                default:
                    return null;
            }
        }
    }

    public SerializableStruct Deserialize() throws  Exception
    {
        SerializableStruct obj = new SerializableStruct();
        obj.Header = ReadHeader();

        if (obj.Header.Type == DataType.STRUCT || obj.Header.ID == DataID.BEGIN.getValue())
        {
            while (true)
            {
                DataHeader hed = ReadHeader();

                if (hed.ID == DataID.END.getValue())
                    break;
                if (hed.ID == DataID.BEGIN.getValue())
                    continue;

                if (hed.Type != DataType.STRUCT)
                {
                    DataSerialized data = new DataSerialized();
                    data.Header = hed;
                    data.Value = ReadData(hed.Type);
                    obj.Data.add(data);
                }
                else
                {
                    DataSerialized data = new DataSerialized();
                    data.Header = hed;
                    data.Value = Deserialize();
                    obj.Data.add(data);
                }
            }
        }
        return obj;
    }
/*
    public void WriteHeader(DataHeader header)
    {
        Str.WriteByte((byte)header.Type);
        Str.WriteByte(header.ID);
    }

    public void WriteHeader(DataType dataType, byte id)
    {
        WriteHeader(new DataHeader() { ID = id, Type = dataType });
    }

    public void WriteDataU8(byte id, byte data)
    {
        WriteHeader(DataType.U8, id);
        Str.WriteByte(data);
    }

    public void WriteDataBool(byte id, bool data)
    {
        WriteHeader(DataType.BOOL, id);
        Str.WriteByte((byte)(data==true?1:0));
    }

    public void WriteDataU8Array(byte id, IEnumerable<byte> data)
    {
        WriteHeader(DataType.U8 | DataType.ARRAY, id);
        Str.WriteUint16((ushort)data.Count());
        Str.Write(data.ToArray(), 0, data.Count());
    }

    public void WriteDataS8(byte id, sbyte data)
    {
        WriteHeader(DataType.S8, id);
        Str.WriteByte((byte)data);
    }
    public void WriteDataU16(byte id, ushort data)
    {
        WriteHeader(DataType.U16, id);
        Str.WriteUint16(data);
    }
    public void WriteDataU32(byte id, uint data)
    {
        WriteHeader(DataType.U32, id);
        Str.WriteUint32(data);
    }
    public void WriteBeginData()
    {
        WriteHeader(DataType.U8,(byte)DataID.BEGIN);
    }
    public void WriteEndData()
    {
        WriteHeader(DataType.U8, (byte)DataID.END);
    }

    public void WriteDataStringUNICODE(byte id, String text)
    {
        WriteHeader(DataType.STRING_UNICODE, id);
        if (text != null)
        {
            Str.WriteByte((byte)(text.Length * 2));
            Str.Write(Encoding.Unicode.GetBytes(text), 0, text.Length * 2);
        }
        else
        {
            Str.WriteByte((byte)(0));
        }
    }

    public void WriteDataStringACSII(byte id, String text)
    {
        WriteHeader(DataType.STRING_ACSI, id);
        Thread.Sleep(3);
        if (text != null)
        {
            Str.WriteByte((byte)(text.Length));
            Str.Write(Encoding.ASCII.GetBytes(text), 0, text.Length);
        }
        else
        {
            Str.WriteByte((byte)(0));
        }
        Thread.Sleep(3);
    }

    public void WriteBeginStruct(byte id)
    {
        WriteHeader(DataType.STRUCT, id);
        Thread.Sleep(3);
        WriteBeginData();
    }

    public void WriteDataU8Array(byte id, byte[] array)
    {
        WriteHeader(DataType.U8 | DataType.ARRAY, id);

        Str.WriteUint16((ushort)array.Length);
        for (int x = 0; x < array.Length; x++)
            Str.WriteByte(array[x]);
    }

    public unsafe void WriteDataDoublePC(byte id, double value)
{
    WriteHeader(DataType.DOUBLE_PC, id);

    byte* data = (byte*)&value;
    Str.Write(new byte[] { data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8] }, 0, 8);
}

    public unsafe void WriteDataDouble(byte id, double value)
{
    WriteHeader(DataType.DOUBLE, id);

    byte* data = (byte*)&value;
    Str.Write(new byte[] { data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8] }, 0, 8);
}

    public void WriteData(byte id, byte value)
    {
        WriteDataU8(id, value);
    }
    public void WriteData(byte id, sbyte value)
    {
        WriteDataS8(id, value);
    }
    public void WriteData(byte id, ushort value)
    {
        WriteDataU16(id, value);
    }
    public void WriteData(byte id, uint value)
    {
        WriteDataU32(id, value);
    }
    public void WriteData(byte id, double value)
    {
        WriteDataDouble(id, value);
    }
    public void WriteData(byte id, bool value)
    {
        WriteDataBool(id, value);
    }
*/
}