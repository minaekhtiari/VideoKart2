package com.hillavas.filmvazhe.screen.control;

/**
 * Created by arashjahani on 5/2/2017 AD.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.hillavas.filmvazhe.R;


public class CornerLableView extends View {



    Paint mTextPaint;
    int mTextColor ;
    float mTextSize ;
    float mTextHeight;
    float mTextWidth;
    int mTextStyle;

    Paint mNumPaint;
    int mNumColor ;
    float mNumSize;
    float mNumHeight;
    float mNumWidth;
    int mNumStyle;

    float mTopPadding;
    float mBottomPadding;
    float mCenterPadding;


    Paint mTrianglePaint;
    int mBackGroundColor;

    float mDegrees;

    String mText = "Top";
    String mNum = "01";

    int width;
    int height;

    public static final int DEGREES_LEFT=-45;
    public static final int DEGREES_RIGHT=45;




    public CornerLableView(Context context) {
        this(context, null);
    }

    public CornerLableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerLableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelTextView);

        mTopPadding = ta.getDimension(R.styleable.LabelTextView_labelTopPadding, dp2px(7));
        mCenterPadding = ta.getDimension(R.styleable.LabelTextView_labelCenterPadding, dp2px(3));
        mBottomPadding = ta.getDimension(R.styleable.LabelTextView_labelBottomPadding, dp2px(3));

        mBackGroundColor=ta.getColor(R.styleable.LabelTextView_backgroundColor, Color.parseColor("#66000000"));
        mTextColor=ta.getColor(R.styleable.LabelTextView_textColor, Color.WHITE);
        mNumColor=ta.getColor(R.styleable.LabelTextView_numColor, Color.WHITE);

        mTextSize=ta.getDimension(R.styleable.LabelTextView_textSize,sp2px(8));
        mNumSize=ta.getDimension(R.styleable.LabelTextView_numSize,sp2px(11));

        mText=ta.getString(R.styleable.LabelTextView_text);
        mNum=ta.getString(R.styleable.LabelTextView_num);

        mTextStyle=ta.getInt(R.styleable.LabelTextView_textStyle,0);
        mNumStyle=ta.getInt(R.styleable.LabelTextView_numStyle,2);

        mDegrees = ta.getInt(R.styleable.LabelTextView_direction, 45);

        ta.recycle();

        initTextPaint();
        initNumPaint();
        initTrianglePaint();

        resetTextStatus();
        resetNumStatus();

    }


    public void setNum(String num) {
        mNum = num;
        resetNumStatus();
        invalidate();
    }

    public void setText(String text){
        mText=text;
        resetTextStatus();
        invalidate();
    }

    public void setBackGroundColor(int color){
        mTrianglePaint.setColor(color);
        invalidate();
    }

    private void initTextPaint() {
        //????????????????????????????????????
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
        if (mTextStyle==1){
            mTextPaint.setTypeface(Typeface.SANS_SERIF);
        }else if (mTextStyle==2){
            mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    private void initNumPaint() {
        //????????????????????????????????????
        mNumPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNumPaint.setColor(mNumColor);
        mNumPaint.setTextAlign(Paint.Align.CENTER);
        mNumPaint.setTextSize(mNumSize);
        if (mNumStyle==1){
            mNumPaint.setTypeface(Typeface.SANS_SERIF);
        }else if (mNumStyle==2){
            mNumPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    private void initTrianglePaint(){
        //???????????????????????????????????????
        mTrianglePaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrianglePaint.setColor(mBackGroundColor);
    }

    private void resetTextStatus() {
        // ??????????????????
        Rect rectText = new Rect();
        mTextPaint.getTextBounds(mText, 0, mText.length(), rectText);
        mTextWidth = rectText.width();
        mTextHeight = rectText.height();
    }

    private void resetNumStatus() {
        // ??????????????????
        Rect rectNum = new Rect();
        mNumPaint.getTextBounds(mNum, 0, mNum.length(), rectNum);
        mNumWidth = rectNum.width();
        mNumHeight = rectNum.height();
    }




    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        canvas.save();

        //???????????????canvas
        canvas.translate(0, (float) ((height * Math.sqrt(2)) - height));
        if (mDegrees==DEGREES_LEFT){
            canvas.rotate(mDegrees, 0, height);
        }else if (mDegrees==DEGREES_RIGHT){
            canvas.rotate(mDegrees, width, height);
        }

        //?????????????????????
        Path path = new Path();
        path.moveTo(0, height);
        path.lineTo(width / 2, 0);
        path.lineTo(width, height);
        path.close();
        canvas.drawPath(path, mTrianglePaint);
        //??????????????????
        canvas.drawText(mText, (width) / 2, mTopPadding + mTextHeight, mTextPaint);
        //??????????????????
        canvas.drawText(mNum, (width) / 2, (mTopPadding + mTextHeight + mCenterPadding + mNumHeight), mNumPaint);

        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = (int) (mTopPadding + mCenterPadding + mBottomPadding + mTextHeight + mNumHeight);
        width = 2 * height;
        //????????????????????????????????????...
        int realHeight= (int) (height * Math.sqrt(2));
        setMeasuredDimension(width,realHeight);
    }

    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public float sp2px(float spValue) {
        final float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return spValue * scale;
    }
}
