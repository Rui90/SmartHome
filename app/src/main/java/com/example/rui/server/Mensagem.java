package com.example.rui.server;

import com.example.rui.smarthome.Kitchen;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;


public class Mensagem  implements Serializable {

    private static final int ROOM = 1;
    private static final int BEDROOM = 2;
    private static final int BATH = 4;
    private static final int KITCHEN = 3;

   private static final long serialVersionUID = 1L;
    
   private int divisao;
     private int elementoAfectado;
    private boolean condicao;
    private LinkedList<AccessPoint> points;
    private Object o;
    private int currentDiv;
    private boolean isAuto = false;
    private BedroomHelper bedroom;
    private BathHelper bath;
    private KitchenHelper kitchen;
    private RoomHelper room;
    private String connected;
    private String ponto;
    private double old;
    private double newP;

   public Mensagem(boolean isAuto) {
      this.divisao = 41;
      this.isAuto = isAuto;
   }

   public Mensagem(RoomHelper room, BedroomHelper bedroom, KitchenHelper kitchen, BathHelper bath){
       this.bedroom = bedroom;
       this.room = room;
       this.kitchen = kitchen;
       this.bath = bath;
   }

   public Mensagem(String connected) {
       this.connected = connected;
       this.divisao = 40;
   }

    public Mensagem(int Divisao, int ElementoAfectado, boolean condicao ) {
        this.divisao = Divisao;
        this.elementoAfectado = ElementoAfectado;
        this.condicao = condicao;
   }

    public Mensagem(String ponto, double old, double newP){
        this.ponto = ponto;
        this.old = old;
        this.newP = newP;
        this.divisao = 46;
        //this.isAuto = true;
    }

   public Mensagem(int currentDiv) {
    	this.currentDiv = currentDiv;
        this.divisao = currentDiv;
    }

    public Mensagem(int currentDiv, RoomHelper room) {
        this.currentDiv = currentDiv;
        this.divisao = currentDiv;
        this.room = room;
    }
    public Mensagem(int currentDiv, BedroomHelper room) {
        this.currentDiv = currentDiv;
        this.divisao = currentDiv;
        this.bedroom = room;
    }
    public Mensagem(int currentDiv, BathHelper room) {
        this.currentDiv = currentDiv;
        this.divisao = currentDiv;
        this.bath = room;
    }
    public Mensagem(int currentDiv, KitchenHelper room) {
        this.currentDiv = currentDiv;
        this.divisao = currentDiv;
        this.kitchen = room;
    }
    public Mensagem(LinkedList<AccessPoint> points){
        this.divisao = 45;
        this.points = points;
        this.isAuto = true;
    }

    public BathHelper getBathHelper() {
        return this.bath;
    }

    public RoomHelper getRoomHelper() {
        return this.room;
    }

    public KitchenHelper getKitchenHelper() {
        return this.kitchen;
    }

    public BedroomHelper getBedroomHelper() {
        return this.bedroom;
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
    
    public String getPonto(){
        return ponto;
    }
    public double getOld(){
        return old;
    }
    public double getNewP(){
        return newP;
    }
}
