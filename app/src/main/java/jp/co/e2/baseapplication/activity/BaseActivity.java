package jp.co.e2.baseapplication.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.debug.DebugHelper;

/**
 * 基底アクテビティ
 */
public class BaseActivity extends AppCompatActivity {
    /**
     * ${inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //例外をキャッチしてメールで送信する仕組みを仕込む
        DebugHelper.setUncaughtExHandler(getApplicationContext());
        DebugHelper.showReportDialog(getApplicationContext(), getFragmentManager());
    }

    /**
     * ツールバーをセット
     */
    protected void setToolbar() {
        //ツールバーをアクションバーとして扱う
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    /**
     * 戻る矢印付きのツールバーをセット
     */
    protected void setBackArrowToolbar() {
        setToolbar();

        ActionBar actionbar = getSupportActionBar();

        if (actionbar != null) {
            actionbar.setHomeButtonEnabled(true);
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
    }
}