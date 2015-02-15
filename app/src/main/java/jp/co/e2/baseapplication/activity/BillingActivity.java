package jp.co.e2.baseapplication.activity;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.billing.IabHelper;
import jp.co.e2.baseapplication.billing.IabResult;
import jp.co.e2.baseapplication.billing.Inventory;
import jp.co.e2.baseapplication.billing.Purchase;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.common.DateHelper;
import jp.co.e2.baseapplication.common.LogUtils;
import jp.co.e2.baseapplication.common.PreferenceUtils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 課金アクテビティ
 *
 * 前提として、アイテムを保持しているかどうかなどは基本的に専用サーバで保持しておく！
 * 複数回購入できるアイテムを買った時も、回数はサーバで持つ
 */
public class BillingActivity extends BaseActivity {
    // 課金のリクエストコード
    private static final Integer REQUEST_CODE_BILLING1 = 1005;
    private static final Integer REQUEST_CODE_BILLING2 = 1006;
    private static final Integer REQUEST_CODE_BILLING3 = 1007;

    // Developer Consoleで得られるBase64encodedな公開鍵
    private static final String BILLING_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApzSzy9SUCEYW8QgVNjSydnYqsLn8SQ7gxIHCmFD/PTPaMDRNu3YEwhSnUayPadnncQ8Ih5lkzA6WVkjdAkDEk5rPQSLvJkxtBPai8PwUwg4p6zMMkEIou3dzKqh3fOF7jNmJfeP3VlibVyu8gr7hduDVfmHSM5SxcNAJcBUg2NwSDWjE5Xof/IJccIgQigYQ64XauLVB3BqSfzVji23RxWSA47K/wbgn6VLZqVip1xpGWjShjOQDQQknsdMOKsyH8VZ+ZJifNYvaxwFf27vVavqYhVfCsiYqT+zSJ9va970CzhV8njRpN5+VhAZJUxtdfEUVnbcmO3+Fxa8s3nEENQIDAQAB";

    // 識別子文字列（自由に指定してOK）
    private static final String PAYLOAD = "my_payload";

    // プロダクトコード
    private static final String PRODUCT_CODE[] = {
            "jp.co.e2.baseapplication.item1",           // 管理対象アイテムのプロダクトID
            "jp.co.e2.baseapplication.item2",           // 管理対象外アイテムのプロダクトID
            "jp.co.e2.baseapplication.item3",           // 定期購入アイテムのプロダクトID
    };

    // プロダクト名
    private static final String PRODUCT_NAME[] = {"管理対象アイテム", "管理対象外アイテム", "定期購入アイテム",};

