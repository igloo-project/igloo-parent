(function($) {
	var methods = {
		init : function(options) {
			return this.each(function() {
				var $this = $(this);
				var gmap = $this.data('gmap');
				if (gmap == null) {
					// Create Map
					var gmap = new google.maps.Map(this, options);
					
					// Create HashMap of markers
					var markers = new Object();
					
					$this.data('gmap', {
						target : $this,
						gmap : gmap
					});
					$this.data('markers', {
						target : $this,
						markers : markers
					});
				}
			});
		},
		setOptions : function(options) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				data.gmap.setOptions(options);
			});
		},
		panTo : function(latLng) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				data.gmap.panTo(latLng);
			});
		},
		setCenter : function(latLng) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				data.gmap.setCenter(latLng);
			});
		},
		setZoom : function(zoom) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				data.gmap.setZoom(zoom);
			});
		},
		fitBounds : function(bounds) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				data.gmap.fitBounds(bounds);
			});
		},
		addMarker : function() {
			
		},
		destroy : function() {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				// Namespacing FTW
				$(window).unbind('.gmap');
				data.gmap.remove();
				data.markers.remove();
				$this.removeData();
			});
		},
	};

	$.fn.gmap = function(method) {
		// Method calling logic
		if (methods[method]) {
			return methods[method].apply(this, Array.prototype.slice.call(
					arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist on jQuery.gmap');
		}

	};

}) (jQuery);