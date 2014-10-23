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
 
				clientSocket = serverSocket.accept(); // accept the client connection
				inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
				bufferedReader = new BufferedReader(inputStreamReader); // get the client message
				
				message = bufferedReader.readLine();
				
				if(message.equals("Ligar luz")){
					System.out.println("Luzes ligadas!");
				} else if(message.equals("Desligar luz")){
					System.out.println("Luzes desligadas!");
				} else if(message.equals("Ligar arcondicionado")){
					System.out.println("O ar condicionado acabou de ser ligado!");
				} else if(message.equals("Desligar arcondicionado")){
					System.out.println("Ar condicionado desligado!");
				} else if(message.equals("Abrir janela")){
					System.out.println("Janela aberta!");
				} else if(message.equals("Fechar janela")){
					System.out.println("Janela fechada!");
				} else {
					
				}
				
				inputStreamReader.close();
				clientSocket.close();
 
			} catch (IOException ex) {
				System.out.println("Problem in message reading");
			}
		}

 
	}
 
}