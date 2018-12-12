package entertainment.games.entity;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class LuAccountLockedReasonCode {
	@Id
	private Integer reasonCodeId;
	private String code;
	private String reason;
	
	public LuAccountLockedReasonCode() {}

	public Integer getReasonCodeId() {
		return reasonCodeId;
	}

	public void setReasonCodeId(Integer reasonCodeId) {
		this.reasonCodeId = reasonCodeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
