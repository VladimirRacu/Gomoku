package com.example.ipd.tictactoe5;

import android.preference.PreferenceActivity;
import android.os.Bundle;

public class UserSettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
