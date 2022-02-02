package test.wicket.more.link.descriptor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.ILinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterModelValidationException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterSerializedFormValidationException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import org.iglooproject.wicket.more.model.CollectionCopyModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import test.wicket.more.business.person.model.Person;
import test.wicket.more.business.person.service.IPersonService;
import test.wicket.more.link.descriptor.page.TestLinkDescriptorNoParameterPage;

public abstract class AbstractAnyTargetTestLinkDescriptor extends AbstractTestLinkDescriptor {
	
	@Autowired
	private IPersonService personService;
	
	protected abstract ILinkDescriptor buildWithNullTarget(
			ILateTargetDefinitionTerminalState<
					IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor
					> builder);
	
	protected abstract ILinkDescriptor buildWithNoParameterTarget(
			ILateTargetDefinitionTerminalState<
					IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor
					> builder);
	
	protected abstract ILinkDescriptor buildWithOneParameterTarget(
			ILateTargetDefinitionTerminalState<
					IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor
					> builder);
	
	protected abstract String getNoParameterTargetPathPrefix();
	
	protected abstract String getOneParameterTargetPathPrefix();

	@Test
	public void url_noParam_validatorPasses() throws Exception {
		ILinkDescriptor linkDescriptor =
				buildWithNoParameterTarget(
						LinkDescriptorBuilder.start()
						.validator(Condition.alwaysTrue())
				);
		assertThat(linkDescriptor.url(), hasPathAndQuery(getNoParameterTargetPathPrefix()));
	}

	@Test
	public void extract_noParam_validatorPasses() throws Exception {
		ILinkDescriptor linkDescriptor =
				buildWithNoParameterTarget(
						LinkDescriptorBuilder.start()
						.validator(Condition.alwaysTrue())
				);
		assertDoesNotThrow(() -> linkDescriptor.extract(new PageParameters()));
	}

	@Test
	public void url_noParam_validatorFails() {
		ILinkDescriptor linkDescriptor = LinkDescriptorBuilder.start()
				.validator(Condition.alwaysFalse())
				.page(TestLinkDescriptorNoParameterPage.class);
		assertThrows(
			LinkParameterValidationRuntimeException.class,
			() -> linkDescriptor.url()
		);
	}

	@Test
	public void extract_noParam_validatorFails() throws Exception {
		ILinkDescriptor linkDescriptor =
				buildWithNoParameterTarget(
						LinkDescriptorBuilder.start()
						.validator(Condition.alwaysFalse())
				);
		assertThrows(
			LinkParameterModelValidationException.class,
			() -> linkDescriptor.extract(new PageParameters())
		);
	}

	@Test
	public void url_noParam_missingTarget() {
		ILinkDescriptor linkDescriptor =
				buildWithNullTarget(
						LinkDescriptorBuilder.start()
				);
		assertThrows(
			LinkInvalidTargetRuntimeException.class,
			() -> linkDescriptor.url()
		);
	}

	@Test
	public void extract_noParam_missingTarget() throws Exception {
		ILinkDescriptor linkDescriptor =
				buildWithNullTarget(
						LinkDescriptorBuilder.start()
				);
		assertDoesNotThrow(() -> linkDescriptor.extract(new PageParameters()));
	}

