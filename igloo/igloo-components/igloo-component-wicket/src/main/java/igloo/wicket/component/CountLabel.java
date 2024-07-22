/**
 * Copyright 2012 55 Minutes (http://www.55minutes.com)
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package igloo.wicket.component;

import igloo.wicket.model.CountMessageModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * Displays a label with up to three different variations based on whether a numeric model is zero,
 * one, or greater than one. This is incredibly useful for printing count messages where the
 * phrasing needs to change according to singular and plural rules, like this:
 *
 * <ul>
 *   <li>There are no friends in your network.
 *   <li>There is <u>one friend</u> in your network.
 *   <li>There are <u>5 friends</u> in your network.
 * </ul>
 *
 * <p>This class is a simple Label wrapper around a CountMessageModel, which does most of the work.
 * Refer to the class documentation of {@link CountMessageModel} for more details.
 *
 * @since 2.0
 */
public class CountLabel extends Label {
  private static final long serialVersionUID = -889821092752173878L;

  private boolean hideIfZeroOrNull = false;

  private IModel<? extends Number> countModel;

  /**
   * Constructs a label that will a display zero, singular or plural variation based on the value of
   * the specified {@code count} model.
   *
   * @param id The wicket:id of the label component. This is will also be used as the message key to
   *     look up the message strings in the localized properties file.
   * @param count A number that will be used to choose either the <code>id.zero</code>, <code>id.one
   *     </code> or <code>id.many</code> string from the properties file. It will also be made
   *     available to the string as an interpolated <code>${count}</code> variable.
   */
  public CountLabel(String id, String messageKey, IModel<? extends Number> countModel) {
    super(id, new CountMessageModel(messageKey, countModel));
    this.countModel = countModel;
    setEscapeModelStrings(false);
  }

  @Override
  protected void onConfigure() {
    super.onConfigure();
    Number count = getCountModelObject();
    setVisible(
        !hideIfZeroOrNull
            || (count != null
                && !(count.longValue() == 0L))); // .equals(0L) will not work for Integers.
  }

  public CountLabel hideIfZeroOrNull() {
    this.hideIfZeroOrNull = true;
    return this;
  }

  private Number getCountModelObject() {
    return countModel != null ? countModel.getObject() : null;
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    if (countModel != null) {
      countModel.detach();
    }
  }
}
