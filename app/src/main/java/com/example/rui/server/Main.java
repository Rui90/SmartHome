package com.example.rui.server;

import com.example.rui.smarthome.MyApplication;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Main implements Serializable {

    private static final long serialVersionUID = 1L;

    //private static final String POINT = "d4:6e:5c:1c:fb:5b";

	private static boolean day = true;
	private static boolean goodTime = true;
	private static boolean auto = false;
	private static int time = 40000;
	private static RoomHelper room = new RoomHelper(false, false, false, false, 0);
	private static BedroomHelper bedroom = new BedroomHelper(false, false, 1, new LinkedList<Perfil>());
	private static BathHelper bath = new BathHelper(false, 0, 0);
	private static KitchenHelper kitchen = new KitchenHelper(false, false, false, false, 0);
	private static LinkedList<AccessPoint> access = new LinkedList<AccessPoint>();
	private static final String IP = "192.168.0.102";
    private static int current_point = 0;

    private static boolean man = false;

    private static String div = "";


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

	public static void main(String[] args) throws InterruptedException, IOException {


        final ServerSocket ss = new ServerSocket(4444);

        //Scanner in = new Scanner(System.in);

        Thread t = new Thread() {

            public void run() {
                System.out.println(" --- SERVER STARTED --- ");

                try {
                        Socket s = null;
                        // ss = new ServerSocket(4444);
                        while (true) {
                            s = ss.accept();
                            ObjectInputStream dis = null;
                            try{
                                dis = new ObjectInputStream(s.getInputStream());

                            } catch (EOFException e) {
                                e.printStackTrace();
                            }

                            //cria-se uma nova mensagem
                            Mensagem m = null;

                            try {
                                m = (Mensagem) dis.readObject();

                            } catch (EOFException e) {
                                e.printStackTrace();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (ClassNotFoundException exp) {
                                exp.printStackTrace();
                            }

                            if (m != null) {
                                //System.out.print(m.getDivisao()+"\n");
                                if (m.getDivisao() == 40) {
                                    System.out.print("Cliente chegou. \n");
                                    Socket s2 = ss.accept();
                                    Mensagem toSendMsg = new Mensagem(room, bedroom, kitchen, bath);
                                    ObjectOutputStream toSend = new ObjectOutputStream(
                                            s2.getOutputStream());
//                                    Mensagem toSendMsg = new Mensagem(room, bedroom, kitchen, bath);
//                                    ObjectOutputStream toSend = new ObjectOutputStream(
//                                            s.getOutputStream());
                                    toSend.writeObject(toSendMsg);
                                    toSend.flush();
                                    toSend.close();
                                    s2.close();
                                } else if (m.getDivisao() == 50) {
                                    //System.out.print("PONTOS: " + m.getPoints()+"\n");
                                    //access = m.getPoints();
                                    //System.out.print("NUMERO DE PONTOS: " + access.size());
                                    auto = m.getAuto();
                                    if(auto){
                                        System.out.print("Modo automático. \n");
                                        //System.out.print("SIZE" + access.size());
//                                        for(int i=0; i<access.size(); i++){
//                                            AccessPoint p = access.get(i);
//                                            System.out.print("Ponto " + i + ": " + p.getScanResult() +" com Dist: "
//                                            + p.getDistance()+"\n");
//                                        }
                                    } else {
                                        System.out.print("Modo manual. \n");
                                    }
                                } else if(m.getDivisao() == 46){

                                    //System.out.print(m.getPonto() + "\nOLD: " + m.getOld() + "\nNEW: " + m.getNewP()+"\n");

                                }
                                else if (m.getDivisao() == ROOM) {

                                    div = "ROOM";




                                    RoomHelper aux = m.getRoomHelper();

                                    //System.out.print("ROOOOM");

                                if(room != null && aux != null){
                                    if (room.isLight() != aux.isLight()) {
                                        if (room.isLight()) {
                                            System.out.print("Luz da sala desligada!\n");
                                            room.setLight(false);
                                        } else {
                                            System.out.print("Luz da sala ligada!\n");
                                        }
                                        room.setLight(aux.isLight());
                                    }

                                    if (room.isWindow() != aux.isWindow()) {
                                        //System.out.print("JA " + room.isWindow());
                                        if (room.isWindow()) {
                                            System.out.print("Janela da sala fechada!\n");
                                            room.setWindow(false);
                                            //System.out.print("AGORA " + room.isWindow()+"\n");
                                            man = true;
                                        } else {
                                            System.out.print("Janela da sala aberta!\n");
                                        }
                                        room.setWindow(aux.isWindow());
                                    }

                                    if (room.isTv() != aux.isTv()) {
                                        if (room.isTv()) {
                                            System.out.print("Televisao da sala desligada!\n");
                                        } else {
                                            System.out.print("Televisao da sala ligada!\n");
                                        }
                                        room.setTv(aux.isTv());
                                    }

                                    if (room.isArcondicionado() != aux.isArcondicionado()) {
                                        if (room.isArcondicionado()) {
                                            System.out.print("Ar condicionado desligado!\n");
                                        } else {
                                            System.out.print("Ar condicionado ligado!\n");
                                        }
                                        room.setArcondicionado(aux.isArcondicionado());
                                    }

                                    if (room.getTemperatureArCond() != aux.getTemperatureArCond()) {
                                        System.out.print("Temperatura do ar condicionado a: " + aux.getTemperatureArCond() + "graus\n");
                                        room.setTemperatureArCond(aux.getTemperatureArCond());
                                    }

                                    room = m.getRoomHelper();
                                }

                                } else if (m.getDivisao() == BEDROOM) {

                                    div = "BEDROOM";

                                    BedroomHelper aux = m.getBedroomHelper();

                                    if(bedroom != null && aux != null){
                                        if(bedroom.isLight() != aux.isLight()){
                                            if(bedroom.isLight()){
                                                System.out.print("Luz do quarto desligada!\n");
                                            }else {
                                                System.out.print("Luz do quarto ligada!\n");
                                            }
                                            bedroom.setLight(aux.isLight());
                                        }

                                        if(bedroom.isWindow() != aux.isWindow()){
                                            if(bedroom.isWindow()){
                                                System.out.print("Janela do quarto fechada!\n");
                                            }else {
                                                System.out.print("Janela do quarto aberta!\n");
                                            }
                                            bedroom.setWindow(aux.isWindow());
                                        }

                                        if(bedroom.getPerfis().size() != aux.getPerfis().size()){

                                            System.out.print("LUZ: " + m.getBedroomHelper().isLight() + "\n" +
                                                "WINDOW: " + m.getBedroomHelper().isWindow() + "\n");

                                            if(aux.getPerfis().size()==0){
                                                System.out.print("Sem modos\n");
                                            } else {
                                                System.out.print("Lista de Modos:\n");
                                            }
                                            for(int i=0; i < aux.getPerfis().size(); i++){
                                                Perfil p = aux.getPerfis().get(i);
                                                if(p.getWindow_perfil() && p.getLight_Perfil()){
                                                System.out.print("Modo " + i + ": " + p.getName_perfil()
                                                        + " com janela aberta e luz ligada.\n");
                                                    //aux.setWindow(true);
                                                    //aux.setLight(true);
                                                }
                                                if(!p.getLight_Perfil() && p.getWindow_perfil()) {
                                                System.out.print("Modo " + i + ": " + p.getName_perfil()
                                                        + " com janela aberta e luz desligada.\n");
                                                    //aux.setWindow(true);
                                                }
                                                if(p.getLight_Perfil() && !p.getWindow_perfil()){
                                                System.out.print("Modo " + i + ": " + p.getName_perfil()
                                                        + " com janela fechada e luz ligada.\n");
                                                    //aux.setLight(true);
                                                }
                                                if(!p.getLight_Perfil() && !p.getWindow_perfil()){
                                                System.out.print("Modo " + i + ": " + p.getName_perfil()
                                                        + " com janela fechada e luz desligada.\n");
                                                    //aux.setWindow(false);
                                                    //aux.setLight(false);
                                                }
                                            }
                                            //System.out.print("\n");
                                            bedroom.setPerfis(aux.getPerfis());
                                        }
                                        //if(!man){
                                            bedroom = m.getBedroomHelper();
                                        //}

                                    }

                                } else if (m.getDivisao() == KITCHEN) {

                                    div = "KITCHEN";

                                    KitchenHelper aux = m.getKitchenHelper();

                                    if(kitchen != null && aux != null){
                                        if(kitchen.isLight() != aux.isLight()){
                                            if(kitchen.isLight()){
                                                System.out.print("Luz da cozinha desligada!\n");
                                            }else {
                                                System.out.print("Luz da cozinha ligada!\n");
                                            }
                                            kitchen.setLight(aux.isLight());
                                        }

                                        if(kitchen.isWindow() != aux.isWindow()){
                                            if(kitchen.isWindow()){
                                                System.out.print("Janela da cozinha fechada!\n");
                                            }else {
                                                System.out.print("Janela da cozinha aberta!\n");
                                            }
                                            kitchen.setWindow(aux.isWindow());
                                        }

                                        if(kitchen.isMicrowave() != aux.isMicrowave()){
                                            if(kitchen.isMicrowave()){
                                                System.out.print("Microondas desligado!\n");
                                            }else {
                                                System.out.print("Microondas ligado!\n");
                                            }
                                            kitchen.setMicrowave(aux.isMicrowave());
                                        }

                                        if(kitchen.isForno() != aux.isForno()){
                                            if(kitchen.isForno()){
                                                System.out.print("Forno desligado!\n");
                                            }else {
                                                System.out.print("Forno ligado!\n");
                                            }
                                            kitchen.setForno(aux.isForno());
                                        }

                                        if(kitchen.getTempForno() != aux.getTempForno()){
                                            System.out.print("Temperatura do forno a: "+aux.getTempForno()+"graus\n");
                                            kitchen.setTempForno(aux.getTempForno());
                                        }

                                        kitchen = m.getKitchenHelper();
                                    }

                                } else if (m.getDivisao() == BATH) {

                                    div = "BATH";
                                    System.out.print(div+"\n");

                                    BathHelper aux = m.getBathHelper();

                                    if(bath != null && aux != null){
                                        if(bath.isLight() != aux.isLight()){
                                            if(bath.isLight()){
                                                System.out.print("Luz do wc desligada!\n");
                                            }else {
                                                System.out.print("Luz do wc ligada!\n");
                                            }
                                            bath.setLight(aux.isLight());
                                        }

                                        if(bath.getQuantity() != aux.getQuantity()){
                                            System.out.print("Quantidade de água da banheira a: "+aux.getQuantity()+"%\n");
                                            bath.setQuantity(aux.getQuantity());
                                        }

                                        if(bath.getTemperature() != aux.getTemperature()){
                                            System.out.print("Temperatura de água da banheira a: "+aux.getTemperature()+"graus\n");
                                            bath.setTemperature(aux.getTemperature());
                                        }

                                        bath = m.getBathHelper();
                                    }


                                }
                                //é aqui que ele ao entrar numa divisao acende a luz!
                                if (m.getDivisao() == m.getDiv() && auto) {
                                    current_point = m.getDiv();
                                    if (!goodTime || !day) {
                                        if (current_point == ROOM) {
                                            room.setLight(true);
                                            System.out.println("Entrei na sala e liguei a luz!");
                                            Thread t = new Thread() {

                                                public void run() {
                                                    try {
                                                        Socket s = new Socket(IP, 4444);
                                                        ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                                        Mensagem toSendMsg = new Mensagem(room, bedroom, kitchen, bath);
                                                        //System.out.print("PARA ENVIAR: " + toSendMsg +"\n");
                                                        dos.writeObject(toSendMsg);
                                                        dos.flush();
                                                        dos.close();
                                                        s.close();
                                                    } catch (UnknownHostException e) {
                                                        e.printStackTrace();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };
                                            t.start();
                                        } else if (current_point == BEDROOM) {
                                            bedroom.setLight(true);
                                            System.out.println("Entrei no quarto e liguei a luz!");
                                            Thread t = new Thread() {

                                                public void run() {
                                                    try {
                                                        Socket s = new Socket(IP, 4444);
                                                        ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                                        Mensagem toSendMsg = new Mensagem(room, bedroom, kitchen, bath);
                                                        //System.out.print("PARA ENVIAR: " + toSendMsg +"\n");
                                                        dos.writeObject(toSendMsg);
                                                        dos.flush();
                                                        dos.close();
                                                        s.close();
                                                    } catch (UnknownHostException e) {
                                                        e.printStackTrace();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };
                                            t.start();
                                        } else if (current_point == KITCHEN) {
                                            kitchen.setLight(true);
                                            System.out.println("Entrei na cozinha e liguei a luz!");
                                            Thread t = new Thread() {

                                                public void run() {
                                                    try {
                                                        Socket s = new Socket(IP, 4444);
                                                        ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                                        Mensagem toSendMsg = new Mensagem(room, bedroom, kitchen, bath);
                                                        //System.out.print("PARA ENVIAR: " + toSendMsg +"\n");
                                                        dos.writeObject(toSendMsg);
                                                        dos.flush();
                                                        dos.close();
                                                        s.close();
                                                    } catch (UnknownHostException e) {
                                                        e.printStackTrace();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };
                                            t.start();
                                        } else if (current_point == BATH) {
                                            System.out.println("Entrei na casa de banho e liguei a luz!");
                                            bath.setLight(true);
                                            Thread t = new Thread() {

                                                public void run() {
                                                    try {
                                                        Socket s = new Socket(IP, 4444);
                                                        ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                                        Mensagem toSendMsg = new Mensagem(room, bedroom, kitchen, bath);
                                                        //System.out.print("PARA ENVIAR: " + toSendMsg +"\n");
                                                        dos.writeObject(toSendMsg);
                                                        dos.flush();
                                                        dos.close();
                                                        s.close();
                                                    } catch (UnknownHostException e) {
                                                        e.printStackTrace();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };
                                            t.start();
                                        }
                                    }
                                }



                                // se modo automatico, auto = true, sen�o auto = false!
                                // adicionar os 4 access Points por ordem.
                            }
                            dis.close();
                            s.close();
                        }



                    } catch(EOFException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();

                    }

            }
            };
            t.start();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(auto){
                        if (!day && goodTime) {
                            System.out.print("NOITE e BOM TEMPO\n");

                            goodTime = false;

                            if(current_point == ROOM){
                                room.setLight(true);
                                room.setWindow(false);
                            } else if(current_point == BEDROOM){
                                bedroom.setLight(true);
                                bedroom.setWindow(false);
                            } else if(current_point == KITCHEN){
                                kitchen.setLight(true);
                                kitchen.setWindow(false);
                            } else if(current_point == BATH){
                                System.out.print("APANHEI");
                                bath.setLight(true);
                            }

                            Thread t = new Thread() {

                                public void run() {
                                    try {
                                        Socket s = new Socket(IP, 4444);
                                        ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                        Mensagem toSendMsg = new Mensagem(room, bedroom, kitchen, bath);
                                        //System.out.print("PARA ENVIAR: " + toSendMsg +"\n");
                                        dos.writeObject(toSendMsg);
                                        dos.flush();
                                        dos.close();
                                        s.close();
                                    } catch (UnknownHostException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            t.start();

                        } else if (!day && !goodTime) {
                            System.out.print("NOITE e MAU TEMPO\n");

                            day = true;

                            if(current_point == ROOM){
                                room.setLight(true);
                                room.setWindow(false);
                            } else if(current_point == BEDROOM){
                                bedroom.setLight(true);
                                bedroom.setWindow(false);
                            } else if(current_point == KITCHEN){
                                kitchen.setLight(true);
                                kitchen.setWindow(false);
                            } else if(current_point == BATH){
                                bath.setLight(true);
                            }

                            Thread t = new Thread() {

                                public void run() {
                                    try {
                                        Socket s = new Socket(IP, 4444);
                                        ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                        Mensagem toSendMsg = new Mensagem(room, bedroom, kitchen, bath);
                                        //System.out.print("PARA ENVIAR: " + toSendMsg +"\n");
                                        dos.writeObject(toSendMsg);
                                        dos.flush();
                                        dos.close();
                                        s.close();
                                    } catch (UnknownHostException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            t.start();

                        } else if(day && !goodTime) {

                            System.out.print("DIA e MAU TEMPO\n");

                            goodTime = true;

                            if(current_point == ROOM){
                                room.setLight(true);
                                room.setWindow(false);
                            } else if(current_point == BEDROOM){
                                bedroom.setLight(true);
                                bedroom.setWindow(false);
                            } else if(current_point == KITCHEN){
                                kitchen.setLight(true);
                                kitchen.setWindow(false);
                            } else if(current_point == BATH){
                                bath.setLight(true);
                            }

                            Thread t = new Thread() {

                                public void run() {
                                    try {
                                        Socket s = new Socket(IP, 4444);
                                        ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                        Mensagem toSendMsg = new Mensagem(room, bedroom, kitchen, bath);
                                        //System.out.print("PARA ENVIAR: " + toSendMsg +"\n");
                                        dos.writeObject(toSendMsg);
                                        dos.flush();
                                        dos.close();
                                        s.close();
                                    } catch (UnknownHostException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            t.start();

                        } else {

                            System.out.print("DIA e BOM TEMPO\n");

                            day = false;

                            if(current_point == ROOM){
                                room.setLight(false);
                                room.setWindow(true);
                            } else if(current_point == BEDROOM){
                                bedroom.setLight(false);
                                bedroom.setWindow(true);
                            } else if(current_point == KITCHEN){
                                kitchen.setLight(false);
                                kitchen.setWindow(true);
                            } else if(current_point == BATH){
                                bath.setLight(false);
                            }

                            Thread t = new Thread() {

                                public void run() {
                                    try {
                                        Socket s = new Socket(IP, 4444);
                                        ObjectOutputStream dos = new ObjectOutputStream((s.getOutputStream()));
                                        Mensagem toSendMsg = new Mensagem(room, bedroom, kitchen, bath);
                                        //System.out.print("PARA ENVIAR: " + toSendMsg +"\n");
                                        dos.writeObject(toSendMsg);
                                        dos.flush();
                                        dos.close();
                                        s.close();
                                    } catch (UnknownHostException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            t.start();
                        }
                    }
            }
        }, 30000, 30000);

//        Thread t2 = new Thread(){
//            final Timer timer = new Timer();
//            //if (auto) {
//            //System.out.print("AQUIIIIIIIIi");
//            class dayNight extends TimerTask {
//                public void run() {
//
//                                                /*Socket s = new Socket(IP, 4444);
//                                                ObjectOutputStream dos = new ObjectOutputStream(
//                                                        (s.getOutputStream()));*/
//                    if(auto){
//                        if (!day && goodTime) {
//                            System.out.print("NOITE e BOM TEMPO\n");
//                            day = true;
//                            //dos.writeUTF("Boa noite!");
//                            room.setLight(false);
//                            room.setWindow(true);
//                            bedroom.setLight(false);
//                            bedroom.setWindow(true);
//                            kitchen.setLight(false);
//                            kitchen.setWindow(true);
//                        } else if (!day && !goodTime) {
//                            System.out.print("NOITE e MAU TEMPO\n");
//                            day = true;
//                            //dos.writeUTF("Bom Dia!");
//                            room.setWindow(false);
//                            bedroom.setWindow(false);
//                            kitchen.setWindow(false);
//                            //  passa para dia e fecha janelas e abre a luz se tivermos nessa divisao
//                        } else {
//                            System.out.print("DIA e BOM TEMPO\n");
//                            day = false;
//                            room.setLight(false);
//                            room.setWindow(false);
//                            bedroom.setLight(false);
//                            bedroom.setWindow(false);
//                            kitchen.setWindow(false);
//                            kitchen.setLight(false);
//                            // fechar janelas na app
//                            // acender luz na divisao corrente
//                        }
//                    }
//
//
//                    //dos.flush();
//                    //dos.close();
//                    //s.close();
//
//                }
//            }
//
//            class goodTimebadTime extends TimerTask {
//                public void run() {
//                                            /*try {
//                                                Socket s = new Socket(IP, 4444);
//                                                ObjectOutputStream dos = new ObjectOutputStream(
//                                                        (s.getOutputStream()));*/
//
//                    if(auto) {
//                        if (!day && !goodTime) {
//                            goodTime = true;
//                            //dos.writeUTF("Bom tempo, aproveite o dia para ir lá fora!");
//                            room.setLight(false);
//                            room.setWindow(true);
//                            bedroom.setLight(false);
//                            bedroom.setWindow(true);
//                            kitchen.setLight(false);
//                            kitchen.setWindow(true);
//                            time = 80000;
//                        } else if (!day && goodTime) {
//                            goodTime = false;
//                            //dos.writeUTF("Chegou a tempestade!");
//                            room.setWindow(false);
//                            bedroom.setWindow(false);
//                            kitchen.setWindow(false);
//                            time = 60000;
//                            //  passa para dia e fecha janelas e abre a luz se tivermos nessa divisao
//                        } else if (day && goodTime) {
//                            goodTime = false;
//                            room.setLight(false);
//                            room.setWindow(false);
//                            bedroom.setLight(false);
//                            bedroom.setWindow(false);
//                            kitchen.setWindow(false);
//                            kitchen.setLight(false);
//                            time = 60000;
//                            // fechar janelas na app
//                            // acender luz na divisao corrente
//                        } else {
//                            goodTime = true;
//                            time = 80000;
//                        }
//                    }
//
//                    //dos.flush();
//                    //dos.close();
//                    //s.close();
//                    //}
//                }
//            }
//
//            timer.schedule(new dayNight(), 0, 60000);
//            timer.schedule(new goodTimebadTime(), 0, time);
//
//            };
//            t2.start();
        }


    }