	@Test
	public void url_oneParamMandatory_mandatoryFails() {
		IModel<Long> model = Model.of((Long)null);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model, Long.class).mandatory()
				);
		assertThrows(
			LinkParameterValidationRuntimeException.class,
			() -> linkDescriptor.url()
		);
	}

	@Test
	public void extract_oneParamMandatory_mandatoryFails() throws Exception {
		IModel<Long> model = Model.of((Long)null);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model, Long.class).mandatory()
				);
		assertThrows(
			LinkParameterSerializedFormValidationException.class,
			() -> linkDescriptor.extract(new PageParameters())
		);
	}

	@Test
	public void url_oneParamMandatory_mandatoryPasses() {
		IModel<Long> model = Model.of(1L);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model, Long.class).mandatory()
				);
		assertThat(linkDescriptor.url(), hasPathAndQuery(getOneParameterTargetPathPrefix() + "1"));
	}

	@Test
	public void extract_oneParamMandatory_mandatoryPasses() throws Exception {
		IModel<Long> model = Model.of((Long)null);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model, Long.class).mandatory()
				);
		linkDescriptor.extract(new PageParameters().add(CommonParameters.ID, 1L));
		assertEquals(Long.valueOf(1L), model.getObject());
	}

	@Test
	public void url_oneParamOptional_validatorFails() {
		IModel<Long> model = Model.of(1L);
		ILinkDescriptor linkDescriptor =
			buildWithOneParameterTarget(
					LinkDescriptorBuilder.start()
					.map(CommonParameters.ID, model, Long.class).optional()
					.validator(Condition.alwaysFalse())
			);
		assertThrows(
			LinkParameterValidationRuntimeException.class,
			() -> linkDescriptor.url()
		);
	}

	@Test
	public void extract_oneParamOptional_validatorFails() throws Exception {
		IModel<Long> model = Model.of(1L);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model, Long.class).optional()
						.validator(Condition.alwaysFalse())
				);
		assertThrows(
			LinkParameterModelValidationException.class,
			() -> linkDescriptor.extract(new PageParameters())
		);
	}

	@Test
	public void url_oneParamOptional_validatorPasses() {
		IModel<Long> model = Model.of(1L);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model, Long.class).optional()
						.validator(Condition.alwaysTrue())
				);
		assertThat(linkDescriptor.url(), hasPathAndQuery(getOneParameterTargetPathPrefix() + "1"));
	}

	@Test
	public void extract_oneParamOptional_validatorPasses() throws Exception {
		IModel<Long> model = Model.of((Long)null);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model, Long.class).optional()
						.validator(Condition.alwaysTrue())
				);
		linkDescriptor.extract(new PageParameters().add(CommonParameters.ID, 1L));
		assertEquals(Long.valueOf(1L), model.getObject());
	}

	@Test
	public void url_oneParamOptional_genericEntity() throws Exception {
		Person person = new Person("John", "Doe");
		personService.create(person);
		IModel<Person> model = GenericEntityModel.of(person);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model, Person.class).optional()
				);
		assertThat(linkDescriptor.url(),
				hasPathAndQuery(getOneParameterTargetPathPrefix() + person.getId()));
	}

	@Test
	public void extract_oneParamOptional_genericEntity() throws Exception {
		Person person = new Person("John", "Doe");
		personService.create(person);
		IModel<Person> model = GenericEntityModel.of(person);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model, Person.class).optional()
				);
		linkDescriptor.extract(new PageParameters().add(CommonParameters.ID, person.getId()));
		assertEquals(person, model.getObject());
	}

	@Test
	public void url_oneParamOptional_collection() {
		IModel<List<Long>> model = new ListModel<Long>(Lists.newArrayList(1L, 2L));
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.mapCollection(CommonParameters.ID, model, List.class, Long.class).optional()
				);
		assertThat(linkDescriptor.url(),
				hasPathAndQuery(getOneParameterTargetPathPrefix() + "1,2"));
	}

	@Test
	public void extract_oneParamOptional_collection() throws Exception {
		IModel<List<Long>> model = new ListModel<Long>(null);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.mapCollection(CommonParameters.ID, model, List.class, Long.class).optional()
				);
		linkDescriptor.extract(new PageParameters().add(CommonParameters.ID, "1,2"));
		assertEquals(Lists.newArrayList(1L, 2L), model.getObject());
	}

	@Test
	public void url_oneParamOptional_afterSerialization() {
		IModel<Long> model = Model.of(1L);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model, Long.class).optional()
				);
		ILinkDescriptor deserializedLinkDescriptor = serializeAndDeserialize(linkDescriptor);
		assertThat(deserializedLinkDescriptor.url(),
				hasPathAndQuery(getOneParameterTargetPathPrefix() + "1"));
	}

	/**
	 * We've had issues in the past with serializing TypeDescriptors of Collection<? extends GenericEntity>
	 */
	@Test
	public void serialization_oneParamOptional_genericEntityCollection() throws Exception {
		Person person1 = new Person("Jane", "Doe");
		personService.create(person1);
		Person person2 = new Person("John", "Doe");
		personService.create(person2);
		IModel<List<Person>> model = CollectionCopyModel.custom(
				Suppliers2.<Person>arrayListAsList(), GenericEntityModel.<Person>factory()
		);
		model.setObject(ImmutableList.of(person1, person2));
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.mapCollection(CommonParameters.ID, model, List.class, Person.class).optional()
				);
		Pair<IModel<List<Person>>, ILinkDescriptor> deserialized =
				serializeAndDeserialize(Pair.with(model, linkDescriptor));
		assertThat(deserialized.getValue1().url(),
				hasPathAndQuery(getOneParameterTargetPathPrefix() + person1.getId() + "," + person2.getId()));
	}

	@Test
	public void extract_oneParamOptional_afterSerialization() throws Exception {
		IModel<Long> model = Model.of((Long) null);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model, Long.class).optional()
				);
		Pair<IModel<Long>, ILinkDescriptor> deserialized =
				serializeAndDeserialize(Pair.with(model, linkDescriptor));
		assertNull(deserialized.getValue0().getObject());
		deserialized.getValue1().extract(new PageParameters().add(CommonParameters.ID, 1L));
		assertEquals(Long.valueOf(1L), deserialized.getValue0().getObject());
	}

	@Test
	public void url_twoParam() {
		IModel<Long> model1 = Model.of(1L);
		IModel<Long> model2 = Model.of(2L);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model1, Long.class).mandatory()
						.map("param2", model2, Long.class).mandatory()
				);
		assertThat(linkDescriptor.url(),
				hasPathAndQuery(getOneParameterTargetPathPrefix() + "1?param2=2"));
	}

	@Test
	public void extract_twoParam() throws Exception {
		IModel<Long> model1 = Model.of((Long) null);
		IModel<Long> model2 = Model.of((Long) null);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model1, Long.class).mandatory()
						.map("param2", model2, Long.class).mandatory()
				);
		linkDescriptor.extract(new PageParameters()
				.add(CommonParameters.ID, 1L)
				.add("param2", 2L)
		);
		assertEquals(Long.valueOf(1L), model1.getObject());
		assertEquals(Long.valueOf(2L), model2.getObject());
	}

	@Test
	public void url_threeParam() {
		IModel<Long> model1 = Model.of(1L);
		IModel<Long> model2 = Model.of(2L);
		IModel<Long> model3 = Model.of(3L);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model1, Long.class).mandatory()
						.map("param2", model2, Long.class).mandatory()
						.map("param3", model3, Long.class).mandatory()
				);
		assertThat(linkDescriptor.url(),
				hasPathAndQuery(getOneParameterTargetPathPrefix() + "1?param2=2&param3=3"));
	}

	@Test
	public void extract_threeParam() throws Exception {
		IModel<Long> model1 = Model.of((Long) null);
		IModel<Long> model2 = Model.of((Long) null);
		IModel<Long> model3 = Model.of((Long) null);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model1, Long.class).mandatory()
						.map("param2", model2, Long.class).mandatory()
						.map("param3", model3, Long.class).mandatory()
				);
		linkDescriptor.extract(new PageParameters()
				.add(CommonParameters.ID, 1L)
				.add("param2", 2L)
				.add("param3", 3L)
		);
		assertEquals(Long.valueOf(1L), model1.getObject());
		assertEquals(Long.valueOf(2L), model2.getObject());
		assertEquals(Long.valueOf(3L), model3.getObject());
	}

	@Test
	public void url_fourParam() {
		IModel<Long> model1 = Model.of(1L);
		IModel<Long> model2 = Model.of(2L);
		IModel<Long> model3 = Model.of(3L);
		IModel<Long> model4 = Model.of(4L);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model1, Long.class).mandatory()
						.map("param2", model2, Long.class).mandatory()
						.map("param3", model3, Long.class).mandatory()
						.map("param4", model4, Long.class).mandatory()
				);
		assertThat(linkDescriptor.url(),
				hasPathAndQuery(getOneParameterTargetPathPrefix() + "1?param2=2&param3=3&param4=4"));
	}

	@Test
	public void extract_fourParam() throws Exception {
		IModel<Long> model1 = Model.of((Long) null);
		IModel<Long> model2 = Model.of((Long) null);
		IModel<Long> model3 = Model.of((Long) null);
		IModel<Long> model4 = Model.of((Long) null);
		ILinkDescriptor linkDescriptor =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.map(CommonParameters.ID, model1, Long.class).mandatory()
						.map("param2", model2, Long.class).mandatory()
						.map("param3", model3, Long.class).mandatory()
						.map("param4", model4, Long.class).mandatory()
				);
		linkDescriptor.extract(new PageParameters()
				.add(CommonParameters.ID, 1L)
				.add("param2", 2L)
				.add("param3", 3L)
				.add("param4", 4L)
		);
		assertEquals(Long.valueOf(1L), model1.getObject());
		assertEquals(Long.valueOf(2L), model2.getObject());
		assertEquals(Long.valueOf(3L), model3.getObject());
		assertEquals(Long.valueOf(4L), model4.getObject());
	}
}
