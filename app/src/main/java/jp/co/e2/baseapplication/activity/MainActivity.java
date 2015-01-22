package jp.co.e2.baseapplication.activity;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.dialog.SampleDialog;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * メインアクテビティ
 *
 * @access public
 */
public class MainActivity extends BaseActivity implements SampleDialog.CallbackListener {
    private static final int DIALOG_TAG = 1;

    /**
     * onCreate
     *
     * @param savedInstanceState
     * @return void
     * @access protected
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new MainFragment()).commit();
        }
    }

    /**
     * onCreateOptionsMenu
     *
     * @param menu
     * @return Boolean
     * @access public
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * onOptionsItemSelected
     *
     * @param item
     * @return Boolean
     * @access public
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Integer id = item.getItemId();

        if (id == R.id.action_content1) {
            SampleDialog sampleDialog = SampleDialog.getInstance(DIALOG_TAG, "サンプル", "サンプルダイアログです");
            sampleDialog.setCallbackListener(this);
            sampleDialog.show(getFragmentManager(), "dialog");
            return true;
        } else if (id == R.id.action_content2) {
            Intent intent = new Intent(MainActivity.this, AsyncTaskActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_content3) {
            Intent intent = new Intent(MainActivity.this, BillingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * サンプルダイアログでOKボタンが押された
     *
     * @return void
     * @access public
     */
    @Override
    public void onClickSampleDialogOk(int tag) {
        AndroidUtils.showToastS(getApplicationContext(), "OK!");
    }

    /**
     * サンプルダイアログでキャンセルボタンが押された
     *
     * @return void
     * @access public
     */
    @Override
    public void onClickSampleDialogCancel(int tag) {
        AndroidUtils.showToastS(getApplicationContext(), "cancel!");
    }

    /**
     * MainFragment
     *
     * @access public
     */
    public static class MainFragment extends Fragment {
        /**
         * コンストラクタ
         *
         * @access public
         */
        public MainFragment() {
        }

        /**
         * onCreateView
         *
         * @param inflater
         * @param container
         * @param savedInstanceState
         * @return View
         * @access public
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            View view = inflater.inflate(R.layout.fragment_main, container, false);

            // fragment再生成抑止、必要に応じて
            setRetainInstance(true);

            return view;
        }
    }
}