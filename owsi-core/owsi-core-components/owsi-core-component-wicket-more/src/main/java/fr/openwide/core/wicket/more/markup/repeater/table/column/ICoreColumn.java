package fr.openwide.core.wicket.more.markup.repeater.table.column;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.sort.ISortIconStyle;
import fr.openwide.core.wicket.more.markup.html.sort.TableSortLink.CycleMode;

public interface ICoreColumn<T, S extends ISort<?>> extends IColumn<T, S> {
	
	void setSortProperty(S sort);

	IModel<String> getSortTooltipTextModel();

	void setSortTooltipTextModel(IModel<String> sortTooltipTextModel);
	
	ISortIconStyle getSortIconStyle();
	
	void setSortIconStyle(ISortIconStyle sortIconStyle);
	
	CycleMode getSortCycleMode();

	void setSortCycleMode(CycleMode sortCycleMode);

	ICoreColumn<T, S> addCssClass(String cssClass);

}
