package fr.openwide.core.jpa.validator.constraint.util;

import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.Path.Node;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.javatuples.KeyValue;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.fieldpath.FieldPath;
import fr.openwide.core.commons.util.fieldpath.FieldPathComponent;

public class ConstraintViolationUtils {

	public static final String CLASS_LEVEL_CONSTRAINT_PROPERTY_PATH = "__OW__CLASS_LEVEL_CONSTRAINT_PROPERTY_PATH";

	@SuppressWarnings("unchecked")
	public static FieldPath getFieldPath(ConstraintViolation<?> constraintViolation) {
		ConstraintViolationImpl<?> constraintViolationImpl = (ConstraintViolationImpl<?>) constraintViolation;
		
		return Optional.fromNullable(
				constraintViolationImpl.getExpressionVariables().get(CLASS_LEVEL_CONSTRAINT_PROPERTY_PATH) != null
						? FieldPath.of(
								Iterables.concat(
										Iterables.transform(
												(List<KeyValue<FieldPath, Object>>) constraintViolationImpl.getExpressionVariables().get(CLASS_LEVEL_CONSTRAINT_PROPERTY_PATH),
												new Function<KeyValue<FieldPath, Object>, List<FieldPathComponent>>() {
													@Override
													public List<FieldPathComponent> apply(KeyValue<FieldPath, Object> input) {
														return Lists.newArrayList(input.getKey().iterator());
													}
												}
										)
								)
						)
						: FieldPath.fromString(constraintViolationImpl.getPropertyPath().toString().replaceAll("\\[[0-9]*\\]", "\\[\\*\\]"))
		).or(FieldPath.ROOT);
	}

	@SuppressWarnings("unchecked")
	public static List<KeyValue<FieldPath, Object>> listNodes(Object rootBean, ConstraintViolation<?> constraintViolation) {
		ConstraintViolationImpl<?> constraintViolationImpl = (ConstraintViolationImpl<?>) constraintViolation;
		
		List<KeyValue<FieldPath, Object>> nodes = Lists.newArrayList();
		
		if (constraintViolationImpl.getExpressionVariables().containsKey(CLASS_LEVEL_CONSTRAINT_PROPERTY_PATH)) {
			nodes.addAll(Lists.reverse(Lists.newArrayList((List<KeyValue<FieldPath, Object>>) constraintViolationImpl.getExpressionVariables().get(CLASS_LEVEL_CONSTRAINT_PROPERTY_PATH))));
		} else {
			nodes.addAll(Lists.transform(
					Lists.reverse(Lists.newArrayList(constraintViolation.getPropertyPath())),
					new Function<Node, KeyValue<FieldPath, Object>>() {
						@Override
						public KeyValue<FieldPath, Object> apply(Node input) {
							NodeImpl nodeImpl = (NodeImpl) input;
							return KeyValue.<FieldPath, Object>with(getFieldPath(nodeImpl), nodeImpl.getValue());
						}
					}
			));
		}
		nodes.add(KeyValue.<FieldPath, Object>with(FieldPath.ROOT, rootBean));
		
		return nodes;
	}

	public static FieldPath getFieldPath(NodeImpl nodeImpl) {
		if (nodeImpl == null) {
			return null;
		}
		
		FieldPath fieldPath = FieldPath.fromString(nodeImpl.getName());
		
		if (fieldPath == null) {
			return fieldPath;
		}
		
		if (nodeImpl.isIterable()) {
			fieldPath = fieldPath.item();
		}
		
		return fieldPath;
	}

	public static FieldPath getCurrentFieldPath(FieldPath fieldPath) {
		if (fieldPath == null) {
			return null;
		}
		
		if (fieldPath.isRoot()) {
			return fieldPath;
		}
		
		if (fieldPath.isItem()) {
			return fieldPath.parent().get().relativeToParent().get().item();
		} else {
			return fieldPath.relativeToParent().get();
		}
	}

	private ConstraintViolationUtils() {
	}

}
