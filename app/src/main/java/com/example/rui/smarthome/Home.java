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
import android.media.MediaPlayer;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import android.os.Bundle;
import android.os.Looper;

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

import android.widget.Button;

import android.widget.ImageButton;
import android.widget.RadioGroup;

import android.widget.Toast;


import java.io.IOException;

import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import android.os.Handler;

import com.example.rui.server.*;


import android.speech.RecognizerIntent;
import java.util.List;

public class Home extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    WifiManager mainWifiObj;
    static WifiScanReceiver wifiReceiver;
    private static Handler hm;

    //private static final String POINT = "d4:6e:5c:1c:fb:5b";
    private static final String POINT = "e8:94:f6:43:2f:c0";

    Timer timer = new Timer();

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private static String getIp() {
        return "192.168.0.100";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_home);

        mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        Thread t = new Thread() {

            public void run() {
                try {

                    Socket s = new Socket(getIp(), 4444);

                    ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                    Mensagem msg = new Mensagem("Cliente ligado!");
                    dos.writeObject(msg);

                    dos.flush();
                    dos.close();


                    Socket s2 = new Socket(getIp(), 4444);
                    ObjectInputStream dis = new ObjectInputStream(s2.getInputStream());

                    //cria-se uma nova mensagem
                    final Mensagem m = (Mensagem) dis.readObject();

                    Handler handler = new Handler(Looper.getMainLooper());

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Informação da casa recebida!", Toast.LENGTH_LONG).show();
                        }
                    });
                    //Toast.makeText(getApplicationContext(), "RECEBI MENSAGEM", Toast.LENGTH_LONG).show();

                    if(m != null){
                        Log.d("d", "MENSAGEM INICIAL RECEBIDA: " + m);
                        ((MyApplication) getApplication()).setBathHelper(m.getBathHelper());
                        ((MyApplication) getApplication()).setRoomHelper(m.getRoomHelper());
                        ((MyApplication) getApplication()).setKitchenHelper(m.getKitchenHelper());
                        ((MyApplication) getApplication()).setBedroomHelper(m.getBedroomHelper());
                    }
                    s.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        t.start();

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
        final int pos = position;
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

        Thread t = new Thread() {

            public void run() {
                try {
                    Socket s = new Socket(((MyApplication) getApplication()).getIp(), 4444);
                    ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                    Mensagem msg = new Mensagem(pos);
                    dos.writeObject(msg);
                    dos.flush();
                    dos.close();
                    s.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
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
        if (id == R.id.changeMode) {
            if(((MyApplication) getApplication()).getMode()){
                ((MyApplication) getApplication()).setMode(false);
            }else {
                ((MyApplication) getApplication()).setMode(true);
            }
            Thread t = new Thread() {

                public void run() {
                    try {
                        Socket s = new Socket(getIp(), 4444);
                        ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                        Mensagem msg = new Mensagem(((MyApplication) getApplication()).getMode());
                        dos.writeObject(msg);
                        dos.flush();
                        dos.close();
                        s.close();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
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

    @Override
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
                final List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
                /*for(int i=0; i<wifiScanList.size(); i++){
                    Log.d("d", "ponto " + i + ": " + wifiScanList.get(i).BSSID + " com dist: " +
                    calculateDistance(wifiScanList.get(i).level, wifiScanList.get(i).frequency));
                }*/
                double dist = 0.0;

                if(((MyApplication) getApplication()).getSize()==0) {

                    Thread t = new Thread() {

                        public void run() {
                            try {
                                Socket s = new Socket(getIp(), 4444);
                                ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                Mensagem msg = new Mensagem();
                                dos.writeObject(msg);
                                dos.flush();
                                dos.close();
                                s.close();
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    t.start();

                    for (int i = 0; i < wifiScanList.size(); i++) {
                        // ver se encontrei o ponto
                        if (wifiScanList.get(i).BSSID.equals(POINT)) {
                            dist = calculateDistance(wifiScanList.get(i).level, wifiScanList.get(i).frequency);
                            AccessPoint ponto = new AccessPoint(wifiScanList.get(i).BSSID, dist);
                            ((MyApplication) getApplication()).addAccessPoints(ponto);
                            Log.d("g", "ENCONTREI O PONTO com dist: " + dist);
                        }
                    }
                }

                if(((MyApplication) getApplication()).getSize()==1){

                    final double newDist = calculateDistance(wifiScanList.get(0).level, wifiScanList.get(0).frequency);
                    Log.d("s", "ANTIGA DIST A: " + dist + "   NOVA DIST A: " + newDist +"\n");

                    double dif = newDist - dist;
                    if(dif < 0){
                        dif = 0;
                    }

                    if(dif >= 0 && dif <= 20){
                        cases(1);
                    } else if(dif > 20 && dif <= 30) {
                        cases(2);
                    } else if(dif > 30 && dif <= 45) {
                        cases(3);
                    } else if(dif > 45 && dif <= 60){
                        cases(4);
                    } else {
                        cases(0);
                    }

                    Thread t = new Thread() {

                        public void run() {
                            try {
                                Socket s = new Socket(getIp(), 4444);
                                ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                Mensagem msg = new Mensagem(((MyApplication) getApplication()).getList().get(0).getScanResult() ,
                                        ((MyApplication) getApplication()).getList().get(0).getDistance() , newDist);
                                dos.writeObject(msg);
                                dos.flush();
                                dos.close();
                                s.close();
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    t.start();
                }
            }
        }
    }

    public double calculateDistance(double signalLevelInDb, double freqInMHz) {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }

    public class ModeSelect extends  Fragment {

        private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

        View view;
        private String screen_Size = "medium";

        //MediaPlayer mp = MediaPlayer.create(this, );

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

            //if(!((MyApplication) getActivity().getApplication()).getFirst()){
                startVoiceRecognitionActivity();
            //}
            //Toast.makeText(getActivity(), "Que modo deseja?", Toast.LENGTH_LONG).show();

            final RadioGroup radiogroup = (RadioGroup) view.findViewById(R.id.radiogroup);
            final Button buttonradio = (Button) view.findViewById(R.id.RadioButton);
            buttonradio.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int buttonID = radiogroup.getCheckedRadioButtonId();
                    switch (buttonID) {
                        case R.id.manualRadio: {
                            ((MyApplication) getActivity().getApplication()).setMode(false);

                            Thread t = new Thread() {

                                public void run() {
                                    try {
                                        Socket s = new Socket(getIp(), 4444);
                                        ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                        Mensagem msg = new Mensagem(false);
                                        dos.writeObject(msg);
                                        dos.flush();
                                        dos.close();
                                        s.close();
                                    } catch (UnknownHostException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            t.start();

                            Fragment fragment = new HomeView();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, fragment)
                                    .commit();
                            break;
                        }
                        case R.id.modoauto: {
                            ((MyApplication) getActivity().getApplication()).setMode(true);
                            Fragment fragment = new HomeView();
                            Thread t = new Thread() {

                                public void run() {
                                    try {
                                        Socket s = new Socket(getIp(), 4444);
                                        ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                        Mensagem msg = new Mensagem(true);
                                        dos.writeObject(msg);
                                        dos.flush();
                                        dos.close();
                                        s.close();
                                    } catch (UnknownHostException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            t.start();

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

        private void startVoiceRecognitionActivity() {
            ((MyApplication) getActivity().getApplication()).setFirst(true);
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Escolha Modo Manual ou Automático");
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
                ArrayList<String> matches = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                Log.d("z", ""+matches);
                for(int i=0; i < matches.size(); i++){
                    if(matches.get(i).equals("manual") || matches.get(i).equals("modo manual")){
                        ((MyApplication) getActivity().getApplication()).setMode(false);

                        Thread t = new Thread() {

                            public void run() {
                                try {
                                    Socket s = new Socket(getIp(), 4444);
                                    ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                    Mensagem msg = new Mensagem(false);
                                    dos.writeObject(msg);
                                    dos.flush();
                                    dos.close();
                                    s.close();
                                } catch (UnknownHostException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        t.start();

                        Fragment fragment = new HomeView();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                    } else if(matches.get(i).equals("automático") || matches.get(i).equals("modo automático")
                            || matches.get(i).equals("automatico") || matches.get(i).equals("modo automatico")){
                        ((MyApplication) getActivity().getApplication()).setMode(true);
                        Fragment fragment = new HomeView();
                        Thread t = new Thread() {

                            public void run() {
                                try {
                                    Socket s = new Socket(getIp(), 4444);
                                    ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                    Mensagem msg = new Mensagem(true);
                                    dos.writeObject(msg);
                                    dos.flush();
                                    dos.close();
                                    s.close();
                                } catch (UnknownHostException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        t.start();

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                    }
                }
            }

            super.onActivityResult(requestCode, resultCode, data);
        }

//        @Override
//        public void onPause() {
//            try{
//                unregisterReceiver(wifiReceiver);
//                super.onPause();
//            } catch(Exception e){
//
//            }
//        }


    }

    public class HomeView extends Fragment {

        private static final int VOICE_RECOGNITION_REQUEST_CODE = 1235;

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

            if((width>720 && height > 1100) || (height>720 && width > 1100)){
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

            if(!((MyApplication) getActivity().getApplication()).getSecond()){
                startVoiceRecognitionActivity();
            }

            //mNavigationDrawerFragment.getListView().setItemChecked(0,true);
            final ImageButton quarto = (ImageButton) view.findViewById(R.id.quarto);
            //Button quarto = (Button) view.findViewById((R.id.quarto));
            final ImageButton sala = (ImageButton) view.findViewById((R.id.sala));
            final ImageButton cozinha = (ImageButton) view.findViewById((R.id.cozinha));
            final ImageButton wc = (ImageButton) view.findViewById((R.id.wc));

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

        private void startVoiceRecognitionActivity() {
            ((MyApplication) getActivity().getApplication()).setSecond(true);
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            //intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
                // Fill the list view with the strings the recognizer thought it could have heard
                ArrayList<String> matches = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                Log.d("z", ""+matches);
                for(int i=0; i < matches.size(); i++){
                    if(matches.get(i).equals("quarto")){
                        getActivity().getActionBar().setTitle("Bedroom");
                        Fragment fragment = new Bedroom();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                    } else if(matches.get(i).equals("sala")){
                        getActivity().getActionBar().setTitle("Room");
                        Fragment fragment = new Room();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                    } else if(matches.get(i).equals("cozinha")){
                        getActivity().getActionBar().setTitle("Kitchen");
                        Fragment fragment = new Kitchen();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                    } else if(matches.get(i).equals("casa de banho") || matches.get(i).equals("wc")
                            || matches.get(i).equals("banho")){
                        getActivity().getActionBar().setTitle("Bath");
                        Fragment fragment = new Bath();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                    }
                }
            }

            super.onActivityResult(requestCode, resultCode, data);
        }

//        @Override
//        public void onPause() {
//            try{
//                unregisterReceiver(wifiReceiver);
//                super.onPause();
//            } catch(Exception e){
//
//            }
//        }

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
