package org.iglooproject.wicket.bootstrap5.console.maintenance.search.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.bootstrap.api.confirm.AjaxConfirmLink;
import org.iglooproject.bootstrap.api.confirm.ConfirmLink;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.action.IAction;
import org.iglooproject.wicket.action.IAjaxAction;
import org.iglooproject.wicket.bootstrap5.console.common.component.JavaClassesDropDownMultipleChoice;
import org.iglooproject.wicket.bootstrap5.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.model.Detachables;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

public class ConsoleMaintenanceSearchPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = 2718354274888156322L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceSearchPage.class);

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
			.page(ConsoleMaintenanceSearchPage.class);
	}

	@SpringBean
	private IHibernateSearchService hibernateSearchService;

	private final IModel<Collection<Class<?>>> classesChoicesModel;

	private final IModel<List<Class<?>>> classesModel = new ListModel<>(new ArrayList<>());
	private final IModel<String> idsModel = new Model<>();

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
							Session.get().success(getString("common.success"));
						} catch(Exception e) {
							LOGGER.error("Erreur lors la réindexation complète.", e);
							Session.get().error(getString("common.error.unexpected"));
						}
						setResponsePage(ConsoleMaintenanceSearchPage.class);
					}
				})
				.create("reindexContent")
		);
		
		Form<?> reindexClassesForm = new Form<>("reindexClassesForm");
		add(reindexClassesForm);
		
		classesChoicesModel = new LoadableDetachableModel<Collection<Class<?>>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected Collection<Class<?>> load() {
				try {
					return hibernateSearchService.getIndexedRootEntities();
				} catch (ServiceException e) {
					LOGGER.error("Erreur lors de la récupération de la liste des classes indexées.", e);
					Session.get().error(getString("console.maintenance.search.reindex.partial.error.getClasses"));
					reindexClassesForm.setVisibilityAllowed(false);
					return ImmutableList.of();
				}
			}
		};
		classesChoicesModel.getObject(); // early load
		
		reindexClassesForm.add(
			new JavaClassesDropDownMultipleChoice("classes", classesModel, Suppliers2.arrayList(), classesChoicesModel)
				.setRequired(true)
				.setLabel(new ResourceModel("console.maintenance.search.reindex.partial.form.classes"))
				.add(new LabelPlaceholderBehavior()),
			
			new TextArea<>("ids", idsModel)
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
							Set<Long> entityIds = Sets.newTreeSet();
							for (String entityIdString : StringUtils.splitAsList(StringUtils.normalizeNewLines(idsModel.getObject()),StringUtils.NEW_LINE_ANTISLASH_N)) {
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
							idsModel.setObject(null);
							
							Session.get().success(getString("common.success"));
						} catch (Exception e) {
							LOGGER.error("Erreur lors la réindexation d'entités.", e);
							Session.get().error(getString("common.error.unexpected"));
						}
						setResponsePage(ConsoleMaintenanceSearchPage.class);
					}
				})
				.create("reindexClasses")
		);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(classesChoicesModel, classesModel, idsModel);
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return ConsoleMaintenanceSearchPage.class;
	}

}
