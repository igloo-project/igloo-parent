package org.igloo.jpa.test;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class EntityManagerFactoryExtension implements ParameterResolver {

	private Object[] settings;

	public EntityManagerFactoryExtension(Object... settings) {
		this.settings = Optional.ofNullable(settings).orElse(new Object[0]);
	}

	public EntityManagerFactoryExtension(Supplier<Object[]> settingsSupplier) {
		this.settings = settingsSupplier.get();
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return Arrays.asList(EntityManagerFactory.class, EntityManager.class, EntityTransaction.class)
				.contains(parameterContext.getParameter().getType());
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		EntityManagerFactoryWrapper wrapper = extensionContext.getStore(Namespace.GLOBAL).getOrComputeIfAbsent(
				"entityManagerFactoryWrapper",
				p -> new EntityManagerFactoryWrapper(EntityManagerFactoryHelper.generateEntityManagerFactory(settings)),
				EntityManagerFactoryWrapper.class);
		if (parameterContext.getParameter().getType().equals(EntityManagerFactory.class)) {
			return wrapper.emf;
		} else if (parameterContext.getParameter().getType().equals(EntityManager.class)) {
			wrapper.em = wrapper.emf.createEntityManager();
			return wrapper.em;
		} else if (parameterContext.getParameter().getType().equals(EntityTransaction.class)) {
			wrapper.t = wrapper.em.getTransaction();
			wrapper.t.begin();
			return wrapper.t;
		} else {
			throw new IllegalStateException("Unkown parameter " + parameterContext.toString());
		}
	}

	static class EntityManagerFactoryWrapper implements CloseableResource {
		final EntityManagerFactory emf;
		EntityManager em;
		EntityTransaction t;
		public EntityManagerFactoryWrapper(EntityManagerFactory emf) {
			this.emf = emf;
		}
		@Override
		public void close() throws Throwable {
			if (t != null && t.isActive()) {
				t.rollback();
			}
			if (em != null && em.isOpen()) {
				em.close();
			}
			emf	.close();
		}
	}

}
