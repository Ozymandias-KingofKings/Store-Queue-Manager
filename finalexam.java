import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.Socket;
import java.net.SocketException;

public class finalexam {
	public void connect() {
		try{
			Socket client; 
			client = new Socket("localhost", 3214) ;
			//whatever stuff the client does
			client.close();
		}
		catch(SocketException se) {
			se.printStackTrace();
		}
		catch(IOException i) {
			i.printStackTrace();
		}
		
	}
	public void showProperties(BeanInfo z) {
		PropertyDescriptor[] a = z.getPropertyDescriptors();
		for (int i=0;i<a.length;i++) {
			System.out.println(a[i].getReadMethod().toString());
			System.out.println(a[i].getWriteMethod().toString());
		}
	}
	public void showType(Object obj) {
		Class c = obj.getClass();
		System.out.println();
		Method[] methods = c.getMethods(); 

        for (Method method:methods) 
            System.out.println(method.getName());
	}
	
	
	public String replaceOzy(String ozy) {
		return ozy.replaceAll("\\b(and)\\b", " between");
	}
	public static void main(String[] args) {
		finalexam aha = new finalexam();
		System.out.println(aha.replaceOzy("Ozymandias\r\n" + 
				"\r\n" + 
				"I met a traveller from an antique land\r\n" + 
				"Who said: Two vast and trunkless legs of stone\r\n" + 
				"Stand in the desart. Near them, on the sand,\r\n" + 
				"Half sunk, a shattered visage lies, whose frown,\r\n" + 
				"And wrinkled lip, and sneer of cold command,\r\n" + 
				"Tell that its sculptor well those passions read \r\n" + 
				"Which yet survive, stamped on these lifeless things,\r\n" + 
				"The hand that mocked them and the heart that fed:\r\n" + 
				"And on the pedestal these words appear:\r\n" + 
				"\"My name is Ozymandias, king of kings:\r\n" + 
				"Look on my works, ye Mighty, and despair!\"\r\n" + 
				"Nothing beside remains. Round the decay\r\n" + 
				"Of that colossal wreck, boundless and bare\r\n" + 
				"The lone and level sands stretch far away.\r\n" + 
				"-Percy Bysshe Shelley\r\n" + 
				"\r\n" + 
				""));
	}
}
