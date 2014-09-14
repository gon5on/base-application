package com.example.baseapplication.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;

/**
 * ダイアログ基底クラス
 * 
 * @access public
 */
public abstract class BaseDialog<Interface> extends DialogFragment
{
    public static final Integer LISTENER_ACTIVITY = 1;
    public static final Integer LISTENER_FRAGMENT = 2;

    protected Interface mCallbackListener = null;

    /**
     * onAttach
     * 
     * @param Activity activity
     * @return void
     * @access public
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        Integer listenerType = getArguments().getInt("listenerType");

        if (listenerType == BaseDialog.LISTENER_ACTIVITY) {
            mCallbackListener = (Interface) activity;
        }
        else if (listenerType == BaseDialog.LISTENER_FRAGMENT) {
            mCallbackListener = (Interface) getTargetFragment();
        }
    }

    /**
     * ダイアログのイベントリスナーを登録する
     * 
     * アクテビティから呼ばれているのか、フラグメントから呼ばれているのかを判別して、
     * 適当な方法でイベントリスナーを登録する
     * 
     * @param CallbackListener listener
     * @return void
     * @access public
     */
    public void setCallbackListener(Interface listener)
    {
        Integer listenerType;

        if (listener instanceof Activity) {
            listenerType = BaseDialog.LISTENER_ACTIVITY;
        }
        else if (listener instanceof Fragment) {
            listenerType = BaseDialog.LISTENER_FRAGMENT;
            setTargetFragment((Fragment) listener, 0);
        }
        else {
            throw new IllegalArgumentException(listener.getClass() + " must be either an Activity or a Fragment");
        }

        Bundle bundle = getArguments();
        bundle.putInt("listenerType", listenerType);
        setArguments(bundle);
    }

    /**
     * コールバックリスナーを削除
     * 
     * @return void
     * @access public
     */
    public void removeCallbackListener()
    {
        mCallbackListener = null;
    }
}
