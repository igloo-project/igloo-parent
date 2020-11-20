package org.iglooproject.wicket.bootstrap4.console.maintenance.ehcache.page;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.bootstrap4.console.maintenance.ehcache.component.ConsoleMaintenanceEhCacheCacheEditPopup;
import org.iglooproject.wicket.bootstrap4.console.maintenance.ehcache.component.ConsoleMaintenanceEhCacheProgressBarPanel;
import org.iglooproject.wicket.bootstrap4.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.console.maintenance.ehcache.model.EhCacheCacheInformationModel;
import org.iglooproject.wicket.more.console.maintenance.ehcache.model.EhCacheCacheListModel;
import org.iglooproject.wicket.more.console.maintenance.ehcache.model.EhCacheCacheManagerListModel;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.action.IAjaxAction;
import org.iglooproject.wicket.more.markup.html.basic.PlaceholderContainer;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.model.PercentageFloatToBigDecimalModel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.component.AjaxConfirmLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.more.markup.html.template.js.clipboard.ClipboardBehavior;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.rendering.CoreRenderers;
import org.iglooproject.wicket.more.rendering.ShortenedJavaNameRenderer;
import org.iglooproject.wicket.more.util.binding.CoreWicketMoreBindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.wiquery.core.events.KeyboardEvent;
import org.wicketstuff.wiquery.core.events.MouseEvent;

import com.google.common.collect.ImmutableList;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

