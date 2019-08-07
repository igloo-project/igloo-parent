const Sidebar = {
	hide() {
		var sidebar = document.getElementsByClassName('sidebar');
		var $sidebar = $(sidebar);
		
		$sidebar.addClass('sidebar-closing');
		$sidebar.removeClass('sidebar-open');
		
		$(document.body).removeClass('sidebar-open');
		
		var sidebarTransitionDuration = Util.getTransitionDurationFromElement(sidebar);
		
		$sidebar
			.one(Util.TRANSITION_END, () => {
				$sidebar.removeClass('sidebar-closing');
				
				var backdrop = document.getElementsByClassName('sidebar-backdrop');
				var $backdrop = $(backdrop)
				
				$backdrop.removeClass('show');
				
				const backdropTransitionDuration = Util.getTransitionDurationFromElement(backdrop);
				
				$backdrop
					.one(Util.TRANSITION_END, () => {
						$backdrop.remove();
					})
					.emulateTransitionEnd(backdropTransitionDuration);
			})
			.emulateTransitionEnd(sidebarTransitionDuration);
	},
	
	show() {
		var backdrop = document.createElement('div')
		backdrop.className = 'sidebar-backdrop fade';
			
		var $backdrop = $(backdrop);
		
		$backdrop.appendTo(document.body);
		
		$backdrop.click(Sidebar.hide);
		
		Util.reflow(backdrop);
		
		$backdrop.addClass('show');
		
		var sidebar = document.getElementsByClassName('sidebar');
		var $sidebar = $(sidebar);
		
		const backdropTransitionDuration = Util.getTransitionDurationFromElement(backdrop);
		
		$backdrop
			.one(Util.TRANSITION_END, () => {
				$(document.body).addClass('sidebar-open');
				
				$sidebar.addClass('sidebar-opening');
				
				const sidebarTransitionDuration = Util.getTransitionDurationFromElement(sidebar);
				
				$sidebar
					.one(Util.TRANSITION_END, () => {
						$sidebar.addClass('sidebar-open');
						$sidebar.removeClass('sidebar-opening');
					})
					.emulateTransitionEnd(sidebarTransitionDuration);
			})
			.emulateTransitionEnd(backdropTransitionDuration);
	},
	
	toggle() {
		if ($(document.body).hasClass('sidebar-open')) {
			Sidebar.hide();
		} else {
			Sidebar.show();
		}
	}
}

$(function() {
	$('.navbar-toggler').click(Sidebar.toggle)
});
