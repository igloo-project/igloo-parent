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
package fr.openwide.springmvc.web.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import org.springframework.web.servlet.support.JspAwareRequestContext;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springframework.web.util.ExpressionEvaluationUtils;
import org.springframework.web.util.TagUtils;

/**
 * Obtient le jeton de version actuellement utilisé
 * 
 * @author Open Wide
 */
public class TimestampVersionTag extends TagSupport implements TryCatchFinally {
    private static final long serialVersionUID = 1784952134539605957L;

    /**
     * Nom du bean fournissant le timestamp version à utiliser
     */
    public static final String TIMESTAMP_VERSION_BEAN = "timestampVersion";
    
    /**
     * nom de l'attribut contenant le {@link PageContext}
     */
    private static final String REQUEST_CONTEXT_PAGE_ATTRIBUTE = RequestContextAwareTag.REQUEST_CONTEXT_PAGE_ATTRIBUTE;
    
    // début des attributs
    
    /**
     * variable dans laquelle stocker la valeur (optionnel)
     */
    private String var;

    /**
     * Le scope à appliquer pour la variable
     */
    private String scope = TagUtils.SCOPE_PAGE;
    
    // fin des attributes
    
    /**
     * Timestamp de version
     */
    private String timestampVersion;
    
    /**
     * Consultation du {@link RequestContext}
     */
    private transient RequestContext requestContext;
    
    @Override
    public int doStartTag() throws JspException {
        if (this.requestContext == null) {
            this.requestContext = new JspAwareRequestContext(this.pageContext);
            this.pageContext.setAttribute(REQUEST_CONTEXT_PAGE_ATTRIBUTE, this.requestContext);
        }
        if (timestampVersion == null) {
            timestampVersion = (String) getRequestContext().getWebApplicationContext()
                .getBean(TIMESTAMP_VERSION_BEAN).toString();
        }
        
        String resolvedVar = ExpressionEvaluationUtils.evaluateString("var", this.var, pageContext);

        // store or print the output
        if (resolvedVar != null) {
            String resolvedScope = ExpressionEvaluationUtils.evaluateString("scope", this.scope, pageContext);
            pageContext.setAttribute(resolvedVar, timestampVersion, TagUtils.getScope(resolvedScope));
        } else {
            try {
                pageContext.getOut().print(timestampVersion);
            } catch (java.io.IOException ex) {
                throw new JspTagException(ex.toString(), ex);
            }
        }
        return super.doStartTag();
    }
    
    // début du setting des attributs
    
    public void setVar(String var) {
        this.var = var;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
    
    // fin des attributs

    /**
     * Return the current RequestContext.
     */
    private RequestContext getRequestContext() {
        return this.requestContext;
    }

    @Override
    public void doCatch(Throwable arg0) throws Throwable {
        throw arg0;
    }

    @Override
    public void doFinally() {
        this.requestContext = null;
        this.scope = TagUtils.SCOPE_PAGE;
    }

}
