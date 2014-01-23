package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.itemit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.ArrayItemOptions;
import org.odlabs.wiquery.core.options.ITypedOption;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.core.options.Options;
import org.odlabs.wiquery.core.options.StringOption;

public class ItemIt implements ChainableStatement, Serializable {

	private static final long serialVersionUID = -455362449241377644L;

	/**
	 * Nom du champ (name du input).
	 */
	private String fieldName;

	/**
	 * Source json interne à l'application (url servie par wicket).
	 */
	private String internalJsonSource;

	/**
	 * Définition des groupes.
	 */
	private ArrayItemOptions<ItemItGroupOptions> groups = new ArrayItemOptions<ItemItGroupOptions>();

	/**
	 * Sources externes, fournies au format javascript (aucun échappement effectué).
	 */
	private List<String> externalJsonSources = new ArrayList<String>();

	/**
	 * Utilisation ou non de la confirmation lors de la suppression.
	 */
	private Boolean removeConfirmation;

	/**
	 * Texte à insérer dans le champ.
	 */
	private String placeholderText;

	/**
	 * Utilisation des animations.
	 */
	private Boolean animate;

	/**
	 * Ordre du champ dans le formulaire.
	 */
	private Integer tabIndex;

	/**
	 * Evénement appelé lors de l'initialisation. Si fourni, la fonction doit s'occuper d'ajouter l'élément
	 * dans ItemIt à l'aide de .createItem(item)
	 */
	private JsScope onItemInit;

	/**
	 * Evénement appelé lors de l'ajout d'un nouvel item.
	 */
	private JsScope onItemAdded;

	/**
	 * Evénement appelé lors de la suppression d'un item.
	 */
	private JsScope onItemRemoved;

	/**
	 * Evénement appelé lors du clic sur un item.
	 */
	private JsScope onItemClicked;

	/**
	 * Fonction qui fournit un identifier à partir d'un item.
	 */
	private JsScope identifier;

	/**
	 * Fonction qui formate un item dans le champ ItemIt.
	 */
	private JsScope formatItem;

	/**
	 * Fonction qui formate un élément dans la liste des propositions.
	 */
	private JsScope formatCompleteItem;

	/**
	 * Autorise ou non de nouveaux éléments.
	 */
	private Boolean allowCreate;

	/**
	 * Autorise ou non de nouveaux éléments sur l'événement blur. Nécessite {@link ItemIt#allowCreate}
	 */
	private Boolean allowCreateOnBlur;

	/**
	 * Autorise ou non la suppression d'éléments.
	 */
	private Boolean allowRemove;

	/**
	 * Fonction de création d'un nouvel item, dans le cas où allowCreate = true.
	 */
	private JsScope newItem;

	/**
	 * Désactivation de la saisie.
	 */
	private Boolean disableInput;

	/**
	 * Noeud DOM d'un élément sur lequel se baser pour le positionnement et la largeur du champ. Champ non échappé.
	 */
	private Boolean menuWidthOfItemIt;

	/**
	 * Classe css à ajouter au champs autocomplete
	 */
	private String autocompleteClass;

	/**
	 * Nombre de caractères minimum avant proposition
	 */
	private Integer minLength;

	private Component owner;

	@Override
	public String chainLabel() {
		return "itemit";
	}

