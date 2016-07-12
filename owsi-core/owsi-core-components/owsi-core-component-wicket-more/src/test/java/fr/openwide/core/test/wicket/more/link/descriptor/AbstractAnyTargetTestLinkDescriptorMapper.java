package fr.openwide.core.test.wicket.more.link.descriptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import fr.openwide.core.test.wicket.more.business.person.model.Person;
import fr.openwide.core.test.wicket.more.business.person.service.IPersonService;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.parameter.CommonParameters;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterModelValidationException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterSerializedFormValidationException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import fr.openwide.core.wicket.more.markup.html.factory.ConditionFactories;
import fr.openwide.core.wicket.more.markup.html.factory.DetachableFactories;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public abstract class AbstractAnyTargetTestLinkDescriptorMapper extends AbstractTestLinkDescriptor {
	
	@Autowired
	private IPersonService personService;
	
	protected abstract <T> IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, T> buildWithNullTarget(
			ILateTargetDefinitionTerminalState<
					IOneParameterLinkDescriptorMapper<IPageLinkDescriptor, T>,
					IOneParameterLinkDescriptorMapper<IResourceLinkDescriptor, T>,
					IOneParameterLinkDescriptorMapper<IImageResourceLinkDescriptor, T>
					> builder);
	
	protected abstract <T> IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, T> buildWithOneParameterTarget(
			ILateTargetDefinitionTerminalState<
					IOneParameterLinkDescriptorMapper<IPageLinkDescriptor, T>,
					IOneParameterLinkDescriptorMapper<IResourceLinkDescriptor, T>,
					IOneParameterLinkDescriptorMapper<IImageResourceLinkDescriptor, T>
					> builder);

	/**
	 * @return A link descriptor mapper that will point to a null target.
	 */
	protected abstract <T> IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, T>
			buildWithParameterDependentNullTarget(IOneChosenParameterState<
					?,
					T,
					IOneParameterLinkDescriptorMapper<IPageLinkDescriptor, T>,
					IOneParameterLinkDescriptorMapper<IResourceLinkDescriptor, T>,
					IOneParameterLinkDescriptorMapper<IImageResourceLinkDescriptor, T>
					> builder);
	
	/**
	 * @return A link descriptor mapper that will point to the no-parameter target if the parameter is
	 * absent, and to the one-parameter target otherwise.
	 */
	protected abstract <T> IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, T>
			buildWithParameterDependentTarget(IOneChosenParameterState<
					?,
					T,
					IOneParameterLinkDescriptorMapper<IPageLinkDescriptor, T>,
					IOneParameterLinkDescriptorMapper<IResourceLinkDescriptor, T>,
					IOneParameterLinkDescriptorMapper<IImageResourceLinkDescriptor, T>
					> builder);
	
	protected abstract String getNoParameterTargetPathPrefix();
	
	protected abstract String getOneParameterTargetPathPrefix();

	@Test(expected = LinkInvalidTargetRuntimeException.class)
	public void url_oneParamOptional_missingTarget() {
		IModel<Long> model = Model.of((Long) null);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithNullTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).optional()
				);
		mapper.map(model).url();
	}

	@Test
	public void extract_oneParamOptional_missingTarget() throws Exception {
		IModel<Long> model = Model.of((Long) null);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithNullTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).optional()
				);
		mapper.map(model).extract(new PageParameters());
		// Just check that there is no exception thrown, which means that target validation is *not* performed
	}

	@Test(expected = LinkInvalidTargetRuntimeException.class)
	public void url_oneParamOptional_missingParameterDependentTarget() {
		IModel<Long> model = Model.of((Long) null);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithParameterDependentNullTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).optional()
				);
		mapper.map(model).url();
	}

	@Test
	public void extract_oneParamOptional_missingParameterDependentTarget() throws Exception {
		IModel<Long> model = Model.of((Long) null);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithParameterDependentNullTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).optional()
				);
		mapper.map(model).extract(new PageParameters());
		// Just check that there is no exception thrown, which means that target validation is *not* performed
	}

	@Test(expected = LinkParameterValidationRuntimeException.class)
	public void url_oneParamMandatory_mandatoryFails() {
		IModel<Long> model = Model.of((Long)null);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).mandatory()
				);
		mapper.map(model).url();
	}

	@Test(expected = LinkParameterSerializedFormValidationException.class)
	public void extract_oneParamMandatory_mandatoryFails() throws Exception {
		IModel<Long> model = Model.of((Long)null);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).mandatory()
				);
		mapper.map(model).extract(new PageParameters());
	}

	@Test
	public void url_oneParamMandatory_mandatoryPasses() {
		IModel<Long> model = Model.of(1L);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).mandatory()
				);
		assertThat(mapper.map(model).url(), hasPathAndQuery(getOneParameterTargetPathPrefix() + "1"));
	}

	@Test
	public void extract_oneParamMandatory_mandatoryPasses() throws Exception {
		IModel<Long> model = Model.of((Long)null);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).mandatory()
				);
		mapper.map(model).extract(new PageParameters().add(CommonParameters.ID, 1L));
		assertEquals(Long.valueOf(1L), model.getObject());
	}

	@Test(expected = LinkParameterValidationRuntimeException.class)
	public void url_oneParamOptional_validatorFails() {
		IModel<Long> model = Model.of(1L);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).mandatory()
						.validator(Condition.alwaysFalse())
				);
		mapper.map(model).url();
	}

	@Test(expected = LinkParameterModelValidationException.class)
	public void extract_oneParamOptional_validatorFails() throws Exception {
		IModel<Long> model = Model.of((Long) null);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).optional()
						.validator(Condition.alwaysFalse())
				);
		mapper.map(model).extract(new PageParameters());
	}

	@Test
	public void url_oneParamOptional_validatorPasses() {
		IModel<Long> model = Model.of(1L);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).optional()
						.validator(Condition.alwaysTrue())
				);
		assertThat(mapper.map(model).url(), hasPathAndQuery(getOneParameterTargetPathPrefix() + "1"));
	}

	@Test
	public void extract_oneParamOptional_validatorPasses() throws Exception {
		IModel<Long> model = Model.of((Long)null);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).mandatory()
						.validator(Condition.alwaysTrue())
				);
		mapper.map(model).extract(new PageParameters().add(CommonParameters.ID, 1L));
		assertEquals(Long.valueOf(1L), model.getObject());
	}

	@Test(expected = LinkParameterValidationRuntimeException.class)
	public void url_oneParamOptional_parameterDependentValidatorFails() {
		IModel<Long> model = Model.of((Long) null);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).optional()
						.validator(DetachableFactories.forUnit(ConditionFactories.predicate(Predicates.notNull())))
				);
		mapper.map(model).url();
	}

	@Test(expected = LinkParameterModelValidationException.class)
	public void extract_oneParamOptional_parameterDependentValidatorFails() throws Exception {
		IModel<Long> model = Model.of((Long) null);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).optional()
						.validator(DetachableFactories.forUnit(ConditionFactories.predicate(Predicates.notNull())))
				);
		mapper.map(model).extract(new PageParameters());
	}

	@Test
	public void url_oneParamOptional_parameterDependentValidatorPasses() {
		IModel<Long> model = Model.of(1L);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).optional()
						.validator(DetachableFactories.forUnit(ConditionFactories.predicate(Predicates.notNull())))
				);
		assertThat(mapper.map(model).url(),
				hasPathAndQuery(getOneParameterTargetPathPrefix() + "1"));
	}

	@Test
	public void extract_oneParamOptional_parameterDependentValidatorPasses() throws Exception {
		IModel<Long> model = Model.of((Long) null);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).optional()
						.validator(DetachableFactories.forUnit(ConditionFactories.predicate(Predicates.notNull())))
				);
		mapper.map(model).extract(new PageParameters().add(CommonParameters.ID, 1L));
		assertEquals(Long.valueOf(1L), model.getObject());
	}

	@Test
	public void url_oneParamOptional_genericEntity() throws Exception {
		Person person = new Person("John", "Doe");
		personService.create(person);
		IModel<Person> model = GenericEntityModel.of(person);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Person> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.model(Person.class).map(CommonParameters.ID).optional()
				);
		assertThat(mapper.map(model).url(),
				hasPathAndQuery(getOneParameterTargetPathPrefix() + person.getId()));
	}

	@Test
	public void extract_oneParamOptional_genericEntity() throws Exception {
		Person person = new Person("John", "Doe");
		personService.create(person);
		IModel<Person> model = GenericEntityModel.of(person);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Person> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.model(Person.class).map(CommonParameters.ID).optional()
				);
		mapper.map(model).extract(new PageParameters().add(CommonParameters.ID, person.getId()));
		assertEquals(person, model.getObject());
	}

	@Test
	public void url_oneParamOptional_collection() {
		IModel<List<Long>> model = new ListModel<Long>(Lists.newArrayList(1L, 2L));
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, List<Long>> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.<List<Long>>model(List.class).mapCollection(CommonParameters.ID, Long.class).optional()
				);
		assertThat(mapper.map(model).url(),
				hasPathAndQuery(getOneParameterTargetPathPrefix() + "1,2"));
	}

	@Test
	public void extract_oneParamOptional_collection() throws Exception {
		IModel<List<Long>> model = new ListModel<Long>(null);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, List<Long>> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.<List<Long>>model(List.class).mapCollection(CommonParameters.ID, Long.class).optional()
				);
		mapper.map(model).extract(new PageParameters().add(CommonParameters.ID, "1,2"));
		assertEquals(Lists.newArrayList(1L, 2L), model.getObject());
	}

	@Test
	public void url_oneParamOptional_afterSerialization() {
		IModel<Long> model = Model.of(1L);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).optional()
				);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> deserializedMapper =
				serializeAndDeserialize(mapper);
		assertThat(deserializedMapper.map(model).url(),
				hasPathAndQuery(getOneParameterTargetPathPrefix() + "1"));
	}

	@Test
	public void extract_oneParamOptional_afterSerialization() throws Exception {
		IModel<Long> model = Model.of(1L);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithOneParameterTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).optional()
				);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> deserialized =
				serializeAndDeserialize(mapper);
		deserialized.map(model).extract(new PageParameters().add(CommonParameters.ID, 1L));
		assertEquals(Long.valueOf(1L), model.getObject());
	}

	@Test
	public void url_oneParamOptional_targetDependsOnParameter() {
		IModel<Long> model = Model.of((Long) null);
		IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, Long> mapper =
				buildWithParameterDependentTarget(
						LinkDescriptorBuilder.start()
						.model(Long.class).map(CommonParameters.ID).optional()
				);
		assertThat(mapper.map(model).url(), hasPathAndQuery(getNoParameterTargetPathPrefix()));
		
		model.setObject(1L);
		assertThat(mapper.map(model).url(), hasPathAndQuery(getOneParameterTargetPathPrefix() + "1"));
	}
}
