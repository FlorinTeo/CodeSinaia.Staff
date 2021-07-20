package endpoints;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class IRControl
 */
@WebServlet(description = "Instant Reaction Control Panel", urlPatterns = { "/IRControl" })
public class IRControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IRControl() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * IRServer REST API: http://localhost:8080/InstantReaction/IRServer?cmd={command}
     * 
     * Supported commands:
     *     ?cmd=status: returns a list of all Hosts and Guests logged into the site
     *     
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        String answer;
        
        if (cmd == null) {
            answer = "IRControl_Error: (null) command is invalid!";
        } else if (cmd.equalsIgnoreCase("status")) {
            answer = doCmdStatus(request, response);
        } else {
            answer = String.format("IRControl_Error: Command {%s} is not supported!", cmd);
        }
        
        response.getWriter().print(answer);
    }
    
    /**
     * IRServer ?cmd=status handler: http://localhost:8080/InstantReaction/IRServer?cmd=status
     */
    private String doCmdStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO: handling the "status" command
        return "IRControl_TODO: {status} command processor to be implemented!";
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
