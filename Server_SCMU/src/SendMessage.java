import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SendMessage {
	
	private Socket client;
    private PrintWriter printwriter;

	protected Void doInBackground(String messsage) {
        try {

            client = new Socket("192.168.0.101", 8080); // connect to the server
            printwriter = new PrintWriter(client.getOutputStream(), true);
            printwriter.write(messsage); // write the message to output stream

            printwriter.flush();
            printwriter.close();
            client.close(); // closing the connection

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	
}
