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
			var $this = $(this);
			var gmap = $this.data('gmap');
			gmap.setOptions(options);
		},
		panTo : function(latLng) {
			return this.each(function() {
				var $this = $(this);
				var gmap = $this.data('gmap');
				gmap.panTo(latLng);
			});
		},
		setCenter : function(latLng) {
			return this.each(function() {
				var $this = $(this);
				var gmap = $this.data('gmap');
				gmap.setCenter(latLng);
			});
		},
		fitBounds : function(bounds) {
			return this.each(function() {
				var $this = $(this);
				var gmap = $this.data('gmap');
				gmap.fitBounds(bounds);
			});
		},
		addMarker : function() {
			
		}
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