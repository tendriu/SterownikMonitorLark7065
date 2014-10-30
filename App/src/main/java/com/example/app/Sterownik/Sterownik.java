package com.example.app.Sterownik;

import android.util.Xml;

import com.example.app.Sterownik.BinarySerialization.DataSerialized;
import com.example.app.Sterownik.BinarySerialization.SerializableStruct;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by nayk on 10.12.13.
 */


public class Sterownik extends SterownikConnection {
    public Hashtable<String, TempSensorDevice> Temps;
    public boolean Updating;
    public TrybCO TrybCO;
    public TrybCWU TrybCWU;
    public TrybKolektory TrybKolektory;
    public Device PompaCO;
    public Device PompaKolektory;
    public Device Wentylator;
    public AppSettings Settings;

    public Sterownik(String ip, int port) {
        super(ip, port);
        Temps = new Hashtable<String, TempSensorDevice>();
        Temps.put("CO", new TempSensorDevice("CO"));
        Temps.put("CWU", new TempSensorDevice("CWU"));
        Temps.put("KolWyj", new TempSensorDevice("KolWyj"));
        Temps.put("KolWej", new TempSensorDevice("KolWej"));
        Temps.put("BojWej", new TempSensorDevice("BojWej"));
        Temps.put("BojWyj", new TempSensorDevice("BojWyj"));
        Temps.put("Zew", new TempSensorDevice("Zew"));
        Temps.put("Dom", new TempSensorDevice("Dom"));

        TrybCO = new TrybCO();
        TrybCWU = new TrybCWU();
        TrybKolektory = new TrybKolektory();
        PompaCO = new Device();
        PompaKolektory = new Device();
        Wentylator = new Device();

        Settings = new AppSettings();
    }

    @Override
    public void OnConnected() throws Exception {
        super.OnConnected();

        ReadSettings();
    }

    public String GetSterownikIP() {
        return "192.168.88.38";
    }

    public void ReadTemps() throws Exception {
        synchronized (Client) {
            try {
                ReadAllTemps();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void ReadAllTemps() throws Exception {

        SerializableStruct data = GetProcedureData((byte) 4);
        if (data == null) return;

        for (DataSerialized dd : data.Data)
        {
            SerializableStruct czujnikData = (SerializableStruct)dd.Value;
            String nazwa = czujnikData.GetData(0);
            if (Temps.containsKey(nazwa))
            {
                TempSensorDevice temp = Temps.get(nazwa);
                temp.Temperature = czujnikData.GetData(1);
            //    temp.Type = (TempSensorDeviceType)czujnikData.GetData<byte>(2);
                int stat = czujnikData.GetData(3);
                temp.Status = TempSensorDeviceStatus.FromInt(stat);
            }
        }
    }

    public void ReadDevicesState() throws Exception {

        SerializableStruct data = GetProcedureData(5);
        if (data == null) return;

        PompaCO.Status = data.GetData(0);
        PompaKolektory.Status = data.GetData(1);
        Wentylator.Status = data.GetData(2);
    }

    public void WriteSettings() throws IOException {
       /* synchronized (Client) {
            WriteSetting("TempCO", String.valueOf(Settings.TempCO));
            WriteSetting("HisterezaCO", String.valueOf(Settings.HisterezaCO));
            WriteSetting("TempPompyCO", String.valueOf(Settings.TempPompyCO));
            //  WriteSetting("CzasPrzedmuchu", String.valueOf(Settings.CzasPrzedmuchu.Ticks));
            //   WriteSetting("CzasWentDoBrakuPaliwa",String.valueOf(Settings.CzasWentDoBrakuPaliwa.Ticks));
            //  WriteSetting("CzasWentDoWygaszenia",String.valueOf( Settings.CzasWentDoWygaszenia.Ticks));
            //  WriteSetting("OkresPrzedmuchu", String.valueOf(Settings.OkresPrzedmuchu.));
        }*/
    }

    public void ReadSettings() throws Exception {

        SerializableStruct data = GetProcedureData(7);
        if (data == null) return;

        Settings.TempCO = data.GetData(0);
        Settings.TempCWU = data.GetData(1);
        Settings.TempPompyCO = data.GetData(2);
    }

    public void ReadTrybCO() throws Exception {

        SerializableStruct data = GetProcedureData(6);
        if (data == null) return;

        int status = data.GetData(1);
        TrybCO.Status = TrybStatusEnum.values()[status];
        TrybCO.Przedmuch = data.GetData(2);
        TrybCO.BrakPaliwa = data.GetData(4);
        TrybCO.Grzanie = data.GetData(3);
    }


}
