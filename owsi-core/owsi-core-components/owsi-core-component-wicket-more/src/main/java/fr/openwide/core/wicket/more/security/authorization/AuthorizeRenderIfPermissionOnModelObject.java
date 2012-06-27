package fr.openwide.core.wicket.more.security.authorization;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE })
@Documented
@Inherited
public @interface AuthorizeRenderIfPermissionOnModelObject {

	String[] permissions();

}