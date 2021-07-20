package endpoints;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class IRHost
 */
@WebServlet("/IRHost")
public class IRHost extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private boolean _stateLogin = false;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IRHost() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * IRHost REST API: http://localhost:8080/InstantReaction/IRHost?cmd={command}...
     * 
     * Supported commands:
     *     ?cmd=login&name={username}&password={password}: authenticates and logs in the user. 
     *     ?cmd=logout&name={username}: logs out the user.
     *     ?cmd=status: returns the current context for all Hosts and Guests logged into the site.
     *
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        String answer;
        
        if (cmd == null) {
            answer = "IRHost_Error: (null) command is invalid!";
        } else if (cmd.equalsIgnoreCase("login")) {
            answer = doCmdLogin(request, response);
        } else if (cmd.equalsIgnoreCase("logout")) {
            answer = doCmdLogout(request, response);
        } else if (cmd.equalsIgnoreCase("status")) {
            answer = doCmdStatus(request, response);
        } else {
            answer = String.format("IRHost_Error: Command {%s} is not supported!", cmd);
        }
        
        response.getWriter().print(answer);
    }
    
    /**
     * IRHost ?cmd=login handler. Expects name and password as command parameters.
     * http://localhost:8080/InstantReaction/IRHost?cmd=login&name={username}&password={password}
     */
    private String doCmdLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remoteIP = request.getRemoteAddr();
        String name=request.getParameter("name");
        String password = request.getParameter("password");
        String answer;
        
        if (name == null || password == null) {
            // need both name and password to login
            answer = "IRHost_Error: missing parameter(s) for ?cmd=login command.";
        } else if (_stateLogin) {
            // cannot login twice
            answer = String.format("IRHost_Error1: %s@%s is already logged in!", name, remoteIP);
        } else {
            // TODO: handling the "login" command
            answer = String.format("IRHost_TODO: {login} command processor for %s@%s", name, remoteIP);
            // login successful, update internal state
            _stateLogin = true;
        }
        
        return answer;
    }
    
    /**
     * IRHost ?cmd=logout handler. Expects name as command parameter.
     * http://localhost:8080/InstantReaction/IRHost?cmd=logout&name={username}
     */
    private String doCmdLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remoteIP = request.getRemoteAddr();
        String name=request.getParameter("name");
        String answer;
        
        if (name == null) {
            answer = "IRHost_Error: missing parameter(s) for ?cmd=logout command.";
        } else if (!_stateLogin) {
            // cannot logout if not logged in
            answer = String.format("IRHost_Error2: %s@%s is not logged in!", name, remoteIP);
        } else {
            // TODO: handling the "logout" command
            answer = String.format("IRHost_TODO: {logout} command processor for %s@%s", name, remoteIP);
            // logout successful, update internal state
            _stateLogin = false;
        }
        
        return answer;
    }
    
    /**
     * IRHost ?cmd=status handler.
     * http://localhost:8080/InstantReaction/IRHost?cmd=status
     */
    private String doCmdStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO: handling the "status" command
        return "IRHost_TODO: {status} command processor to be implemented!";
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
