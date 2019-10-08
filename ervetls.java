import java.io.PrintWriter;

import javax.servlet.*

public class ervetls extends Servlet{
	public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException{
		//container will call service method so that servlet can handle ServletRequest req
		PrintWriter output = resp.getWriter();
		output.println("this is how you respond to a client request");
	}
}
