package igloo.cache.wicket.page;

import igloo.bootstrap.confirm.AjaxConfirmLink;
import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.cache.binding.CacheBindings;
import igloo.cache.monitor.CacheManagerWrapper;
import igloo.cache.monitor.ICacheWrapper;
import igloo.cache.wicket.component.ConsoleMaintenanceCacheProgressBarPanel;
import igloo.cache.wicket.component.ConsoleMaintenanceJcacheCacheEditPopup;
import igloo.cache.wicket.model.CacheManagerListModel;
import igloo.console.maintenance.template.ConsoleMaintenanceTemplate;
import igloo.wicket.action.IAjaxAction;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.PlaceholderContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.model.BindingModel;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.model.PercentageFloatToBigDecimalModel;
import org.iglooproject.wicket.more.markup.html.template.js.clipboard.ClipboardBehavior;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.rendering.CoreRenderers;
import org.iglooproject.wicket.more.rendering.ShortenedJavaNameRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.wicketstuff.wiquery.core.events.KeyboardEvent;
import org.wicketstuff.wiquery.core.events.MouseEvent;

/**
 * Page listing all Spring-managed {@link CacheManager} / {@link Cache}.
 *
 * <p>For each {@link CacheManager}:
 *
 * <ul>
 *   <li>a button allows to reset all caches
 *   <li>all caches are listed
 * </ul>
 *
 * <p>For each {@link Cache}
 *
 * <ul>
 *   <li>statistics (hit/miss/hit ratio, current and max size) are displayed
 *   <li>a form allows to modify max size (only if cache is configured with a max size)
 *   <li>a button allows to reset the cache
 * </ul>
 */
public class ConsoleMaintenanceCachesPage extends ConsoleMaintenanceTemplate {

  private static final long serialVersionUID = -7061578100018864443L;

