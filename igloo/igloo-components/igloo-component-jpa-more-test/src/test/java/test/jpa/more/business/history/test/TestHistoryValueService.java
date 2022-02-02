package test.jpa.more.business.history.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.iglooproject.commons.util.rendering.IRenderer;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.business.generic.service.IEntityService;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.OngoingStubbing;

import test.jpa.more.business.entity.model.TestEntity;
import test.jpa.more.business.history.service.TestHistoryValueServiceImpl;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestHistoryValueService /** Mocked, no need for extending a base class */ {
	
	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	
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
	
	@BeforeEach
	public void initPropertyService() {
		OngoingStubbing<Locale> ongoing = when(propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));
		ongoing.thenReturn(DEFAULT_LOCALE);
	}

	@Test
	void retrieveGenericEntityMissing() {
		GenericEntityReference<Long, TestEntity> missingEntityReference = GenericEntityReference.of(TestEntity.class, 1L);
		HistoryValue historyValue = new HistoryValue("label", missingEntityReference);
		
		when(entityService.getEntity(missingEntityReference)).thenReturn(null);
		
		assertNull(historyValueService.retrieve(historyValue));
	}

	@Test
	void retrieveGenericEntityPresent() {
		TestEntity testEntity = new TestEntity();
		GenericEntityReference<Long, TestEntity> presentEntityReference = GenericEntityReference.of(TestEntity.class, 1L);
		HistoryValue historyValue = new HistoryValue("label", presentEntityReference);
		
		when(entityService.getEntity(presentEntityReference)).thenReturn(testEntity);
		
		assertEquals(testEntity, historyValueService.retrieve(historyValue));
	}

	@Test
	void renderHistoryValueExplicitRendererWithRetrieval() {
		final String expectedRendering = "EXPECTED";
		
		TestEntity testEntity = new TestEntity();
		GenericEntityReference<Long, TestEntity> presentEntityReference = GenericEntityReference.of(TestEntity.class, 1L);
		HistoryValue historyValue = new HistoryValue("UNEXPECTED", presentEntityReference);
		
		when(entityService.getEntity(presentEntityReference)).thenReturn(testEntity);
		
		when(renderer.render(testEntity, DEFAULT_LOCALE)).thenReturn(expectedRendering);
		
		assertEquals(expectedRendering, historyValueService.render(historyValue, renderer, DEFAULT_LOCALE));
	}

	@Test
	void renderHistoryValueImplicitRendererWithRetrieval() {
		final String expectedRendering = "EXPECTED";
		
		TestEntity testEntity = new TestEntity();
		GenericEntityReference<Long, TestEntity> presentEntityReference = GenericEntityReference.of(TestEntity.class, 1L);
		HistoryValue historyValue = new HistoryValue("UNEXPECTED", presentEntityReference);
		
		when(entityService.getEntity(presentEntityReference)).thenReturn(testEntity);
		
		when(renderer.render(testEntity, DEFAULT_LOCALE)).thenReturn(expectedRendering);
		
		when(rendererService.findRenderer(TestEntity.class)).then(/* Bypass generics with an Answer */ new Returns(renderer));
		
		assertEquals(expectedRendering, historyValueService.render(historyValue, DEFAULT_LOCALE));
	}

	@Test
	void renderHistoryValueExplicitRendererNoRetrieval() {
		final String expectedRendering = "EXPECTED";
		
		HistoryValue historyValue = new HistoryValue(expectedRendering);
		
		assertEquals(expectedRendering, historyValueService.render(historyValue, renderer, DEFAULT_LOCALE));
	}

	@Test
	void renderHistoryValueImplicitRendererNoRetrieval() {
		final String expectedRendering = "EXPECTED";
		
		HistoryValue historyValue = new HistoryValue(expectedRendering);
		
		assertEquals(expectedRendering, historyValueService.render(historyValue, DEFAULT_LOCALE));
	}

	@Test
	void createNullExplicitRenderer() {
		final String expectedRendering = "EXPECTED";
		
		when(renderer.render(null, DEFAULT_LOCALE)).thenReturn(expectedRendering);
		
		HistoryValue expectedValue = new HistoryValue(expectedRendering);
		
		assertEquals(expectedValue, historyValueService.create(null, renderer));
	}

	@Test
	void createNullImplicitRenderer() {
		HistoryValue expectedValue = new HistoryValue();
		
		assertEquals(expectedValue, historyValueService.create(null));
	}

	@Test
	void createEntityTypeExplicitRenderer() {
		final String expectedRendering = "EXPECTED";
		final TestEntity originalValue = new TestEntity();
		originalValue.setId(1L);
		
		when(renderer.render(originalValue, DEFAULT_LOCALE)).thenReturn(expectedRendering);
		
		HistoryValue expectedValue = new HistoryValue(expectedRendering, GenericEntityReference.of(originalValue));
		
		assertEquals(expectedValue, historyValueService.create(originalValue, renderer));
	}

	@Test
	void createEntityTypeImplicitRenderer() {
		final String expectedRendering = "EXPECTED";
		final TestEntity originalValue = new TestEntity();
		originalValue.setId(1L);
		
		when(renderer.render(originalValue, DEFAULT_LOCALE)).thenReturn(expectedRendering);
		
		when(rendererService.findRenderer(TestEntity.class)).then(/* Bypass generics with an Answer */ new Returns(renderer));
		
		HistoryValue expectedValue = new HistoryValue(expectedRendering, GenericEntityReference.of(originalValue));
		
		assertEquals(expectedValue, historyValueService.create(originalValue));
	}

	@Test
	void createMiscTypeExplicitRenderer() {
		final String expectedRendering = "EXPECTED";
		final Object originalValue = new Object();
		
		when(renderer.render(originalValue, DEFAULT_LOCALE)).thenReturn(expectedRendering);
		
		HistoryValue expectedValue = new HistoryValue(expectedRendering);
		
		assertEquals(expectedValue, historyValueService.create(originalValue, renderer));
	}

	@Test
	void createMiscTypeImplicitRenderer() {
		final String expectedRendering = "EXPECTED";
		final Object originalValue = new Object();
		
		when(renderer.render(originalValue, DEFAULT_LOCALE)).thenReturn(expectedRendering);
		
		when(rendererService.findRenderer(Object.class)).then(/* Bypass generics with an Answer */ new Returns(renderer));
		
		HistoryValue expectedValue = new HistoryValue(expectedRendering);
		
		assertEquals(expectedValue, historyValueService.create(originalValue));
	}
}
