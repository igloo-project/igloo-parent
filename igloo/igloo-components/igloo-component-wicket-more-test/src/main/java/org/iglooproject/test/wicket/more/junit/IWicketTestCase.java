package org.iglooproject.test.wicket.more.junit;

import org.apache.wicket.util.tester.WicketTester;

public interface IWicketTestCase<T extends WicketTester> {
	
	void setWicketTester(T tester);

}
