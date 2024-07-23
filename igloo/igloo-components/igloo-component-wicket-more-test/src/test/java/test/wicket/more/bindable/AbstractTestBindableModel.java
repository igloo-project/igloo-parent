package test.wicket.more.bindable;

import org.apache.wicket.model.IModel;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import test.wicket.more.AbstractWicketMoreTestCase;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractTestBindableModel extends AbstractWicketMoreTestCase {

  protected static RootValueBinding rootBinding() {
    return new RootValueBinding();
  }

  protected static SimplePropertyValueBinding simplePropertyBinding() {
    return new SimplePropertyValueBinding();
  }

  protected static CollectionPropertyItemValueBinding collectionPropertyItemBinding() {
    return new CollectionPropertyItemValueBinding();
  }

  @Spy protected RootValue rootValue = new RootValue();

  @Mock(answer = Answers.RETURNS_SMART_NULLS)
  protected IModel<RootValue> rootModel;
}
