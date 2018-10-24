package entertainment.games.dto;

import java.util.ArrayList;
import java.util.List;

import entertainment.games.enums.MessageType;

public class MessagesDto {
	private List<Message> messages;
	
	public MessagesDto() {
		messages = new ArrayList<>();
	}

	//convenience methods for getting certain message types
	public List<Message> getAlerts() {
		List<Message> errors = new ArrayList<>();
		if (!messages.isEmpty()) {
			for (Message message : messages) {
				if (message.getType() == MessageType.ALERT) {
					errors.add(message);
				}
			}
		}
		return errors;
	}
	
	public List<Message> getConfirmations() {
		List<Message> errors = new ArrayList<>();
		if (!messages.isEmpty()) {
			for (Message message : messages) {
				if (message.getType() == MessageType.CONFIRMATION) {
					errors.add(message);
				}
			}
		}
		return errors;
	}
	
	public List<Message> getInformationals() {
		List<Message> errors = new ArrayList<>();
		if (!messages.isEmpty()) {
			for (Message message : messages) {
				if (message.getType() == MessageType.INFORMATIONAL) {
					errors.add(message);
				}
			}
		}
		return errors;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
}
