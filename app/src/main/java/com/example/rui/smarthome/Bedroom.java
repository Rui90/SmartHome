package com.example.rui.smarthome;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by Rui on 10/10/2014.
 */
public class Bedroom extends Fragment {

    // metodo para mostrar o que vai aparecer na criaco
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // vai buscar o layout que queres mostrar no ecra
        View view = inflater.inflate(R.layout.bedroom_layout, container, false);

        return view;
    }

}
