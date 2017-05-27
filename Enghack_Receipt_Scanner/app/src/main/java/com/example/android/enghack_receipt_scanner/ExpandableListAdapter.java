package com.example.android.enghack_receipt_scanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kevin on 2017-05-26.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter{
    private Context mContext;
    private List<String> mHeaders;
    private HashMap<String, List<Product>> mChildren;

    public ExpandableListAdapter(Context context){
        mContext = context;
        mHeaders = new ArrayList<>();
        mChildren = new HashMap<>();
    }

    public void populateHeaders(List<String> data){
        for (String object : data){
            if (!mHeaders.contains(object)){
                mHeaders.add(object);
            }
        }
    }

    public void populateChildren(HashMap<String, List<Product>> data){
        for (String category : data.keySet()) {
            if (!mChildren.containsKey(category)) {
                mChildren.put(category, new ArrayList<Product>());
            }
            for (Product item : data.get(category)) {
                mChildren.get(category).add(item);
            }
        }
    }

    @Override
    public int getGroupCount() {
        return mHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = 0;

        for (String title : mHeaders) {
            if (mChildren.containsKey(title)){
                if (mChildren.get(title) != null){
                    size += mChildren.get(title).size();
                }
            }
        }
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mHeaders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildren.get(mHeaders.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_header, parent, false);
            TextView title = (TextView) convertView.findViewById(R.id.list_header_text);
            title.setText((String)getGroup(groupPosition));
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_child, parent, false);
            TextView title = (TextView) convertView.findViewById(R.id.list_child_name);
            title.setText(((Product)getChild(groupPosition, childPosition)).getName());
            TextView price = (TextView) convertView.findViewById(R.id.list_child_price);
            price.setText("$" + ((Product)getChild(groupPosition, childPosition)).getPrice().toString());
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
