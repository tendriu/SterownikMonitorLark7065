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


    }


}
