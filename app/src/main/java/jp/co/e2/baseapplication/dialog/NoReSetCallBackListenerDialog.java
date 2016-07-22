package jp.co.e2.baseapplication.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

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
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //ダイアログ生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("コールバックリスナー再設定しないダイアログ");

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
