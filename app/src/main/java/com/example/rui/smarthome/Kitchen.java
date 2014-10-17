package com.example.rui.smarthome;

/*import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;*/

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by Rui on 10/10/2014.
 */
public class Kitchen extends Fragment {

    // metodo para mostrar o que vai aparecer na criaco
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // vai buscar o layout que queres mostrar no ecra
        View view = inflater.inflate(R.layout.kitchen_layout, container, false);

        return view;
    }
}
