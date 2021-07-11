package in.jiraautomation.exception;

import org.apache.log4j.Logger;

/**
 * Custom JIRAAutomationn exception class to throw custom exception.
 * @author Mudit Raina
 *
 */
public class JIRAConnectivityException extends RuntimeException{

	Logger logger = Logger.getLogger(JIRAConnectivityException.class);
	
	private static final long serialVersionUID = 2709516478740088928L;
	
	public JIRAConnectivityException(String message) {
		super(message);
	}

	
	

}
