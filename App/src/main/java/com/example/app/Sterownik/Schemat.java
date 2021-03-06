package com.example.app.Sterownik;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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

boolean state = false;
    public void Update() throws IOException {

        if (mainActivity.SterownikClient.Connected)
        {
            try
            {
                mainActivity.SterownikClient.ReadTemps();
                mainActivity.SterownikClient.ReadTrybCO();
                mainActivity.SterownikClient.ReadDevicesState();
            }catch (Exception ex)
            {
                mainActivity.SterownikClient.Disconnect();
            }
        }
        else
        {
            if(mainActivity.SterownikClient.Connecting == false)
                 mainActivity.SterownikClient.Connect();
        }

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                    TextView brakPol = (TextView)mainActivity.findViewById(R.id.brakpolView);

                    if(brakPol!=null)
                    {
                        if(mainActivity.SterownikClient.Connected)
                            brakPol.setVisibility(View.INVISIBLE);
                        else
                            brakPol.setVisibility(View.VISIBLE);
                    }


                    TempSensorView co = (TempSensorView) mainActivity.findViewById(R.id.co);
                    TempSensorView cwu = (TempSensorView) mainActivity.findViewById(R.id.cwu);
                    TempSensorView zew = (TempSensorView) mainActivity.findViewById(R.id.zew);
                    TempSensorView kwej = (TempSensorView) mainActivity.findViewById(R.id.kwej);
                    TempSensorView kwyj = (TempSensorView) mainActivity.findViewById(R.id.kwyj);
                    TempSensorView bwej = (TempSensorView) mainActivity.findViewById(R.id.bwej);
                    TempSensorView bwyj = (TempSensorView) mainActivity.findViewById(R.id.bwyj);
                    TempSensorView dom = (TempSensorView) mainActivity.findViewById(R.id.dom);

                    if (co != null) {
                        co.UpdateValues(mainActivity.SterownikClient.Temps.get("CO"));
                        cwu.UpdateValues(mainActivity.SterownikClient.Temps.get("CWU"));
                        zew.UpdateValues(mainActivity.SterownikClient.Temps.get("Zew"));
                        kwej.UpdateValues(mainActivity.SterownikClient.Temps.get("KolWej"));
                        kwyj.UpdateValues(mainActivity.SterownikClient.Temps.get("KolWyj"));
                        bwej.UpdateValues(mainActivity.SterownikClient.Temps.get("BojWej"));
                        bwyj.UpdateValues(mainActivity.SterownikClient.Temps.get("BojWyj"));
                        dom.UpdateValues(mainActivity.SterownikClient.Temps.get("Dom"));
                    }

                TextView trybinfoText = (TextView)mainActivity.findViewById(R.id.trybinfo_text);

                if(trybinfoText != null)
                {
                    String text = "";
                    int bacgroud = Color.WHITE;
                    int visiblity = View.INVISIBLE;

                    if(mainActivity.SterownikClient.TrybCO.BrakPaliwa)
                    {
                        text = "BRAK PALIWA";
                        bacgroud =Color.RED;
                        visiblity = View.GONE;

                        if(mainActivity.SterownikClient.TrybCO.Status == TrybStatusEnum.Sterowanie)
                        {
                            MediaPlayer player = MediaPlayer.create(mainActivity,Settings.System.DEFAULT_NOTIFICATION_URI);
                            player.start();
                        }

                    }else
                    if(mainActivity.SterownikClient.TrybCO.Grzanie)
                    {
                        text = "GRZANIE";
                        bacgroud =Color.BLUE;
                        visiblity = View.VISIBLE;
                    }else
                    if(mainActivity.SterownikClient.TrybCO.Przedmuch)
                    {
                        text = "PRZEDMUCH";
                        bacgroud =Color.BLUE;
                        visiblity = View.VISIBLE;
                    }else
                    {
                        visiblity = View.INVISIBLE;
                    }

                    trybinfoText.setText(text);
                    trybinfoText.setBackgroundColor(bacgroud);
                    SetViewVisiblityFade(trybinfoText,visiblity);
                }

                TextView statusText = (TextView)mainActivity.findViewById(R.id.trybstatus_text);
                TextView trybText = (TextView)mainActivity.findViewById(R.id.tryb_text);

                if(statusText!=null && trybText != null)
                {
                    if(mainActivity.SterownikClient.TrybCO.Status == TrybStatusEnum.Zatrzymany)
                    {
                        SetViewVisiblityFade(statusText,View.INVISIBLE);
                        SetViewVisiblityFade(trybText,View.INVISIBLE);
                    }else
                    {
                        String status = mainActivity.SterownikClient.TrybCO.Status.toString().toUpperCase();
                        statusText.setText("STATUS: " + status);
                        trybText.setText("TRYB CO, TEMP. ZADANA: " + String.valueOf(mainActivity.SterownikClient.Settings.TempCO));
                        SetViewVisiblityFade(statusText,View.VISIBLE);
                        SetViewVisiblityFade(trybText,View.VISIBLE);
                    }
                }

                PompaView pco = (PompaView)mainActivity.findViewById(R.id.pompa_co);
                PompaView pkol = (PompaView)mainActivity.findViewById(R.id.pompa_kol);
                WentylatorView went = (WentylatorView)mainActivity.findViewById(R.id.wentylator);

                if(mainActivity.SterownikClient.PompaCO.Status)
                    pco.StartRotate(mainActivity);
                else pco.StopRotating();

                if(mainActivity.SterownikClient.PompaKolektory.Status)
                    pkol.StartRotate(mainActivity);
                else pkol.StopRotating();

                if(mainActivity.SterownikClient.Wentylator.Status)
                    went.StartRotate(mainActivity);
                else went.StopRotating();

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView img =(ImageView) mainActivity.findViewById(R.id.imageView);
        if(img!=null)
        {
            BitmapFactory.Options myOptions = new BitmapFactory.Options();
            myOptions.inDither = true;
            myOptions.inScaled = false;
            myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            myOptions.inDither = false;
            myOptions.inPurgeable = true;
            Bitmap preparedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tlo, myOptions);
            Drawable background = new BitmapDrawable(preparedBitmap);
            img.setImageDrawable(background);
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

            TimerLoop = new Timer();
            TimerLoop.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        Update();
                    } catch (Exception ex) {

                    }
                }
            }, 0, 4000);

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    void SetViewVisiblityFade(View view, int visiblity)
    {
        if(visiblity == View.INVISIBLE)
        {
            if(view.getVisibility() == View.VISIBLE) {

                AnimationSet out_ =(AnimationSet) AnimationUtils.makeOutAnimation(mainActivity, false);
                view.startAnimation(out_);
                view.setVisibility(View.INVISIBLE);
            }
        }else
        if(view.getVisibility() == View.INVISIBLE)
        {
            Animation in = AnimationUtils.loadAnimation(mainActivity, R.anim.fade_id);
            view.startAnimation(in);
            view.setVisibility(View.VISIBLE);
        }
        else
          if(visiblity == View.GONE)
          {
              Animation pulse = AnimationUtils.loadAnimation(mainActivity, R.anim.pulse);
              if(view.getAnimation() != pulse)
              {
                  view.clearAnimation();
                  view.startAnimation(pulse);
                  view.setVisibility(View.VISIBLE);
              }
          }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        TimerLoop.cancel();
    }
}
