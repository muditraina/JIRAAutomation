package in.jiraautomation.entity;


/**
 * This class is a model class for JIRA account info.
 * The info is read is used to authenticate user.
 * @author Mudit Raina
 *
 */
public class JIRAAccount {

	private String projectName;
	private String email;
	private String apiToken;
	
	
	
	public JIRAAccount(String projectName, String email, String apiToken) {
		this.projectName = projectName;
		this.email = email;
		this.apiToken = apiToken;
	}
	
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		
		this.projectName = projectName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	
	

}
