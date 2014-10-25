package com.example.rui.smarthome;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.*;

public class Home extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    WifiManager mainWifiObj;
    WifiScanReceiver wifiReceiver;
    //ListView list;
    private static MyApplication accesspoints = new MyApplication();
    private List<AccessPoint> accessPointsList = new ArrayList<AccessPoint>();
    String[] wifis;

    Timer timer = new Timer();

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_home);

        mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        timer.schedule(new RemindTask(), 0, //initial delay
                1 * 3000);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        Fragment fragment = new HomeView();
        //int position = mNavigationDrawerFragment.getListView().getSelectedItemPosition();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    class RemindTask extends TimerTask {
        public void run() {
                wifiReceiver = new WifiScanReceiver();
                mainWifiObj.startScan();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        cases(position);
    }

    private void cases(int position) {
        switch(position) {
            case 0: {
                Fragment fragment = new HomeView();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
            }
            case 1: {
                Fragment fragment = new Room();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
            }
            case 2:
            {
                Fragment fragment = new Bedroom();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
            }
            case 3:
            {
                Fragment fragment = new Kitchen();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
            }
            case 4: {
                Fragment fragment = new Bath();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
            }
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.home);
                break;
            case 1:
                mTitle = getString(R.string.dinnerroom);
                break;
            case 2:
                mTitle = getString(R.string.bedroom);
                break;
            case 3:
                mTitle = getString(R.string.kitchen);
            break;
            case 4:
                mTitle = getString(R.string.bath);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        List l = fragmentManager.getFragments();
        if (l == null) {
            finish();
        } else {
            String[] parts = l.get(l.size() - 1).toString().split("\\{");
            if (parts[0].equals("Home")) {
                finish();
            } else {
                Fragment fragment = new HomeView();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            }
        }
    }

    @Override
    public void onStop(){
        try{
            unregisterReceiver(wifiReceiver);
            super.onStop();
        } catch(Exception e){

        }
        finish();
    }

    protected void onPause() {
        try{
            unregisterReceiver(wifiReceiver);
            super.onPause();
        } catch(Exception e){

        }
    }

    protected void onResume() {
        registerReceiver(wifiReceiver, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            wifis = new String[wifiScanList.size()];

            Map<String, Double> mapa = new HashMap<String, Double>();
            double minDist;
            int aux = 0; // é uma variavel que vai tratr d mudança de página
            int break_aux = 0; // uma variavel que vai fazer com que nao se tenha que percorrer toda a lista de wifi... sempre que encontrarmos na wifiScanList um accessPoint igual a
            // uma das nossas guardadas, aumenta, quando chegar as 4 é porque ja comparamos com todos e nao vale a pena continuar
            if(accessPointsList.size() == 4){
                minDist = accessPointsList.get(0).getDistance();
                for(int i = 0; i < wifiScanList.size() && break_aux < 4; i++){
                    for(int j = 0; j < accessPointsList.size(); j++){
                        if(wifiScanList.get(i).SSID.equals(accessPointsList.get(j).getScanResult())){
                            break_aux++;
                            double newDist = calculateDistance(wifiScanList.get(i).level,
                                    wifiScanList.get(i).frequency);
                            double AccessPointDist = accessPointsList.get(j).getDistance();
                            if(AccessPointDist < minDist){
                                minDist = AccessPointDist;
                                if(newDist < minDist){
                                    minDist = newDist;
                                    aux = i;
                                    Log.d("entrei", "valor aux: " + aux + " valor de minDist: " + minDist);
                                }
                            }
                        }
                    }
                }
                //cases(aux);
            }

           /* String str;
            Double min = 0.0;
            for(Map.Entry<String,Double> e:mapa.entrySet()){
                if(min.compareTo(e.getValue())>0){
                    str = e.getKey();
                    min = e.getValue();
                    Toast.makeText(getApplicationContext(), "A aproximar de: " + str,
                            Toast.LENGTH_LONG).show();
                }
            }*/

            if(wifiScanList.size() >= 4 && accessPointsList.size() == 0 ) {

                for (int i = 0; i < 4; i++) {

                    double value = calculateDistance(wifiScanList.get(i).level,
                            wifiScanList.get(i).frequency);
                    AccessPoint ponto = new AccessPoint(wifiScanList.get(i).SSID, value);
                    accessPointsList.add(ponto);

                    wifis[i] = ((wifiScanList.get(i)).SSID + "\n" +
                            "Level: " + wifiScanList.get(i).level + "\n" +
                            "Frequency: " + wifiScanList.get(i).frequency +
                            "\n" + "Distance: " + value + "\n");
                }
                /*ListView list = (ListView) findViewById(R.id.listView1);
                list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, wifis));*/

            }
        }
    }

    public double calculateDistance(double signalLevelInDb, double freqInMHz) {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }

    public static class HomeView extends Fragment {

        private String screen_Size = "medium";
        View view;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            // Inflate the layout for this fragment

            if((width>720 && height > 1100)){
                screen_Size = "large";
            }

            // verificar consoante a orientaçao qual o layout correcto para um device medium

            if((getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().
                    heightPixels) && screen_Size.equals("medium"))
            {
                view = inflater.inflate(R.layout.fragment_home_land, container, false);
            }
            else if((getResources().getDisplayMetrics().widthPixels<getResources().getDisplayMetrics().
                    heightPixels) && screen_Size.equals("medium"))
            {
                view = inflater.inflate(R.layout.fragment_home, container, false);
            }

            // verificar consoante a orientaçao qual o layout correcto para um device large

            if((getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().
                    heightPixels) && screen_Size.equals("large"))
            {
                view = inflater.inflate(R.layout.home_large_land, container, false);
            }
            else if((getResources().getDisplayMetrics().widthPixels<getResources().getDisplayMetrics().
                    heightPixels) && screen_Size.equals("large"))
            {
                view = inflater.inflate(R.layout.home_large, container, false);
            }

            //mNavigationDrawerFragment.getListView().setItemChecked(0,true);
            Button quarto = (Button) view.findViewById((R.id.quarto));
            Button sala = (Button) view.findViewById((R.id.sala));
            Button cozinha = (Button) view.findViewById((R.id.cozinha));
            Button wc = (Button) view.findViewById((R.id.wc));

            getActivity().getActionBar().setTitle("Home");

            quarto.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getActivity().getActionBar().setTitle("Bedroom");
                    Fragment fragment = new Bedroom();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

            sala.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getActivity().getActionBar().setTitle("Room");
                    Fragment fragment = new Room();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

            cozinha.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getActivity().getActionBar().setTitle("Kitchen");
                    Fragment fragment = new Kitchen();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

            wc.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getActivity().getActionBar().setTitle("Bath");
                    Fragment fragment = new Bath();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

            return view;
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            newFrag();
        }

        public void newFrag(){
            Fragment fragment = new HomeView();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Home) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
