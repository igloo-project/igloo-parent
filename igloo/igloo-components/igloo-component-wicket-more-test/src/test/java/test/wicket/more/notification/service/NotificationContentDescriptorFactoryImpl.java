package test.wicket.more.notification.service;

import igloo.wicket.component.CoreLabel;
import java.util.Locale;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.pages.BrowserInfoPage;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import org.iglooproject.wicket.more.notification.service.AbstractNotificationContentDescriptorFactory;
import org.iglooproject.wicket.more.notification.service.IWicketContextProvider;

public class NotificationContentDescriptorFactoryImpl
    extends AbstractNotificationContentDescriptorFactory
    implements INotificationContentDescriptorFactory {

  public NotificationContentDescriptorFactoryImpl(IWicketContextProvider wicketContextProvider) {
    super(wicketContextProvider);
  }

  @Override
  public INotificationContentDescriptor simpleContent(String content) {
    return new AbstractSimpleWicketNotificationDescriptor("notification.panel.simpleContent") {
      @Override
      public Page createPage(Locale locale) {
        return new BrowserInfoPage();
      }

      @Override
      public Class<? extends Component> getComponentClass() {
        return CoreLabel.class;
      }
    };
  }
}
