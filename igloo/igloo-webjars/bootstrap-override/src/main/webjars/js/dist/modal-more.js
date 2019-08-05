var _show = $.fn.modal.Constructor.prototype.show;
$.fn.modal.Constructor.prototype.show = function show(relatedTarget) {
	this._ignoreTransitioning();
	
	this._appendToBody();
	
	_show.apply(this, relatedTarget);
};

var _hide = $.fn.modal.Constructor.prototype.hide;
$.fn.modal.Constructor.prototype.hide = function hide(event) {
	this._ignoreTransitioning();
	
	var _animate = $(this._element).hasClass('fade');
	
	if (_animate) {
		$(this._element).removeClass('fade');
	}
	
	_hide.apply(this, arguments);
	
	if (_animate) {
		$(this._element).addClass('fade');
	}
	
	this._appendToParent();
};

// Move modal to html body.
$.fn.modal.Constructor.prototype._appendToBody = function _appendToBody() {
	this._parent = $(this._element).parent();
	
	if (!$(this._parent).length || $(this._parent).is(document.body)) {
		this._parent = null;
	}
	
	if (this._parent) {
		$(this._element).appendTo(document.body);
	}
};

// Put back modal to its parent.
$.fn.modal.Constructor.prototype._appendToParent = function _appendToParent() {
	if (this._parent) {
		$(this._element).appendTo(this._parent);
	}
};

$.fn.modal.Constructor.prototype._ignoreTransitioning = function _ignoreTransitioning() {
	this._isTransitioning = false;
};

// See https://stackoverflow.com/a/19574076
$.fn.modal.Constructor.prototype._enforceFocus = function _enforceFocus() {};
