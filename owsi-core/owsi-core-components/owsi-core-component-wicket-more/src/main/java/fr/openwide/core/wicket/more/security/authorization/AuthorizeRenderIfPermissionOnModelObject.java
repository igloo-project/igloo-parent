package fr.openwide.core.wicket.more.security.authorization;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IValidatorState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.IOneParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperOneChosenParameterMappingState;

/**
 * @deprecated Use validation features in {@link LinkDescriptorBuilder LinkDescriptors} instead.
 * Here's how to add permission validation to your link descriptors:
 * <ul>
 * <li>When building a LinkDescriptor mapper: <code>new LinkDescriptorBuilder().page(...).model(MyParameterClass.class).map("id").permission(MyPermissionConstants.MY_PERMISSION).build()</code>
 * <li>When building a LinkDescriptor directly (legacy syntax): <code>new LinkDescriptorBuilder().page(...).map("id", myParameterModel, MyParameterClass.class).permission(MyPermissionConstants.MY_PERMISSION).build()</code>
 * </ul>
 * @see IValidatorState#permission(org.apache.wicket.model.IModel, String, String...)
 * @see IParameterMapperOneChosenParameterMappingState#permission(String)
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE })
@Documented
@Inherited
public @interface AuthorizeRenderIfPermissionOnModelObject {

	String[] permissions();

}