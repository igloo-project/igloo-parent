package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class FancyboxPopupPanel extends Panel {

	private static final long serialVersionUID = -5718336639316134199L;

	private static final String POPUP_CONTENT_ID = "popupContent";

	private WebMarkupContainer replaceableContainer;

	private WebMarkupContainer hideableContainer;

	private Panel popupContent;

	public FancyboxPopupPanel(String id, boolean hidden) {
		super(id);
		
		// this container is always present ; this is the replaceable container. Its content can be replaced by Ajax
		// queries
		replaceableContainer = new WebMarkupContainer("replaceableContainer");
		replaceableContainer.setOutputMarkupId(true);
		
		// this container is present only if popup has been requested, otherwise it is not rendered (setVisible(false))
		hideableContainer = new WebMarkupContainer("hideableContainer");
		hideableContainer.setVisible(!hidden);
		
		WebMarkupContainer placeholder = new WebMarkupContainer(POPUP_CONTENT_ID);
		
		hideableContainer.add(placeholder);
		replaceableContainer.add(hideableContainer);
		add(replaceableContainer);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		initializePopupContent();
	}

	/**
	 * popup content is lazy ; if it is the first time we switch to visible, then we need to load content
	 */
	public void initializePopupContent() {
		if (hideableContainer.isVisible() && popupContent == null) {
			hideableContainer.removeAll();
			popupContent = getPopupContentPanel(POPUP_CONTENT_ID);
			hideableContainer.add(popupContent);
		}
	}

	public void show(AjaxRequestTarget target) {
		hideableContainer.setVisible(true);
		initializePopupContent();
		if (popupContent instanceof IPopupContentAwareComponent) {
			((IPopupContentAwareComponent) popupContent).show(target);
		}
		if (target != null) {
			target.add(replaceableContainer);
		}
	}

	public void hide(AjaxRequestTarget target) {
		hideableContainer.setVisible(false);
		if (popupContent instanceof IPopupContentAwareComponent) {
			((IPopupContentAwareComponent) popupContent).hide(target);
		}
		if (target != null) {
			target.add(replaceableContainer);
		}
	}

	public WebMarkupContainer getReplaceableContainer() {
		return replaceableContainer;
	}

	public WebMarkupContainer getHideableContainer() {
		return hideableContainer;
	}

	protected abstract Panel getPopupContentPanel(String id);

}
