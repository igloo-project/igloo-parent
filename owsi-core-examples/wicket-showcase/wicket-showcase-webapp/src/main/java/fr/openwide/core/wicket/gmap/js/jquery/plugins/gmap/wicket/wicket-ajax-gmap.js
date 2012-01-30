function callWicket(callbackURL, result) {
	var parameters = {
			result: $.toJSON(result)
	};
	var wcall = wicketAjaxPost(callbackURL, $.param(parameters, false), null, null, null, null);
}

function callWicketFromDrawingShapes(callbackURL, polygons, polylines) {
	var parameters = {
			polygons: $.toJSON(polygons),
			polylines: $.toJSON(polylines)
	};
	var wcall = wicketAjaxPost(callbackURL, $.param(parameters, false), null, null, null, null);
}