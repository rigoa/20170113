package com.rigo.noo;

import android.app.ActivityGroup;
import android.os.Bundle;

public class MainActivity extends ActivityGroup {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
