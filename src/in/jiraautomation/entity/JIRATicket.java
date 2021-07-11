package in.jiraautomation.entity;


/**
 * Model class which relates to fields associated with a JIRA ticket
 * @author Mudit Raina
 *
 */
public class JIRATicket {

	private String ticketID;
	private String ticketKey;
	private String ticketSummary;
	
	private JIRAAttachment jiraAttachment;

	public JIRATicket(String ticketID, String ticketKey, String ticketSummary) {
		this.ticketID = ticketID;
		this.ticketKey = ticketKey;
		this.ticketSummary = ticketSummary;
	}

	public String getTicketID() {
		return ticketID;
	}

	public void setTicketID(String ticketID) {
		this.ticketID = ticketID;
	}

	public String getTicketKey() {
		return ticketKey;
	}

	public void setTicketKey(String ticketKey) {
		this.ticketKey = ticketKey;
	}

	public String getTicketSummary() {
		return ticketSummary;
	}

	public void setTicketSummary(String ticketSummary) {
		this.ticketSummary = ticketSummary;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ticketID +" " + ticketKey+" "+ticketSummary;
	}

}
