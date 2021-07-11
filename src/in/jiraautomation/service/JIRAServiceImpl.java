package in.jiraautomation.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import in.jiraautomation.connectionutil.JIRAAcccoutInfoReader;
import in.jiraautomation.entity.JIRAAccount;
import in.jiraautomation.entity.JIRAAttachment;
import in.jiraautomation.exception.JIRAConnectivityException;
import in.jiraautomation.exception.JIRAInvalidInfoException;
import in.jiraautomation.util.JiraAutomationMessages;

/**
 * Implementation for JIRAService class.
 * 
 * @author Mudit Raina
 *
 */
public class JIRAServiceImpl implements JIRAService {

	// JIRA account details loaded up which is read from account.properties file.
	public static JIRAAccount jiraAccount = JIRAAcccoutInfoReader.loadJIRAAccountInfo();

	private Logger logger = Logger.getLogger(JIRAServiceImpl.class);

	/**
	 * Gets summary for all defects in a JIRAProject.
	 * 
	 * @return
	 * @throws JSONException
	 * @throws JIRAInvalidInfoException
	 */
	@Override
	public JSONArray getAllDefectsSummary() throws JIRAInvalidInfoException, JIRAConnectivityException {

		logger.info("***** Retrieving all summaries for project from JIRA Server *****");

		HttpResponse<JsonNode> response;
		JSONArray jsonArr = new JSONArray();

		try {

			if (checkJIRAInfo(jiraAccount)) {

				response = Unirest
						.get("https://" + jiraAccount.getProjectName()
								+ ".atlassian.net/rest/api/3/search?jql=&fields=summary")
						.basicAuth(jiraAccount.getEmail(), jiraAccount.getApiToken())
						.header("Accept", "application/json").asJson();

				if (response.getStatus() != 200) {

					if (response.getBody().getObject().has("errorMessages"))
						throw new JIRAInvalidInfoException(
								response.getBody().getObject().getJSONArray("errorMessages").getString(0));

					else if (response.getBody().getObject().has("errorMessage"))
						throw new JIRAConnectivityException(JiraAutomationMessages.CONNECTIVITY_ERROR + "\n"
								+ response.getBody().getObject().getString("errorMessage"));
					else
						throw new JIRAConnectivityException(JiraAutomationMessages.CONNECTIVITY_ERROR);

				}

				jsonArr = response.getBody().getObject().getJSONArray("issues");

				logger.info("***** All summaries for project from JIRA Server retrieved *****");
			} else {
				logger.error(JiraAutomationMessages.INCORRECT_INFO);
				informInvalidAccountInfo(jiraAccount);
			}

		} catch (UnirestException e) {
			throw new JIRAConnectivityException(JiraAutomationMessages.CONNECTIVITY_ERROR);
		}

		return jsonArr;
	}

	/**
	 * Gets summary for all defects in a JIRAProject.`
	 * 
	 * @return
	 * @throws JIRAInvalidInfoException
	 */
	@Override
	public List<JIRAAttachment> getAllDefectsAttachment(String id)
			throws JIRAInvalidInfoException, JIRAConnectivityException {

		logger.info("***** Retrieving all attachment for ticket " + id + " from JIRA Server *****");

		HttpResponse<JsonNode> response;

		List<JIRAAttachment> attachmentDetails = new ArrayList<JIRAAttachment>();
		JSONArray jsonArr = new JSONArray();

		try {

			if (checkJIRAInfo(jiraAccount)) {
				response = Unirest
						.get("https://" + jiraAccount.getProjectName() + ".atlassian.net/rest/api/3/issue/" + id
								+ "?=&fields=attachment")
						.basicAuth(jiraAccount.getEmail(), jiraAccount.getApiToken())
						.header("Accept", "application/json").asJson();

				if (response.getStatus() != 200) {

					if (response.getBody().getObject().has("errorMessages"))
						throw new JIRAInvalidInfoException(
								response.getBody().getObject().getJSONArray("errorMessages").getString(0));

					else if (response.getBody().getObject().has("errorMessage"))
						throw new JIRAConnectivityException(JiraAutomationMessages.CONNECTIVITY_ERROR + "\n"
								+ response.getBody().getObject().getString("errorMessage"));
					else
						throw new JIRAConnectivityException(JiraAutomationMessages.CONNECTIVITY_ERROR);

				}

				jsonArr = response.getBody().getObject().getJSONObject("fields").getJSONArray("attachment");

				for (int i = 0; i < jsonArr.length(); i++) {

					attachmentDetails.add(new JIRAAttachment(jsonArr.getJSONObject(i).getString("filename"),
							jsonArr.getJSONObject(i).getString("mimeType")));

				}
			} else {
				informInvalidAccountInfo(jiraAccount);
			}

		} catch (UnirestException e) {
			throw new JIRAConnectivityException(JiraAutomationMessages.CONNECTIVITY_ERROR);
		}

		return attachmentDetails;
	}

	@Override
	public JSONObject searchSummary(String summary) {

		HttpResponse<JsonNode> response = null;

		String jql = "summary~\"" + summary + "\"";
		String encodedURL = encodeJQL(jql);
		JSONObject jsonObj = new JSONObject();

		try {
			response = Unirest
					.get("https://" + JIRAServiceImpl.jiraAccount.getProjectName()
							+ ".atlassian.net/rest/api/3/search?jql=" + encodedURL)
					.basicAuth(JIRAServiceImpl.jiraAccount.getEmail(), JIRAServiceImpl.jiraAccount.getApiToken())
					.header("Accept", "application/json").asJson();
			jsonObj = response.getBody().getObject();

		} catch (UnirestException e) {
			e.printStackTrace();
		}

		return jsonObj;

	}

	/**
	 * Encodes the url paramater.
	 * 
	 * @param message
	 * @return
	 */
	private String encodeJQL(String message) {
		try {
			return URLEncoder.encode(message, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			String msg = "UnsupportedOperationException: " + e.getMessage();
			throw new RuntimeException(msg);
		}
	}

	/**
	 * Checks the JIRA account fields for null or empty values.
	 * 
	 * @param jiraAcc
	 * @return
	 */
	private boolean checkJIRAInfo(JIRAAccount jiraAcc) {

		boolean isValidInfo = false;

		if (isNotNullEmpty(jiraAcc.getProjectName()) && isNotNullEmpty(jiraAcc.getEmail())
				&& isNotNullEmpty(jiraAcc.getApiToken())) {
			isValidInfo = true;
		}
		return isValidInfo;

	}

	/**
	 * utility method to check null and empty string.
	 * 
	 * @param field
	 * @return
	 */
	private static boolean isNotNullEmpty(String field) {

		boolean isValidField = false;

		if (field != null) {
			if (!field.isEmpty())
				isValidField = true;
		}

		return isValidField;

	}

	/**
	 * This methods informs in case of null or empty fields found related to JIRA
	 * authentication.
	 * 
	 * @param ja
	 * @throws JIRAInvalidInfoException
	 */
	private void informInvalidAccountInfo(JIRAAccount ja) throws JIRAInvalidInfoException {

		if (!isNotNullEmpty(ja.getProjectName())) {
			throw new JIRAInvalidInfoException(JiraAutomationMessages.INAVLID_PROJECT_NAME);
		}
		if (!isNotNullEmpty(ja.getEmail())) {
			throw new JIRAInvalidInfoException(JiraAutomationMessages.INVALID_EMAIL);
		}
		if (!isNotNullEmpty(ja.getApiToken())) {
			throw new JIRAInvalidInfoException(JiraAutomationMessages.INVALID_API_TOKEN);
		}
	}
}
