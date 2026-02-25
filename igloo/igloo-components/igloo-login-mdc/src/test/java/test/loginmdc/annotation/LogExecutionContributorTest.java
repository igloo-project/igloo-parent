package test.loginmdc.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import igloo.loginmdc.annotation.LogExecution;
import igloo.loginmdc.annotation.LogExecution.LogLevel;
import igloo.loginmdc.annotation.LogExecutionContributor;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

@SpringBootTest(
    classes = LogExecutionContributorTest.Configuration.class,
    properties = "logging.config=classpath:log4j2-test.properties")
class LogExecutionContributorTest {

  @Autowired private IMyServiceTest myServiceTest;

  private final ByteArrayOutputStream silentOutput = new ByteArrayOutputStream();
  private final PrintStream originalOutput = System.out;

  @BeforeEach
  void muteOuput() {
    System.setOut(new PrintStream(silentOutput));
  }

  @AfterEach
  void unmuteOuput() {
    System.setOut(originalOutput);
  }

  @Test
  void testLog() {
    myServiceTest.methodLog();

    assertThat(silentOutput.toString())
        .contains("INFO - Entering method: MyServiceTestImpl#methodLog()")
        .contains("INFO - Exiting method: MyServiceTestImpl#methodLog()");
  }

  @Test
  void testLogArgs() {
    myServiceTest.methodLogArgs("", 0);

    assertThat(silentOutput.toString())
        .contains("INFO - Entering method: MyServiceTestImpl#methodLogArgs(String, Integer)")
        .contains("INFO - Exiting method: MyServiceTestImpl#methodLogArgs(String, Integer)");
  }

  @Test
  void testLogLevel() {
    myServiceTest.methodLogLevel();

    assertThat(silentOutput.toString())
        .contains("WARN - Entering method: MyServiceTestImpl#methodLogLevel()")
        .contains("WARN - Exiting method: MyServiceTestImpl#methodLogLevel()");
  }

  @Test
  void testLogWithAdditionalMessages() {
    myServiceTest.methodLogWithAdditionalMessages();

    assertThat(silentOutput.toString())
        .contains("INFO - Entering method: MyServiceTestImpl#methodLogWithAdditionalMessages()")
        .contains("INFO - Additional message before execution")
        .contains("INFO - Exiting method: MyServiceTestImpl#methodLogWithAdditionalMessages()")
        .contains("INFO - Additional message after execution");
  }

  @Test
  void testLogWithAdditionalMessagesLevel() {
    myServiceTest.methodLogWithAdditionalMessagesLevel();

    assertThat(silentOutput.toString())
        .contains(
            "DEBUG - Entering method: MyServiceTestImpl#methodLogWithAdditionalMessagesLevel()")
        .contains("DEBUG - Additional message before execution")
        .contains(
            "DEBUG - Exiting method: MyServiceTestImpl#methodLogWithAdditionalMessagesLevel()")
        .contains("WARN - Additional message after execution");
  }

  @Test
  void testLogExceptionWithStackTrace() {
    assertThatThrownBy(() -> myServiceTest.methodLogExceptionWithStackTrace())
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Fake error");

    assertThat(silentOutput.toString())
        .contains("INFO - Entering method: MyServiceTestImpl#methodLogExceptionWithStackTrace()")
        .contains(
            "INFO - Exiting method MyServiceTestImpl#methodLogExceptionWithStackTrace() with exception = RuntimeException : Fake error")
        .contains("INFO - Additional message after throwing")
        .contains("ERROR - Fake error")
        .contains("java.lang.RuntimeException: Fake error");
  }

  @org.springframework.context.annotation.Configuration
  @EnableAspectJAutoProxy
  static class Configuration {

    @Bean
    public LogExecutionContributor logExecutionContributor() {
      return new LogExecutionContributor();
    }

    @Bean
    public IMyServiceTest myServiceTest() {
      return new MyServiceTestImpl();
    }
  }

  static interface IMyServiceTest {

    void methodLog();

    void methodLogArgs(String arg1, Integer arg2);

    void methodLogLevel();

    void methodLogWithAdditionalMessages();

    void methodLogWithAdditionalMessagesLevel();

    void methodLogExceptionWithStackTrace();
  }

  @Service
  static class MyServiceTestImpl implements IMyServiceTest {

    @LogExecution
    @Override
    public void methodLog() {
      // Nothing to do
    }

    @LogExecution
    @Override
    public void methodLogArgs(String arg1, Integer arg2) {
      // Nothing to do
    }

    @LogExecution(level = Level.WARN)
    @Override
    public void methodLogLevel() {
      // Nothing to do
    }

    @LogExecution(
        beforeAdditionalLogMessage = "Additional message before execution",
        afterReturningAdditionalLogMessage = "Additional message after execution")
    @Override
    public void methodLogWithAdditionalMessages() {
      // Nothing to do
    }

    @LogExecution(
        level = Level.DEBUG,
        beforeAdditionalLogMessage = "Additional message before execution",
        afterReturningAdditionalLogLevel = LogLevel.WARN,
        afterReturningAdditionalLogMessage = "Additional message after execution")
    @Override
    public void methodLogWithAdditionalMessagesLevel() {
      // Nothing to do
    }

    @LogExecution(
        afterThrowingAdditionalLogMessage = "Additional message after throwing",
        afterThrowingLogStackTrace = true,
        afterThrowingLogStackTraceLevel = LogLevel.ERROR)
    @Override
    public void methodLogExceptionWithStackTrace() {
      throw new RuntimeException("Fake error");
    }
  }
}
