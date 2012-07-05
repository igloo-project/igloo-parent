package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.component;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.html.panel.FragmentMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.Panel;

public class DelegatedMarkupPanel extends Panel {

	private static final long serialVersionUID = -5918955824552499431L;

	private final Class<?> markupClazz;

	public DelegatedMarkupPanel(String id, Class<?> markupClazz) {
		super(id);
		this.markupClazz = markupClazz;
	}

	@Override
	public Markup getAssociatedMarkup() {
		return MarkupFactory.get().getMarkup((MarkupContainer) findParent(markupClazz), markupClazz, false);
	}

	@Override
	protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
		return new FragmentMarkupSourcingStrategy(getId(), null) {
			@Override
			public IMarkupFragment chooseMarkup(Component component) {
				return getAssociatedMarkup();
			}
		};
	}

}
