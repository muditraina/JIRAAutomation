package in.jiraautomation.exception;

import org.apache.log4j.Logger;

public class JIRAInvalidInfoException extends Exception {

	private static final long serialVersionUID = -3263172654548572597L;

	Logger logger = Logger.getLogger(JIRAInvalidInfoException.class);

	public JIRAInvalidInfoException(String message) {

		super(message);
	}

}
