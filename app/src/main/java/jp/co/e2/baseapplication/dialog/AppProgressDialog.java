package jp.co.e2.baseapplication.dialog;

import jp.co.e2.baseapplication.dialog.AppProgressDialog.CallbackListener;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * プログレスダイアログ
 *
 * @access public
 */
public class AppProgressDialog extends BaseDialog<CallbackListener> {
    private static ProgressDialog mDialog;

    /**
     * インスタンスを返す
     *
     * @param title
     * @return SampleDialog
     * @access public
     */
    public static AppProgressDialog getInstance(String title) {
        AppProgressDialog dialog = new AppProgressDialog();

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        dialog.setArguments(bundle);

        return dialog;
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //bundleから値を取り出す
        String title = getArguments().getString("title");

        if (mDialog == null) {
            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage(title);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setCancelable(true);
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
     */    @Override
    public void dismiss() {
        if (getDialog() != null) {
            getDialog().dismiss();
        }
    }

    /**
     * コールバックリスナー
     *
     * @access public
     */
    public interface CallbackListener {
        /**
         * プログレスダイアログでキャンセルが押された
         *
         * @access public
         */
        public void onProgressDialogCancel();
    }
}