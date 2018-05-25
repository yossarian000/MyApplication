package com.example.peterjombik.myapplication;

import android.content.ClipData;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.peterjombik.myapplication.R.id.all;
import static com.example.peterjombik.myapplication.R.id.fragment_gridview;
import static com.example.peterjombik.myapplication.R.id.fragment_gridview;
import static com.example.peterjombik.myapplication.R.id.parent;
import static com.example.peterjombik.myapplication.R.id.section_label;

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

    static ArrayList<ItemObject> myItemList = new ArrayList<ItemObject>();

    public static itemadapter adapter;

    public static String DataValue;

    TextView DataReceived;

    MqttHelper mqttHelper;


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

        adapter = new itemadapter(this, android.R.layout.simple_list_item_1, myItemList);

        //DataReceived = (TextView) findViewById(R.id.DataReceived);

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
            return true;
        }
        if (id == R.id.action_addnew) {

            if (mycounter == 0){
                myItemList.add(new ItemObject(content, dataValue, "baseline_android_black_36dp"));
            }
            else if (mycounter == 1){
                myItemList.add(new ItemObject(content, dataValue,"baseline_android_black_36dp"));
            }
            else if (mycounter == 2){
                myItemList.add(new ItemObject(content,dataValue, "baseline_android_black_36dp"));
            }
            else if (mycounter == 3){
                myItemList.add(new ItemObject(content, dataValue,"baseline_android_black_36dp"));
            }
            else{
                myItemList.add(new ItemObject(content, dataValue,"baseline_android_black_36dp"));
            }

            adapter.notifyDataSetChanged();

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

                Long selectedItem = adapterView.getItemIdAtPosition(i);

                Toast toast = Toast.makeText(getContext(), selectedItem.toString(), Toast.LENGTH_SHORT);
                toast.show();
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

                        if (itemTitle.equals("Delete...")){
                            myItemList.remove(myItemList.get(selectedItem));
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
                    rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                    textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                    break;
                }
                case 2: {
                    //View view = inflater.inflate(R.layout.fragment_gridview, container, false);
                    //super.onCreate(savedInstanceState);
                    rootView = inflater.inflate(R.layout.fragment_gridview, container, false);
                    GridView gridview = (GridView) rootView.findViewById(fragment_gridview);

                    //myItemList.add(new ItemObject("hehe", "ic_launcher_foreground"));

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
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug",mqttMessage.toString());

                switch (topic){
                    case "/sensors/temp02":{
                        myItemList.get(0).setDataValue(mqttMessage.toString());
                        myItemList.get(0).setContent(topic);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                    case "/sensors/hum01":{
                        myItemList.get(1).setDataValue(mqttMessage.toString());
                        myItemList.get(1).setContent(topic);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }

                //DataReceived.setText(mqttMessage.toString());
                //myItemList.get(0).setDataValue(mqttMessage.toString());
                //DataValue = mqttMessage.toString();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

}
