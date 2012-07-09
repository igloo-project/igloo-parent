/*
 * jQuery UI Autocomplete 1.8.16
 * 
 * Modified to handle metadata and multiple selection groups
 * Modified to handle multiple sources
 *
 * Copyright 2011, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Autocomplete
 *
 * Depends:
 *	jquery.ui.core.js
 *	jquery.ui.widget.js
 *	jquery.ui.position.js
 */
(function( $, undefined ) {

// used to prevent race conditions with remote data sources
var requestIndex = 0;

$.widget( "ui.itemautocomplete", {
	options: {
		appendTo: "body",
		autoFocus: false,
		delay: 300,
		minLength: 1,
		position: {
			my: "left top",
			at: "left bottom",
			collision: "none"
		},
		source: null,
		groups: [ { name: "default", title: null, className: null } ],
		// optionally html markup for item
		renderItem: function(item) { return document.createTextNode(item.label); },
		// full text item for input field
		labelItem: function(item) { return item.label },
		positionAndWidth: null, // optionally a dom jquery element to match in size and position
		autocompleteClass: "",
		/* attribut positionné sur l'item qui indique des classes css à ajouter */
		cssClassNameAttribute : "type"
	},

	pending: 0,

	_create: function() {
		var self = this,
			doc = this.element[ 0 ].ownerDocument,
			suppressKeyPress;

		this.element
			.addClass( "ui-autocomplete-input" )
			.attr( "autocomplete", "off" )
			// TODO verify these actually work as intended
			.attr({
				role: "textbox",
				"aria-autocomplete": "list",
				"aria-haspopup": "true"
			})
			.bind( "keydown.itemautocomplete", function( event ) {
				if ( self.options.disabled || self.element.propAttr( "readOnly" ) ) {
					return;
				}

				suppressKeyPress = false;
				var keyCode = $.ui.keyCode;
				switch( event.keyCode ) {
				case keyCode.PAGE_UP:
					self._move( "previousPage", event );
					break;
				case keyCode.PAGE_DOWN:
					self._move( "nextPage", event );
					break;
				case keyCode.UP:
					self._move( "previous", event );
					// prevent moving cursor to beginning of text field in some browsers
					event.preventDefault();
					break;
				case keyCode.DOWN:
					self._move( "next", event );
					// prevent moving cursor to end of text field in some browsers
					event.preventDefault();
					break;
				case keyCode.RIGHT:
					// de manière à permettre le déplacement normal dans le champ input
					// on n'intercepte l'événement que si le focus a déjà été déplacé dans
					// les propositions (avec la touche DOWN)
					if (self.currentMenu) {
						self._move( "nextMenu", event);
					}
					break;
				case keyCode.LEFT:
					// de manière à permettre le déplacement normal dans le champ input
					// on n'intercepte l'événement que si le focus a déjà été déplacé dans
					// les propositions (avec la touche DOWN)
					if (self.currentMenu) {
						self._move( "previousMenu", event);
					}
					break;
				case keyCode.ENTER:
				case keyCode.NUMPAD_ENTER:
					// when menu is open and has focus
					if ( self.currentMenu && self.currentMenu.active ) {
						// #6055 - Opera still allows the keypress to occur
						// which causes forms to submit
						suppressKeyPress = true;
						event.preventDefault();
					}
					//passthrough - ENTER and TAB both select the current element
				case keyCode.TAB:
					if ( !self.currentMenu || !self.currentMenu.active ) {
						return;
					}
					self.currentMenu.select( event );
					break;
				case keyCode.ESCAPE:
					self.element.val( self.term );
					self.close( event );
					break;
				default:
					// keypress is triggered before the input value is changed
					clearTimeout( self.searching );
					self.searching = setTimeout(function() {
						// only search if the value has changed
						if ( self.term != self.element.val() ) {
							self.selectedItem = null;
							self.search( null, event );
						}
					}, self.options.delay );
					break;
				}
			})
			.bind( "keypress.itemautocomplete", function( event ) {
				if ( suppressKeyPress ) {
					suppressKeyPress = false;
					event.preventDefault();
				}
			})
			.bind( "focus.itemautocomplete", function() {
				if ( self.options.disabled ) {
					return;
				}

				self.selectedItem = null;
				self.previous = self.element.val();
			})
			.bind( "blur.itemautocomplete", function( event ) {
				if ( self.options.disabled ) {
					return;
				}

				clearTimeout( self.searching );
				// clicks on the menu (or a button to trigger a search) will cause a blur event
				self.closing = setTimeout(function() {
					self.close( event );
					self._change( event );
				}, 150 );
			});
		this._initSource();
		this.response = function() {
			return self._response.apply( self, arguments );
		};
		this.menuContainer = $( "<div></div>" )
			.addClass( "ui-autocomplete" )
			.addClass( self.options.autocompleteClass )
			.hide()
			.appendTo( $( this.options.appendTo || "body", doc )[0] )
			// prevent the close-on-blur in case of a "slow" click on the menu (long mousedown)
			.mousedown(function( event ) {
				// clicking on the scrollbar causes focus to shift to the body
				// but we can't detect a mouseup or a click immediately afterward
				// so we have to track the next mousedown and close the menu if
				// the user clicks somewhere outside of the autocomplete
				var menuElement = self.menuContainer[0];
				if ( !$( event.target ).closest( ".ui-menu-item" ).length ) {
					setTimeout(function() {
						$( document ).one( 'mousedown', function( event ) {
							if ( event.target !== self.element[ 0 ] &&
								event.target !== menuElement &&
								!$.ui.contains( menuElement, event.target ) ) {
								self.close();
							}
						});
					}, 1 );
				}
				
				// use another timeout to make sure the blur-event-handler on the input was already triggered
				setTimeout(function() {
					clearTimeout( self.closing );
				}, 13);
			});
		this.menugroups = new Object();
		this.typeToMenugroup = new Object();
		for (var index in this.options.groups) {
			var group = this.options.groups[index];
			var singleMenuOuter = $("<div></div>").addClass("menu-outer").addClass(group.className).appendTo(this.menuContainer);
			var singleMenu = $("<div></div>").addClass("menu").addClass(group.className).appendTo(singleMenuOuter);
			if (group.title) {
				var singleMenuTitle = $("<div></div>").addClass("title").appendTo(singleMenu).text(group.title);
			}
			var menu = $("<ul></ul>")
				.menu({
					focus: function( event, ui ) {
						var item = ui.item.data( "item.itemautocomplete" );
						if ( false !== self._trigger( "focus", event, { item: item } ) ) {
							// use value to match what will end up in the input, if it was a key event
							if ( /^key/.test(event.originalEvent.type) ) {
								self.element.val( self._labelItem(item) );
							}
						}
					},
					selected: function( event, ui ) {
						var item = ui.item.data( "item.itemautocomplete" ),
							previous = self.previous;
						
						// only trigger when focus was lost (click on menu)
						if ( self.element[0] !== doc.activeElement ) {
							self.element.focus();
							self.previous = previous;
							// #6109 - IE triggers two focus events and the second
							// is asynchronous, so we need to reset the previous
							// term synchronously and asynchronously :-(
							setTimeout(function() {
								self.previous = previous;
								self.selectedItem = item;
							}, 1);
						}
	
						if ( false !== self._trigger( "select", event, { item: item } ) ) {
							self.element.val( self._labelItem(item) );
						}
						// reset the term after the select event
						// this allows custom select handling to work properly
						self.term = self.element.val();
	
						self.close( event );
						self.selectedItem = item;
					},
					blur: function( event, ui ) {
						// don't set the value of the text field if it's already correct
						// this prevents moving the cursor unnecessarily
						if ( self.menuContainer.is(":visible") &&
							( self.element.val() !== self.term ) ) {
							self.element.val( self.term );
						}
					}
				})
				.zIndex( this.element.zIndex() + 1 )
				// workaround for jQuery bug #5781 http://dev.jquery.com/ticket/5781
				.css({ top: 0, left: 0 })
				.hide()
				.appendTo(singleMenu)
				.data( "menu" );
			
			this.menugroups[group.name] = menu;
			if (this.options.groups.length == 1) {
				this.typeToMenugroup["default"] = this.menugroups[group.name]
			} else {
				for (var index in group.types) {
					this.typeToMenugroup[group.types[index]] = this.menugroups[group.name];
				}
			}
		}
		
		$("<div></div>").addClass("clear-both").appendTo(this.menuContainer);
		
		if ( $.fn.bgiframe ) {
			 this.menuContainer.bgiframe();
		}
		// turning off autocomplete prevents the browser from remembering the
		// value when navigating through history, so we re-enable autocomplete
		// if the page is unloaded before the widget is destroyed. #7790
		self.beforeunloadHandler = function() {
			self.element.removeAttr( "autocomplete" );
		};
		$( window ).bind( "beforeunload", self.beforeunloadHandler );
	},

	_labelItem: function(item) {
		return this.options.labelItem(item);
	},

	destroy: function() {
		this.element
			.removeClass( "ui-autocomplete-input" )
			.removeAttr( "autocomplete" )
			.removeAttr( "role" )
			.removeAttr( "aria-autocomplete" )
			.removeAttr( "aria-haspopup" );
		for (type in this.menugroups) {
			this.menugroups[type].element.remove();
		}
		this.menuContainer.remove();
		$( window ).unbind( "beforeunload", this.beforeunloadHandler );
		$.Widget.prototype.destroy.call( this );
	},

	_setOption: function( key, value ) {
		$.Widget.prototype._setOption.apply( this, arguments );
		if ( key === "source" ) {
			this._initSource();
		}
		if ( key === "appendTo" ) {
			this.menuContainer.appendTo( $( value || "body", this.element[0].ownerDocument )[0] )
		}
		if ( key === "disabled" && value && this.xhr ) {
			this.xhr.abort();
		}
	},

	_initSource: function() {
		var self = this,
			array,
			url;
		this.sources = new Array();
		if (this.options.sources) {
			for (var index in this.options.sources) {
				var source = this.options.sources[index];
				if ($.isArray(source)) {
					array = source;
					this.sources.push(function( request, response ) {
						response( $.ui.autocompletegroups.filter(array, request.term) );
					});
				} else if ( typeof source === "string" ) {
					url = source;
					this.sources.push(function( request, response ) {
						if ( self.xhr ) {
							self.xhr.abort();
						}
						self.xhr = $.ajax({
							url: url,
							data: request,
							dataType: "json",
							context: {
								autocompleteRequest: ++requestIndex
							},
							success: function( data, status ) {
								if ( this.autocompleteRequest === requestIndex ) {
									response( data );
								}
							},
							error: function() {
								if ( this.autocompleteRequest === requestIndex ) {
									response( [] );
								}
							}
						});
					});
				} else {
					this.sources.push(source);
				}
				
				this.source = function (request, response) {
					var responses = self.sources.length;
					var responsesBack = 0;
					var items = [];
					var callback = function(addedItems) {
						responsesBack++;
						$.merge(items, addedItems);
						// si on a reçu tous les retours, on affiche les résultats
						if (responsesBack == responses) {
							response(items);
						}
					};
					
					for (var index in self.sources) {
						var source = self.sources[index];
						source(request, callback);
					}
				}
			}
		} else {
			if ( $.isArray(this.options.source) ) {
				array = this.options.source;
				this.source = function( request, response ) {
					response( $.ui.itemautocomplete.filter(array, request.term) );
				};
			} else if ( typeof this.options.source === "string" ) {
				url = this.options.source;
				this.source = function( request, response ) {
					if ( self.xhr ) {
						self.xhr.abort();
					}
					self.xhr = $.ajax({
						url: url,
						data: request,
						dataType: "json",
						context: {
							itemautocompleteRequest: ++requestIndex
						},
						success: function( data, status ) {
							if ( this.itemautocompleteRequest === requestIndex ) {
								response( data );
							}
						},
						error: function() {
							if ( this.itemautocompleteRequest === requestIndex ) {
								response( [] );
							}
						}
					});
				};
			} else {
				this.source = this.options.source;
			}
		}
	},

	search: function( value, event ) {
		value = value != null ? value : this.element.val();

		// always save the actual value, not the one passed as an argument
		this.term = this.element.val();

		if ( value.length < this.options.minLength ) {
			return this.close( event );
		}

		clearTimeout( this.closing );
		if ( this._trigger( "search", event ) === false ) {
			return;
		}

		return this._search( value );
	},

	_search: function( value ) {
		this.pending++;
		this.element.addClass( "ui-autocomplete-loading" );

		this.source( { term: value }, this.response );
	},

	_response: function( content ) {
		if ( !this.options.disabled && content && content.length ) {
			content = this._normalize( content );
			this._suggest( content );
			this._trigger( "open" );
		} else {
			this.close();
		}
		this.pending--;
		if ( !this.pending ) {
			this.element.removeClass( "ui-autocomplete-loading" );
		}
	},

	close: function( event ) {
		clearTimeout( this.closing );
		if ( this.menuContainer.is(":visible") ) {
			this.menuContainer.hide();
			this.currentMenu = null;
			for (type in this.menugroups) {
				this.menugroups[type].element.hide();
				this.menugroups[type].deactivate();
			}
			this._trigger( "close", event );
		}
	},
	
	_change: function( event ) {
		if ( this.previous !== this.element.val() ) {
			this._trigger( "change", event, { item: this.selectedItem } );
		}
	},

	_normalize: function( items ) {
		// assume all items have the right format when the first item is complete
		if ( items.length && items[0].label && items[0].label ) {
			return items;
		}
		return $.map( items, function(item) {
			if ( typeof item === "string" ) {
				return {
					label: item,
					value: item
				};
			}
			return $.extend({
				label: item.label || item.value,
				value: item.value || item.label
			}, item );
		});
	},

	_suggest: function( items ) {
		for (type in this.menugroups) {
			this.menugroups[type].element.empty();
			this.menuContainer.zIndex( this.element.zIndex() + 1 );
		}
		this._renderMenu( items );
		// TODO refresh should check if the active item is still in the dom, removing the need for a manual deactivate
		for (type in this.menugroups) {
			this.menugroups[type].deactivate();
			this.menugroups[type].refresh();
		}
		
		// size and position menu
		for (type in this.menugroups) {
			this.menugroups[type].element.show();
		}
		this.menuContainer.show();
		this._resizeMenu();
		
		var elementPosition;
		if (this.options.positionAndWidth) {
			elementPosition = this.options.positionAndWidth;
		} else {
			elementPosition = this.element;
		}
		this.menuContainer.position( $.extend({
			of: elementPosition
		}, this.options.position ));
		
		// focus sur le premier élément du premier groupe non vide
		if ( this.options.autoFocus ) {
			this._focusFirst();
		}
	},

	_focusFirst: function() {
		var menu;
		for (var groupIndex in this.options.groups) {
			menu = this.menugroups[this.options.groups[groupIndex]["name"]];
			if (!menu.empty()) {
				break;
			}
		}
		menu.next( new $.Event("mouseover") );
		this.currentMenu = menu;
	},

	_resizeMenu: function() {
		var container = this.menuContainer;
		var elementOuterWith;
		if (this.options.positionAndWidth) {
			elementOuterWidth = this.options.positionAndWidth;
		} else {
			elementOuterWidth = this.element;
		}
		container.outerWidth( Math.max(
			// Firefox wraps long text (possibly a rounding bug)
			// so we add 1px to avoid the wrapping (#7513)
				container.width( "" ).outerWidth() + 1,
				elementOuterWidth.outerWidth()
		) );
	},

	_renderMenu: function( items ) {
		var self = this;
		$.each( items, function( index, item ) {
			self._renderItem( item );
		});
	},

	_getMenuGroup: function (item) {
		if (this.typeToMenugroup["default"] !== undefined) {
			return this.typeToMenugroup["default"].element;
		} else {
			return this.typeToMenugroup[item.type].element;
		}
	},

	_renderItem: function( item ) {
		var ul = this._getMenuGroup(item);
		var li = $( "<li></li>" )
			.data( "item.itemautocomplete", item )
			.append( $( "<a></a>" ).append( $("<span class='label'></span>").append(this.options.renderItem(item)) ) )
			.appendTo( ul );
		if (item[this.options.cssClassNameAttribute]) {
			li.addClass(item[this.options.cssClassNameAttribute]);
		}
		return li;
	},

	_move: function( direction, event ) {
		if (!this.currentMenu) {
			this._focusFirst();
			return;
		}
		if (direction == "nextMenu" || direction == "previousMenu") {
			if (this.menuContainer.is(":visible")) {
				// on cherche le menu courant dans la liste des menus
				// une fois trouvé, on cherche le premier menu non vide suivant
				var menu = null;
				var currentMenuFound = false;
				var previousMenu = null;
				for (var groupIndex in this.options.groups) {
					var menuItem = this.menugroups[this.options.groups[groupIndex]["name"]];
					if (!currentMenuFound) {
						if (menuItem == this.currentMenu) {
							currentMenuFound = true;
							if (direction == "previousMenu") {
								menu = previousMenu;
								break;
							} else {
								continue;
							}
						}
					} else {
						if (!menuItem.empty()) {
							menu = menuItem;
							break;
						}
					}
					if (!menuItem.empty()) {
						previousMenu = menuItem;
					}
				}
				
				if (menu != null) {
					if (this.currentMenu) {
						this.currentMenu.deactivate();
					}
					this.currentMenu = menu;
					direction = "next";
				} else {
					return;
				}
			} else {
				return;
			}
			// on empêche l'événement par défaut
			event.preventDefault();
		}
		if ( !this.menuContainer.is(":visible") ) {
			this.search( null, event );
			return;
		}
		if ( !this.currentMenu ) {
			return;
		}
		if ( this.currentMenu.first() && /^previous/.test(direction) ||
				this.currentMenu.last() && /^next/.test(direction) ) {
			this.element.val( this.term );
			this.currentMenu.deactivate();
			this.currentMenu = null;
			return;
		}
		this.currentMenu[ direction ]( event );
	},

	widget: function() {
		return this.menuContainer;
	}
});

$.extend( $.ui.itemautocomplete, {
	escapeRegex: function( value ) {
		return value.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
	},
	filter: function(array, term) {
		var matcher = new RegExp( $.ui.itemautocomplete.escapeRegex(term), "i" );
		return $.grep( array, function(value) {
			return matcher.test( value.label || value.value || value );
		});
	}
});

}( jQuery ));

