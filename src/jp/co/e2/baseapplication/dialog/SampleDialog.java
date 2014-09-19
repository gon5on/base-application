package jp.co.e2.baseapplication.dialog;

import jp.co.e2.baseapplication.dialog.SampleDialog.CallbackListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * サンプルダイアログ
 * 
 * @access public
 */
public class SampleDialog extends BaseDialog<CallbackListener>
{
    /**
     * インスタンスを返す
     * 
     * @String String title
     * @String String msg
     * @return SampleDialog
     * @access public
     */
    public static SampleDialog getInstance(String title, String msg)
    {
        SampleDialog dialog = new SampleDialog();

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("msg", msg);
        dialog.setArguments(bundle);

        return dialog;
    }

    /**
     * onCreateDialog
     * 
     * @param Bundle savedInstanceState
     * @return Dialog
     * @access public
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        //bundleから値を取り出す
        String title = getArguments().getString("title");
        String msg = getArguments().getString("msg");

        //ダイアログ生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(msg);

        //ボタンにイベントをセット
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCallbackListener != null) {
                    mCallbackListener.onClickSampleDialogOk();
                }
                dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCallbackListener != null) {
                    mCallbackListener.onClickSampleDialogCancel();
                }
                dismiss();
            }
        });

        return builder.create();
    }

    /**
     * コールバックリスナー
     * 
     * @access public
     */
    public interface CallbackListener
    {
        public void onClickSampleDialogOk();

        public void onClickSampleDialogCancel();
    }
}
