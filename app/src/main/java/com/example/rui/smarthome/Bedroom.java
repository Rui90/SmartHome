package com.example.rui.smarthome;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rui.server.Mensagem;
import com.example.rui.server.Perfil;

import java.io.IOException;
import java.io.ObjectInputStream;
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
public class Bedroom extends Fragment {

    List<String> list = new ArrayList<String>();
    private Context context;

    private Socket client;
    private PrintWriter printwriter;
    private String messsage;

    private static final int BEDROOM = 2;

    private static final int WINDOW = 10;
    private static final int LIGHT = 11;

    View view;
    private String screen_Size = "medium";

    // metodo para mostrar o que vai aparecer na criaco
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getActivity();

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
            view = inflater.inflate(R.layout.bedroom_layout_land, container, false);
        }
        else if((getResources().getDisplayMetrics().widthPixels<getResources().getDisplayMetrics().
                heightPixels) && screen_Size.equals("medium"))
        {
            view = inflater.inflate(R.layout.bedroom_layout, container, false);
        }

        if((getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().
                heightPixels) && screen_Size.equals("large"))
        {
            view = inflater.inflate(R.layout.bedroom_large_land, container, false);
        }
        else if((getResources().getDisplayMetrics().widthPixels<getResources().getDisplayMetrics().
                heightPixels) && screen_Size.equals("large"))
        {
            view = inflater.inflate(R.layout.bedroom_layout_large, container, false);
        }

        final ImageButton button = (ImageButton) view.findViewById(R.id.imageButton2);
        ImageButton light = (ImageButton) view.findViewById(R.id.lampada);

        if(screen_Size.equals("medium")){
            if(((MyApplication) getActivity().getApplication()).getBedroomHelper().isLight()){
                light.setImageResource(R.drawable.lampada11);
            }else {
                light.setImageResource(R.drawable.lampada1);
            }
            if (((MyApplication) getActivity().getApplication()).getBedroomHelper().isWindow()) {
                if (((MyApplication) getActivity().getApplication()).getIsNight())
                    button.setImageResource(R.drawable.bednight1);
                else
                    button.setImageResource(R.drawable.windowroom1);
            }
            else{
                button.setImageResource(R.drawable.janela1);
            }
        }else if(screen_Size.equals("large")) {
            if(((MyApplication) getActivity().getApplication()).getBedroomHelper().isLight()){
                light.setImageResource(R.drawable.lampada22);
            }else {
                light.setImageResource(R.drawable.lampada2);
            }
            if (((MyApplication) getActivity().getApplication()).getBedroomHelper().isWindow()) {
                if (((MyApplication) getActivity().getApplication()).getIsNight())
                    button.setImageResource(R.drawable.bednight2);
                else
                    button.setImageResource(R.drawable.windowroom2);
            }
            else{
                 button.setImageResource(R.drawable.janela2);
            }
        }

        receiveMessage(getActivity());

        lightButton();

        windowButton();

        Button create_perfil = (Button) view.findViewById(R.id.createprofile);
        Button edit_perfil = (Button) view.findViewById(R.id.editMode);
        Button set_perfil = (Button) view.findViewById(R.id.buttonSet);
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

        list = new ArrayList<String>();
        //Log.d("b", "SIZE: "+((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().size());
        if(((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis() != null){
            for(int i = 0; i < ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().size(); i++) {
                list.add(((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().get(i).getName_perfil());
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);



        //Actualiza os campos sempre que se altera o perfil seleccionado
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
               // loadSelectedProfile();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        set_perfil.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(list.size() > 0) {
                    final String selected = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                    ((MyApplication) getActivity().getApplication()).getBedroomHelper().setModo(spinner.getSelectedItemPosition());
                    for (int i = 0; i < ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().size(); i++) {
                            if(((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().get(i).getName_perfil().equals(selected)) {
                                Perfil p = ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().get(i);
                                ((MyApplication) getActivity().getApplication()).getBedroomHelper().setLight(p.getLight_Perfil());
                                ((MyApplication) getActivity().getApplication()).getBedroomHelper().setWindow(p.getWindow_perfil());
                                break;
                            }
                    }
                    ((MyApplication) getActivity().getApplication()).getBedroomHelper();
                    Thread t = new Thread() {

                        public void run() {
                            try {
                                Socket s = new Socket(((MyApplication) getActivity().getApplication()).getIp(), 4444);
                                Mensagem m = new Mensagem(BEDROOM, ((MyApplication) getActivity().getApplication()).getBedroomHelper());
                                Log.d("f",((MyApplication) getActivity().getApplication()).getBedroomHelper().isLight() + "\n" +
                                        ((MyApplication) getActivity().getApplication()).getBedroomHelper().isWindow());
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
                    Toast.makeText(getActivity().getApplicationContext(), "Selecionei: " + selected, Toast.LENGTH_LONG).show();
                }
            }
        });

        edit_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.size() > 0) {

                    final Dialog dialog = new Dialog(getActivity());

                    dialog.setTitle("Editar perfil");
                    dialog.setContentView(R.layout.popup_perfil);
                    dialog.show();
                    Button saveBtn = (Button) dialog.findViewById(R.id.save);
                    Button cancelBtn = (Button) dialog.findViewById(R.id.cancel);
                    Button apagar = (Button) dialog.findViewById(R.id.deleteButton);
                    apagar.setVisibility(View.VISIBLE);
                    String selected = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                    final Perfil prof = getProfileByName(selected);

                    Log.d("prof", prof.getName_perfil());

                    final EditText name = (EditText) dialog.findViewById(R.id.name_profile);
                    final Switch window = (Switch) dialog.findViewById(R.id.window);
                    final Switch light  = (Switch) dialog.findViewById(R.id.lightSwitch);
                    Log.d("y", "SIZE: " + ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().size());
                    for (int i = 0; i < ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().size(); i++) {
                        Log.d("f", "DENTRO DO FOR " + selected);
                                if(((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().get(i).getName_perfil().equals(selected)) {
                                    Log.d("c", "DENTRO DO IF " + prof.getWindow_perfil());
                                    Perfil p = ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().get(i);
                                    name.setText(selected);
                                    window.setChecked(p.getWindow_perfil());
                                    light.setChecked(p.getLight_Perfil());
                                    break;
                                }

                    }

                    apagar.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            List<String> list = new ArrayList<String>();
                            for (int i = 0; i < ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().size(); i++) {
                                    if (((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().get(i).getName_perfil().equals(prof.getName_perfil())) {
                                        ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().remove(i);
                                        break;
                                    }
                            }

                            for (int i = 0; i < ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().size(); i++) {
                                list.add(((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().get(i).getName_perfil());
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(dataAdapter);
                            dialog.hide();

                            Thread t = new Thread() {

                                public void run() {
                                    try {
                                        Socket s = new Socket(((MyApplication) getActivity().getApplication()).getIp(), 4444);
                                        ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                        //((MyApplication) getActivity().getApplication()).getBedroomHelper().setLight(true);
                                        Mensagem msg = new Mensagem(BEDROOM, ((MyApplication) getActivity().getApplication()).getBedroomHelper());
                                        dos.writeObject(msg);
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

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    });
                    saveBtn.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            String name_perfil = name.getText().toString();
                            boolean window_perfil = window.isChecked();
                            boolean light_perfil = light.isChecked();
                            //int valor = 0;

                            List<String> list = new ArrayList<String>();
                            for (int i = 0; i < ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().size(); i++) {
                                if (((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().get(i).getName_perfil().equals(prof.getName_perfil())) {
                                    ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().get(i).setNamePerfil(name_perfil);
                                    ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().get(i).setWindow_perfil(window_perfil);
                                    ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().get(i).setLight_perfil(light_perfil);
                                }
                            }

                            for (int i = 0; i < ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().size(); i++) {
                                list.add(((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().get(i).getName_perfil());
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(dataAdapter);

                            dialog.hide();

                            Thread t = new Thread() {

                                public void run() {
                                    try {
                                        Socket s = new Socket(((MyApplication) getActivity().getApplication()).getIp(), 4444);
                                        ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                        //((MyApplication) getActivity().getApplication()).getBedroomHelper().setLight(true);
                                        Mensagem msg = new Mensagem(BEDROOM, ((MyApplication) getActivity().getApplication()).getBedroomHelper());
                                        dos.writeObject(msg);
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
                    Toast.makeText(getActivity().getApplicationContext(), "Não tem modos criados!", Toast.LENGTH_LONG).show();
                }
            }

        });

        create_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("Criar modo");
                dialog.setContentView(R.layout.popup_perfil);
                dialog.show();
                Button saveBtn = (Button) dialog.findViewById(R.id.save);
                Button cancelBtn = (Button) dialog.findViewById(R.id.cancel);

                final EditText name = (EditText) dialog.findViewById(R.id.name_profile);
                final Switch window = (Switch) dialog.findViewById(R.id.window);
                final Switch lightSwitch = (Switch) dialog.findViewById(R.id.lightSwitch);

                cancelBtn.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });
                saveBtn.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        String name_perfil = name.getText().toString();
                        boolean window_perfil = window.isChecked();
                        boolean light_perfil = lightSwitch.isChecked();
//
                        ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().add(new Perfil(name_perfil, window_perfil, light_perfil));
                        Log.d("c", "WINDOWWWWWWWW: " + window_perfil + "\n");
                        Log.d("v", "LIGHTTTTTTTTT: " + light_perfil + "\n");
                        list = new ArrayList<String>();
                        for(int i = 0; i < ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().size(); i++) {
                            list.add(((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().get(i).getName_perfil());
                        }
                        Toast.makeText(getActivity().getApplicationContext(), "List size: " + list.size() + " MyApp Size: " +
                                        ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().size(),
                                Toast.LENGTH_LONG).show();

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                                android.R.layout.simple_spinner_item, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(dataAdapter);
                        //Closes the dialog
                        dialog.hide();
                        Thread t = new Thread() {

                            public void run() {
                                try {
                                    Log.d("g", "CRIEI UM MODO");
                                    Socket s = new Socket(((MyApplication) getActivity().getApplication()).getIp(), 4444);
                                    ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                        //((MyApplication) getActivity().getApplication()).getBedroomHelper().setLight(true);
                                    Mensagem msg = new Mensagem(BEDROOM, ((MyApplication) getActivity().getApplication()).getBedroomHelper());
                                    dos.writeObject(msg);
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
            }
        });
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        newFrag();
    }

    public void lightButton() {
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
                                if (!((MyApplication) getActivity().getApplication()).getBedroomHelper().isLight()) {
                                    ((MyApplication) getActivity().getApplication()).getBedroomHelper().setLight(true);
                                    Mensagem msg = new Mensagem(BEDROOM, ((MyApplication) getActivity().getApplication()).getBedroomHelper());
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "Luz ligada");
                                            if(screen_Size.equals("medium")){
                                                button.setImageResource(R.drawable.lampada11);
                                            }else if(screen_Size.equals("large")){
                                                button.setImageResource(R.drawable.lampada22);
                                            }

                                        }
                                    });
                                } else {
                                    ((MyApplication) getActivity().getApplication()).getBedroomHelper().setLight(false);
                                    Mensagem msg = new Mensagem(BEDROOM, ((MyApplication) getActivity().getApplication()).getBedroomHelper());
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "Luz desligada");
                                            if(screen_Size.equals("medium")){
                                                button.setImageResource(R.drawable.lampada1);
                                            }else if(screen_Size.equals("large")){
                                                button.setImageResource(R.drawable.lampada2);
                                            }
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
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
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
                                if(!((MyApplication) getActivity().getApplication()).getBedroomHelper().isWindow()){
                                    ((MyApplication) getActivity().getApplication()).getBedroomHelper().setWindow(true);
                                    Mensagem msg = new Mensagem(BEDROOM, ((MyApplication) getActivity().getApplication()).getBedroomHelper());
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "Janela aberta");
                                            if(screen_Size.equals("medium")){
                                                if(((MyApplication) getActivity().getApplication()).getIsNight())
                                                    button.setImageResource(R.drawable.bednight1);
                                                else
                                                    button.setImageResource(R.drawable.windowroom1);
                                            }else if(screen_Size.equals("large")){
                                                if(((MyApplication) getActivity().getApplication()).getIsNight())
                                                    button.setImageResource(R.drawable.bednight2);
                                                else
                                                    button.setImageResource(R.drawable.windowroom2);
                                            }
                                        }
                                    });
                                } else {
                                    ((MyApplication) getActivity().getApplication()).getBedroomHelper().setWindow(false);
                                    Mensagem msg = new Mensagem(BEDROOM, ((MyApplication) getActivity().getApplication()).getBedroomHelper());
                                    dos.writeObject(msg);
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast(view.getContext(), "Janela fechada");
                                            if(screen_Size.equals("medium")){
                                                button.setImageResource(R.drawable.janela1);
                                            }else if(screen_Size.equals("large")){
                                                button.setImageResource(R.drawable.janela2);
                                            }
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

    public void newFrag(){
        Fragment fragment = new Bedroom();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * Método que obtém um perfil através do seu nome
     *
     * @param name
     * @return
     */
    private Perfil getProfileByName(String name) {
        for (int i = 0; i < ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().size(); i++) {
            Perfil pr = ((MyApplication) getActivity().getApplication()).getBedroomHelper().getPerfis().get(i);
            if (pr.getName_perfil().compareTo(name) == 0) {
                return pr;
            }
        }
        return null;
    }

    public void receiveMessage(final Activity act){

        Thread t = new Thread(){

            public void run(){
                try{
                    ServerSocket ss = new ServerSocket(4444);
                    while(true){
                        Socket s = ss.accept();
                        ObjectInputStream dis = new ObjectInputStream(s.getInputStream());
                        //Log.d("o", "TOU A ESPERA");
                        final Mensagem m = (Mensagem) dis.readObject();
                        if(m != null){
                            ((MyApplication) act.getApplication()).setBedroomHelper(m.getBedroomHelper());
                            ((MyApplication) act.getApplication()).setIsNight(m.getIsNight());
                        }
                        Log.d("p", "RECEBI: " + m);


                        Handler handler = new Handler(Looper.getMainLooper());

                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                Log.d("b", "RUNNN");
                                Toast.makeText(act, "JANELA: " + m.getBedroomHelper().isWindow() + " e LUZ: "
                                        + m.getBedroomHelper().isLight(), Toast.LENGTH_LONG).show();
                            }
                        });
                        dis.close();
                        s.close();
                        Fragment fragment = new Bedroom();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                    }
                }
                catch(IOException e){

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }


}
