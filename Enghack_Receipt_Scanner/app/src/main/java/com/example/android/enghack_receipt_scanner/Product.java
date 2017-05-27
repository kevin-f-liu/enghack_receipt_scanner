package com.example.android.enghack_receipt_scanner;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by Kenta on 2017-05-26.
 */

public class Product{
    public static final Integer MM_DD_YYYYY = 1;

    private String name;
    private String store;
    private Float price;
    private Integer month;
    private Integer day;
    private Integer year;

    private HashMap<String, Integer> monthName = new HashMap<>();

    private void setMonthName(){
        monthName.put("January", 1);
        monthName.put("Febraury", 2);
        monthName.put("March", 3);
        monthName.put("April", 4);
        monthName.put("May", 5);
        monthName.put("June", 6);
        monthName.put("July", 7);
        monthName.put("August", 8);
        monthName.put("September", 9);
        monthName.put("October", 10);
        monthName.put("November", 11);
        monthName.put("December", 12);
        monthName.put("Jan", 1);
        monthName.put("Feb", 2);
        monthName.put("Mar", 3);
        monthName.put("Apr", 4);
        monthName.put("May", 5);
        monthName.put("Jun", 6);
        monthName.put("Jul", 7);
        monthName.put("Aug", 8);
        monthName.put("Sep", 9);
        monthName.put("Oct", 10);
        monthName.put("Nov", 11);
        monthName.put("Dec", 12);
    }

    public Product(String initName, String initStore, float initPrice, Integer initMonth, Integer initDay, Integer initYear){
        name = initName;
        store = initStore;
        price = initPrice;
        month = initMonth;
        day = initDay;
        year = initYear;
        setMonthName();
    }

    public Product(String initName, String initStore, String initPrice, String initMonth, String initDay, String initYear){
        name = initName;
        store = initStore;
        price = Float.valueOf(initPrice);
        try {
            month = Integer.valueOf(initMonth);
        } catch (NumberFormatException e) {
            setMonth(month);
        }
        day = Integer.valueOf(initDay);
        year = Integer.valueOf(initYear);
        setMonthName();
    }

    public Product(String initName, String initStore, String initPrice, String Date, Integer type){
        name = initName;
        store = initStore;
        try {
//            if (initPrice.contains("$")) {
//                price = Float.valueOf(initPrice.substring(1));
//            } else {
                price = Float.valueOf(initPrice.substring(1));
//            }
        } catch (Exception e) {
            Log.d("CONSTRUCTOR", e.getClass().toString());
            Log.d("CONSTRUCTOR_PRICE", initPrice);
        }
        setFormatDate(Date, type);
        setMonthName();
    }

    public void setFormatDate(String input, Integer type) {
        switch (type) {
            case 1:
                try {
                    Integer indexStart = 0;
                    Integer indexEnd = 0;
                    Boolean isChanged = false;
                    for (int i = 0; i < input.length()-2; i++) {
                        if (Character.isDigit(input.charAt(i))
                                && Character.isDigit(input.charAt(i+1))
                                && input.charAt(i+2) == '/') {
                                indexStart = i;
                                isChanged = true;
                        }
                        if (isChanged) break;
                    }
                    String start = input.substring(indexStart);
                    Log.d("START", start);
                    Boolean changed = false;
                    for (int i = 0; i <= start.length()-4; i++) {
                        if (Character.isDigit(start.charAt(i))
                            && Character.isDigit(start.charAt(i+1))
                            && Character.isDigit(start.charAt(i+2))
                            && Character.isDigit(start.charAt(i+3))){
                            indexEnd = i+4;
                            changed = true;
                        }
                        if (changed) break;
                    }
                    String end = start.substring(0, indexEnd);
                    Log.d("END", end);
                    Integer seperator = end.indexOf("/");
                    month = Integer.valueOf(end.substring(0, seperator));
                    String end1 = end.substring(seperator + 1);
                    seperator = end1.indexOf("/");
                    day = Integer.valueOf(end1.substring(0, seperator));
                    Integer space = end1.indexOf(" ");
                    if (space != -1) {
                        year = Integer.valueOf(end1.substring(seperator + 1, space));
                    } else year = Integer.valueOf(end1.substring(seperator + 1));
                } catch (Exception e) {
                    Log.d("FORMAT DATE", e.getClass().toString());
                    Log.d("FORMAT DATE", input);
                    Log.d("FORMAT DATE", e.getMessage());
                }
                break;
            default:
                year = 0;
                month = 0;
                day = 0;
                break;
        }
    }


    public String serialize() {
        //Log.d("SERIALIZE PRICE", price.toString());
        String serialized = name+"&%#!"+store+"&%#!"+price.toString()+"&%#!"+month.toString()+"&%#!"+day.toString()+"&%#!"+year.toString();
        return serialized;
    }

    public static Product makeFromSerialize(String input) {
        Integer index;
        index = input.indexOf("&%#!");
        String name = input.substring(0, index);

        String temp = input.substring(index+4);
        index = temp.indexOf("&%#!");
        String store = temp.substring(0, index);

        String temp2 = temp.substring(index+4);
        index = temp2.indexOf("&%#!");
        String price = temp2.substring(0, index);

        String temp3 = temp2.substring(index+4);
        index = temp3.indexOf("&%#!");
        String month = temp3.substring(0, index);

        String temp4 = temp3.substring(index+4);
        index = temp4.indexOf("&%#!");
        String day = temp4.substring(0, index);

        String year = temp4.substring(index+4);

        return new Product(name, store, price, month, day, year);
    }

    public void setName(String newName){
        name = newName;
    }

    public void setStore(String newStore){
        store = newStore;
    }

    public void setPrice(Float newPrice){
        price = newPrice;
    }

    public void setMonth(Integer newMonth){
        month = newMonth;
    }

    public void setMonth(String time){
        for (String key : monthName.keySet()) {
            if (time.contains(key)) {
                month = monthName.get(key);
            }
        }
    }

    public void setDay(Integer newDay){
        day = newDay;
    }

    public void setYear(Integer newYear){
        year = newYear;
    }

    public String getName(){
        return name;
    }

    public String getStore(){
        return store;
    }

    public Float getPrice(){
        return price;
    }

    public Integer getMonth(){
        return month;
    }

    public Integer getDay(){
        return day;
    }

    public Integer getYear(){
        return year;
    }
}
