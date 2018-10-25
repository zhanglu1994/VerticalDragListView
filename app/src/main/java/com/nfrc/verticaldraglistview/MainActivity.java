package com.nfrc.verticaldraglistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ListView mListiew;
    private ArrayList<String> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListiew = findViewById(R.id.mListiew);

        mItems = new ArrayList<>();

        for (int i = 0;i < 200;i++){
            mItems.add("i -> " + i);
        }


        mListiew.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mItems.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {


                TextView textView = new TextView(MainActivity.this);
                textView.setText(mItems.get(position));


                return textView;
            }
        });

    }
}
