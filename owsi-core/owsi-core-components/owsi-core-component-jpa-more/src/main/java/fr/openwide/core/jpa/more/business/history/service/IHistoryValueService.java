package fr.openwide.core.jpa.more.business.history.service;

import fr.openwide.core.commons.util.rendering.IRenderer;
import fr.openwide.core.jpa.more.business.history.model.embeddable.HistoryValue;

public interface IHistoryValueService {

	<T> HistoryValue create(T object);
	
	<T> HistoryValue create(T value, IRenderer<? super T> renderer);
	
	Object retrieve(HistoryValue value);
	
	@SuppressWarnings("rawtypes")
	String render(HistoryValue value, IRenderer renderer);
	
	String render(HistoryValue value);

}
