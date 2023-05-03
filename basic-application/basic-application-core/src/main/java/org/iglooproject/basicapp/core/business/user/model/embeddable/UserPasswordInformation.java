package org.iglooproject.basicapp.core.business.user.model.embeddable;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OrderColumn;

import org.bindgen.Bindable;
import org.iglooproject.commons.util.collections.CollectionUtils;

import com.google.common.collect.Lists;

@Embeddable
@Bindable
public class UserPasswordInformation implements Serializable {

	private static final long serialVersionUID = -5388035775227696038L;

	@Column
	private Instant lastUpdateDate;

	@ElementCollection
	@OrderColumn
	private List<String> history = Lists.newArrayList();

	public Instant getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Instant lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public List<String> getHistory() {
		return history;
	}

	public void setHistory(List<String> history) {
		CollectionUtils.replaceAll(this.history, history);
	}

}
