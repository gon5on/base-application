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
public class AppProgressDialog extends BaseDialog<CallbackListener>
{
    private static ProgressDialog mDialog;

    /**
     * インスタンスを返す
     * 
     * @String String title
     * @return SampleDialog
     * @access public
     */
    public static AppProgressDialog getInstance(String title)
    {
        AppProgressDialog dialog = new AppProgressDialog();

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        dialog.setArguments(bundle);

        return dialog;
    }

    /**
     * onCreateDialog
     * 
     * @param Bundle savedInstanceState
     * @return Dialog mProgressDialog
     * @access public
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
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
     * onCancel
     * 
     * @param DialogInterface dialog
     * @return void
     * @access public
     */
    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);

        if (mCallbackListener != null) {
            mCallbackListener.onProgressDialogCancel();
        }
    }

    /**
     * onDestroyView
     * 
     * @return void
     * @access public
     */
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        mDialog = null;
    }

    /**
     * getDialog
     * 
     * @return void
     * @access public
     */
    @Override
    public Dialog getDialog()
    {
        return mDialog;
    }

    /**
     * dismiss
     * 
     * @return void
     * @access public
     */
    @Override
    public void dismiss()
    {
        if (getDialog() != null) {
            getDialog().dismiss();
        }
    }

    /**
     * コールバックリスナー
     * 
     * @access public
     */
    public interface CallbackListener
    {
        public void onProgressDialogCancel();
    }
}