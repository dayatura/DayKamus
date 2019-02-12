package com.example.dayatura.kamusku;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVReader {

    public static int INDONESIA = 0;
    public static int JAWA = 1;
    public static int SUNDA = 2;
    public static int MINANG = 3;



//    public static void main(String[] args) {
//
//        String csvFile = "data.csv";
//        BufferedReader br = null;
//        String line = "";
//        String cvsSplitBy = ",";
//
//        List<List<String>> data = new ArrayList<List<String>>();
//
//
//        try {
//
//            br = new BufferedReader(new FileReader(csvFile));
//            while ((line = br.readLine()) != null) {
//
//                // use comma as separator
//                List<String> temp = Arrays.asList(line.split(cvsSplitBy));
//                data.add(temp);
//                // String[] kata = line.split(cvsSplitBy);
//
//                // System.out.println(kata[0] + "\t" + kata[1]+ "\t" + kata[2]+ "\t" + kata[3]);
//
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//
//    }

    public static List<List<String>> read(Context ctx) {
//        String csvFile = dict_filename;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        List<List<String>> data = new ArrayList<List<String>>();

        try {
            br = new BufferedReader(new InputStreamReader(ctx.getResources().openRawResource(R.raw.data)));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                List<String> temp = Arrays.asList(line.split(cvsSplitBy));
                data.add(temp);
                // String[] kata = line.split(cvsSplitBy);

                // System.out.println(kata[0] + "\t" + kata[1]+ "\t" + kata[2]+ "\t" + kata[3]);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
}