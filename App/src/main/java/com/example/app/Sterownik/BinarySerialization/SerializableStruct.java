package com.example.app.Sterownik.BinarySerialization;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nayk on 2014-10-26.
 */
public class SerializableStruct {
    public DataHeader Header;
    public ArrayList<DataSerialized> Data;

    public SerializableStruct() throws Exception
    {
        Data = new ArrayList<DataSerialized>();
    }

    public boolean ExistsID(int id)
    {
        return FindFromID(id) != null;
    }

    DataSerialized FindFromID(int id)
    {
        DataSerialized ret = null;
        for(int x=0;x<Data.size();x++)
        {
            if(Data.get(x).Header.ID == id)
            {
                ret = Data.get(x);
                break;
            }
        }
        return  ret;
    }

    public <T> T GetData(int id)
    {
        DataSerialized data = FindFromID(id);
        if (data != null)
        {
            return (T)data.Value;
        }
        T hh = null;
        return hh;
            /*
            if (Data.ContainsKey(id))
                return (T)Data[id];
            else return default(T);*/
    }

  /*  public T Cast<T>()
    {
        if (typeof(T) == typeof(DateTime))
        {
            try
            {
                object obj = new DateTime(GetData<ushort>(1), GetData<byte>(2), GetData<byte>(3), GetData<byte>(4), GetData<byte>(5), GetData<byte>(6));
                return (T)obj;
            }
            catch (Exception)
            {
                object obj = new DateTime();
                return (T)obj;
            }

        }
        else
            throw new Exception("Określone rzutowanie jest niepoprawne!");
    }*/
}
