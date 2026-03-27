package igloo.console.maintenance.search.component;

import com.google.common.primitives.Longs;
import igloo.bootstrap.modal.WorkInProgressPopup;
import igloo.console.common.form.JavaClassDropDownSingleChoice;
import igloo.console.maintenance.search.page.ConsoleMaintenanceSearchPage;
import igloo.wicket.feedback.FeedbackUtils;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.commons.util.exception.IllegalSwitchValueException;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleMaintenanceSearchReindexPartialPanel extends Panel {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ConsoleMaintenanceSearchReindexPartialPanel.class);

  @SpringBean private IHibernateSearchService hibernateSearchService;

  private enum ReindexPartialType {
    UPDATE,
    DELETE;
  }

  public ConsoleMaintenanceSearchReindexPartialPanel(
      String id, IModel<List<Class<?>>> entitiesClassModel) {
    super(id);

    IModel<Class<?>> entityClassModel = Model.of();
    IModel<ReindexPartialType> typeModel = Model.of();
    IModel<String> idsModel = new Model<>();

    WorkInProgressPopup loadingPopup =
        new WorkInProgressPopup("loadingPopup", new ResourceModel("common.action.loading"));
    add(loadingPopup);

    Form<?> form = new Form<>("form");

    add(
        form.add(
            new EnumDropDownSingleChoice<>(
                    "type",
                    typeModel,
                    ReindexPartialType.class,
                    new EnumChoiceRenderer<ReindexPartialType>() {
                      @Override
                      protected String resourceKey(ReindexPartialType object) {
                        return "console.maintenance.search.reindex.partial.form.type.%s"
                            .formatted(object.name());
                      }
                    })
                .setRequired(true)
                .setLabel(new ResourceModel("console.maintenance.search.reindex.partial.form.type"))
                .add(new LabelPlaceholderBehavior()),
            new JavaClassDropDownSingleChoice("entity", entityClassModel, entitiesClassModel)
                .setRequired(true)
                .setLabel(
                    new ResourceModel("console.maintenance.search.reindex.partial.form.entity"))
                .add(new LabelPlaceholderBehavior()),
            new TextArea<>("ids", idsModel)
                .setRequired(true)
                .setLabel(new ResourceModel("console.maintenance.search.reindex.partial.form.ids"))
                .add(new LabelPlaceholderBehavior()),
            new AjaxButton("reindex") {
              @Override
              public void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);
                loadingPopup.updateAjaxAttributes(attributes);
              }

              @Override
              protected void onSubmit(AjaxRequestTarget target) {
                try {
                  LOGGER.warn("Indexing - Start");

                  ReindexPartialType type = typeModel.getObject();

                  @SuppressWarnings("unchecked")
                  Class<GenericEntity<Long, ?>> entityClass =
                      (Class<GenericEntity<Long, ?>>) entityClassModel.getObject();

                  Set<Long> entityIds =
                      Optional.ofNullable(idsModel.getObject())
                          .orElse("")
                          .lines()
                          .filter(Predicates2.hasText())
                          .map(String::strip)
                          .map(Longs::tryParse)
                          .filter(Objects::nonNull)
                          .collect(Collectors.toCollection(LinkedHashSet::new));

                  switch (type) {
                    case UPDATE ->
                        entityIds.forEach(
                            entityId ->
                                hibernateSearchService.reindexEntity(entityClass, entityId));
                    case DELETE ->
                        entityIds.forEach(
                            entityId -> hibernateSearchService.deleteEntity(entityClass, entityId));
                    default -> new IllegalSwitchValueException(type);
                  }

                  LOGGER.warn("Indexing - Done");
                  Session.get().success(getString("common.success"));
                } catch (Exception e) {
                  LOGGER.error("Error while partial reindexing.", e);
                  Session.get().error(getString("common.error.unexpected"));
                }

                throw ConsoleMaintenanceSearchPage.linkDescriptor().newRestartResponseException();
              }

              @Override
              protected void onError(AjaxRequestTarget target) {
                FeedbackUtils.refreshFeedback(target, getPage());
              }
            }));
  }
}
