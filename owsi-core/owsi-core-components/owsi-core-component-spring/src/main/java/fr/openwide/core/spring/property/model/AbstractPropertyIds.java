package fr.openwide.core.spring.property.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;

public abstract class AbstractPropertyIds {
	
	private static ConcurrentMap<Class<?>, PropertyRegistryKeyDeclaration> declarationsByDeclaringClass =
			new ConcurrentHashMap<>();
	
	protected AbstractPropertyIds() {
	}
	
	public static <T> ImmutablePropertyId<T> immutable(String key) {
		PropertyRegistryKeyDeclaration declaration = getDeclaration();
		ImmutablePropertyId<T> id = new ImmutablePropertyId<T>(declaration, key);
		declaration.addKey(id);
		return id;
	}
	
	public static <T> MutablePropertyId<T> mutable(String key) {
		PropertyRegistryKeyDeclaration declaration = getDeclaration();
		MutablePropertyId<T> id = new MutablePropertyId<T>(declaration, key);
		declaration.addKey(id);
		return id;
	}
	
	public static <T> ImmutablePropertyIdTemplate<T> immutableTemplate(String key) {
		PropertyRegistryKeyDeclaration declaration = getDeclaration();
		ImmutablePropertyIdTemplate<T> template = new ImmutablePropertyIdTemplate<T>(declaration, key);
		declaration.addKey(template);
		return template;
	}
	
	public static <T> MutablePropertyIdTemplate<T> mutableTemplate(String key) {
		PropertyRegistryKeyDeclaration declaration = getDeclaration();
		MutablePropertyIdTemplate<T> template = new MutablePropertyIdTemplate<T>(declaration, key);
		declaration.addKey(template);
		return template;
	}

	private static PropertyRegistryKeyDeclaration getDeclaration() {
		/* This is not API, but the method still exists in Java 8 and there are discussions about
		 * adding an API in Java 9.
		 * In Java 9, we'll have to either use this new API if it exists, or otherwise we'll remove
		 * the declaring class auto-detection, which will prevent us from checking if every property IDs of
		 * a given class have been registered (see PropertyServiceImpl). 
		 */
		@SuppressWarnings({ "deprecation", "restriction" })
		Class<?> callingClass = sun.reflect.Reflection.getCallerClass(3);
		return getDeclaration(callingClass);
	}

	private static PropertyRegistryKeyDeclaration getDeclaration(Class<?> declaringClass) {
		PropertyRegistryKeyDeclaration declaration = new PropertyRegistryKeyDeclaration(declaringClass);
		return MoreObjects.firstNonNull(
				declarationsByDeclaringClass.putIfAbsent(declaringClass, declaration),
				declaration
		);
	}
	
	private static final class PropertyRegistryKeyDeclaration implements IPropertyRegistryKeyDeclaration {
		private static final long serialVersionUID = 1L;

		private final Class<?> declaringClass;
		
		private final Set<IPropertyRegistryKey<?>> declaredKeys = Sets.newLinkedHashSet();

		public PropertyRegistryKeyDeclaration(Class<?> declaringClass) {
			super();
			this.declaringClass = declaringClass;
		}

		@Override
		public Class<?> getDeclaringClass() {
			return declaringClass;
		}

		@Override
		public Set<IPropertyRegistryKey<?>> getDeclaredKeys() {
			return Collections.unmodifiableSet(declaredKeys);
		}
		
		public void addKey(IPropertyRegistryKey<?> key) {
			synchronized(declaredKeys) {
				declaredKeys.add(key);
			}
		}

		protected Object writeReplace() {
			return new SerializedForm(this);
		}
		
		private static final class SerializedForm implements Serializable {
			private static final long serialVersionUID = 1L;
			
			private Class<?> declaringClass;
			
			public SerializedForm(PropertyRegistryKeyDeclaration declaration) {
				super();
				this.declaringClass = declaration.getDeclaringClass();
			}
			
			protected Object readResolve() {
				return getDeclaration(declaringClass);
			}
		}
	}
}
