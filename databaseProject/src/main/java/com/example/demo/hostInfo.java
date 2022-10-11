package com.example.demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class hostInfo {
    private static String USER;
    private static String PASS;
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://LocalHost:3306";

//    public static void main(String[] args) {
//        System.out.println(readin(1,1));
//        System.out.println(readin(1,2));
//    }

    public static String readin(int row,int col){
        String result = null;
        try {
            String last;
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/hostInfo.csv"));
            String line = null;
            int index=0;
            while((line=reader.readLine())!=null){

                String item[] = line.split(",");

                if(index==row-1){
                    if(item.length>=col-1){
                        result = item[col-1];
                    }
                }
                index++;

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;

    }

    public static String getPASS() {
        return readin(1,2);
    }

    public static String getUSER() {
        return readin(1,1);
    }

    public static String getDbUrl() {
        return DB_URL;
    }

    public static String getJdbcDriver() {
        return JDBC_DRIVER;
    }
}


