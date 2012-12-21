;
var OWSI = OWSI || {};

OWSI.StringUtils = (function() {
	var accentMap = {
		'à':'a','á':'a','â':'a','é':'e','è':'e','ê':'e','ë':'e','í':'i','ï':'i','ô':'o','ó':'o','ú':'u','û':'u','ù':'u','ç':'c'
	};

	var tagifyRegexp = /[^a-zA-Z0-9.]/g;
	var urlizeRegexp = /[^a-zA-Z0-9]/g;
	var cleanDuplicateDashes = /--+/g;

	var _trim = function(input) {
		return input.replace(/^\s+/g, '').replace(/\s+$/g, '');
	};

	var _sanitizeString = function(input, regexp) {
		if (!input) { return ''; }
		
		var result = _trim(input);
		result = removeAccents(result);
		result = result
				.replace(regexp, '-')
				.replace(cleanDuplicateDashes, '-')
				.replace(/^-/, '').replace(/-$/, '');
		return result;
	}

	var removeAccents = function(input) {
		if (!input) { return ''; }

		var s = input.toLowerCase();
		var ret = '';
		for (var i = 0; i < s.length; i++) {
			ret += accentMap[s.charAt(i)] || s.charAt(i);
		}
		return ret;
	};

	var tagify = function(input) {
		return _sanitizeString(input, tagifyRegexp);
	};

	var urlize = function(input) {
		return _sanitizeString(input, urlizeRegexp);
	};

	return {
		removeAccents: removeAccents,
		tagify: tagify,
		urlize: urlize
	}
}) ();