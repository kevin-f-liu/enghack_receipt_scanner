package com.example.android.enghack_receipt_scanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static private int RC_BARCODE_CAPTURE = 9001;

    private ExpandableListView mExpandableList;
    private ExpandableListAdapter mAdapter;
    private List<String> mSettingHeaders;
    private HashMap<String, String> mSettingChildren;


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
        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
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
        
    }

}
