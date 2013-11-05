package fr.openwide.core.wicket.more.console.maintenance.ehcache.component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.wicket.markup.html.list.OddEvenListView;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.console.common.component.JavaPackageLabel;
import fr.openwide.core.wicket.more.console.maintenance.ehcache.model.EhCacheCacheInformation;
import fr.openwide.core.wicket.more.console.maintenance.ehcache.model.EhCacheCacheInformationModel;
import fr.openwide.core.wicket.more.console.maintenance.ehcache.model.EhCacheCacheListModel;
import fr.openwide.core.wicket.more.markup.html.basic.PercentageValueLabel;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.template.flash.zeroclipboard.ZeroClipboardBehavior;
import fr.openwide.core.wicket.more.markup.html.template.flash.zeroclipboard.ZeroClipboardDataAttributeAppender;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component.AjaxConfirmLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.listfilter.ListFilterBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.listfilter.ListFilterOptions;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.util.binding.CoreWicketMoreBinding;

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
						"console.maintenance.ehcache.cacheManager.purge.confirm", this,
							Model.of(item.getModelObject().getName()),
							Model.of(item.getModelObject().getName()));
				
				AjaxConfirmLink<CacheManager> purgerCache = new AjaxConfirmLink<CacheManager>("cacheManagerPurgeLink",
						item.getModel(), new ResourceModel("common.confirmTitle"), purgerCacheTextModel,
						new ResourceModel("common.yes"), new ResourceModel("common.no")) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						try {
							item.getModelObject().clearAll();
							getSession().success(getString("console.maintenance.ehcache.cacheManager.purge.success"));
						} catch (Exception e) {
							LOGGER.error("Erreur lors de la purge du cache manager", e);
							getSession().error(getString("console.maintenance.ehcache.cacheManager.purge.failure"));
						}
						
						cacheList.detach();
						FeedbackUtils.refreshFeedback(target, getPage());
						target.add(EhCacheCachePortfolioPanel.this);
					}
				};
				item.add(purgerCache);
				
				IModel<String> statisticsTextModel = new StringResourceModel(
						"console.maintenance.ehcache.cacheManager.statistics.clear.confirm", this,
							Model.of(item.getModelObject().getName()),
							Model.of(item.getModelObject().getName()));
				
				AjaxConfirmLink<CacheManager> clearStatistics = new AjaxConfirmLink<CacheManager>("clearStatistics",
						item.getModel(), new ResourceModel("common.confirmTitle"), statisticsTextModel,
						new ResourceModel("common.yes"), new ResourceModel("common.no")) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						try {
							Set<String> cacheNames = Sets.newTreeSet(GenericEntity.DEFAULT_STRING_COLLATOR);
							cacheNames.addAll(Arrays.asList(item.getModelObject().getCacheNames()));
							
							for (String cacheName : cacheNames) {
								Cache cache = item.getModelObject().getCache(cacheName);
								cache.clearStatistics();
							}
							getSession().success(getString("console.maintenance.ehcache.cacheManager.statistics.clear.success"));
						} catch (Exception e) {
							LOGGER.error("Erreur lors de la purge du cache manager", e);
							getSession().error(getString("console.maintenance.ehcache.cacheManager.statistics.clear.failure"));
						}
						
						cacheList.detach();
						FeedbackUtils.refreshFeedback(target, getPage());
						target.add(EhCacheCachePortfolioPanel.this);
					}
				};
				item.add(clearStatistics);
				
				CacheManager cacheManager = item.getModelObject();
				cacheList = new OddEvenListView<Cache>("cacheList", new EhCacheCacheListModel(
						cacheManager)) {
					private static final long serialVersionUID = 1890847954543497948L;
					
					@Override
					protected void populateItem(final ListItem<Cache> item) {
						EhCacheCacheInformationModel cacheInformationModel =
								new EhCacheCacheInformationModel(item.getModelObject());
						
						item.add(new JavaPackageLabel("cacheName", BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBinding.ehCacheCacheInformation().name())));
						item.add(new TextField<String>("cacheNameInput", BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBinding.ehCacheCacheInformation().name())));
						
						WebMarkupContainer copyToClipboard = new WebMarkupContainer("copyToClipboard");
						copyToClipboard.add(new ZeroClipboardDataAttributeAppender(BindingModel.of(
								cacheInformationModel, CoreWicketMoreBinding.ehCacheCacheInformation().name())));
						item.add(copyToClipboard);
						
						item.add(new Label("cacheMaxElements", BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBinding.ehCacheCacheInformation().maxElementsInMemory())));
						item.add(new Label("cacheStoredElements", BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBinding.ehCacheCacheInformation().memoryStoreObjectCount())));
						item.add(new Label("cacheEvictionCount", BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBinding.ehCacheCacheInformation().evictionCount())));
						item.add(new PercentageValueLabel("cacheCacheFillRatio", BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBinding.ehCacheCacheInformation().cacheFillRatio())));
						item.add(new Label("cacheHits", BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBinding.ehCacheCacheInformation().cacheHits())));
						item.add(new Label("cacheMisses", BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBinding.ehCacheCacheInformation().cacheMisses())));
						item.add(new PercentageValueLabel("cacheHitRatio", BindingModel.of(cacheInformationModel, 
								CoreWicketMoreBinding.ehCacheCacheInformation().hitRatio())));
						
						WebMarkupContainer progressBarsContainer = new WebMarkupContainer("progressBarsContainer");
						item.add(progressBarsContainer);
						EhCacheProgressBarComponent progressBarObject = 
								new EhCacheProgressBarComponent("progressBarObject", 
										BindingModel.of(cacheInformationModel, CoreWicketMoreBinding.ehCacheCacheInformation().cacheFillRatio()),
										true, 70, 90);
						progressBarsContainer.add(progressBarObject);
						
						EhCacheProgressBarComponent progressBarHit = 
								new EhCacheProgressBarComponent("progressBarHit", 
										BindingModel.of(cacheInformationModel, CoreWicketMoreBinding.ehCacheCacheInformation().hitRatio()),
										false, 20, 60);
						progressBarsContainer.add(progressBarHit);
						
						IModel<String> viderCacheTextModel = new StringResourceModel(
								"console.maintenance.ehcache.portfolio.viderCache.confirm", this, 
								BindingModel.of(cacheInformationModel, CoreWicketMoreBinding.ehCacheCacheInformation().name()),
								BindingModel.of(cacheInformationModel, CoreWicketMoreBinding.ehCacheCacheInformation().name()));
						
						AjaxConfirmLink<Cache> viderCache = new AjaxConfirmLink<Cache>("viderCache",
								item.getModel(), new ResourceModel("common.confirmTitle"), viderCacheTextModel,
								new ResourceModel("common.yes"), new ResourceModel("common.no")) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								try {
									item.getModelObject().removeAll();
									getSession()
											.success(getString("console.maintenance.ehcache.portfolio.viderCache.success"));
								} catch (Exception e) {
									LOGGER.error("Erreur lors du vidage du cache", e);
									getSession().error(getString("console.maintenance.ehcache.portfolio.viderCache.error"));
								}
								
								cacheList.detach();
								FeedbackUtils.refreshFeedback(target, getPage());
								target.add(EhCacheCachePortfolioPanel.this);
							}
						};
						item.add(viderCache);
						
						modificationPanel = new EhCacheCacheModificationPanel("modificationPanel",
								cacheInformationModel, EhCacheCachePortfolioPanel.this);
						
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
