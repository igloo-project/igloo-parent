package org.iglooproject.test.wicket.core.junit;

import org.apache.wicket.util.tester.WicketTester;

public interface IWicketTestCase<T extends WicketTester> {

  void setWicketTester(T tester);
}
