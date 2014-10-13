package com.example.rui.smarthome;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by Rui on 10/10/2014.
 */
public class Exterior extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // vai buscar o layout que queres mostrar no ecra
        View view = inflater.inflate(R.layout.exterior_layout, container, false);

        ToggleButton toggle = (ToggleButton) view.findViewById(R.id.rega);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getActivity().getApplicationContext(), "Rega ligada", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Rega desligada", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

}
