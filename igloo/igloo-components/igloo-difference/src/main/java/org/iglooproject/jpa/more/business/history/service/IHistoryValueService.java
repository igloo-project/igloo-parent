package org.iglooproject.jpa.more.business.history.service;

import java.util.Locale;

import com.google.common.base.Optional;

import org.iglooproject.commons.util.rendering.IRenderer;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;

public interface IHistoryValueService {

	<T> HistoryValue create(T object);
	
	<T> HistoryValue create(T value, IRenderer<? super T> renderer);
	
	Object retrieve(HistoryValue value);
	
	@SuppressWarnings("rawtypes")
	String render(HistoryValue value, IRenderer renderer, Locale locale);
	
	String render(HistoryValue value, Locale locale);
	
	/**
	 * @param historyValue The {@link HistoryValue} to be matched against.
	 * @param value The value that we want to compare to <code>historyValue</code>
	 * @return <code>Optional.of(true)</code> or <code>Optional.of(false)</code> if the answer could be determined, or
	 * <code>Optional.absent()</code> if there is not enough information in the {@link HistoryValue} to give a
	 * definitive answer.
	 */
	Optional<Boolean> matches(HistoryValue historyValue, Object value);

}
