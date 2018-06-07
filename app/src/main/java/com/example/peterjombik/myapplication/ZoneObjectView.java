package com.example.peterjombik.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import static com.example.peterjombik.myapplication.R.id.fragment_gridview;

public class ZoneObjectView extends AppCompatActivity {

    GridView gridView;
    String zone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_object_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ZoneView_toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            zone = bundle.getString("zone");
        }

        for (int i = 0; i < MainActivity.myItemList.size(); i++){
            if (MainActivity.myItemList.get(i).getZone().equals(zone)){
                MainActivity.myZoneItems.add(MainActivity.myItemList.get(i));
            }
        }

        gridView = (GridView) findViewById(R.id.zoneitemsgrid);
        gridView.setAdapter(MainActivity.zoneitemsadapter);


        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                MainActivity.myZoneItems.clear();
                finish();
            }
        });

    }

}
