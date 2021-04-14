package endpoints;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import contexts.AnswerContext;
import contexts.AttendantContext;
import contexts.MemberContext;
import contexts.QuestionContext;
import contexts.ServerContext;
import schemas.JsonAnswer;
import schemas.JsonAttendantStatus;
import schemas.JsonQuestion;
import schemas.JsonSpeakerStatus;
import schemas.JsonStatus;

/**
 * Servlet implementation class Attendant
 */
@WebServlet(description = "Attendant web endpoint", urlPatterns = { "/Attendant" })
public class Attendant extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServerContext _serverContext = null;

    /**
     * On initialization retrieve and retain _serverContext 
     */
    public void init() throws ServletException {
        _serverContext = (ServerContext) getServletContext().getAttribute("context");
    }

    /**
     * Handles all GET commands for this servlet
     * http://localhost:8080/KnowledgeChecker/Attendant...
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
     * http://localhost:8080/KnowledgeChecker/Attendant...
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        JsonStatus jsonStatus;

        if (cmd == null) {
            jsonStatus = doFail(request, response);
        } else if (cmd.equalsIgnoreCase("answer")) {
            jsonStatus = doCmdAnswer(request, response);
        } else {
            jsonStatus = doFail(request, response);
        }
        
        String answer = (new Gson()).toJson(jsonStatus);
        response.getWriter().print(answer);
    }
    
    /**
     * Handles missing or unrecognized commands
     * http://localhost:8080/KnowledgeChecker/Attendant?abc=xyz&...
     */
    private JsonStatus doFail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonStatus jsonStatus = new JsonStatus(request.getRemoteAddr());
        jsonStatus.Assert(false, "Error: Command missing or unsupported!");
        return jsonStatus;
    }
    
    /**
     * Handles Attendant ping:
     * http://localhost:8080/KnowledgeChecker/Attendant?cmd=ping
     * returns a JsonStatus
     */
    private JsonStatus doCmdPing(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ipAddress = request.getRemoteAddr();
        JsonStatus jsonStatus = new JsonStatus(ipAddress);
        
        // Check if the member is logged in!
        MemberContext memberContext = _serverContext.getMember(ipAddress);
        jsonStatus.Assert(
                memberContext != null && memberContext instanceof AttendantContext,
                "Error: You are NOT logged in as Attendant!");

        
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
     * Handles Attendant login:
     * http://localhost:8080/KnowledgeChecker/Attendant?cmd=login&name=user_name
     * returns a JsonAttendantStatus
     */
    private JsonStatus doCmdLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ipAddress = request.getRemoteAddr();
        String name = request.getParameter("name");
        JsonAttendantStatus jsonStatus = new JsonAttendantStatus(ipAddress);
        
        // Check if the login name is valid
        jsonStatus.Assert(name != null && name.length() > 0, "Error: Need a name to login!");
        
        // On success, check if the member is not already logged in!
        AttendantContext attendantContext = null;
        if (jsonStatus.Success) {
            attendantContext = new AttendantContext(name, ipAddress);
            MemberContext memberContext = _serverContext.getMember(ipAddress);
            jsonStatus.Assert(memberContext == null, "Error: You are already logged in!");
            jsonStatus.Name = (memberContext == null) ? attendantContext.getName() : memberContext.getName();
            jsonStatus.Role = (memberContext == null) ? attendantContext.getRole() : memberContext.getRole();
        }
        
        // On success, login and add current question to jsonStatus
        if (jsonStatus.Success) {
            _serverContext.loginMember(attendantContext);
            jsonStatus.Message = "Success: You are now logged in!";
            QuestionContext crtQuestion = _serverContext.getQuestion(); 
            jsonStatus.Question = (crtQuestion != null) ? crtQuestion.toJsonSchema() : null; 
        }
        
        return jsonStatus;
    }

    /**
     * Handles Attendant logout:
     * http://localhost:8080/KnowledgeChecker/Attendant?cmd=logout
     * returns JsonStatus
     */
    private JsonStatus doCmdLogout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String ipAddress = request.getRemoteAddr();
        JsonStatus jsonStatus = new JsonStatus(ipAddress);
        
        // Check if the member is logged in!
        MemberContext memberContext = _serverContext.getMember(ipAddress);
        jsonStatus.Assert(
                memberContext != null && memberContext instanceof AttendantContext,
                "Error: You are NOT logged in as Attendant!");
        
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
     * Handles Attendant status:
     * http://localhost:8080/KnowledgeChecker/Attendant?cmd=status
     * returns JsonAttendantStatus
     */
    private JsonStatus doCmdStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ipAddress = request.getRemoteAddr();
        JsonAttendantStatus jsonStatus = new JsonAttendantStatus(ipAddress);
        
        // Check if the member is logged in!
        MemberContext memberContext = _serverContext.getMember(ipAddress);
        jsonStatus.Assert(
                memberContext != null && memberContext instanceof AttendantContext,
                "Error: You are NOT logged in as Attendant!");
        
        // On success, dump the memberContext as status message
        if (jsonStatus.Success) {
            // "touch" the member context to keep active
            memberContext.touch();
            jsonStatus.Name = memberContext.getName();
            jsonStatus.Role = memberContext.getRole();
            jsonStatus.Message = memberContext.toString();
            QuestionContext crtQuestion = _serverContext.getQuestion();
            jsonStatus.Question = (crtQuestion != null) ? crtQuestion.toJsonSchema() : null;
        }
        
        return jsonStatus;
    }
    
    /**
     * Handles Attendant answer:
     * http://localhost:8080/KnowledgeChecker/Attendant?cmd=answer
     * POST body request contains a JSON serialized Answer
     * return a JsonSpeakerStatus
     */
    private JsonStatus doCmdAnswer(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String ipAddress = request.getRemoteAddr();
        JsonAttendantStatus jsonStatus = new JsonAttendantStatus(ipAddress);
        
        // Check if the member is logged in!
        MemberContext memberContext = _serverContext.getMember(ipAddress);
        jsonStatus.Assert(
                memberContext != null && memberContext instanceof AttendantContext,
                "Error: You are NOT logged in as Attendant!");
        
        // On success, check if there's a standing question
        QuestionContext crtQuestion = _serverContext.getQuestion();
        if (jsonStatus.Success) {
            jsonStatus.Assert(crtQuestion != null, "Error: Question being answer expired!");
        }
        
        // On success, fetch the answer and check if it matches the question
        JsonAnswer jsonAnswer = null;
        if (jsonStatus.Success) {
            jsonStatus.Name = memberContext.getName();
            jsonStatus.Role = memberContext.getRole();
            jsonAnswer = (new Gson()).fromJson(request.getReader(), JsonAnswer.class);
            jsonStatus.Assert(crtQuestion.getID() == jsonAnswer.QuestionID, "Error: Question being answered has changed!");
        }
        
        // On success, build the AnswerContext and record it to the member
        AnswerContext answerContext = null;
        if (jsonStatus.Success) {
            answerContext = new AnswerContext(crtQuestion, jsonAnswer);
            ((AttendantContext)memberContext).setAnswer(answerContext);
        }
        
        // in the end add current standing question (if any) to the status
        jsonStatus.Question = (crtQuestion != null) ? crtQuestion.toJsonSchema() : null;
        
        return jsonStatus;
    }
}
