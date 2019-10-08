import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Lab3 {
	public static void main(String[] args) {
		ArrayList<String> thing = new ArrayList<String>();
		String filename = "Carts.csv";
		String line = null;
		try {
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				String test = line;
				String patternString = "([01]+)";

				Pattern pattern = Pattern.compile(patternString);

				Matcher matcher = pattern.matcher(test);

				//System.out.println(matcher.groupCount());
				int groupCount = matcher.groupCount();

				while (matcher.find()) {
					for (int i=0;i<groupCount;i++) {
						thing.add(matcher.group(i));
					}
				}
			}
			Client client = new Client(thing);
			DataServer server = new DataServer();
			Thread thread = new Thread() {
				public void run() {
					client.connect();
				}
			};
			Thread thread2 = new Thread() {
				public void run() {
					server.connect();
				}
			};
			thread.start();
			thread2.start();
		}
		catch(IOException a) {

		}



	}
}
