package jp.co.e2.baseapplication.view;

import jp.co.e2.baseapplication.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 小数点第何位まで入力可能なEditText
 *
 * 小数点以下の桁数を下記のようにxmlで指定する
 * attrs.xmlもres/valuesに入れておくことが必要
 * ---------------------------------------------------------------------
 * xmlns:custom="http://schemas.android.com/apk/res/XXX.XXX.XXXXXXXXXX"
 *
 * custom:decimalPointLength="1"
 * ---------------------------------------------------------------------
 */
public class DecimalPointEditText extends EditText {
    private Integer mDecimalPointLength = 0;

    /**
     * コンテキスト
     *
     * @param context コンテキスト
     */
    public DecimalPointEditText(Context context) {
        super(context);

        init(context, null);
    }

    /**
     * コンテキスト
     *
     * @param context コンテキスト
     * @param attrs 属性
     */
    public DecimalPointEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    /**
     * コンテキスト
     *
     * @param context コンテキスト
     * @param attrs 属性
     * @param defStyle スタイル
     */
    public DecimalPointEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //属性から小数点以下の桁数を取得
        if (attrs != null) {
            mDecimalPointLength = attrs.getAttributeIntValue(null, "decimalPointLength", 0);
        }

        init(context, attrs);
    }

    /**
     * 入力欄初期設定
     *
     * @param context コンテキスト
     * @param attrs 属性
     */
    private void init(Context context, AttributeSet attrs) {
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        //属性から小数点以下の桁数を取得
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DecimalPointEditText);
            mDecimalPointLength = array.getInt(R.styleable.DecimalPointEditText_decimalPointLength, 0);
        }

        InputFilter[] filters = {new DecimalPointLengthFilter()};
        setFilters(filters);
    }

    /**
     * DecimalPointLengthFilter
     */
    private class DecimalPointLengthFilter implements InputFilter {
        /**
         * ${inheritDoc}
         */
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            //小数点桁数の指定が無ければ、何もしない
            if (mDecimalPointLength == 0) {
                return source;
            }

            //ドットで区切る
            String[] array = (String.valueOf(dest) + source).split("\\.");

            //未入力 or ドット未入力であればリターン
            if (array.length < 2) {
                return source;
            }

            //入力文字がドット以前であればリターン
            if (array[0].length() >= dstart) {
                return source;
            }

            //指定の小数点桁数を超えてないかどうか
            if (array[1].length() > mDecimalPointLength) {
                return "";
            }

            return source;
        }
    }
}
