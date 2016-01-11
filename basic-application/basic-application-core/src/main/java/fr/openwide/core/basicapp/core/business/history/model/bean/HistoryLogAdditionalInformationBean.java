package fr.openwide.core.basicapp.core.business.history.model.bean;

import fr.openwide.core.jpa.more.business.history.model.bean.AbstractHistoryLogAdditionalInformationBean;

public final class HistoryLogAdditionalInformationBean extends AbstractHistoryLogAdditionalInformationBean {
	
	public static final HistoryLogAdditionalInformationBean empty() {
		return new HistoryLogAdditionalInformationBean();
	}
	
	private HistoryLogAdditionalInformationBean(Object... secondaryObjects) {
		super(secondaryObjects);
	}
	
}