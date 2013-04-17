/* French initialisation for the jQuery UI month picker plugin. */
jQuery(function($){
	$.monthpicker.regional['fr'] = {
		prevText: 'Précédent',
		nextText: 'Suivant',
		monthNames: ['Janvier','Février','Mars','Avril','Mai','Juin',
		'Juillet','Août','Septembre','Octobre','Novembre','Décembre'],
		monthNamesShort: ['Janv.','Févr.','Mars','Avril','Mai','Juin',
		'Juil.','Août','Sept.','Oct.','Nov.','Déc.'],
		dateFormat: 'mm/yy',
		yearSuffix: ''
	};
	$.monthpicker.setDefaults($.monthpicker.regional['fr']);
});