package in.jiraautomation.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
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

/**
 * Implementation for JIRAService class.
 * 
 * @author Mudit Raina
 *
 */
public class JIRAServiceImpl implements JIRAService {

	public static JIRAAccount jiraAccount = JIRAAcccoutInfoReader.loadJIRAAccountInfo();

	Logger logger = Logger.getLogger(JIRAServiceImpl.class);

	/**
	 * Gets summary for all defects in a JIRAProject.
	 * 
	 * @return
	 * @throws JIRAInvalidInfoException
	 */
	@Override
	public JSONArray getAllDefectsSummary() throws JIRAInvalidInfoException {

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
					throw new JIRAConnectivityException(
							"Issue connecting to your JIRA account, check account info used !");
				}

				jsonArr = response.getBody().getObject().getJSONArray("issues");

				logger.info("***** All summaries for project from JIRA Server retrieved *****");
			} else {
				informInvalidAccountInfo(jiraAccount);
			}

		} catch (UnirestException e) {
			// throw new JIRAConnectivityException("Issue connecting to your JIRA account,
			// check account info used !");
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
	public List<JIRAAttachment> getAllDefectsAttachment(String id) throws JIRAInvalidInfoException {

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

					// String s =
					// response.getBody().getObject().getJSONArray("errorMessages").get(0).toString();

					throw new JIRAConnectivityException(
							"Issue connecting to your JIRA account, check account info used !");

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
			// throw new JIRAConnectivityException("Issue connecting to your JIRA account,
			// check account info used !");
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

	private String encodeJQL(String message) {
		try {
			return URLEncoder.encode(message, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			String msg = "UnsupportedOperationException: " + e.getMessage();
			throw new RuntimeException(msg);
		}
	}

	private boolean checkJIRAInfo(JIRAAccount jiraAcc) {

		boolean isValidInfo = false;

		if (isNotNullEmpty(jiraAcc.getProjectName()) && isNotNullEmpty(jiraAcc.getEmail())
				&& isNotNullEmpty(jiraAcc.getApiToken())) {
			isValidInfo = true;
		}
		return isValidInfo;

	}

	private static boolean isNotNullEmpty(String field) {

		boolean isValidField = false;

		if (field != null) {
			if (!field.isEmpty())
				isValidField = true;
		}

		return isValidField;

	}

	private void informInvalidAccountInfo(JIRAAccount ja) throws JIRAInvalidInfoException {

		if (!isNotNullEmpty(ja.getProjectName())) {
			throw new JIRAInvalidInfoException("The project name not mentioned. Please specify project name!");
		}
		if (!isNotNullEmpty(ja.getEmail())) {
			throw new JIRAInvalidInfoException("The account email not mentioned. Please specify email!");
		}
		if (!isNotNullEmpty(ja.getApiToken())) {
			throw new JIRAInvalidInfoException("The api token not mentioned. Please specify api token!");
		}
	}

}
