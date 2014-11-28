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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rui.server.Mensagem;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rui on 10/10/2014.
 */
public class Kitchen extends Fragment {

    //Camera cam = Camera.open();
    View view;
    //final Camera.Parameters p = cam.getParameters();
    int x = 0;
    int y = 0;
    private String screen_Size = "medium";

    private Socket client;
    private PrintWriter printwriter;
    private String messsage;

    private static MyApplication myApplication = new MyApplication(0, false, "");
    private static MyApplication myApplication2 = new MyApplication(false, 0, "", "");

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static InputStreamReader inputStreamReader;
    private static BufferedReader bufferedReader;
    private static final int KITCHEN = 3;

    private static final int WINDOW = 10;
    private static final int LIGHT = 11;

    // metodo para mostrar o que vai aparecer na criacao
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
            view = inflater.inflate(R.layout.kitchen_layout_land, container, false);
        }
        else if((getResources().getDisplayMetrics().widthPixels<getResources().getDisplayMetrics().
                heightPixels) && screen_Size.equals("medium"))
        {
            view = inflater.inflate(R.layout.kitchen_layout, container, false);
        }

        if((getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().
                heightPixels) && screen_Size.equals("large"))
        {
            view = inflater.inflate(R.layout.kitchen_large_land, container, false);
        }
        else if((getResources().getDisplayMetrics().widthPixels<getResources().getDisplayMetrics().
                heightPixels) && screen_Size.equals("large"))
        {
            view = inflater.inflate(R.layout.kitchen_layout_large, container, false);
        }

        lightButton();

        windowButton();

        //final Switch arcondicionadoOnOff = (Switch) view.findViewById(R.id.arcondicionado);
        final Switch microwave = (Switch) view.findViewById(R.id.microwave);
        final Switch forno = (Switch) view.findViewById(R.id.forno);
        final Button microwave_button = (Button) view.findViewById(R.id.mode);
        final Button forno_button = (Button) view.findViewById(R.id.set2);
        final Chronometer chronometer = (Chronometer) view.findViewById(R.id.chronometer);
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinnerKitchen);
        //final TextView temperatur = (TextView) view.findViewById(R.id.temp);
        final SeekBar fornoseek = (SeekBar) view.findViewById(R.id.seekBar3);
        //final SeekBar arcondicionado = (SeekBar) view.findViewById(R.id.seekBar);
        final TextView value = (TextView) view.findViewById(R.id.textView2);

        microwave.setChecked(myApplication2.isMicrowave());

        fornoseek.setMax(250);
        fornoseek.setLeft(0);
        fornoseek.incrementProgressBy(myApplication.getKitchen_forno());
        value.setText(Integer.toString(myApplication.getKitchen_forno()));
        forno.setChecked(myApplication.getForno());
        fornoseek.setEnabled(myApplication.getForno());

        List<String> list = new ArrayList<String>();
        list.add("Descongelar");
        list.add("Temperatura baixa");
        list.add("Temperatura media");
        list.add("Temperatura alta");
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        microwave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                myApplication2.setMicrowave(b);

                if(myApplication2.isMicrowave()) {
                    messsage = "MICROONDAS LIGADO";
                    Thread t = new Thread() {

                        public void run() {
                            try {
                                Socket s = new Socket("192.168.0.100", 4444);
                                DataOutputStream dos = new DataOutputStream((s.getOutputStream()));
                                dos.writeUTF(messsage);
                                view.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showToast(view.getContext(), messsage);
                                    }
                                });
                                dos.flush();
                                dos.close();
                                s.close();
                            } catch (UnknownHostException e) {

                            } catch (IOException e) {

                            }
                        }
                    };
                    t.start();

                    microwave_button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if(microwave.isChecked()){
                                messsage = "MICROONDAS NO MODO: " +
                                        spinner.getSelectedItem().toString();
                                Thread t = new Thread() {

                                    public void run() {
                                        try {
                                            Socket s = new Socket("192.168.0.100", 4444);
                                            DataOutputStream dos = new DataOutputStream((s.getOutputStream()));
                                            dos.writeUTF(messsage);
                                            view.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    showToast(view.getContext(), messsage);
                                                }
                                            });
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
                } else if(!b){
                    messsage = "MICROONDAS DESLIGADO";
                    Thread t = new Thread() {

                        public void run() {
                            try {
                                Socket s = new Socket("192.168.0.100", 4444);
                                DataOutputStream dos = new DataOutputStream((s.getOutputStream()));
                                dos.writeUTF(messsage);
                                view.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showToast(view.getContext(), messsage);
                                    }
                                });
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

        forno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                fornoseek.setMax(250);
                fornoseek.incrementProgressBy(10);
                fornoseek.setProgress(myApplication.getKitchen_forno());
                fornoseek.setEnabled(myApplication.getForno());
                myApplication.setForno(b);
                //fornoseek.setLeft(0);

                if (myApplication.getForno()) {
                    fornoseek.setEnabled(myApplication.getForno());

                    messsage = "FORNO LIGADO";
                    Thread t = new Thread() {

                        public void run() {
                            try {
                                Socket s = new Socket("192.168.0.100", 4444);
                                DataOutputStream dos = new DataOutputStream((s.getOutputStream()));
                                dos.writeUTF(messsage);
                                view.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showToast(view.getContext(), messsage);
                                    }
                                });
                                dos.flush();
                                dos.close();
                                s.close();
                            } catch (UnknownHostException e) {

                            } catch (IOException e) {

                            }
                        }
                    };
                    t.start();

                    fornoseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            value.setText(Integer.toString(progress));
                            myApplication.setKitchen_forno(progress);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }

                    });

                    forno_button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if(forno.isChecked()){
                                messsage = "FORNO COM TEMPERATURA: " + value.getText() + " GRAUS";
                                Thread t = new Thread() {

                                    public void run() {
                                        try {
                                            Socket s = new Socket("192.168.0.100", 4444);
                                            DataOutputStream dos = new DataOutputStream((s.getOutputStream()));
                                            dos.writeUTF(messsage);
                                            view.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    showToast(view.getContext(), messsage);
                                                }
                                            });
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
                } else {
                    myApplication.setForno(false);
                    myApplication.setKitchen_forno(0);
                    fornoseek.setProgress(0);
                    fornoseek.setMax(0);
                    fornoseek.setLeft(0);
                    fornoseek.incrementProgressBy(0);
                    value.setText(" ");

                    messsage = "FORNO DESLIGADO";
                    Thread t = new Thread() {

                        public void run() {
                            try {
                                Socket s = new Socket("192.168.0.100", 4444);
                                DataOutputStream dos = new DataOutputStream((s.getOutputStream()));
                                dos.writeUTF(messsage);
                                view.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showToast(view.getContext(), messsage);
                                    }
                                });
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

        fornoseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(myApplication.getForno()){
                    value.setText(Integer.toString(progress));
                    myApplication.setKitchen_forno(progress);

                    messsage = "TEMPERATURA DO FORNO: " + Integer.toString(progress) + " GRAUS";
                    Thread t = new Thread() {

                        public void run() {
                            try {
                                Socket s = new Socket("192.168.0.100", 4444);
                                DataOutputStream dos = new DataOutputStream((s.getOutputStream()));
                                dos.writeUTF(messsage);
                                view.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showToast(view.getContext(), messsage);
                                    }
                                });
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

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });

