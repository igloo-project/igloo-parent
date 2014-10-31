package fr.openwide.core.basicapp.web.application.administration.component;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.wicket.more.markup.html.navigation.paging.HideablePagingNavigator;
import fr.openwide.core.wicket.more.markup.repeater.data.OddEvenDataView;

public abstract class AbstractUserPortfolioPanel<U extends User> extends Panel {

	private static final long serialVersionUID = 3233717910646663737L;
	
	private final DataView<U> dataView;
	
	public AbstractUserPortfolioPanel(String wicketId, IDataProvider<U> dataProvider, int itemsPerPage) {
		super(wicketId);
		
		dataView = new OddEvenDataView<U>("item", dataProvider, itemsPerPage) {
			private static final long serialVersionUID = 8487422965167269031L;

			@Override
			protected void populateItem(final Item<U> item) {
				final IModel<U> itemModel = item.getModel();
				
				AbstractUserPortfolioPanel.this.populateItem(item, itemModel);
			}
		};
		add(dataView);

		add(new WebMarkupContainer("emptyList") {
			private static final long serialVersionUID = 6700720373087584498L;

			@Override
			public boolean isVisible() {
				return dataView.getDataProvider().size() == 0;
			}
		});
		
		add(new HideablePagingNavigator("pager", dataView));
	}
	
	public IPageable getPageable() {
		return dataView;
	}

	protected abstract void populateItem(final Item<U> item, final IModel<U> itemModel);

}
