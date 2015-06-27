package jp.co.e2.baseapplication.activity;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.common.AndroidUtils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * カードビューアクテビティ
 */
public class CardViewActivity extends BaseActivity {
    /**
     * ファクトリーメソッドもどき
     *
     * @param activity アクテビティ
     * @return Intent intent
     */
    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, CardViewActivity.class);

        return intent;
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        if (savedInstanceState == null) {
            //ツールバーセット
            setToolbar();

            //ドロワーセット
            setDrawer(true);

            getFragmentManager().beginTransaction().add(R.id.container, PlaceholderFragment.newInstance()).commit();
        }
    }

    /**
     * PlaceholderFragment
     */
    public static class PlaceholderFragment extends Fragment {
        private View mView = null;

        /**
         * ファクトリーメソッド
         *
         * @return PlaceholderFragment fragment
         */
        public static PlaceholderFragment newInstance() {
            Bundle args = new Bundle();

            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(args);

            return fragment;
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mView = inflater.inflate(R.layout.fragment_card_view, container, false);

            // fragment再生成抑止、必要に応じて
            setRetainInstance(true);

            //カードビューをセットする
            setCardView();

            return mView;
        }

        /**
         * カードビューをセットする
         */
        private void setCardView() {
            LinearLayout cardLinear = (LinearLayout) mView.findViewById(R.id.linearLayout);
            cardLinear.removeAllViews();

            //便宜上、同じカードを並べる
            for(int i = 0; i < 5; i++) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.part_card_view_item, null);
                CardView cardView = (CardView) linearLayout.findViewById(R.id.cardView);
                TextView textBox = (TextView) linearLayout.findViewById(R.id.textView);
                textBox.setText("CardView" + i + "\nCardView" + i + "\nCardView" + i );
                cardView.setTag(i);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AndroidUtils.showToastS(getActivity(), v.getTag() + "番目のCardViewがクリックされました");
                    }
                });

                cardLinear.addView(linearLayout,i);
            }
        }
    }
}