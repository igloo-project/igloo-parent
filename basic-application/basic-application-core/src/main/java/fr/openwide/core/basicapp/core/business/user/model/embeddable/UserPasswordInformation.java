package fr.openwide.core.basicapp.core.business.user.model.embeddable;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.bindgen.Bindable;
import org.hibernate.annotations.Type;

import fr.openwide.core.basicapp.core.business.user.model.UserPasswordSecretQuestion;
import fr.openwide.core.commons.util.CloneUtils;

@Embeddable
@Bindable
public class UserPasswordInformation implements Serializable {

	private static final long serialVersionUID = -5388035775227696038L;

	@Column
	private Date lastUpdateDate;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private UserPasswordSecretQuestion secretQuestion;

	@Column
	@Type(type = "org.hibernate.type.StringClobType")
	private String secretQuestionAnswerHash;

	@Column
	@Type(type = "org.hibernate.type.StringClobType")
	private String history;

	public Date getLastUpdateDate() {
		return CloneUtils.clone(lastUpdateDate);
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = CloneUtils.clone(lastUpdateDate);
	}

	public UserPasswordSecretQuestion getSecretQuestion() {
		return secretQuestion;
	}

	public void setSecretQuestion(UserPasswordSecretQuestion secretQuestion) {
		this.secretQuestion = secretQuestion;
	}

	public String getSecretQuestionAnswerHash() {
		return secretQuestionAnswerHash;
	}

	public void setSecretQuestionAnswerHash(String secretQuestionAnswerHash) {
		this.secretQuestionAnswerHash = secretQuestionAnswerHash;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

}
