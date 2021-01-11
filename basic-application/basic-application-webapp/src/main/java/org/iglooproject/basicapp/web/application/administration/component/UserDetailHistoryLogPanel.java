package org.iglooproject.basicapp.web.application.administration.component;

import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.business.history.model.atomic.HistoryEventType;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.history.column.HistoryLogDetailColumn;
import org.iglooproject.basicapp.web.application.history.component.factory.CustomizableHistoryComponentFactory;
import org.iglooproject.basicapp.web.application.history.model.HistoryLogDataProvider;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.more.business.history.search.HistoryLogSort;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.util.DatePattern;

public class UserDetailHistoryLogPanel extends GenericPanel<User> {

	private static final long serialVersionUID = -5322394816999794266L;

	@SpringBean
	private IPropertyService propertyService;

	public UserDetailHistoryLogPanel(String id, final IModel<? extends User> userModel) {
		super(id, userModel);
		setOutputMarkupPlaceholderTag(true);
		
		HistoryLogDataProvider dataProvider = HistoryLogDataProvider.object(userModel);
		dataProvider.addMandatoryDifferenceEventType(HistoryEventType.UPDATE);
		
		add(
			DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
				.addLabelColumn(new ResourceModel("business.history.date"), Bindings.historyLog().date(), DatePattern.SHORT_DATETIME)
					.withSort(HistoryLogSort.DATE, SortIconStyle.DEFAULT, CycleMode.DEFAULT_REVERSE)
					.withClass("date date-lg")
				.addLabelColumn(new ResourceModel("business.history.subject"), Bindings.historyLog().subject())
					.withClass("text text-lg")
				.addColumn(
					new HistoryLogDetailColumn(
						new CustomizableHistoryComponentFactory()
							.compositeBlockForPath(FieldPath.fromString(".value0"))
					)
				)
					.withClass("text text-xl")
				.bootstrapCard()
					.title("administration.user.detail.audits")
					.ajaxPager(AddInPlacement.FOOTER_RIGHT)
				.build("history", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE))
		);
	}

}
