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
import android.widget.Toast;

import com.example.rui.server.Mensagem;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import static android.widget.Toast.makeText;

/**
 * Created by Rui on 10/10/2014.
 */
public class Room extends Fragment {

    View view;

    private String screen_Size = "medium";

    private Socket client;
    private PrintWriter printwriter;
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static InputStreamReader inputStreamReader;
    private static BufferedReader bufferedReader;
    private static final int ROOM = 1;

    private static final int WINDOW = 10;
    private static final int LIGHT = 11;
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

        if((width>720 && height > 1100) || (height>720 && width>1100)){
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

        receiveMessage();

        lightButton();

        windowButton();

        tvButton();

        final Switch arcondicionadoOnOff = (Switch) view.findViewById(R.id.arcondicionado);
        arcondicionadoOnOff.setChecked(((MyApplication) getActivity().getApplication()).getRoomHelper().isArcondicionado());
        final SeekBar arcondicionado = (SeekBar) view.findViewById(R.id.seekBar);
        final TextView value = (TextView) view.findViewById(R.id.textView2);

        arcondicionado.setMax(40);
        arcondicionado.setLeft(0);
        arcondicionado.incrementProgressBy(((MyApplication) getActivity().getApplication()).getRoomHelper().getTemperatureArCond());

        //arcondicionadoOnOff.setChecked(((MyApplication) getActivity().getApplication()).getRoomHelper().isArcondicionado());
        arcondicionado.setEnabled(((MyApplication) getActivity().getApplication()).getRoomHelper().isArcondicionado());
        value.setText(Integer.toString(((MyApplication) getActivity().getApplication()).getRoomHelper().getTemperatureArCond()));

        arcondicionadoOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                arcondicionado.setMax(40);
                //arcondicionado.setLeft(0);
                arcondicionado.incrementProgressBy(1);
                arcondicionado.setProgress(((MyApplication) getActivity().getApplication()).getRoomHelper().getTemperatureArCond());
                arcondicionado.setEnabled(((MyApplication) getActivity().getApplication()).getRoomHelper().isArcondicionado());
                ((MyApplication) getActivity().getApplication()).getRoomHelper().setArcondicionado(b);

                if(((MyApplication) getActivity().getApplication()).getRoomHelper().isArcondicionado()){
                    arcondicionado.setEnabled(((MyApplication) getActivity().getApplication()).getRoomHelper().isArcondicionado());
                    ((MyApplication) getActivity().getApplication()).getRoomHelper().setArcondicionado(true);

                    Thread t = new Thread() {

                        public void run() {
                            try {
                                Socket s = new Socket(((MyApplication) getActivity().getApplication()).getIp(), 4444);
                                ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                if(((MyApplication) getActivity().getApplication()).getRoomHelper().isArcondicionado()){
                                    ((MyApplication) getActivity().getApplication()).getRoomHelper().setArcondicionado(true);
                                    Mensagem msg = new Mensagem(ROOM, ((MyApplication) getActivity().getApplication()).getRoomHelper());
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "Ar condicionado ligado!");
                                        }
                                    });
                                } else {
                                    ((MyApplication) getActivity().getApplication()).getRoomHelper().setArcondicionado(false);
                                    Mensagem msg = new Mensagem(ROOM, ((MyApplication) getActivity().getApplication()).getRoomHelper());
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "Ar condicionado desligado!");
                                        }
                                    });
                                }
                                dos.flush();
                                dos.close();
                                s.close();
                            } catch (UnknownHostException e) {

                            } catch (IOException e) {

                            }
                        }
                    };
                    t.start();

                    arcondicionado.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            value.setText(Integer.toString(progress));
                            ((MyApplication) getActivity().getApplication()).getRoomHelper().setTemperatureArCond(progress);
                            Log.d("g", ""+((MyApplication) getActivity().getApplication()).getRoomHelper().getTemperatureArCond());
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            Thread t = new Thread() {

                                public void run() {
                                    try {
                                        Socket s = new Socket(((MyApplication) getActivity().getApplication()).getIp(), 4444);
                                        Mensagem m = new Mensagem(ROOM, ((MyApplication) getActivity().getApplication()).getRoomHelper());
                                        ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                        dos.writeObject(m);
                                        dos.flush();
                                        dos.close();
                                        s.close();
                                    } catch (UnknownHostException e) {

                                    } catch (IOException e) {

                                    }
                                }
                            };
                            t.start();
                        }

                    });
                } else {
                    ((MyApplication) getActivity().getApplication()).getRoomHelper().setArcondicionado(false);
                    ((MyApplication) getActivity().getApplication()).getRoomHelper().setTemperatureArCond(0);
                    arcondicionado.setProgress(0);
                    arcondicionado.setMax(0);
                    arcondicionado.setLeft(0);
                    arcondicionado.incrementProgressBy(0);
                    value.setText(" ");

                    Thread t = new Thread() {

                        public void run() {
                            try {
                                Socket s = new Socket(((MyApplication) getActivity().getApplication()).getIp(), 4444);
                                ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                if(((MyApplication) getActivity().getApplication()).getRoomHelper().isArcondicionado()){
                                    ((MyApplication) getActivity().getApplication()).getRoomHelper().setArcondicionado(true);
                                    Mensagem msg = new Mensagem(ROOM, ((MyApplication) getActivity().getApplication()).getRoomHelper());
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "Ar condicionado ligado");
                                        }
                                    });
                                } else {
                                    ((MyApplication) getActivity().getApplication()).getRoomHelper().setArcondicionado(false);
                                    Mensagem msg = new Mensagem(ROOM, ((MyApplication) getActivity().getApplication()).getRoomHelper());
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "Ar condicionado desligado");
                                        }
                                    });
                                }
                                dos.flush();
                                dos.close();
                                s.close();
                            } catch (UnknownHostException e) {

                            } catch (IOException e) {

                            }
                        }
                    };
                    t.start();
                }
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
                    Thread t = new Thread() {

                        public void run() {
                            try {
                                Socket s = new Socket(((MyApplication) getActivity().getApplication()).getIp(), 4444);
                                ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                if(!((MyApplication) getActivity().getApplication()).getRoomHelper().isLight()){
                                    ((MyApplication) getActivity().getApplication()).getRoomHelper().setLight(true);
                                    Mensagem msg = new Mensagem(ROOM, ((MyApplication) getActivity().getApplication()).getRoomHelper());
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "Luz ligada");
                                        }
                                    });
                                } else {
                                    ((MyApplication) getActivity().getApplication()).getRoomHelper().setLight(false);
                                    Mensagem msg = new Mensagem(ROOM, ((MyApplication) getActivity().getApplication()).getRoomHelper());
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "Luz desligada");
                                        }
                                    });
                                }
                                dos.flush();
                                dos.close();
                                s.close();
                            } catch (UnknownHostException e) {

                            } catch (IOException e) {

                            }
                        }
                    };
                    t.start();
                    button.setBackgroundColor(Color.LTGRAY);
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    button.setBackgroundColor(Color.WHITE);
                }

                return true;
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
                    Thread t = new Thread() {

                        public void run() {
                            try {
                                Socket s = new Socket(((MyApplication) getActivity().getApplication()).getIp(), 4444);
                               // ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                if(!((MyApplication) getActivity().getApplication()).getRoomHelper().isWindow()){
                                    ((MyApplication) getActivity().getApplication()).getRoomHelper().setWindow(true);
                                    Mensagem msg = new Mensagem(ROOM, ((MyApplication) getActivity().getApplication()).getRoomHelper());
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "Janela aberta");
                                        }
                                    });
                                } else {
                                    ((MyApplication) getActivity().getApplication()).getRoomHelper().setWindow(false);
                                    Mensagem msg = new Mensagem(ROOM, ((MyApplication) getActivity().getApplication()).getRoomHelper());
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "Janela fechada");
                                        }
                                    });
                                }
                                dos.flush();
                                dos.close();
                                s.close();
                            } catch (UnknownHostException e) {

                            } catch (IOException e) {

                            }
                        }
                    };
                    t.start();
                    button.setBackgroundColor(Color.LTGRAY);
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    button.setBackgroundColor(Color.WHITE);
                }

                return true;
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
                    Thread t = new Thread() {

                        public void run() {
                            try {
                                Socket s = new Socket(((MyApplication) getActivity().getApplication()).getIp(), 4444);
                                ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                if(!((MyApplication) getActivity().getApplication()).getRoomHelper().isTv()){
                                    ((MyApplication) getActivity().getApplication()).getRoomHelper().setTv(true);
                                    Mensagem msg = new Mensagem(ROOM, ((MyApplication) getActivity().getApplication()).getRoomHelper());
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "Tv ligada");
                                        }
                                    });
                                } else {
                                    ((MyApplication) getActivity().getApplication()).getRoomHelper().setTv(false);
                                    Mensagem msg = new Mensagem(ROOM, ((MyApplication) getActivity().getApplication()).getRoomHelper());
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "Tv desligada");
                                        }
                                    });
                                }
                                dos.flush();
                                dos.close();
                                s.close();
                            } catch (UnknownHostException e) {

                            } catch (IOException e) {

                            }
                        }
                    };
                    t.start();
                    tvbutton.setBackgroundColor(Color.LTGRAY);
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    tvbutton.setBackgroundColor(Color.WHITE);
                }

                return true;
            }
        });
    }

    private void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public void newFrag(){
        Fragment fragment = new Room();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void receiveMessage(){

        Thread t = new Thread(){

            public void run(){
                try{
                    ServerSocket ss = new ServerSocket(4444);
                    while(true){
                        Socket s = ss.accept();
                        ObjectInputStream dis = new ObjectInputStream(s.getInputStream());
                        final String msg = dis.readUTF();
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                showToast(view.getContext(), msg);
                            }
                        });
                        dis.close();
                        s.close();
                    }
                }
                catch(IOException e){

                }
            }
        };
        t.start();
    }

    /*private class SendMessage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {

                client = new Socket("192.168.0.101", 4444); // connect to the server
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

    }*/
}
