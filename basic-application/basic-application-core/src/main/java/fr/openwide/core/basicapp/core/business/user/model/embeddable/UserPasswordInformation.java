package fr.openwide.core.basicapp.core.business.user.model.embeddable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.bindgen.Bindable;
import org.hibernate.annotations.Type;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.spring.util.StringUtils;

@Embeddable
@Bindable
public class UserPasswordInformation implements Serializable {

	private static final long serialVersionUID = -5388035775227696038L;

	private static final String HISTORY_SEPARATOR = "\n";

	@Column
	private Date lastUpdateDate;

//	@ManyToOne(optional = true, fetch = FetchType.LAZY)
//	private UserPasswordSecretQuestion secretQuestion;

//	@Column
//	@Type(type = "org.hibernate.type.StringClobType")
//	private String secretQuestionAnswerHash;

	@Column
	@Type(type = "org.hibernate.type.StringClobType")
	private String history;

	public Date getLastUpdateDate() {
		return CloneUtils.clone(lastUpdateDate);
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = CloneUtils.clone(lastUpdateDate);
	}

//	public UserPasswordSecretQuestion getSecretQuestion() {
//		return secretQuestion;
//	}
//
//	public void setSecretQuestion(UserPasswordSecretQuestion secretQuestion) {
//		this.secretQuestion = secretQuestion;
//	}

//	public String getSecretQuestionAnswerHash() {
//		return secretQuestionAnswerHash;
//	}
//
//	public void setSecretQuestionAnswerHash(String secretQuestionAnswerHash) {
//		this.secretQuestionAnswerHash = secretQuestionAnswerHash;
//	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	@Transient
	public List<String> getHistoryList() {
		if (!StringUtils.hasText(history)) {
			return Lists.newArrayList();
		}
		
		return Lists.newLinkedList(
				Splitter.on(HISTORY_SEPARATOR)
						.omitEmptyStrings()
						.trimResults()
						.split(history)
		);
	}

}
