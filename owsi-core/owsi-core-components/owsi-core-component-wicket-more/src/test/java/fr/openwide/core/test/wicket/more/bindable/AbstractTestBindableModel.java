package fr.openwide.core.test.wicket.more.bindable;

import org.apache.wicket.model.IModel;
import org.junit.Rule;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import fr.openwide.core.test.wicket.more.AbstractWicketMoreTestCase;

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

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();

	@Spy
	protected RootValue rootValue = new RootValue();

	@Mock(answer = Answers.RETURNS_SMART_NULLS)
	protected IModel<RootValue> rootModel;

}
