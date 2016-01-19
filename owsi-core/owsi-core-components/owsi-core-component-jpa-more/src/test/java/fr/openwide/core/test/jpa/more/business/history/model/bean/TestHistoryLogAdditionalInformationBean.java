package fr.openwide.core.test.jpa.more.business.history.model.bean;

import fr.openwide.core.jpa.more.business.history.model.bean.AbstractHistoryLogAdditionalInformationBean;
import fr.openwide.core.test.jpa.more.business.entity.model.TestEntity;

public final class TestHistoryLogAdditionalInformationBean extends AbstractHistoryLogAdditionalInformationBean {
	
	public static final TestHistoryLogAdditionalInformationBean empty() {
		return new TestHistoryLogAdditionalInformationBean();
	}
	
	public static final TestHistoryLogAdditionalInformationBean of(TestEntity secondaryObject) {
		return new TestHistoryLogAdditionalInformationBean(secondaryObject);
	}
	
	private TestHistoryLogAdditionalInformationBean(Object... secondaryObjects) {
		super(secondaryObjects);
	}
	
}