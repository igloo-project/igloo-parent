package basicapp.front.console.notification.demo.page;

import basicapp.back.business.notification.service.INotificationService;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import basicapp.front.BasicApplicationSession;
import basicapp.front.console.notification.demo.template.ConsoleNotificationDemoTemplate;
import basicapp.front.console.notification.demo.util.ConsoleNotificationDemoEntry;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.PlaceholderContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.SequenceProviders;
import java.time.Instant;
import java.util.List;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.repeater.collection.SpecificModelCollectionView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleNotificationDemoListPage extends ConsoleNotificationDemoTemplate {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ConsoleNotificationDemoListPage.class);

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(ConsoleNotificationDemoListPage.class);
  }

  @SpringBean private INotificationService notificationService;

  public ConsoleNotificationDemoListPage(PageParameters parameters) {
    super(parameters);

    add(
        new Link<Void>("sendExample") {
          private static final long serialVersionUID = 1L;

          @Override
          public void onClick() {
            try {
              notificationService.sendExampleNotification(BasicApplicationSession.get().getUser());
              Session.get().success(getString("common.success"));
            } catch (ServiceException e) {
              LOGGER.error("Error while sending example notification", e);
              Session.get().error(getString("common.error.unexpected"));
            }
          }
        });

    var demoEntriesModel = SequenceProviders.fromItemModels(createDemoEntries());

    add(
        new SpecificModelCollectionView<
            INotificationContentDescriptor, ConsoleNotificationDemoEntry>(
            "demoEntries", demoEntriesModel) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void populateItem(SpecificModelItem item) {
            ConsoleNotificationDemoEntry entry = item.getSpecificModel();

            item.add(
                new Link<Void>("link") {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void onClick() {
                    setResponsePage(
                        new ConsoleNotificationDemoDetailPage(new PageParameters(), entry));
                  }
                }.add(new CoreLabel("label", entry.getLabelModel())));
          }
        }.add(Condition.collectionModelNotEmpty(demoEntriesModel).thenShow()),
        new PlaceholderContainer("placeholder")
            .condition(Condition.collectionModelNotEmpty(demoEntriesModel)));
  }

  private List<ConsoleNotificationDemoEntry> createDemoEntries() {
    return List.of(
        new ConsoleNotificationDemoEntry("example") {
          private static final long serialVersionUID = 1L;

          @Override
          public INotificationContentDescriptor getDescriptor() {
            User user = new User();
            user.setId(1L);
            user.setUsername("dwight.schrute");
            user.setLastName("Schrute");
            user.setFirstName("Dwight");
            user.setLastLoginDate(Instant.now());

            return descriptorService.example(user, Instant.now());
          }
        },
        new ConsoleNotificationDemoEntry("userPasswordRecoveryRequest") {
          private static final long serialVersionUID = 1L;

          @Override
          public INotificationContentDescriptor getDescriptor() {
            User user = new User();
            user.setId(1L);
            user.setUsername("dwight.schrute");
            user.getPasswordRecoveryRequest().setToken("1337");
            user.getPasswordRecoveryRequest().setCreationDate(Instant.now());
            user.getPasswordRecoveryRequest().setType(UserPasswordRecoveryRequestType.RESET);
            user.getPasswordRecoveryRequest()
                .setInitiator(UserPasswordRecoveryRequestInitiator.USER);

            return descriptorService.userPasswordRecoveryRequest(user, Instant.now());
          }
        });
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return ConsoleNotificationDemoListPage.class;
  }
}
