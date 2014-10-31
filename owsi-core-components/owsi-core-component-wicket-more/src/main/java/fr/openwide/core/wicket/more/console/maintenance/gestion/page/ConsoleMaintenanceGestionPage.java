package fr.openwide.core.wicket.more.console.maintenance.gestion.page;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.commons.util.functional.Predicates2;
import fr.openwide.core.jpa.more.business.parameter.service.IAbstractParameterService;
import fr.openwide.core.wicket.more.console.maintenance.template.ConsoleMaintenanceTemplate;
import fr.openwide.core.wicket.more.console.template.ConsoleTemplate;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureContainer;

public class ConsoleMaintenanceGestionPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = 5922671571871437558L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceGestionPage.class);

	@SpringBean
	private IAbstractParameterService parameterService;
	
	public ConsoleMaintenanceGestionPage(PageParameters parameters) {
		super(parameters);
		
		addHeadPageTitleKey("console.maintenance.gestion");
		
		IModel<Boolean> maintenanceModel = new LoadableDetachableModel<Boolean>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected Boolean load() {
				return parameterService.isInMaintenance();
			}
		};
		
		add(
				new EnclosureContainer("introMaintenanceActivee")
						.model(Predicates2.isTrue(), maintenanceModel),
				new EnclosureContainer("introMaintenanceDesactivee")
						.model(Predicates2.isFalse(), maintenanceModel),
				new Link<Void>("activerMaintenance") {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						try {
							parameterService.setParameterMaintenance(true);
							Session.get().success(getString("console.maintenance.gestion.maintenance.activer.success"));
						} catch (Exception e) {
							LOGGER.error("Erreur lors de l'activation du mode maintenance.", e);
							Session.get().error(getString("common.error.unexpected"));
						}
					}
				}
						.add(new EnclosureBehavior().model(Predicates2.isFalse(), maintenanceModel)),
				new Link<Void>("desactiverMaintenance") {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						try {
							parameterService.setParameterMaintenance(false);
							Session.get().success(getString("console.maintenance.gestion.maintenance.desactiver.success"));
						} catch (Exception e) {
							LOGGER.error("Erreur lors de la désactivation du mode maintenance.", e);
							Session.get().error(getString("common.error.unexpected"));
						}
					}
				}
						.add(new EnclosureBehavior().model(Predicates2.isTrue(), maintenanceModel))
		);
	}

	@Override
	protected Class<? extends ConsoleTemplate> getMenuItemPageClass() {
		return ConsoleMaintenanceGestionPage.class;
	}

}
