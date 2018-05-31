package controllers;


public class Transaction {
	private Personalizations[] personalizations;
	private String from;
	private String reply_to;
	private String subject;
	private Content[] content;
	public Personalizations[] getPersonalizations() {
		return personalizations;
	}
	public void setPersonalizations(Personalizations[] personalizations) {
		this.personalizations = personalizations;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getReply_to() {
		return reply_to;
	}
	public void setReply_to(String reply_to) {
		this.reply_to = reply_to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Content[] getContent() {
		return content;
	}
	public void setContent(Content[] content) {
		this.content = content;
	}
}