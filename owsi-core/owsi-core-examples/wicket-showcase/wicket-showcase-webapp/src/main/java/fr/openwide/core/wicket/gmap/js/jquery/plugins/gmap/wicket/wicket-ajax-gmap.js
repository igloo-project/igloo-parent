function callWicket(callbackURL, result) {
	var parameters = {
			result: $.toJSON(result)
	};
	var wcall = wicketAjaxPost(callbackURL, $.param(parameters, false), null, null, null, null);
}