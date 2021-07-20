package endpoints;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import contexts.HostContext;
import contexts.MemberContext;
import contexts.QuestionContext;
import contexts.ServerContext;
import schemas.JsonQuestionContext;
import schemas.JsonServerStatus;
import schemas.JsonStatus;

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
     * IRHost REST (GET) API: http://localhost:8080/InstantReaction/IRHost?cmd={command}...
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
        JsonStatus result = new JsonStatus();
        
        if (cmd == null) {
            result.Success = false;
            result.Message = "IRHost_Error: (null) command is invalid!";
        } else if (cmd.equalsIgnoreCase("login")) {
            result = doCmdLogin(request, response);
        } else if (cmd.equalsIgnoreCase("logout")) {
            result = doCmdLogout(request, response);
        } else if (cmd.equalsIgnoreCase("status")) {
            result = doCmdStatus(request, response);
        } else {
            result.Success = false;
            result.Message = String.format("IRHost_Error: Command {%s} is not supported!", cmd);
        }
        
        String answer = (new Gson()).toJson(result);
        response.getWriter().print(answer);
    }
    
    /**
     * IRHost ?cmd=login handler. Expects name and password as command parameters.
     * http://localhost:8080/InstantReaction/IRHost?cmd=login&name={username}&password={password}
     */
    private JsonStatus doCmdLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remoteIP = request.getRemoteAddr();
        String name=request.getParameter("name");
        String password = request.getParameter("password");
        JsonStatus result = new JsonStatus();
        
        if (name == null || password == null) {
            // need both name and password to login
            result.Success = false;
            result.Message = "IRHost_Error: missing parameter(s) for ?cmd=login command.";
        } else {
            // all is good, build a host context before proceeding further.
            MemberContext hostContext = new HostContext(remoteIP, name);
            
            // attempt to login the host, answer the call with "succeeded" or "failed"
            // depending on the success as returned by the server context login call.
            if (_serverContext.loginMember(hostContext)) {
                result.Success = true;
                result.Message = String.format("IRHost_TODO: {login} command processor for %s succeeded!", hostContext);
            } else {
                // if the host is already logged in, the result is actually successful.
                MemberContext memberContext = _serverContext.getMember(hostContext);
                result.Success = (memberContext != null) && (memberContext instanceof HostContext);
                result.Message = String.format("IRHost_TODO: {login} command processor for %s failed!", hostContext);
            }
        }
        
        return result;
    }
    
    /**
     * IRHost ?cmd=logout handler. Expects name as command parameter.
     * http://localhost:8080/InstantReaction/IRHost?cmd=logout&name={username}
     */
    private JsonStatus doCmdLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remoteIP = request.getRemoteAddr();
        String name=request.getParameter("name");
        JsonStatus result = new JsonStatus();
        
        if (name == null) {
            result.Success = false;
            result.Message = "IRHost_Error: missing parameter(s) for ?cmd=logout command.";
        } else {
            // all is good, build a guest context before proceeding further.
            MemberContext hostContext = new HostContext(remoteIP, name);
            
            // attempt to logout the host, answer the call with "succeeded" or "failed"
            // depending on the success as returned by the server context logout call.
            if (_serverContext.logoutMember(hostContext)) {
                result.Success = true;
                result.Message = String.format("IRHost_TODO: {logout} command processor for %s succeeded!", hostContext);
            } else {
                // if the host is not logged in, the result is actually successful.
                result.Success = (_serverContext.getMember(hostContext) == null);
                result.Message = String.format("IRHost_TODO: {logout} command processor for %s failed!", hostContext);
            }
        }
        
        return result;
    }
    
    /**
     * IRHost ?cmd=status handler.
     * http://localhost:8080/InstantReaction/IRHost?cmd=status
     */
    private JsonStatus doCmdStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return _serverContext.toJson();
    }

    /**
     * IRHost REST (POST) API: http://localhost:8080/InstantReaction/IRHost?cmd={command}...
     * 
     * Supported commands:
     *     ?cmd=ask&name={username}: ask the question given in the request body (JsonQuestionContext instance). 
     *     ?cmd=clear&name={username}: clears the currently outstanding question.
     *
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        JsonStatus result = new JsonStatus();

        if (cmd == null) {
            result.Success = false;
            result.Message = "IRHost_Error: (null) command is invalid!";
        } else if (cmd.equalsIgnoreCase("ask")) {
            result = doCmdAsk(request, response);
        } else if (cmd.equalsIgnoreCase("clear")) {
            result = doCmdClear(request, response);
        } else {
            result.Success = false;
            result.Message = String.format("IRHost_Error: Command {%s} is not supported!", cmd);
        }
        
        String answer = (new Gson()).toJson(result);
        response.getWriter().print(answer);
    }
    
    /**
     * Handles Speaker ask:
     * http://localhost:8080/IRHost?cmd=ask
     * POST body request contains a JSON serialized Question
     * return a JsonSpeakerStatus
     */
    private JsonStatus doCmdAsk(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        JsonServerStatus result = new JsonServerStatus();
        MemberContext memberContext = _serverContext.getMember(
                new HostContext(
                        request.getRemoteAddr(),
                        request.getParameter("name")));
        
        // verify the identity of the caller
        if (memberContext == null || !(memberContext instanceof HostContext)) {
            result.Success = false;
            result.Message = "IRHost_Error: unrecognized host name for the ?cmd=ask command.";
        } else {
            JsonQuestionContext jsonQuestion = (new Gson()).fromJson(request.getReader(), JsonQuestionContext.class);
            QuestionContext questionContext = new QuestionContext(jsonQuestion);
            result.Success = _serverContext.setQuestion(questionContext);
            if (!result.Success) {
                result.Message = "IRHost_Error: failed setting question on server.";
            }
        }
        
        if (result.Success) {
            result = _serverContext.toJson();
        }
        
        return result;
    }
    
    /**
     * Handles the clear question request:
     * http://localhost:8080/IRHost?cmd=clear
     * Returns a JsonSpeakerStatus
     */
    private JsonStatus doCmdClear(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        JsonServerStatus result = new JsonServerStatus();
        MemberContext memberContext = _serverContext.getMember(
                new HostContext(
                        request.getRemoteAddr(),
                        request.getParameter("name")));
        
        // verify the identity of the caller
        if (memberContext == null || !(memberContext instanceof HostContext)) {
            result.Success = false;
            result.Message = "IRHost_Error: unrecognized host name for the ?cmd=clear command.";
        } else {
            result.Success = _serverContext.setQuestion(null);
            if (!result.Success) {
                result.Message = "IRHost_Error: failed clearing question from server.";
            }
        }
        
        if (result.Success) {
            result = _serverContext.toJson();
        }
        
        return result;
    }

}
