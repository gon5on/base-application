package jp.co.e2.baseapplication.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.co.e2.baseapplication.R;

/**
 * サブアクテビティ
 *
 * 前のアクテビティから値を引き渡すサンプル
 */
public class SubActivity extends BaseActivity {
    private static final String PARAM1 = "param1";
    private static final String PARAM2 = "param2";

    /**
     * ファクトリーメソッドもどき
     *
     * @param activity アクテビティ
     * @param param1 パラメータ1
     * @param param2 パラメータ2
     * @return intent インテント
     */
    public static Intent newInstance(Activity activity, String param1, Integer param2) {
        Intent intent = new Intent(activity, SubActivity.class);
        intent.putExtra(PARAM1, param1);
        intent.putExtra(PARAM2, param2);

        return intent;
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common_no_navigation);

        //ツールバーセット
        setBackArrowToolbar();

        if (savedInstanceState == null) {
            String param1 = getIntent().getStringExtra(PARAM1);
            Integer param2 = getIntent().getIntExtra(PARAM2, 0);
            PlaceholderFragment fragment = PlaceholderFragment.newInstance(param1, param2);

            getFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();

        if(itemId == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * PlaceholderFragment
     */
    public static class PlaceholderFragment extends Fragment {
        private View mView;

        /**
         * ファクトリーメソッド
         *
         * @param param1 パラメータ1
         * @param param2 パラメータ2
         * @return fragment フラグメント
         */
        public static PlaceholderFragment newInstance(String param1, Integer param2) {
            Bundle args = new Bundle();
            args.putString(PARAM1, param1);
            args.putInt(PARAM2, param2);

            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(args);

            return fragment;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mView = inflater.inflate(R.layout.fragment_sub, container, false);

            String param1 = getArguments().getString(PARAM1);
            Integer param2 = getArguments().getInt(PARAM2);

            TextView textViewParam1 = (TextView) mView.findViewById(R.id.textViewParam1);
            textViewParam1.setText(param1);

            TextView textViewParam2 = (TextView) mView.findViewById(R.id.textViewParam2);
            textViewParam2.setText(String.valueOf(param2));

            return mView;
        }
    }
}