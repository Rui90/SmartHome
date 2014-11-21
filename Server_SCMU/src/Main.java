import java.awt.List;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

	private static boolean day = true; 
	private static boolean goodTime = true;
	private static ArrayList<String> divisoes = new ArrayList<String>(); 
	private static boolean auto = true; 
	private static int time = 20000;
	private static Room room = new Room(false, false, false, false);
	private static Bedroom bedroom = new Bedroom(false, false, 1); 
	private static Bath bath = new Bath(false);
	private static Kitchen kitchen = new Kitchen(false, false); 
	private static ArrayList<AccessPoint> access = new ArrayList<AccessPoint>(); 
	
	
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
						DataInputStream dis = new DataInputStream(
								s.getInputStream());
					
						//cria-se uma nova mensagem
						Mensagem m = new Mensagem(1, 2, true);
						if(m.getDivisao() == 0) {
							
						} else if(m.getDivisao() == ROOM){
							if(m.getElemencto() == WINDOW){
								room.setWindow(m.getCondicao());
							}else if(m.getDivisao() == LIGHT){
								room.setLight(m.getCondicao());
							}								
						}else if(m.getDivisao() == BEDROOM){
							if(m.getElemencto() == WINDOW){
								bedroom.setWindow(m.getCondicao());
							}else if(m.getDivisao() == LIGHT){
								bedroom.setLight(m.getCondicao());
							}		
						}else if(m.getDivisao() == KITCHEN){
							if(m.getElemencto() == WINDOW){
								kitchen.setWindow(m.getCondicao());
							}else if(m.getDivisao() == LIGHT){
								kitchen.setLight(m.getCondicao());
							}		
						}else if(m.getDivisao() == BATH ){
							if(m.getElemencto() == LIGHT){
								bath.setLight(m.getCondicao());
							}								
						}
						// se modo automatico, auto = true, senão auto = false! 
						// adicionar os 4 access Points por ordem.
						
						System.out.println(dis.readUTF());
						dis.close();
						s.close();
					}
				} catch (IOException e) {

				}
			}
		};
		t.start();

		class dayNight extends TimerTask {
			public void run() {
					try {
						Socket s = new Socket("192.168.1.101", 4444);
						DataOutputStream dos = new DataOutputStream(
							(s.getOutputStream()));
						
						if(!day && goodTime) {
							day = true; 
							dos.writeUTF("É de noite!");
							room.setLight(false);
							room.setWindow(true);
							bedroom.setLight(false);
							bedroom.setWindow(true);
							kitchen.setLight(false);
							kitchen.setWindow(true);
						} else if(!day && !goodTime){
							day = true; 
							dos.writeUTF("É de dia!");
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
						Socket s = new Socket("192.168.1.101", 4444);
						DataOutputStream dos = new DataOutputStream(
							(s.getOutputStream()));
						
						if(!day && !goodTime) {
							goodTime = true; 
							dos.writeUTF("É de noite!");
							room.setLight(false);
							room.setWindow(true);
							bedroom.setLight(false);
							bedroom.setWindow(true);
							kitchen.setLight(false);
							kitchen.setWindow(true);
							time = 20000; 
						} else if(!day && goodTime){
							goodTime = false; 
							dos.writeUTF("É de dia!");
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

		//String command = "";
		/*while (true) {
			//command = in.next();
			//System.out.println(command);
			final Thread tt = new Thread() {

					public void run() {
						while(true) {
							try {
								Socket s = new Socket("192.168.0.100", 4444);
								DataOutputStream dos = new DataOutputStream(
									(s.getOutputStream()));
								
								if(day) {
									day = false; 
									dos.writeUTF("É de noite!");
								} else {
									day = true; 
									dos.writeUTF("É de dia!");
								}
								dos.flush();
								dos.close();
								s.close();
								//Thread.sleep(60000);
						} catch (IOException e) {

						} 
					}
				}
			};
			tt.start();
			Thread.sleep(10000);*/
		}


	}

	/*
	 * public static void main(String[] args) { try { InetAddress addr =
	 * InetAddress.getByName("192.168.0.100"); serverSocket = new
	 * ServerSocket(4444, 50, addr); // Server socket
	 * 
	 * } catch (IOException e) {
	 * System.out.println("Could not listen on port: 4444"); }
	 * 
	 * System.out.println("Server started. Listening to the port 4444");
	 * 
	 * while (true) { try {
	 * 
	 * clientSocket = serverSocket.accept(); // accept the client connection
	 * outputStreamReader = new
	 * ObjectOutputStream(clientSocket.getOutputStream()); inputStreamReader =
	 * new ObjectInputStream(clientSocket.getInputStream()); //bufferedReader =
	 * new BufferedReader(inputStreamReader); // get the client message
	 * 
	 * outputStreamReader.writeChars("O Gui é gay!");
	 * outputStreamReader.flush();
	 * 
	 * message = bufferedReader.readLine();
	 * 
	 * if(message != null) { System.out.println(message); }
	 * /*if(message.equals("Ligar luz")){ System.out.println("Luzes ligadas!");
	 * } else if(message.equals("Desligar luz")){
	 * System.out.println("Luzes desligadas!"); } else
	 * if(message.equals("Ligar arcondicionado")){
	 * System.out.println("O ar condicionado acabou de ser ligado!"); } else
	 * if(message.equals("Desligar arcondicionado")){
	 * System.out.println("Ar condicionado desligado!"); } else
	 * if(message.equals("Abrir janela")){ System.out.println("Janela aberta!");
	 * } else if(message.equals("Fechar janela")){
	 * System.out.println("Janela fechada!"); } else {
	 * 
	 * }
	 */
	/*
	 * inputStreamReader.close(); clientSocket.close();
	 * 
	 * } catch (IOException ex) {
	 * System.out.println("Problem in message reading"); } }
	 * 
	 * 
	 * }
	 */
//}