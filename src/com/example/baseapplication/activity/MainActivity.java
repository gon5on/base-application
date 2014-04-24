package com.example.baseapplication.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.baseapplication.R;

/**
 * メインアクテビティ
 * 
 * @access public
 */
public class MainActivity extends AppActivity
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
        setContentView(R.layout.activity_main);
    }
}
