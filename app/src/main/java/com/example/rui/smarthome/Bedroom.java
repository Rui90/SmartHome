package com.example.rui.smarthome;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rui on 10/10/2014.
 */
public class Bedroom extends Fragment {

    List<Perfil> perfis = new ArrayList<Perfil>();
    private Context context;

    // metodo para mostrar o que vai aparecer na criaco
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getActivity();

        // vai buscar o layout que queres mostrar no ecra
        View view = inflater.inflate(R.layout.bedroom_layout, container, false);
        Switch light = (Switch) view.findViewById(R.id.lightswitch);
        Button create_perfil = (Button) view.findViewById(R.id.createprofile);

        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

        perfis = PerfisQuarto.ReadXML(context);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < perfis.size(); i++) {
            Perfil prof = perfis.get(i);
            list.add(prof.getName_perfil());
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


        create_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("Criar perfil");
                dialog.setContentView(R.layout.popup_perfil);
                dialog.show();
                Button saveBtn = (Button) dialog.findViewById(R.id.save);
                Button cancelBtn = (Button) dialog.findViewById(R.id.cancel);

                final EditText name = (EditText) dialog.findViewById(R.id.name_profile);
                final Switch light = (Switch) dialog.findViewById(R.id.window);
                final SeekBar intensity = (SeekBar) dialog.findViewById(R.id.seekBar);
                intensity.setMax(100);
                intensity.setLeft(0);
                intensity.incrementProgressBy(1);
                intensity.setProgress(0);
                final TextView value = (TextView) dialog.findViewById(R.id.valueLight);

                intensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        value.setText(Integer.toString(progress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                });

                cancelBtn.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });
                saveBtn.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        String name_perfil = name.getText().toString();
                        boolean light_perfil = light.isChecked();
                        int valor = 0;
                        if(!value.getText().toString().equals("")) {
                            valor = Integer.parseInt(value.getText().toString());
                        }
                        Perfil aux = new Perfil(name_perfil, light_perfil, valor);
                        PerfisQuarto.WriteXML(getActivity(), aux);

                        perfis = PerfisQuarto.ReadXML(context);
                        List<String> list = new ArrayList<String>();
                        for (int i = 0; i < perfis.size(); i++) {
                            Perfil prof = perfis.get(i);
                            list.add(prof.getName_perfil());
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                                android.R.layout.simple_spinner_item, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(dataAdapter);
                        //Closes the dialog
                        dialog.hide();

                    }

                });
            }
        });
        return view;
    }

}
