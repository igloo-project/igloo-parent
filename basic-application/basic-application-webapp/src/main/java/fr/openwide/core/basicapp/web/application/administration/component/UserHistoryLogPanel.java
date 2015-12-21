package fr.openwide.core.basicapp.web.application.administration.component;

import static fr.openwide.core.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.basicapp.core.business.history.model.atomic.HistoryEventType;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.history.column.HistoryLogDetailColumn;
import fr.openwide.core.basicapp.web.application.history.model.HistoryLogDataProvider;
import fr.openwide.core.jpa.more.business.history.search.HistoryLogSort;
import fr.openwide.core.spring.property.service.IPropertyService;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.DecoratedCoreDataTablePanel.AddInPlacement;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.DataTableBuilder;
import fr.openwide.core.wicket.more.markup.html.sort.SortIconStyle;
import fr.openwide.core.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import fr.openwide.core.wicket.more.util.DatePattern;

public class UserHistoryLogPanel extends GenericPanel<User> {
	
	private static final long serialVersionUID = 809335942700940194L;
	
	@SpringBean
	private IPropertyService propertyService;

	public UserHistoryLogPanel(String id, final IModel<? extends User> userModel) {
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
								.withClass("text text-sm")
						.addLabelColumn(new ResourceModel("business.history.action"), Bindings.historyLog().eventType())
								.withClass("text text-sm")
						.addColumn(new HistoryLogDetailColumn())
								.withClass("text")
						.bootstrapPanel()
								.title("administration.user.audits")
								.ajaxPager(AddInPlacement.FOOTER_RIGHT)
						.build("history", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE))
		);
	}

}
