package com.example.app.Sterownik;

/**
 * Created by nayk on 10.12.13.
 */

public abstract class Tryb {
    public TrybStatusEnum Status;
    public Tryb()
    {
        Status = TrybStatusEnum.Zatrzymany;
    }
}