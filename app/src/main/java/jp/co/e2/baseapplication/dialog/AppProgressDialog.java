package jp.co.e2.baseapplication.dialog;

import jp.co.e2.baseapplication.dialog.AppProgressDialog.CallbackListener;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * プログレスダイアログ
 */
public class AppProgressDialog extends BaseDialog<CallbackListener> {
    private static final String TITLE = "title";

    private static ProgressDialog mDialog;

    /**
     * インスタンスを返す
     *
     * @param title タイトル
     * @return AppProgressDialog
     */
    public static AppProgressDialog getInstance(String title) {
        AppProgressDialog dialog = new AppProgressDialog();

        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        dialog.setArguments(bundle);

        return dialog;
    }

    /**
     * ${inheritDoc}
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //bundleから値を取り出す
        String title = getArguments().getString(TITLE);

        if (mDialog == null) {
            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage(title);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

        return mDialog;
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        if (mCallbackListener != null) {
            mCallbackListener.onProgressDialogCancel();
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mDialog = null;
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public Dialog getDialog() {
        return mDialog;
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void dismiss() {
        if (getDialog() != null) {
            getDialog().dismiss();
        }
    }

    /**
     * コールバックリスナー
     */
    public interface CallbackListener {
        /**
         * プログレスダイアログでキャンセルが押された
         */
        void onProgressDialogCancel();
    }
}