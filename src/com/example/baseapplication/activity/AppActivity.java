package com.example.baseapplication.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.baseapplication.debug.Debug;

/**
 * 基底アクテビティ
 * 
 * @access public
 */
public class AppActivity extends FragmentActivity
{
    /**
     * onCreate
     * 
     * @param Bundle savedInstanceState
     * @reurn void
     * @access protected
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //デバッグ
        Debug.setUncaughtExHandler(getApplicationContext());
        Debug.showDebugReportDialog(getApplicationContext(), getFragmentManager());
    }
}