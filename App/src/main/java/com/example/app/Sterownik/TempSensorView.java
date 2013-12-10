package com.example.app.Sterownik;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;

/**
 * Created by nayk on 09.12.13.
 */
public class TempSensorView extends View {

    public double Temperature = 0;
    public String SensorName = "NONE";
    public int FontSize = 20;

    public TempSensorView(Context context) {
        super(context);
    }

    public TempSensorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(isInEditMode())
        {
            String name = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.app.Sterownik","Name");
            SensorName = name;
        }else
        {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TempSensorView, 0, 0);
            String name = ta.getString(R.styleable.TempSensorView_Name);
            ta.recycle();
            SensorName = name;
        }
    }

    public TempSensorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void SetTemperature(double value)
    {
        Temperature = value;
        requestLayout();
        invalidate();
    }

    public void SetName(String name)
    {
        SensorName = name;
        requestLayout();
        invalidate();
    }
    String tempToString()
    {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return  decimalFormat.format(Temperature);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(FontSize);

        float Left = 0;
        float Right = 0;
        float width = getWidth();
        float height = getHeight();

        float tempWidth = p.measureText(tempToString()) + 6;
        Right = tempWidth;

        p.setColor(Color.GRAY);

        canvas.drawRect(0,0,tempWidth,height,p);
        p.setColor(Color.WHITE);
        canvas.drawRect(1, 1, tempWidth - 1, height - 1, p);

        p.setColor(Color.BLACK);
        canvas.drawText(tempToString(),3,FontSize,p);

        float znakWidth = p.measureText("°C") + 6;
        Left = Right-1;
        Right +=znakWidth;

        p.setColor(Color.GRAY);
        canvas.drawRect(Left,0,Right,height,p);
        p.setColor(Color.WHITE);
        canvas.drawRect(Left+1,1,Right-1,height-1, p);

        p.setColor(Color.BLACK);
        canvas.drawText("°C",Left+3,FontSize,p);

        Left +=znakWidth;
        float nameWidth = p.measureText(SensorName) + 6;
        Right+=nameWidth;
        p.setColor(Color.BLACK);
        canvas.drawRect(Left,0,Right,height, p);

        p.setColor(Color.WHITE);
        canvas.drawText(SensorName,Left+3,FontSize,p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(FontSize);

        float width = p.measureText(tempToString()) + 6 +p.measureText("°C") + 6 + p.measureText(SensorName) + 6;
        setMeasuredDimension((int)width,FontSize +6);

    }

}
