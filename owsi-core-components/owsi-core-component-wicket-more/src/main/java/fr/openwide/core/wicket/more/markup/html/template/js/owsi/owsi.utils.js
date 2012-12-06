;
var OWSI = OWSI || {};

OWSI.StringUtils = (function() {
	var accentMap = {
		'à':'a','á':'a','â':'a','é':'e','è':'e','ê':'e','ë':'e','í':'i','ï':'i','ô':'o','ó':'o','ú':'u','û':'u','ù':'u','ç':'c'
	};
	
	return {
		removeAccents: function(input) {
			if (!input) { return ''; }
	
			var s = input.toLowerCase();
			var ret = '';
			for (var i = 0; i < s.length; i++) {
				ret += accentMap[s.charAt(i)] || s.charAt(i);
			}
			return ret;
		}
	}
}) ();