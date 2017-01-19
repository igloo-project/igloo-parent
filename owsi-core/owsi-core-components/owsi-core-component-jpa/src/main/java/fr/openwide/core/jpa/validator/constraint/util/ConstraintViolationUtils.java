package fr.openwide.core.jpa.validator.constraint.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path.Node;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.javatuples.KeyValue;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import fr.openwide.core.commons.util.fieldpath.FieldPath;
import fr.openwide.core.commons.util.fieldpath.FieldPathComponent;
import fr.openwide.core.commons.util.functional.SerializablePredicate;

public class ConstraintViolationUtils {

	public static final String CLASS_LEVEL_CONSTRAINT_PROPERTY_PATH = "__OW__CLASS_LEVEL_CONSTRAINT_PROPERTY_PATH";


	public static <T, V> Multimap<FieldPath, KeyValue<ConstraintViolationImpl<V>, List<KeyValue<FieldPath, Object>>>> getInfo(
			T rootBean,
			V validationBean,
			Iterable<ConstraintViolation<V>> violations
	) {
		ImmutableListMultimap.Builder<FieldPath, KeyValue<ConstraintViolationImpl<V>, List<KeyValue<FieldPath, Object>>>> builder =
				ImmutableListMultimap.<FieldPath, KeyValue<ConstraintViolationImpl<V>, List<KeyValue<FieldPath, Object>>>>builder();
		
		for (ConstraintViolation<V> constraintViolation : violations) {
			ConstraintViolationImpl<V> violation = (ConstraintViolationImpl<V>) constraintViolation;
			
			List<KeyValue<FieldPath, Object>> nodes = listNodes(rootBean, validationBean, violation);
			
			builder.put(getFieldPath(validationBean, constraintViolation), KeyValue.<ConstraintViolationImpl<V>, List<KeyValue<FieldPath, Object>>>with(violation, nodes));
		}
		
		return builder.build();
	}

	@SuppressWarnings("unchecked")
	private static <V> FieldPath getFieldPath(V validationBean, ConstraintViolation<V> constraintViolation) {
		ConstraintViolationImpl<?> constraintViolationImpl = (ConstraintViolationImpl<?>) constraintViolation;
		
		FieldPath fieldPath = Optional.fromNullable(
				FieldPath.fromString(constraintViolationImpl.getPropertyPath().toString().replaceAll("\\[.*\\]", "\\[\\*\\]"))
		).or(FieldPath.ROOT);
		
		if (constraintViolationImpl.getExpressionVariables().get(CLASS_LEVEL_CONSTRAINT_PROPERTY_PATH) != null) {
			fieldPath = fieldPath.append(
					FieldPath.of(
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
			);
		}
		
		if (validationBean instanceof IValidationBean) {
			Map<FieldPath, List<KeyValue<FieldPath, Object>>> reelRootPaths = ((IValidationBean) validationBean).getReelRootPaths();
			
			for (Entry<FieldPath, List<KeyValue<FieldPath, Object>>> entry : reelRootPaths.entrySet()) {
				if (fieldPath.startsWith(entry.getKey())) {
					FieldPath newRootFieldPath = FieldPath.of();
					for (KeyValue<FieldPath, Object> fieldPathEntry : entry.getValue()) {
						newRootFieldPath.append(fieldPathEntry.getKey());
					}
					fieldPath = fieldPath.relativeTo(entry.getKey()).get().compose(newRootFieldPath);
				}
			}
		}
		
		return fieldPath;
	}

	@SuppressWarnings("unchecked")
	private static <T, V> List<KeyValue<FieldPath, Object>> listNodes(T rootBean, V validationBean, ConstraintViolation<?> constraintViolation) {
		ConstraintViolationImpl<?> constraintViolationImpl = (ConstraintViolationImpl<?>) constraintViolation;
		
		List<KeyValue<FieldPath, Object>> nodes = Lists.newArrayList();
		
		if (constraintViolationImpl.getExpressionVariables().containsKey(CLASS_LEVEL_CONSTRAINT_PROPERTY_PATH)) {
			nodes.addAll(Lists.reverse(Lists.newArrayList((List<KeyValue<FieldPath, Object>>) constraintViolationImpl.getExpressionVariables().get(CLASS_LEVEL_CONSTRAINT_PROPERTY_PATH))));
		}
		
		List<Node> propertyNodes = Lists.newArrayList(constraintViolation.getPropertyPath());
		
		Iterables.removeIf(
				propertyNodes,
				new SerializablePredicate<Node>() {
					private static final long serialVersionUID = 1L;
					@Override
					public boolean apply(Node input) {
						return ElementKind.BEAN.equals(input.getKind());
					}
				}
		);
		
		nodes.addAll(
				Lists.transform(
						Lists.reverse(propertyNodes),
						new Function<Node, KeyValue<FieldPath, Object>>() {
							@Override
							public KeyValue<FieldPath, Object> apply(Node input) {
								NodeImpl nodeImpl = (NodeImpl) input;
								return KeyValue.<FieldPath, Object>with(getFieldPath(nodeImpl), nodeImpl.getValue());
							}
						}
				)
		);
		
		KeyValue<FieldPath, Object> currentLastNode = Iterables.getLast(nodes, null);
		if (currentLastNode != null && currentLastNode.getKey() != null && validationBean instanceof IValidationBean) {
			Map<FieldPath, List<KeyValue<FieldPath, Object>>> reelRootPaths = ((IValidationBean) validationBean).getReelRootPaths();
			
			if (reelRootPaths.containsKey(currentLastNode.getKey())) {
				nodes.remove(currentLastNode);
				nodes.addAll(Lists.reverse(reelRootPaths.get(currentLastNode.getKey())));
			}
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
