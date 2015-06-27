package jp.co.e2.baseapplication.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.fragment.NavigationDrawerFragment;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.debug.DebugHelper;
import jp.co.e2.baseapplication.dialog.SampleDialog;

/**
 * 基底アクテビティ
 */
public class BaseActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, SampleDialog.CallbackListener {
    private static final int DIALOG_TAG = 1;

    private Toolbar mToolbar;

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

    /**
     * ${inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ツールバーセットセット
     */
    protected void setToolbar() {
        //ツールバーをアクションバーとして扱う
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    /**
     * ドロワーセット
     *
     * @param backFlg インジケータに戻るボタンを表示するフラグ
     */
    protected void setDrawer(boolean backFlg) {
        NavigationDrawerFragment navigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar, backFlg);

        //戻るボタン表示
        if (backFlg) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position){
            case 0:
                SampleDialog sampleDialog = SampleDialog.getInstance(DIALOG_TAG, "サンプル", "サンプルダイアログです");
                sampleDialog.setCallbackListener(this);
                sampleDialog.show(getFragmentManager(), "dialog");
                break;
            case 1:
                startActivity(AsyncTaskActivity.newIntent(this));
                break;
            case 2:
                startActivity(BillingActivity.newIntent(this));
                break;
            case 3:
                startActivity(CardViewActivity.newIntent(this));
                break;
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void onClickSampleDialogOk(int tag) {
        AndroidUtils.showToastS(getApplicationContext(), "OK!");
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void onClickSampleDialogCancel(int tag) {
        AndroidUtils.showToastS(getApplicationContext(), "cancel!");
    }
}