package com.example.app.Sterownik;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nayk on 11.12.13.
 */
public class PompaView extends View {

    public boolean Rotating = false;
    Animation anim = null;
    int Bacground = Color.GRAY;

    public PompaView(Context context) {
        super(context);
    }

    public PompaView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PompaView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void StartRotate(Activity activity) {
        if (!Rotating) {
            Rotating = true;
            if(anim == null)
            {
                anim = AnimationUtils.loadAnimation(activity,R.anim.rotating_pompa);
            }
            this.startAnimation(anim);
            Bacground = Color.argb(255,37,166,253);
            invalidate();
        }
    }

    public void StopRotating() {
        if (Rotating) {
            Rotating = false;
            if(anim!=null)
            {
                anim.cancel();
                anim = null;
                clearAnimation();
            }
            Bacground = Color.GRAY;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float wid = getWidth();
        float hei = getHeight();

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.GRAY);
        canvas.drawArc(new RectF(0, 0, wid, hei), 0, 360, true, p);

        p.setColor(Color.WHITE);
        canvas.drawArc(new RectF(2, 2, wid - 2, hei - 2), 0, 360, true, p);

        double rx = (wid / 2) * 0.6;
        double ry = (hei / 2) * 0.6;
        double kat = (Math.PI * 120) / 180;
        double x = Math.sin(kat) * rx;
        double y = Math.cos(kat) * ry;

        p.setColor(Bacground);


        Path path = new Path();
        path.moveTo(wid / 2, (float) (ry + hei / 2));
        path.lineTo((float) (x + wid / 2), (float) (y + hei / 2));
        path.lineTo((float) (-x + wid / 2), (float) (y + hei / 2));
        canvas.drawPath(path, p);
    }


}
