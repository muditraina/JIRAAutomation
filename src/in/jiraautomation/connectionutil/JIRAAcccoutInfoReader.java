package in.jiraautomation.connectionutil;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import in.jiraautomation.entity.JIRAAccount;
import in.jiraautomation.util.JiraAutomationMessages;

/**
 * This reader class reads the account information used to authenticate JIRA
 * user from a properties file. Also, provides instance for JIRAAccount.
 * 
 * @author Mudit Raina
 *
 */
public class JIRAAcccoutInfoReader {

	private static JIRAAccount jiraAcc = null;
	static Logger logger = Logger.getLogger(JIRAAcccoutInfoReader.class);

	static {

		Properties properties = new Properties();
		try {

			FileReader reader = new FileReader("resources/account.properties");
			properties.load(reader);

			logger.info(JiraAutomationMessages.READING_JIRA_INFO);
			
			String pName = properties.getProperty("projectName");
			String email = properties.getProperty("email");
			String token = properties.getProperty("apiToken");

			jiraAcc = new JIRAAccount(pName, email, token);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This returns JIRAAccount instance with populated account info read from a
	 * properties file.
	 * 
	 * @return
	 */
	public static JIRAAccount loadJIRAAccountInfo() {

		return jiraAcc;

	}
}
