package com.example.android.enghack_receipt_scanner;

import com.example.android.enghack_receipt_scanner.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by janak on 2017-05-27.
 */
//taking products and sorting by date and return the hashmap
public class sortByDate {

    private static Integer dateInt(int day, int month, int year) {
        return year*10000+month*100+day;
    }

    public static HashMap<Integer, List<Product>> sort(ArrayList<Product> prod) {

        HashMap<Integer, List<Product>> productSorted = new HashMap<>();

        for (Product p : prod) {
            int date = dateInt(p.getDay(), p.getMonth(), p.getYear());
            if (productSorted.get(date)==null) {
                List<Product> plist = new ArrayList<Product>();
                plist.add(p);
                productSorted.put(date, plist);
            }
            else {
                List<Product> plist = productSorted.get(date);
                plist.add(p);
            }
        }

        return productSorted;
    }
}

