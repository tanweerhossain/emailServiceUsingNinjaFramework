package controllers;



import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.activation.MailcapCommandMap;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.sendgrid.*;

public class JsonParserServlet {
		private String SENDGRID_API_KEY = "NULL";
		
		private void validateMailFormat(String email) throws Exception{
			int posAtTheRate = email.indexOf("@");
			int posDot = email.lastIndexOf(".");
			if( !( ( posAtTheRate>0 && posAtTheRate<email.length()-3  )&&( posDot>(posAtTheRate+1) && posDot<email.length()-1 ) ) )
				throw new Exception("Email format is not valid.");
		}
		private void senderOnlyToPresent(Personalizations personalizations,Personalization personalization) throws Exception{
			for( int j = 0 ; j < personalizations.getTo().length ; ++j ){
				validateMailFormat(personalizations.getTo()[j]);
				personalization.addTo( new Email(personalizations.getTo()[j]));
			}
		}
		private void senderOnlyCcPresent(Personalizations personalizations,Personalization personalization) throws Exception{
			for( int j = 0 ; j < personalizations.getCc().length ; ++j ){
				validateMailFormat(personalizations.getCc()[j]);
				personalization.addCc( new Email(personalizations.getCc()[j]));
			}
		}
		private void senderOnlyBccPresent(Personalizations personalizations,Personalization personalization) throws Exception{
			for( int j = 0 ; j < personalizations.getBcc().length ; ++j ){
				validateMailFormat(personalizations.getBcc()[j]);
				personalization.addBcc( new Email(personalizations.getBcc()[j]));
			}
		}
		private void senderOnlyToAbsent(Personalizations personalizations,Personalization personalization) throws Exception{
			for( int j = 0 ; j < personalizations.getBcc().length ; ++j ){
				validateMailFormat(personalizations.getBcc()[j]);
				personalization.addBcc( new Email(personalizations.getBcc()[j]));
			}
			for( int j = 0 ; j < personalizations.getCc().length ; ++j ){
				validateMailFormat(personalizations.getCc()[j]);
				personalization.addCc( new Email(personalizations.getCc()[j]));
			}
		}
		private void senderOnlyCcAbsent(Personalizations personalizations,Personalization personalization) throws Exception{
			for( int j = 0 ; j < personalizations.getTo().length ; ++j ){
				validateMailFormat(personalizations.getTo()[j]);
				personalization.addTo( new Email(personalizations.getTo()[j]));
			}
			for( int j = 0 ; j < personalizations.getBcc().length ; ++j ){
				validateMailFormat(personalizations.getBcc()[j]);
				personalization.addBcc( new Email(personalizations.getBcc()[j]));
			}
		}
		private void senderOnlyBccAbsent(Personalizations personalizations,Personalization personalization) throws Exception{
			for( int j = 0 ; j < personalizations.getTo().length ; ++j ){
				validateMailFormat(personalizations.getTo()[j]);
				personalization.addTo( new Email(personalizations.getTo()[j]));
			}
			for( int j = 0 ; j < personalizations.getCc().length ; ++j ){
				validateMailFormat(personalizations.getCc()[j]);
				personalization.addCc( new Email(personalizations.getCc()[j]));
			}
		}
		private Status senderAllPresent(Transaction transaction)throws Exception{
			
			//Mail sending format calls
			Mail mail = new Mail();
			mail.from = new Email(transaction.getFrom());
			mail.replyTo = new Email(transaction.getReply_to());
			mail.subject = transaction.getSubject();
			for( int i = 0 ; i < transaction.getContent().length ; ++i ){
				if( transaction.getContent()[i].type == null || transaction.getContent()[i].value == null || transaction.getContent()[i].type.length() == 0 || transaction.getContent()[i].value.length() == 0 )
					throw new Exception("Content is missing or content format is wrong");
				mail.addContent( new com.sendgrid.Content( transaction.getContent()[i].type, transaction.getContent()[i].value ));
			}
			for( int i = 0 ; i < transaction.getPersonalizations().length ; ++i ){
				Personalization personalization = new Personalization();

				//All sender addresses are present
				if( transaction.getPersonalizations()[i].getTo() != null  && 
						transaction.getPersonalizations()[i].getBcc() != null &&
						transaction.getPersonalizations()[i].getCc() != null ) {
						for( int j = 0 ; j < transaction.getPersonalizations()[i].getTo().length ; ++j ){
							validateMailFormat(transaction.getPersonalizations()[i].getTo()[j]);
							personalization.addTo( new Email(transaction.getPersonalizations()[i].getTo()[j]));
						}
						for( int j = 0 ; j < transaction.getPersonalizations()[i].getBcc().length ; ++j ){
							validateMailFormat(transaction.getPersonalizations()[i].getBcc()[j]);
							personalization.addBcc( new Email(transaction.getPersonalizations()[i].getBcc()[j]));
						}
						for( int j = 0 ; j < transaction.getPersonalizations()[i].getCc().length ; ++j ){
							validateMailFormat(transaction.getPersonalizations()[i].getCc()[j]);
							personalization.addCc( new Email(transaction.getPersonalizations()[i].getCc()[j]));
						}
				}	//Only bcc is present
				else if( transaction.getPersonalizations()[i].getTo() == null  && 
								transaction.getPersonalizations()[i].getBcc() != null &&
								transaction.getPersonalizations()[i].getCc() == null ){
						senderOnlyBccPresent(transaction.getPersonalizations()[i], personalization);
				}//Only cc is present
				else if( transaction.getPersonalizations()[i].getTo() == null  && 
								transaction.getPersonalizations()[i].getBcc() == null &&
								transaction.getPersonalizations()[i].getCc() != null ){
						senderOnlyCcPresent(transaction.getPersonalizations()[i], personalization);
				}//Only to is present
				else if( transaction.getPersonalizations()[i].getTo() != null  && 
								transaction.getPersonalizations()[i].getBcc() == null &&
								transaction.getPersonalizations()[i].getCc() == null ){
						senderOnlyToPresent(transaction.getPersonalizations()[i], personalization);
				}//Only to is absent
				else if( transaction.getPersonalizations()[i].getTo() == null  && 
								transaction.getPersonalizations()[i].getBcc() != null &&
								transaction.getPersonalizations()[i].getCc() != null ){
						senderOnlyToAbsent(transaction.getPersonalizations()[i], personalization);
				}//Only cc is absent
				else if( transaction.getPersonalizations()[i].getTo() != null  && 
								transaction.getPersonalizations()[i].getBcc() != null &&
								transaction.getPersonalizations()[i].getCc() == null ){
						senderOnlyCcAbsent(transaction.getPersonalizations()[i], personalization);
				}//Only bcc is absent
				else if( transaction.getPersonalizations()[i].getTo() != null  && 
								transaction.getPersonalizations()[i].getBcc() == null &&
								transaction.getPersonalizations()[i].getCc() != null ){
						senderOnlyBccAbsent(transaction.getPersonalizations()[i], personalization);
				}
				else{
					throw new Exception("Receiver email is missing");
				}
				if(transaction.getPersonalizations()[i].getSubject() == null && transaction.getSubject() == null )
					throw new Exception("Email subject is missing.");
				personalization.setSubject(transaction.getPersonalizations()[i].getSubject());				
				mail.addPersonalization(personalization);
			}


			System.out.println("???????????????????????? " + SENDGRID_API_KEY);
			SendGrid sg = new SendGrid(SENDGRID_API_KEY.trim());
			Request request = new Request();
			Response response = new Response();
			try {
				request.setMethod(Method.POST);
				request.setEndpoint("mail/send");
				request.setBody(mail.build());
				response = sg.api(request);
				System.out.println(response.getStatusCode());
				System.out.println(response.getBody());
				System.out.println(response.getHeaders());
			} catch (IOException ex) {
				throw new Exception( ex.toString());
			}
			Status status = new Status();

			status.setHeader(response.getHeaders().toString());
			if( response.getStatusCode() == 202 )
				status.setStatus(true);
			else
				status.setStatus(false); 
			status.setMessage("Email sent successfully");
			return (status);
		}
		private Status sendMailWithValidation(Transaction transaction) throws Exception{
			//Check availability of transaction
			if( transaction == null )
				throw new Exception("Email format is bad");

			//check from availability
			if( transaction.getFrom() == null )
				throw new Exception("Sender email is missing");

			//validate from mail format
			validateMailFormat(transaction.getFrom());

			//reply_to is not present
			if( transaction.getReply_to() == null )
				transaction.setReply_to(transaction.getFrom());

			//validate reply_to mail format
			validateMailFormat(transaction.getReply_to());

			//validate personalizations
			if( transaction.getPersonalizations() == null || transaction.getPersonalizations().length == 0 )
				throw new Exception("Receiver email address is missing.");

			//content is missing
			if( transaction.getContent() == null || transaction.getContent().length == 0 )
				throw new Exception("Content is missing");

			return senderAllPresent(transaction);
		}
    public Status doPost(Transaction object, String SENDGRID_API_KEY)
            throws IOException {
    		this.SENDGRID_API_KEY = SENDGRID_API_KEY;
    		Status response = new Status();
    			try {
					response = (sendMailWithValidation(object));
    			}
				catch (Exception ex) {
					ex.printStackTrace();
					Status status = new Status();
					status.setMessage(ex.getMessage());
					status.setStatus(false);
					
					return status;
				}
        	return response;
		}
		protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
						throws ServletException, IOException {
				setAccessControlHeaders(resp);
				resp.setStatus(HttpServletResponse.SC_OK);
		}

		private void setAccessControlHeaders(HttpServletResponse resp) {
			resp.setHeader("Access-Control-Allow-Origin", "*");
			resp.setHeader("Access-Control-Allow-Credentials", "true");
			resp.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,DELETE,PUT");
			resp.setHeader("Access-Control-Allow-Headers", "Authorization,Access-Control-Allow-Headers,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
		}
}


