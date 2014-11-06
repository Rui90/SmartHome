package com.example.rui.smarthome;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import static android.widget.Toast.makeText;

/**
 * Created by Rui on 10/10/2014.
 */
public class Room extends Fragment {

    //Camera cam = Camera.open();
    View view;
    //final Camera.Parameters p = cam.getParameters();
    int x = 0;
    int y = 0;
    int z = 0;
    private String screen_Size = "medium";

    private Socket client;
    private PrintWriter printwriter;
    private String messsage;
    private static MyApplication myApplication = new MyApplication(false, 0);
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static InputStreamReader inputStreamReader;
    private static BufferedReader bufferedReader;
    private Switch arcondicionadoOnOff;

    // metodo para mostrar o que vai aparecer na criacao
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        if((width>720 && height > 1100)){
            screen_Size = "large";
        }

        if((getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().
                heightPixels) && screen_Size.equals("medium"))
        {
            view = inflater.inflate(R.layout.dinnerroom_layout_land, container, false);
        }
        else if((getResources().getDisplayMetrics().widthPixels<getResources().getDisplayMetrics().
                heightPixels) && screen_Size.equals("medium"))
        {
            view = inflater.inflate(R.layout.dinnerroom_layout, container, false);
        }

        if((getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().
                heightPixels) && screen_Size.equals("large"))
        {
            view = inflater.inflate(R.layout.dinnerroom_large_land, container, false);
        }
        else if((getResources().getDisplayMetrics().widthPixels<getResources().getDisplayMetrics().
                heightPixels) && screen_Size.equals("large"))
        {
            view = inflater.inflate(R.layout.dinnerroom_large_layout, container, false);
        }

        lightButton();

        windowButton();

        tvButton();

        arcondicionadoOnOff = (Switch) view.findViewById(R.id.arcondicionado);
        final SeekBar arcondicionado = (SeekBar) view.findViewById(R.id.seekBar);
        final TextView value = (TextView) view.findViewById(R.id.textView2);
        arcondicionado.setMax(40);
        arcondicionado.setLeft(0);
        arcondicionado.incrementProgressBy(myApplication.getRoomACValue());
        value.setText(Integer.toString(myApplication.getRoomACValue()));
        arcondicionadoOnOff.setChecked(myApplication.getRoomACState());
        arcondicionado.setEnabled(myApplication.getRoomACState());

        arcondicionadoOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                arcondicionado.setMax(40);
                //arcondicionado.setLeft(0);
                arcondicionado.incrementProgressBy(1);
                arcondicionado.setProgress(myApplication.getRoomACValue());
                arcondicionado.setEnabled(myApplication.getRoomACState());
                myApplication.setRoomACState(b);

                if(myApplication.getRoomACState()){
                    arcondicionado.setEnabled(myApplication.getRoomACState());
                    messsage = "Ligar arcondicionado";
                    SendMessage sendMessageTask = new SendMessage();
                    sendMessageTask.execute();

                    arcondicionado.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            value.setText(Integer.toString(progress));
                            myApplication.setRoomACValue(progress);

                            messsage = "Temperatura do ar condicionado na sala: " + Integer.toString(progress);
                            SendMessage sendMessageTask = new SendMessage();
                            sendMessageTask.execute();
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }

                    });
                } else {
                    myApplication.setRoomACState(false);
                    myApplication.setRoomACValue(0);
                    arcondicionado.setProgress(0);
                    arcondicionado.setMax(0);
                    arcondicionado.setLeft(0);
                    arcondicionado.incrementProgressBy(0);
                    value.setText(" ");

                    messsage = "Desligar arcondicionado";
                    SendMessage sendMessageTask = new SendMessage();
                    sendMessageTask.execute();
                }
            }
        });

        arcondicionado.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(myApplication.getRoomACState()){
                    value.setText(Integer.toString(progress));
                    myApplication.setRoomACValue(progress);

                    messsage = "Temperatura do ar condicionado na sala: " + Integer.toString(progress);
                    SendMessage sendMessageTask = new SendMessage();
                    sendMessageTask.execute();
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });




        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        newFrag();
    }

    public void lightButton(){
        final ImageButton button = (ImageButton) view.findViewById(R.id.lampada);
        button.setBackgroundColor(Color.WHITE);
        button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    button.setBackgroundColor(Color.LTGRAY);
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    button.setBackgroundColor(Color.WHITE);
                }

                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(x%2==0){
                    messsage = "Ligar luz";
                    SendMessage sendMessageTask = new SendMessage();
                    sendMessageTask.execute();
                } else {
                    messsage = "Desligar luz";
                    SendMessage sendMessageTask = new SendMessage();
                    sendMessageTask.execute();
                }
                x++;
            }
        });
    }

    public void windowButton(){
        final ImageButton button = (ImageButton) view.findViewById(R.id.imageButton2);
        button.setBackgroundColor(Color.WHITE);
        button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    button.setBackgroundColor(Color.LTGRAY);
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    button.setBackgroundColor(Color.WHITE);
                }

                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(y%2==0){
                    messsage = "Abrir janela";
                    SendMessage sendMessageTask = new SendMessage();
                    sendMessageTask.execute();
                } else {
                    messsage = "Fechar janela";
                    SendMessage sendMessageTask = new SendMessage();
                    sendMessageTask.execute();
                }
                y++;
            }
        });
    }

    public void tvButton(){
        final ImageButton tvbutton = (ImageButton) view.findViewById(R.id.tvButton);
        tvbutton.setBackgroundColor(Color.WHITE);
        tvbutton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tvbutton.setBackgroundColor(Color.LTGRAY);
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    tvbutton.setBackgroundColor(Color.WHITE);
                }

                return true;
            }
        });

        tvbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(z%2==0){
                    messsage = "Ligar tv";
                    SendMessage sendMessageTask = new SendMessage();
                    sendMessageTask.execute();
                } else {
                    messsage = "Desligar tv";
                    SendMessage sendMessageTask = new SendMessage();
                    sendMessageTask.execute();
                }
                z++;
            }
        });
    }

    public void newFrag(){
        Fragment fragment = new Room();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private class SendMessage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {

                client = new Socket("192.168.0.100", 4444); // connect to the server
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
