package com.greathaoqigreathaoqi.timetolearn;

import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

public class mode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        ListView lv = findViewById(R.id.list);
        ArrayList<HashMap<String,Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        for(int i= 0;i<10;i++){
            HashMap<String,Object> map = new HashMap<String, Object>();
            map.put("text","mode "+i);
            arrayList.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,arrayList,R.layout.mode_layout,new String[]{"text"},new int[]{R.id.mode_text});
    }
}
