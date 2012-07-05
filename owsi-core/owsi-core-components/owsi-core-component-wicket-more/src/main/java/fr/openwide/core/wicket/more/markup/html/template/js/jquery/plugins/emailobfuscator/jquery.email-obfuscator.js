(function ($) {
	$.fn.obfuscateEmail = function (user, domain, subject, body, generateLabel) {
		var emailAddress = user + '@' + domain;
		var href = "mailto:" + emailAddress;
		if (subject) {
			href = href + "?subject=" + encodeURIComponent(subject);
		}
		if (body) {
			if (subject) {
				href = href + "&body="
			} else {
				href = href + "?body="
			}
			href = href + encodeURIComponent(body);
		}
		$(this, "a").attr("href", href);
		if (generateLabel) {
			$(this, "a").text(emailAddress);
		}
	};
})(jQuery);