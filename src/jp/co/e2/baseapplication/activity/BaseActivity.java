package jp.co.e2.baseapplication.activity;

import jp.co.e2.baseapplication.debug.DebugHelper;
import android.app.Activity;
import android.os.Bundle;

/**
 * 基底アクテビティ
 * 
 * @access public
 */
public abstract class BaseActivity extends Activity
{
    /**
     * onCreate
     * 
     * @param Bundle savedInstanceState
     * @return void
     * @access protected
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            //デバッグ
            DebugHelper.setUncaughtExHandler(getApplicationContext());
            DebugHelper.showReportDialog(getApplicationContext(), getFragmentManager());
        }
    }
}