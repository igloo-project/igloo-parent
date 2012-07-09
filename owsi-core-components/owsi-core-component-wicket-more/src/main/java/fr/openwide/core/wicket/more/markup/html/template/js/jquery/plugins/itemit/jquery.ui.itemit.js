/*
* Multi-select widget component, inspired by jQuery UI Tag-it! with complex objects
* 
* =========================================
* Tag-it
* @version v2.0 (06/2011)
*
* Copyright 2011, Levy Carneiro Jr.
* Released under the MIT license.
* http://aehlke.github.com/tag-it/LICENSE
* 
* Homepage:
*   http://aehlke.github.com/tag-it/
*
* Authors:
*   Levy Carneiro Jr.
*   Martin Rehfeld
*   Tobias Schmidt
*   Skylar Challand
*   Alex Ehlke
*
* Maintainer:
*   Alex Ehlke - Twitter: @aehlke
*
* Dependencies:
*   jQuery v1.4+
*   jQuery UI v1.8+
* =========================================
*/
(function($) {

	$.widget('ui.itemit', {
		options: {
			fieldName			: 'itemit',
			availableObjects	: [],
			jsonSources		 	: null,
			removeConfirmation	: false,
			placeholderText		: null,
			allowCreate			: false,
			allowRemove			: true,
			disableInput		: false,
			autocompleteClass	: "",
			minLength			: 3,
			/* attribut positionné sur l'item qui indique des classes css à ajouter */
			cssClassNameAttribute : "type",
			
			// Whether to animate item removals or not.
			animate: true,
			
			// Optionally set a tabindex attribute on the input that gets
			// created for item-it.
			tabIndex: null,
			
			// Event callbacks.
			onItemInit   : null, // used in place of item creation at init time if present
			onItemAdded  : null,
			onItemRemoved: null,
			onItemClicked: null,
			
			// groupes pour les résultats : configuration par défaut sans groupe
			groups: [ { name: "default", title: null, className: null } ],
			
			// build a unique item identifier
			identifier: function(item) { return item.id },
			// format added item
			formatItem: function(item) { return item.label },
			// full text label
			labelItem: function(item) { return item.label },
			// format item in complete list
			formatCompleteItem: function(item) { return item.label },
			newItem: function(label) { return { label: label, id: null } },
			menuWidthOfItemIt: false // menu width can match itemit width or be auto.
		},


		_create: function() {
			// for handling static scoping inside callbacks
			var that = this;

			// A new list is created after current element
			this.itemList = $('<ul></ul>').insertAfter(this.element);
			this.element.css('display', 'none');

			this._itemInput = $('<input type="text" />').addClass('ui-widget-content');
			if (this.options.tabIndex) {
				this._itemInput.attr('tabindex', this.options.tabIndex);
			}
			if (this.options.placeholderText) {
				this._itemInput.attr('placeholder', this.options.placeholderText);
			}
			if (this.options.disableInput) {
				this._itemInput.css('display', 'none');
			}
			if (!this.options.allowRemove) {
				this.itemList.addClass('remove-disabled')
			}

			this.options.jsonSources = this.options.jsonSources || function(search, showChoices) {
				var filter = search.term.toLowerCase();
				var choices = $.grep(this.options.availableObjects, function(element) {
					// Only match autocomplete options that begin with the search term.
					// (Case insensitive.)
					return (element.label.toLowerCase().indexOf(filter) === 0);
				});
				showChoices(this._subtractArray(choices, this.assignedItems()));
			};

			// Bind jsonSource callback functions to this context.
			if ($.isFunction(this.options.jsonSources)) {
				this.options.jsonSources = $.proxy(this.options.jsonSources, this);
			}

			this.itemList
				.addClass('itemit')
				.addClass('ui-widget ui-widget-content ui-corner-all')
				// Create the input field.
				.append($('<li class="itemit-new"></li>').append(this._itemInput))
				.click(function(e) {
					var target = $(e.target);
					if (target.hasClass('itemit-label')) {
						that._trigger('onItemClicked', e, target.closest('.itemit-choice'));
					} else {
						// Sets the focus() to the input field, if the user
						// clicks anywhere inside the UL. This is needed
						// because the input field needs to be of a small size.
						that._itemInput.focus();
					}
				});

			// Add existing items from the input list, if any.
			this.element.children('input').each(function() {
				var input = $(this);
				if (input.attr('name') == that.options.fieldName) {
					var $this = $(this);
					var object = $.secureEvalJSON($this.val());
					if (that.options.onItemInit) {
						that.options.onItemInit(object, that.element);
					} else {
						that.createItem(object);
					}
					// input elements are removed from source once added in item list
					$this.remove();
				}
			});

			// Events.
			this._itemInput
				.keydown(function(event) {
					// Backspace is not detected within a keypress, so it must use keydown.
					if (event.which == $.ui.keyCode.BACKSPACE && that._itemInput.val() === '') {
						if (that.options.allowRemove) {
							var itemElement = that._lastItem();
							if (itemElement.size() > 0) {
								if (!that.options.removeConfirmation || itemElement.hasClass('remove')) {
									// When backspace is pressed, the last item is deleted.
									that.removeItemElement(itemElement);
								} else if (that.options.removeConfirmation) {
									itemElement.addClass('remove ui-state-highlight');
								}
							}
						}
					} else if (event.which == $.ui.keyCode.ENTER) {
						event.preventDefault();
						if (that.options.allowCreate && that._itemInput.val() != '') {
							that.createItem(that.options.newItem(that._itemInput.val()));
						}
					} else if (that.options.removeConfirmation) {
						that._lastItem().removeClass('remove ui-state-highlight');
					}
				});

			// Autocomplete.
			if (this.options.availableObjects || this.options.jsonSources) {
				this._itemInput.itemautocomplete({
					groups: this.options.groups,
					positionAndWidth: this.options.menuWidthOfItemIt ? this.element.parent() : undefined,
					sources: this.options.jsonSources,
					select: function(event, ui) {
						// Delete the last item if we autocomplete something despite the input being empty
						// This happens because the input's blur event causes the item to be created when
						// the user clicks an autocomplete item.
						// The only artifact of this is that while the user holds down the mouse button
						// on the selected autocomplete item, an item is shown with the pre-autocompleted text,
						// and is changed to the autocompleted text upon mouseup.
						if (that._itemInput.val() === '') {
							that.removeItemElement(that._lastItem(), false);
						}
						that.createItem(ui.item);
						// Preventing the item input to be updated with the chosen value.
						return false;
					},
					labelItem: this.options.labelItem,
					renderItem: this.options.formatCompleteItem,
					autocompleteClass: this.options.autocompleteClass,
					minLength: this.options.minLength,
					cssClassNameAttribute: this.options.cssClassNameAttribute
				});
			}
		},

		_lastItem: function() {
			return this.itemList.children('.itemit-choice:last');
		},

		updateItems: function() {
			var that = this;
			var items = [];
			this.itemList.children('.itemit-choice').each(function() {
				$("input", this).attr('value', $.toJSON(that._item(this)));
			});
		},

		assignedItems: function() {
			// Returns an array of item object values
			var that = this;
			var items = [];
			this.itemList.children('.itemit-choice').each(function() {
				items.push(that._item(this));
			});
			return items;
		},

		_subtractArray: function(a1, a2) {
			var result = [];
			for (var i = 0; i < a1.length; i++) {
				if ($.inArray(a1[i], a2) == -1) {
					result.push(a1[i]);
				}
			}
			return result;
		},

		_item: function(itemElement, item) {
			if (item != null) {
				var data = $.extend({}, $(itemElement).data('itemit'), { item: item });
				$(itemElement).data('itemit', data);
			} else {
				return $(itemElement).data('itemit').item;
			}
		},

		_itemEquals: function(item1, item2) {
			if (this.options.identifier(item1) === null && this.options.identifier(item2) === null
					&& this.options.allowCreate) {
				// les éléments créés (identifier null) peuvent être ajoutés.
				return false;
			}
			return this.options.identifier(item1) == this.options.identifier(item2);
		},

		_isNew: function(item) {
			var that = this;
			var isNew = true;
			this.itemList.children('.itemit-choice').each(function(i) {
				if (that._itemEquals(item, that._item(this))) {
					isNew = false;
					return false;
				}
			});
			return isNew;
		},

		_formatItem: function(item) {
			return this.options.formatItem(item);
		},

		createItem: function(item) {
			var that = this;
			
			if (!this._isNew(item) || item === null) {
				return false;
			}
			
			var label = $(this.options.onItemClicked ? '<a class="itemit-label"></a>' : '<span class="itemit-label"></span>').html(this._formatItem(item));
			
			// Create item element.
			var itemElement = $('<li></li>')
				.addClass('itemit-choice ui-widget-content ui-state-default ui-corner-all')
				.append(label);
			if (item.type) {
				itemElement.addClass(item.type);
			}
			this._item(itemElement, item);
			
			// Button for removing the item element.
			if (that.options.allowRemove) {
				var removeItemIcon = $('<span></span>')
					.addClass('ui-icon ui-icon-close');
				var removeItem = $('<a><span class="text-icon">\xd7</span></a>') // \xd7 is an X
					.addClass('itemit-close')
					.append(removeItemIcon)
					.click(function(e) {
						// Removes an item element when the little 'x' is clicked.
						that.removeItemElement(itemElement);
					});
				itemElement.append(removeItem);
			}
			
			this._trigger('onItemAdded', null, item);
			
			var input = $('<input type="hidden" style="display:none;" value="" name="" />');
			input.attr('name', this.options.fieldName);
			input.attr('value', $.toJSON(item));
			
			itemElement.append(input);
			
			// Cleaning the input.
			this._itemInput.val('');
			
			// insert item element
			this._itemInput.parent().before(itemElement);
		},
		
		removeItem: function(item, animate) {
			// Returns an array of item object values
			var that = this;
			var items = [];
			this.itemList.children('.itemit-choice').each(function() {
				var assignedItem = that._item(this);
				if (that._itemEquals(assignedItem, item)) {
					that.removeItemElement(this, animate);
				}
			});
		},
		
		removeItemElement: function(itemElement, animate) {
			var that = this;
			animate = animate || this.options.animate;

			itemElement = $(itemElement);

			// Animate the removal.
			if (animate) {
				var item = this._item(itemElement);
				itemElement.fadeOut('fast').hide('blind', {direction: 'horizontal'}, 'fast', function() {
					itemElement.remove();
					that._trigger('onItemRemoved', null, item);
				}).dequeue();
			} else {
				itemElement.remove();
			}
		},

		removeAll: function() {
			// Removes all items.
			var that = this;
			this.itemList.children('.itemit-choice').each(function(index, itemElement) {
				that.removeItemElement(itemElement, false);
			});
		},

		disable: function() {
			this._itemInput.hide();
		},

		enable: function() {
			this._itemInput.show();
		}

	});

})(jQuery);
