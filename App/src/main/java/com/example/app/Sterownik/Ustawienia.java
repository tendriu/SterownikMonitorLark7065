package com.example.app.Sterownik;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import java.io.IOException;

public class Ustawienia extends Fragment {

    private MainActivity mainActivity;

    // TODO: Rename and change types and number of parameters
    public static Ustawienia newInstance() {
        Ustawienia fragment = new Ustawienia();
        return fragment;
    }

    public Ustawienia() {
        // Required empty public constructor
    }

    boolean showed = false;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (mainActivity == null) return;


        if (hidden == false) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        mainActivity.SterownikClient.ReadSettings();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            NumberPicker cop = (NumberPicker) mainActivity.findViewById(R.id.tempcoPicker);
                            NumberPicker pompa = (NumberPicker) mainActivity.findViewById(R.id.temppompyPicker);
                            NumberPicker hist = (NumberPicker) mainActivity.findViewById(R.id.histrezaPicker);


                            cop.setValue(mainActivity.SterownikClient.Settings.TempCO);
                            pompa.setValue(mainActivity.SterownikClient.Settings.TempPompyCO);
                            hist.setValue(mainActivity.SterownikClient.Settings.HisterezaCO);
                        }
                    });


                }
            }).start();

        } else {

            NumberPicker cop = (NumberPicker) mainActivity.findViewById(R.id.tempcoPicker);
            NumberPicker pompa = (NumberPicker) mainActivity.findViewById(R.id.temppompyPicker);
            NumberPicker hist = (NumberPicker) mainActivity.findViewById(R.id.histrezaPicker);

            mainActivity.SterownikClient.Settings.TempCO = cop.getValue();
            mainActivity.SterownikClient.Settings.TempPompyCO = pompa.getValue();
            mainActivity.SterownikClient.Settings.HisterezaCO = hist.getValue();

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        mainActivity.SterownikClient.WriteSettings();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ustawienia, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NumberPicker cop = (NumberPicker) mainActivity.findViewById(R.id.tempcoPicker);
        if (cop != null) {
            cop.setMinValue(40);
            cop.setMaxValue(80);
        }
        NumberPicker pompa = (NumberPicker) mainActivity.findViewById(R.id.temppompyPicker);
        if (pompa != null) {
            pompa.setMinValue(20);
            pompa.setMaxValue(45);
        }
        NumberPicker hist = (NumberPicker) mainActivity.findViewById(R.id.histrezaPicker);
        if (hist != null) {
            hist.setMinValue(0);
            hist.setMaxValue(5);
        }
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
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
