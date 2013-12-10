package com.example.app.Sterownik;

/**
 * Created by nayk on 10.12.13.
 */
public class AppSettings {

    public int TempCO; //ustawiona temperatura pieca

    public int TempCWU; //ustawiona temperatura wody w wymienniku

    public int TempPompyCO; //temperatura włączania pompy CO

    public int HisterezaCO; //histereza sterowania piecem

    public TimeSpan CzasPrzedmuchu;//sekundy

    public TimeSpan OkresPrzedmuchu; //minuty

    public TimeSpan CzasWentDoBrakuPaliwa; //czas pracy wentylatora bez przyrostu temp. kotła

    public TimeSpan CzasWentDoWygaszenia; //czas pracy wentylatora bez przyrostu temp. do wygaszenia

    public int PwmWindMax, PwmWindMin;

    public boolean SterowanieKolektory;

    public float KolektoryDeltaWl;//róznica temperatur między kolektorami a CWU kiedy nastąpi załączenie pompy kolektorów

    public float KolektoryDeltaWyl; //róznica temperatur między kolektorami a CWU kiedy nastąpi wyłączenie pompy kolektorów

    public TimeSpan KolektoryCzasObiegu; //czas pewłnego obiegu płynu w instalacji kolektorów

    public int Godzina;

    public int Minuta;

}