package endpoints;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import contexts.GuestContext;
import contexts.MemberContext;
import contexts.ServerContext;
import schemas.JsonStatus;

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
        JsonStatus result = new JsonStatus();
        
        if (cmd == null) {
            result.Success = false;
            result.Message = "IRGuest_Error: (null) command is invalid!";
        } else if (cmd.equalsIgnoreCase("login")) {
            result = doCmdLogin(request, response);
        } else if (cmd.equalsIgnoreCase("logout")) {
            result = doCmdLogout(request, response);
        } else if (cmd.equalsIgnoreCase("status")) {
            result = doCmdStatus(request, response);
        } else {
            result.Success = false;
            result.Message = String.format("IRGuest_Error: Command {%s} is not supported!", cmd);
        }
        
        String answer = (new Gson()).toJson(result);
        response.getWriter().print(answer);
    }

    /**
     * IRGuest ?cmd=login handler. Expects name as command parameter.
     * http://localhost:8080/InstantReaction/IRGuest?cmd=login&name={username}
     */
    private JsonStatus doCmdLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remoteIP = request.getRemoteAddr();
        String name=request.getParameter("name");
        JsonStatus result = new JsonStatus();
        
        if (name == null) {
            result.Success = false;
            result.Message = "IRGuest_Error: missing parameter(s) for ?cmd=login command.";
        } else {
            // all is good, build a guest context before proceeding further.
            MemberContext guestContext = new GuestContext(remoteIP, name);
            
            // attempt to login the guest, answer the call with "succeeded" or "failed"
            // depending on the success as returned by the server context login call.
            if (_serverContext.loginMember(guestContext)) {
                result.Success = true;
                result.Message = String.format("IRGuest_TODO: {login} command processor for %s succeeded!", guestContext);
            } else {
                // if the guest is already logged in, the result is actually successful.
                MemberContext memberContext = _serverContext.getMember(guestContext);
                result.Success = (memberContext != null) && (memberContext instanceof GuestContext);
                result.Message = String.format("IRGuest_TODO: {login} command processor for %s failed!", guestContext);
            }
        }
        
        return result;
    }
    
    /**
     * IRGuest ?cmd=logout handler. Expects name as command parameter.
     * http://localhost:8080/InstantReaction/IRGuest?cmd=logout&name={username}
     */
    private JsonStatus doCmdLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remoteIP = request.getRemoteAddr();
        String name=request.getParameter("name");
        JsonStatus result = new JsonStatus();
        
        if (name == null) {
            result.Success = false;
            result.Message = "IRGuest_Error: missing parameter(s) for ?cmd=logout command.";
        } else {
            // all is good, build a guest context before proceeding further.
            MemberContext guestContext = new GuestContext(remoteIP, name);
            
            // attempt to logout the guest, answer the call with "succeeded" or "failed"
            // depending on the success as returned by the server context logout call.
            if (_serverContext.logoutMember(guestContext)) {
                result.Success = true;
                result.Message = String.format("IRGuest_TODO: {logout} command processor for %s succeeded!", guestContext);
            } else {
                // if the guest is not logged in, the result is actually successful.
                result.Success = (_serverContext.getMember(guestContext) == null);
                result.Message = String.format("IRGuest_TODO: {logout} command processor for %s failed!", guestContext);
            }
        }
        
        return result;
    }
    
    /**
     * IRGuest ?cmd=status handler.
     * http://localhost:8080/InstantReaction/IRGuest?cmd=status
     */
    private JsonStatus doCmdStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO: handling the "status" command
        JsonStatus result = new JsonStatus();
        result.Success = true;
        result.Message = "IRGuest_TODO: {status} command processor to be implemented!";
        return result;
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
