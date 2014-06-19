package fr.openwide.core.wicket.more.condition;

import java.util.Arrays;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

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
	
	public Condition or(Condition operand) {
		return composite(BooleanOperator.OR, this, operand);
	}
	
	public Condition nor(Condition operand) {
		return composite(BooleanOperator.NOR, this, operand);
	}
	
	public Condition and(Condition operand) {
		return composite(BooleanOperator.AND, this, operand);
	}
	
	public Condition nand(Condition operand) {
		return composite(BooleanOperator.NAND, this, operand);
	}
	
	public Condition negate() {
		return new NegateCondition(this);
	}
	
	private static class NegateCondition extends Condition {
		private static final long serialVersionUID = 1L;

		private final Condition condition;
		
		public NegateCondition(Condition condition) {
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
	
	public static Condition composite(BooleanOperator operator, Condition ... operands) {
		return new CompositeCondition(operator, Arrays.asList(operands));
	}
	
	public static Condition composite(BooleanOperator operator, Iterable<? extends Condition> operands) {
		return new CompositeCondition(operator, operands);
	}
	
	private static class CompositeCondition extends Condition {
		private static final long serialVersionUID = 1L;
		
		private final BooleanOperator operator;

		private final Iterable<? extends Condition> operands;
		
		public CompositeCondition(BooleanOperator operator, Iterable<? extends Condition> operands) {
			super();
			this.operator = operator;
			this.operands = ImmutableList.copyOf(operands);
		}

		@Override
		public boolean applies() {
			return operator.apply(operands);
		}
		
		@Override
		public void detach() {
			super.detach();
			for (Condition operand : operands) {
				operand.detach();
			}
		}
	}
	
	public static Condition constant(boolean value) {
		return value ? alwaysTrue() : alwaysFalse();
	}
	
	public static Condition alwaysTrue() {
		return ConstantCondition.ALWAYS_TRUE;
	}
	
	public static Condition alwaysFalse() {
		return ConstantCondition.ALWAYS_FALSE;
	}
	
	private static class ConstantCondition extends Condition {
		private static final long serialVersionUID = -7678144550356610455L;
		
		private static ConstantCondition ALWAYS_TRUE = new ConstantCondition(true) {
			private static final long serialVersionUID = -8786829515620843503L;
			private Object readResolve() {
				return ALWAYS_TRUE;
			}
		};
		private static ConstantCondition ALWAYS_FALSE = new ConstantCondition(false) {
			private static final long serialVersionUID = -6055735778127387150L;
			private Object readResolve() {
				return ALWAYS_FALSE;
			}
		};
		
		private final boolean value;

		public ConstantCondition(boolean value) {
			super();
			this.value = value;
		}
		
		@Override
		public boolean applies() {
			return value;
		}
		
		@Override
		public String toString() {
			return String.valueOf(value);
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
	
	public static Condition anyChildVisible(MarkupContainer container) {
		return new AnyChildVisibleCondition(container);
	}
	
	private static class AnyChildVisibleCondition extends Condition {
		private static final long serialVersionUID = 1L;
		
		private final MarkupContainer container;
		
		public AnyChildVisibleCondition(MarkupContainer container) {
			super();
			this.container = container;
		}
		
		@Override
		public boolean applies() {
			for (Component child : container) {
				child.configure();
				if (child.determineVisibility()) {
					return true;
				}
			}
			return false;
		}
	}
	
	public static Condition role(String role) {
		return new AnyRoleCondition(ImmutableList.of(role));
	}
	
	public static Condition anyRole(String role, String ... otherRoles) {
		return new AnyRoleCondition(Lists.asList(role, otherRoles));
	}
	
	private static class AnyRoleCondition extends Condition {
		private static final long serialVersionUID = 1L;
		
		private final Iterable<String> roleNames;
		
		public AnyRoleCondition(Iterable<String> roleNames) {
			super();
			this.roleNames = ImmutableSet.copyOf(roleNames);
		}
		
		@Override
		public boolean applies() {
			for (String roleName : roleNames) {
				if (AbstractCoreSession.get().hasRole(roleName)) {
					return true;
				}
			}
			return false;
		}
	}
	
	public static Condition permission(String permissionName) {
		return new AnyGlobalPermissionCondition(ImmutableList.of(permissionName));
	}
	
	public static Condition anyPermission(String permissionName, String ... otherPermissionNames) {
		return new AnyGlobalPermissionCondition(Lists.asList(permissionName, otherPermissionNames));
	}
	
	public static Condition permission(Permission permission) {
		return new AnyGlobalPermissionCondition(permission);
	}
	
	private static class AnyGlobalPermissionCondition extends Condition {
		private static final long serialVersionUID = 1L;
		
		@SpringBean
		private PermissionFactory permissionFactory;
		
		@SpringBean
		private IAuthenticationService authenticationService;
		
		private final Iterable<Permission> permissions;
		
		public AnyGlobalPermissionCondition(Iterable<String> permissionNames) {
			super();
			Injector.get().inject(this);
			this.permissions = permissionFactory.buildFromNames(ImmutableList.copyOf(permissionNames));
		}
		
		public AnyGlobalPermissionCondition(Permission permission) {
			super();
			Injector.get().inject(this);
			this.permissions = ImmutableList.of(permission);
		}
		
		@Override
		public boolean applies() {
			for (Permission permission : permissions) {
				if (authenticationService.hasPermission(permission)) {
					return true;
				}
			}
			return false;
		}
	}
	
	public static Condition permission(IModel<?> securedObjectModel, String permissionName) {
		return new AnyObjectPermissionCondition(securedObjectModel, ImmutableList.of(permissionName));
	}
	
	public static Condition anyPermission(IModel<?> securedObjectModel, String permissionName, String ... otherPermissionNames) {
		return new AnyObjectPermissionCondition(securedObjectModel, Lists.asList(permissionName, otherPermissionNames));
	}
	
	public static Condition permission(IModel<?> securedObjectModel, Permission permission) {
		return new AnyObjectPermissionCondition(securedObjectModel, permission);
	}
	
	private static class AnyObjectPermissionCondition extends Condition {
		private static final long serialVersionUID = 1L;
		
		@SpringBean
		private PermissionFactory permissionFactory;
		
		@SpringBean
		private IAuthenticationService authenticationService;
		
		private final IModel<?> securedObjectModel;
		
		private final Iterable<Permission> permissions;
		
		public AnyObjectPermissionCondition(IModel<?> securedObjectModel, Iterable<String> permissionNames) {
			super();
			Injector.get().inject(this);
			this.securedObjectModel = securedObjectModel;
			this.permissions = permissionFactory.buildFromNames(ImmutableList.copyOf(permissionNames));
		}
		
		public AnyObjectPermissionCondition(IModel<?> securedObjectModel, Permission permission) {
			super();
			Injector.get().inject(this);
			this.securedObjectModel = securedObjectModel;
			this.permissions = ImmutableList.of(permission);
		}
		
		@Override
		public boolean applies() {
			Object securedObject = securedObjectModel.getObject();
			for (Permission permission : permissions) {
				if (authenticationService.hasPermission(securedObject, permission)) {
					return true;
				}
			}
			return false;
		}
		
		@Override
		public void detach() {
			super.detach();
			securedObjectModel.detach();
		}
	}
}