    /**
     * ファクトリーメソッドもどき
     *
     * @param activity アクテビティ
     * @return Intent intent
     */
    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, BillingActivity.class);

        return intent;
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, PlaceholderFragment.newInstance()).commit();
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);

        //fragmentのonActivityResultは自動で呼ばれないので、明示的に呼ぶ
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * PlaceholderFragment
     */
    public static class PlaceholderFragment extends Fragment {
        private View mView = null;
        private IabHelper mHelper;                          //課金ヘルパー
        private Purchase mItem1Purchase;                    //管理対象アイテム消費用に管理対象アイテムを保持しておく（デバッグ用）
        private String mLog = "";                           //メール送信ログ

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

            mView = inflater.inflate(R.layout.fragment_billing, container, false);

            // fragment再生成抑止、必要に応じて
            setRetainInstance(true);

            //イベントをセットする
            setEvent();

            // 課金準備
            setupBilling();

            return mView;
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public void onDestroy() {
            super.onDestroy();

            if (mHelper != null) {
                mHelper.dispose();
                mHelper = null;
            }
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            // オブジェクトが生成されていない
            if (mHelper == null) {
                return;
            }

            // 課金ヘルパーの方のハンドリングできなかったら、通常のonActivityResultを呼ぶ
            if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            }

            LogUtils.d("request code " + requestCode);
            LogUtils.d("result code " + resultCode);
        }

        /**
         * イベントをセットする
         */
        private void setEvent() {
            //管理対象アイテム購入
            Button buttonBilling1 = (Button) mView.findViewById(R.id.buttonBilling1);
            buttonBilling1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHelper.launchPurchaseFlow(getActivity(), PRODUCT_CODE[0], REQUEST_CODE_BILLING1, mPurchaseFinishedListener, PAYLOAD);
                }
            });

            //管理対象外アイテム購入
            Button buttonBilling2 = (Button) mView.findViewById(R.id.buttonBilling2);
            buttonBilling2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHelper.launchPurchaseFlow(getActivity(), PRODUCT_CODE[1], REQUEST_CODE_BILLING2, mPurchaseFinishedListener, PAYLOAD);
                }
            });

            //定期購入アイテム
            Button buttonBilling3 = (Button) mView.findViewById(R.id.buttonBilling3);
            buttonBilling3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHelper.subscriptionsSupported()) {
                        mHelper.launchSubscriptionPurchaseFlow(getActivity(), PRODUCT_CODE[2], REQUEST_CODE_BILLING3, mPurchaseFinishedListener, PAYLOAD);
                    }
                }
            });

            // 管理対象アイテム消費（普通は消費されない、デバッグ用）
            Button buttonUse1 = (Button) mView.findViewById(R.id.buttonUse1);
            buttonUse1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItem1Purchase != null) {
                        mHelper.consumeAsync(mItem1Purchase, mConsumeFinishedListener);
                    }
                }
            });

            // 管理対象外アイテム消費
            Button buttonUse2 = (Button) mView.findViewById(R.id.buttonUse2);
            buttonUse2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //保持アイテム数を変更する
                    Integer cnt = PreferenceUtils.get(getActivity(), "item2_cnt", 0) - 1;
                    cnt = (cnt < 0) ? 0 : cnt;
                    PreferenceUtils.save(getActivity(), "item2_cnt", cnt);

                    AndroidUtils.showToastS(getActivity(), "消費完了\n管理対象外アイテム保持数 " + cnt);
                }
            });

            //定期購入キャンセル
            Button buttonUse3 = (Button) mView.findViewById(R.id.buttonUse3);
            buttonUse3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //キャンセル画面（google play）へ
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=cjp.co.e2.baseapplication"));
                    startActivity(intent);
                }
            });

            // 課金状態確認ボタン
            Button buttonBillingStatus = (Button) mView.findViewById(R.id.buttonBillingStatus);
            buttonBillingStatus.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                }
            });

            // ログ送信ボタン（メーラー起動）
            Button buttonSend = (Button) mView.findViewById(R.id.buttonSend);
            buttonSend.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "課金ログ");
                    intent.putExtra(Intent.EXTRA_TEXT, mLog);
                    intent.setType("message/rfc822");
                    startActivity(intent);
                }
            });
        }

        /**
         * 課金準備
         */
        private void setupBilling() {
            // 課金ヘルパー作成
            mHelper = new IabHelper(getActivity(), BILLING_PUBLIC_KEY);

            // デバッグを有効にする場合(デフォルトは無効)
            mHelper.enableDebugLogging(true);

            // セットアップ
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    if (!result.isSuccess()) {
                        AndroidUtils.showToastL(getActivity(), "セットアップ失敗\n結果:" + result);
                        LogUtils.d("セットアップ失敗　" + result);
                        return;
                    }

                    // オブジェクトが生成されていない
                    if (mHelper == null) {
                        return;
                    }

                    // アイテム購入復元処理
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                }
            });
        }

        //課金状態確認
        IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                // オブジェクトが生成されていない
                if (mHelper == null) {
                    return;
                }

                // 購入情報照会失敗
                if (result.isFailure()) {
                    AndroidUtils.showToastL(getActivity(), "購入情報照会失敗\n結果:" + result);
                    LogUtils.d("購入情報照会失敗　" + result);
                    return;
                }

                //管理対象アイテムを消費可能なように管理対象アイテムを保持しておく（デバッグ用）
                Purchase item1 = inventory.getPurchase(PRODUCT_CODE[0]);
                if (item1 != null && verifyDeveloperPayload(item1)) {
                    mItem1Purchase = inventory.getPurchase(PRODUCT_CODE[0]);
                }

                //管理対象外アイテムは消費処理がされていなかったら、複数回購入可能なように消費処理を走らせておく
                Purchase item2 = inventory.getPurchase(PRODUCT_CODE[1]);
                if (item2 != null && verifyDeveloperPayload(item2)) {
                    mHelper.consumeAsync(inventory.getPurchase(PRODUCT_CODE[1]), mConsumeFinishedListener);
                }

                //ログ関連
                String msg = "";
                mLog += "●Purchase item status\n";
                mLog += new DateHelper().format(DateHelper.FMT_DATETIME) + "\n";

                for (int i = 0; i < PRODUCT_CODE.length; i++) {
                    Purchase item = inventory.getPurchase(PRODUCT_CODE[i]);

                    if (item != null && verifyDeveloperPayload(item)) {
                        LogUtils.d(PRODUCT_NAME[i] + " 購入済!!!");
                        msg += PRODUCT_NAME[i] + " 購入済!!!\n";
                        mLog += String.valueOf(inventory.getPurchase(PRODUCT_CODE[i])) + "\n";
                    } else {
                        LogUtils.d(PRODUCT_NAME[i] + " 未購入");
                        msg += PRODUCT_NAME[i] + " 未購入\n";
                        mLog += "No exist " + PRODUCT_CODE[i] + "\n";
                    }
                    mLog += "------------------------------------------\n";
                }
                mLog += "==========================================\n";
                AndroidUtils.showToastL(getActivity(), msg);
            }
        };

        //実際の購入
        IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
            public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                // オブジェクトが生成されていない
                if (mHelper == null)
                    return;

                // エラー時の処理、キャンセル時もここに
                if (result.isFailure()) {
                    AndroidUtils.showToastS(getActivity(), "購入失敗\n結果：" + result);
                    LogUtils.d("購入失敗　" + result);
                    return;
                }

                //管理対象外アイテム購入後の処理
                if (purchase.getSku().equals(PRODUCT_CODE[1])) {
                    //保持アイテム数を変更する
                    Integer cnt = PreferenceUtils.get(getActivity(), "item2_cnt", 0) + 1;
                    PreferenceUtils.save(getActivity(), "item2_cnt", cnt);

                    //複数回購入可能なように、消費処理を走らせる
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                }

                //ログ関連
                String msg = "";
                mLog += "●Item buy!\n";
                mLog += new DateHelper().format(DateHelper.FMT_DATETIME) + "\n";

                for (int i = 0; i < PRODUCT_CODE.length; i++) {
                    if (purchase.getSku().equals(PRODUCT_CODE[i])) {
                        LogUtils.d(PRODUCT_NAME[i] + " 購入完了!!!");
                        msg += PRODUCT_NAME[i] + " 購入完了!!!\n";
                        mLog += String.valueOf(purchase) + "\n";

                        if (purchase.getSku().equals(PRODUCT_CODE[1])) {
                            msg += PRODUCT_NAME[1] + "保持数 " + PreferenceUtils.get(getActivity(), "item2_cnt", 0) + "\n";
                        }
                    }
                }
                mLog += "==========================================\n";
                AndroidUtils.showToastL(getActivity(), msg);
            }
        };

        //アイテム消費
        //消費というか、次また購入できるようにするための処理、という方が正しい
        //アイテムを実際に使用したときに呼ぶ必要はない、使ったかどうかは自分のサーバで管理しましょうね、とのこと
        IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
            public void onConsumeFinished(Purchase purchase, IabResult result) {
                // オブジェクトが生成されていない
                if (mHelper == null) {
                    return;
                }

                // エラー時の処理
                if (result.isFailure()) {
                    AndroidUtils.showToastS(getActivity(), "消費失敗\n結果：" + result);
                    LogUtils.d("消費失敗　" + result);
                    return;
                }

                //ログ関連
                String msg = "";
                mLog += "●Item use!\n";
                mLog += new DateHelper().format(DateHelper.FMT_DATETIME) + "\n";

                for (int i = 0; i < PRODUCT_CODE.length; i++) {
                    if (purchase.getSku().equals(PRODUCT_CODE[i])) {
                        LogUtils.d(PRODUCT_NAME[i] + " 消費完了!!!");
                        msg += PRODUCT_NAME[i] + " 消費完了!!!\n";
                        mLog += String.valueOf(purchase) + "\n";
                    }
                }
                mLog += "==========================================\n";
                AndroidUtils.showToastL(getActivity(), msg);
            }
        };

        /**
         * 識別子確認
         *
         * @param p 購入情報
         * @return boolean
         */
        private boolean verifyDeveloperPayload(Purchase p) {
            //TODO なんか識別子判定処理書いたほうがいいっぽい
            //String payload = p.getDeveloperPayload();
            //・・・
            //・・・

            return true;
        }
    }
}