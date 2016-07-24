package jp.co.e2.baseapplication.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import jp.co.e2.baseapplication.R;

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
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.useDialogFragmentDialog))
                .setPositiveButton(getString(R.string.ok), null)
                .create();
    }
}