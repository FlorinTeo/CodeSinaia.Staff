import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import contexts.ServerContext;

/**
 * Class intercepting the main events in the server live cycle
 */
@WebListener
public class ServerListener implements ServletContextListener {

    /**
     * Code executed when the server is starting, before all servlets are activated.
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        // create a custom-made ServerContext instance and attach it to the
        // platform's servlet context, under the "context" attribute name.
        event.getServletContext().setAttribute("context", new ServerContext());
    }
    
    /**
     * Code executed when the server is shutting down, after all servlets are deactivated.
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}
