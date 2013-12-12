package com.example.app.Sterownik;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by nayk on 11.12.13.
 */
public class PompaView extends View {
    public PompaView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float wid = getWidth();
        float hei = getHeight();

        Paint p = new Paint();
        p.setColor(Color.GRAY);
        canvas.drawCircle(wid / 2f,hei / 2f , wid / 2,p);

        p.setColor(Color.WHITE);
        canvas.drawCircle((wid) / 2f,(hei) / 2f , wid / 2.5f,p);

        double kat = (Math.PI * 210) / 180;
        double x = Math.sin(kat) * hei / 2;
        double y = Math.cos(kat) * hei / 2;

        p.setColor(Color.BLACK);
        canvas.drawLine(wid/2,hei,(float)(x + wid /2),(float)(y + hei/2),p);

    }


}
