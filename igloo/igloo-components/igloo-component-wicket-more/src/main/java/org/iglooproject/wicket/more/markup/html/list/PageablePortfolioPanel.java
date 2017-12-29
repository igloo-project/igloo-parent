package org.iglooproject.wicket.more.markup.html.list;

import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.wicket.markup.html.basic.CountLabel;
import org.iglooproject.wicket.more.markup.html.navigation.paging.HideablePagingNavigator;
import org.iglooproject.wicket.more.util.binding.CoreWicketMoreBindings;

public abstract class PageablePortfolioPanel<E extends GenericEntity<Long, ?>> extends AbstractGenericItemListPanel<E> {
	
	private static final long serialVersionUID = -1175407789883711459L;
	
	private final String countResourceKey;
	
	private final IModel<Integer> countModel;
	
	public PageablePortfolioPanel(String id, IModel<? extends List<E>> listModel, int itemsPerPage,
			String countResourceKey) {
		super(id, listModel, itemsPerPage);
		this.countResourceKey = countResourceKey;
		countModel = new PropertyModel<Integer>(listModel,
				CoreWicketMoreBindings.list().size().getPath());
	}
	
	public PageablePortfolioPanel(String id, IDataProvider<E> dataProvider, int itemsPerPage,
			String countResourceKey) {
		super(id, dataProvider, itemsPerPage);
		this.countResourceKey = countResourceKey;
		countModel = new PropertyModel<Integer>(dataProvider,
				CoreWicketMoreBindings.iBindableDataProvider().size().getPath());
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		// Count
		add(new CountLabel("count", countResourceKey, countModel));
		
		// Pagers
		add(new HideablePagingNavigator("topPager", getDataView()));
		add(new HideablePagingNavigator("bottomPager", getDataView()));
	}
}