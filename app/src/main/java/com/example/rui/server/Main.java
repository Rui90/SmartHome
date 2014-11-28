package com.example.rui.server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

	private static boolean day = true; 
	private static boolean goodTime = true;
	private static ArrayList<String> divisoes = new ArrayList<String>(); 
	private static boolean auto = true; 
	private static int time = 20000;
	private static RoomHelper room = new RoomHelper(false, false, false, false);
	private static BedroomHelper bedroom = new BedroomHelper(false, false, 1);
	private static BathHelper bath = new BathHelper(false);
	private static KitchenHelper kitchen = new KitchenHelper(false, false);
	private static LinkedList<AccessPoint> access = new LinkedList<AccessPoint>();
	private static final String IP = "192.168.1.101";
    private static int current_point = 0;
	
	
    private static final int ROOM = 1;
    private static final int BEDROOM = 2;
    private static final int BATH = 4;
    private static final int KITCHEN = 3;

    private static final int WINDOW = 10;
    private static final int LIGHT = 11;
	
	/*
	 * private static ServerSocket serverSocket; private static Socket
	 * clientSocket; private static ObjectInputStream inputStreamReader; private
	 * static ObjectOutputStream outputStreamReader; private static
	 * BufferedReader bufferedReader; private static String message; private
	 * static SendMessage sm;
	 */

	public static void main(String[] args) throws InterruptedException {

		//Scanner in = new Scanner(System.in);
		Timer timer = new Timer();

		Thread t = new Thread() {
			public void run() {
				System.out.println("SERVER STARTED");
				try {
					ServerSocket ss = new ServerSocket(4444);
					while (true) {
						Socket s = ss.accept();
						ObjectInputStream dis = new ObjectInputStream(
								s.getInputStream());
					
						//cria-se uma nova mensagem
						Mensagem m = (Mensagem) dis.readObject();
                        if(m != null){
                            System.out.println(m.getDivisao());
                        }
						if(m.getDivisao() == 0) {
                            access = m.getPoints();
							
						} else if(m.getDivisao() == ROOM){
							if(m.getElemento() == WINDOW){
								room.setWindow(m.getCondicao());
							}else if(m.getDivisao() == LIGHT){
								room.setLight(m.getCondicao());
							}								
						}else if(m.getDivisao() == BEDROOM){
							if(m.getElemento() == WINDOW){
								bedroom.setWindow(m.getCondicao());
							}else if(m.getDivisao() == LIGHT){
								bedroom.setLight(m.getCondicao());
							}		
						}else if(m.getDivisao() == KITCHEN){
							if(m.getElemento() == WINDOW){
								kitchen.setWindow(m.getCondicao());
							}else if(m.getDivisao() == LIGHT){
								kitchen.setLight(m.getCondicao());
							}		
						}else if(m.getDivisao() == BATH ){
							if(m.getElemento() == LIGHT){
								bath.setLight(m.getCondicao());
							}								
						}else if(m.getDivisao() == m.getDiv()){
                            current_point = m.getDiv();
                        }
						// se modo automatico, auto = true, sen�o auto = false! 
						// adicionar os 4 access Points por ordem.

						dis.close();
						s.close();
					}
				} catch (IOException e) {
					e.printStackTrace();

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.start();

		class dayNight extends TimerTask {
			public void run() {
					try {
						Socket s = new Socket(IP, 4444);
						ObjectOutputStream dos = new ObjectOutputStream(
							(s.getOutputStream()));
						
						if(!day && goodTime) {
							day = true; 
							dos.writeUTF("� de noite!");
							room.setLight(false);
							room.setWindow(true);
							bedroom.setLight(false);
							bedroom.setWindow(true);
                            kitchen.setLight(false);
							kitchen.setWindow(true);
						} else if(!day && !goodTime){
							day = true; 
							dos.writeUTF("� de dia!");
							room.setWindow(false);	
							bedroom.setWindow(false);
                            kitchen.setWindow(false);
							//  passa para dia e fecha janelas e abre a luz se tivermos nessa divisao
						} else {
							day = false;
							room.setLight(false);
							room.setWindow(false);	
							bedroom.setLight(false);
							bedroom.setWindow(false);
                            kitchen.setWindow(false);
                            kitchen.setLight(false);
							// fechar janelas na app
							// acender luz na divisao corrente
						}
						
						dos.flush();
						dos.close();
						s.close();
				} catch (IOException e) {

				} 
			}
		}
		
		class goodTimebadTime extends TimerTask {
			public void run() {
					try {
						Socket s = new Socket(IP, 4444);
						ObjectOutputStream dos = new ObjectOutputStream(
							(s.getOutputStream()));
						
						if(!day && !goodTime) {
							goodTime = true; 
							dos.writeUTF("� de noite!");
							room.setLight(false);
							room.setWindow(true);
							bedroom.setLight(false);
							bedroom.setWindow(true);
                            kitchen.setLight(false);
                            kitchen.setWindow(true);
							time = 20000; 
						} else if(!day && goodTime){
							goodTime = false; 
							dos.writeUTF("� de dia!");
							room.setWindow(false);
							bedroom.setWindow(false);
                            kitchen.setWindow(false);
							time = 15000; 
							//  passa para dia e fecha janelas e abre a luz se tivermos nessa divisao
						} else if (day && goodTime){
							goodTime = false;
							room.setLight(false);
							room.setWindow(false);	
							bedroom.setLight(false);
							bedroom.setWindow(false);
                            kitchen.setWindow(false);
                            kitchen.setLight(false);
							time = 15000; 
							// fechar janelas na app
							// acender luz na divisao corrente
						} else {
							goodTime = true; 
							time = 20000; 
						}
						dos.flush();
						dos.close();
						s.close();
				} catch (IOException e) {

				} 
			}
		}
		 
		timer.schedule(new dayNight(), 0, 15000);
		timer.schedule(new goodTimebadTime(), 0, time);

		}
	}
