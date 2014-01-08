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

import org.apache.wicket.model.AbstractPropertyModel;
import org.apache.wicket.model.IModel;
import org.bindgen.Binding;
import org.bindgen.BindingRoot;

/**
 * An improved and simplified version of the BindingModel implementation of
 * wicket-bindgen.
 * 
 * It is based on PropertyModel instead of using bindgen features because
 * bindgen behavior is not the same as Wicket one. Typically, when an
 * object is null in the path, Wicket tries to instantiate using the default
 * constructor which allows to have cost().quantity() working even if
 * cost property is null.
 * 
 * @author igor.vaynberg
 * @author Open Wide
 * 
 * @param <R>
 *            type of object that is the root of the expression
 * @param <T>
 *            type of object returned by the expression
 */
public class BindingModel<R, T> extends AbstractPropertyModel<T> {
	private static final long serialVersionUID = -4018038300151228083L;

	private final Binding<T> binding;
	
	public BindingModel(IModel<? extends R> root, BindingRoot<R, T> binding) {
		super(root);
		this.binding = binding;
	}
	
	/**
	 * Convenience method to convert a {@link BindingRoot} into a
	 * {@link BindingModel}
	 * 
	 * @param <T>
	 * @param binding
	 * @return binding model for {@code binding}
	 */
	public static <R, T> BindingModel<R, T> of(IModel<? extends R> root, BindingRoot<R, T> binding) {
		return new BindingModel<R, T>(root, binding);
	}

	@Override
	protected String propertyExpression() {
		return binding.getPath();
	}

}
