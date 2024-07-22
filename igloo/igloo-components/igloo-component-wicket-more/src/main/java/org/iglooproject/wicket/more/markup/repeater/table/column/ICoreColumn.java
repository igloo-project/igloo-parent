package org.iglooproject.wicket.more.markup.repeater.table.column;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.html.sort.ISortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;

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
