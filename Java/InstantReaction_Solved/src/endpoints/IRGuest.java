package endpoints;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import contexts.GuestContext;
import contexts.MemberContext;
import contexts.ServerContext;

/**
 * Servlet implementation class IRGuest
 */
@WebServlet("/IRGuest")
public class IRGuest extends HttpServlet {
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
    public IRGuest() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * IRGuest REST API: http://localhost:8080/InstantReaction/IRGuest?cmd={command}...
     * 
     * Supported commands:
     *     ?cmd=login&name={username}: logs in a guest into the site. 
     *     ?cmd=logout&name={username}: logs out the guest.
     *     ?cmd=status: returns the current context for this Guest.
     *
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        String answer;
        
        if (cmd == null) {
            answer = "IRGuest_Error: (null) command is invalid!";
        } else if (cmd.equalsIgnoreCase("login")) {
            answer = doCmdLogin(request, response);
        } else if (cmd.equalsIgnoreCase("logout")) {
            answer = doCmdLogout(request, response);
        } else if (cmd.equalsIgnoreCase("status")) {
            answer = doCmdStatus(request, response);
        } else {
            answer = String.format("IRGuest_Error: Command {%s} is not supported!", cmd);
        }
        
        response.getWriter().print(answer);
    }

    /**
     * IRGuest ?cmd=login handler. Expects name as command parameter.
     * http://localhost:8080/InstantReaction/IRGuest?cmd=login&name={username}
     */
    private String doCmdLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remoteIP = request.getRemoteAddr();
        String name=request.getParameter("name");
        String answer;
        
        if (name == null) {
            answer = "IRGuest_Error: missing parameter(s) for ?cmd=login command.";
        } else {
            // all is good, build a guest context before proceeding further.
            MemberContext guestContext = new GuestContext(remoteIP, name);
            
            // attempt to login the guest, answer the call with "succeeded" or "failed"
            // depending on the success as returned by the server context login call.
            if (_serverContext.loginMember(guestContext)) {
                answer = String.format("IRGuest_TODO: {login} command processor for %s succeeded!", guestContext);
            } else {
                answer = String.format("IRGuest_TODO: {login} command processor for %s failed!", guestContext);
            }
        }
        
        return answer;
    }
    
    /**
     * IRGuest ?cmd=logout handler. Expects name as command parameter.
     * http://localhost:8080/InstantReaction/IRGuest?cmd=logout&name={username}
     */
    private String doCmdLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remoteIP = request.getRemoteAddr();
        String name=request.getParameter("name");
        String answer;
        
        if (name == null) {
            answer = "IRGuest_Error: missing parameter(s) for ?cmd=logout command.";
        } else {
            // all is good, build a guest context before proceeding further.
            MemberContext guestContext = new GuestContext(remoteIP, name);
            
            // attempt to logout the guest, answer the call with "succeeded" or "failed"
            // depending on the success as returned by the server context logout call.
            if (_serverContext.logoutMember(guestContext)) {
                answer = String.format("IRGuest_TODO: {logout} command processor for %s succeeded!", guestContext);
            } else {
                answer = String.format("IRGuest_TODO: {logout} command processor for %s failed!", guestContext);
            }
        }
        
        return answer;
    }
    
    /**
     * IRGuest ?cmd=status handler.
     * http://localhost:8080/InstantReaction/IRGuest?cmd=status
     */
    private String doCmdStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO: handling the "status" command
        return "IRGuest_TODO: {status} command processor to be implemented!";
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}