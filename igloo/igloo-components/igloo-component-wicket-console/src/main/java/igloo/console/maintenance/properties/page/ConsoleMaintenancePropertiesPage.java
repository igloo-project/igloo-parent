package igloo.console.maintenance.properties.page;

import static org.iglooproject.spring.property.SpringPropertyIds.PROPERTIES_HIDDEN;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import igloo.console.maintenance.template.ConsoleMaintenanceTemplate;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.PlaceholderContainer;
import igloo.wicket.condition.Condition;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
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
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.spring.property.model.PropertyId;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.PropertySourceLogger;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.javatuples.KeyValue;
import org.wicketstuff.wiquery.core.events.KeyboardEvent;

public class ConsoleMaintenancePropertiesPage extends ConsoleMaintenanceTemplate {

  private static final long serialVersionUID = -6149952103369498125L;

  private static final String ENVIRONEMENT_PREFIX = "environment.";

  @SpringBean private IPropertyRegistry propertyRegistry;

  @SpringBean private IPropertyService propertyService;

  @SpringBean private PropertySourceLogger propertySourceLogger;

  public ConsoleMaintenancePropertiesPage(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(new ResourceModel("console.maintenance.properties")));

    IModel<String> filterModel = new Model<>();

    WebMarkupContainer propertiesContainer = new WebMarkupContainer("propertiesContainer");

    add(
        new TextField<>("filter", filterModel)
            .setLabel(new ResourceModel("console.maintenance.properties.filter"))
            .add(new LabelPlaceholderBehavior())
            .add(
                new AjaxFormComponentUpdatingBehavior(KeyboardEvent.KEYUP.getEventLabel()) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  protected void onUpdate(AjaxRequestTarget target) {
                    target.add(propertiesContainer);
                  }
                }));

    add(propertiesContainer.setOutputMarkupId(true));

    IModel<List<PropertyId<?>>> propertyIdsModel =
        new LoadableDetachableModel<List<PropertyId<?>>>() {
          private static final long serialVersionUID = 1L;

          @Override
          protected List<PropertyId<?>> load() {
            return propertyRegistry.listRegistered().stream()
                .filter(PropertyId.class::isInstance)
                .map(p -> (PropertyId<?>) p)
                .filter(Predicates2.compose(Predicates2.hasText(), PropertyId::getKey))
                .filter(
                    Predicates2.compose(
                        getFilterPredicate(filterModel.getObject()), PropertyId::getKey))
                .sorted(Ordering.natural().onResultOf(PropertyId::getKey))
                .collect(ImmutableList.toImmutableList());
          }
        };

    ListView<PropertyId<?>> propertyIdsListView =
        new ListView<PropertyId<?>>("propertyIds", propertyIdsModel) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void populateItem(ListItem<PropertyId<?>> item) {
            IModel<PropertyId<?>> propertyIdModel = item.getModel();
            IModel<Object> propertyIdValueModel =
                new LoadableDetachableModel<Object>() {
                  private static final long serialVersionUID = 1L;

                  @Override
                  protected Object load() {
                    return propertyService.get(propertyIdModel.getObject());
                  }
                };
            item.setVisible(true);
            item.add(
                new CoreLabel("name", propertyIdModel.map(PropertyId::getKey)),
                new CoreLabel(
                    "value",
                    Condition.contains(
                                Model.ofList(propertyService.get(PROPERTIES_HIDDEN)),
                                propertyIdModel.map(PropertyId::getKey))
                            .applies()
                        ? new ResourceModel("console.maintenance.properties.hiddenProperty")
                        : propertyIdValueModel));
          }
        };

    propertiesContainer.add(
        propertyIdsListView.add(Condition.collectionModelNotEmpty(propertyIdsModel).thenShow()),
        new PlaceholderContainer("emptyListPropertyIds")
            .condition(Condition.componentVisible(propertyIdsListView)));

    IModel<List<KeyValue<String, Object>>> propertiesModel =
        new LoadableDetachableModel<List<KeyValue<String, Object>>>() {
          private static final long serialVersionUID = 1L;

          @Override
          protected List<KeyValue<String, Object>> load() {
            List<KeyValue<String, Object>> properties =
                propertySourceLogger.listProperties().entrySet().stream()
                    .map(e -> KeyValue.with((String) e.getKey(), e.getValue()))
                    .filter(Predicates2.compose(Predicates2.hasText(), KeyValue::getKey))
                    .filter(
                        Predicates2.compose(
                            getFilterPredicate(filterModel.getObject()), KeyValue::getKey))
                    .sorted(Ordering.natural().onResultOf(KeyValue::getKey))
                    .collect(ImmutableList.toImmutableList());

            List<String> propertyKeys =
                properties.stream()
                    .map(KeyValue::getKey)
                    .sorted()
                    .collect(ImmutableList.toImmutableList());

            List<String> propertyIdKeys =
                propertyIdsModel.getObject().stream()
                    .map(PropertyId::getKey)
                    .sorted()
                    .collect(ImmutableList.toImmutableList());

            List<String> propertyKeysExclude =
                propertyKeys.stream()
                    .filter(k -> k.startsWith(ENVIRONEMENT_PREFIX))
                    .filter(
                        k ->
                            propertyKeys.contains(
                                org.apache.commons.lang3.StringUtils.removeStart(
                                    k, ENVIRONEMENT_PREFIX)))
                    .sorted()
                    .collect(ImmutableList.toImmutableList());

            return properties.stream()
                .filter(e -> !propertyIdKeys.contains(e.getKey()))
                .filter(e -> !propertyKeysExclude.contains(e.getKey()))
                .sorted(Ordering.natural().onResultOf(KeyValue::getKey))
                .collect(ImmutableList.toImmutableList());
          }
        };

    ListView<KeyValue<String, Object>> propertiesListView =
        new ListView<KeyValue<String, Object>>("properties", propertiesModel) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void populateItem(ListItem<KeyValue<String, Object>> item) {
            item.add(
                new CoreLabel("name", item.getModel().map(KeyValue::getKey)),
                new CoreLabel(
                    "value",
                    Condition.contains(
                                Model.ofList(propertyService.get(PROPERTIES_HIDDEN)),
                                item.getModel().map(KeyValue::getKey))
                            .applies()
                        ? new ResourceModel("console.maintenance.properties.hiddenProperty")
                        : item.getModel().map(KeyValue::getValue)));
          }
        };

    propertiesContainer.add(
        propertiesListView.add(Condition.collectionModelNotEmpty(propertiesModel).thenShow()),
        new PlaceholderContainer("emptyListProperties")
            .condition(Condition.componentVisible(propertiesListView)));
  }

  private Predicate<String> getFilterPredicate(String filter) {
    Predicate<String> filterPredicate = Predicates2.alwaysTrue();
    if (StringUtils.hasText(filter)) {
      for (String split : filter.split("\\s+")) {
        filterPredicate =
            filterPredicate.and(Predicates2.contains(Pattern.compile("(?i)^.*" + split + ".*$")));
      }
    }
    ;
    return filterPredicate;
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return ConsoleMaintenancePropertiesPage.class;
  }
}
