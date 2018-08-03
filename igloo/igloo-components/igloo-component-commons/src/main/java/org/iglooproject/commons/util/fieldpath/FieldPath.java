package org.iglooproject.commons.util.fieldpath;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.text.StringTokenizer;
import org.apache.commons.text.matcher.StringMatcher;
import org.apache.commons.text.matcher.StringMatcherFactory;
import org.bindgen.Bindable;
import org.bindgen.Binding;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * A symbolic representation of the "field path", i.e. the chain of properties that need to be accessed in order to get
 * a certain value from an object.
 * <p>The "FieldPath" may be represented as a character string, such as:
 * <ul>
 * <li>"": the root fieldPath
 * <li>".myProperty": the path from the root to "myProperty"
 * <li>".myProperty.subProperty": the path from the root to the "subProperty" property of "myProperty"
 * <li>".myCollectionProperty[*]": the path from the root to an element of the (collection) property "myCollectionProperty"
 * <li>".myCollectionProperty[*].subProperty": the path from the root to the "subProperty" property of an element of the (collection) property "myCollectionProperty"
 * </ul>
 * <p>Note that these are <em>symbolic</em> representations, and as such they are not suitable for automatic retrieval
 * of values from a given root. For this sort of treatments, prefer the use of {@link Binding Bindings}
 */
@Bindable
public class FieldPath implements Iterable<FieldPathComponent>, Serializable { // NOSONAR: this class CANNOT be final because of the ROOT static field. 

	private static final long serialVersionUID = 2486324046309761966L;
	
	public static final FieldPath ROOT = new FieldPath(ImmutableList.<FieldPathComponent>of()) {
		private static final long serialVersionUID = 1L;
		private Object readResolve() {
			return ROOT;
		}
	};

	private final List<FieldPathComponent> components;
	
	private static final StringMatcher DELIMITER_MATCHER = StringMatcherFactory.INSTANCE.charSetMatcher('.', '[', ']');
	private static final String ITEM_TOKEN = "*";
	
	public static final FieldPath fromString(String string) {
		if (StringUtils.isBlank(string)) {
			return null;
		} else {
			List<FieldPathComponent> components = Lists.newLinkedList();
			StringTokenizer tokenizer = new StringTokenizer(string, DELIMITER_MATCHER);
			for (String token : tokenizer.getTokenList()) {
				if (ITEM_TOKEN.equals(token)) {
					components.add(FieldPathComponent.ITEM);
				} else {
					components.add(new FieldPathPropertyComponent(token));
				}
			}
			return new FieldPath(components);
		}
	}
	
	public static final FieldPath fromBinding(Binding<?> binding) {
		if (binding == null) {
			return null;
		} else {
			List<FieldPathComponent> components = Lists.newLinkedList();
			Binding<?> currentBinding = binding;
			Binding<?> parentBinding = currentBinding.getParentBinding();
			while (parentBinding != null) {
				components.add(0, new FieldPathPropertyComponent(currentBinding.getName()));
				currentBinding = parentBinding;
				parentBinding = currentBinding.getParentBinding();
			}
			return new FieldPath(components);
		}
	}

	public static FieldPath of(Iterable<FieldPathComponent> components) {
		return new FieldPath(components);
	}

	public static FieldPath of(FieldPathComponent ... components) {
		return new FieldPath(ImmutableList.copyOf(components));
	}

	private FieldPath(Iterable<FieldPathComponent> components) {
		super();
		this.components = ImmutableList.copyOf(components);
	}

	private FieldPath(Iterable<FieldPathComponent> components, Iterable<FieldPathComponent> addedComponents) {
		super();
		this.components = ImmutableList.<FieldPathComponent>builder().addAll(components).addAll(addedComponents).build();
	}

	private FieldPath(Iterable<FieldPathComponent> components, FieldPathComponent addedComponent) {
		super();
		this.components = ImmutableList.<FieldPathComponent>builder().addAll(components).add(addedComponent).build();
	}
	
	@Override
	public Iterator<FieldPathComponent> iterator() {
		return components.iterator();
	}
	
	public int size() {
		return components.size();
	}
	
	protected Optional<FieldPathComponent> lastComponent() {
		int size = components.size();
		if (size > 0) {
			return Optional.of(components.get(size - 1));
		} else {
			return Optional.absent();
		}
	}
	
	public Optional<FieldPath> parent() {
		int size = components.size();
		if (size > 0) {
			return Optional.of(new FieldPath(components.subList(0, size - 1)));
		} else {
			return Optional.absent();
		}
	}
	
	public boolean isRoot() {
		return components.isEmpty();
	}
	
	public boolean isItem() {
		FieldPathComponent lastComponent = lastComponent().orNull();
		return lastComponent == FieldPathComponent.ITEM;
	}
	
	public Optional<FieldPath> container() {
		if (isItem()) {
			return parent();
		} else {
			return Optional.absent();
		}
	}
	
	public Optional<FieldPath> rootContainer() {
		int itemIndex = components.indexOf(FieldPathComponent.ITEM);
		if (itemIndex >= 0) {
			return Optional.of(new FieldPath(components.subList(0, itemIndex)));
		} else {
			return Optional.absent();
		}
	}
	
	public FieldPath item() {
		return new FieldPath(components, FieldPathComponent.ITEM);
	}
	
	public boolean startsWith(FieldPath other) {
		if (other.components.size() > components.size()) {
			return false;
		} else {
			return components.subList(0, other.components.size()).equals(other.components);
		}
	}
	
	public Optional<FieldPath> relativeTo(FieldPath other) {
		if (startsWith(other)) {
			return Optional.of(new FieldPath(components.subList(other.components.size(), components.size())));
		} else {
			return Optional.absent();
		}
	}
	
	public Optional<FieldPath> relativeToParent() {
		if (isRoot()) {
			return Optional.absent();
		} else {
			return relativeTo(parent().get());
		}
	}
	
	public FieldPath append(FieldPath other) {
		return new FieldPath(components, other.components);
	}
	
	public FieldPath append(FieldPathComponent component) {
		return new FieldPath(components, component);
	}
	
	public FieldPath compose(FieldPath other) {
		return new FieldPath(other.components, components);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FieldPath) {
			FieldPath other = (FieldPath) obj;
			return new EqualsBuilder()
					.append(components, other.components)
					.build();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(components)
				.build();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (FieldPathComponent component : components) {
			component.appendTo(builder);
		}
		return builder.toString();
	}
}
