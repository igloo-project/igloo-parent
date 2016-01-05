package fr.openwide.core.basicapp.core.business.history.model.bean;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.more.business.history.model.bean.AbstractHistoryLogObjectsBean;

public final class HistoryLogObjectsBean<T> extends AbstractHistoryLogObjectsBean<T> {
	
	public static HistoryLogObjectsBean<User> of(User user) {
		return new HistoryLogObjectsBean<>(user);
	}
	
	private HistoryLogObjectsBean(T mainObject, Object... secondaryObjects) {
		super(mainObject, secondaryObjects);
	}
	
}