//        arcondicionadoOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                arcondicionado.setMax(40);
//                arcondicionado.setLeft(0);
//                arcondicionado.incrementProgressBy(1);
//                arcondicionado.setProgress(0);
//
//                if(b){
//                    messsage = "Ligar arcondicionado";
//                    SendMessage sendMessageTask = new SendMessage();
//                    sendMessageTask.execute();
//
//                    arcondicionado.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//                        @Override
//                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                            value.setText(Integer.toString(progress));
//
////                            messsage = Integer.toString(progress);
////                            SendMessage sendMessageTask = new SendMessage();
////                            sendMessageTask.execute();
//                        }
//
//                        @Override
//                        public void onStartTrackingTouch(SeekBar seekBar) {
//                        }
//
//                        @Override
//                        public void onStopTrackingTouch(SeekBar seekBar) {
//                        }
//
//                    });
//                } else if(!b) {
//                    arcondicionado.setProgress(0);
//                    arcondicionado.setMax(0);
//                    arcondicionado.setLeft(0);
//                    arcondicionado.incrementProgressBy(0);
//                    value.setText(" ");
//
//                    messsage = "Desligar arcondicionado";
//                    SendMessage sendMessageTask = new SendMessage();
//                    sendMessageTask.execute();
//                }
//            }
//        });

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
                                if(x%2==0){
                                    Mensagem msg = new Mensagem(KITCHEN, LIGHT, true);
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "LUZ LIGADA");
                                        }
                                    });
                                } else {
                                    Mensagem msg = new Mensagem(KITCHEN, LIGHT, false);
                                    dos.writeObject(msg);
                                    dos.writeUTF("LUZ DA COZINHA DESLIGADA");
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
                                ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                if (y % 2 == 0) {
                                    Mensagem msg = new Mensagem(KITCHEN, WINDOW, true);
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "JANELA ABERTA");
                                        }
                                    });
                                } else {
                                    Mensagem msg = new Mensagem(KITCHEN, WINDOW, false);
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "JANELA FECHADA");
                                        }
                                    });
                                }
                                y++;
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
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    button.setBackgroundColor(Color.WHITE);
                }

                return true;
            }
        });
    }

    private void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public void newFrag(){
        Fragment fragment = new Kitchen();
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
                        DataInputStream dis = new DataInputStream(s.getInputStream());
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

}
