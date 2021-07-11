package in.jiraautomation.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import in.jiraautomation.entity.JIRAAttachment;
import in.jiraautomation.exception.JIRAConnectivityException;
import in.jiraautomation.exception.JIRAInvalidInfoException;
import in.jiraautomation.service.JIRAService;
import in.jiraautomation.service.JIRAServiceImpl;

/**
 * Test class for JIRAAutmation tool, uses testNG framework to execute test
 * cases.
 * 
 * @author Mudit Raina
 *
 */
public class JIRAAutomationTest {

	JIRAService jiraService = new JIRAServiceImpl();

	Logger logger = Logger.getLogger(JIRAAutomationTest.class);

	/**
	 * Test case to check the presence of duplicate summary in a JIRA project. The
	 * test case fails if duplicate summary is found.
	 */
	@Test
	// (expectedExceptions = { JIRAInvalidInfoException.class,
	// JIRAConnectivityException.class })
	public void testDuplicateSummary() {

		logger.info("***** Running testDuplicateSummary() test case *****");

		JSONArray summaryIssue;
		try {
			summaryIssue = jiraService.getAllDefectsSummary();

			Set<String> summaryUnique = new HashSet<String>();
			List<String> summaryAll = new LinkedList<String>();

			for (int i = 0; i < summaryIssue.length(); i++) {

				JSONObject obj = summaryIssue.getJSONObject(i);

				summaryUnique.add(obj.getJSONObject("fields").getString("summary"));
				summaryAll.add(obj.getJSONObject("fields").getString("summary"));

			}

			for (String string : summaryUnique) {
				int count = Collections.frequency(summaryAll, string);
				if (count > 1)
					logger.info(count + " duplicates found for summary " + "\"" + string + "\"" + " in your project !");
			}

			Assert.assertEquals(summaryAll.size(), summaryUnique.size());

			logger.info("***** Running testDuplicateSummary() test case completed *****");
		} catch (JIRAInvalidInfoException e) {
			Assert.fail(e.getMessage(), e);
			logger.error(e.getMessage());
		} catch (JIRAConnectivityException ce) {
			Assert.fail(ce.getMessage(), ce);
			logger.error(ce.getMessage());
		}

	}

	/**
	 * Test case to check the presence of duplicate attachment in a JIRA project.
	 * The test case fails if attachment with duplicate name is found. The
	 * attachments are first compared on the basis of MIME type and if same then
	 * check is performed to check the first 10 chars of the attachment name. If
	 * found same the test fails.
	 * 
	 * @param ticketID
	 */
	@Test
	// (expectedExceptions = { JIRAInvalidInfoException.class,
	// JIRAConnectivityException.class })
	@Parameters("ticketID")
	public void testDupilcateAttachmentName(String ticketID) {

		logger.info("***** Running testDupilcateAttachmentName() test case *****");

		List<JIRAAttachment> attachmentDetails;
		try {
			attachmentDetails = jiraService.getAllDefectsAttachment(ticketID);

			Set<String> uniqueAttachments = new HashSet<String>();

			int count = 0;

			for (int i = 0; i < attachmentDetails.size(); i++) {
				for (int j = i + 1; j < attachmentDetails.size(); j++) {

					String mimeType = attachmentDetails.get(i).getMimeType();
					String nextMimeType = attachmentDetails.get(j).getMimeType();

					String attachement = attachmentDetails.get(i).getAttachmentName().substring(0,
							attachmentDetails.get(i).getAttachmentName().lastIndexOf("."));
					String nextAttachement = attachmentDetails.get(j).getAttachmentName().substring(0,
							attachmentDetails.get(j).getAttachmentName().lastIndexOf("."));

					if (mimeType.equalsIgnoreCase(nextMimeType)) {

						if (attachement.length() > 9 && nextAttachement.length() > 9) {

							if (attachement.substring(0, 10).equalsIgnoreCase((nextAttachement).substring(0, 10))) {

								count++;
								uniqueAttachments.add(attachement.substring(0, 10) + " " + mimeType);
								break;

							}

						}

					}

				}
			}

			if (uniqueAttachments.size() > 0) {

				for (String attachmemt : uniqueAttachments) {
					logger.info("Duplicate attachment containing chars " + attachmemt + " type");
				}
			}

			Assert.assertEquals(count, 0);

			logger.info("***** Running testDupilcateAttachmentName() test case completed *****");

		} catch (JIRAInvalidInfoException e) {
			Assert.fail(e.getMessage(), e);
			logger.error(e.getMessage());
		} catch (JIRAConnectivityException ce) {
			Assert.fail(ce.getMessage(), ce);
			logger.error(ce.getMessage());
		}
	}
}