  private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceCachesPage.class);

  public ConsoleMaintenanceCachesPage(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("console.maintenance.caches")));

    add(new ClipboardBehavior());

    CacheManagerListModel cacheManagerListModel = new CacheManagerListModel();
    add(
        new ListView<CacheManagerWrapper>("cacheManagers", cacheManagerListModel) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void populateItem(final ListItem<CacheManagerWrapper> item) {
            item.setOutputMarkupId(true);

            IModel<CacheManagerWrapper> cacheManagerModel = item.getModel();
            IModel<String> cacheManagerNameModel =
                cacheManagerModel.map(CacheManagerWrapper::getName);

            IModel<String> filterModel = new Model<>();

            WebMarkupContainer cachesContainer = new WebMarkupContainer("cachesContainer");
            IModel<List<Cache>> cacheListModel =
                () ->
                    item.getModelObject().getCacheManager().getCacheNames().stream()
                        .map(n -> item.getModelObject().getCacheManager().getCache(n))
                        .collect(Collectors.toList());

            item.add(
                new CoreLabel("name", cacheManagerNameModel),
                new TextField<>("filter", filterModel)
                    .setLabel(new ResourceModel("console.maintenance.caches.cacheManager.filter"))
                    .add(new LabelPlaceholderBehavior())
                    .add(
                        new AjaxFormComponentUpdatingBehavior(KeyboardEvent.KEYUP.getEventLabel()) {
                          private static final long serialVersionUID = 1L;

                          @Override
                          protected void onUpdate(AjaxRequestTarget target) {
                            target.add(cachesContainer);
                          }
                        }),
                AjaxConfirmLink.<CacheManagerWrapper>build()
                    .title(new ResourceModel("common.action.confirm.title"))
                    .content(
                        new StringResourceModel(
                                "console.maintenance.caches.cacheManager.purge.confirm")
                            .setParameters(cacheManagerNameModel))
                    .yesNo()
                    .onClick(
                        new IAjaxAction() {
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
                        new ListView<Cache>("caches", cacheListModel) {
                          private static final long serialVersionUID = 1L;

                          @Override
                          protected void populateItem(final ListItem<Cache> item) {
                            item.setOutputMarkupId(true);

                            IModel<ICacheWrapper> cacheModel =
                                () -> ICacheWrapper.wrap(item.getModelObject());
                            IModel<String> cacheNameModel = cacheModel.map(ICacheWrapper::getName);

                            item.add(
                                new CoreLabel(
                                    "name",
                                    ShortenedJavaNameRenderer.get().asModel(cacheNameModel)),
                                new TextField<>("nameInput", cacheNameModel),
                                new BlankLink("copy")
                                    .add(new ClipboardBehavior().text(cacheNameModel)),
                                new CoreLabel(
                                    "evictionCount",
                                    BindingModel.of(
                                        cacheModel, CacheBindings.cache().cacheEvictions())),
                                new CoreLabel(
                                    "cacheHits",
                                    BindingModel.of(cacheModel, CacheBindings.cache().cacheHits())),
                                new CoreLabel(
                                    "cacheMisses",
                                    BindingModel.of(
                                        cacheModel, CacheBindings.cache().cacheMisses())),
                                new CoreLabel(
                                    "hitRatio",
                                    CoreRenderers.percent()
                                        .asModel(
                                            PercentageFloatToBigDecimalModel.of(
                                                BindingModel.of(
                                                    cacheModel,
                                                    CacheBindings.cache().cacheHitRatio())))),
                                new CoreLabel(
                                    "memoryStoreObjectCount",
                                    BindingModel.of(
                                        cacheModel, CacheBindings.cache().currentSize())),
                                new CoreLabel(
                                    "maxElementsInMemory",
                                    BindingModel.of(cacheModel, CacheBindings.cache().maxSize())),
                                new CoreLabel(
                                    "cacheFillRatio",
                                    CoreRenderers.percent()
                                        .asModel(
                                            PercentageFloatToBigDecimalModel.of(
                                                BindingModel.of(
                                                    cacheModel,
                                                    CacheBindings.cache().cacheFillRatio())))));

                            item.add(
                                new ConsoleMaintenanceCacheProgressBarPanel(
                                    "progressBarObject",
                                    BindingModel.of(
                                        cacheModel, CacheBindings.cache().cacheFillRatio()),
                                    true,
                                    0.7f,
                                    0.9f),
                                new ConsoleMaintenanceCacheProgressBarPanel(
                                    "progressBarHit",
                                    BindingModel.of(
                                        cacheModel, CacheBindings.cache().cacheHitRatio()),
                                    false,
                                    0.2f,
                                    0.6f));

                            ConsoleMaintenanceJcacheCacheEditPopup editPopup =
                                new ConsoleMaintenanceJcacheCacheEditPopup(
                                    "editPopup", cacheModel, item);
                            item.add(
                                editPopup,
                                new BlankLink("edit")
                                    .add(
                                        new AjaxModalOpenBehavior(editPopup, MouseEvent.CLICK) {
                                          private static final long serialVersionUID = 1L;

                                          @Override
                                          protected void onShow(AjaxRequestTarget target) {
                                            editPopup.setModel(cacheModel);
                                          }
                                        }));

                            item.add(
                                AjaxConfirmLink.<Cache>build()
                                    .title(new ResourceModel("common.action.confirm.title"))
                                    .content(
                                        new StringResourceModel(
                                                "console.maintenance.caches.cacheManager.cache.clear.confirm.content")
                                            .setParameters(cacheNameModel))
                                    .yesNo()
                                    .onClick(
                                        new IAjaxAction() {
                                          private static final long serialVersionUID = 1L;

                                          @Override
                                          public void execute(AjaxRequestTarget target) {
                                            try {
                                              item.getModelObject().clear();
                                              Session.get().success(getString("common.success"));
                                              target.add(item);
                                            } catch (Exception e) {
                                              LOGGER.error("Erreur lors du vidage du cache", e);
                                              Session.get()
                                                  .error(getString("common.error.unexpected"));
                                            }
                                            FeedbackUtils.refreshFeedback(target, getPage());
                                          }
                                        })
                                    .create("clear", item.getModel()));
                          }
                        }.setReuseItems(true),
                        new PlaceholderContainer("emptyList")
                            .condition(Condition.collectionModelNotEmpty(cacheListModel)))
                    .setOutputMarkupId(true));
          }
        }.setReuseItems(true));
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return ConsoleMaintenanceCachesPage.class;
  }
}
