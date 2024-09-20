package basicapp.front.announcement.page;

import static basicapp.front.common.util.CssClassConstants.BTN_TABLE_ROW_ACTION;
import static basicapp.front.common.util.CssClassConstants.TABLE_ROW_DISABLED;
import static basicapp.front.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import basicapp.back.business.announcement.model.Announcement;
import basicapp.back.business.announcement.predicate.AnnouncementPredicates;
import basicapp.back.business.announcement.search.AnnouncementSort;
import basicapp.back.business.announcement.service.controller.IAnnouncementControllerService;
import basicapp.back.util.binding.Bindings;
import basicapp.front.announcement.model.AnnouncementDataProvider;
import basicapp.front.announcement.popup.AnnouncementPopup;
import basicapp.front.announcement.renderer.AnnouncementEnabledRenderer;
import basicapp.front.announcement.template.AnnouncementTemplate;
import basicapp.front.common.component.AnnouncementMessagePanel;
import basicapp.front.common.renderer.ActionRenderers;
import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.bootstrap.modal.OneParameterModalOpenAjaxAction;
import igloo.wicket.action.IOneParameterAjaxAction;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.model.Detachables;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public class AnnouncementListPage extends AnnouncementTemplate {

  private static final long serialVersionUID = -4746355629579854697L;

  private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementListPage.class);

  @SpringBean private IAnnouncementControllerService announcementControllerService;

  @SpringBean private IPropertyService propertyService;

  public static IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(AnnouncementListPage.class);
  }

  private final AnnouncementDataProvider dataProvider = new AnnouncementDataProvider();

  public AnnouncementListPage(PageParameters parameters) {
    super(parameters);

    AnnouncementPopup popup = new AnnouncementPopup("popup");
    add(popup);

    EnclosureContainer headerElementsSection = new EnclosureContainer("headerElementsSection");
    add(headerElementsSection.anyChildVisible());

    headerElementsSection.add(
        new EnclosureContainer("actionsContainer")
            .anyChildVisible()
            .add(
                new BlankLink("add")
                    .add(
                        new AjaxModalOpenBehavior(popup, MouseEvent.CLICK) {
                          private static final long serialVersionUID = 1L;

                          @Override
                          protected void onShow(AjaxRequestTarget target) {
                            popup.setUpAdd(new Announcement());
                          }
                        })));

    DecoratedCoreDataTablePanel<?, ?> results =
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addBootstrapBadgeColumn(
                Model.of(), Bindings.announcement(), AnnouncementEnabledRenderer.get())
            .badgePill()
            .hideLabel()
            .withClass("cell-w-60 text-center")
            .addLabelColumn(
                new ResourceModel("business.announcement.id"), Bindings.announcement().id())
            .withSort(AnnouncementSort.ID, SortIconStyle.NUMERIC, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-60")
            .addLabelColumn(
                new ResourceModel("business.announcement.type"),
                Bindings.announcement().type(),
                EnumRenderer.get())
            .withClass("cell-w-100")
            .addColumn(
                new AbstractCoreColumn<Announcement, AnnouncementSort>(
                    new ResourceModel("business.announcement.message")) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void populateItem(
                      Item<ICellPopulator<Announcement>> cellItem,
                      String componentId,
                      IModel<Announcement> rowModel) {
                    cellItem.add(new AnnouncementMessagePanel(componentId, rowModel));
                  }
                })
            .withClass("cell-w-400")
            .addLabelColumn(
                new ResourceModel("business.announcement.publication.startDateTime"),
                Bindings.announcement().publication().startDateTime())
            .withSort(
                AnnouncementSort.PUBLICATION_START_DATE_TIME,
                SortIconStyle.DEFAULT,
                CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-120")
            .addLabelColumn(
                new ResourceModel("business.announcement.publication.endDateTime"),
                Bindings.announcement().publication().endDateTime())
            .withClass("cell-w-120")
            .addActionColumn()
            .addAction(
                ActionRenderers.edit(),
                new OneParameterModalOpenAjaxAction<IModel<Announcement>>(popup) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  protected void onShow(
                      AjaxRequestTarget target, IModel<Announcement> announcementModel) {
                    super.onShow(target, announcementModel);
                    popup.setUpEdit(announcementModel.getObject());
                  }
                })
            .addConfirmAction(ActionRenderers.delete())
            .title(new ResourceModel("common.action.delete"))
            .content(new ResourceModel("common.action.confirm.content"))
            .confirm()
            .onClick(
                new IOneParameterAjaxAction<>() {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void execute(AjaxRequestTarget target, IModel<Announcement> parameter) {
                    try {
                      announcementControllerService.deleteAnnouncement(parameter.getObject());
                      Session.get().success(getString("common.success"));
                      throw new RestartResponseException(getPage());
                    } catch (RestartResponseException e) {
                      throw e;
                    } catch (Exception e) {
                      LOGGER.error("Error when deleting an announcement.", e);
                      Session.get().error(getString("common.error.unexpected"));
                      FeedbackUtils.refreshFeedback(target, getPage());
                    }
                  }
                })
            .withClassOnElements(BTN_TABLE_ROW_ACTION)
            .end()
            .withClass("cell-w-actions-2x cell-w-fit")
            .rows()
            .withClass(
                itemModel ->
                    Condition.predicate(itemModel, AnnouncementPredicates.disabled())
                        .then(TABLE_ROW_DISABLED)
                        .otherwise(""))
            .end()
            .bootstrapCard()
            .count("announcement.common.count")
            .ajaxPagers()
            .build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));

    add(results);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(dataProvider);
  }
}
