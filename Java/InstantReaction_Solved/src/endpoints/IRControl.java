package endpoints;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import contexts.ServerContext;
import schemas.JsonStatus;

/**
 * Servlet implementation class IRControl
 */
@WebServlet(description = "Instant Reaction Control Panel", urlPatterns = { "/IRControl" })
public class IRControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // ServerContext shared across all servlets
    private ServerContext _serverContext = null;
    
    /**
     * On initialization retrieve and retain _serverContext 
     */
    public void init() throws ServletException {
        _serverContext = (ServerContext) getServletContext().getAttribute("context");
    }
    
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
        JsonStatus result = new JsonStatus();
        
        if (cmd == null) {
            result.Success = false;
            result.Message = "IRControl_Error: (null) command is invalid!";
        } else if (cmd.equalsIgnoreCase("status")) {
            result = doCmdStatus(request, response);
        } else {
            result.Success = false;
            result.Message = String.format("IRControl_Error: Command {%s} is not supported!", cmd);
        }
        
        String answer = (new Gson()).toJson(result);
        response.getWriter().print(answer);
    }
    
    /**
     * IRServer ?cmd=status handler: http://localhost:8080/InstantReaction/IRServer?cmd=status
     */
    private JsonStatus doCmdStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Returns the complete server status in json form.
        return _serverContext.toJson();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
