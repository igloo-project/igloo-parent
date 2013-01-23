package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.datepickersync;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.util.lang.Args;
import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;

import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.wicket.more.markup.html.form.DatePicker;

public class DatePickerSync implements ChainableStatement, Serializable {

	private static final long serialVersionUID = 5661268666461455313L;
	
	private static final String DATE_PICKER_SYNC = "datePickerSync";
	
	private static final String JS_ARRAY_START = "[";
	private static final String JS_ARRAY_CLOSE = "]";
	
	private final DatePicker courant;
	
	private final List<DatePicker> precedents = Lists.newArrayList();
	
	private final List<DatePicker> suivants = Lists.newArrayList();
	
	public DatePickerSync(DatePicker courant) {
		this(courant, null, null);
	}
	
	public DatePickerSync(DatePicker courant, DatePicker precedent, DatePicker suivant) {
		super();
		
		Args.notNull(courant, "courant");
		this.courant = courant;
		
		if (precedent != null) {
			addPrecedents(precedent);
		}
		if (suivant != null) {
			addSuivants(suivant);
		}
	}
	
	@Override
	public String chainLabel() {
		return DATE_PICKER_SYNC;
	}
	
	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options();
		
		if (!precedents.isEmpty()) {
			StringBuilder precedentsSb = new StringBuilder(JS_ARRAY_START);
			for (DatePicker precedent : precedents) {
				if (precedentsSb.length() > JS_ARRAY_START.length()) {
					precedentsSb.append(", ");
				}
				precedentsSb.append(new JsStatement().$(precedent).render(false).toString());
			}
			precedentsSb.append(JS_ARRAY_CLOSE);
			
			options.put("precedents", precedentsSb.toString());
		}
		
		if (!suivants.isEmpty()) {
			StringBuilder suivantsSb = new StringBuilder(JS_ARRAY_START);
			for (DatePicker suivant : suivants) {
				if (suivantsSb.length() > JS_ARRAY_START.length()) {
					suivantsSb.append(", ");
				}
				suivantsSb.append(new JsStatement().$(suivant).render(false).toString());
			}
			suivantsSb.append(JS_ARRAY_CLOSE);
			
			options.put("suivants", suivantsSb.toString());
		}
		
		return new CharSequence[] { options.getJavaScriptOptions() };
	}
	
	public List<DatePicker> getPrecedents() {
		return CloneUtils.clone(precedents);
	}
	
	public DatePickerSync addPrecedents(DatePicker... precedents) {
		for (DatePicker precedent : precedents) {
			if (precedent != null) {
				if (precedent.equals(courant)) {
					throw new IllegalArgumentException("Le date picker courant ne peut pas être ajouté en tant que précédent.");
				}
				if (!this.precedents.contains(precedent)) {
					this.precedents.add(precedent);
				}
			}
		}
		return this;
	}
	
	public List<DatePicker> getSuivants() {
		return CloneUtils.clone(suivants);
	}
	
	public DatePickerSync addSuivants(DatePicker... suivants) {
		for (DatePicker suivant : suivants) {
			if (suivant != null) {
				if (suivant.equals(courant)) {
					throw new IllegalArgumentException("Le date picker courant ne peut pas être ajouté en tant que suivant.");
				}
				if (!this.suivants.contains(suivant)) {
					this.suivants.add(suivant);
				}
			}
		}
		return this;
	}
}
