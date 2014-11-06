import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	private static ServerSocket serverSocket;
	private static Socket clientSocket;
	private static InputStreamReader inputStreamReader;
	private static BufferedReader bufferedReader;
	private static String message;

	private static boolean lamp_room, window, tv, lamp_wc, lamp_kitchen,
			window_kitchen, lamp_bedroom, window_bedroom = false;

	public static void main(String[] args) {
		try {
			InetAddress addr = InetAddress.getByName("192.168.0.100");
			serverSocket = new ServerSocket(4444, 50, addr); // Server socket

		} catch (IOException e) {
			System.out.println("Could not listen on port: 4444");
		}

		System.out.println("Server started. Listening to the port 4444");

		while (true) {
			try {

				clientSocket = serverSocket.accept(); 
				inputStreamReader = new InputStreamReader(
						clientSocket.getInputStream());
				bufferedReader = new BufferedReader(inputStreamReader); 

				message = bufferedReader.readLine();

				if (message.equals("Ligar luz")) {
					if (lamp_room) {
						System.out.println("Luzes da sala desligadas!");
						lamp_room = false;
					} else {
						System.out.println("Luzes da sala ligadas!");
						lamp_room = true;
					}
				} else if (message.equals("Desligar luz")) {
					if (lamp_room) {
						System.out.println("Luzes da sala desligadas!");
						lamp_room = false;
					} else {
						System.out.println("Luzes da sala ligadas!");
						lamp_room = true;
					}
				} 
				
				else if (message.equals("Ligar luz2")) {
					if (lamp_wc) {
						System.out.println("Luzes do wc desligadas!");
						lamp_wc = false;
					} else {
						System.out.println("Luzes do wc ligadas!");
						lamp_wc = true;
					}
				} else if (message.equals("Desligar luz2")) {
					if (lamp_wc) {
						System.out.println("Luzes do wc desligadas!");
						lamp_wc = false;
					} else {
						System.out.println("Luzes do wc ligadas!");
						lamp_wc = true;
					}
				} 
				
				else if (message.equals("Ligar luz3")) {
					if (lamp_kitchen) {
						System.out.println("Luz da cozinha desligada!");
						lamp_kitchen = false;
					} else {
						System.out.println("Luz da cozinha ligada!");
						lamp_kitchen = true;
					}
				} else if (message.equals("Desligar luz3")) {
					if (lamp_kitchen) {
						System.out.println("Luz da cozinha desligada!");
						lamp_kitchen = false;
					} else {
						System.out.println("Luz da cozinha ligada!");
						lamp_kitchen = true;
					}
				} 
				
				else if (message.equals("Ligar luz4")) {
					if (lamp_bedroom) {
						System.out.println("Luz do quarto desligado!");
						lamp_bedroom = false;
					} else {
						System.out.println("Luz do quarto ligado!");
						lamp_bedroom = true;
					}
				} else if (message.equals("Desligar luz4")) {
					if (lamp_bedroom) {
						System.out.println("Luz do quarto desligado!");
						lamp_bedroom = false;
					} else {
						System.out.println("Luz do quarto ligado!");
						lamp_bedroom = true;
					}
				} 
				
				else if (message.equals("Ligar arcondicionado")) {
					System.out
							.println("O ar condicionado acabou de ser ligado!");
				} else if (message.equals("Desligar arcondicionado")) {
					System.out.println("Ar condicionado desligado!");
				} 
				
				else if (message.equals("Abrir janela")) {
					if (window) {
						System.out.println("Janela da sala fechada!");
						window = false;
					} else {
						System.out.println("Janela da sala aberta!");
						window = true;
					}
				} else if (message.equals("Fechar janela")) {
					if (window) {
						System.out.println("Janela da sala fechada!");
						window = false;
					} else {
						System.out.println("Janela da sala aberta!");
						window = true;
					}
				} 
				
				else if (message.equals("Abrir janela2")) {
					if (window_kitchen) {
						System.out.println("Janela da cozinha fechada!");
						window_kitchen = false;
					} else {
						System.out.println("Janela da cozinha aberta!");
						window_kitchen = true;
					}
				} else if (message.equals("Fechar janela2")) {
					if (window_kitchen) {
						System.out.println("Janela da cozinha fechada!");
						window_kitchen = false;
					} else {
						System.out.println("Janela da cozinha aberta!");
						window_kitchen = true;
					}
				} 
				
				else if (message.equals("Abrir janela3")) {
					if (window_bedroom) {
						System.out.println("Janela do quarto fechada!");
						window_bedroom = false;
					} else {
						System.out.println("Janela do quarto aberta!");
						window_bedroom = true;
					}
				} else if (message.equals("Fechar janela3")) {
					if (window_bedroom) {
						System.out.println("Janela do quarto fechada!");
						window_bedroom = false;
					} else {
						System.out.println("Janela do quarto aberta!");
						window_bedroom = true;
					}
				} 
				
				else if (message.equals("Ligar tv")) {
					if (tv) {
						System.out.println("Tv da sala desligada!");
						tv = false;
					} else {
						System.out.println("Tv da sala ligada!");
						tv = true;
					}
				} else if (message.equals("Desligar tv")) {
					if (tv) {
						System.out.println("Tv da sala desligada!");
						tv = false;
					} else {
						System.out.println("Tv da sala ligada!");
						tv = true;
					}
				} 
				
				else {
					System.out.println(message);
				}

				inputStreamReader.close();
				clientSocket.close();

			} catch (IOException ex) {
				System.out.println("Problem in message reading");
			}
		}

	}

}