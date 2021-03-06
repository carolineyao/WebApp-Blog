package webapp;
 
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
 
public class ShareServlet extends HttpServlet {
  	private static final Logger log = Logger.getLogger(ShareServlet.class.getName());
 
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {

    	sendEmail();
    	
    	try{
    		String to = req.getParameter("content");
    		

    		
    		  //TODO
		      // set the email
		      String from = "webappblogsc@gmail.com";

		      String host = "smtp.gmail.com";
		      //Properties properties = System.getProperties();

		      Properties properties = new Properties();
		      Session session = Session.getDefaultInstance(properties, null);
		      
		      properties.setProperty("mail.smtp.host", host);
		      //Session session = Session.getDefaultInstance(properties);
		      MimeMessage message = new MimeMessage(session);
		      message.setFrom(new InternetAddress(from));

	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

		     message.setSubject("You have to see this blog!");
		     message.setText("Hey check out this amazing blog I found!");

	         Transport.send(message);
	         //System.out.println("message sent");
	         
    	}
    	catch(AddressException e) {
					e.printStackTrace();
		}
		catch(MessagingException e) {
			e.printStackTrace();
		}
		catch (Exception ex) {
						ex.printStackTrace();
		}
        resp.sendRedirect("/webapp.jsp");
    }
    
    public static void sendEmail()
    {
    	Properties props = new Properties();
    	Session session = Session.getDefaultInstance(props, null);

    	try {
    	  Message msg = new MimeMessage(session);
    	  msg.setFrom(new InternetAddress("webappblogsc@gmail.com", "Example.com Admin"));
    	  msg.addRecipient(Message.RecipientType.TO,
    	                   new InternetAddress("sonaljain240@yahoo.com", "Mr. User"));
    	  msg.setSubject("Your Example.com account has been activated");
    	  Transport.send(msg);
    	} catch (AddressException e) {
    	  // ...
    	} catch (MessagingException e) {
    	  // ...
    	} catch (UnsupportedEncodingException e) {
    	  // ...
    	}
    }
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
              throws IOException {
    	UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        Entity greeting = new Entity("Email",user.getEmail());
        greeting.setProperty("email", user.getEmail());
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(greeting);
        
        resp.sendRedirect("/showallposts.jsp");
    }
}