public class ConsoleMaintenanceEhCachePage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = -7061578100018864443L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceEhCachePage.class);

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
			.page(ConsoleMaintenanceEhCachePage.class);
	}

	public ConsoleMaintenanceEhCachePage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(
			new ResourceModel("console.maintenance.ehcache"),
			ConsoleMaintenanceEhCachePage.linkDescriptor()
		));
		
		add(new ClipboardBehavior());
		
		add(
			new ListView<CacheManager>("cacheManagers", new EhCacheCacheManagerListModel()) {
				private static final long serialVersionUID = 1L;
				@Override
				protected void populateItem(final ListItem<CacheManager> item) {
					item.setOutputMarkupId(true);
					
					IModel<CacheManager> cacheManagerModel = item.getModel();
					IModel<String> cacheManagerNameModel = cacheManagerModel.map(CacheManager::getName);
					
					IModel<String> filterModel = new Model<>();
					
					WebMarkupContainer cachesContainer = new WebMarkupContainer("cachesContainer");
					
					IModel<List<Cache>> cachesModel = new LoadableDetachableModel<List<Cache>>() {
						private static final long serialVersionUID = 1L;
						@Override
						protected List<Cache> load() {
							List<Cache> caches = new EhCacheCacheListModel(cacheManagerNameModel).getObject();
							String filter = filterModel.getObject();
							
							if (!StringUtils.hasText(filter)) {
								return caches;
							}
							
							return caches.stream()
								.filter(
									Predicates2.compose(
										Predicates2.contains(Pattern.compile("(?i)^.*" + filter + ".*$")),
										Cache::getName
									)
								)
								.collect(ImmutableList.toImmutableList());
						}
					};
					
					item.add(
						new CoreLabel("name", cacheManagerNameModel),
						
						new TextField<>("filter", filterModel)
							.setLabel(new ResourceModel("console.maintenance.ehcache.cacheManager.filter"))
							.add(new LabelPlaceholderBehavior())
							.add(
								new AjaxFormComponentUpdatingBehavior(KeyboardEvent.KEYUP.getEventLabel()) {
									private static final long serialVersionUID = 1L;
									@Override
									protected void onUpdate(AjaxRequestTarget target) {
										target.add(cachesContainer);
									}
								}
							),
						
						AjaxConfirmLink.<CacheManager>build()
							.title(new ResourceModel("common.action.confirm.title"))
							.content(
								new StringResourceModel("console.maintenance.ehcache.cacheManager.purge.confirm")
									.setParameters(cacheManagerNameModel)
							)
							.yesNo()
							.onClick(new IAjaxAction() {
								private static final long serialVersionUID = 1L;
								@Override
								public void execute(AjaxRequestTarget target) {
									try {
										item.getModelObject().clearAll();
										filterModel.setObject(null);
										Session.get().success(getString("common.success"));
										target.add(item);
									} catch (Exception e) {
										LOGGER.error("Erreur lors de la purge du cache manager", e);
										Session.get().error(getString("common.error.unexpected"));
									}
									FeedbackUtils.refreshFeedback(target, getPage());
								}
							})
							.create("purge", item.getModel()),
						
						cachesContainer
							.add(
								new ListView<Cache>("caches", cachesModel) {
									private static final long serialVersionUID = 1L;
									@Override
									protected void populateItem(final ListItem<Cache> item) {
										item.setOutputMarkupId(true);
										
										IModel<Cache> cacheModel = item.getModel();
										
										EhCacheCacheInformationModel cacheInformationModel = new EhCacheCacheInformationModel(cacheModel);
										IModel<String> cacheNameModel = BindingModel.of(cacheInformationModel, CoreWicketMoreBindings.ehCacheCacheInformation().name());
										
										item.add(
											new CoreLabel("name", ShortenedJavaNameRenderer.get().asModel(cacheNameModel)),
											new TextField<>("nameInput", cacheNameModel),
											new BlankLink("copy")
												.add(new ClipboardBehavior().text(cacheNameModel)),
											new CoreLabel("evictionCount", BindingModel.of(cacheInformationModel, CoreWicketMoreBindings.ehCacheCacheInformation().evictionCount())),
											new CoreLabel("cacheHits", BindingModel.of(cacheInformationModel, CoreWicketMoreBindings.ehCacheCacheInformation().cacheHits())),
											new CoreLabel("cacheMisses", BindingModel.of(cacheInformationModel, CoreWicketMoreBindings.ehCacheCacheInformation().cacheMisses())),
											new CoreLabel("hitRatio",
												CoreRenderers.percent().asModel(
													PercentageFloatToBigDecimalModel.of(
														BindingModel.of(cacheInformationModel, CoreWicketMoreBindings.ehCacheCacheInformation().hitRatio())
													)
												)
											),
											new CoreLabel("memoryStoreObjectCount", BindingModel.of(cacheInformationModel, CoreWicketMoreBindings.ehCacheCacheInformation().memoryStoreObjectCount())),
											new CoreLabel("maxElementsInMemory", BindingModel.of(cacheInformationModel, CoreWicketMoreBindings.ehCacheCacheInformation().maxElementsInMemory())),
											new CoreLabel("cacheFillRatio",
												CoreRenderers.percent().asModel(
													PercentageFloatToBigDecimalModel.of(
														BindingModel.of(cacheInformationModel, CoreWicketMoreBindings.ehCacheCacheInformation().cacheFillRatio())
													)
												)
											)
										);
										
										item.add(
											new ConsoleMaintenanceEhCacheProgressBarPanel("progressBarObject",
												BindingModel.of(cacheInformationModel,
												CoreWicketMoreBindings.ehCacheCacheInformation().cacheFillRatio()),
												true, 0.7f, 0.9f
											),
											new ConsoleMaintenanceEhCacheProgressBarPanel("progressBarHit", 
												BindingModel.of(cacheInformationModel, CoreWicketMoreBindings.ehCacheCacheInformation().hitRatio()),
												false, 0.2f, 0.6f
											)
										);
										
										ConsoleMaintenanceEhCacheCacheEditPopup editPopup = new ConsoleMaintenanceEhCacheCacheEditPopup("editPopup", cacheInformationModel, item);
										item.add(
											editPopup,
											new BlankLink("edit")
												.add(
													new AjaxModalOpenBehavior(editPopup, MouseEvent.CLICK) {
														private static final long serialVersionUID = 1L;
														@Override
														protected void onShow(AjaxRequestTarget target) {
															editPopup.getModel().setObject(cacheInformationModel.getObject());
														}
													}
												)
										);
										
										item.add(
											AjaxConfirmLink.<Cache>build()
												.title(new ResourceModel("common.action.confirm.title"))
												.content(
													new StringResourceModel("console.maintenance.ehcache.cacheManager.cache.clear.confirm.content")
														.setParameters(cacheNameModel)
												)
												.yesNo()
												.onClick(new IAjaxAction() {
													private static final long serialVersionUID = 1L;
													@Override
													public void execute(AjaxRequestTarget target) {
														try {
															item.getModelObject().removeAll();
															Session.get().success(getString("common.success"));
															target.add(item);
														} catch (Exception e) {
															LOGGER.error("Erreur lors du vidage du cache", e);
															Session.get().error(getString("common.error.unexpected"));
														}
														FeedbackUtils.refreshFeedback(target, getPage());
													}
												})
												.create("clear", item.getModel())
										);
									}
								}
									.setReuseItems(true),
								new PlaceholderContainer("emptyList")
									.condition(Condition.collectionModelNotEmpty(cachesModel))
							)
							.setOutputMarkupId(true)
					);
				}
			}
				.setReuseItems(true)
		);
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return ConsoleMaintenanceEhCachePage.class;
	}

}
