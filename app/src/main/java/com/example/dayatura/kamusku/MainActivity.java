package com.example.dayatura.kamusku;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {


//    Map<Integer, String> dict = new HashMap<Integer, String>() {{
//        put(1, "ambo");
//        put(2, "nio");
//        put(3, "lalok");
//    }};
//    String dict_filename = "data.csv";
//    List<List<String>> dict = CSVReader.read(this);

    EditText text_input;
    EditText text_translated;

    private Spinner spinner;
    private static final String[] language = {"Jawa", "Sunda", "Minang"};

    private int selected_language;
    private Kamus kamus;
    private List<List<String>> dict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        String dict_filename = "data.csv";
        dict = CSVReader.read(this);

        kamus = new Kamus(4);

        String[] kata = {"air", "basah", "buruk", "nol", "satu", "dua", "tiga", "empat", "lima", "enam", "tujuh" , "delapan", "sembilan", "sepuluh", "besar", "kecil", "kursi", "lepas", "lapar", "tidur", "makan", "mandi", "meja", "minum", "piring", "putus", "bagus", "rumah", "rumput", "telinga", "itu", "dimana", "kapan", "berapa", "apa", "siapa", "bagaimana", "dia", "pergi", "berangkat", "kenyang", "mengantuk", "ramai", "hangat", "potong", "sarung", "retak", "dinding", "besok"};
        for (int i=0; i<kata.length; i++){
            kamus.insert_tree(kata[i], i+1);
        }

//        kamus.insert_tree("domba");
//        kamus.insert_tree("cicak");
//        kamus.insert_tree("candaan");
//        kamus.insert_tree("candi");
//        kamus.insert_tree("bebek");
//        kamus.insert_tree("dambaan");
//        kamus.insert_tree("celah");
//        kamus.insert_tree("aku", 1);
//        kamus.insert_tree("tidur", 3);
//        kamus.insert_tree("mau", 2);



//        Log.d(getClass().getName(), kamus.print_memory_tree());



        text_input = (EditText) findViewById(R.id.txt_input);
        text_translated = (EditText) findViewById(R.id.text_translated);


        selected_language = CSVReader.JAWA;
        text_input.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                final String text = s.toString();

                new Thread(new Runnable() {
                    public void run() {
                        translate(text, kamus, dict);
                    }
                }).start();

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });



        spinner = (Spinner)findViewById(R.id.spin_language);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,language);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void translate(String text, Kamus kamus, List<List<String>> dict) {
        final String[] words = text.split(" ");
        for (int i=0; i<words.length; i++){
//                            if (words[i].length()!=0)
//                            if (kamus.search_tree(words[i])) {
//                                words[i] = dict.get(words[i]);
//                            }
            if (words[i].length()>0){
                int dict_idx  = kamus.dict_idx(words[i]);
                if (dict_idx!=0) words[i] = dict.get(dict_idx).get(selected_language);
            }

        }
        text_translated.post(new Runnable() {
            @Override
            public void run() {
                text_translated.setText(TextUtils.join(" ", words));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (i) {
            case 0:
                selected_language = CSVReader.JAWA;
                translate(text_input.getText().toString(),kamus, dict);
                break;
            case 1:
                selected_language = CSVReader.SUNDA;
                translate(text_input.getText().toString(),kamus, dict);
                break;
            case 2:
                selected_language = CSVReader.MINANG;
                translate(text_input.getText().toString(),kamus, dict);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        adapterView.setSelection(selected_language);
    }
}
