package basicapp.front.role.component;

import basicapp.back.security.model.BasicApplicationPermissionCategoryEnum;
import basicapp.back.security.model.BasicApplicationPermissionUtils;
import basicapp.front.common.renderer.PermissionNameRenderer;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.markup.html.form.CheckGroup;
import igloo.wicket.model.Models;
import igloo.wicket.model.ReadOnlyMapModel;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.markup.repeater.map.MapItem;
import org.iglooproject.wicket.more.markup.repeater.map.MapView;
import org.iglooproject.wicket.more.rendering.EnumRenderer;

public class RoleSavePermissionsPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public RoleSavePermissionsPanel(String id, IModel<Set<String>> permissionsModel) {
    super(id);

    IModel<Map<BasicApplicationPermissionCategoryEnum, List<String>>> mapModel =
        LoadableDetachableModel.of(() -> BasicApplicationPermissionUtils.PERMISSIONS);

    add(
        new CheckGroup<>("permissionsGroup", permissionsModel, Suppliers2.<String>hashSet())
            .add(
                new MapView<>(
                    "categories",
                    ReadOnlyMapModel.of(mapModel, Models.serializableModelFactory())) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  protected void populateItem(
                      MapItem<BasicApplicationPermissionCategoryEnum, List<String>> item) {
                    IModel<BasicApplicationPermissionCategoryEnum> keyModel = item.getModel();
                    IModel<List<String>> valueModel = item.getValueModel();

                    item.add(
                        new CoreLabel(
                            "category",
                            EnumRenderer.get().render(keyModel.getObject(), getLocale())),
                        new CollectionView<>(
                            "permissions", valueModel, Models.serializableModelFactory()) {
                          private static final long serialVersionUID = 1L;

                          @Override
                          protected void populateItem(Item<String> item) {
                            item.add(
                                new Check<>("permissionCheck", item.getModel())
                                    .setLabel(
                                        PermissionNameRenderer.get().asModel(item.getModel())));
                          }
                        });
                  }
                }));
  }
}
