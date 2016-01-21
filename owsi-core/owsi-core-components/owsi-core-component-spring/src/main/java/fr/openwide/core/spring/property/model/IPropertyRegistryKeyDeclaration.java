package fr.openwide.core.spring.property.model;

import java.io.Serializable;
import java.util.Set;

public interface IPropertyRegistryKeyDeclaration extends Serializable {
	
	Class<?> getDeclaringClass();
	
	Set<IPropertyRegistryKey<?>> getDeclaredKeys();

}
