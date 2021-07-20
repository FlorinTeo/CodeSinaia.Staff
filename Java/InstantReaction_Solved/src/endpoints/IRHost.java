package endpoints;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import contexts.HostContext;
import contexts.MemberContext;
import contexts.ServerContext;

/**
 * Servlet implementation class IRHost
 */
@WebServlet("/IRHost")
public class IRHost extends HttpServlet {
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
        } else {
            // all is good, build a guest context before proceeding further.
            MemberContext hostContext = new HostContext(remoteIP, name);
            
            // attempt to login the host, answer the call with "succeeded" or "failed"
            // depending on the success as returned by the server context login call.
            if (_serverContext.loginMember(hostContext)) {
                answer = String.format("IRHost_TODO: {login} command processor for %s succeeded!", hostContext);
            } else {
                answer = String.format("IRHost_TODO: {login} command processor for %s failed!", hostContext);
            }
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
        } else {
            // all is good, build a guest context before proceeding further.
            MemberContext hostContext = new HostContext(remoteIP, name);
            
            // attempt to logout the host, answer the call with "succeeded" or "failed"
            // depending on the success as returned by the server context logout call.
            if (_serverContext.logoutMember(hostContext)) {
                answer = String.format("IRHost_TODO: {logout} command processor for %s succeeded!", hostContext);
            } else {
                answer = String.format("IRHost_TODO: {logout} command processor for %s failed!", hostContext);
            }
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
