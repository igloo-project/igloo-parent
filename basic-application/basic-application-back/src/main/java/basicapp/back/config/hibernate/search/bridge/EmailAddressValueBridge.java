package basicapp.back.config.hibernate.search.bridge;

import basicapp.back.business.common.model.EmailAddress;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class EmailAddressValueBridge implements ValueBridge<EmailAddress, String> {

  @Override
  public String toIndexedValue(EmailAddress value, ValueBridgeToIndexedValueContext context) {
    if (value == null) {
      return null;
    }
    return value.getValue();
  }
}
