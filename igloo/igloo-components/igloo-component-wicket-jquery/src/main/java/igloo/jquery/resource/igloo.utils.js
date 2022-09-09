;
var Igloo = Igloo || {};

Igloo.StringUtils = (function() {
	var accentMap = {
		'á':'a','à':'a','â':'a','ä':'a',
		'é':'e','è':'e','ê':'e','ë':'e',
		'í':'i','ì':'i','î':'i','ï':'i',
		'ó':'o','ò':'o','ô':'o','ö':'o',
		'ú':'u','ù':'u','û':'u','ü':'u',
		'ç':'c'
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