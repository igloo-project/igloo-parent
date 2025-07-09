package test;

import org.springframework.test.context.TestPropertySource;

@TestPropertySource(
    properties = "spring.jpa.properties.igloo.historylog.optimization.enabled=false")
public class TestHistoryEntityReferenceLegacy extends AbstractTestHistoryEntityReference {}
