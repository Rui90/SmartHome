package com.example.rui.smarthome;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

/**
 * Created by Rui on 10/10/2014.
 */
public class Bath extends Fragment {

    static Camera cam = Camera.open();
    View view;
    final Camera.Parameters p = cam.getParameters();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().
                heightPixels)
        {
            view = inflater.inflate(R.layout.bath_layout_land, container, false);
        }
        else
        {
            view = inflater.inflate(R.layout.bath_layout, container, false);
        }

        lightButton();

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        newFrag();
    }

    public void lightButton(){
        ToggleButton toggle = (ToggleButton) view.findViewById(R.id.light);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    p.setFlashMode(p.FLASH_MODE_TORCH);
                    cam.setParameters(p);
                } else {
                    p.setFlashMode(p.FLASH_MODE_OFF);
                    cam.setParameters(p);
                }
            }
        });
    }

    public void newFrag(){
        Fragment fragment = new Bath();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
