/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.openwide.core.wicket.more.model;

import org.apache.wicket.model.IModel;
import org.bindgen.Binding;
import org.bindgen.BindingRoot;

/**
 * An improved and simplified version of the BindingModel implementation of
 * wicket-bindgen.
 * 
 * @author igor.vaynberg
 * @author Open Wide
 * 
 * @param <R>
 *            type of object that is the root of the expression
 * @param <T>
 *            type of object returned by the expression
 */
public class BindingModel<R, T> implements IModel<T> {
	private static final long serialVersionUID = -4018038300151228083L;

	private final Binding<T> binding;
	
	private final IModel<? extends R> root;

	public BindingModel(IModel<? extends R> root, BindingRoot<R, T> binding) {
		this.root = root;
		this.binding = binding;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public T getObject() {
		if (root == null) {
			return binding.get();
		} else {
			return ((BindingRoot<R, T>) binding).getSafelyWithRoot(root.getObject());
		}
	}

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unchecked")
	public void setObject(T object) {
		try {
			if (root == null) {
				binding.set(object);
			} else {
				((BindingRoot<R, T>) binding).setWithRoot(root.getObject(), object);
			}
		} catch (NullPointerException e) {
			// we ignore NPE like Wicket does with its own implementation of PropertyModel.
		}
	}

	/** {@inheritDoc} */
	@Override
	public void detach() {
		if (root != null) {
			root.detach();
		}
	}

	/**
	 * Convenience method to convert a {@link BindingRoot} into a
	 * {@link BindingRootModel}
	 * 
	 * @param <T>
	 * @param binding
	 * @return binding model for {@code binding}
	 */
	public static <R, T> IModel<T> of(IModel<? extends R> root, BindingRoot<R, T> binding) {
		return new BindingModel<R, T>(root, binding);
	}

}
