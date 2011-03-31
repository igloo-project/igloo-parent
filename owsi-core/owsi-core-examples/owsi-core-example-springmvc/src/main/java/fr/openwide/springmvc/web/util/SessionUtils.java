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
package fr.openwide.springmvc.web.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

/**
 * <p>Utilitaire pour ajouter des données dans la session (messages, erreurs).</p>
 * 
 * @author Open Wide
 */
public final class SessionUtils {
    
    public static final String SESSION_SUCCESS_ATTRIBUTE_NAME = "successes";
    public static final String SESSION_WARNING_ATTRIBUTE_NAME = "warnings";
    public static final String SESSION_NOTICE_ATTRIBUTE_NAME = "notices";
    public static final String SESSION_ERROR_ATTRIBUTE_NAME = "errors";
    
    /**
     * Enregistre en session un message de succès.
     * 
     * @param session la session
     * @param messageOrCode le message ou le code du message
     */
    public static void registerSuccess(HttpSession session, String messageOrCode) {
        registerMessage(session, messageOrCode, SESSION_SUCCESS_ATTRIBUTE_NAME);
    }
    
    /**
     * Enregistre en session un message d'avertissement.
     * 
     * @param session la session
     * @param messageOrCode le message ou le code du message
     */
    public static void registerWarning(HttpSession session, String messageOrCode) {
        registerMessage(session, messageOrCode, SESSION_WARNING_ATTRIBUTE_NAME);
    }
    
    /**
     * Enregistre en session un message à caractère informatif.
     * 
     * @param session la session
     * @param messageOrCode le message ou le code du message
     */
    public static void registerNotice(HttpSession session, String messageOrCode) {
        registerMessage(session, messageOrCode, SESSION_NOTICE_ATTRIBUTE_NAME);
    }
    
    /**
     * Enregistre en session un message d'erreur.
     * 
     * @param session la session
     * @param messageOrCode le message ou le code du message
     */
    public static void registerError(HttpSession session, String messageOrCode) {
        registerMessage(session, messageOrCode, SESSION_ERROR_ATTRIBUTE_NAME);
    }
    
    /**
     * Enregistre en session un message.
     * 
     * @param session la session
     * @param messageOrCode le message ou le code du message
     * @param attributeName le nom de l'attribut en session
     */
    private static void registerMessage(HttpSession session, String messageOrCode, String attributeName) {
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) session.getAttribute(attributeName);
        if (list == null) {
            list = new ArrayList<String>();
            session.setAttribute(attributeName, list);
        }
        
        list.add(messageOrCode);
    }

    /**
     * Constructeur privé pour éviter l'instanciation accidentelle de la classe.
     */
    private SessionUtils() {
    }
}
