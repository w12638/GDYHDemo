package mySession;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


public class SessionListener implements HttpSessionListener {
	  public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		  HttpSession session = httpSessionEvent.getSession();
	        MySessionContext.AddSession(session);
	        
	    }
	    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
	        HttpSession session = httpSessionEvent.getSession();
	        MySessionContext.DelSession(session);
	    }

}
