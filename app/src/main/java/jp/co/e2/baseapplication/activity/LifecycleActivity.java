package jp.co.e2.baseapplication.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.common.LogUtils;

/**
 * ライフサイクルアクテビティ
 *
 * ↓参考になる！！
 * http://www.atmarkit.co.jp/ait/articles/1604/04/news011.html
 * http://qiita.com/suzukihr/items/90a93e79dc67c585de75
 */
public class LifecycleActivity extends BaseActivity {
    /**
     * ${inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        LogUtils.d("Activity.onOptionsItemSelected()");

        int itemId = item.getItemId();

        if(itemId == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common);

        //ツールバーセット
        setBackArrowToolbar();

        if (savedInstanceState == null) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            getFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }

        LogUtils.d("Activity.onCreate()");
        LogUtils.d("In Activity.onCreate() " + ((savedInstanceState == null) ? "= null" : "!= null"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRestart() {
        super.onRestart();

        LogUtils.d("Activity.onRestart()");
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onStart() {
        super.onStart();

        LogUtils.d("Activity.onStart()");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        LogUtils.d("Activity.onRestoreInstanceState()");
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();

        LogUtils.d("Activity.onResume()");
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onPause() {
        super.onPause();

        LogUtils.d("Activity.onPause()");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //onRestoreInstanceStateが呼ばれるようにするために、ダミーの値を保存しておく
        outState.putInt("dummy", 1);

        LogUtils.d("Activity.onSaveInstanceState()");
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onStop() {
        super.onStop();

        LogUtils.d("Activity.onStop()");
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        LogUtils.d("Activity.onDestroy()");
    }

    /**
     * PlaceholderFragment
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * {@inheritDoc}
         */
        @Override
        public void onAttach(Context context) {
            super.onAttach(context);

            LogUtils.d("Fragment.Override()");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            LogUtils.d("Fragment.onCreate()");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            LogUtils.d("Fragment.onCreateView()");

            return inflater.inflate(R.layout.fragment_lifecycle, container, false);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            LogUtils.d("Fragment.onViewCreated()");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            LogUtils.d("Fragment.onActivityCreated()");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onStart() {
            super.onStart();

            LogUtils.d("Fragment.onStart()");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onResume() {
            super.onResume();

            LogUtils.d("Fragment.onResume()");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onViewStateRestored(Bundle savedInstanceState) {
            super.onViewStateRestored(savedInstanceState);

            LogUtils.d("Fragment.onViewStateRestored()");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPause() {
            super.onPause();

            LogUtils.d("Fragment.onPause()");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);

            //onRestoreInstanceStateが呼ばれるようにするために、ダミーの値を保存しておく
            outState.putInt("dummy2", 2);

            LogUtils.d("Fragment.onSaveInstanceState()");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onStop() {
            super.onStop();

            LogUtils.d("Fragment.onStop()");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onDestroyView() {
            super.onDestroyView();

            LogUtils.d("Fragment.onDestroyView()");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onDestroy() {
            super.onDestroy();

            LogUtils.d("Fragment.onDestroy()");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onDetach() {
            super.onDetach();

            LogUtils.d("Fragment.onDetach()");
        }
    }
}