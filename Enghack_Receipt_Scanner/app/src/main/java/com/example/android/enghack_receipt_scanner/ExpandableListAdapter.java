package com.example.android.enghack_receipt_scanner;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

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
        return mChildren.get(getGroup(groupPosition)).size();
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
        Log.d("GROUP", String.valueOf(groupPosition));
        Log.d("MAXCHILD", String.valueOf(getChildrenCount(groupPosition)));
        Log.d("CHILD", String.valueOf(childPosition));

        if (convertView == null) {
            TextView price;
            if (childPosition == 0) {
                convertView = layoutInflater.inflate(R.layout.list_child_date, parent, false);
                TextView name = (TextView) convertView.findViewById(R.id.list_child_date_name);
                price = (TextView) convertView.findViewById(R.id.list_child_date_price);
                name.setText(((Product) getChild(groupPosition, childPosition)).getName());
                TextView date = (TextView) convertView.findViewById(R.id.date_text);
                Product product = ((Product) getChild(groupPosition, childPosition));
                date.setText(String.valueOf(product.getMonth())+"/"+ String.valueOf(product.getDay())
                    +"/"+String.valueOf(product.getYear()));
            }
            else if (((Product) getChild(groupPosition, childPosition)).getDay() != ((Product) getChild(groupPosition, childPosition-1)).getDay()
                    || ((Product) getChild(groupPosition, childPosition)).getMonth() != ((Product) getChild(groupPosition, childPosition-1)).getMonth()
                    || (((Product) getChild(groupPosition, childPosition)).getYear() - ((Product) getChild(groupPosition, childPosition-1)).getYear()) != 0) {
                convertView = layoutInflater.inflate(R.layout.list_child_date, parent, false);
                price = (TextView) convertView.findViewById(R.id.list_child_date_price);
                TextView name = (TextView) convertView.findViewById(R.id.list_child_date_name);
                name.setText(((Product) getChild(groupPosition, childPosition)).getName());
            }
            else {
                convertView = layoutInflater.inflate(R.layout.list_child, parent, false);
                price = (TextView) convertView.findViewById(R.id.list_child_price);
                TextView title = (TextView) convertView.findViewById(R.id.list_child_name);
                title.setText(((Product) getChild(groupPosition, childPosition)).getName());
            }
            price.setText("$" + ((Product)getChild(groupPosition, childPosition)).getPrice().toString());
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
