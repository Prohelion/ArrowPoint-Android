package au.com.teamarrow.arrowpoint.utils;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.content.res.Resources;

import com.example.arrowpoint.R;

/**
 * Created by bruce on 11/6/14.
 */
public class SetPointProgress extends View {
    private Paint paint;
    protected Paint textPaint;

    private RectF rectF = new RectF();

    private float strokeWidth;
    private float bottomTextSize;
    private String bottomText;
    private int progress = 0;
    private int max;
    private int color1;
    private int color2;
    private int finishedStrokeColor;
    private int unfinishedStrokeColor;
    private float arcAngle;
    private float arcBottomHeight;

    private final int default_finished_color = Color.WHITE;
    private final int default_unfinished_color = Color.rgb(72, 106, 176);
    private final float default_bottom_text_size;
    private final float default_stroke_width;
    private final int default_max = 100;
    private final float default_arc_angle = 360 * 0.5f;

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_STROKE_WIDTH = "stroke_width";
    private static final String INSTANCE_BOTTOM_TEXT_SIZE = "bottom_text_size";
    private static final String INSTANCE_BOTTOM_TEXT = "bottom_text";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color";
    private static final String INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";
    private static final String INSTANCE_ARC_ANGLE = "arc_angle";

    public SetPointProgress(Context context) {
        this(context, null);
    }

    public SetPointProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SetPointProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_bottom_text_size = sp2px(getResources(), 10);
        default_stroke_width = dp2px(getResources(), 4);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();

        initPainters();
    }

    protected void initByAttributes(TypedArray attributes) {
        color1 = attributes.getColor(R.styleable.ArcProgress_arc_color1, default_finished_color);
        color2 = attributes.getColor(R.styleable.ArcProgress_arc_color2, default_finished_color);
        finishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_finished_color, default_finished_color);
        unfinishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_unfinished_color, default_unfinished_color);
        arcAngle = attributes.getFloat(R.styleable.ArcProgress_arc_angle, default_arc_angle);
        setMax(attributes.getInt(R.styleable.ArcProgress_arc_max, default_max));
        setProgress(attributes.getInt(R.styleable.ArcProgress_arc_progress, 0));
        strokeWidth = attributes.getDimension(R.styleable.ArcProgress_arc_stroke_width, default_stroke_width);
        bottomTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_bottom_text_size, default_bottom_text_size);
        bottomText = attributes.getString(R.styleable.ArcProgress_arc_bottom_text);
    }

    protected void initPainters() {
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);

        paint = new Paint();
        paint.setColor(default_unfinished_color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.BUTT);
    }


    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //rectF.set(strokeWidth / 2f, strokeWidth / 2f, width - strokeWidth / 2f, MeasureSpec.getSize(heightMeasureSpec) - strokeWidth / 2f);
        float radius = MeasureSpec.getSize(heightMeasureSpec) - (strokeWidth);
        rectF.set(width/2f - radius, strokeWidth / 2f,  width/2f + radius, (2f*radius));
        float angle = (360 - arcAngle) / 2f;
        arcBottomHeight = bottomTextSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startAngle = 270 - arcAngle / 2f;
        float finishedSweepAngle = progress / (float) getMax() * arcAngle;

        paint.setColor(unfinishedStrokeColor);
        canvas.drawArc(rectF, startAngle, arcAngle, false, paint);
        paint.setColor(finishedStrokeColor);
        canvas.drawArc(rectF, startAngle, finishedSweepAngle, false, paint);

        String text = String.valueOf(getProgress());

        if (!TextUtils.isEmpty(getBottomText())) {
            textPaint.setTextSize(bottomTextSize);
            float bottomTextBaseline = getHeight() - arcBottomHeight - (textPaint.descent() + textPaint.ascent()) / 2;
            canvas.drawText(getBottomText(), (getWidth() - textPaint.measureText(getBottomText())) / 2.0f, bottomTextBaseline, textPaint);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(INSTANCE_STROKE_WIDTH, getStrokeWidth());
        bundle.putFloat(INSTANCE_BOTTOM_TEXT_SIZE, getBottomTextSize());
        bundle.putString(INSTANCE_BOTTOM_TEXT, getBottomText());
        bundle.putInt(INSTANCE_PROGRESS, getProgress());
        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor());
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedStrokeColor());
        bundle.putFloat(INSTANCE_ARC_ANGLE, getArcAngle());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            strokeWidth = bundle.getFloat(INSTANCE_STROKE_WIDTH);
            bottomTextSize = bundle.getFloat(INSTANCE_BOTTOM_TEXT_SIZE);
            bottomText = bundle.getString(INSTANCE_BOTTOM_TEXT);
            setMax(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getInt(INSTANCE_PROGRESS));
            finishedStrokeColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR);
            unfinishedStrokeColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR);
            initPainters();
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public String getBottomText() {
        return bottomText;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (this.progress > getMax()) {
            this.progress %= getMax();
        }
        finishedStrokeColor = color1;
        invalidate();
    }

    public void setProgress(int progress, boolean secondaryColor) {
        this.progress = progress;
        if (this.progress > getMax()) {
            this.progress %= getMax();
        }
        if (secondaryColor){
            finishedStrokeColor = color2;
        }else{
            finishedStrokeColor = color1;
        }
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }

    public float getBottomTextSize() {
        return bottomTextSize;
    }

    public int getFinishedStrokeColor() {
        return finishedStrokeColor;
    }


    public int getUnfinishedStrokeColor() {
        return unfinishedStrokeColor;
    }


    public float getArcAngle() {
        return arcAngle;
    }


}

