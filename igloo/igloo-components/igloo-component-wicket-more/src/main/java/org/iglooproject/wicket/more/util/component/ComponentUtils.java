package org.iglooproject.wicket.more.util.component;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.wicket.Component;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.AbstractModalPopupPanel;

import com.google.common.collect.Streams;

public final class ComponentUtils {

	public static final Stream<Component> parents(Component component) {
		return Streams.stream(new ComponentParentIterator(component));
	}

	public static final boolean anyParent(Component component, Predicate<? super Component> predicate) {
		return parents(component).anyMatch(predicate);
	}

	public static final boolean anyParentModal(Component component) {
		return anyParent(component, c -> c instanceof AbstractModalPopupPanel);
	}

	public static final AbstractModalPopupPanel<?> getParentModal(Component component) {
		return parents(component).filter(c -> c instanceof AbstractModalPopupPanel).map(c -> (AbstractModalPopupPanel<?>) c).findFirst().orElse(null);
	}

	private static final class ComponentParentIterator implements Iterator<Component> {
		
		private Component component;
		
		public ComponentParentIterator(Component component) {
			this.component = component;
		}
		
		@Override
		public boolean hasNext() {
			return component != null && component.getParent() != null;
		}
		
		@Override
		public Component next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			component = component.getParent();
			return component;
		}
		
	}

	private ComponentUtils() {
	}

}
