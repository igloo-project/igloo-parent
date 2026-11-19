package org.iglooproject.jpa.more.business.history.service;

import com.google.common.base.Optional;
import java.util.Locale;
import org.iglooproject.commons.util.rendering.IRenderer;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventValue;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;
import org.iglooproject.jpa.more.business.history.model.embeddable.IHistoryValue;
import org.jspecify.annotations.Nullable;

public interface IHistoryValueService {

  <T extends @Nullable Object> HistoryValue createHistoryValue(T object);

  <T extends @Nullable Object> HistoryEventValue createHistoryEventValue(T object);

  <T> HistoryValue createHistoryValue(T object, IRenderer<? super T> renderer);

  <T> HistoryEventValue createHistoryEventValue(T object, IRenderer<? super T> renderer);

  @Nullable Object retrieve(@Nullable IHistoryValue value);

  @SuppressWarnings("rawtypes")
  @Nullable String render(
      @Nullable IHistoryValue value, @Nullable IRenderer renderer, Locale locale);

  @Nullable String render(@Nullable IHistoryValue value, Locale locale);

  /**
   * @param historyValue The {@link HistoryValue} to be matched against.
   * @param value The value that we want to compare to <code>historyValue</code>
   * @return <code>Optional.of(true)</code> or <code>Optional.of(false)</code> if the answer could
   *     be determined, or <code>Optional.absent()</code> if there is not enough information in the
   *     {@link HistoryValue} to give a definitive answer.
   */
  Optional<Boolean> matches(@Nullable IHistoryValue historyValue, @Nullable Object value);
}
