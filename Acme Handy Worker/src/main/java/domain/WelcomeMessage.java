package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class WelcomeMessage extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String languageCode;
	private String text;

	// Constructors -----------------------------------------------------------

	public WelcomeMessage() {
		super();
	}

	// Getters and Setters ---------------------------------------------------

	@NotBlank
	@Column(unique = true)
	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	@NotBlank
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
