package com.example.rui.smarthome;

import java.util.List;

/**
 * Created by Rui on 21/11/2014.
 */
public class Mensagem {
    private static final int Points = 0;
    private static final int ROOM = 1;
    private static final int BEDROOM = 2;
    private static final int BATH = 4;
    private static final int KITCHEN = 3;

    private static final int WINDOW = 10;
    private static final int LIGHT = 11;


    private int divisao;
    private int elementoAfectado;
    private boolean condicao;
    private List<AccessPoint> points;

    public Mensagem(int Divisao, int ElementoAfectado, boolean condicao ) {
        this.divisao = Divisao;
        this.elementoAfectado = ElementoAfectado;
        this.condicao = condicao;
    }

    public Mensagem(List<AccessPoint> points){
        this.divisao = 0;
        this.points = points;

    }

}
