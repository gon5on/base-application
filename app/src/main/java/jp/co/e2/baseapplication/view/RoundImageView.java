package jp.co.e2.baseapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import jp.co.e2.baseapplication.R;

/**
 * 円形に表示するimageView
 *
 * 円形の元となる、shape_circle.xmlも必要になる
 *
 * @access public
 */
public class RoundImageView extends TouchFeedbackImageView implements View.OnTouchListener {
    Paint mMaskedPaint;
    Paint mCopyPaint;
    Drawable mMaskDrawable;
    Rect mBounds;
    RectF mBoundsF;

    /**
     * コンテキスト
     *
     * @param context
     * @access public
     */
    public RoundImageView(Context context) {
        this(context, null);

        setOnTouchListener(this);
    }

    /**
     * コンテキスト
     *
     * @param context
     * @param attrs 指定した属性値
     * @access public
     */
    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mMaskedPaint = new Paint();
        mMaskedPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        mCopyPaint = new Paint();
        mMaskDrawable = getResources().getDrawable(R.drawable.shape_circle);
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onDraw(Canvas canvas) {
        int sc = canvas.saveLayer(mBoundsF, mCopyPaint, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG);

        mMaskDrawable.setBounds(mBounds);
        mMaskDrawable.draw(canvas);

        canvas.saveLayer(mBoundsF, mMaskedPaint, 0);

        super.onDraw(canvas);

        canvas.restoreToCount(sc);
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mBounds = new Rect(0, 0, w, h);
        mBoundsF = new RectF(mBounds);
    }
}