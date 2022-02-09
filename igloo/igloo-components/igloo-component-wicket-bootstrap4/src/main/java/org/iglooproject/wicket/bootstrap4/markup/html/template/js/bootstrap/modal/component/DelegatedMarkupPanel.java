package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.component;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupException;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.MarkupNotFoundException;
import org.apache.wicket.markup.html.panel.FragmentMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.Panel;

public class DelegatedMarkupPanel extends Panel {

	private static final long serialVersionUID = -5918955824552499431L;

	/** The wicket:id of the associated markup fragment */
	private final String associatedMarkupId;

	private final Class<?> markupClazz;

	public DelegatedMarkupPanel(String id, Class<?> markupClazz) {
		this(id, id, markupClazz);
	}

	public DelegatedMarkupPanel(String id, String associatedMarkupId, Class<?> markupClazz) {
		super(id);
		this.associatedMarkupId = associatedMarkupId;
		this.markupClazz = markupClazz;
	}

	@Override
	public Markup getAssociatedMarkup() {
		return MarkupFactory.get().getMarkup((MarkupContainer) findParent(markupClazz), markupClazz, false);
	}

	@Override
	protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
		final String markupId = associatedMarkupId;
		return new FragmentMarkupSourcingStrategy(markupId, null) {
			@Override
			public IMarkupFragment chooseMarkup(Component component) {
				return getAssociatedMarkup();
			}
			
			/*
			 * Same implementation as in FragmentMarkupSourcingStrategy, with just one difference: we allow the use of any
			 * tag on the fragment definition, not just wicket:fragment. See the comment "OVERRIDDEN HERE" below.
			 */
			@Override
			public IMarkupFragment getMarkup(MarkupContainer container, Component child) {
				// Get the markup to search for the fragment markup
				IMarkupFragment markup = chooseMarkup(container);
				if (markup == null)
				{
					throw new MarkupException("The fragments markup provider has no associated markup. " +
						"No markup to search for fragment markup with id: " + markupId);
				}

				// Search for the fragment markup
				IMarkupFragment childMarkup = markup.find(markupId);
				if (childMarkup == null)
				{
					// There is one more option if the markup provider has associated markup
					MarkupContainer markupProvider = getMarkupProvider(container);
					Markup associatedMarkup = markupProvider.getAssociatedMarkup();
					if (associatedMarkup != null)
					{
						markup = associatedMarkup;
						childMarkup = markup.find(markupId);
					}
				}

				if (childMarkup == null)
				{
					throw new MarkupNotFoundException("No Markup found for Fragment '" + markupId
							+ "' in providing markup container " + getMarkupProvider(container));
				}
				// OVERRIDDEN HERE: we want to allow the use of any tag, not just wicket:fragment
//				else
//				{
//					MarkupElement fragmentTag = childMarkup.get(0);
//					if ((fragmentTag instanceof WicketTag && ((WicketTag)fragmentTag).isFragementTag()) == false)
//					{
//						throw new MarkupNotFoundException("Markup found for Fragment '" + markupId
//								+ "' in providing markup container " + getMarkupProvider(container)
//								+ " is not a fragment tag");
//					}
//				}

				if (child == null)
				{
					return childMarkup;
				}

				// search for the child inside the fragment markup
				return childMarkup.find(child.getId());
			}
		};
	}

}
