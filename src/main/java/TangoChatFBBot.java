package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultJsonMapper;
import com.restfb.FacebookClient;
import com.restfb.JsonMapper;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.Message;
import com.restfb.types.send.SendResponse;
import com.restfb.types.webhook.WebhookEntry;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.types.webhook.messaging.MessagingItem;

@WebServlet("/Webhook")
public class TangoChatFBBot extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private String accessToken = "EAAFpN5jZChuEBAI4sjm8nXLPOs6rO9ZB3GZBYZA3LQybwU3kNYQya6ZCHZBBbZChBFhFkerf1ZAunlgadN43HCbwlJCqqSp3tZCWl9SZCyZARcpB2f4NHzeV9jm0t5a0TpCQfBltsRdv7Xu6sBAdTPFTazwopIpS1ZCW9dCEGMqitXQ6ZCAZDZD";
	
	private String verifyToken ="BotTango";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String hubToken = request.getParameter("hub.verify_token");
		String hubChallenge = request.getParameter("hub.challenge");
		
		if(verifyToken.equals(hubToken)) {
			response.getWriter().write(hubChallenge);
			response.getWriter().flush();
			response.getWriter().close();
		}else {
			response.getWriter().write("incorrect token");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer sb =  new StringBuffer();
		BufferedReader br = request.getReader();
		String line = "";
		
		while((line = br.readLine()) != null ) {
			sb.append(line);
		}
		
		JsonMapper mapper = new DefaultJsonMapper();
		WebhookObject webHookObject = mapper.toJavaObject(sb.toString(), WebhookObject.class);
		
		for(WebhookEntry entry : webHookObject.getEntryList()) {
			if(entry.getMessaging() != null) {
				for(MessagingItem mItem : entry.getMessaging()) {
					String senderId = mItem.getSender().getId();
					IdMessageRecipient recipient = new IdMessageRecipient(senderId);
					
					if(mItem.getMessage() != null && mItem.getMessage().getText() != null) {
						SendMessage(recipient, new Message("hi"));
					}
				}
			}
		}
	}
	
	void SendMessage(IdMessageRecipient recipient, Message message) {
		FacebookClient pageClient = new DefaultFacebookClient(accessToken, Version.VERSION_2_12);

		SendResponse resp = pageClient.publish("me/messages", SendResponse.class,
		     Parameter.with("recipient", recipient),
			 Parameter.with("message", message));
	}

}
