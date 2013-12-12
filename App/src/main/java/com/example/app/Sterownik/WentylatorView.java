package com.example.app.Sterownik;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by nayk on 12.12.13.
 */
public class WentylatorView extends View {

    public boolean Rotating = false;
    Animation anim = null;
    int Bacground = Color.GRAY;

    public WentylatorView(Context context) {
        super(context);
    }

    public WentylatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WentylatorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void StartRotate(Activity activity) {
        if (!Rotating) {
            Rotating = true;
            if(anim == null)
            {
                anim = AnimationUtils.loadAnimation(activity, R.anim.rotating_pompa);
            }
            this.startAnimation(anim);
            Bacground = Color.argb(255,255,0,0);
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
        p.setColor(Color.BLACK);
        canvas.drawArc(new RectF(0, 0, wid, hei), 0, 360, true, p);

        p.setColor(Color.WHITE);
        canvas.drawArc(new RectF(2, 2, wid - 2, hei - 2), 0, 360, true, p);

        double rx = (wid / 2) * 0.7;
        double ry = (hei / 2) * 0.7;
        double kat = (Math.PI * 15) / 180;
        double x = Math.sin(kat) * rx;
        double y = Math.cos(kat) * ry;

        p.setColor(Bacground);

        canvas.drawCircle(wid / 2f, hei / 2f, wid * 0.05f, p);

        Path path = new Path();
        path.moveTo(wid / 2, (float) ( hei / 2));
        path.lineTo((float) (x + wid / 2), (float) (y + hei / 2));
        path.lineTo(wid / 2, (float) (hei / 2 + ry + 2));
        path.lineTo((float) (-x + wid / 2), (float) (y + hei / 2));
        canvas.drawPath(path, p);

        canvas.rotate(120,wid / 2, hei /2);
        canvas.drawPath(path, p);
        canvas.rotate(120,wid / 2, hei /2);
        canvas.drawPath(path, p);
    }
}
