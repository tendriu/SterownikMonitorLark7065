package com.example.app.Sterownik;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nayk on 09.12.13.
 */
public class TempSensorView extends View {

    public double Temperature = 0;
    public String SensorName = "NONE";

    public TempSensorView(Context context) {
        super(context);
    }

    public TempSensorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        String packageName = "http://schemas.android.com/apk/res/res-auto";
       // mUrl = attrs.getAttributeValue(packageName, "url"); - See more at: http://droid-blog.net/2012/04/24/how-to-add-attributes-to-your-custom-view/#sthash.AZAIhlLo.dpuf
    }

    public TempSensorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void SetTemperature(double value)
    {
        Temperature = value;
        invalidate();
    }

    public void SetName(String name)
    {
        SensorName = name;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(25);

        float Left = 0;
        float Right = 0;
        float width = getWidth();
        float height = getHeight();

        float tempWidth = p.measureText(String.valueOf(Temperature)) + 6;
        Right = tempWidth;

        p.setColor(Color.GRAY);

        canvas.drawRect(0,0,tempWidth,height,p);
        p.setColor(Color.WHITE);
        canvas.drawRect(1, 1, tempWidth - 1, height - 1, p);

        p.setColor(Color.BLACK);
        canvas.drawText(String.valueOf(Temperature),3,25,p);

        float znakWidth = p.measureText("°C") + 6;
        Left = Right-1;
        Right +=znakWidth;

        p.setColor(Color.GRAY);
        canvas.drawRect(Left,0,Right,height,p);
        p.setColor(Color.WHITE);
        canvas.drawRect(Left+1,1,Right-1,height-1, p);

        p.setColor(Color.BLACK);
        canvas.drawText("°C",Left+3,25,p);

        Left +=znakWidth;
        float nameWidth = p.measureText(SensorName) + 6;
        Right+=nameWidth;
        p.setColor(Color.BLACK);
        canvas.drawRect(Left,0,Right,height, p);

        p.setColor(Color.WHITE);
        canvas.drawText(SensorName,Left+3,25,p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);



    }

}
