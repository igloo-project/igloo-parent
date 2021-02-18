$(function() {
	$(document).on('shown.bs.tab', 'a[data-toggle="tab"]', function(e) {
		history.pushState({}, '', e.target.hash);
	});
	
	var hash = document.location.hash;
	if (hash) {
		$('a[href="' + hash + '"]').tab('show');
	}
});
