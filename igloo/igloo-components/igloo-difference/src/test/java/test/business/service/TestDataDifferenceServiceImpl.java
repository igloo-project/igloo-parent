package test.business.service;

import igloo.difference.AbstractConfiguredDifferenceServiceImpl;
import igloo.difference.model.DifferenceFields;
import test.business.model.TestData;

public class TestDataDifferenceServiceImpl extends AbstractConfiguredDifferenceServiceImpl<TestData>
    implements ITestDataDifferenceService {

  public TestDataDifferenceServiceImpl(DifferenceFields fields) {
    super(fields);
  }
}
