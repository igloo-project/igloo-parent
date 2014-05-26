package fr.openwide.core.wicket.more.condition;

import org.apache.wicket.Component;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.acls.model.Permission;

import com.google.common.base.Predicate;

import fr.openwide.core.commons.util.functional.Predicates2;
import fr.openwide.core.jpa.security.service.IAuthenticationService;
import fr.openwide.core.wicket.more.AbstractCoreSession;

public abstract class Condition implements IModel<Boolean>, IDetachable {
	
	private static final long serialVersionUID = -3315852580233582804L;

	public abstract boolean applies();
	
	/**
	 * @deprecated Provided only to satisfy the {@link IModel} interface. Use {@link #applies()} instead.
	 */
	@Override
	@Deprecated
	public final Boolean getObject() {
		return applies();
	}

	/**
	 * @deprecated Provided only to satisfy the {@link IModel} interface. Not supported.
	 */
	@Override
	@Deprecated
	public void setObject(Boolean object) {
		throw new UnsupportedOperationException("Cannot set the value of a condition");
	}

	@Override
	public void detach() { }
	
	public static Condition not(Condition condition) {
		return new NotCondition(condition);
	}
	
	private static class NotCondition extends Condition {
		private static final long serialVersionUID = 1L;

		private final Condition condition;
		
		public NotCondition(Condition condition) {
			super();
			this.condition = condition;
		}
		
		@Override
		public boolean applies() {
			return !condition.applies();
		}
		
		@Override
		public void detach() {
			super.detach();
			condition.detach();
		}
	}
	
	public static <T> Condition predicate(IModel<? extends T> model, Predicate<? super T> predicate) {
		return new PredicateCondition<>(model, predicate);
	}
	
	private static class PredicateCondition<T> extends Condition {
		private static final long serialVersionUID = 1L;

		private final IModel<? extends T> model;
		private final Predicate<? super T> predicate;
		
		public PredicateCondition(IModel<? extends T> model, Predicate<? super T> predicate) {
			super();
			this.model = model;
			this.predicate = predicate;
		}
		
		@Override
		public boolean applies() {
			return predicate.apply(model.getObject());
		}
		
		@Override
		public void detach() {
			super.detach();
			if (predicate instanceof IDetachable) {
				((IDetachable)predicate).detach();
			}
			model.detach();
		}
	}
	
	/**
	 * @see Predicates2#isTrue()
	 */
	public static Condition isTrue(IModel<Boolean> model) {
		return predicate(model, Predicates2.isTrue());
	}

	/**
	 * @see Predicates2#isTrueOrNull()
	 */
	public static Condition isTrueOrNull(IModel<Boolean> model) {
		return predicate(model, Predicates2.isTrueOrNull());
	}

	/**
	 * @see Predicates2#isFalse()
	 */
	public static Condition isFalse(IModel<Boolean> model) {
		return predicate(model, Predicates2.isFalse());
	}

	/**
	 * @see Predicates2#isFalseOrNull()
	 */
	public static Condition isFalseOrNull(IModel<Boolean> model) {
		return predicate(model, Predicates2.isFalseOrNull());
	}
	
	public static Condition visible(Component component) {
		return new VisibleCondition(component);
	}
	
	private static class VisibleCondition extends Condition {
		private static final long serialVersionUID = 1L;
		
		private final Component component;
		
		public VisibleCondition(Component component) {
			super();
			this.component = component;
		}
		
		@Override
		public boolean applies() {
			component.configure();
			return component.determineVisibility();
		}
	}
	
	public static Condition role(String role) {
		return new RoleCondition(role);
	}
	
	private static class RoleCondition extends Condition {
		private static final long serialVersionUID = 1L;
		
		private final String roleName;
		
		public RoleCondition(String roleName) {
			super();
			this.roleName = roleName;
		}
		
		@Override
		public boolean applies() {
			return AbstractCoreSession.get().hasRole(roleName);
		}
	}
	
	public static Condition permission(Permission permission) {
		return new GlobalPermissionCondition(permission);
	}
	
	private static class GlobalPermissionCondition extends Condition {
		private static final long serialVersionUID = 1L;
		
		private final Permission permission;
		
		public GlobalPermissionCondition(Permission permission) {
			super();
			this.permission = permission;
		}
		
		@Override
		public boolean applies() {
			return AbstractCoreSession.get().hasPermission(permission);
		}
	}
	public static Condition permission(IModel<?> securedObjectModel, Permission permission) {
		return new ObjectPermissionCondition(securedObjectModel, permission);
	}
	
	private static class ObjectPermissionCondition extends Condition {
		private static final long serialVersionUID = 1L;
		
		@SpringBean
		private IAuthenticationService authenticationService;
		
		private final IModel<?> securedObjectModel;
		
		private final Permission permission;
		
		public ObjectPermissionCondition(IModel<?> securedObjectModel, Permission permission) {
			super();
			this.securedObjectModel = securedObjectModel;
			this.permission = permission;
			Injector.get().inject(this);
		}
		
		@Override
		public boolean applies() {
			return authenticationService.hasPermission(securedObjectModel.getObject(), permission);
		}
		
		@Override
		public void detach() {
			super.detach();
			securedObjectModel.detach();
		}
	}
}
