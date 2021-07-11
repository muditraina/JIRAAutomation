package in.jiraautomation.exception;

/**
 * Exception class to throw Invalid info exception incase of wrong JIRA account info is provided.
 * @author Mudit Raina
 *
 */
public class JIRAInvalidInfoException extends RuntimeException {

	private static final long serialVersionUID = -3263172654548572597L;

	public JIRAInvalidInfoException(String message) {

		super(message);
	}

}
