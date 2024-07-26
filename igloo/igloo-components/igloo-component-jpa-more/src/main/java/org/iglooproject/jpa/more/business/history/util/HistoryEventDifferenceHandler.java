package org.iglooproject.jpa.more.business.history.util;

import com.google.common.collect.ImmutableList;
import de.danielbechler.diff.path.NodePath;
import java.util.Arrays;
import java.util.List;
import org.bindgen.BindingRoot;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.more.business.difference.model.Difference;
import org.iglooproject.jpa.more.business.difference.util.DiffUtils;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;
import org.iglooproject.jpa.more.business.history.service.IGenericHistoryEventSummaryService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A {@link IHistoryDifferenceHandler} that will {@link
 * IGenericHistoryEventSummaryService#refresh(HistoryEventSummary, AbstractHistoryLog) refresh} a
 * {@link HistoryEventSummary} whenever differences are detected on particular paths.
 */
public class HistoryEventDifferenceHandler<T, HL extends AbstractHistoryLog<?, ?, ?>>
    implements IHistoryDifferenceHandler<T, HL> {

  @Autowired private IGenericHistoryEventSummaryService<?> historyEventSummaryService;

  private final BindingRoot<T, HistoryEventSummary> bindingToEventToRefresh;

  private final List<NodePath> pathsToCheck;

  public HistoryEventDifferenceHandler(
      BindingRoot<T, HistoryEventSummary> bindingToEventToRefresh, FieldPath... pathsToCheck) {
    this(
        bindingToEventToRefresh,
        Arrays.asList(pathsToCheck).stream().map(DiffUtils.toNodePathFunction())::iterator);
  }

  public HistoryEventDifferenceHandler(
      BindingRoot<T, HistoryEventSummary> bindingToEventToRefresh,
      Iterable<NodePath> pathsToCheck) {
    super();
    this.bindingToEventToRefresh = bindingToEventToRefresh;
    this.pathsToCheck = ImmutableList.copyOf(pathsToCheck);
  }

  @Override
  public void handle(T entity, Difference<? extends T> difference, HL historyLog) {
    for (NodePath path : pathsToCheck) {
      if (difference.hasChange(path)) {
        HistoryEventSummary evenement = bindingToEventToRefresh.getSafelyWithRoot(entity);
        historyEventSummaryService.refresh(evenement, historyLog);
        return;
      }
    }
  }
}
