package com.example.android.enghack_receipt_scanner.ui;

import com.example.android.enghack_receipt_scanner.Product;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by janak on 2017-05-27.
 */
//taking products and sorting by date and return the hashmap
public class sortByDate {

    private static Integer dateInt(int day, int month, int year) {
        return year*10000+month*100+day;
    }

    public static HashMap<Integer, ArrayList<Product>> sortByDate(ArrayList<Product> prod) {

        HashMap<Integer, ArrayList<Product>> productSorted = new HashMap<>();

        for (Product p : prod) {
            int date = dateInt(p.getDay(), p.getMonth(), p.getYear());
            if (productSorted.get(date)==null) {
                ArrayList<Product> plist = new ArrayList<Product>();
                plist.add(p);
                productSorted.put(date, plist);
            }
            else {
                ArrayList<Product> plist = productSorted.get(date);
                plist.add(p);
            }
        }

        return productSorted;
    }
}

