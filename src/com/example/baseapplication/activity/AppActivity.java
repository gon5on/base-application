package com.example.baseapplication.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.baseapplication.debug.Debug;

/**
 * 基底アクテビティ
 * 
 * @access public
 */
public class AppActivity extends Activity
{
    /**
     * onCreate
     * 
     * @param Bundle savedInstanceState
     * @return void
     * @access protected
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            //デバッグ
            Debug.setUncaughtExHandler(getApplicationContext());
            Debug.showReportDialog(getApplicationContext(), getFragmentManager());
        }
    }
}