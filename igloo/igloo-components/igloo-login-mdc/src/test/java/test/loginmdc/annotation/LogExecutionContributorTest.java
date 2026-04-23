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
import test.loginmdc.model.DummyUser;

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
        .contains(
            "INFO - methodeSignature:MyServiceTestImpl#methodLog() - Entering method: MyServiceTestImpl#methodLog()")
        .contains("INFO - methodeSignature:MyServiceTestImpl#methodLog() - execTime:")
        .contains("- Exiting method: MyServiceTestImpl#methodLog()");
  }

  @Test
  void testLogArgs() {
    myServiceTest.methodLogArgs("", 0);

    assertThat(silentOutput.toString())
        .contains(
            "INFO - methodeSignature:MyServiceTestImpl#methodLogArgs(String, Integer) - Entering method: MyServiceTestImpl#methodLogArgs(String, Integer)")
        .contains(
            "INFO - methodeSignature:MyServiceTestImpl#methodLogArgs(String, Integer) - execTime:")
        .contains("Exiting method: MyServiceTestImpl#methodLogArgs(String, Integer)");
  }

  @Test
  void testLogReplaceMessages() {
    myServiceTest.methodReplaceMessages("", 0);

    assertThat(silentOutput.toString())
        .contains(
            "INFO - methodeSignature:MyServiceTestImpl#methodReplaceMessages(String, Integer) - Start methodReplaceMessages")
        .contains(
            "INFO - methodeSignature:MyServiceTestImpl#methodReplaceMessages(String, Integer) - execTime:")
        .contains("- Finish methodReplaceMessages");
  }

  @Test
  void testLogArgsSpEL() {
    myServiceTest.methodLogArgsSpEL(new DummyUser(1L, "g.abidbole"), 0);

    assertThat(silentOutput.toString())
        .contains(
            "INFO - methodeSignature:MyServiceTestImpl#methodLogArgsSpEL(DummyUser, Integer) - Entering MyServiceTestImpl#methodLogArgsSpEL with user : userId = 1 username = g.abidbole")
        .contains(
            "INFO - methodeSignature:MyServiceTestImpl#methodLogArgsSpEL(DummyUser, Integer) - execTime:")
        .contains(" - Exiting method: MyServiceTestImpl#methodLogArgsSpEL(DummyUser, Integer)");
  }

  @Test
  void testLogLevel() {
    myServiceTest.methodLogLevel();

    assertThat(silentOutput.toString())
        .contains(
            "WARN - methodeSignature:MyServiceTestImpl#methodLogLevel() - Entering method: MyServiceTestImpl#methodLogLevel()")
        .contains("WARN - methodeSignature:MyServiceTestImpl#methodLogLevel() - execTime:")
        .contains(" Exiting method: MyServiceTestImpl#methodLogLevel()");
  }

  @Test
  void testLogWithAdditionalMessages() {
    myServiceTest.methodLogWithAdditionalMessages(new DummyUser(1L, "g.abidbole"));

    assertThat(silentOutput.toString())
        .contains(
            "INFO - methodeSignature:MyServiceTestImpl#methodLogWithAdditionalMessages(DummyUser) - Entering method: MyServiceTestImpl#methodLogWithAdditionalMessages(DummyUser) - userId = 1")
        .contains(
            "INFO - methodeSignature:MyServiceTestImpl#methodLogWithAdditionalMessages(DummyUser) - execTime:")
        .contains(
            "Exiting method: MyServiceTestImpl#methodLogWithAdditionalMessages(DummyUser) - userId = 1");
  }

  @Test
  void testLogWithAdditionalMessagesLevel() {
    myServiceTest.methodLogWithAdditionalMessagesLevel(new DummyUser(1L, "g.abidbole"));

    assertThat(silentOutput.toString())
        .contains(
            "DEBUG - methodeSignature:MyServiceTestImpl#methodLogWithAdditionalMessagesLevel(DummyUser) - Entering method: MyServiceTestImpl#methodLogWithAdditionalMessagesLevel(DummyUser) - userId = 1")
        .contains(
            "WARN - methodeSignature:MyServiceTestImpl#methodLogWithAdditionalMessagesLevel(DummyUser) - execTime:")
        .contains(
            "Exiting method: MyServiceTestImpl#methodLogWithAdditionalMessagesLevel(DummyUser) - userId = 1");
  }

  @Test
  void testLogExceptionWithStackTrace() {
    assertThatThrownBy(() -> myServiceTest.methodLogExceptionWithStackTrace())
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Fake error");

    assertThat(silentOutput.toString())
        .contains(
            "INFO - methodeSignature:MyServiceTestImpl#methodLogExceptionWithStackTrace() - Entering method: MyServiceTestImpl#methodLogExceptionWithStackTrace()")
        .contains(
            "ERROR - methodeSignature:MyServiceTestImpl#methodLogExceptionWithStackTrace() - execTime:")
        .contains(
            " Exiting method MyServiceTestImpl#methodLogExceptionWithStackTrace() with exception = RuntimeException : Fake error")
        .contains("Additional message after throwing exception")
        .contains("java.lang.RuntimeException: Fake error");
  }

  @Test
  void testLogErrorWithStackTrace() {
    assertThatThrownBy(() -> myServiceTest.methodErrorWithStackTrace())
        .isInstanceOf(Error.class)
        .hasMessage("Fake error");

    assertThat(silentOutput.toString())
        .contains(
            "INFO - methodeSignature:MyServiceTestImpl#methodErrorWithStackTrace() - Entering method: MyServiceTestImpl#methodErrorWithStackTrace()")
        .contains(
            "ERROR - methodeSignature:MyServiceTestImpl#methodErrorWithStackTrace() - execTime:")
        .contains(
            "Exiting method MyServiceTestImpl#methodErrorWithStackTrace() with exception = Error : Fake error")
        .contains("Additional message after throwing error")
        .contains("java.lang.Error: Fake error");
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

  interface IMyServiceTest {

    void methodLog();

    void methodLogArgs(String arg1, Integer arg2);

    void methodReplaceMessages(String arg1, Integer arg2);

    void methodLogArgsSpEL(DummyUser user, Integer arg2);

    void methodLogLevel();

    void methodLogWithAdditionalMessages(DummyUser user);

    void methodLogWithAdditionalMessagesLevel(DummyUser user);

    void methodLogExceptionWithStackTrace();

    void methodErrorWithStackTrace();
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

    @LogExecution(
        beforeLogMessage = "Start methodReplaceMessages",
        afterReturningLogMessage = "Finish methodReplaceMessages")
    @Override
    public void methodReplaceMessages(String arg1, Integer arg2) {
      // Nothing to do
    }

    @LogExecution(
        beforeLogMessage =
            "Entering MyServiceTestImpl#methodLogArgsSpEL with user : userId = #{#user?.id} username = #{#user?.username}")
    @Override
    public void methodLogArgsSpEL(DummyUser user, Integer arg2) {}

    @LogExecution(level = Level.WARN)
    @Override
    public void methodLogLevel() {
      // Nothing to do
    }

    @LogExecution(LogMessageAdditionalInformation = "userId = #{#user?.id}")
    @Override
    public void methodLogWithAdditionalMessages(DummyUser user) {
      // Nothing to do
    }

    @LogExecution(
        level = Level.DEBUG,
        LogMessageAdditionalInformation = "userId = #{#user?.id}",
        afterReturningLogLevel = LogLevel.WARN)
    @Override
    public void methodLogWithAdditionalMessagesLevel(DummyUser user) {
      // Nothing to do
    }

    @LogExecution(
        afterThrowingLogStackTrace = true,
        afterThrowingLogLevel = LogLevel.ERROR,
        LogMessageAdditionalInformation = "Additional message after throwing exception")
    @Override
    public void methodLogExceptionWithStackTrace() {
      throw new RuntimeException("Fake error");
    }

    @LogExecution(
        afterThrowingLogStackTrace = true,
        afterThrowingLogLevel = LogLevel.ERROR,
        LogMessageAdditionalInformation = "Additional message after throwing error")
    @Override
    public void methodErrorWithStackTrace() {
      throw new Error("Fake error");
    }
  }
}
