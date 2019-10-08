import java.net.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;
public class DataServer extends ServerSocket{
	public DataServer() throws IOException{
		String sfilename = "Barcodes3of9.XML";
		String zfilename = "BCProducts.xml";
		String efilename = "Carts.xml";
		XMLHandler xml = new XMLHandler();
		parseBarcodeToChar(xml, sfilename);
		parsePrices(xml, zfilename);
		//showCarts(xml, efilename);
		Collections.sort(this.whycantiuseahashmap, new Comparator<String[]>(){
			public int compare(String[] strings, String[] otherStrings) {
				return strings[0].compareTo(otherStrings[0]);
			}
		});
	}
	public DataServer(int address, ArrayList<String[]> notahashmap) throws IOException{
		//arraylist should be sorted in ascending order by character acsii
		super(address);
		Collections.sort(notahashmap, new Comparator<String[]>(){
			public int compare(String[] strings, String[] otherStrings) {
				return strings[0].compareTo(otherStrings[0]);
			}
		});
		this.whycantiuseahashmap = notahashmap;
	}
	public ArrayList<String[]> whycantiuseahashmap = new ArrayList<String[]>();

	public double interpret(String barcode) {
		String after = "1.00";
		for(int i = 0;i<whycantiuseahashmap.size();i++) {
			if (whycantiuseahashmap.get(i)[0].equals(barcode)) {
				after=whycantiuseahashmap.get(i)[1];
				break;
			}
		}
		return Double.parseDouble(after);


	}
	LinkedList<Node> cartline = new LinkedList<Node>();
	Map<BitSet, String> mapBarcodeToChar = new HashMap<>();
	Map<String, Double> mapNameToPrice = new HashMap<>();
	public BitSet ToBitset (String before, String markzero, String markone) {
		BitSet after = new BitSet();
		int rsc = 0;
		for (int i = 0;i<before.length();i++) {

			if (before.substring(rsc,rsc+1).equals(markzero)) {
				after.set(rsc,false);
				rsc++;
			}
			else if (before.substring(rsc, rsc+1).equals(markone)) {
				after.set(rsc, true);
				rsc++;
			}
		}
		return after;
	}
	public String readBarcode3of9(String barcode) {
		String after = new String();
		for (int i=0;i<barcode.length();i+=9) {
			after+=mapBarcodeToChar.get(ToBitset(barcode.substring(i,i+9),"0","1"));
		}
		return after;
	}
	public void parsePrices(XMLHandler xml, String zfile) {
		Document doc;
		String[] priceField = new String[]{"Price"};
		String[] barcodeField = new String[]{"Barcode"};
		try {
			doc = xml.ReadXML(zfile);
			NodeList nodelist = xml.GetNodes(doc,"Product");
			ArrayList<String> barcodeStrings = xml.GetNodeStrings(nodelist, barcodeField);
			ArrayList<String> priceStrings = xml.GetNodeStrings(nodelist, priceField);

			//System.out.println(priceStrings);
			if (priceStrings.size()!=barcodeStrings.size()) {
				System.out.println("Error! Different numbers of prices and barcodes!");
			}
			for (int i = 0; i<priceStrings.size();i++) {
				String[] a = new String[] {barcodeStrings.get(i),priceStrings.get(i)};

				whycantiuseahashmap.add(a);
			}

		}
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void parseBarcodeToChar(XMLHandler xml, String sfile) {
		Document doc;
		String[] charField = new String[]{"Character"};
		String[] binaryField = new String[]{"Binary"};
		try {
			doc = xml.ReadXML(sfile);
			NodeList nodelist = xml.GetNodes(doc,"Symbol");
			ArrayList<String> charStrings = xml.GetNodeStrings(nodelist, charField);
			ArrayList<String> binaryStrings = xml.GetNodeStrings(nodelist, binaryField);

			//System.out.println(charStrings);
			if (charStrings.size()!=binaryStrings.size()) {
				System.out.println("There are different numbers of characters and binaries!");
				return;
			}
			for (int i = 0; i<charStrings.size();i++) {
				mapBarcodeToChar.put(ToBitset(binaryStrings.get(i), "0","1"),charStrings.get(i));
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void showCarts(XMLHandler xml, String efile) {
		Document doc;
		// String[] productField = new String[]{"item"};
		try {
			doc = xml.ReadXML(efile);
			NodeList nodelist = xml.GetNodes(doc,"Cart");
			for (int z=0;z<nodelist.getLength();z++) {
				cartline.add(nodelist.item(z));
			}


		}
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

	}
	public void connect() {
		try(ServerSocket serversocket = new ServerSocket(3214)){
			try {
				Socket connectionSocket = serversocket.accept();
				System.out.println("Server connected");
				DataInputStream input; 
				input = new DataInputStream(connectionSocket.getInputStream()); 
				PrintStream output; 
				output = new PrintStream(connectionSocket.getOutputStream());
				DataOutputStream out;
				out = new DataOutputStream(connectionSocket.getOutputStream());
				BufferedReader d = new BufferedReader(new InputStreamReader(input));

				String read = null;
				double z = 0.0;

				while ((read=d.readLine()) != null) {
					System.out.print(readBarcode3of9(read) + "     ");
					out.writeDouble(z=interpret(read));
					out.flush();

				}
				output.close();
				input.close(); 
				connectionSocket.close(); 
			}finally {
				serversocket.close(); 
			}
		}
		catch(IOException except) {
			except.printStackTrace();
		}
		finally {

		}
	}
}
