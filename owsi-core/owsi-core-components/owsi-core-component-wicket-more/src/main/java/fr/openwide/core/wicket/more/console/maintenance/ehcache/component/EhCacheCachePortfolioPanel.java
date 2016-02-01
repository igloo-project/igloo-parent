package fr.openwide.core.wicket.more.console.maintenance.ehcache.component;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.wiquery.core.events.MouseEvent;

import fr.openwide.core.wicket.markup.html.list.OddEvenListView;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.console.common.component.JavaPackageLabel;
import fr.openwide.core.wicket.more.console.maintenance.ehcache.model.EhCacheCacheInformation;
import fr.openwide.core.wicket.more.console.maintenance.ehcache.model.EhCacheCacheInformationModel;
import fr.openwide.core.wicket.more.console.maintenance.ehcache.model.EhCacheCacheListModel;
import fr.openwide.core.wicket.more.markup.html.action.AbstractAjaxAction;
import fr.openwide.core.wicket.more.markup.html.basic.PercentageValueLabel;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.model.PercentageFloatToBigDecimalModel;
import fr.openwide.core.wicket.more.markup.html.template.flash.zeroclipboard.ZeroClipboardBehavior;
import fr.openwide.core.wicket.more.markup.html.template.flash.zeroclipboard.ZeroClipboardDataAttributeModifier;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component.AjaxConfirmLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.listfilter.ListFilterBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.listfilter.ListFilterOptions;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.util.binding.CoreWicketMoreBindings;

