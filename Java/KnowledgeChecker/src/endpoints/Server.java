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
 * Servlet implementation class Control
 */
@WebServlet("/Server")
public class Server extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServerContext _serverContext = null;
    
    public void init() throws ServletException {
        _serverContext = (ServerContext) getServletContext().getAttribute("context");
    }
    
    /**
     * Handles all GET commands for this servlet
     * http://localhost:8080/KnowledgeChecker/Server...
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        JsonStatus jsonStatus;

        if (cmd == null) {
            jsonStatus = doFail(request, response);
        } else if (cmd.equalsIgnoreCase("status")) {
            jsonStatus = doCmdStatus(request, response);
        } else {
            jsonStatus = doFail(request, response);
        }
        
        String answer = (new Gson()).toJson(jsonStatus);
        response.getWriter().print(answer);
    }
    
    /**
     * Handles missing or unrecognized commands
     * http://localhost:8080/KnowledgeChecker/Speaker?abc=xyz&...
     */
    private JsonStatus doFail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonStatus jsonStatus = new JsonStatus(request.getRemoteAddr());
        jsonStatus.Assert(false, "Error: Command missing or unsupported!");
        return jsonStatus;
    }
    
    /**
     * Handles Speaker ping:
     * http://localhost:8080/KnowledgeChecker/Server?cmd=status
     */
    private JsonStatus doCmdStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Serialize server context in JSON
        return _serverContext.toJsonSchema(request.getRemoteAddr());
    }
}
