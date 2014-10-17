package com.example.rui.smarthome;

import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by Rui on 10/10/2014.
 */
public class Bath extends Fragment {

    static Camera cam = Camera.open();
    View view;
    final Camera.Parameters p = cam.getParameters();
    int x = 0;

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

        final SeekBar waterQuantity = (SeekBar) view.findViewById(R.id.seekBar);
        waterQuantity.setMax(100);
        waterQuantity.setLeft(0);
        waterQuantity.incrementProgressBy(1);
        waterQuantity.setProgress(0);
        final TextView value = (TextView) view.findViewById(R.id.textView);

        waterQuantity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

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

        final SeekBar waterTemperature = (SeekBar) view.findViewById(R.id.seekBar2);
        waterTemperature.setMax(100);
        waterTemperature.setLeft(0);
        waterTemperature.incrementProgressBy(1);
        waterTemperature.setProgress(0);
        final TextView value2 = (TextView) view.findViewById(R.id.textView2);

        waterTemperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value2.setText(Integer.toString(progress));
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

        ImageButton button = (ImageButton) view.findViewById(R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(x%2==0){
                    p.setFlashMode(p.FLASH_MODE_TORCH);
                    cam.setParameters(p);
                } else {
                    p.setFlashMode(p.FLASH_MODE_OFF);
                    cam.setParameters(p);
                }
                x++;
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
