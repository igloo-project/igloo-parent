package fr.openwide.core.test.jpa.more.business.history.test;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.fieldpath.FieldPath;
import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;
import fr.openwide.core.jpa.business.generic.service.IEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.difference.model.Difference;
import fr.openwide.core.jpa.more.business.difference.util.IDifferenceFromReferenceGenerator;
import fr.openwide.core.jpa.more.business.difference.util.IHistoryDifferenceGenerator;
import fr.openwide.core.jpa.more.business.history.model.atomic.HistoryDifferenceEventType;
import fr.openwide.core.jpa.more.business.history.model.embeddable.HistoryDifferencePath;
import fr.openwide.core.jpa.more.business.history.model.embeddable.HistoryValue;
import fr.openwide.core.jpa.more.junit.difference.TestHistoryDifferenceCollectionMatcher;
import fr.openwide.core.jpa.more.junit.difference.TestHistoryDifferenceDescription;
import fr.openwide.core.test.jpa.more.business.AbstractJpaMoreTestCase;
import fr.openwide.core.test.jpa.more.business.entity.model.TestEntity;
import fr.openwide.core.test.jpa.more.business.history.model.TestHistoryDifference;
import fr.openwide.core.test.jpa.more.business.history.model.TestHistoryLog;
import fr.openwide.core.test.jpa.more.business.history.model.atomic.TestHistoryEventType;
import fr.openwide.core.test.jpa.more.business.history.model.bean.TestHistoryLogAdditionalInformationBean;
import fr.openwide.core.test.jpa.more.business.history.service.ITestHistoryLogService;

public class TestHistoryLogService extends AbstractJpaMoreTestCase {
	
	private static final Date DATE = new Date();

	/*
	 * Only here to mock some parameters passed to the log() method.
	 * The Spring context is still used for most beans.
	 */
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Mock(answer = Answers.RETURNS_MOCKS)
	private IDifferenceFromReferenceGenerator<TestEntity> differenceGeneratorMock;
	
	@Mock
	private IHistoryDifferenceGenerator<TestEntity> historyDifferenceGeneratorMock;
	
	@Autowired
	private ITestHistoryLogService historyLogService;
	
	@Autowired
	private IEntityService entityService;

	private TransactionTemplate writeTransactionTemplate;
	
	private HistoryValue entityHistoryValueBefore;
	private HistoryValue entityHistoryValueAfter;
	
	private HistoryValue stringHistoryValueAfter = new HistoryValue("after");
	
	private HistoryValue createExpectedHistoryValue(TestEntity entity) {
		return new HistoryValue(entity.toString(), GenericEntityReference.of(entity));
	}
	
	@Autowired
	private void setTransactionTemplate(PlatformTransactionManager transactionManager) {
		DefaultTransactionAttribute writeTransactionAttribute =
				new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
		writeTransactionAttribute.setReadOnly(false);
		writeTransactionTemplate = new TransactionTemplate(transactionManager, writeTransactionAttribute);
	}
	
	@Before
	public void initValues() throws ServiceException, SecurityServiceException {
		TestEntity before = new TestEntity("beforeEntity");
		TestEntity after = new TestEntity("afterEntity");
		testEntityService.create(before);
		testEntityService.create(after);
		entityHistoryValueBefore = createExpectedHistoryValue(before);
		entityHistoryValueAfter = createExpectedHistoryValue(after);
	}
	