	@Override
	public CharSequence[] statementArgs() {
		Options options = new Options(owner);
		if (fieldName != null) {
			options.putLiteral("fieldName", fieldName);
		}
		// pour que la définition de groupe par défaut soit prise en compte
		// il ne faut surtout pas intégrer groups si la valeur est vide.
		if (groups != null && !groups.isEmpty()) {
			options.put("groups", groups);
		}
		
		ArrayItemOptions<ITypedOption<?>> jsonSources = new ArrayItemOptions<ITypedOption<?>>();
		if (internalJsonSource != null) {
			// quote value
			jsonSources.add(new LiteralOption(internalJsonSource));
		}
		for (String jsonSource : externalJsonSources) {
			jsonSources.add(new StringOption(jsonSource));
		}
		if (jsonSources.size() > 0) {
			options.put("jsonSources", jsonSources);
		}
		
		if (removeConfirmation != null) {
			options.put("removeConfirmation", removeConfirmation);
		}
		if (placeholderText != null) {
			options.putLiteral("placeholderText", placeholderText);
		}
		if (animate != null) {
			options.put("animate", animate);
		}
		if (tabIndex != null) {
			options.put("tabIndex", tabIndex);
		}
		if (onItemAdded != null) {
			options.put("onItemAdded", onItemAdded);
		}
		if (onItemRemoved != null) {
			options.put("onItemRemoved", onItemRemoved);
		}
		if (onItemClicked != null) {
			options.put("onItemClicked", onItemClicked);
		}
		if (onItemInit != null) {
			options.put("onItemInit", onItemInit);
		}
		if (formatItem != null) {
			options.put("formatItem", formatItem);
		}
		if (formatCompleteItem != null) {
			options.put("formatCompleteItem", formatCompleteItem);
		}
		if (allowCreate != null) {
			options.put("allowCreate", allowCreate);
		}
		if (allowCreateOnBlur != null) {
			options.put("allowCreateOnBlur", allowCreateOnBlur);
		}
		if (allowRemove != null) {
			options.put("allowRemove", allowRemove);
		}
		if (newItem != null) {
			options.put("newItem", newItem);
		}
		if (disableInput != null) {
			options.put("disableInput", disableInput);
		}
		if (menuWidthOfItemIt != null) {
			options.put("menuWidthOfItemIt", menuWidthOfItemIt);
		}
		if (autocompleteClass != null) {
			options.put("autocompleteClass", JsUtils.quotes(autocompleteClass));
		}
		if (minLength != null) {
			options.put("minLength", minLength);
		}
		return new CharSequence[] { options.getJavaScriptOptions() };
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public ArrayItemOptions<ItemItGroupOptions> getGroups() {
		return groups;
	}

	public void setGroups(ArrayItemOptions<ItemItGroupOptions> groups) {
		this.groups = groups;
	}

	public String getInternalJsonSource() {
		return internalJsonSource;
	}

	public void setInternalJsonSource(String internalJsonSource) {
		this.internalJsonSource = internalJsonSource;
	}

	public void addExternalJsonSource(String jsonSource) {
		externalJsonSources.add(jsonSource);
	}

	public Boolean getRemoveConfirmation() {
		return removeConfirmation;
	}

	public void setRemoveConfirmation(Boolean removeConfirmation) {
		this.removeConfirmation = removeConfirmation;
	}

	public String getPlaceholderText() {
		return placeholderText;
	}

	public void setPlaceholderText(String placeholderText) {
		this.placeholderText = placeholderText;
	}

	public Boolean getAnimate() {
		return animate;
	}

	public void setAnimate(Boolean animate) {
		this.animate = animate;
	}

	public Integer getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(Integer tabIndex) {
		this.tabIndex = tabIndex;
	}

	public JsScope getOnItemInit() {
		return onItemInit;
	}

	public void setOnItemInit(JsScope onItemInit) {
		this.onItemInit = onItemInit;
	}

	public JsScope getOnItemAdded() {
		return onItemAdded;
	}

	public void setOnItemAdded(JsScope onItemAdded) {
		this.onItemAdded = onItemAdded;
	}

	public JsScope getOnItemRemoved() {
		return onItemRemoved;
	}

	public void setOnItemRemoved(JsScope onItemRemoved) {
		this.onItemRemoved = onItemRemoved;
	}

	public JsScope getOnItemClicked() {
		return onItemClicked;
	}

	public void setOnItemClicked(JsScope onItemClicked) {
		this.onItemClicked = onItemClicked;
	}

	public JsScope getIdentifier() {
		return identifier;
	}

	public void setIdentifier(JsScope identifier) {
		this.identifier = identifier;
	}

	public JsScope getFormatItem() {
		return formatItem;
	}

	public void setFormatItem(JsScope formatItem) {
		this.formatItem = formatItem;
	}

	public JsScope getFormatCompleteItem() {
		return formatCompleteItem;
	}

	public void setFormatCompleteItem(JsScope formatCompleteItem) {
		this.formatCompleteItem = formatCompleteItem;
	}

	public Boolean getAllowCreate() {
		return allowCreate;
	}

	public void setAllowCreate(Boolean allowCreate) {
		this.allowCreate = allowCreate;
	}

	public Boolean getAllowCreateOnBlur() {
		return allowCreateOnBlur;
	}

	public void setAllowCreateOnBlur(Boolean allowCreateOnBlur) {
		this.allowCreateOnBlur = allowCreateOnBlur;
	}

	public Boolean getAllowRemove() {
		return allowRemove;
	}

	public void setAllowRemove(Boolean allowRemove) {
		this.allowRemove = allowRemove;
	}

	public JsScope getNewItem() {
		return newItem;
	}

	public void setNewItem(JsScope newItem) {
		this.newItem = newItem;
	}

	public Boolean getDisableInput() {
		return disableInput;
	}

	public void setDisableInput(Boolean disableInput) {
		this.disableInput = disableInput;
	}

	public Boolean getMenuWidthOfItemIt() {
		return menuWidthOfItemIt;
	}

	public void setMenuWidthOfItemIt(Boolean menuWidthOfItemIt) {
		this.menuWidthOfItemIt = menuWidthOfItemIt;
	}

	public String getAutocompleteClass() {
		return autocompleteClass;
	}

	public void setAutocompleteClass(String autocompleteClass) {
		this.autocompleteClass = autocompleteClass;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public Component getOwner() {
		return owner;
	}

	public void setOwner(Component owner) {
		this.owner = owner;
	}

}
