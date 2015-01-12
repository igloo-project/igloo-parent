package fr.openwide.core.basicapp.web.application.administration.template;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.config.application.BasicApplicationConfigurer;
import fr.openwide.core.basicapp.web.application.administration.component.AbstractUserPortfolioPanel;
import fr.openwide.core.basicapp.web.application.administration.component.UserSearchPanel;
import fr.openwide.core.basicapp.web.application.administration.export.UserExcelTableExport;
import fr.openwide.core.basicapp.web.application.administration.form.AbstractUserPopupPanel;
import fr.openwide.core.basicapp.web.application.administration.model.UserDataProvider;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import fr.openwide.core.wicket.more.export.excel.component.AbstractExcelExportAjaxLink;
import fr.openwide.core.wicket.more.export.excel.component.ExcelExportWorkInProgressModalPopupPanel;
import fr.openwide.core.wicket.more.markup.html.link.BlankLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.AjaxModalOpenBehavior;

public abstract class AdministrationUserPortfolioTemplate<U extends User> extends AdministrationTemplate {

	private static final long serialVersionUID = 1824247169136460059L;

	@SpringBean
	private BasicApplicationConfigurer configurer;
	
	protected UserTypeDescriptor<U> typeDescriptor;
	
	public AdministrationUserPortfolioTemplate(PageParameters parameters, UserTypeDescriptor<U> typeDescriptor, IModel<String> pageTitleModel) {
		super(parameters);
		this.typeDescriptor = typeDescriptor;
		
		Logger logger = LoggerFactory.getLogger(AdministrationUserPortfolioTemplate.class);
		logger.debug("debug");
		logger.info("info");
		logger.warn("warn");
		logger.error("error");
		
		AbstractUserPopupPanel<U> addPopup = createAddPopupPanel("addPopup");
		
		final UserDataProvider<U> dataProvider = new UserDataProvider<>(typeDescriptor.getEntityClass());
		AbstractUserPortfolioPanel<U> portfolioPanel = createPortfolioPanel("portfolio", dataProvider, configurer.getPortfolioItemsPerPage());
		
		add(
				new Label("title", pageTitleModel),
				
				new UserSearchPanel<>("searchPanel", portfolioPanel.getPageable(), typeDescriptor, dataProvider),
				
				portfolioPanel,
				
				addPopup,
				new BlankLink("addButton")
						.add(
								new AjaxModalOpenBehavior(addPopup, MouseEvent.CLICK)
						)
		);
		
		// Export Excel
		ExcelExportWorkInProgressModalPopupPanel loadingPopup = new ExcelExportWorkInProgressModalPopupPanel("loadingPopup");
		add(
				loadingPopup,
				new AbstractExcelExportAjaxLink("exportExcelButton", loadingPopup, "export-users-") {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected Workbook generateWorkbook() {
						UserExcelTableExport export = new UserExcelTableExport(this);
						return export.generate(dataProvider);
					}
				}
		);
	}
	
	protected abstract AbstractUserPopupPanel<U> createAddPopupPanel(String wicketId);
	
	protected abstract AbstractUserPortfolioPanel<U> createPortfolioPanel(String wicketId, IDataProvider<U> dataProvider, int itemsPerPage);

	@SuppressWarnings("rawtypes")
	@Override
	protected Class<? extends AdministrationUserPortfolioTemplate> getSecondMenuPage() {
		return getClass();
	}
}
