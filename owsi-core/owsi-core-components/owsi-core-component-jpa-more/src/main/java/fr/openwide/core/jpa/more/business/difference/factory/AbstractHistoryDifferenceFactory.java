package fr.openwide.core.jpa.more.business.difference.factory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Supplier;

import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.selector.CollectionItemElementSelector;
import de.danielbechler.diff.selector.ElementSelector;
import de.danielbechler.diff.selector.MapKeyElementSelector;
import fr.openwide.core.commons.util.exception.IllegalSwitchValueException;
import fr.openwide.core.commons.util.rendering.IRenderer;
import fr.openwide.core.jpa.more.business.difference.model.Difference;
import fr.openwide.core.jpa.more.business.difference.selector.IKeyAwareSelector;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryDifference;
import fr.openwide.core.jpa.more.business.history.model.atomic.HistoryDifferenceEventType;
import fr.openwide.core.jpa.more.business.history.model.embeddable.HistoryDifferencePath;
import fr.openwide.core.jpa.more.business.history.model.embeddable.HistoryValue;
import fr.openwide.core.jpa.more.business.history.service.IHistoryValueService;
import fr.openwide.core.jpa.more.rendering.service.IRendererService;
import fr.openwide.core.jpa.more.util.fieldpath.model.FieldPath;

public abstract class AbstractHistoryDifferenceFactory<T> implements IHistoryDifferenceFactory<T> {
	
	@Autowired
	private IRendererService rendererService;
	
	@Autowired
	private IHistoryValueService historyValueService;

	protected final <HD extends AbstractHistoryDifference<HD, ?>> void add(HD parent, List<HD> children) {
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
	
	@SuppressWarnings({ "unchecked", "deprecation" }) // Ces méthodes sont les seuls moyen d'accéder aux clés...
	protected final HistoryValue newSelectorKeyHistoryValue(DiffNode parentNode, ElementSelector selector) {
		Object key;
		if (selector instanceof CollectionItemElementSelector) {
			key = ((CollectionItemElementSelector)selector).getItem();
		} else if (selector instanceof MapKeyElementSelector) {
			key = ((MapKeyElementSelector)selector).getKey();
		} else if (selector instanceof IKeyAwareSelector) {
			key = ((IKeyAwareSelector<?>)selector).getKey();
		} else {
			key = null;
		}
		if (key != null) {
			@SuppressWarnings("rawtypes")
			IRenderer renderer = rendererService.findRenderer(parentNode.getValueType(), key.getClass());
			return historyValueService.create(key, renderer);
		} else {
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes" })
	protected final <HD extends AbstractHistoryDifference<HD, ?>> HD newHistoryDifference(Supplier<HD> historyDifferenceSupplier,
			Difference<T> rootDifference, DiffNode parentNode,
			DiffNode currentNode, FieldPath currentNodeRelativePath, HistoryDifferenceEventType action) {
		final T before = rootDifference.getBefore();
		final T after = rootDifference.getAfter();
		
		HistoryValue lastSelectorKeyAuditValue = newSelectorKeyHistoryValue(parentNode, currentNode.getElementSelector()); 
		
		HistoryDifferencePath historyDifferencePath = new HistoryDifferencePath(currentNodeRelativePath, lastSelectorKeyAuditValue);
		
		IRenderer renderer = rendererService.findRenderer(parentNode.getValueType(), currentNodeRelativePath, currentNode.getValueType());
		HistoryValue historyValueBefore = newHistoryValue(before, currentNode, renderer);
		HistoryValue historyValueAfter = newHistoryValue(after, currentNode, renderer);
		
		HD historyDifference = historyDifferenceSupplier.get();
		historyDifference.init(historyDifferencePath, action, historyValueBefore, historyValueAfter);
		return historyDifference;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected final HistoryValue newHistoryValue(T root, DiffNode node, IRenderer renderer) {
		Object value = node.canonicalGet(root);
		return historyValueService.create(value, renderer);
	}
}