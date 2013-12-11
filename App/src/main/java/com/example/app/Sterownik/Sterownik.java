package com.example.app.Sterownik;

import android.util.Xml;

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
    public void OnConnected() throws IOException {
        super.OnConnected();

        ReadSettings();
    }

    public String GetSterownikIP() {
        return "192.168.0.12";
    }

    public void ReadTemps() throws IOException {
        synchronized (Client) {
            ReadAllTemps();
        }
    }

    private void ReadAllTemps() throws IOException {

        WriteProcedureHeader((short) 5);
        int count = ReadInt08();
        for (int x = 0; x < count; x++) {
            int nameLength = ReadInt08();
            String name = ReadString(nameLength);
            byte[] buff = ReadArray(4);
            if (Temps.containsKey(name)) {
                Temps.get(name).Temperature = (buff[0] + buff[1] / 100.0f) * (buff[2] == 1 ? -1 : 1);
            }
        }
    }

    public void WriteSetting(String valueName, String value) throws IOException {
        ByteArrayOutputStream mem = new ByteArrayOutputStream();
        byte[] nameBuff = valueName.getBytes();
        mem.write((byte) nameBuff.length);
        mem.write(nameBuff, 0, nameBuff.length);
        byte[] valueBuff = value.getBytes();
        mem.write(valueBuff, 0, valueBuff.length);
        WriteProcedureHeader((short) 6, mem.toByteArray());
    }

    public void ReadDevicesState() throws IOException {
        synchronized (Client) {
            WriteProcedureHeader((short) 3, (byte) 1);
            PompaCO.Status = (ReadArray(1)[0] == 1);
            WriteProcedureHeader((short) 3, (byte) 2);
            PompaKolektory.Status = (ReadArray(1)[0] == 1);
            WriteProcedureHeader((short) 3, (byte) 3);
            Wentylator.Status = (ReadArray(1)[0] == 1);
        }
    }

    public String ReadSetting(String name) throws IOException {
        WriteProcedureHeader((short) 4, name.getBytes());
        byte[] len = ReadArray(2);
        int length = (len[0] | (len[1] << 8));
        byte[] valBuff = ReadArray(length);
        String value = new String(valBuff);
        if(value.indexOf(0)>=0)
            return  value.substring(0,value.indexOf(0));
        return value;
    }

    public void WriteSettings() throws IOException {
        synchronized (Client) {
            WriteSetting("TempCO", String.valueOf(Settings.TempCO));
            WriteSetting("HisterezaCO", String.valueOf(Settings.HisterezaCO));
            WriteSetting("TempPompyCO", String.valueOf(Settings.TempPompyCO));
            //  WriteSetting("CzasPrzedmuchu", String.valueOf(Settings.CzasPrzedmuchu.Ticks));
            //   WriteSetting("CzasWentDoBrakuPaliwa",String.valueOf(Settings.CzasWentDoBrakuPaliwa.Ticks));
            //  WriteSetting("CzasWentDoWygaszenia",String.valueOf( Settings.CzasWentDoWygaszenia.Ticks));
            //  WriteSetting("OkresPrzedmuchu", String.valueOf(Settings.OkresPrzedmuchu.));
        }
    }

    public void ReadSettings() throws IOException {
        synchronized (Client) {

            Settings.TempCO = Integer.parseInt(ReadSetting("TempCO"));
            Settings.HisterezaCO = Integer.parseInt(ReadSetting("HisterezaCO"));
            Settings.TempPompyCO = Integer.parseInt(ReadSetting("TempPompyCO"));

        }
       /* TimeSpan timeSpanValue = TimeSpan.Zero;
        if (TimeSpan.TryParse(ReadSetting("CzasPrzedmuchu"), out timeSpanValue))
        {
        Settings.CzasPrzedmuchu = timeSpanValue;
        Settings.OnPropertyChanged("CzasPrzedmuchu");
        }
        if (TimeSpan.TryParse(ReadSetting("CzasWentDoBrakuPaliwa"), out timeSpanValue))
        {
        Settings.CzasWentDoBrakuPaliwa = timeSpanValue;
        Settings.OnPropertyChanged("CzasWentDoBrakuPaliwa");
        }
        if (TimeSpan.TryParse(ReadSetting("CzasWentDoWygaszenia"), out timeSpanValue))
        {
        Settings.CzasWentDoWygaszenia = timeSpanValue;
        Settings.OnPropertyChanged("CzasWentDoWygaszenia");
        }
        if (TimeSpan.TryParse(ReadSetting("OkresPrzedmuchu"), out timeSpanValue))
        {
        Settings.OkresPrzedmuchu = timeSpanValue;
        Settings.OnPropertyChanged("OkresPrzedmuchu");
        }*/
    }

    public void ReadTrybCO() throws IOException {
        byte[] data = null;
        synchronized (Client) {
            WriteProcedureHeader((short) 2, (byte) 1);
            data = ReadArray(4);
        }
        TrybCO.Status = TrybStatusEnum.values()[data[0]];
        TrybCO.Przedmuch = (data[1] == 0 ? false : true);
        TrybCO.BrakPaliwa = (data[2] == 0 ? false : true);
        TrybCO.Grzanie = (data[3] == 0 ? false : true);

    }


}
