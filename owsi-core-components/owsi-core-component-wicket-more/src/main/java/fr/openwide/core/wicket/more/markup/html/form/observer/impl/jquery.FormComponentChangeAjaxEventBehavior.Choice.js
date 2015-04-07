// Copied from AjaxFormChoiceComponentUpdatingBehavior.js, with rewrites

;var OWSI = OWSI || {};

;(function (undefined) {
	"use strict";

	// introduce a namespace
	OWSI.FormComponentChangeAjaxEventBehavior = OWSI.FormComponentChangeAjaxEventBehavior || {};
	if (typeof (OWSI.FormComponentChangeAjaxEventBehavior.Choice) === "undefined") {
		OWSI.FormComponentChangeAjaxEventBehavior.Choice = {};
		/**
		 * Get all checked input values.
		 *
		 * @param name input name of choice
		 * @param attrs ajax attributes
		 */
		OWSI.FormComponentChangeAjaxEventBehavior.Choice.getInputValues = function(name, attrs) {
			return $("#" + attrs.c)
					.find('input[name="' + name + '"]:checked')
					.map(function (index, input) {
						return {'name': input.name, 'value': input.value};
					})
					.toArray();
		};
	}
})();
