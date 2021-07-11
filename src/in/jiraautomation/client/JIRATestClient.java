package in.jiraautomation.client;

import in.jiraautomation.exception.JIRAConnectivityException;
import in.jiraautomation.exception.JIRAInvalidInfoException;
import in.jiraautomation.service.JIRAService;
import in.jiraautomation.service.JIRAServiceImpl;

/**
 * This is a test client, used in development process. 
 * Not used, only for testing while development.
 * @author Mudit Raina
 *
 */
public class JIRATestClient {

	
	
	
	public static void main(String[] args)  {
		
		JIRAService js = new JIRAServiceImpl();
		
		try {
			js.getAllDefectsSummary();
			js.getAllDefectsAttachment("10000");
		} catch (JIRAInvalidInfoException e) {
			System.out.println(e.getMessage());
		}catch (JIRAConnectivityException ce) {
			System.out.println(ce.getMessage());
		}
		
		
		
		
	}

}
