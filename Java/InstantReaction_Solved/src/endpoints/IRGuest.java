package endpoints;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import contexts.AnswerContext;
import contexts.GuestContext;
import contexts.MemberContext;
import contexts.ServerContext;
import schemas.JsonAnswerContext;
import schemas.JsonServerStatus;
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
        JsonServerStatus result = _serverContext.toJson();
        // For guest we do not want to return the list of members, we only want the outstanding question.
        result.Members = null;
        return result;
    }
    
    /**
     * IRGuest REST (POST) API: http://localhost:8080/InstantReaction/IRGuest?cmd={command}...
     * 
     * Supported commands:
     *     ?cmd=answer&name={username}: provides the answer for the outstanding question on server
     *                                  (JsonAnswerContext in the POST body). 
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        JsonStatus result = new JsonStatus();

        if (cmd == null) {
            result.Success = false;
            result.Message = "IRGuest_Error: (null) command is invalid!";
        } else if (cmd.equalsIgnoreCase("answer")) {
            result = doCmdAnswer(request, response);
        } else {
            result.Success = false;
            result.Message = String.format("IRGuest_Error: Command {%s} is not supported!", cmd);
        }
        
        String answer = (new Gson()).toJson(result);
        response.getWriter().print(answer);
    }

    /**
     * IRGuest ?cmd=status handler.
     * http://localhost:8080/InstantReaction/IRGuest?cmd=status
     */
    private JsonStatus doCmdAnswer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonStatus result = new JsonStatus();
        MemberContext memberContext = _serverContext.getMember(
                new GuestContext(
                        request.getRemoteAddr(),
                        request.getParameter("name")));
        AnswerContext answerContext = new AnswerContext();
        
        // verify the identity of the caller
        if (memberContext == null || !(memberContext instanceof GuestContext)) {
            result.Success = false;
            result.Message = "IRGuest_Error: unrecognized guest name for the ?cmd=answer command.";
        } else if (_serverContext.getQuestion() == null) {
            result.Success = false;
            result.Message = "IRGuest_Error: No outstanding question.";
        } else {
            // deserialize the answer from the POST body
            JsonAnswerContext jsonAnswer = (new Gson()).fromJson(request.getReader(), JsonAnswerContext.class);
            answerContext = new AnswerContext(jsonAnswer);
            
            // verify if the answer is not stale (for an old question)
            if (answerContext.getQuestionID() != _serverContext.getQuestionCount()) {
                result.Success = false;
                result.Message = "IRGuest_Error: Answering to obsolete question.";
            } else {
                // if all good set the answer in the member context and loop it back
                ((GuestContext)memberContext).setAnswer(answerContext);
                result.Success = true;
                result.Message = answerContext.getText();
            }
        }
        
        return result;
    }
}
