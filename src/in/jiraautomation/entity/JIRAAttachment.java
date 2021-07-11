package in.jiraautomation.entity;

/**
 * Model class which relates to fields associated with a JIRA ticket's
 * attachment.
 * 
 * @author Mudit Raina
 *
 */
public class JIRAAttachment {

	private String attachmentName;
	private String mimeType;

	public JIRAAttachment(String attachmentName, String mimeType) {
		this.attachmentName = attachmentName;
		this.mimeType = mimeType;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.attachmentName + " " + this.mimeType;
	}

}
