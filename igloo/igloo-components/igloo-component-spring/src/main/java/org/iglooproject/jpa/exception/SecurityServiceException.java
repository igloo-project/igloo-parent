/*
 * Copyright (C) 2009-2010 Open Wide
 * Contact: contact@openwide.fr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.iglooproject.jpa.exception;

/**
 * Exception générée lorsqu'un service refuse l'accès aux données à un utilisateur n'ayant pas les
 * droits suffisants.
 *
 * @author Open Wide
 */
public class SecurityServiceException extends Exception {

  private static final long serialVersionUID = -3589212911857116468L;

  public SecurityServiceException() {
    super();
  }

  public SecurityServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public SecurityServiceException(String message) {
    super(message);
  }

  public SecurityServiceException(Throwable cause) {
    super(cause);
  }
}
