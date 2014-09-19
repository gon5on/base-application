package jp.co.e2.baseapplication.activity;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.billing.IabHelper;
import jp.co.e2.baseapplication.billing.IabResult;
import jp.co.e2.baseapplication.billing.Inventory;
import jp.co.e2.baseapplication.billing.Purchase;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.common.LogUtils;
import jp.co.e2.baseapplication.common.PrefarenceUtils;
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
 * 
 * @access public
 */
public class BillingActivity extends BaseActivity
{
    // 課金のリクエストコード
    private static final Integer REQUEST_CODE_BILLING1 = 1005;
    private static final Integer REQUEST_CODE_BILLING2 = 1006;
    private static final Integer REQUEST_CODE_BILLING3 = 1007;

    // Developer Consoleで得られるBase64encodedな公開鍵
    private static final String BILLING_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApzSzy9SUCEYW8QgVNjSydnYqsLn8SQ7gxIHCmFD/PTPaMDRNu3YEwhSnUayPadnncQ8Ih5lkzA6WVkjdAkDEk5rPQSLvJkxtBPai8PwUwg4p6zMMkEIou3dzKqh3fOF7jNmJfeP3VlibVyu8gr7hduDVfmHSM5SxcNAJcBUg2NwSDWjE5Xof/IJccIgQigYQ64XauLVB3BqSfzVji23RxWSA47K/wbgn6VLZqVip1xpGWjShjOQDQQknsdMOKsyH8VZ+ZJifNYvaxwFf27vVavqYhVfCsiYqT+zSJ9va970CzhV8njRpN5+VhAZJUxtdfEUVnbcmO3+Fxa8s3nEENQIDAQAB";

    // 識別子文字列（自由に指定してOK）
    private static final String PAYLOAD = "my_payload";

