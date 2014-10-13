package com.example.rui.smarthome;

import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by Rui on 10/10/2014.
 */
public class Bath extends Fragment {

    static Camera cam = null;
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try{
            cam = Camera.open();
        } catch (Exception e){

        }
        final Camera.Parameters p = cam.getParameters();

        // vai buscar o layout que queres mostrar no ecra
        view = inflater.inflate(R.layout.bath_layout, container, false);

        if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().
                heightPixels)
        {
            //Toast.makeText(getActivity().getApplicationContext(), "LANDSCAPE", Toast.LENGTH_LONG).show();
            view = inflater.inflate(R.layout.bath_layout_land, container, false);
            //Toast.makeText(this,"Screen switched to Landscape mode",Toast.LENGTH_SHORT).show();
            //setContentView(R.layout.fragment_home_landscape);
            ToggleButton toggle = (ToggleButton) view.findViewById(R.id.light);
            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        p.setFlashMode(p.FLASH_MODE_TORCH);
                        cam.setParameters(p);
                        //Toast.makeText(getActivity().getApplicationContext(), "Luz Ligada", Toast.LENGTH_LONG).show();
                    } else {
                        p.setFlashMode(p.FLASH_MODE_OFF);
                        cam.setParameters(p);
                        //Toast.makeText(getActivity().getApplicationContext(), "Luz Desligada", Toast.LENGTH_LONG).show();
                    }
                }
            });
            return view;
        }
        else
        {
            //Toast.makeText(getActivity().getApplicationContext(), "PORTRAIT", Toast.LENGTH_LONG).show();
            ToggleButton toggle = (ToggleButton) view.findViewById(R.id.light);
            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        p.setFlashMode(p.FLASH_MODE_TORCH);
                        cam.setParameters(p);
                        //Toast.makeText(getActivity().getApplicationContext(), "Luz Ligada", Toast.LENGTH_LONG).show();
                    } else {
                        p.setFlashMode(p.FLASH_MODE_OFF);
                        cam.setParameters(p);
                        //Toast.makeText(getActivity().getApplicationContext(), "Luz Desligada", Toast.LENGTH_LONG).show();
                    }
                }
            });
            return view;
            //Toast.makeText(this,"Screen switched to Portrait mode",Toast.LENGTH_SHORT).show();
        }

        //return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().setContentView(R.layout.bath_layout_land);
            Toast.makeText(getActivity().getApplicationContext(), "LANDSCAPEEEEE", Toast.LENGTH_LONG).show();
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(getActivity().getApplicationContext(), "PORTRAITTTTT", Toast.LENGTH_LONG).show();
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
}
