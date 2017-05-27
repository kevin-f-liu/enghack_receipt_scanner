package com.example.android.enghack_receipt_scanner;

import java.util.HashMap;

/**
 * Created by Kenta on 2017-05-26.
 */

public class Product {
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

    public Product(String initName, String initStore, float initPrice, int initMonth, int initDay, int initYear){
        name = initName;
        store = initStore;
        price = initPrice;
        month = initMonth;
        day = initDay;
        year = initYear;
        setMonthName();
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

    public void setPrice(float newPrice){
        price = newPrice;
    }

    public void setMonth(int newMonth){
        month = newMonth;
    }

    public void setMonth(String time){
        for (String key : monthName.keySet()) {
            if (time.contains(key)) {
                month = monthName.get(key);
            }
        }
    }

    public void setDay(int newDay){
        day = newDay;
    }

    public void setYear(int newYear){
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
