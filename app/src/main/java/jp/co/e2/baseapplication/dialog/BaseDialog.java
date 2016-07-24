package jp.co.e2.baseapplication.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

/**
 * ダイアログ基底クラス
 */
public abstract class BaseDialog<Interface> extends DialogFragment {
    public static final int LISTENER_ACTIVITY = 1;
    public static final int LISTENER_FRAGMENT = 2;

    protected Interface mCallbackListener = null;

    /**
     * ${inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Integer listenerType = getArguments().getInt("listenerType");

        if (listenerType == BaseDialog.LISTENER_ACTIVITY) {
            mCallbackListener = (Interface) context;
        } else if (listenerType == BaseDialog.LISTENER_FRAGMENT) {
            mCallbackListener = (Interface) getTargetFragment();
        }

    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void onDetach() {
        super.onDetach();

        mCallbackListener = null;
    }

    /**
     * ダイアログのイベントリスナーを登録する
     *
     * アクテビティから呼ばれているのか、フラグメントから呼ばれているのかを判別して、
     * 適当な方法でイベントリスナーを登録する
     *
     * @param listener コールバックリスナー
     */
    public void setCallbackListener(Interface listener) {
        Integer listenerType;

        if (listener instanceof Activity) {
            listenerType = BaseDialog.LISTENER_ACTIVITY;
        } else if (listener instanceof Fragment) {
            listenerType = BaseDialog.LISTENER_FRAGMENT;
            setTargetFragment((Fragment) listener, 0);
        } else {
            throw new IllegalArgumentException(listener.getClass() + " must be either an Activity or a Fragment");
        }

        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt("listenerType", listenerType);
        setArguments(bundle);
    }

    /**
     * コールバックリスナーを削除
     */
    public void removeCallbackListener() {
        mCallbackListener = null;
    }
}
