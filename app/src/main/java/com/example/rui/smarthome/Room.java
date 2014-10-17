package com.example.rui.smarthome;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by Rui on 10/10/2014.
 */
public class Room extends Fragment {

    //Camera cam = Camera.open();
    View view;
    //final Camera.Parameters p = cam.getParameters();
    int x = 0;

    // metodo para mostrar o que vai aparecer na criacao
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().
                heightPixels)
        {
            view = inflater.inflate(R.layout.dinnerroom_layout_land, container, false);
        }
        else
        {
            view = inflater.inflate(R.layout.dinnerroom_layout, container, false);
        }

        lightButton();

        final Switch arcondicionadoOnOff = (Switch) view.findViewById(R.id.arcondicionado);
        final SeekBar arcondicionado = (SeekBar) view.findViewById(R.id.seekBar);
        final TextView value = (TextView) view.findViewById(R.id.textView2);
        arcondicionado.setMax(0);
        arcondicionado.setLeft(0);
        arcondicionado.incrementProgressBy(0);

        arcondicionadoOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    arcondicionado.setMax(40);
                    arcondicionado.setLeft(0);
                    arcondicionado.incrementProgressBy(1);
                    arcondicionado.setProgress(0);

                    arcondicionado.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

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
                } else {
                    arcondicionado.setProgress(0);
                    arcondicionado.setMax(0);
                    arcondicionado.setLeft(0);
                    arcondicionado.incrementProgressBy(0);
                    value.setText(" ");
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
        ImageButton button = (ImageButton) view.findViewById(R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(x%2==0){
                    //p.setFlashMode(p.FLASH_MODE_TORCH);
                    //cam.setParameters(p);
                } else {
                    //p.setFlashMode(p.FLASH_MODE_OFF);
                    //cam.setParameters(p);
                }
                x++;
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
}
