package igloo.console.maintenance.data.page;

import igloo.bootstrap.confirm.ConfirmLink;
import igloo.console.maintenance.template.ConsoleMaintenanceTemplate;
import igloo.wicket.action.IOneParameterAction;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.PlaceholderContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.BindingModel;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.upgrade.model.DataUpgradeRecord;
import org.iglooproject.jpa.more.business.upgrade.model.DataUpgradeRecordBinding;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.iglooproject.jpa.more.business.upgrade.service.IAbstractDataUpgradeService;
import org.iglooproject.jpa.more.business.upgrade.service.IDataUpgradeRecordService;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleMaintenanceDataPage extends ConsoleMaintenanceTemplate {

  private static final long serialVersionUID = -6149952103369498125L;

  private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceDataPage.class);

  private static final DataUpgradeRecordBinding dataUpgradeRecordBinding =
      new DataUpgradeRecordBinding();

  @SpringBean private IPropertyService propertyService;

  @SpringBean private IDataUpgradeRecordService dataUpgradeRecordService;

  @SpringBean private IAbstractDataUpgradeService dataUpgradeService;

  public ConsoleMaintenanceDataPage(PageParameters parameters) {
    super(parameters);

    if (dataUpgradeService == null) {
      throw new RestartResponseException(getFirstMenuPage());
    }

    addBreadCrumbElement(
        new BreadCrumbElement(new ResourceModel("console.navigation.maintenance.data")));

    IModel<List<IDataUpgrade>> dataUpgrades =
        new LoadableDetachableModel<List<IDataUpgrade>>() {
          private static final long serialVersionUID = 1L;

          @Override
          protected List<IDataUpgrade> load() {
            return dataUpgradeService.listDataUpgrades();
          }
        };

    ListView<IDataUpgrade> dataUpgradeListView =
        new ListView<IDataUpgrade>("dataUpgrades", dataUpgrades) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void populateItem(ListItem<IDataUpgrade> item) {
            IModel<DataUpgradeRecord> dataUpgradeRecordModel =
                LoadableDetachableModel.of(
                    () -> dataUpgradeRecordService.getByDataUpgrade(item.getModelObject()));

            item.add(
                new CoreLabel("name", item.getModel().map(IDataUpgrade::getName)),
                new CoreLabel(
                        "executionDate",
                        BindingModel.of(
                            dataUpgradeRecordModel, dataUpgradeRecordBinding.executionDate()))
                    .showPlaceholder(),
                new BooleanIcon(
                        "autoPerform",
                        BindingModel.of(
                            dataUpgradeRecordModel, dataUpgradeRecordBinding.autoPerform()))
                    .hideIfNullOrFalse());

            Component runLink =
                ConfirmLink.<IDataUpgrade>build()
                    .title(new ResourceModel("common.action.confirm.title"))
                    .content(new ResourceModel("common.action.confirm.content"))
                    .confirm()
                    .onClick(
                        new IOneParameterAction<IModel<IDataUpgrade>>() {
                          private static final long serialVersionUID = 1L;

                          @Override
                          public void execute(IModel<IDataUpgrade> parameter) {
                            try {
                              dataUpgradeService.executeDataUpgrade(parameter.getObject());
                              Session.get().success(getString("common.success"));
                            } catch (Exception e) {
                              LOGGER.error(
                                  "Erreur lors de l'exécution de la mise à jour '"
                                      + getModelObject()
                                      + "'",
                                  e);
                              Session.get().error(getString("common.error.unexpected"));
                            }
                            setResponsePage(getPage());
                          }
                        })
                    .create("execute", item.getModel());

            item.add(
                runLink.add(
                    Condition.isTrue(
                            BindingModel.of(
                                dataUpgradeRecordModel, dataUpgradeRecordBinding.done()))
                        .thenHide()),
                new PlaceholderContainer("done").condition(Condition.componentVisible(runLink)));
          }
        };

    add(
        dataUpgradeListView.add(Condition.collectionModelNotEmpty(dataUpgrades).thenShow()),
        new PlaceholderContainer("emptyList")
            .condition(Condition.componentVisible(dataUpgradeListView)));
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return ConsoleMaintenanceDataPage.class;
  }
}
