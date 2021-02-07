package nutent.entertainment.web.common;

import javax.servlet.http.HttpServletRequest;
import nutent.entertainment.web.dto.Message;
import nutent.entertainment.web.dto.MessagesDto;
import nutent.entertainment.web.enums.MessageType;

public class MessageUtils {
	
	public static void addMessage(HttpServletRequest request, String messageText, MessageType type) {
		MessagesDto messages = getMessages(request);
		if (messages == null) {
			messages = new MessagesDto();
		}
		Message message = new Message();
		message.setType(type);
		message.setMessage(messageText);
		messages.getMessages().add(message);
		setMessages(request, messages);
	}
	
	public static MessagesDto getMessages(HttpServletRequest request) {
		MessagesDto messages = (MessagesDto)request.getAttribute(ContextConstants.MESSAGES);
		return messages;
	}
	
	public static void setMessages(HttpServletRequest request, MessagesDto messages) {
		request.setAttribute(ContextConstants.MESSAGES, messages);
	}
	
	
}
