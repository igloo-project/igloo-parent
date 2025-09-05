package org.iglooproject.jpa.more.business.difference.factory;

import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.selector.CollectionItemElementSelector;
import de.danielbechler.diff.selector.ElementSelector;
import de.danielbechler.diff.selector.MapKeyElementSelector;
import java.util.List;
import org.iglooproject.commons.util.exception.IllegalSwitchValueException;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.commons.util.rendering.IRenderer;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.more.business.difference.model.Difference;
import org.iglooproject.jpa.more.business.difference.selector.IKeyAwareSelector;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;
import org.iglooproject.jpa.more.business.history.model.atomic.HistoryDifferenceEventType;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryDifferencePath;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;
import org.iglooproject.jpa.more.business.history.service.IHistoryValueService;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractHistoryDifferenceFactory<T> implements IHistoryDifferenceFactory<T> {

  @Autowired private IRendererService rendererService;

  @Autowired private IHistoryValueService historyValueService;

  protected final <HD extends AbstractHistoryDifference<HD, ?>> void add(
      HD parent, List<HD> children) {
    parent.setDifferences(children);
    for (HD subDifference : children) {
      subDifference.setParentDifference(parent);
    }
  }

  protected final HistoryDifferenceEventType toHistoryDifferenceAction(DiffNode node) {
    switch (node.getState()) {
      case ADDED:
        return HistoryDifferenceEventType.ADDED;
      case CHANGED:
        return HistoryDifferenceEventType.UPDATED;
      case REMOVED:
        return HistoryDifferenceEventType.REMOVED;
      case UNTOUCHED:
        return HistoryDifferenceEventType.UNTOUCHED;
      default:
        throw new IllegalSwitchValueException(node.getState());
    }
  }

  @SuppressWarnings({
    "unchecked",
    "deprecation"
  }) // Ces méthodes sont les seuls moyen d'accéder aux clés...
  protected final HistoryValue newSelectorKeyHistoryValue(
      DiffNode parentNode, ElementSelector selector) {
    Object key;
    if (selector instanceof CollectionItemElementSelector) {
      key = ((CollectionItemElementSelector) selector).getItem();
    } else if (selector instanceof MapKeyElementSelector) {
      key = ((MapKeyElementSelector) selector).getKey();
    } else if (selector instanceof IKeyAwareSelector) {
      key = ((IKeyAwareSelector<?>) selector).getKey();
    } else {
      key = null;
    }
    if (key != null) {
      @SuppressWarnings("rawtypes")
      IRenderer renderer = rendererService.findRenderer(parentNode.getValueType(), key.getClass());
      return historyValueService.createHistoryValue(key, renderer);
    } else {
      return null;
    }
  }

  @SuppressWarnings({"rawtypes"})
  protected final <HD extends AbstractHistoryDifference<HD, ?>> HD newHistoryDifference(
      Supplier2<HD> historyDifferenceSupplier,
      Difference<T> rootDifference,
      DiffNode parentNode,
      DiffNode currentNode,
      FieldPath currentNodeRelativePath,
      HistoryDifferenceEventType action) {
    final T before = rootDifference.getBefore();
    final T after = rootDifference.getAfter();

    HistoryValue lastSelectorKeyAuditValue =
        newSelectorKeyHistoryValue(parentNode, currentNode.getElementSelector());

    HistoryDifferencePath historyDifferencePath =
        new HistoryDifferencePath(currentNodeRelativePath, lastSelectorKeyAuditValue);

    IRenderer renderer =
        rendererService.findRenderer(
            parentNode.getValueType(), currentNodeRelativePath, currentNode.getValueType());
    HistoryValue historyValueBefore = newHistoryValue(before, currentNode, renderer);
    HistoryValue historyValueAfter = newHistoryValue(after, currentNode, renderer);

    HD historyDifference = historyDifferenceSupplier.get();
    historyDifference.init(historyDifferencePath, action, historyValueBefore, historyValueAfter);
    return historyDifference;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected final HistoryValue newHistoryValue(T root, DiffNode node, IRenderer renderer) {
    Object value = node.canonicalGet(root);
    return historyValueService.createHistoryValue(value, renderer);
  }
}