public class EhCacheCachePortfolioPanel extends GenericPanel<List<CacheManager>> {
	private static final long serialVersionUID = -7588751914016782042L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EhCacheCachePortfolioPanel.class);
	
	private ListView<Cache> cacheList;
	
	private EhCacheCacheModificationPanel modificationPanel;
	
	public EhCacheCachePortfolioPanel(String id, IModel<List<CacheManager>> model) {
		super(id, model);
		setOutputMarkupId(true);

		add(new ZeroClipboardBehavior());

		ListView<CacheManager> cacheManagerList = new ListView<CacheManager>("cacheManagerList", getModel()) {
			private static final long serialVersionUID = -6252990816594161742L;

			@Override
			protected void populateItem(final ListItem<CacheManager> item) {
				item.setOutputMarkupId(true);
				item.add(new ListFilterBehavior(getListFilterInitOptions()));
				
				item.add(new Label("cacheManagerName", item.getModelObject().getName()));
				
				IModel<String> purgerCacheTextModel = new StringResourceModel(
						"console.maintenance.ehcache.cacheManager.purge.confirm")
						.setParameters(item.getModelObject().getName());
				
				AjaxConfirmLink<CacheManager> purgerCache = AjaxConfirmLink.<CacheManager>build()
						.title(new ResourceModel("common.confirmTitle")).content(purgerCacheTextModel)
						.yesNo()
						.onClick(new AbstractAjaxAction() {
							private static final long serialVersionUID = 1L;
							@Override
							public void execute(AjaxRequestTarget target) {
								try {
									item.getModelObject().clearAll();
									getSession().success(getString("console.maintenance.ehcache.cacheManager.purge.success"));
								} catch (Exception e) {
									LOGGER.error("Erreur lors de la purge du cache manager", e);
									getSession().error(getString("console.maintenance.ehcache.cacheManager.purge.failure"));
								}
								
								cacheList.detach();
								FeedbackUtils.refreshFeedback(target, getPage());
								target.add(item);
							}
						})
						.create("cacheManagerPurgeLink", item.getModel());
				item.add(purgerCache);
				
				CacheManager cacheManager = item.getModelObject();
				cacheList = new OddEvenListView<Cache>("cacheList", new EhCacheCacheListModel(
						cacheManager)) {
					private static final long serialVersionUID = 1890847954543497948L;
					
					@Override
					protected void populateItem(final ListItem<Cache> item) {
						item.setOutputMarkupId(true);
						
						EhCacheCacheInformationModel cacheInformationModel =
								new EhCacheCacheInformationModel(item.getModelObject());
						
						item.add(new JavaPackageLabel("cacheName", BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBindings.ehCacheCacheInformation().name())));
						item.add(new TextField<String>("cacheNameInput", BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBindings.ehCacheCacheInformation().name())));
						
						WebMarkupContainer copyToClipboard = new WebMarkupContainer("copyToClipboard");
						copyToClipboard.add(new ZeroClipboardDataAttributeModifier(BindingModel.of(
								cacheInformationModel, CoreWicketMoreBindings.ehCacheCacheInformation().name())));
						item.add(copyToClipboard);
						
						item.add(new Label("cacheMaxElements", BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBindings.ehCacheCacheInformation().maxElementsInMemory())));
						item.add(new Label("cacheStoredElements", BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBindings.ehCacheCacheInformation().memoryStoreObjectCount())));
						item.add(new Label("cacheEvictionCount", BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBindings.ehCacheCacheInformation().evictionCount())));
						item.add(new PercentageValueLabel("cacheCacheFillRatio", PercentageFloatToBigDecimalModel.of(BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBindings.ehCacheCacheInformation().cacheFillRatio())), false, true));
						item.add(new Label("cacheHits", BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBindings.ehCacheCacheInformation().cacheHits())));
						item.add(new Label("cacheMisses", BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBindings.ehCacheCacheInformation().cacheMisses())));
						item.add(new PercentageValueLabel("cacheHitRatio", PercentageFloatToBigDecimalModel.of(BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBindings.ehCacheCacheInformation().hitRatio())), false, true));
						
						WebMarkupContainer progressBarsContainer = new WebMarkupContainer("progressBarsContainer");
						item.add(progressBarsContainer);
						EhCacheProgressBarComponent progressBarObject = 
								new EhCacheProgressBarComponent("progressBarObject", 
										BindingModel.of(cacheInformationModel, CoreWicketMoreBindings.ehCacheCacheInformation().cacheFillRatio()),
										true, 70, 90);
						progressBarsContainer.add(progressBarObject);
						
						EhCacheProgressBarComponent progressBarHit = 
								new EhCacheProgressBarComponent("progressBarHit", 
										BindingModel.of(cacheInformationModel, CoreWicketMoreBindings.ehCacheCacheInformation().hitRatio()),
										false, 20, 60);
						progressBarsContainer.add(progressBarHit);
						
						IModel<String> viderCacheTextModel = new StringResourceModel(
								"console.maintenance.ehcache.portfolio.viderCache.confirm")
								.setParameters(BindingModel.of(cacheInformationModel, CoreWicketMoreBindings.ehCacheCacheInformation().name()));
						
						AjaxConfirmLink<Cache> viderCache = AjaxConfirmLink.<Cache>build()
								.title(new ResourceModel("common.confirmTitle")).content(viderCacheTextModel)
								.yesNo()
								.onClick(new AbstractAjaxAction() {
									private static final long serialVersionUID = 1L;
									@Override
									public void execute(AjaxRequestTarget target) {
										try {
											item.getModelObject().removeAll();
											getSession().success(getString("console.maintenance.ehcache.portfolio.viderCache.success"));
										} catch (Exception e) {
											LOGGER.error("Erreur lors du vidage du cache", e);
											getSession().error(getString("console.maintenance.ehcache.portfolio.viderCache.error"));
										}
										
										cacheList.detach();
										FeedbackUtils.refreshFeedback(target, getPage());
										target.add(item);
									}
								})
								.create("viderCache", item.getModel());
						
						item.add(viderCache);
						
						modificationPanel = new EhCacheCacheModificationPanel("modificationPanel",
								cacheInformationModel, item);
						
						AbstractLink modifierCache = new AbstractLink("modifierCache") {
							private static final long serialVersionUID = 1L;
						};
						modifierCache.add(new AjaxModalOpenBehavior(modificationPanel, MouseEvent.CLICK) {
							private static final long serialVersionUID = 1L;
							
							@Override
							protected void onShow(AjaxRequestTarget target) {
								modificationPanel.getModel().setObject(new EhCacheCacheInformation(item.getModelObject()));
							}
						});
						item.add(modifierCache);
						item.add(modificationPanel);
					}
				};
				item.add(cacheList);
			}

		};
		add(cacheManagerList);
	}
	
	private ListFilterOptions getListFilterInitOptions() {
		ListFilterOptions options = new ListFilterOptions();
		
		options.setScanSelector(".cache-name");
		
		return options;
	}
}
