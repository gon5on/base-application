package jp.co.e2.baseapplication.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import jp.co.e2.baseapplication.R;

/**
 * 画面再生成時にコールバックリスナーを再設定するダイアログ
 *
 * フラグメントからコールバックリスナーとセットされたか、
 * アクテビティからセットされたか、判断する処理は省いているので、真似してはだめ！！
 */
public class ReSetCallBackListenerDialog extends DialogFragment {
    private CallbackListener mCallbackListener = null;

    /**
     * ${inheritDoc}
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //ダイアログ生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("コールバックリスナー再設定するダイアログ");

        //ボタンにイベントをセット
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCallbackListener != null) {
                    mCallbackListener.onClickReSetCallBackListenerDialogDialogOk();
                }
            }
        });

        return builder.create();
    }

    /**
     * ${inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mCallbackListener = (CallbackListener) getTargetFragment();
    }

    /**
     * ダイアログのイベントリスナーを登録する
     *
     * @param listener コールバックリスナー
     */
    public void setCallbackListener(CallbackListener listener) {
        setTargetFragment((Fragment) listener, 0);
    }

    /**
     * コールバックリスナー
     */
    public interface CallbackListener {
        /**
         * 画面再生成時にコールバックリスナーを再設定するダイアログでOKが押された
         */
        void onClickReSetCallBackListenerDialogDialogOk();
    }
}
