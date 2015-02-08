package jp.co.e2.baseapplication.activity;

import jp.co.e2.baseapplication.debug.DebugHelper;

import android.app.Activity;
import android.os.Bundle;

/**
 * 基底アクテビティ
 *
 * @access public
 */
public abstract class BaseActivity extends Activity {
    /**
     * ${inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            //デバッグを仕込む
            DebugHelper.setUncaughtExHandler(getApplicationContext());
            DebugHelper.showReportDialog(getApplicationContext(), getFragmentManager());
        }
    }
}