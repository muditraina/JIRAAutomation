package in.jiraautomation.exception;

/**
 * Exception class to throw JIRAConnectivityException in case of there is some connectivity issue with JIRA
 * @author Mudit Raina
 *
 */
public class JIRAConnectivityException extends RuntimeException{

	
	private static final long serialVersionUID = 2709516478740088928L;
	
	public JIRAConnectivityException(String message) {
		super(message);
	}

	
	

}