/*
 * jQuery UI Menu (not officially released)
 * 
 * This widget isn't yet finished and the API is subject to change. We plan to finish
 * it for the next release. You're welcome to give it a try anyway and give us feedback,
 * as long as you're okay with migrating your code later on. We can help with that, too.
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Menu
 *
 * Depends:
 *	jquery.ui.core.js
 *  jquery.ui.widget.js
 */
(function($) {

$.widget("ui.menu", {
	_create: function() {
		var self = this;
		this.element
			.addClass("ui-menu ui-widget ui-widget-content ui-corner-all")
			.attr({
				role: "listbox",
				"aria-activedescendant": "ui-active-menuitem"
			})
			.click(function( event ) {
				if ( !$( event.target ).closest( ".ui-menu-item a" ).length ) {
					return;
				}
				// temporary
				event.preventDefault();
				self.select( event );
			});
		this.refresh();
	},
	
	refresh: function() {
		var self = this;

		// don't refresh list items that are already adapted
		var items = this.element.children("li:not(.ui-menu-item):has(a)")
			.addClass("ui-menu-item")
			.attr("role", "menuitem");
		
		items.children("a")
			.addClass("ui-corner-all")
			.attr("tabindex", -1)
			// mouseenter doesn't work with event delegation
			.mouseenter(function( event ) {
				self.activate( event, $(this).parent() );
			})
			.mouseleave(function() {
				self.deactivate();
			});
	},

	empty: function() {
		return !this.element || $(".ui-menu-item", this.element).length == 0;
	},

	activate: function( event, item ) {
		this.deactivate();
		if (this.hasScroll()) {
			var offset = item.offset().top - this.element.offset().top,
				scroll = this.element.scrollTop(),
				elementHeight = this.element.height();
			if (offset < 0) {
				this.element.scrollTop( scroll + offset);
			} else if (offset >= elementHeight) {
				this.element.scrollTop( scroll + offset - elementHeight + item.height());
			}
		}
		this.active = item.eq(0)
			.children("a")
				.addClass("ui-state-hover")
				.attr("id", "ui-active-menuitem")
			.end();
		this._trigger("focus", event, { item: item });
	},

	deactivate: function() {
		if (!this.active) { return; }

		this.active.children("a")
			.removeClass("ui-state-hover")
			.removeAttr("id");
		this._trigger("blur");
		this.active = null;
	},

	next: function(event) {
		this.move("next", ".ui-menu-item:first", event);
	},

	previous: function(event) {
		this.move("prev", ".ui-menu-item:last", event);
	},

	first: function() {
		return this.active && !this.active.prevAll(".ui-menu-item").length;
	},

	last: function() {
		return this.active && !this.active.nextAll(".ui-menu-item").length;
	},

	move: function(direction, edge, event) {
		if (!this.active) {
			this.activate(event, this.element.children(edge));
			return;
		}
		var next = this.active[direction + "All"](".ui-menu-item").eq(0);
		if (next.length) {
			this.activate(event, next);
		} else {
			this.activate(event, this.element.children(edge));
		}
	},

	// TODO merge with previousPage
	nextPage: function(event) {
		if (this.hasScroll()) {
			// TODO merge with no-scroll-else
			if (!this.active || this.last()) {
				this.activate(event, this.element.children(".ui-menu-item:first"));
				return;
			}
			var base = this.active.offset().top,
				height = this.element.height(),
				result = this.element.children(".ui-menu-item").filter(function() {
					var close = $(this).offset().top - base - height + $(this).height();
					// TODO improve approximation
					return close < 10 && close > -10;
				});

			// TODO try to catch this earlier when scrollTop indicates the last page anyway
			if (!result.length) {
				result = this.element.children(".ui-menu-item:last");
			}
			this.activate(event, result);
		} else {
			this.activate(event, this.element.children(".ui-menu-item")
				.filter(!this.active || this.last() ? ":first" : ":last"));
		}
	},

	// TODO merge with nextPage
	previousPage: function(event) {
		if (this.hasScroll()) {
			// TODO merge with no-scroll-else
			if (!this.active || this.first()) {
				this.activate(event, this.element.children(".ui-menu-item:last"));
				return;
			}

			var base = this.active.offset().top,
				height = this.element.height();
				result = this.element.children(".ui-menu-item").filter(function() {
					var close = $(this).offset().top - base + height - $(this).height();
					// TODO improve approximation
					return close < 10 && close > -10;
				});

			// TODO try to catch this earlier when scrollTop indicates the last page anyway
			if (!result.length) {
				result = this.element.children(".ui-menu-item:first");
			}
			this.activate(event, result);
		} else {
			this.activate(event, this.element.children(".ui-menu-item")
				.filter(!this.active || this.first() ? ":last" : ":first"));
		}
	},

	hasScroll: function() {
		return this.element.height() < this.element[ $.fn.prop ? "prop" : "attr" ]("scrollHeight");
	},

	select: function( event ) {
		this._trigger("selected", event, { item: this.active });
	}
});

}(jQuery));
