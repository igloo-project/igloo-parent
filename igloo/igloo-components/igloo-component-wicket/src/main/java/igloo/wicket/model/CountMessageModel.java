/**
 * Copyright 2012 55 Minutes (http://www.55minutes.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package igloo.wicket.model;

import java.io.Serializable;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Args;

/**
 * Selects an appropriate localized string based on whether a count value is
 * zero, one or greater than one. This makes it easier to produce singluar and
 * plural phrases.
 * <p>
 * For example, consider the case where we would like to set one of these three
 * feedback messages. Notice how the phrasing is slightly different depending on
 * the number of items. Furthermore in the latter two examples we would like to
 * embed a hyperlink to a bookmarkable page.
 * <ul>
 * <li>There are no friends in your network.</li>
 * <li>There is <u>one friend</u> in your network.</li>
 * <li>There are <u>5 friends</u> in your network.</li>
 * </ul>
 * <p>
 * Normally in Wicket you would accomplish this using tedious if/else statements
 * and string concatenation, conditionally visible fragments/containers, or the
 * incredibly arcane syntax of Java's {@link java.text.ChoiceFormat
 * ChoiceFormat} (and note that you'd have to carefully escape the "&lt;" and
 * "&gt;" if you want to include HTML markup as we need for the hyperlinks in
 * this example). All of these approaches are clumsy, and it would be hard for a
 * non-programmer to localize or edit the strings.
 * <p>
 * You can now use this model in a label (also consider
 * {@link fiftyfive.wicket.basic.CountLabel CountLabel} as a shortcut for this
 * common use case), or call {@link #getObject getObject()} to get the
 * interpolated string value for use in a feedback message.
 * 
 * @since 2.0
 */
public class CountMessageModel implements IComponentAssignedModel<String> {
	private static final long serialVersionUID = 3717960938783720989L;

	private String messageKey;
	private IModel<? extends Number> countModel;

	/**
	 * Constructs a CountMessageModel that will choose the appropriate localized
	 * string from a properties file.
	 * 
	 * @param messageKey
	 *            The key that will be consulted in the properties file. The key
	 *            will have ".zero", ".one" or ".many" appended to it depending
	 *            on the value of the {@code count}.
	 * @param component
	 *            The Wicket component that will be used for finding the
	 *            properties file. Usually this is the page or panel where you
	 *            plan to use the model.
	 * @param count
	 *            The number that will be used in the message. This will be used
	 *            to substitute any <code>${count}</code> expressions in the
	 *            message.
	 */
	public CountMessageModel(String messageKey, IModel<? extends Number> count) {
		super();
		Args.notNull(messageKey, "messageKey");
		Args.notNull(count, "count");
		
		this.messageKey = messageKey;
		this.countModel = count;
	}
	
	@Override
	public IWrapModel<String> wrapOnAssignment(Component component) {
		return new AssignmentWrapper(component);
	}

	@Override
	public void detach() {
		IComponentAssignedModel.super.detach();
		this.countModel.detach();
	}

	@Override
	public String getObject() {
		// this shouldn't be called in the Wicket environment BUT we sometimes use this in Excel exports
		return Application.get().getResourceSettings().getLocalizer().getString(getResourceKey(), null,
				Model.of(getPropertySubstitutionBean()));
	}

	public int getCount() {
		Number num = this.countModel.getObject();
		return num != null ? num.intValue() : 0;
	}

	public String getResourceKey() {
		StringBuilder sb = new StringBuilder();
		sb.append(messageKey);
		sb.append(getResourceSuffix());
		return sb.toString();
	}
	
	/**
	 * Returns either ".zero", ".one", or ".many" depending on the current value
	 * of the count model.
	 */
	public String getResourceSuffix() {
		int count = getCount();

		if (0 == count) {
			return ".zero";
		} else if (1 == count) {
			return ".one";
		} else {
			return ".many";
		}
	}
	
	public PropertySubstitutionBean getPropertySubstitutionBean() {
		return new PropertySubstitutionBean(getCount());
	}
	
	private static class PropertySubstitutionBean implements Serializable {
		private static final long serialVersionUID = 5422751133639920585L;
		
		private int count;
		
		private PropertySubstitutionBean(int count) {
			this.count = count;
		}
		
		@SuppressWarnings("unused")
		private int getCount() {
			return count;
		}
	}
	
	private class AssignmentWrapper extends LoadableDetachableModel<String> implements IWrapModel<String> {
		private static final long serialVersionUID = 1L;

		private final Component component;

		public AssignmentWrapper(Component component) {
			this.component = component;
		}

		/**
		 * @see org.apache.wicket.model.IWrapModel#getWrappedModel()
		 */
		@Override
		public IModel<String> getWrappedModel() {
			return CountMessageModel.this;
		}

		@Override
		protected String load() {
			return Application.get().getResourceSettings().getLocalizer()
					.getString(CountMessageModel.this.getResourceKey(), component,
					Model.of(CountMessageModel.this.getPropertySubstitutionBean()));
		}

		@Override
		protected void onDetach() {
			CountMessageModel.this.detach();
		}
	}
	
}
