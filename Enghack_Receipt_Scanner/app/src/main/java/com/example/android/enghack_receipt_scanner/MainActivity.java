package com.example.android.enghack_receipt_scanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    static private int RECEIPT_CAPTURE = 9001;

    private ExpandableListView mExpandableList;
    private ExpandableListAdapter mAdapter;
    private List<String> mSettingHeaders;
    private HashMap<String, List<String>> mSettingChildren;
    private SharedPreferences mPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView cameraText = (TextView) findViewById(R.id.camera_text);
        cameraText.setTypeface(getFATypeface(this));

        FrameLayout cameraButton = (FrameLayout) findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToCamera();
            }
        });

        mExpandableList = (ExpandableListView) findViewById(R.id.expandable_list);
    }


    public Typeface getFATypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");
    }

    public void switchToCamera() {
        Intent intent = new Intent(this, OcrCaptureActivity.class);
        startActivityForResult(intent, RECEIPT_CAPTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECEIPT_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {

                }
            }
        }

        if (mAdapter == null) {
            mAdapter = new ExpandableListAdapter(this);
            //populate data
            mExpandableList.setAdapter(mAdapter);
        }
    }


    private void save() {
        if (mPreferences == null) {
            mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        }

        if (mPreferences.contains("Headers")) {
            delete("Headers");
        }
        mPreferences.edit().putStringSet("Headers", new HashSet<String>(mSettingHeaders)).commit();
        for (String title : mSettingHeaders) {
            if (mPreferences.contains(title)) {
                delete(title);
            }
            mPreferences.edit().putStringSet(title, new HashSet<String>(mSettingChildren.get(title))).commit();
        }
    }

    private void delete(String key) {
        if (mPreferences != null) {
            mPreferences.edit().remove(key).commit();
        }
    }

    private List<String> getHeaders() {
        if (mPreferences != null && mPreferences.contains("Headers")) {
            Set<String> temp = mPreferences.getStringSet("Headers", null);
            if (temp == null) return null;
            else return new ArrayList<String>(temp);
        }
        return null;
    }

}
