package fr.openwide.core.wicket.more.markup.html.list;

import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.odlabs.wiquery.core.javascript.JsUtils;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.wicket.more.markup.html.navigation.paging.HideablePagingNavigator;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy.Tipsy;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy.TipsyBehavior;


public abstract class GenericPortfolioPanel<E extends GenericEntity<Integer, E>> extends AbstractGenericItemListPanel<E> {

	private static final long serialVersionUID = 1L;
	
	private int itemsPerPage;
	
	public GenericPortfolioPanel(String id, IModel<? extends List<E>> listModel, int itemsPerPage) {
		super(id, listModel);
	}
	
	public GenericPortfolioPanel(String id, IDataProvider<E> dataProvider, int itemsPerPage) {
		super(id, dataProvider);
	}
	
	@Override
	protected void onInitialize() {
		getItemsPerPage();
		
		super.onInitialize();
		
		add(new HideablePagingNavigator("pager", getDataView()));
		
		final Tipsy tipsyOptions = new Tipsy();
		tipsyOptions.setFade(true);
		tipsyOptions.setLive(true);
		tipsyOptions.setTitle(JsUtils.quotes("title"));
		
		// needed as tipsy clears title attribute and add original-title when it shows popup
		add(new TipsyBehavior(".pagination [title], .pagination [original-title]", tipsyOptions));
	}

	@Override
	public int getItemsPerPage() {
		return itemsPerPage;
	}


	@Override
	protected IModel<String> getActionText() {
		return new ResourceModel("common.portfolio.action.viewDetails");
	}
	
}