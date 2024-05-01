package vn.mn.quanlynhahang.viewmodel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Map;
import java.util.List;

public class PieChartView extends View {

    private Map<String, Integer> mData;
    private Paint mPaint;
    private int mWidth, mHeight;
    private List<Integer> mColorList;

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public void setColorList(List<Integer> colorList) {
        mColorList = colorList;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mData == null || mData.isEmpty() || mColorList == null || mColorList.isEmpty()) {
            return;
        }

        int size = Math.min(mWidth, mHeight);
        int radius = size / 2;
        int cx = mWidth / 2;
        int cy = mHeight / 2;

        float startAngle = 0;
        int colorIndex = 0;

        for (Map.Entry<String, Integer> entry : mData.entrySet()) {
            int value = entry.getValue();
            float sweepAngle = (float) value / getTotal() * 360;
            mPaint.setColor(mColorList.get(colorIndex % mColorList.size()));
            canvas.drawArc(cx - radius, cy - radius, cx + radius, cy + radius, startAngle, sweepAngle, true, mPaint);
            startAngle += sweepAngle;
            colorIndex++;
        }
    }

    public void setData(Map<String, Integer> data) {
        mData = data;
        invalidate();
    }

    private int getTotal() {
        int total = 0;
        for (int value : mData.values()) {
            total += value;
        }
        return total;
    }
}