	@Before
	public void initMocks() {
		// Make the difference generation fail if the modified object is not attached to the session
		AssertionError error = new AssertionError("Attempt to compute differences on an object that was not attached to the session");
		when(differenceGeneratorMock.diff(
				argThat(not(this.<TestEntity>isAttachedToSession())), Matchers.<TestEntity>anyObject()
		)).thenThrow(error);
		when(differenceGeneratorMock.diffFromReference(
				argThat(not(this.<TestEntity>isAttachedToSession()))
		)).thenThrow(error);
	}
	
	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanEntities(historyLogService);
		super.cleanAll();
	}

	private List<TestHistoryDifference> createExpectedDifferences() {
		TestHistoryDifference difference1 = new TestHistoryDifference(
				new HistoryDifferencePath(FieldPath.fromString(".somePropertyIJustInvented")),
				HistoryDifferenceEventType.ADDED,
				entityHistoryValueBefore, entityHistoryValueAfter
		);
		TestHistoryDifference difference2 = new TestHistoryDifference(
				new HistoryDifferencePath(FieldPath.fromString(".somePropertyIJustInvented2").item(), new HistoryValue("1")),
				HistoryDifferenceEventType.REMOVED,
				null, stringHistoryValueAfter
		);
		return Lists.newArrayList(
				difference1,
				difference2
		);
	}
	
	private Matcher<Collection<TestHistoryDifference>> matchesExpectedDifferences() {
		return new TestHistoryDifferenceCollectionMatcher<>(
				TestHistoryDifferenceDescription.builder()
				.put(FieldPath.fromString(".somePropertyIJustInvented"), HistoryDifferenceEventType.ADDED)
				.putItem(FieldPath.fromString(".somePropertyIJustInvented2"), 1, HistoryDifferenceEventType.REMOVED)
				.build()
		);
	}

	@Test
	public void logNow() throws ServiceException, SecurityServiceException {
		TestEntity object = new TestEntity("object");
		testEntityService.create(object);
		
		TestEntity secondaryObject = new TestEntity("secondaryObject");
		testEntityService.create(secondaryObject);
		
		List<TestHistoryDifference> differences = createExpectedDifferences();
		
		historyLogService.logNow(DATE, TestHistoryEventType.EVENT1, differences, object,
				TestHistoryLogAdditionalInformationBean.of(secondaryObject));
		
		HistoryValue expectedObjectHistoryValue = createExpectedHistoryValue(object);
		HistoryValue expectedSecondaryObjectHistoryValue = createExpectedHistoryValue(secondaryObject);

		entityService.flush();
		entityService.clear();

		List<TestHistoryLog> logs = historyLogService.list();
		
		assertEquals(1, logs.size());
		
		TestHistoryLog log = logs.iterator().next();

		assertNotNull(log.getId());
		
		assertEquals(DATE, log.getDate());
		assertEquals(TestHistoryEventType.EVENT1, log.getEventType());
		assertEquals(expectedObjectHistoryValue, log.getMainObject());
		assertEquals(expectedSecondaryObjectHistoryValue, log.getObject1());
		assertThat(log.getDifferences(), matchesExpectedDifferences());
	}

	@Test
	public void logBeforeCommit() throws ServiceException, SecurityServiceException {
		final TestEntity object = new TestEntity("object");
		testEntityService.create(object);
		
		final TestEntity secondaryObject = new TestEntity("secondaryObject");
		testEntityService.create(secondaryObject);
		
		HistoryValue expectedObjectHistoryValue = createExpectedHistoryValue(object);
		HistoryValue expectedSecondaryObjectHistoryValue = createExpectedHistoryValue(secondaryObject);

		entityService.flush();
		entityService.clear();
		
		Mockito.when(historyDifferenceGeneratorMock.toHistoryDifferences(
						Matchers.<Supplier<TestHistoryDifference>>anyObject(), Matchers.<Difference<TestEntity>>anyObject()
				))
				.then(new Answer<List<TestHistoryDifference>>() {
					@Override
					public List<TestHistoryDifference> answer(InvocationOnMock invocation) throws Throwable {
						return createExpectedDifferences();
					}
				});
		
		final Date before = new Date();
		
		writeTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@SuppressWarnings("unchecked")
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				TestEntity objectReloaded = entityService.getEntity(object);
				TestEntity secondaryObjectReloaded = entityService.getEntity(secondaryObject);
				try {
					historyLogService.logWithDifferences(TestHistoryEventType.EVENT1, objectReloaded,
							TestHistoryLogAdditionalInformationBean.of(secondaryObjectReloaded),
							differenceGeneratorMock, historyDifferenceGeneratorMock);
				} catch (ServiceException|SecurityServiceException e) {
					throw new IllegalStateException(e);
				}
			}
		});
		
		final Date after = new Date();

		List<TestHistoryLog> logs = historyLogService.list();
		
		assertEquals(1, logs.size());
		
		TestHistoryLog log = logs.iterator().next();

		assertNotNull(log.getId());
		
		assertThat(log.getDate(), new TypeSafeMatcher<Date>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("a date between ").appendValue(before).appendText(" and ").appendValue(after);
			}

			@Override
			protected boolean matchesSafely(Date item) {
				return !item.before(before) && !item.after(after);
			}
		});
		assertEquals(TestHistoryEventType.EVENT1, log.getEventType());
		assertEquals(expectedObjectHistoryValue, log.getMainObject());
		assertEquals(expectedSecondaryObjectHistoryValue, log.getObject1());
		assertThat(log.getDifferences(), matchesExpectedDifferences());
	}
}
