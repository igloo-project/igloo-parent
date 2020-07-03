package org.iglooproject.wicket.bootstrap3.console.maintenance.search.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.bootstrap3.console.common.component.JavaClassesListMultipleChoice;
import org.iglooproject.wicket.bootstrap3.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.bootstrap3.console.template.ConsoleTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

public class ConsoleMaintenanceSearchPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = 2718354274888156322L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceSearchPage.class);
	
	@SpringBean(name="hibernateSearchService")
	protected IHibernateSearchService hibernateSearchService;
	
	public ConsoleMaintenanceSearchPage(PageParameters parameters) {
		super(parameters);
		
		addHeadPageTitleKey("console.maintenance.search");
		
		// Réindexation complète
		add(new Link<Void>("reindexContentLink") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				try {
					hibernateSearchService.reindexAll();
					Session.get().success(getString("console.maintenance.search.reindex.success"));
				} catch(Exception e) {
					LOGGER.error("console.maintenance.search.reindex.failure", e);
					Session.get().error(getString("console.maintenance.search.reindex.failure"));
				}
				setResponsePage(ConsoleMaintenanceSearchPage.class);
			}
		});
		
		// Réindexation partielle
		Form<?> reindexClassesForm = new Form<>("reindexClassesForm");
		add(reindexClassesForm);
		
		try {
			final IModel<List<Class<?>>> classesModel = new ListModel<>(new ArrayList<Class<?>>());
			
			JavaClassesListMultipleChoice classesChoice = new JavaClassesListMultipleChoice("classes", classesModel,
					hibernateSearchService.getIndexedRootEntities());
			classesChoice.setLabel(new ResourceModel("console.maintenance.search.reindex.partial.form.classes"));
			classesChoice.setRequired(true);
			reindexClassesForm.add(classesChoice);
			
			final TextArea<String> idsTextArea = new TextArea<>("ids", new Model<String>(""));
			idsTextArea.setLabel(new ResourceModel("console.maintenance.search.reindex.partial.form.ids"));
			idsTextArea.add(new AttributeModifier("placeholder",
					new ResourceModel("console.maintenance.search.reindex.partial.form.ids.placeholder")));
			reindexClassesForm.add(idsTextArea);
			
			reindexClassesForm.add(new SubmitLink("reindexClassesLink", reindexClassesForm) {
				private static final long serialVersionUID = 6601566553381397066L;
				
				@SuppressWarnings("unchecked")
				@Override
				public void onSubmit() {
					try {
						Set<Long> entityIds = Sets.newHashSet();
						
						for (String entityIdString : StringUtils.splitAsList(
								StringUtils.normalizeNewLines(idsTextArea.getModelObject()),
								StringUtils.NEW_LINE_ANTISLASH_N)) {
							if (StringUtils.hasText(entityIdString)) {
								try {
									entityIds.add(Long.parseLong(StringUtils.trimWhitespace(entityIdString)));
								} catch (NumberFormatException e) {
									// On ignore les id saisis qui ne sont pas numériques
								}
							}
						}
						
						if (entityIds.isEmpty()) {
							hibernateSearchService.reindexClasses(classesModel.getObject());
						} else {
							for (Class<?> clazz : classesModel.getObject()) {
								for (Long entityId : entityIds) {
									try {
										hibernateSearchService.reindexEntity((Class<GenericEntity<Long, ?>>) clazz, entityId);
									} catch (IllegalArgumentException e) {
										// On ignore les classes qui ne sont pas des GenericEntity.
									}
								}
							}
						}
						classesModel.getObject().clear();
						idsTextArea.setModelObject("");
						
						Session.get().success(getString("console.maintenance.search.reindex.success"));
					} catch (Exception e) {
						LOGGER.error("Erreur lors la réindexation d'entités", e);
						Session.get().error(getString("console.maintenance.search.reindex.failure"));
					}
				}
			});
		} catch (Exception e) {
			LOGGER.error("Erreur lors de la récupération de la liste des classes indexées", e);
			Session.get().error(getString("console.maintenance.search.reindex.partial.error.getClasses"));
			reindexClassesForm.setVisible(false);
		}
	}
	
	@Override
	protected Class<? extends ConsoleTemplate> getMenuItemPageClass() {
		return ConsoleMaintenanceSearchPage.class;
	}
}
