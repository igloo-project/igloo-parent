package test.wicket.more.model;

import static org.assertj.core.api.Assertions.assertThat;

import igloo.wicket.model.BindingModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.bindgen.java.lang.StringBinding;
import org.junit.jupiter.api.Test;
import test.wicket.more.AbstractWicketMoreTestCase;

public class TestSelfModel extends AbstractWicketMoreTestCase {

  /** Root-only binding model loading */
  @Test
  public void testSelfModel() {
    String string = "maChaine";
    IModel<String> stringModel = BindingModel.of(Model.of(string), new StringBinding());
    assertThat(stringModel.getObject()).isEqualTo(string);
  }
}