    // サンプルプロダクトコード
    private static final String PRODUCT_CODE[] = {
            "jp.co.e2.baseapplication.item1",           // 管理対象アイテムのプロダクトID
            "jp.co.e2.baseapplication.item2",           // 管理対象外アイテムのプロダクトID
            "jp.co.e2.baseapplication.item3",           // 定期購入アイテムのプロダクトID
    };

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
        setContentView(R.layout.activity_billing);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new BillingFragment()).commit();
        }
    }

    /**
     * onCreateView
     * 
     * @param LayoutInflater inflater
     * @param ViewGroup container
     * @param Bundle savedInstanceState
     * @access public
     * @return View
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);

        //fragmentのonActivityResultは自動で呼ばれないので、明示的に呼ぶ
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * AsyncTaskFragment
     * 
     * @access public
     */
    public static class BillingFragment extends Fragment
    {
        private View mView = null;

        private IabHelper mHelper;                          //課金ヘルパー
        private Purchase mItem1Purchase;                    //管理型アイテム消費用に管理型アイテムを保持しておく（デバッグ用）

        /**
         * コンストラクタ
         * 
         * @access public
         */
        public BillingFragment()
        {
        }

        /**
         * onCreateView
         * 
         * @param LayoutInflater inflater
         * @param ViewGroup container
         * @param Bundle savedInstanceState
         * @access public
         * @return View
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
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
         * onDestroy
         * 
         * @return void
         * @access public
         */
        @Override
        public void onDestroy()
        {
            super.onDestroy();

            if (mHelper != null) {
                mHelper.dispose();
                mHelper = null;
            }
        }

        /**
         * onActivityResult
         * 
         * @param int requestCode
         * @param int resultCode
         * @param Intent data
         * @return void
         * @access protected
         */
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            // オブジェクトが生成されていない
            if (mHelper == null) {
                return;
            }

            // 課金ヘルパーの方のハンドリングできなかったら、通常のonActivityResultを呼ぶ
            if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            }

            LogUtils.d(requestCode);
            LogUtils.d(resultCode);

            /*// リクエストコードを判別
            if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SAMPLE_BILLING1) {
                Integer responseCode = data.getIntExtra("RESPONSE_CODE", 0);

                LogUtils.d("onActivityResult responseCode:" + responseCode);

                // 注文情報を取得する
                String purchase_data = data.getStringExtra("INAPP_PURCHASE_DATA");

                try {
                    // JSONオブジェクトへ変換する
                    JSONObject object = new JSONObject(purchase_data);

                    // 注文情報をログに出力する
                    LogUtils.d("result", "orderId = " + object.getString("orderId"));
                    LogUtils.d("result", "packageName = " + object.getString("packageName"));
                    LogUtils.d("result", "productId = " + object.getString("productId"));
                    LogUtils.d("result", "purchaseTime = " + object.getString("purchaseTime"));
                    LogUtils.d("result", "purchaseState = " + object.getString("purchaseState"));
                    LogUtils.d("result", "developerPayload = " + object.getString("developerPayload"));
                    LogUtils.d("result", "purchaseToken = " + object.getString("purchaseToken"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }*/
        }

        /**
         * イベントをセットする
         * 
         * @return void
         * @access private
         */
        private void setEvent()
        {
            //管理型アイテム購入
            Button buttonBilling1 = (Button) mView.findViewById(R.id.buttonBilling1);
            buttonBilling1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHelper.launchPurchaseFlow(getActivity(), PRODUCT_CODE[0], REQUEST_CODE_BILLING1, mPurchaseFinishedListener, PAYLOAD);
                }
            });

            //非管理型アイテム購入
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

            // 管理型アイテム消費（普通は消費されない、デバッグ用）
            Button buttonUse1 = (Button) mView.findViewById(R.id.buttonUse1);
            buttonUse1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItem1Purchase != null) {
                        mHelper.consumeAsync(mItem1Purchase, mConsumeFinishedListener);
                        AndroidUtils.showToastS(getActivity(), "消費が完了しました");
                    }
                }
            });

            // 非管理型アイテム消費
            Button buttonUse2 = (Button) mView.findViewById(R.id.buttonUse2);
            buttonUse2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //保持アイテム数を変更する
                    Integer cnt = PrefarenceUtils.get(getActivity(), "item2_cnt", 0) - 1;
                    cnt = (cnt < 0) ? 0 : cnt;
                    PrefarenceUtils.save(getActivity(), "item2_cnt", cnt);

                    AndroidUtils.showToastS(getActivity(), "消費が完了しました\n非管理型アイテム保持数:" + cnt);
                }
            });

            //定期購入キャンセル
            Button buttonUse3 = (Button) mView.findViewById(R.id.buttonUse3);
            buttonUse3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
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
        }

        /**
         * 課金準備
         * 
         * @return void
         * @access private
         */
        private void setupBilling()
        {
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
            public void onQueryInventoryFinished(IabResult result, Inventory inventory)
            {
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

                String msg = "";

                //管理型アイテムの確認
                Purchase item1 = inventory.getPurchase(PRODUCT_CODE[0]);
                if (item1 != null && verifyDeveloperPayload(item1)) {
                    LogUtils.d("管理型アイテム(" + PRODUCT_CODE[0] + ") 購入済!!!");
                    msg += "管理型アイテム　 購入済!!!\n";

                    mItem1Purchase = inventory.getPurchase(PRODUCT_CODE[0]);    //管理型アイテム消費用に管理型アイテムを保持しておく（デバッグ用）
                } else {
                    LogUtils.d("管理型アイテム(" + PRODUCT_CODE[0] + ") 未購入");
                    msg += "管理型アイテム　未購入\n";
                }

                //非管理型アイテムの確認
                Purchase item2 = inventory.getPurchase(PRODUCT_CODE[1]);
                if (item2 != null && verifyDeveloperPayload(item1)) {
                    LogUtils.d("非管理型アイテム(" + PRODUCT_CODE[1] + ") 購入済!!!");
                    msg += "非管理型アイテム 購入済!!!\n";

                    //複数回購入可能なように、消費処理を走らせておく
                    mHelper.consumeAsync(inventory.getPurchase(PRODUCT_CODE[1]), mConsumeFinishedListener);
                } else {
                    LogUtils.d("非管理型アイテム(" + PRODUCT_CODE[1] + ") 未購入");
                    msg += "非管理型アイテム　未購入\n";
                }

                //定期購入アイテムの確認
                Purchase item3 = inventory.getPurchase(PRODUCT_CODE[2]);
                if (item3 != null && verifyDeveloperPayload(item3)) {
                    LogUtils.d("定期購入(" + PRODUCT_CODE[2] + ") 購入済!!!");
                    msg += "定期購入　購入済!!!";
                } else {
                    LogUtils.d("定期購入(" + PRODUCT_CODE[2] + ") 未購入");
                    msg += "定期購入　未購入";
                }

                AndroidUtils.showToastL(getActivity(), msg);
            }
        };

        //実際の購入
        IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
            public void onIabPurchaseFinished(IabResult result, Purchase purchase)
            {
                // オブジェクトが生成されていない
                if (mHelper == null)
                    return;

                // エラー時の処理、キャンセル時もここに
                if (result.isFailure()) {
                    AndroidUtils.showToastS(getActivity(), "購入失敗\n結果：" + result);
                    LogUtils.d("購入失敗　" + result);
                    return;
                }

                //管理型アイテム購入後の処理
                if (purchase.getSku().equals(PRODUCT_CODE[0])) {
                    LogUtils.d("管理型アイテム(" + PRODUCT_CODE[0] + ") 購入完了!!!");
                    AndroidUtils.showToastL(getActivity(), "購入が完了しました\nプロダクトID:" + purchase.getSku());
                }
                //非管理型アイテム購入後の処理
                else if (purchase.getSku().equals(PRODUCT_CODE[1])) {
                    LogUtils.d("非管理型アイテム(" + PRODUCT_CODE[1] + ") 購入完了!!!");

                    //保持アイテム数を変更する
                    Integer cnt = PrefarenceUtils.get(getActivity(), "item2_cnt", 0) + 1;
                    PrefarenceUtils.save(getActivity(), "item2_cnt", cnt);

                    //複数回購入可能なように、消費処理を走らせる
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);

                    AndroidUtils.showToastS(getActivity(), "購入が完了しました\n非管理型アイテム保持数:" + cnt);
                }
                //定期購入アイテム購入後の処理
                else if (purchase.getSku().equals(PRODUCT_CODE[2])) {
                    LogUtils.d("定期購入アイテム(" + PRODUCT_CODE[2] + ") 購入完了!!!");

                    AndroidUtils.showToastL(getActivity(), "購入が完了しました\nプロダクトID:" + purchase.getSku());
                }
            }
        };

        //アイテム消費
        //消費というか、次また購入できるようにするための処理、という方が正しい
        //アイテムを実際に使用したときに呼ぶ必要はない、使ったかどうかは自分のサーバで管理しましょうね、とのこと
        IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
            public void onConsumeFinished(Purchase purchase, IabResult result)
            {
                // オブジェクトが生成されていない
                if (mHelper == null)
                    return;

                //成功時の処理
                if (result.isSuccess()) {
                    // TODO 課金アイテムによっての各処理

                    AndroidUtils.showToastL(getActivity(), "消費が完了しました　プロダクトID:" + purchase.getSku());
                    LogUtils.d("消費成功　" + result);
                }
                // エラー時の処理
                else {
                    AndroidUtils.showToastS(getActivity(), "消費失敗\n結果：" + result);
                    LogUtils.d("消費失敗　" + result);
                }
            }
        };

        /**
         * 識別子確認
         * 
         * @param Purchase p
         * @return boolean
         * @access private
         */
        private boolean verifyDeveloperPayload(Purchase p)
        {
            //String payload = p.getDeveloperPayload();

            //TODO 　なんか識別子判定処理書いたほうがいいっぽい

            return true;
        }
    }
}