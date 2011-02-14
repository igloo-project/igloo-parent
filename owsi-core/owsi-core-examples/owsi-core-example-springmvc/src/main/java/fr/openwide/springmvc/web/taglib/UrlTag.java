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

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import org.apache.taglibs.standard.resources.Resources;
import org.apache.taglibs.standard.tag.common.core.ImportSupport;
import org.apache.taglibs.standard.tag.common.core.ParamParent;
import org.apache.taglibs.standard.tag.common.core.ParamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.JspAwareRequestContext;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springframework.web.util.ExpressionEvaluationUtils;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;
import org.springframework.web.util.TagUtils;

/**
 * Taglib permettant de générer une URL à partir du thème courant.
 * 
 * Cette taglib implémente de manière croisée c:url et spring:theme de manière
 * à générer une URL à partir d'une clé du thème.
 * 
 * Les différents attributs de la taglib sont documentés sur les champs de cette classe
 * 
 * @author Open Wide
 */
public class UrlTag extends BodyTagSupport implements ParamParent,
		TryCatchFinally {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UrlTag.class);

	/**
	 * Default separator for splitting an arguments String: a comma (",")
	 */
	public static final String DEFAULT_ARGUMENT_SEPARATOR = ",";

	/**
	 * Nom du bean fournissant le timestamp version à utiliser
	 */
	public static final String TIMESTAMP_VERSION_BEAN = "timestampVersion";

	/**
	 * nom de l'attribut contenant le {@link PageContext}
	 */
	private static final String REQUEST_CONTEXT_PAGE_ATTRIBUTE = RequestContextAwareTag.REQUEST_CONTEXT_PAGE_ATTRIBUTE;

	/**
	 * Le message à résoudre ; prend la précédence sur le paramètre <i>code</i>
	 */
	private Object message;

	/**
	 * Le code à résoudre par le thème
	 */
	private String code;

	/**
	 * Les arguments à appliquer lors de la résolution du code
	 */
	private Object arguments;

	/**
	 * Possibilité de spécifier un séparateur autre que la ','
	 */
	private String argumentSeparator = DEFAULT_ARGUMENT_SEPARATOR;

	/**
	 * Le texte à utiliser si le code ne peut être résolu
	 */
	private String text;

	/**
	 * La variable dans laquelle stocker le résultat
	 */
	private String var;

	/**
	 * Le scope à appliquer pour la variable
	 */
	private String scope = TagUtils.SCOPE_PAGE;

	/**
	 * Echapper le résultat pour une utilisation javascript
	 */
	private boolean javaScriptEscape = false;

	/**
	 * Echapper le résultat pour une utilisation html
	 */
	private Boolean htmlEscape;

	/**
	 * Contexte alternatif pour la construction de l'url
	 */
	private String context;

	/**
	 * Indique l'ajout d'une variable get avec un timestamp
	 */
	private Boolean addTimestampVersion = false;

	// fin des attributs de la taglib

	/**
	 * Paramètres de la taglib
	 */
	private ParamSupport.ParamManager params;

	/**
	 * Consultation du {@link RequestContext}
	 */
	private transient RequestContext requestContext;

	/**
	 * Stockage du code résolu
	 */
	private String value;

	/**
	 * Timestamp de version
	 */
	private String timestampVersion;

	@Override
	public void release() {
		super.release();
	}

	/**
	 * Create and expose the current RequestContext. Delegates to
	 * {@link #doStartTagInternal()} for actual work.
	 * 
	 * @see #REQUEST_CONTEXT_PAGE_ATTRIBUTE
	 * @see org.springframework.web.servlet.support.JspAwareRequestContext
	 */
	@Override
	public final int doStartTag() throws JspException {
		params = new ParamSupport.ParamManager();
		this.requestContext = (RequestContext) this.pageContext
				.getAttribute(REQUEST_CONTEXT_PAGE_ATTRIBUTE);
		try {
			if (this.requestContext == null) {
				this.requestContext = new JspAwareRequestContext(
						this.pageContext);
				this.pageContext.setAttribute(REQUEST_CONTEXT_PAGE_ATTRIBUTE,
						this.requestContext);
			}
			return doStartTagInternal();
		} catch (JspException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw ex;
		} catch (RuntimeException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw ex;
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new JspTagException(ex.getMessage(), ex);
		}
	}

	/**
	 * Resolves the message, escapes it if demanded, and writes it to the page
	 * (or exposes it as variable).
	 * 
	 * @see #resolveMessage()
	 * @see org.springframework.web.util.HtmlUtils#htmlEscape(String)
	 * @see org.springframework.web.util.JavaScriptUtils#javaScriptEscape(String)
	 */
	private int doStartTagInternal() throws JspException, IOException {
		try {
			// Resolve the unescaped message.
			String msg = resolveMessage();

			value = msg;

			return EVAL_BODY_BUFFERED;
		} catch (NoSuchMessageException ex) {
			throw new JspTagException(getNoSuchMessageExceptionDescription(ex));
		}
	}

	/**
	 * Resolve the specified message into a concrete message String. The
	 * returned message String should be unescaped.
	 */
	private String resolveMessage() throws JspException, NoSuchMessageException {
		MessageSource messageSource = getMessageSource();
		if (messageSource == null) {
			throw new JspTagException("No corresponding MessageSource found");
		}

		// Evaluate the specified MessageSourceResolvable, if any.
		MessageSourceResolvable resolvedMessage = null;
		if (this.message instanceof MessageSourceResolvable) {
			resolvedMessage = (MessageSourceResolvable) this.message;
		} else if (this.message != null) {
			String expr = this.message.toString();
			resolvedMessage = (MessageSourceResolvable) ExpressionEvaluationUtils
					.evaluate("message", expr, MessageSourceResolvable.class,
							pageContext);
		}

		if (resolvedMessage != null) {
			// We have a given MessageSourceResolvable.
			return messageSource.getMessage(resolvedMessage,
					getRequestContext().getLocale());
		}

		String resolvedCode = ExpressionEvaluationUtils.evaluateString("code",
				this.code, pageContext);
		String resolvedText = ExpressionEvaluationUtils.evaluateString("text",
				this.text, pageContext);

		if (resolvedCode != null || resolvedText != null) {
			// We have a code or default text that we need to resolve.
			Object[] argumentsArray = resolveArguments(this.arguments);
			if (resolvedText != null) {
				// We have a fallback text to consider.
				return messageSource.getMessage(resolvedCode, argumentsArray,
						resolvedText, getRequestContext().getLocale());
			} else {
				// We have no fallback text to consider.
				return messageSource.getMessage(resolvedCode, argumentsArray,
						getRequestContext().getLocale());
			}
		}

		// All we have is a specified literal text.
		return resolvedText;
	}

	/**
	 * Resolve the given arguments Object into an arguments array.
	 * 
	 * @param arguments
	 *            the specified arguments Object
	 * @return the resolved arguments as array
	 * @throws JspException
	 *             if argument conversion failed
	 * @see #setArguments
	 */
	private Object[] resolveArguments(Object arguments) throws JspException {
		if (arguments instanceof String) {
			String[] stringArray = StringUtils.delimitedListToStringArray(
					(String) arguments, this.argumentSeparator);
			if (stringArray.length == 1) {
				Object argument = ExpressionEvaluationUtils.evaluate(
						"argument", stringArray[0], pageContext);
				if (argument != null && argument.getClass().isArray()) {
					return ObjectUtils.toObjectArray(argument);
				} else {
					return new Object[] { argument };
				}
			} else {
				Object[] argumentsArray = new Object[stringArray.length];
				for (int i = 0; i < stringArray.length; i++) {
					argumentsArray[i] = ExpressionEvaluationUtils.evaluate(
							"argument[" + i + "]", stringArray[i], pageContext);
				}
				return argumentsArray;
			}
		} else if (arguments instanceof Object[]) {
			return (Object[]) arguments;
		} else if (arguments instanceof Collection<?>) {
			return ((Collection<?>) arguments).toArray();
		} else if (arguments != null) {
			// Assume a single argument object.
			return new Object[] { arguments };
		} else {
			return null;
		}
	}

	/**
	 * Return the HTML escaping setting for this tag, or the default setting if
	 * not overridden.
	 * 
	 * @see #isDefaultHtmlEscape()
	 */
	private boolean isHtmlEscape() {
		if (this.htmlEscape != null) {
			return this.htmlEscape.booleanValue();
		} else {
			return isDefaultHtmlEscape();
		}
	}

	/**
	 * Return the applicable default HTML escape setting for this tag.
	 * <p>
	 * The default implementation checks the RequestContext's setting, falling
	 * back to <code>false</code> in case of no explicit default given.
	 * 
	 * @see #getRequestContext()
	 */
	private boolean isDefaultHtmlEscape() {
		if (getRequestContext() != null) {
			return getRequestContext().isDefaultHtmlEscape();
		} else {
			return true;
		}
	}

	/**
	 * Return the current RequestContext.
	 */
	private RequestContext getRequestContext() {
		return this.requestContext;
	}

	/**
	 * Return default exception message.
	 */
	private String getNoSuchMessageExceptionDescription(
			NoSuchMessageException ex) {
		return ex.getMessage();
	}

	/**
	 * Use the theme MessageSource for theme message resolution.
	 */
	private MessageSource getMessageSource() {
		return getRequestContext().getTheme().getMessageSource();
	}

	/**
	 * Utilise le contenu du tag (tags param) pour appliquer les paramètres et
	 * la résolution du code effectuée sur le doStartTag pour construire l'url
	 * demandée, l'échapper (si demandé) et la restituer.
	 */
	@Override
	public int doEndTag() throws JspException {
		String result;

		// add (already encoded) parameters
		String baseUrl = resolveUrl(value, context, pageContext);
		if (addTimestampVersion) {
			if (timestampVersion == null) {
				timestampVersion = (String) getRequestContext()
						.getWebApplicationContext().getBean(
								TIMESTAMP_VERSION_BEAN).toString();
			}
			params.addParameter("timestampVersion", timestampVersion);
		}
		result = params.aggregateParams(baseUrl);

		// if the URL is relative, rewrite it
		if (!ImportSupport.isAbsoluteUrl(result)) {
			HttpServletResponse response = ((HttpServletResponse) pageContext
					.getResponse());
			result = response.encodeURL(result);
		}
		// HTML and/or JavaScript escape, if demanded.
		result = isHtmlEscape() ? HtmlUtils.htmlEscape(result) : result;
		result = this.javaScriptEscape ? JavaScriptUtils
				.javaScriptEscape(result) : result;

		String resolvedVar = ExpressionEvaluationUtils.evaluateString("var",
				this.var, pageContext);

		// store or print the output
		if (resolvedVar != null) {
			String resolvedScope = ExpressionEvaluationUtils.evaluateString(
					"scope", this.scope, pageContext);
			pageContext.setAttribute(resolvedVar, result, TagUtils
					.getScope(resolvedScope));
		} else {
			try {
				pageContext.getOut().print(result);
			} catch (java.io.IOException ex) {
				throw new JspTagException(ex.toString(), ex);
			}
		}

		return EVAL_PAGE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.taglibs.standard.tag.common.core.ParamParent#addParameter(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void addParameter(String name, String value) {
		params.addParameter(name, value);
	}

	/**
	 * Construit une url complète à partir d'une url et d'un contexte
	 * (optionnel). Si le contexte n'est pas fourni, alors le contexte courant
	 * est utilisé.
	 * 
	 * @param url
	 *            l'url à construire
	 * @param context
	 *            le contexte pour lequel fournir l'url (null si entraine
	 *            l'utilisation du contexte courant)
	 * @param pageContext
	 *            le pageContext pour pouvoir trouver le contexte courant
	 * @return
	 * @throws JspException
	 */
	private static String resolveUrl(String url, String context,
			PageContext pageContext) throws JspException {
		// don't touch absolute URLs
		if (ImportSupport.isAbsoluteUrl(url)) {
			return url;
		}

		// normalize relative URLs against a context root
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		if (context == null) {
			if (url.startsWith("/")) {
				return (request.getContextPath() + url);
			} else {
				return url;
			}
		} else {
			if (!context.startsWith("/") || !url.startsWith("/")) {
				throw new JspTagException(Resources
						.getMessage("IMPORT_BAD_RELATIVE"));
			}
			if (context.equals("/")) {
				// Don't produce string starting with '//', many
				// browsers interpret this as host name, not as
				// path on same host.
				return url;
			} else {
				return (context + url);
			}
		}
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setArguments(String arguments) {
		this.arguments = arguments;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setHtmlEscape(String htmlEscape) throws JspException {
		this.htmlEscape = Boolean.valueOf(ExpressionEvaluationUtils
				.evaluateBoolean("htmlEscape", htmlEscape, pageContext));
	}

	public void setJavaScriptEscape(String javaScriptEscape)
			throws JspException {
		this.javaScriptEscape = ExpressionEvaluationUtils.evaluateBoolean(
				"javaScriptEscape", javaScriptEscape, pageContext);
	}

	public void setArgumentSeparator(String argumentSeparator) {
		this.argumentSeparator = argumentSeparator;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public void setAddTimestampVersion(Boolean addTimestampVersion) {
		this.addTimestampVersion = addTimestampVersion;
	}

	@Override
	public void doCatch(Throwable arg0) throws Throwable {
		throw arg0;
	}

	@Override
	public void doFinally() {
		this.release();
	}
}
