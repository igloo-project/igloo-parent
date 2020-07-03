package org.iglooproject.wicket.bootstrap4.console.maintenance.search.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.bootstrap4.console.common.component.JavaClassesListMultipleChoice;
import org.iglooproject.wicket.bootstrap4.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.action.IAction;
import org.iglooproject.wicket.more.markup.html.action.IAjaxAction;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.component.AjaxConfirmLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.component.ConfirmLink;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

public class ConsoleMaintenanceSearchPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = 2718354274888156322L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceSearchPage.class);

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.page(ConsoleMaintenanceSearchPage.class);
	}

	@SpringBean(name="hibernateSearchService")
	protected IHibernateSearchService hibernateSearchService;
	
	public ConsoleMaintenanceSearchPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("console.maintenance.search")));
		
		add(
			ConfirmLink.<Void>build()
				.title(new ResourceModel("common.action.confirm.title"))
				.content(new ResourceModel("common.action.confirm.content"))
				.confirm()
				.onClick(new IAction() {
					private static final long serialVersionUID = 1L;
					@Override
					public void execute() {
						try {
							hibernateSearchService.reindexAll();
							Session.get().success(getString("console.maintenance.search.reindex.success"));
						} catch(Exception e) {
							LOGGER.error("console.maintenance.search.reindex.failure", e);
							Session.get().error(getString("console.maintenance.search.reindex.failure"));
						}
						setResponsePage(ConsoleMaintenanceSearchPage.class);
					}
				})
				.create("reindexContent")
		);
		
		Form<?> reindexClassesForm = new Form<>("reindexClassesForm");
		add(reindexClassesForm);
		
		try {
			final IModel<List<Class<?>>> classesModel = new ListModel<>(new ArrayList<Class<?>>());
			
			final TextArea<String> idsTextArea = new TextArea<>("ids", new Model<String>(""));
			
			reindexClassesForm.add(
				new JavaClassesListMultipleChoice("classes", classesModel, hibernateSearchService.getIndexedRootEntities())
					.setRequired(true)
					.setLabel(new ResourceModel("console.maintenance.search.reindex.partial.form.classes")),
				
				idsTextArea
					.setLabel(new ResourceModel("console.maintenance.search.reindex.partial.form.ids"))
					.add(new AttributeModifier("placeholder", new ResourceModel("console.maintenance.search.reindex.partial.form.ids.placeholder"))),
				
				AjaxConfirmLink.<Void>build()
					.title(new ResourceModel("common.action.confirm.title"))
					.content(new ResourceModel("common.action.confirm.content"))
					.submit(reindexClassesForm)
					.confirm()
					.onClick(new IAjaxAction() {
						private static final long serialVersionUID = 1L;
						@Override
						public void execute(AjaxRequestTarget target) {
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
												@SuppressWarnings("unchecked")
												Class<GenericEntity<Long, ?>> genericEntityClazz = (Class<GenericEntity<Long, ?>>) clazz;
												hibernateSearchService.reindexEntity(genericEntityClazz, entityId);
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
							setResponsePage(ConsoleMaintenanceSearchPage.class);
						}
					})
					.create("reindexClasses")
			);
		} catch (Exception e) {
			LOGGER.error("Erreur lors de la récupération de la liste des classes indexées", e);
			Session.get().error(getString("console.maintenance.search.reindex.partial.error.getClasses"));
			reindexClassesForm.setVisibilityAllowed(false);
		}
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return ConsoleMaintenanceSearchPage.class;
	}

}
