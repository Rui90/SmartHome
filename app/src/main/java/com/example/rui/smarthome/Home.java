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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import android.os.Handler;

public class Home extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    WifiManager mainWifiObj;
    WifiScanReceiver wifiReceiver;
    //ListView list;
    String[] wifis;
    private static Handler hm;
    private Socket client;
    private PrintWriter printwriter;
    private String messsage;

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

       // MySyncTask asyncTask = new MySyncTask (1,2);
        //asyncTask.execute(1000);
        //hm = new Handler();
        /*hm=new Handler()
        {
            public void handleMessage(Message msg)
            {
                switch(msg.what)
                {
                    case 0:
                        Toast.makeText(getApplicationContext(),"Sincronização efectuada com sucesso",Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(),"Erro na sincronização",Toast.LENGTH_LONG).show();
                        break;
                }

            }
        };*/

        timer.schedule(new RemindTask(), 0, //initial delay
                1 * 3000);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        Fragment fragment = new ModeSelect();
        //int position = mNavigationDrawerFragment.getListView().getSelectedItemPosition();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();


    }

    public static Handler GetHandler(){
        return hm;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            //CENA PERIODICA A EXECUTAR

            //volta a chamar este handler, dizendo que vai executar ao fim de 3000ms
            //handler.postDelayed(this, 3000);
        }
    };

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
            if ( ((MyApplication) getApplication()).getMode()) {
             //   Log.d("entrei", "mode: " + mode);
                List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
                wifis = new String[wifiScanList.size()];

                double minDist;
                int aux = 0; // é uma variavel que vai tratr d mudança de página
                int break_aux = 0; // uma variavel que vai fazer com que nao se tenha que percorrer toda a lista de wifi... sempre que encontrarmos na wifiScanList um accessPoint igual a
                // uma das nossas guardadas, aumenta, quando chegar as 4 é porque ja comparamos com todos e nao vale a pena continuar
                if (((MyApplication) getApplication()).getSize() == 4) {
                   // Log.d("entrei", "mode2: " + mode);
                    minDist = ((MyApplication) getApplication()).getAccessPoint(0).getDistance();
                    for (int i = 0; i < wifiScanList.size() && break_aux < 4; i++) {
                        for (int j = 0; j < ((MyApplication) getApplication()).getSize(); j++) {
                            if (wifiScanList.get(i).BSSID.equals(((MyApplication) getApplication()).getAccessPoint(j).getScanResult())) {
                                break_aux++;
                                double newDist = calculateDistance(wifiScanList.get(i).level,
                                        wifiScanList.get(i).frequency);
                                double AccessPointDist = ((MyApplication) getApplication()).getAccessPoint(j).getDistance();
                                if (AccessPointDist < minDist) {
                                  //  Log.d("entrei", "mode3: " + mode);
                                    minDist = AccessPointDist;
                                    if (newDist < minDist) {
                                        Log.d("entrei", "nunca ca entro e nao sei porque!");
                                        minDist = newDist;
                                        aux = i;
                                        Log.d("entrei", "valor aux: " + aux + " valor de minDist: " + minDist);
                                    }
                                }
                            }
                        }
                    }
                    cases(aux);
                }

                if (wifiScanList.size() >= 4 && ((MyApplication) getApplication()).getSize() == 0) {
                    for (int i = 0; i < 4; i++) {
                        double value = calculateDistance(wifiScanList.get(i).level,
                                wifiScanList.get(i).frequency);
                        AccessPoint ponto = new AccessPoint(wifiScanList.get(i).BSSID, value);
                        ((MyApplication) getApplication()).addAccessPoints(ponto);

                        wifis[i] = ((wifiScanList.get(i)).BSSID + "\n" +
                                "Level: " + wifiScanList.get(i).level + "\n" +
                                "Frequency: " + wifiScanList.get(i).frequency +
                                "\n" + "Distance: " + value + "\n");
                    }

                    messsage = "Ponto 1: " + ((MyApplication) getApplication()).getAccessPoint(0).getScanResult()
                            + "   Distancia: " + ((MyApplication) getApplication()).getAccessPoint(0).getDistance()
                            + "-Ponto 2: " + ((MyApplication) getApplication()).getAccessPoint(1).getScanResult()
                            + "   Distancia: " + ((MyApplication) getApplication()).getAccessPoint(1).getDistance()
                            + "-Ponto 3: " + ((MyApplication) getApplication()).getAccessPoint(2).getScanResult()
                            + "   Distancia: " + ((MyApplication) getApplication()).getAccessPoint(2).getDistance()
                            +  "-Ponto 4: " + ((MyApplication) getApplication()).getAccessPoint(3).getScanResult()
                            + "   Distancia: " + ((MyApplication) getApplication()).getAccessPoint(3).getDistance();

                    //Log.d("tag", messsage);
                    SendMessage sendMessageTask = new SendMessage();
                    sendMessageTask.execute();
                }
            }
        }
    }

    public double calculateDistance(double signalLevelInDb, double freqInMHz) {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }

    public static class ModeSelect extends  Fragment {

        View view;
        private String screen_Size = "medium";

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            if((width>720 && height > 1100)){
                screen_Size = "large";
            }

            if(screen_Size.equals("large")){
                view = inflater.inflate(R.layout.mainfragment_large, container, false);
            } else{
                view = inflater.inflate(R.layout.mainfragment, container, false);
            }

            final RadioGroup radiogroup = (RadioGroup) view.findViewById(R.id.radiogroup);
            final Button buttonradio = (Button) view.findViewById(R.id.RadioButton);
            buttonradio.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int buttonID = radiogroup.getCheckedRadioButtonId();
                    switch (buttonID) {
                        case R.id.manualRadio: {
                            ((MyApplication) getActivity().getApplication()).setMode(false);
                            Toast.makeText(getActivity().getApplicationContext(), "false", Toast.LENGTH_LONG).show();
                            Fragment fragment = new HomeView();
                            //int position = mNavigationDrawerFragment.getListView().getSelectedItemPosition();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, fragment)
                                    .commit();
                            break;
                        }
                        case R.id.modoauto: {
                            ((MyApplication) getActivity().getApplication()).setMode(true);
                            Fragment fragment = new HomeView();
                            Toast.makeText(getActivity().getApplicationContext(), "true", Toast.LENGTH_LONG).show();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, fragment)
                                    .commit();
                            break;
                        }
                    }
                }
            });
            return view;
        }

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



    String advice = "";

    class MySyncTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                Socket s = new Socket("192.168.152.1", 8080);
                InputStreamReader streamReader = new InputStreamReader(s.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);

                String advice = reader.readLine();
                reader.close();
                Log.d("msg", advice);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            Log.d("msg", "Cool");
        }

        protected void onPreExecute() {
            //log.i("start");
        }
    }


    private class SendMessage extends AsyncTask<Void, Void, Void> {
        private Socket client;
        ObjectOutputStream toServer = null;
        ObjectInputStream fromServer = null;

        @Override
        protected Void doInBackground(Void... params) {
            try {

                client = new Socket("192.168.0.101", 4444); // connect to the server

                toServer = new ObjectOutputStream(client.getOutputStream());
                fromServer = new ObjectInputStream(client.getInputStream());

               // if(params[0].request.equals("GET")){
                 toServer.writeObject("Fuckoff!");
                try {
                    if(fromServer.readObject() != null) {
                        Toast.makeText(getApplicationContext(), "Sim não é null", Toast.LENGTH_LONG).show();
                        Log.d("abc", "nao sou null, sou fixe!");
                    }

                }catch(ClassNotFoundException e) {

                }
                //}
                printwriter = new PrintWriter(client.getOutputStream(), true);

                printwriter.write(messsage); // write the message to output stream

                printwriter.flush();
                printwriter.close();
                client.close(); // closing the connection

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
