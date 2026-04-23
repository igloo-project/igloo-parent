package igloo.console.maintenance.search.component;

import igloo.bootstrap.confirm.AjaxConfirmLink;
import igloo.bootstrap.modal.WorkInProgressPopup;
import igloo.console.common.form.JavaClassChoiceRenderer;
import igloo.console.maintenance.search.page.ConsoleMaintenanceSearchPage;
import igloo.wicket.action.IAjaxAction;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IDetachableFactory;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.form.ListMultipleChoice;
import igloo.wicket.model.CollectionCopyModel;
import igloo.wicket.model.CountMessageModel;
import igloo.wicket.model.ICollectionModel;
import igloo.wicket.model.ISequenceProvider;
import igloo.wicket.renderer.Renderer;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.ThrottlingSettings;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.markup.html.form.IndependentNestedForm;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.CoreDataTable;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.IDataTableFactory;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleMaintenanceSearchReindexFullPanel extends Panel {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ConsoleMaintenanceSearchReindexFullPanel.class);

  @SpringBean private IHibernateSearchService hibernateSearchService;

  public ConsoleMaintenanceSearchReindexFullPanel(
      String id, IModel<List<Class<?>>> entitiesClassModel) {
    super(id);
    setOutputMarkupId(true);

    ICollectionModel<Class<?>, List<Class<?>>> choicesModel =
        CollectionCopyModel.serializable(Suppliers2.arrayList());

    IModel<String> choicesSearchTermModel = Model.of();

    ICollectionModel<Class<?>, List<Class<?>>> selectionModel =
        CollectionCopyModel.serializable(Suppliers2.arrayList());

    IModel<List<Class<?>>> choicesChoicesModel =
        LoadableDetachableModel.of(
            () -> {
              var valuesStream =
                  entitiesClassModel.getObject().stream()
                      .filter(c -> !selectionModel.getObject().contains(c));

              String term = choicesSearchTermModel.getObject();
              if (StringUtils.hasText(term)) {
                valuesStream =
                    valuesStream.filter(
                        c ->
                            c.getSimpleName()
                                .toLowerCase()
                                .contains(choicesSearchTermModel.getObject().toLowerCase()));
              }

              return valuesStream.toList();
            });

    WorkInProgressPopup loadingPopup =
        new WorkInProgressPopup("loadingPopup", new ResourceModel("common.action.loading"));
    add(loadingPopup);

    ListMultipleChoice<Class<?>> entitiesFormComponent =
        new ListMultipleChoice<>(
            "choices", choicesModel, choicesChoicesModel, JavaClassChoiceRenderer.get());

    AjaxButton add =
        new AjaxButton("add") {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            onSelectionSubmit(target, choicesModel, choicesChoicesModel, selectionModel);
          }

          @Override
          protected void onError(AjaxRequestTarget target) {
            onSelectionError(target);
          }
        };

    DecoratedCoreDataTablePanel<Class<?>, ISort<?>> selection =
        DataTableBuilder.start(selectionModel)
            .addLabelColumn(
                new CountMessageModel(
                    "console.maintenance.search.reindex.full.selection.count",
                    selectionModel::size),
                Renderer.stringValue().onResultOf(Class::getSimpleName))
            .withClass("cell-w-300")
            .addColumn(
                new AbstractCoreColumn<>(Model.of()) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public Component getHeader(String componentId) {
                    return new SelectionActionHeaderFragment(
                        componentId, choicesModel, choicesChoicesModel, selectionModel);
                  }

                  @Override
                  public void populateItem(
                      Item<ICellPopulator<Class<?>>> cellItem,
                      String componentId,
                      IModel<Class<?>> rowModel) {
                    cellItem.add(
                        new SelectionActionFragment(
                            componentId,
                            rowModel,
                            choicesModel,
                            choicesChoicesModel,
                            selectionModel));
                  }
                })
            .withClass("cell-w-fit")
            .withFactory(
                new IDataTableFactory<Class<?>, ISort<?>>() {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public CoreDataTable<Class<?>, ISort<?>> create(
                      String id,
                      Map<IColumn<Class<?>, ISort<?>>, Condition> columns,
                      ISequenceProvider<Class<?>> sequenceProvider,
                      List<
                              IDetachableFactory<
                                  ? super IModel<? extends Class<?>>, ? extends Behavior>>
                          rowsBehaviorFactories,
                      List<Behavior> tableBehaviors,
                      long rowsPerPage) {
                    CoreDataTable<Class<?>, ISort<?>> dataTable =
                        new CoreDataTable<>(
                            id,
                            columns,
                            sequenceProvider,
                            rowsBehaviorFactories,
                            tableBehaviors,
                            rowsPerPage);
                    dataTable.add(new ClassAttributeAppender("table-sm"));
                    return dataTable;
                  }
                })
            .decorate()
            .ajaxPager(AddInPlacement.FOOTER_RIGHT)
            .build("selection", 20);

    add(
        new AjaxLink<>("all") {
          @Override
          public void onClick(AjaxRequestTarget target) {
            choicesSearchTermModel.setObject(null);

            choicesModel.clear();
            entitiesClassModel.getObject().stream().forEach(choicesModel::add);
            selectionModel.clear();

            onSelectionSubmit(target, choicesModel, choicesChoicesModel, selectionModel);
          }
        });

    add(
        new Form<>("form")
            .add(
                new WebMarkupContainer("choicesContainer")
                    .add(
                        new IndependentNestedForm<>("search")
                            .add(
                                new TextField<>("term", choicesSearchTermModel)
                                    .setLabel(
                                        new ResourceModel(
                                            "console.maintenance.search.reindex.full.choices.search.term"))
                                    .add(new LabelPlaceholderBehavior())
                                    .add(
                                        new AjaxFormComponentUpdatingBehavior("input") {
                                          @Override
                                          protected void onUpdate(AjaxRequestTarget target) {
                                            target.add(entitiesFormComponent);
                                          }

                                          @Override
                                          protected void updateAjaxAttributes(
                                              AjaxRequestAttributes attributes) {
                                            super.updateAjaxAttributes(attributes);
                                            attributes.setThrottlingSettings(
                                                new ThrottlingSettings(
                                                    Duration.ofMillis(200), true));
                                          }
                                        })),
                        entitiesFormComponent
                            .setLabel(
                                new ResourceModel(
                                    "console.maintenance.search.reindex.full.choices.entities"))
                            .setRequired(true)
                            .add(new LabelPlaceholderBehavior())
                            .add(
                                new AjaxFormSubmitBehavior("dblclick") {
                                  @Override
                                  protected void onSubmit(AjaxRequestTarget target) {
                                    target.focusComponent(null);
                                    onSelectionSubmit(
                                        target, choicesModel, choicesChoicesModel, selectionModel);
                                  }

                                  @Override
                                  protected void onError(AjaxRequestTarget target) {
                                    onSelectionError(target);
                                  }
                                })
                            .setOutputMarkupId(true)),
                add,
                new WebMarkupContainer("selectionContainer")
                    .add(
                        selection,
                        AjaxConfirmLink.<Void>build()
                            .title(
                                new ResourceModel(
                                    "console.maintenance.search.reindex.common.action.reindex"))
                            .content(new ResourceModel("common.action.confirm.content"))
                            .confirm()
                            .onClick(
                                new IAjaxAction() {
                                  private static final long serialVersionUID = 1L;

                                  @Override
                                  public void execute(AjaxRequestTarget target) {
                                    try {
                                      Collection<Class<?>> selection = selectionModel.getObject();

                                      if (selection.isEmpty()) {
                                        Session.get()
                                            .error(
                                                getString(
                                                    "console.maintenance.search.reindex.full.selection.error.empty"));
                                        throw ConsoleMaintenanceSearchPage.linkDescriptor()
                                            .newRestartResponseException();
                                      }

                                      LOGGER.warn("Indexing - Start");
                                      hibernateSearchService.reindexClasses(selection);
                                      LOGGER.warn("Indexing - Done");

                                      Session.get().success(getString("common.success"));
                                    } catch (RestartResponseException e) {
                                      throw e;
                                    } catch (Exception e) {
                                      LOGGER.error("Error while mass indexing.", e);
                                      Session.get().error(getString("common.error.unexpected"));
                                    }

                                    throw ConsoleMaintenanceSearchPage.linkDescriptor()
                                        .newRestartResponseException();
                                  }

                                  @Override
                                  public void updateAjaxAttributes(
                                      AjaxRequestAttributes attributes) {
                                    IAjaxAction.super.updateAjaxAttributes(attributes);
                                    loadingPopup.updateAjaxAttributes(attributes);
                                  }
                                })
                            .create("reindex"))));
  }

  private void onSelectionSubmit(
      AjaxRequestTarget target,
      ICollectionModel<Class<?>, List<Class<?>>> choicesModel,
      IModel<List<Class<?>>> choicesChoicesModel,
      ICollectionModel<Class<?>, List<Class<?>>> selectionModel) {
    choicesModel.getObject().stream().forEach(selectionModel::add);
    choicesModel.clear();
    onSelectionAction(target, choicesModel, choicesChoicesModel);
  }

  private void onSelectionError(AjaxRequestTarget target) {
    FeedbackUtils.refreshFeedback(target, getPage());
  }

  private void onSelectionAction(
      AjaxRequestTarget target,
      ICollectionModel<Class<?>, List<Class<?>>> choicesModel,
      IModel<List<Class<?>>> choicesChoicesModel) {
    choicesModel.detach();
    choicesChoicesModel.detach();
    target.add(ConsoleMaintenanceSearchReindexFullPanel.this);
  }

  public class SelectionActionHeaderFragment extends Fragment {

    private static final long serialVersionUID = 1L;

    public SelectionActionHeaderFragment(
        String id,
        ICollectionModel<Class<?>, List<Class<?>>> choicesModel,
        IModel<List<Class<?>>> choicesChoicesModel,
        ICollectionModel<Class<?>, List<Class<?>>> selectionModel) {
      super(id, "selectionActionHeaderFragment", ConsoleMaintenanceSearchReindexFullPanel.this);

      add(
          AjaxConfirmLink.build()
              .title(new ResourceModel("common.action.remove.all"))
              .content(new ResourceModel("common.action.confirm.content"))
              .confirm()
              .onClick(
                  new IAjaxAction() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void execute(AjaxRequestTarget target) {
                      selectionModel.clear();
                      onSelectionAction(target, choicesModel, choicesChoicesModel);
                    }
                  })
              .create("removeAll")
              .add(Condition.isNotEmpty(selectionModel).thenShow()));
    }
  }

  public class SelectionActionFragment extends Fragment {

    private static final long serialVersionUID = 1L;

    public SelectionActionFragment(
        String id,
        IModel<Class<?>> rowModel,
        ICollectionModel<Class<?>, List<Class<?>>> choicesModel,
        IModel<List<Class<?>>> choicesChoicesModel,
        ICollectionModel<Class<?>, List<Class<?>>> selectionModel) {
      super(id, "selectionActionFragment", ConsoleMaintenanceSearchReindexFullPanel.this);

      add(
          new AjaxLink<>("remove", rowModel) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
              selectionModel.remove(rowModel.getObject());
              onSelectionAction(target, choicesModel, choicesChoicesModel);
            }
          });
    }
  }
}
