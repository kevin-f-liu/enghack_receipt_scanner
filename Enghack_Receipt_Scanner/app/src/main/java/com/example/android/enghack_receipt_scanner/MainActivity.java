package com.example.android.enghack_receipt_scanner;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.text.TextBlock;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.R.attr.width;

public class MainActivity extends AppCompatActivity {
    static private int RECEIPT_CAPTURE = 9001;

    private ExpandableListView mExpandableList;
    private ExpandableListAdapter mAdapter;
    private List<String> mSettingHeaders;
    private HashMap<String, List<Product>> mSettingChildren;
    private SharedPreferences mPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar topBar = getSupportActionBar();
        if (topBar != null) {
            topBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6A8347")));
        }

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

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
        mExpandableList.setIndicatorBounds(width-100, width);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSettingHeaders = getHeaders();
        mSettingChildren = getChildren(mSettingHeaders);
        mAdapter = new ExpandableListAdapter(this);
        mAdapter.populateHeaders(mSettingHeaders);
        mAdapter.populateChildren(mSettingChildren);
        mExpandableList.setAdapter(mAdapter);
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            mExpandableList.expandGroup(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reset_button) {
            reset();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            try {
                ArrayList<String> output = new ArrayList<>();
                ArrayList<Product> products = new ArrayList<>();
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    if (data != null) {
                        String store = "";
                        output = data.getStringArrayListExtra("TextBlockObject");
                        for (String item : output) {
                            Log.d("SERIALIZE", item);
                            products.add(Product.makeFromSerialize(item));
                        }
                        store = products.get(products.size() - 1).getStore();
                        Log.d("STORE", store);

                        if (mSettingHeaders == null) {
                            mSettingHeaders = new ArrayList<>();
                        }
                        if (!mSettingHeaders.contains(store)) {
                            mSettingHeaders.add(store);
                        }
                        if (mSettingChildren == null) {
                            mSettingChildren = new HashMap<>();
                        }
                        if (mSettingChildren.containsKey(store)) {
                            mSettingChildren.get(store).addAll(products);
                        } else {
                            mSettingChildren.put(store, products);
                        }
                        mAdapter = new ExpandableListAdapter(this);
                        mAdapter.populateHeaders(mSettingHeaders);
                        mAdapter.populateChildren(mSettingChildren);
                        mExpandableList.setAdapter(mAdapter);
                        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
                            mExpandableList.expandGroup(i);
                        }
                        save();
                    }
                }
            }catch (Exception e) {
                Toast.makeText(this, "Error. Please try scanning again.", Toast.LENGTH_LONG).show();
            }
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
            List<String> serializedProducts = new ArrayList<>();
            if (mPreferences.contains(title)) {
                delete(title);
            }
            for (Product item : mSettingChildren.get(title)) {
                serializedProducts.add(item.serialize());
            }
            mPreferences.edit().putStringSet(title, new HashSet<String>(serializedProducts)).commit();
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
            if (temp == null) return new ArrayList<>();
            else {
                return new ArrayList<String>(temp);
            }
        }
        return new ArrayList<String>();
    }

    private HashMap<String, List<Product>> getChildren(List<String> headers){
        HashMap<String, List<Product>> toReturn = new HashMap<>();
        Log.d("SIZE", String.valueOf(headers.size()));

        for (String title : headers) {
            Set<String> temp = mPreferences.getStringSet(title, null);
            if (temp != null) {
                ArrayList<String> serializedList = new ArrayList<String>(temp);
                ArrayList<Product> productList = new ArrayList<Product>();
                for (String serializedData : serializedList) {
                    productList.add(Product.makeFromSerialize(serializedData));
                }
                toReturn.put(title, productList);
            }
        }

        return toReturn;
    }

    private void reset() {
        if (mPreferences != null) mPreferences.edit().clear().commit();
        mAdapter = new ExpandableListAdapter(this);
        mExpandableList.setAdapter(mAdapter);
    }

}
