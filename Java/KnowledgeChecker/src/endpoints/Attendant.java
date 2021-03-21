package endpoints;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import contexts.AttendantContext;
import contexts.MemberContext;
import contexts.ServerContext;
import contexts.SpeakerContext;

/**
 * Servlet implementation class Attendant
 */
@WebServlet("/Attendant")
public class Attendant extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ServerContext _serverContext = null;
	
    public void init()
            throws ServletException {
    	_serverContext = (ServerContext) getServletContext().getAttribute("context");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		String output = "???";
		
		if (cmd == null) {
			output = "##Error##: no ?cmd= param!";
		} else if (cmd.equalsIgnoreCase("ping")) {
			output = doCmdPing(request, response);
		} else if (cmd.equalsIgnoreCase("login")) {
			output = doCmdLogin(request, response);
		} else  if (cmd.equalsIgnoreCase("logout")) {
			output = doCmdLogout(request, response);
		} else {
			output = String.format("##Error##: '%s' not supported!", cmd);
		}
		
        request.setAttribute("output", output);
        request.getRequestDispatcher("Attendant.jsp").forward(request, response);
	}
	
	private String doCmdPing(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String ipAddress = request.getRemoteAddr();
		String output = "???";
		MemberContext memberContext = _serverContext.getMember(ipAddress);
		if (memberContext == null) {
			output = String.format("##Err##: logout failure for '???@%s'.", ipAddress);
		} else {
			memberContext.touch();
			output = String.format("[%d] Pong to '%s'!", _serverContext.tickCount(), memberContext);
		}
		
		return output;
	}
	
	private String doCmdLogin(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		String ipAddress = request.getRemoteAddr();
		String output = "???";
		if (name == null || name.length() == 0) {
			output += "##Err##: no &name= param!";
		} else {
			AttendantContext attendantContext = new AttendantContext(name, ipAddress);
			if (_serverContext.loginMember(attendantContext) == null) {
				output = String.format("##Err##: login failure for attendant '%s'!", attendantContext.toString());
			} else {
				output =  String.format("Attendant '%s' logged in!", attendantContext.toString()); 
			}
		}
		
		return output.toString();
	}
	
	private String doCmdLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String ipAddress = request.getRemoteAddr();
		String output = "???";
		MemberContext memberContext = _serverContext.logoutMember(ipAddress);
		if (memberContext == null) {
			output = String.format("##Err##: logout failure for 'member@%s'!", ipAddress);
		} else {
			output =  String.format("Attendant '%s' logged out!", memberContext.toString()); 
		}
		
		return output.toString();
	}
}
