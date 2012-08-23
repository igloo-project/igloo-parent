(function ($) {
	$.fn.obfuscateEmail = function (user, domain, cc, subject, body, generateLabel) {
		var emailAddress = user + '@' + domain;
		var href = "mailto:" + emailAddress;
		
		if (cc) {
			href = href + "?cc=" + encodeURIComponent(cc.replace('/µ/g','@'));
		}
		if (subject) {
			if (cc) {
				href = href + "&subject=";
			} else {
				href = href + "?subject=";
			}
			href = href + encodeURIComponent(subject);
		}
		if (body) {
			if (cc || subject) {
				href = href + "&body=";
			} else {
				href = href + "?body=";
			}
			href = href + encodeURIComponent(body);
		}
		$(this, "a").attr("href", href);
		if (generateLabel) {
			$(this, "a").text(emailAddress);
		}
	};
})(jQuery);