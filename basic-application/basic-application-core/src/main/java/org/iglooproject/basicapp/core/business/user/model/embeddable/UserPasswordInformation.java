package org.iglooproject.basicapp.core.business.user.model.embeddable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.OrderColumn;

import org.bindgen.Bindable;
import org.hibernate.annotations.Type;

import com.google.common.collect.Lists;

import org.iglooproject.basicapp.core.config.hibernate.TypeDefinitions;
import org.iglooproject.commons.util.CloneUtils;
import org.iglooproject.commons.util.collections.CollectionUtils;

@Embeddable
@Bindable
public class UserPasswordInformation implements Serializable {

	private static final long serialVersionUID = -5388035775227696038L;

	@Column
	private Date lastUpdateDate;

	@ElementCollection
	@OrderColumn
	@Type(type = TypeDefinitions.STRING_CLOB)
	private List<String> history = Lists.newArrayList();

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
