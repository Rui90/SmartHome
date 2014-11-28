package com.example.rui.server;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;


public class Mensagem  implements Serializable {

   private static final long serialVersionUID = 1L;
    
   private int divisao;
   private int elementoAfectado;
   private boolean condicao;
   private LinkedList<AccessPoint> points;
   private Object o;
   private int currentDiv;
   private boolean isAuto = false;

   public Mensagem(boolean isAuto) {
      this.divisao = 0;
      this.isAuto = isAuto;
   }

    public Mensagem(int Divisao, int ElementoAfectado, boolean condicao ) {
        this.divisao = Divisao;
        this.elementoAfectado = ElementoAfectado;
        this.condicao = condicao;
   }

   public Mensagem(int currentDiv) {
    	this.currentDiv = currentDiv;
        this.divisao = currentDiv;
    }

    public Mensagem(LinkedList<AccessPoint> points){
        this.divisao = 0;
        this.points = points;

    }

   public boolean getAuto() {
       return this.isAuto;
   }
    
   public int getDivisao() {
    	return this.divisao; 
    }

   public int getDiv() {
        return this.currentDiv;
    }

   public void setDiv(int div){
        this.currentDiv = div;
    }

   public int getElemento() {
    	return this.elementoAfectado; 
    }
    
    public boolean getCondicao() {
    	return this.condicao; 
    }
    
    public LinkedList<AccessPoint> getPoints() {
    	return this.points; 
    }
    

}
