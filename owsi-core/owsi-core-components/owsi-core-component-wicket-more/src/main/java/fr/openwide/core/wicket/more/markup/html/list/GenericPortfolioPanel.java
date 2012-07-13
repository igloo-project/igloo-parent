package fr.openwide.core.wicket.more.markup.html.list;

import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.wicket.more.markup.html.navigation.paging.HideablePagingNavigator;

public abstract class GenericPortfolioPanel<E extends GenericEntity<Long, ?>> extends AbstractGenericItemListPanel<E> {

	private static final long serialVersionUID = 3343071412882576215L;
	
	private int itemsPerPage;
	
	public GenericPortfolioPanel(String id, IModel<? extends List<E>> listModel, int itemsPerPage) {
		super(id, listModel);
		
		this.itemsPerPage = itemsPerPage;
	}
	
	public GenericPortfolioPanel(String id, IDataProvider<E> dataProvider, int itemsPerPage) {
		super(id, dataProvider);
		
		this.itemsPerPage = itemsPerPage;
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		add(new HideablePagingNavigator("pager", getDataView()));
	}
	
	@Override
	public int getItemsPerPage() {
		return itemsPerPage;
	}
}