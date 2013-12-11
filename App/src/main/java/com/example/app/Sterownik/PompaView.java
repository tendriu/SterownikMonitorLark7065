package com.example.app.Sterownik;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nayk on 11.12.13.
 */
public class PompaView extends View {
    public PompaView(Context context) {
        super(context);
    }

    public PompaView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PompaView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float wid = getWidth();
        float hei = getHeight();

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.GRAY);
      //  canvas.drawCircle(wid / 2f,hei / 2f , wid / 2,p);

      //  p.setColor(Color.WHITE);
     //   canvas.drawCircle((wid) / 2f,(hei) / 2f , wid / 2.5f,p);
        canvas.drawArc(new RectF(0,0,wid,hei),0,360,true,p);
        p.setColor(Color.WHITE);
        canvas.drawArc(new RectF(2,2,wid-2,hei-2),0,360,true,p);

    }


}
