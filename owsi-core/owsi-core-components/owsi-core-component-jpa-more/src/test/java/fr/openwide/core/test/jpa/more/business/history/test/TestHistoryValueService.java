package fr.openwide.core.test.jpa.more.business.history.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.OngoingStubbing;

import fr.openwide.core.commons.util.rendering.IRenderer;
import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;
import fr.openwide.core.jpa.business.generic.service.IEntityService;
import fr.openwide.core.jpa.more.business.history.model.embeddable.HistoryValue;
import fr.openwide.core.jpa.more.rendering.service.IRendererService;
import fr.openwide.core.spring.property.SpringPropertyIds;
import fr.openwide.core.spring.property.service.IPropertyService;
import fr.openwide.core.test.jpa.more.business.entity.model.TestEntity;
import fr.openwide.core.test.jpa.more.business.history.service.TestHistoryValueServiceImpl;

public class TestHistoryValueService /** Mocked, no need for extending a base class */ {
	
	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Mock
	protected IEntityService entityService;
	
	@Mock
	protected IRendererService rendererService;
	
	@Mock
	protected IPropertyService propertyService;

	@Mock
	protected IRenderer<Object> renderer;

	@InjectMocks
	protected TestHistoryValueServiceImpl historyValueService;
	
	@Before
	public void initPropertyService() {
		OngoingStubbing<Locale> ongoing = when(propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));
		ongoing.thenReturn(DEFAULT_LOCALE);
	}

	@Test
	public void retrieveGenericEntityMissing() {
		GenericEntityReference<Long, TestEntity> missingEntityReference = GenericEntityReference.of(TestEntity.class, 1L);
		HistoryValue historyValue = new HistoryValue("label", missingEntityReference);
		
		when(entityService.getEntity(missingEntityReference)).thenReturn(null);
		
		assertNull(historyValueService.retrieve(historyValue));
	}

	@Test
	public void retrieveGenericEntityPresent() {
		TestEntity testEntity = new TestEntity();
		GenericEntityReference<Long, TestEntity> presentEntityReference = GenericEntityReference.of(TestEntity.class, 1L);
		HistoryValue historyValue = new HistoryValue("label", presentEntityReference);
		
		when(entityService.getEntity(presentEntityReference)).thenReturn(testEntity);
		
		assertEquals(testEntity, historyValueService.retrieve(historyValue));
	}

	@Test
	public void renderHistoryValueExplicitRendererWithRetrieval() {
		final String expectedRendering = "EXPECTED";
		
		TestEntity testEntity = new TestEntity();
		GenericEntityReference<Long, TestEntity> presentEntityReference = GenericEntityReference.of(TestEntity.class, 1L);
		HistoryValue historyValue = new HistoryValue("UNEXPECTED", presentEntityReference);
		
		when(entityService.getEntity(presentEntityReference)).thenReturn(testEntity);
		
		when(renderer.render(testEntity, DEFAULT_LOCALE)).thenReturn(expectedRendering);
		
		assertEquals(expectedRendering, historyValueService.render(historyValue, renderer));
	}

	@Test
	public void renderHistoryValueImplicitRendererWithRetrieval() {
		final String expectedRendering = "EXPECTED";
		
		TestEntity testEntity = new TestEntity();
		GenericEntityReference<Long, TestEntity> presentEntityReference = GenericEntityReference.of(TestEntity.class, 1L);
		HistoryValue historyValue = new HistoryValue("UNEXPECTED", presentEntityReference);
		
		when(entityService.getEntity(presentEntityReference)).thenReturn(testEntity);
		
		when(renderer.render(testEntity, DEFAULT_LOCALE)).thenReturn(expectedRendering);
		
		when(rendererService.findRenderer(TestEntity.class)).then(/* Bypass generics with an Answer */ new Returns(renderer));
		
		assertEquals(expectedRendering, historyValueService.render(historyValue));
	}

	@Test
	public void renderHistoryValueExplicitRendererNoRetrieval() {
		final String expectedRendering = "EXPECTED";
		
		HistoryValue historyValue = new HistoryValue(expectedRendering);
		
		assertEquals(expectedRendering, historyValueService.render(historyValue, renderer));
	}

	@Test
	public void renderHistoryValueImplicitRendererNoRetrieval() {
		final String expectedRendering = "EXPECTED";
		
		HistoryValue historyValue = new HistoryValue(expectedRendering);
		
		assertEquals(expectedRendering, historyValueService.render(historyValue));
	}

	@Test
	public void createNullExplicitRenderer() {
		final String expectedRendering = "EXPECTED";
		
		when(renderer.render(null, DEFAULT_LOCALE)).thenReturn(expectedRendering);
		
		HistoryValue expectedValue = new HistoryValue(expectedRendering);
		
		assertEquals(expectedValue, historyValueService.create(null, renderer));
	}

	@Test
	public void createNullImplicitRenderer() {
		HistoryValue expectedValue = new HistoryValue();
		
		assertEquals(expectedValue, historyValueService.create(null));
	}

	@Test
	public void createEntityTypeExplicitRenderer() {
		final String expectedRendering = "EXPECTED";
		final TestEntity originalValue = new TestEntity();
		originalValue.setId(1L);
		
		when(renderer.render(originalValue, DEFAULT_LOCALE)).thenReturn(expectedRendering);
		
		HistoryValue expectedValue = new HistoryValue(expectedRendering, GenericEntityReference.of(originalValue));
		
		assertEquals(expectedValue, historyValueService.create(originalValue, renderer));
	}

	@Test
	public void createEntityTypeImplicitRenderer() {
		final String expectedRendering = "EXPECTED";
		final TestEntity originalValue = new TestEntity();
		originalValue.setId(1L);
		
		when(renderer.render(originalValue, DEFAULT_LOCALE)).thenReturn(expectedRendering);
		
		when(rendererService.findRenderer(TestEntity.class)).then(/* Bypass generics with an Answer */ new Returns(renderer));
		
		HistoryValue expectedValue = new HistoryValue(expectedRendering, GenericEntityReference.of(originalValue));
		
		assertEquals(expectedValue, historyValueService.create(originalValue));
	}

	@Test
	public void createMiscTypeExplicitRenderer() {
		final String expectedRendering = "EXPECTED";
		final Object originalValue = new Object();
		
		when(renderer.render(originalValue, DEFAULT_LOCALE)).thenReturn(expectedRendering);
		
		HistoryValue expectedValue = new HistoryValue(expectedRendering);
		
		assertEquals(expectedValue, historyValueService.create(originalValue, renderer));
	}

	@Test
	public void createMiscTypeImplicitRenderer() {
		final String expectedRendering = "EXPECTED";
		final Object originalValue = new Object();
		
		when(renderer.render(originalValue, DEFAULT_LOCALE)).thenReturn(expectedRendering);
		
		when(rendererService.findRenderer(Object.class)).then(/* Bypass generics with an Answer */ new Returns(renderer));
		
		HistoryValue expectedValue = new HistoryValue(expectedRendering);
		
		assertEquals(expectedValue, historyValueService.create(originalValue));
	}
}
