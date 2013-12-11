package com.example.app.Sterownik;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nayk on 11.12.13.
 */
public class EllipseView extends View {

    public EllipseView(Context context) {
        super(context);
    }

    public EllipseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EllipseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float wid = getWidth();
        float hei = getHeight();

        Paint p = new Paint();
        p.setColor(Color.RED);

        canvas.drawCircle(wid / 2,hei / 2 , wid / 2,p);
    }


}
