package com.example.android.enghack_receipt_scanner;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.android.enghack_receipt_scanner.sortByDate.sort;

/**
 * Created by kevin on 2017-05-27.
 */

public class ExpandableListMonthAdapter extends ExpandableListAdapter {

    public ExpandableListMonthAdapter(Context context) {
        super(context);
    }

    @Override
    public void addData(ArrayList<Product> input) {
        HashMap<Integer, List<Product>> intDates = sortByDate.sort(input);
        List<Integer> intDateList = new ArrayList<>();

        for (Integer item : intDates.keySet()){
            if (intDateList.size() == 0){
                intDateList.add(item);
            }
            else {
                for (Integer i = 0; i < intDateList.size(); i++) {
                    if (item < intDateList.get(i)) {
                        intDateList.add(i, item);
                        break;
                    }
                }
                intDateList.add(item);
            }
        }

        List<String> stringDateList= new ArrayList<>();
        for (Integer date : intDateList) {
            Integer[] data = returnNumericalDate(date);
            String formatted = String.valueOf(data[0]) + "/" + String.valueOf(data[1]) + "/" + String.valueOf(data[2]);
            stringDateList.add(formatted);
        }
        List<String> headers = new ArrayList<>();
        for (int i = 0; i<stringDateList.size(); i++) {
            Integer seperator = stringDateList.get(i).indexOf("/");
            String postFirst = stringDateList.get(i).substring(seperator+1);
            Integer seperator2 = seperator+1+postFirst.indexOf("/");
            String monthYear = stringDateList.get(i).substring(0, seperator) +"/"+ stringDateList.get(i).substring(seperator2+1);
            if (!headers.contains(monthYear)) {
                headers.add(monthYear);
            }
        }
        mHeaders = headers;

        HashMap<String, List<Product>> children = new HashMap<>();
        for (String header : mHeaders) {
            ArrayList<Product> sameDate = new ArrayList<>();
            for (Product product : input) {
                if ((String.valueOf(product.getMonth()) + "/" + String.valueOf(product.getYear())).equals(header)){
                    sameDate.add(product);
                }
            }
            children.put(header, sameDate);
        }

        mChildren = new HashMap<>();
        //add totals:
        for (String header : mHeaders) {
            HashMap<String, Float> companies = new HashMap<>();
            for (Product product : children.get(header)) {
                if (!companies.containsKey(product.getStore())){
                    companies.put(product.getStore(), product.getPrice());
                }
                else {
                    Float prev = companies.get(product.getStore());
                    prev += product.getPrice();
                    companies.remove(product.getStore());
                    companies.put(product.getStore(), Math.round(prev*100f)/100f);
                }
            }
            for (String key : companies.keySet()) {
                Integer divider = header.indexOf("/");
                children.get(header).add(new Product("Total", key, companies.get(key), Integer.valueOf(header.substring(0, divider)), 1, Integer.valueOf(header.substring(divider+1))));
            }
        }

        for (String header : mHeaders) {
            mChildren.put(header, new ArrayList<Product>());
            for (Product toAdd : children.get(header)) {
                if (mChildren.get(header).size() == 0) {
                    mChildren.get(header).add(toAdd);
                }
                else {
                    for (int i = mChildren.get(header).size()-1; i >= 0; i--){
                        if (toAdd.getStore().equals(mChildren.get(header).get(i).getStore())) {
                            mChildren.get(header).add(i+1, toAdd);
                            break;
                        }
                    }
                }
            }
            Log.d("MCHILDREN: " + header, String.valueOf(mChildren.get(header).size()));
        }
    }

    public Integer[] returnNumericalDate(Integer dateInt){
        Integer[] vars = new Integer[3];
        vars[0] = (dateInt % 10000)/100;
        vars[1] = dateInt % 100;
        vars[2] = dateInt / 10000;
        return vars;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_header, parent, false);
            TextView title = (TextView) convertView.findViewById(R.id.list_header_text);
            String original = (String)getGroup(groupPosition);
            Integer seperator = original.indexOf("/");
            String year = original.substring(seperator+1);
            String output = "";
            if (original.length() > 0) {
                switch (Integer.valueOf(original.substring(0, seperator))) {
                    case 1:
                        output = "January " + year;
                        break;
                    case 2:
                        output = "February " + year;
                        break;
                    case 3:
                        output = "March " + year;
                        break;
                    case 4:
                        output = "April " + year;
                        break;
                    case 5:
                        output = "May " + year;
                        break;
                    case 6:
                        output = "June " + year;
                        break;
                    case 7:
                        output = "July " + year;
                        break;
                    case 8:
                        output = "August " + year;
                        break;
                    case 9:
                        output = "September " + year;
                        break;
                    case 10:
                        output = "October " + year;
                        break;
                    case 11:
                        output = "November " + year;
                        break;
                    case 12:
                        output = "December " + year;
                        break;
                    default:
                        output = "Default";
                        break;
                }
            }
            title.setText(output);

            RelativeLayout temp = (RelativeLayout) convertView.findViewById(R.id.list_header_layout);

            //CODE FOR ADD MARGINS
            if (groupPosition != 0) {
                RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) temp.getLayoutParams();
                relativeParams.topMargin = 13;
                temp.setLayoutParams(relativeParams);
                temp.requestLayout();
                RelativeLayout disableClick = (RelativeLayout) convertView.findViewById(R.id.list_header_disabled);
                disableClick.setEnabled(false);
            }
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        if (true) {
            TextView price, name, date;
            FrameLayout topBorder;
            if (childPosition == 0) {
                convertView = layoutInflater.inflate(R.layout.list_child_date, parent, false);
                name = (TextView) convertView.findViewById(R.id.list_child_date_name);
                price = (TextView) convertView.findViewById(R.id.list_child_date_price);
                topBorder = (FrameLayout) convertView.findViewById(R.id.date_top_border);
                name.setText(((Product) getChild(groupPosition, childPosition)).getName());
                date = (TextView) convertView.findViewById(R.id.date_text);
                Product product = ((Product) getChild(groupPosition, childPosition));
                date.setText(product.getStore());
            }
            else if (!((Product) getChild(groupPosition, childPosition)).getStore().equals(((Product) getChild(groupPosition, childPosition-1)).getStore())) {
                convertView = layoutInflater.inflate(R.layout.list_child_date, parent, false);
                price = (TextView) convertView.findViewById(R.id.list_child_date_price);
                name = (TextView) convertView.findViewById(R.id.list_child_date_name);
                topBorder = (FrameLayout) convertView.findViewById(R.id.date_top_border);
                name.setText(((Product) getChild(groupPosition, childPosition)).getName());
                date = (TextView) convertView.findViewById(R.id.date_text);
                Product product = ((Product) getChild(groupPosition, childPosition));
                date.setText(product.getStore());
            }
            else {
                convertView = layoutInflater.inflate(R.layout.list_child, parent, false);
                price = (TextView) convertView.findViewById(R.id.list_child_price);
                topBorder = (FrameLayout) convertView.findViewById(R.id.topBorder);
                name = (TextView) convertView.findViewById(R.id.list_child_name);
                name.setText(((Product) getChild(groupPosition, childPosition)).getName());
            }
            if (((Product) getChild(groupPosition, childPosition)).getName().toUpperCase().equals("TOTAL")) {
                name.setTypeface(null, Typeface.BOLD);
                price.setTypeface(null, Typeface.BOLD);
                topBorder.setBackground(mContext.getDrawable(R.drawable.divider_line));
            }
            price.setText("$" + ((Product)getChild(groupPosition, childPosition)).getPrice().toString());
        }

        return convertView;
    }

}
