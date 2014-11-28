package com.example.rui.smarthome;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.example.rui.server.BathHelper;
import com.example.rui.server.Mensagem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Rui on 10/10/2014.
 */
public class Bath extends Fragment {

    View view;
    int x = 0;
    private String screen_Size = "medium";

  /*  private Socket client;
    private PrintWriter printwriter;
    private String messsage;*/

    private static final int BATH = 4;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        if((width>720 && height > 1100) || (height>720 && width > 1100)){
            screen_Size = "large";
        }

        if((getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().
                heightPixels) && screen_Size.equals("medium"))
        {
            view = inflater.inflate(R.layout.bath_layout_land, container, false);
        }
        else if((getResources().getDisplayMetrics().widthPixels<getResources().getDisplayMetrics().
                heightPixels) && screen_Size.equals("medium"))
        {
            view = inflater.inflate(R.layout.bath_layout, container, false);
        }

        if((getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().
                heightPixels) && screen_Size.equals("large"))
        {
            view = inflater.inflate(R.layout.bath_large_land, container, false);
        }
        else if((getResources().getDisplayMetrics().widthPixels<getResources().getDisplayMetrics().
                heightPixels) && screen_Size.equals("large"))
        {
            view = inflater.inflate(R.layout.bath_layout_large, container, false);
        }

        Toast.makeText(getActivity().getApplicationContext(), "BATH!", Toast.LENGTH_SHORT).show();

        receiveMessage();

        lightButton();

        final SeekBar waterQuantity = (SeekBar) view.findViewById(R.id.seekBar);
        waterQuantity.setMax(100);
        waterQuantity.setLeft(0);
        waterQuantity.incrementProgressBy(((MyApplication) getActivity().getApplication()).getBathHelper().getQuantity());
        //waterQuantity.setProgress(0);
        final TextView value = (TextView) view.findViewById(R.id.textView);
        value.setText(Integer.toString(((MyApplication) getActivity().getApplication()).getBathHelper().getQuantity()));

        waterQuantity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                value.setText(Integer.toString(progress));
                ((MyApplication) getActivity().getApplication()).getBathHelper().setQuantity(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                Thread t = new Thread() {

                    public void run() {
                        try {
                            Socket s = new Socket("192.168.0.101", 4444);
                            Mensagem m = new Mensagem(BATH, ((MyApplication) getActivity().getApplication()).getBathHelper());
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

        final SeekBar waterTemperature = (SeekBar) view.findViewById(R.id.seekBar2);
        waterTemperature.setMax(45);
        waterTemperature.setLeft(5);
        waterTemperature.incrementProgressBy(((MyApplication) getActivity().getApplication()).getBathHelper().getTemperature());
        //waterTemperature.setProgress(0);
        final TextView value2 = (TextView) view.findViewById(R.id.textView2);
        value2.setText(Integer.toString(((MyApplication) getActivity().getApplication()).getBathHelper().getTemperature()));

        waterTemperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                value2.setText(Integer.toString(progress));
                ((MyApplication) getActivity().getApplication()).getBathHelper().setTemperature(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Thread t = new Thread() {

                    public void run() {
                        try {
                            Socket s = new Socket("192.168.0.101", 4444);
                            Mensagem m = new Mensagem(BATH, ((MyApplication) getActivity().getApplication()).getBathHelper());
                            ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
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
                                if(!((MyApplication) getActivity().getApplication()).getBathHelper().isLight()){
                                    ((MyApplication) getActivity().getApplication()).getBathHelper().setLight(true);
                                    Mensagem msg = new Mensagem(BATH, ((MyApplication) getActivity().getApplication()).getBathHelper());
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "LUZ LIGADA");
                                        }
                                    });
                                } else {
                                    ((MyApplication) getActivity().getApplication()).getBathHelper().setLight(false);
                                    Mensagem msg = new Mensagem(BATH, ((MyApplication) getActivity().getApplication()).getBathHelper());
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "LUZ DESLIGADA");
                                        }
                                    });
                                }
                                x++;
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

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                Thread t = new Thread() {

                    public void run() {
                    try {
                        Socket s = new Socket("192.168.0.100", 4444);
                        DataOutputStream dos = new DataOutputStream((s.getOutputStream()));
                        if(x%2==0){
                            dos.writeUTF("LUZ LIGADA");
                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    showToast(view.getContext(), "LUZ LIGADA");
                                }
                            });
                        } else {
                            dos.writeUTF("LUZ DESLIGADA");
                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    showToast(view.getContext(), "LUZ DESLIGADA");
                                }
                            });
                        }
                        x++;
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
        });*/
    }

    public void newFrag(){
        Fragment fragment = new Bath();
        FragmentManager fragmentManager = getFragmentManager();
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
                                showToast(getActivity().getApplicationContext(), msg);
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

    private void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }



    /*public void showToast2(final String toast)
    {
        try{
            getActivity().runOnUiThread(new Runnable() {
                public void run()
                {
                    Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch(NullPointerException e){

        }

    }*/
}
