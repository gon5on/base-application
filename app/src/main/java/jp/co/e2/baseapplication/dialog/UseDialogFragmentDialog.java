package jp.co.e2.baseapplication.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * フラグメントダイアログを継承したダイアログ
 *
 * フラグメントダイアログを使用しないで作成したダイアログとの比較用で、
 * 最低限のものしか書いてないので、参考にしてはいけない！！
 */
public class UseDialogFragmentDialog extends DialogFragment {
    /**
     * ${inheritDoc}
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage("画面再生成でも保持されるダイアログ")
                .create();
    }
}