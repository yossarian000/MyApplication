package com.example.peterjombik.myapplication;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.peterjombik.myapplication.R.id.fragment_gridview;
import static com.example.peterjombik.myapplication.R.id.toolbar;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private PlaceholderFragment.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private int mycounter = 0;
    public static ArrayList<ItemObject> myItemList = new ArrayList<ItemObject>();
    public static itemadapter adapter;
    public static String DataValue;

    public static MqttHelper mqttHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new PlaceholderFragment.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        //DataReceived = (TextView) findViewById(R.id.DataReceived);
        try {
            SharedPreferences appsharedprefs = getSharedPreferences("KoJo", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = appsharedprefs.getString("MyObject", "");
            Type type = new TypeToken<ArrayList<ItemObject>>() {
            }.getType();
            ArrayList<ItemObject> test = gson.fromJson(json, type);
            //userList = (ArrayList<ItemObject>) ObjectSerializer.deserialize(prefs.getString("ObjectList", ObjectSerializer.serialize(new ArrayList<ItemObject>())));
            if (test != null) {
                myItemList = test;
            }
        }
        catch (ClassCastException e){

        }

        adapter = new itemadapter(this, android.R.layout.simple_list_item_1, myItemList);

        startMqtt();

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Context context = getApplicationContext();
        int text = R.string.toast_addnew;
        int duration = Toast.LENGTH_SHORT;

        String content = new String();
        String dataValue = new String();

        content = "Item " + mycounter;
        dataValue = DataValue;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

//            ArrayList userList   = new ArrayList();
//            userList.add(new Users());
//            userList.add(new Users());
            SharedPreferences prefs = getSharedPreferences("KoJo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(myItemList);
            editor.putString("MyObject", json);
            editor.commit();

            try {
                editor.putString("ObjectList", ObjectSerializer.serialize(myItemList));
                //editor.putString("ObjectList", ObjectSerializer.serialize("asdasdasd"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            editor.commit();

            Intent intent = new Intent(context, Settings.class);

            startActivity(intent);

            return true;
        }
        if (id == R.id.action_addnew) {

            Intent intent = new Intent(context, ItemConfiguration.class);
            startActivity(intent);
            //Toast toast = Toast.makeText(context, text, duration);
            //toast.show();
            mycounter ++;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        AdapterView.OnItemClickListener myOnItemclickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //int selectedItem = adapterView.getSelectedItemPosition();

                String messageON = "1";
                String messageOFF= "0";
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                String timestamp =  df.format(c);

                if (myItemList.get(i).getActor()) {
                    ImageView myicon = adapterView.findViewById(R.id.imageView);

                    if (myItemList.get(i).getDataValue().equals("1")){
                        try {
                            MainActivity.mqttHelper.mqttAndroidClient.publish(myItemList.get(i).getPublishTopic().toString(), new MqttMessage(messageOFF.getBytes()));
                            myItemList.get(i).setStatus("Published on: " + timestamp);

                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            MainActivity.mqttHelper.mqttAndroidClient.publish(myItemList.get(i).getPublishTopic().toString(), new MqttMessage(messageON.getBytes()));
                            myItemList.get(i).setStatus("Published on: " + timestamp);

                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                    //Toast toast = Toast.makeText(getContext(), selectedItem.toString(), Toast.LENGTH_SHORT);
//                    Toast toast = Toast.makeText(getContext(), "published 1", Toast.LENGTH_SHORT);
//                    toast.show();
                }
            }
        };

        AdapterView.OnItemLongClickListener myOnItemLongclickListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int selectedItem = ((int) adapterView.getItemIdAtPosition(i));

                PopupMenu myPopup = new PopupMenu(getContext(), view);

                myPopup.getMenuInflater().inflate(R.menu.mything_popup, myPopup.getMenu());

                myPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        String itemTitle = item.getTitle().toString();

                        //if (itemTitle.equals("@strings/myPopup01")){
                        if (itemTitle.equals("Delete...")){
                            try {
                                MainActivity.mqttHelper.mqttAndroidClient.unsubscribe(myItemList.get(selectedItem).getTopic().toString());
                            }
                            catch (MqttException ex){

                            }
                            myItemList.remove(myItemList.get(selectedItem));

                        }

                        //else if (itemTitle.equals("@strings/myPopup02")){
                        if (itemTitle.equals("Edit...")){
                            Intent intent = new Intent(getContext(), ItemConfiguration.class);

                            Bundle mBundle = new Bundle();

                            Integer id = selectedItem;
                            String name = MainActivity.myItemList.get(selectedItem).getName().toString();
                            String zone = MainActivity.myItemList.get(selectedItem).getZone().toString();
                            String type = MainActivity.myItemList.get(selectedItem).getType().toString();
                            String topic = MainActivity.myItemList.get(selectedItem).getTopic().toString();
                            Boolean actor = MainActivity.myItemList.get(selectedItem).getActor();
                            String publish = MainActivity.myItemList.get(selectedItem).getPublishTopic();

                            mBundle.putInt("id", id);
                            mBundle.putString("name", name);
                            mBundle.putString("topic", topic);
                            mBundle.putString("zone", zone);
                            mBundle.putString("type", type);
                            mBundle.putBoolean("actor", actor);
                            mBundle.putString("publish", publish);

                            intent.putExtras(mBundle);
                            startActivity(intent);
                        }

                        adapter.notifyDataSetChanged();
                        return true;
                    }
                });

                myPopup.show();

                //myItemList.remove(myItemList.get(i));
                //adapter.notifyDataSetChanged();

                //Toast toast = Toast.makeText(getContext(), "longclick!", Toast.LENGTH_SHORT);
                //toast.show();

                return false;
            }
        };

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        View rootView;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            switch (getArguments().getInt(ARG_SECTION_NUMBER))
            {
                case 1: {
                    rootView = inflater.inflate(R.layout.fragment_floorplan, container, false);
                    ImageView imgview = (ImageView) rootView.findViewById(R.id.mything_floorplan);
                    imgview.setColorFilter(Color.WHITE);
                    //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                    //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                    break;
                }
                case 2: {
                    //View view = inflater.inflate(R.layout.fragment_gridview, container, false);
                    //super.onCreate(savedInstanceState);
                    rootView = inflater.inflate(R.layout.fragment_gridview, container, false);
                    GridView gridview = (GridView) rootView.findViewById(fragment_gridview);

                    gridview.setAdapter(adapter);

                    gridview.setOnItemClickListener(myOnItemclickListener);
                    gridview.setOnItemLongClickListener(myOnItemLongclickListener);

                    break;
                }
                case 3: {
                    rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                    textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                    break;
                }
            }
            return rootView;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    }
    private void startMqtt(){
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            @Override
            public void connectComplete(boolean b, String s) {
                toolbar.setSubtitle("...connected");
                Toast toast = Toast.makeText(getApplicationContext(), "Conencted to MQTT!", Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void connectionLost(Throwable throwable) {
                toolbar.setSubtitle("...disconnected");
                Toast toast = Toast.makeText(getApplicationContext(), "MQTT connection lost", Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug",mqttMessage.toString());

                int itemlistsize = myItemList.size();

                for (int i = 0; i< itemlistsize; i++){
                    if (myItemList.get(i).getTopic().toString().equals(topic)){
                        myItemList.get(i).setDataValue(mqttMessage.toString());
                        adapter.notifyDataSetChanged();
                    }
                }

                //DataReceived.setText(mqttMessage.toString());
                //myItemList.get(0).setDataValue(mqttMessage.toString());
                //DataValue = mqttMessage.toString();
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }
}
