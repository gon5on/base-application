package jp.co.e2.baseapplication.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * タップ時に色が少し濃くなるフィードバックがついているImageView
 *
 * @access public
 */
public class TouchFeedbackImageView extends ImageView implements OnTouchListener {
    /**
     * コンテキスト
     *
     * @param context
     * @access public
     */
    public TouchFeedbackImageView(Context context) {
        super(context);

        setOnTouchListener(this);
    }

    /**
     * コンテキスト
     *
     * @param context
     * @param attrs   指定した属性値
     * @access public
     */
    public TouchFeedbackImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        setOnTouchListener(this);
    }

    /**
     * コンテキスト
     *
     * @param context
     * @param attrs
     * @param defStyle
     * @access public
     */
    public TouchFeedbackImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setOnTouchListener(this);
    }

    /**
     * タッチイベント
     *
     * @param v
     * @param event
     * @return boolean
     * @access public
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                shadeOn(v);
                break;
            case MotionEvent.ACTION_CANCEL:
                shadeOff(v);
                break;
            case MotionEvent.ACTION_UP:
                shadeOff(v);
                break;
        }

        return false;
    }

    /**
     * 影を付ける
     *
     * @param v
     * @access void
     */
    public void shadeOn(View v) {
        ((ImageView) v).setColorFilter(new LightingColorFilter(Color.LTGRAY, 0));
    }

    /**
     * 影を外す
     *
     * @param v
     * @access void
     */
    public void shadeOff(View v) {
        ((ImageView) v).clearColorFilter();
    }
}