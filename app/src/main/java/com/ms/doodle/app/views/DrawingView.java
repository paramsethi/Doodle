package com.ms.doodle.app.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.ms.doodle.app.R;

/**
 * Created by prgirase on 4/23/14.
 */

public class DrawingView extends View
{
    // Keeps track of the path of the drawing.
    private Path drawPath;

    // Keeps track of the color of the brush and canvas.
    private Paint drawPaint, canvasPaint;

    // Set the default color.
    private int paintColor = 0xFF660000;

    // The canvas to draw on.
    private Canvas drawCanvas;

    // Bitmap of the canvas
    private Bitmap canvasBitmap;

    // Keeping track of the brush size.
    private float brushSize, lastBrushSize;

    public DrawingView(Context context, AttributeSet attrSet)
    {
        super(context, attrSet);
        setupDrawing();
    }

    private void setupDrawing()
    {
        /// <summary>
        /// This is used to setup the initial drawing/editing page.
        /// </summary>

        drawPath = new Path();
        drawPaint = new Paint();

        // Set the path to the default color.
        drawPaint.setColor(paintColor);

        // This is to smooth and round edges of the drawing.
        drawPaint.setAntiAlias(true);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);

        // Set the default brush size to medium.
        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setBrushSize(float newSize)
    {
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, getResources().getDisplayMetrics());
        brushSize = pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize)
    {
        lastBrushSize = lastSize;
    }

    public float getLastBrushSize()
    {
        return lastBrushSize;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void setColor(String newColor)
    {
        invalidate();

        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }
}