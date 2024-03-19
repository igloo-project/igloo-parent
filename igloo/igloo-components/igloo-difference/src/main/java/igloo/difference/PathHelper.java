package igloo.difference;

import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.more.business.difference.selector.CollectionItemByIndexSelector;
import org.iglooproject.jpa.more.business.difference.selector.CollectionItemByKeySelector;
import org.iglooproject.jpa.more.business.difference.selector.MapValueByKeySelector;
import org.iglooproject.jpa.more.business.difference.selector.MultimapEntrySelector;

import de.danielbechler.diff.path.NodePath;
import de.danielbechler.diff.selector.BeanPropertyElementSelector;
import de.danielbechler.diff.selector.CollectionItemElementSelector;
import de.danielbechler.diff.selector.MapKeyElementSelector;
import de.danielbechler.diff.selector.RootElementSelector;

public class PathHelper {

	private PathHelper() {}

	/**
	 * Compare java-object-diff path {@link NodePath} (<pre>/justificatifs[...]/field/...</pre>) and {@link FieldPath}
	 * (<pre>justificatifs[*].field...</pre>)
	 */
	public static String normalizeNodePath(NodePath nodePath) {
		StringBuilder sb = new StringBuilder();
		// handle special case
		if (nodePath.getElementSelectors().size() == 1) {
			return ".";
		}
		for (var item : nodePath.getElementSelectors()) {
			if (item instanceof RootElementSelector) {
				// nothing
			} else if (item instanceof BeanPropertyElementSelector property) {
				sb.append(".");
				sb.append(property.getPropertyName());
			} else if (item instanceof CollectionItemElementSelector collection) {
				sb.append("[*]");
			} else if (item instanceof MapKeyElementSelector collection) {
				sb.append("[*]");
			} else if (item instanceof CollectionItemByIndexSelector collection) {
				sb.append("[*]");
			} else if (item instanceof CollectionItemByKeySelector<?, ?> collection) {
				sb.append("[*]");
			} else if (item instanceof MapValueByKeySelector<?> collection) {
				sb.append("[*]");
			} else if (item instanceof MultimapEntrySelector<?, ?> collection) {
				sb.append("[*]");
			} else {
				throw new IllegalStateException("Type %s not known".formatted(item.getClass().getName()));
			}
		}
		return sb.toString();
	}
}
