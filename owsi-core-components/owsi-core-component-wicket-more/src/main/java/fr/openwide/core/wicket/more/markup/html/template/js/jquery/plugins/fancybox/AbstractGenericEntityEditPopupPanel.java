package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;

public abstract class AbstractGenericEntityEditPopupPanel<E extends GenericEntity<Integer, ?>> extends GenericPanel<E> {
	private static final long serialVersionUID = 7714064062698949195L;
	
	public AbstractGenericEntityEditPopupPanel(String id, IModel<? extends E> model) {
		super(id, model);
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		WebMarkupContainer popupLink = new WebMarkupContainer("fancyboxLink");
		DecoratedPopupPanel fancyboxPopupPanel = new DecoratedPopupPanel("fancyboxPopupPanel", "common.editPopup.title", true, false) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected Panel getNotDecoratedPopupContentPanel(String id) {
				return AbstractGenericEntityEditPopupPanel.this.getNotDecoratedPopupContentPanel(id);
			}
		};
		
		popupLink.add(new FancyboxPopupPanelBehavior(fancyboxPopupPanel));
		
		add(popupLink);
		add(fancyboxPopupPanel);
	}
	
	protected abstract Panel getNotDecoratedPopupContentPanel(String id);
}
