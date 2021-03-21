package endpoints;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import contexts.ServerContext;

/**
 * Servlet implementation class Control
 */
@WebServlet(description = "Control channel", urlPatterns = { "/Control" })
public class Control extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ServerContext _serverContext = null; 

    public void init()
            throws ServletException {
    	_serverContext = (ServerContext) getServletContext().getAttribute("context");
    }
    
	/**
	 * http://localhost:8080/KnowledgeChecker/Control?cmd={cmd_param}
	 * @see Control#doCmdContext(HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		String output = "???";
		
		if (cmd == null) {
			output = "##Error##: no ?cmd= param!";
		} else if (cmd.equalsIgnoreCase("context")) {
			output = doCmdContext(response);
		} else {
			output = String.format("##Error##: '%s' not supported!", cmd);
		}
		
        request.setAttribute("output", output);
        request.getRequestDispatcher("index.jsp").forward(request, response);
	}
	
	/**
	 * ?cmd=context
	 * Returns full server context, in html form
	 * @see Control#doGet(HttpServletRequest, HttpServletResponse)
	 */
	private String doCmdContext(HttpServletResponse response) throws IOException {
		return _serverContext.toString();
	}
}
