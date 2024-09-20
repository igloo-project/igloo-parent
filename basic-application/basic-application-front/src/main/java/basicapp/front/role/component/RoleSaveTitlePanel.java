package basicapp.front.role.component;

import basicapp.back.business.role.model.Role;
import basicapp.back.util.binding.Bindings;
import basicapp.front.common.validator.RoleTitleUnicityValidator;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

public class RoleSaveTitlePanel extends Panel {

  private static final long serialVersionUID = 1L;

  public RoleSaveTitlePanel(String id, IModel<Role> roleModel) {
    super(id);

    add(
        new TextField<>("title", BindingModel.of(roleModel, Bindings.role().title()))
            .setRequired(true)
            .setLabel(new ResourceModel("business.role.title"))
            .add(new RoleTitleUnicityValidator(roleModel)));
  }
}
