package igloo.console.maintenance.search.page;

import igloo.console.maintenance.search.component.ConsoleMaintenanceSearchReindexFullPanel;
import igloo.console.maintenance.search.component.ConsoleMaintenanceSearchReindexPartialPanel;
import igloo.console.maintenance.template.ConsoleMaintenanceTemplate;
import igloo.wicket.model.Detachables;
import java.util.Collections;
import java.util.List;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleMaintenanceSearchPage extends ConsoleMaintenanceTemplate {

  private static final long serialVersionUID = 2718354274888156322L;

  private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceSearchPage.class);

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(ConsoleMaintenanceSearchPage.class);
  }

  @SpringBean private IHibernateSearchService hibernateSearchService;

  private final IModel<List<Class<?>>> classesChoicesModel;

  public ConsoleMaintenanceSearchPage(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(new ResourceModel("console.navigation.maintenance.search")));

    classesChoicesModel =
        LoadableDetachableModel.of(
            () -> {
              try {
                return List.copyOf(hibernateSearchService.getIndexedRootEntities());
              } catch (ServiceException e) {
                LOGGER.error("Error while fetching indexed entities.", e);
                Session.get()
                    .error(getString("console.maintenance.search.reindex.common.error.entities"));
                return Collections.emptyList();
              }
            });

    add(
        new ConsoleMaintenanceSearchReindexFullPanel("reindexFull", classesChoicesModel),
        new ConsoleMaintenanceSearchReindexPartialPanel("reindexPartial", classesChoicesModel));
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(classesChoicesModel);
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return ConsoleMaintenanceSearchPage.class;
  }
}
