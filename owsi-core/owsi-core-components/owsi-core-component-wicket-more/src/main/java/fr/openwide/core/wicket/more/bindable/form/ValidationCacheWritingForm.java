package fr.openwide.core.wicket.more.bindable.form;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.wicket.bean.validation.BeanValidationConfiguration;
import org.apache.wicket.bean.validation.GroupsModel;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.javatuples.KeyValue;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import fr.openwide.core.commons.util.fieldpath.FieldPath;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.validator.constraint.util.ConstraintViolationUtils;
import fr.openwide.core.wicket.more.bindable.model.BindableModel;
import fr.openwide.core.wicket.more.bindable.model.IBindableModel;
import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;
import fr.openwide.core.wicket.more.util.model.Detachables;

public class ValidationCacheWritingForm<M extends IBindableModel<T>, T, V> extends CacheWritingForm<T> {

	private static final long serialVersionUID = -3052046009127345521L;

	private final M bindableModel;

	private final IDetachableFactory<M, V> validationBeanFactory;

	private final IModel<Class<?>[]> groupsModel;

	public ValidationCacheWritingForm(String id, M bindableModel, IDetachableFactory<M, V> validationBeanFactory) {
		this(id, bindableModel, validationBeanFactory, new GroupsModel());
	}

	public ValidationCacheWritingForm(String id, M bindableModel, IDetachableFactory<M, V> validationBeanFactory, IModel<Class<?>[]> groupsModel) {
		super(id, bindableModel);
		this.bindableModel = Objects.requireNonNull(bindableModel);
		this.validationBeanFactory = Objects.requireNonNull(validationBeanFactory);
		this.groupsModel = Objects.requireNonNull(groupsModel);
	}

	@Override
	protected void onValidateModelObjects() {
		super.onValidateModelObjects();
		
		V validationBean = validationBeanFactory.create(bindableModel);
		
		Validator validator = BeanValidationConfiguration.get().getValidator();
		Set<ConstraintViolation<V>> violations = validate(validator, validationBean, groupsModel.getObject());
		
		if (violations.isEmpty()) {
			return; // success \o/
		}
		
		Multimap<FieldPath, KeyValue<ConstraintViolationImpl<V>, List<KeyValue<FieldPath, Object>>>> violationsByFieldPath =
				ConstraintViolationUtils.getInfo(getModelObject(), validationBean, violations);
		
		Set<ConstraintViolation<V>> violationsOnForm = Sets.newLinkedHashSet(violations);
		
		visitChildren(FormComponent.class, new IVisitor<FormComponent<?>, Void>() {
			@Override
			public void component(FormComponent<?> object, IVisit<Void> visit) {
				IModel<?> model = object.getModel();
				
				if (model == null || !(model instanceof BindableModel)) {
					return;
				}
				
				BindableModel<?> bindableModel = (BindableModel<?>) model;
				
				FieldPath fieldPath = bindableModel.getFieldPath();
				
				if (violationsByFieldPath.containsKey(fieldPath)) {
					for (KeyValue<ConstraintViolationImpl<V>, List<KeyValue<FieldPath, Object>>> constraintViolationInfo : violationsByFieldPath.get(fieldPath)) {
						if (isMatchingPath(bindableModel, constraintViolationInfo)) {
							object.error(constraintViolationInfo.getKey().getMessage());
							violationsOnForm.remove(constraintViolationInfo.getKey());
						}
					}
				}
			}
		});
		
		for (ConstraintViolation<V> violationOnForm : violationsOnForm) {
			error(violationOnForm.getMessage());
		}
	}

	protected Set<ConstraintViolation<V>> validate(Validator validator, V validationBean, Class<?>[] groups) {
		return validator.validate(validationBean, groups);
	}

	private boolean isMatchingPath(BindableModel<?> bindableModel, KeyValue<ConstraintViolationImpl<V>, List<KeyValue<FieldPath, Object>>> constraintViolationInfo) {
		Iterator<KeyValue<FieldPath, Object>> nodesFromConstraint = constraintViolationInfo.getValue().iterator();
		Iterator<KeyValue<FieldPath, Object>> nodesFromBindableModel = bindableModel.listNodes().iterator();
		
		while (nodesFromConstraint.hasNext() && nodesFromBindableModel.hasNext()) {
			KeyValue<FieldPath, Object> nodeFormConstraint = nodesFromConstraint.next();
			KeyValue<FieldPath, Object> nodeFromBindableModel = nodesFromBindableModel.next();
			
			if (
					nodeFormConstraint == null
				||	nodeFromBindableModel == null
				||	nodeFormConstraint.getKey() == null
				||	nodeFromBindableModel.getKey() == null
				||	!Objects.equals(nodeFormConstraint.getKey(), nodeFromBindableModel.getKey())
				||	!isEquals(nodeFormConstraint.getValue(), nodeFromBindableModel.getValue())
			) {
				return false;
			}
		}
		
		if (nodesFromConstraint.hasNext() || nodesFromBindableModel.hasNext()) {
			return false;
		}
		
		return true;
	}

	private boolean isEquals(Object left, Object right) {
		if (
				((left instanceof GenericEntity) && (right instanceof GenericEntity))
			&&	(!((GenericEntity<?, ?>) left).isNew() || !((GenericEntity<?, ?>) right).isNew())
			&&	!left.equals(right)
		) {
			return false;
		} else if (left != right) {
			return false;
		}
		return true;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(bindableModel, validationBeanFactory, groupsModel);
	}

}