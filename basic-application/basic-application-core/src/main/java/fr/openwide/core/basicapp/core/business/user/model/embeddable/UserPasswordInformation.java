package fr.openwide.core.basicapp.core.business.user.model.embeddable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.OrderColumn;

import org.bindgen.Bindable;
import org.hibernate.annotations.Type;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.commons.util.collections.CollectionUtils;

@Embeddable
@Bindable
public class UserPasswordInformation implements Serializable {

	private static final long serialVersionUID = -5388035775227696038L;

	@Column
	private Date lastUpdateDate;

	@ElementCollection
	@OrderColumn
	@Type(type = "org.hibernate.type.StringClobType")
	private List<String> history;

	public Date getLastUpdateDate() {
		return CloneUtils.clone(lastUpdateDate);
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = CloneUtils.clone(lastUpdateDate);
	}

	public List<String> getHistory() {
		return history;
	}

	public void setHistory(List<String> history) {
		CollectionUtils.replaceAll(this.history, history);
	}

}
