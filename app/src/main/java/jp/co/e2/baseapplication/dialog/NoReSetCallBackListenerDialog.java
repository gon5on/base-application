package jp.co.e2.baseapplication.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import jp.co.e2.baseapplication.R;

/**
 * 画面再生成時にコールバックリスナーを再設定しないダイアログ
 *
 * 画面再生成が走ると、呼び出し元でコールバックリスナーを受け取れなくないので、
 * 真似してはダメなサンプル！！
 */
public class NoReSetCallBackListenerDialog extends DialogFragment {
    private CallbackListener mCallbackListener = null;

    /**
     * ${inheritDoc}
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //ダイアログ生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.noReSetCallbackListenerDialog));

        //ボタンにイベントをセット
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCallbackListener != null) {
                    mCallbackListener.onClickNoReSetCallBackListenerDialogDialogOk();
                }
            }
        });

        return builder.create();
    }

    /**
     * ダイアログのイベントリスナーを登録する
     *
     * @param listener コールバックリスナー
     */
    public void setCallbackListener(CallbackListener listener) {
        mCallbackListener = listener;
    }

    /**
     * コールバックリスナー
     */
    public interface CallbackListener {
        /**
         * 画面再生成時にコールバックリスナーを再設定しないダイアログでOKが押された
         */
        void onClickNoReSetCallBackListenerDialogDialogOk();
    }
}
