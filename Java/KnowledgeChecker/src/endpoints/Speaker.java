package endpoints;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import contexts.AttendantContext;
import contexts.MemberContext;
import contexts.QuestionContext;
import contexts.ServerContext;
import contexts.SpeakerContext;
import schemas.JsonQuestion;
import schemas.JsonSpeakerStatus;
import schemas.JsonStatus;

/**
 * Servlet implementation class Speaker
 */
@WebServlet(description = "Speaker web endpoint", urlPatterns = { "/Speaker" })
public class Speaker extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String SUPER_SECRET_PASSWORD = "_speaker_123"; 
    private ServerContext _serverContext = null;

    /**
     * On initialization retrieve and retain _serverContext 
     */
    public void init() throws ServletException {
        _serverContext = (ServerContext) getServletContext().getAttribute("context");
    }

    /**
     * Handles all GET commands for this servlet
     * http://localhost:8080/KnowledgeChecker/Speaker...
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        JsonStatus jsonStatus;

        if (cmd == null) {
            jsonStatus = doFail(request, response);
        } else if (cmd.equalsIgnoreCase("ping")) {
            jsonStatus = doCmdPing(request, response);
        } else if (cmd.equalsIgnoreCase("login")) {
            jsonStatus = doCmdLogin(request, response);
        } else  if (cmd.equalsIgnoreCase("logout")) {
            jsonStatus = doCmdLogout(request, response);
        } else if (cmd.equalsIgnoreCase("status")) {
            jsonStatus = doCmdStatus(request, response);
        } else {
            jsonStatus = doFail(request, response);
        }
        
        String answer = (new Gson()).toJson(jsonStatus);
        response.getWriter().print(answer);
    }
    
    /**
     * Handles all POST commands for this servlet
     * http://localhost:8080/KnowledgeChecker/Speaker...
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        JsonStatus jsonStatus;

        if (cmd == null) {
            jsonStatus = doFail(request, response);
        } else if (cmd.equalsIgnoreCase("ask")) {
            jsonStatus = doCmdAsk(request, response);
        } else if (cmd.equalsIgnoreCase("clear")) {
            jsonStatus = doCmdClear(request, response);
        } else {
            jsonStatus = doFail(request, response);
        }
        
        String answer = (new Gson()).toJson(jsonStatus);
        response.getWriter().print(answer);
    }
    
    /**
     * Handles missing or unrecognized commands
     * http://localhost:8080/KnowledgeChecker/Speaker?abc=xyz&...
     * returns a JsonStatus
     */
    private JsonStatus doFail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonStatus jsonStatus = new JsonStatus(request.getRemoteAddr());
        jsonStatus.Assert(false, "Error: Command missing or unsupported!");
        return jsonStatus;
    }
    
    /**
     * Handles Speaker ping:
     * http://localhost:8080/KnowledgeChecker/Speaker?cmd=ping
     * returns a JsonStatus
     */
    private JsonStatus doCmdPing(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ipAddress = request.getRemoteAddr();
        JsonStatus jsonStatus = new JsonStatus(ipAddress);
        
        // Check if the member is logged in!
        MemberContext memberContext = _serverContext.getMember(ipAddress);
        jsonStatus.Assert(
                memberContext != null && memberContext instanceof SpeakerContext,
                "Error: You are NOT logged in as Speaker!");
        
        // On success, dump the memberContext as status message
        if (jsonStatus.Success) {
            // "touch" the member context to keep active
            memberContext.touch();
            jsonStatus.Name = memberContext.getName();
            jsonStatus.Role = memberContext.getRole();
            jsonStatus.Message = memberContext.toString();
        }
        
        return jsonStatus;
    }

    /**
     * Handles Speaker login:
     * http://localhost:8080/KnowledgeChecker/Speaker?cmd=login&name=user_name
     * returns a JsonSpeakerStatus
     */
    private JsonStatus doCmdLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ipAddress = request.getRemoteAddr();
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        JsonSpeakerStatus jsonStatus = new JsonSpeakerStatus(ipAddress);
        
        // Check if the login name is valid
        jsonStatus.Assert(name != null && name.length() > 0, "Error: Need a name to login!");
        
        // On success, check if password is valid
        if (jsonStatus.Success) {
            jsonStatus.Assert(SUPER_SECRET_PASSWORD.equals(password), "Error: Invalid password!");
        }
        
        // On success, check if the member is not already logged in!
        SpeakerContext speakerContext = null;
        if (jsonStatus.Success) {
            speakerContext = new SpeakerContext(name, ipAddress);
            MemberContext memberContext = _serverContext.getMember(ipAddress);
            jsonStatus.Assert(memberContext == null, "Error: You are already logged in!");
            jsonStatus.Name = memberContext == null ? speakerContext.getName() : memberContext.getName();
            jsonStatus.Role = memberContext == null ? speakerContext.getRole() : memberContext.getRole();
        }
        
        // On success, add Members to the jsonStatus
        if (jsonStatus.Success) {
            _serverContext.loginMember(speakerContext);
            jsonStatus.Message = "Success: You are now logged in!";
            jsonStatus.Members = _serverContext.toJsonSchema(ipAddress).Members;
        }
        
        return jsonStatus;
    }

    /**
     * Handles Speaker logout:
     * http://localhost:8080/Speaker?cmd=logout
     * returns a JsonStatus
     */
    private JsonStatus doCmdLogout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String ipAddress = request.getRemoteAddr();
        JsonStatus jsonStatus = new JsonStatus(ipAddress);
        
        // Check if the member is logged in!
        MemberContext memberContext = _serverContext.getMember(ipAddress);
        jsonStatus.Assert(
                memberContext != null && memberContext instanceof SpeakerContext,
                "Error: You are NOT logged in as Speaker!");
        
        // On success, logout
        if (jsonStatus.Success) {
            _serverContext.logoutMember(ipAddress);
            jsonStatus.Name = memberContext.getName();
            jsonStatus.Role = memberContext.getRole();
            jsonStatus.Message = "You are now logged out!";
        }

        return jsonStatus;
    }
    
    /**
     * Handles Speaker clear:
     * http://localhost:8080/Speaker?cmd=status
     * returns a JsonSpeakerStatus
     */
    private JsonStatus doCmdStatus(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String ipAddress = request.getRemoteAddr();
        JsonSpeakerStatus jsonStatus = new JsonSpeakerStatus(ipAddress);
        
        // Check if the member is logged in!
        MemberContext memberContext = _serverContext.getMember(ipAddress);
        jsonStatus.Assert(
                memberContext != null && memberContext instanceof SpeakerContext,
                "Error: You are NOT logged in as Speaker!");
        
        // On success, fetch and dispatch the question
        if (jsonStatus.Success) {
            // "touch" the member context to keep active
            memberContext.touch();
            jsonStatus.Name = memberContext.getName();
            jsonStatus.Role = memberContext.getRole();
            jsonStatus.Members = _serverContext.toJsonSchema(ipAddress).Members;
        }
        
        return jsonStatus;
    }
    
    /**
     * Handles Speaker ask:
     * http://localhost:8080/Speaker?cmd=ask
     * POST body request contains a JSON serialized Question
     * return a JsonSpeakerStatus
     */
    private JsonStatus doCmdAsk(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String ipAddress = request.getRemoteAddr();
        JsonSpeakerStatus jsonStatus = new JsonSpeakerStatus(ipAddress);
        
        // Check if the member is logged in!
        MemberContext memberContext = _serverContext.getMember(ipAddress);
        jsonStatus.Assert(
                memberContext != null && memberContext instanceof SpeakerContext,
                "Error: You are NOT logged in as Speaker!");
        
        // On success, fetch and dispatch the question
        if (jsonStatus.Success) {
            jsonStatus.Name = memberContext.getName();
            jsonStatus.Role = memberContext.getRole();
            JsonQuestion jsonQuestion = (new Gson()).fromJson(request.getReader(), JsonQuestion.class);
            jsonQuestion.QuestionID = _serverContext.nextQuestionID();
            QuestionContext questionContext = new QuestionContext(jsonQuestion);
            jsonStatus.Assert(_serverContext.setQuestion(questionContext), "Uncleared question on server!");
        }
        
        // On success, add current members status to response
        if (jsonStatus.Success) {
            jsonStatus.Members = _serverContext.toJsonSchema(ipAddress).Members;
        }
        
        return jsonStatus;
    }
    
    /**
     * Handles Speaker clear:
     * http://localhost:8080/Speaker?cmd=clear
     * Returns a JsonSpeakerStatus
     */
    private JsonStatus doCmdClear(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String ipAddress = request.getRemoteAddr();
        JsonSpeakerStatus jsonStatus = new JsonSpeakerStatus(ipAddress);
        
        // Check if the member is logged in!
        MemberContext memberContext = _serverContext.getMember(ipAddress);
        jsonStatus.Assert(
                memberContext != null && memberContext instanceof SpeakerContext,
                "Error: You are NOT logged in as Speaker!");
        
        // On success, fetch and dispatch the question
        if (jsonStatus.Success) {
            jsonStatus.Name = memberContext.getName();
            jsonStatus.Role = memberContext.getRole();
            jsonStatus.Assert(_serverContext.setQuestion(null), "No question to clear on server!");
        }
        
        // On success, add current members status to response
        if (jsonStatus.Success) {
            jsonStatus.Members = _serverContext.toJsonSchema(ipAddress).Members;
        }
        
        return jsonStatus;
    }
}
