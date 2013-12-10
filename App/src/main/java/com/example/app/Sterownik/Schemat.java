package com.example.app.Sterownik;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class Schemat extends Fragment {

    public MainActivity mainActivity;
    private Timer TimerLoop;

    // TODO: Rename and change types and number of parameters
    public static Schemat newInstance() {
        Schemat fragment = new Schemat();
        return fragment;
    }

    public Schemat() {

    }

    public void Update() throws IOException {

        if (mainActivity.SterownikClient.Connected)
            mainActivity.SterownikClient.ReadTemps();

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                    TempSensorView co = (TempSensorView) mainActivity.findViewById(R.id.co);
                    TempSensorView cwu = (TempSensorView) mainActivity.findViewById(R.id.cwu);
                    TempSensorView zew = (TempSensorView) mainActivity.findViewById(R.id.zew);
                    TempSensorView kwej = (TempSensorView) mainActivity.findViewById(R.id.kwej);
                    TempSensorView kwyj = (TempSensorView) mainActivity.findViewById(R.id.kwyj);
                    TempSensorView bwej = (TempSensorView) mainActivity.findViewById(R.id.bwej);
                    TempSensorView bwyj = (TempSensorView) mainActivity.findViewById(R.id.bwyj);
                    TempSensorView dom = (TempSensorView) mainActivity.findViewById(R.id.dom);

                    if (co != null) {
                        co.SetTemperature(mainActivity.SterownikClient.Temps.get("CO").Temperature);
                        cwu.SetTemperature(mainActivity.SterownikClient.Temps.get("CWU").Temperature);
                        zew.SetTemperature(mainActivity.SterownikClient.Temps.get("Zew").Temperature);
                        kwej.SetTemperature(mainActivity.SterownikClient.Temps.get("KolWej").Temperature);
                        kwyj.SetTemperature(mainActivity.SterownikClient.Temps.get("KolWyj").Temperature);
                        bwej.SetTemperature(mainActivity.SterownikClient.Temps.get("BojWej").Temperature);
                        bwyj.SetTemperature(mainActivity.SterownikClient.Temps.get("BojWyj").Temperature);
                        dom.SetTemperature(mainActivity.SterownikClient.Temps.get("Dom").Temperature);
                    }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schemat, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mainActivity != null) {
            mainActivity.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {

            mainActivity = (MainActivity) activity;

            TimerLoop = new Timer();
            TimerLoop.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        Update();
                    } catch (Exception ex) {

                    }
                }
            }, 0, 1000);

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        TimerLoop.cancel();
    }
}
