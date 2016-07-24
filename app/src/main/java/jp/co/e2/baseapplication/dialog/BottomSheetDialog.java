package jp.co.e2.baseapplication.dialog;

import android.app.Dialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;

import jp.co.e2.baseapplication.R;

/**
 * フラグメントダイアログを継承したダイアログ
 *
 * フラグメントダイアログを使用しないで作成したダイアログとの比較用で、
 * 最低限のものしか書いてないので、参考にしてはいけない！！
 */
public class BottomSheetDialog extends BottomSheetDialogFragment {
    /**
     * ファクトリーメソッド
     *
     * @return BottomSheetDialog
     */
    public static BottomSheetDialog getInstance() {
        return new BottomSheetDialog();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View view = View.inflate(getContext(), R.layout.dialog_bottom_sheet, null);
        dialog.setContentView(view);
    }
}