package basicapp.front.common.form;

import basicapp.back.business.common.model.EmailAddress;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class EmailAddressTextField extends TextField<EmailAddress> {

  private static final long serialVersionUID = 1L;

  public EmailAddressTextField(String id, IModel<EmailAddress> model) {
    super(id, model, EmailAddress.class);
  }

  @Override
  protected String[] getInputTypes() {
    return new String[] {"email"};
  }
}
