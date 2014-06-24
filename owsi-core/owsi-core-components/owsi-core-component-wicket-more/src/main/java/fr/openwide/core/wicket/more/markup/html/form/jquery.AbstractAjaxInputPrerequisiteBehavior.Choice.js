// Copied from AjaxFormChoiceComponentUpdatingBehavior.js, with rewrites

;var OWSI = OWSI || {};

;(function (undefined) {
	"use strict";

	// introduce a namespace
	OWSI.AbtractAjaxInputPrerequisiteBehavior = OWSI.AbtractAjaxInputPrerequisiteBehavior || {};
	if (typeof (OWSI.AbtractAjaxInputPrerequisiteBehavior.Choice) === "undefined") {
		OWSI.AbtractAjaxInputPrerequisiteBehavior.Choice = {};

		/**
		 * Is a change accepted.
		 *
		 * @param name input name of choice
		 * @param attrs ajax attributes
		 */
		OWSI.AbtractAjaxInputPrerequisiteBehavior.Choice.acceptInput = function(name, attrs) {
			var srcElement = attrs.event.target;
			return (srcElement.name === name);
		};

		/**
		 * Get all checked input values.
		 *
		 * @param name input name of choice
		 * @param attrs ajax attributes
		 */
		OWSI.AbtractAjaxInputPrerequisiteBehavior.Choice.getInputValues = function(name, attrs) {
			return $("#" + attrs.c)
					.find('input[name="' + name + '"]:checked')
					.map(function (index, input) {
						return {'name': input.name, 'value': input.value};
					})
					.toArray();
		};
	}
})();
