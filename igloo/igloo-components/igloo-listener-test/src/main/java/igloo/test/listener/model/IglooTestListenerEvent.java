package igloo.test.listener.model;

import org.springframework.context.ApplicationEvent;

public final class IglooTestListenerEvent extends ApplicationEvent {
  private static final long serialVersionUID = 1L;

  public IglooTestListenerEvent(String value) {
    super(value);
  }

  @Override
  public String getSource() {
    return (String) super.getSource();
  }
}
