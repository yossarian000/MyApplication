package com.example.peterjombik.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemConfiguration extends AppCompatActivity {

    private Integer id;
    private EditText topic;
    private EditText name;
    private Spinner staticTypeSpinner;
    private Spinner staticZoneSpinner;
    private Boolean itemedited = false;

    MqttHelper mqttHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_configuration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.Itemconfig_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        staticTypeSpinner = findViewById(R.id.mything_typeinput);
        staticZoneSpinner = findViewById(R.id.mything_zoneinput);

        topic = findViewById(R.id.mything_topicnameinput);
        name = findViewById(R.id.mything_idinput);
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticTypeAdapter = ArrayAdapter.createFromResource(this, R.array.Typelist, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> staticZoneAdapter = ArrayAdapter.createFromResource(this, R.array.Zonelist, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staticZoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticTypeSpinner.setAdapter(staticTypeAdapter);
        staticZoneSpinner.setAdapter(staticZoneAdapter);

        staticTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        staticZoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            id = bundle.getInt("id");
            name.setText(bundle.getString("name"));
            topic.setText(bundle.getString("topic"));

            int zonespinpos = staticZoneAdapter.getPosition(bundle.getString("zone"));
            staticZoneSpinner.setSelection(zonespinpos);

            int typespinpos = staticTypeAdapter.getPosition(bundle.getString("type"));
            staticTypeSpinner.setSelection(typespinpos);

            itemedited = true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_itemconfig, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int menuitemid = item.getItemId();

        Context context = getApplicationContext();
        int text = R.string.toast_addnew;
        int duration = Toast.LENGTH_SHORT;

        String myType = staticTypeSpinner.getSelectedItem().toString();
        String myZone = staticZoneSpinner.getSelectedItem().toString();

        //noinspection SimplifiableIfStatement
        if (menuitemid == R.id.action_itemconfig_save) {

            if (itemedited) {
                MainActivity.myItemList.get(id).setName(name.getText().toString());
                MainActivity.myItemList.get(id).setTopic(topic.getText().toString());
                MainActivity.myItemList.get(id).setZone(myZone);
                MainActivity.myItemList.get(id).setType(myType);

                //MainActivity.mqttHelper.mqttAndroidClient.subscribe("/sensors/hum02", 0);

                MainActivity.adapter.notifyDataSetChanged();
            }
            else {
                MainActivity.myItemList.add(new ItemObject("", name.getText().toString(), "", "baseline_android_black_36dp", topic.getText().toString(), myZone, myType));
                MainActivity.adapter.notifyDataSetChanged();
            }

            finish();

            //Toast toast = Toast.makeText(context, text, duration);
            //toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
