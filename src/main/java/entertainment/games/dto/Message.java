package entertainment.games.dto;

import entertainment.games.enums.MessageType;

public class Message {
	private MessageType type;
	private String message;
	
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
