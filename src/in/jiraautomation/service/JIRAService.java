package in.jiraautomation.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import in.jiraautomation.entity.JIRAAttachment;
import in.jiraautomation.exception.JIRAConnectivityException;
import in.jiraautomation.exception.JIRAInvalidInfoException;

/**
 * Service layer which consists of methods used to get data for JIRAAutomation
 * tool.
 * 
 * @author Mudit Raina
 *
 */
public interface JIRAService {

	/**
	 * Gets summary for all defects in a JIRAProject.
	 * 
	 * @return
	 * @throws JIRAInvalidInfoException
	 */
	public JSONArray getAllDefectsSummary() throws JIRAInvalidInfoException, JIRAConnectivityException;

	/**
	 * Gets all attachments for specified JIRA ticket.
	 * 
	 * @param id
	 * @return
	 * @throws JIRAInvalidInfoException
	 */
	public List<JIRAAttachment> getAllDefectsAttachment(String id) throws JIRAInvalidInfoException, JIRAConnectivityException;

	
	/**
	 * Searches the JIRA summary in your project
	 * @param summary
	 * @return
	 */
	public JSONObject searchSummary(String summary);

}
