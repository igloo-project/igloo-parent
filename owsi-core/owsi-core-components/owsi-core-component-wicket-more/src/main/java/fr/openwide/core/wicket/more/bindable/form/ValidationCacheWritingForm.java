package fr.openwide.core.wicket.more.bindable.form;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.wicket.bean.validation.BeanValidationConfiguration;
import org.apache.wicket.bean.validation.GroupsModel;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.javatuples.KeyValue;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import fr.openwide.core.commons.util.fieldpath.FieldPath;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.validator.constraint.util.ConstraintViolationUtils;
import fr.openwide.core.wicket.more.bindable.model.BindableModel;
import fr.openwide.core.wicket.more.bindable.model.IBindableModel;
import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.WorkingCopyModel;
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
				if (model == null) {
					return;
				}
				
				FieldPath completeFieldPath;
				
				if (model instanceof BindingModel) {
					completeFieldPath = ((BindingModel<?, ?>) object.getModel()).getFieldPath();
				} else if (model instanceof BindableModel) {
					completeFieldPath = ((BindableModel<?>) object.getModel()).getFieldPath();
				} else {
					return;
				}
				
				if (violationsByFieldPath.containsKey(completeFieldPath)) {
					for (KeyValue<ConstraintViolationImpl<V>, List<KeyValue<FieldPath, Object>>> constraintViolationInfo : violationsByFieldPath.get(completeFieldPath)) {
						if (isMatchingPath(model, constraintViolationInfo, completeFieldPath)) {
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
		
		error("error");
	}

	protected Set<ConstraintViolation<V>> validate(Validator validator, V validationBean, Class<?>[] groups) {
		return validator.validate(validationBean, groups);
	}

	private boolean isMatchingPath(IModel<?> model, KeyValue<ConstraintViolationImpl<V>, List<KeyValue<FieldPath, Object>>> constraintViolationInfo, FieldPath completeFieldPath) {
		FieldPath fieldPath = FieldPath.of(completeFieldPath);
		
		List<KeyValue<FieldPath, Object>> nodes = Lists.newArrayList(constraintViolationInfo.getValue());
		
		IModel<?> currentModel = model;
		
		// for a field.child.grandchildren, we check the model:
		// - does the model target same object than constraintValidation ?
		// - does the model target same path than constraintValidation ?
		// we try to test this 2 conditions for each step in the field path
		// 1. is constraintValidation.value == grandchildren and model targets a field.child.grandchildren element
		// 1b. if yes, node = node.parent, path = field.child
		// 2. is node.value == child and model targets a field.child element
		// 2b. if yes, node = node.parent, path = field
		// 3. is node.value == field and model targets a field element
		// 3b. if yes, node = node.parent, path = field
		// 4. is node.value == ROOT and model targets a ROOT path
		// 4b. if yes, bingo!
		
		// NOTA: currentModel == null is encountered:
		// 1. When a collection item is encountered during path traversal, model cannot provide parent model (i.e. model
		// that provides collection item). In this case,
		// result = collectionItem.getObject() == node.value and path == node.path
		// 2. ROOT model is encountered
		while (currentModel != null) {
			if (nodes == null || fieldPath == null) {
				return false;
			}
			
			// unwrap model to access a model container that provides field path information (if possible)
			if (currentModel instanceof BindableModel) {
				currentModel = ((BindableModel<?>) model).getDelegateModel();
				
				if (currentModel instanceof WorkingCopyModel) {
					currentModel = ((WorkingCopyModel<?>) currentModel).getReferenceModel();
				}
			}
			
			// get IModel as a PropertyModel if possible
			AbstractPropertyModel<?> currentModelAsPropertyModel = null;
			if (currentModel instanceof AbstractPropertyModel) {
				currentModelAsPropertyModel = (AbstractPropertyModel<?>) currentModel;
			}
			
			// get path from PropertyModel (root.fieldName)
			FieldPath expressionFieldPath = FieldPath.ROOT;
			if (currentModelAsPropertyModel != null) {
				expressionFieldPath = FieldPath.fromString(currentModelAsPropertyModel.getPropertyExpression());
			} else if (fieldPath != null && !fieldPath.isRoot()) {
				expressionFieldPath = ConstraintViolationUtils.getCurrentFieldPath(fieldPath);
			}
			
			// if no path, introspection is not possible
			if (expressionFieldPath == null) {
				return false;
			}
			
			Object currentModelObject = currentModel.getObject();
			// pass from field1.children.grandchildren to field1.children and keep and check grandchildren (=node)
			KeyValue<FieldPath, Object> node = nodes.remove(0);
			
			// consistency check
			if (!node.getKey().equals(ConstraintViolationUtils.getCurrentFieldPath(expressionFieldPath))) {
				return false;
			}
			
			if (!isEquals(currentModelObject, node.getValue())) {
				return false;
			}
			
			// mimic nodes.remove(0) operation on the other context variables
			// in case we continue while loop
			fieldPath = fieldPath.relativeFrom(node.getKey()).orNull();
			expressionFieldPath = expressionFieldPath.relativeFrom(node.getKey()).orNull();
			
			if (currentModelAsPropertyModel != null) {
				// Deal with parent paths from BindingModel without models to check: as binding model can stack
				// multiple field step (example: field.child.grandchildren), we need to cut more fieldPath,
				// expressionFieldPath and nodes levels.
				while (expressionFieldPath != null && !expressionFieldPath.isRoot() && !nodes.isEmpty() && fieldPath !=  null) {
					KeyValue<FieldPath, Object> currentNode = nodes.remove(0);
					
					// consistency check
					if (!currentNode.getKey().equals(ConstraintViolationUtils.getCurrentFieldPath(expressionFieldPath))) {
						return false;
					}
					
					fieldPath = fieldPath.relativeFrom(currentNode.getKey()).orNull();
					expressionFieldPath = expressionFieldPath.relativeFrom(currentNode.getKey()).orNull();
				}
			}
			
			// we step a model higher in the path to continue iteration
			if (currentModelAsPropertyModel != null) {
				currentModel = currentModelAsPropertyModel.getChainedModel();
			} else {
				currentModel = null;
			}
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