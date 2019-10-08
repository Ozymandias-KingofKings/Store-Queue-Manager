import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
public class Client {
	public ArrayList<String> items;
	public Client(ArrayList<String> cart) {
		this.items = cart;
	}
	public String nextBarcode() {
		return items.remove(0);
	}
	public void connect() {
		try{
			Socket client; 
			client = new Socket("localhost", 3214) ; 
			try {
				System.out.println("Client connected");
				DataInputStream input; 
				input = new DataInputStream(client.getInputStream());
				PrintStream output; 
				output = new PrintStream(client.getOutputStream()); 
				DataOutputStream dataoutput; 
				dataoutput = new DataOutputStream(client.getOutputStream());
				String s = "";
				while (items.size()!=0) {
					s=nextBarcode();
					output.println(s);
					//System.out.println("8");
					System.out.println(input.readDouble());
				}
				output.close(); 
				input.close(); }
			finally {
				client.close(); 
			}
		}

		catch(IOException a) {
			a.printStackTrace();
		}

	}
}
