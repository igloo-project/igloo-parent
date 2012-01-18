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
						gmap : gmap,
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
		addMarker : function(markupId, options) {
			var $this = $(this), data = $this.data('gmap');
			//Ajout du Marker à la carte
			var marker = new google.maps.Marker(options);
			
			//Ajout du Marker dans la HashMap
			data.markers[markupId] = marker;
			
			$this.data('gmap', data);
		},
		addInfoBubble : function(markupId, event, options){
			var $this = $(this), data = $this.data('gmap');
			var marker = data.markers[markupId];
			var infobubble = new InfoBubble(options);
			
			google.maps.event.addListener(marker, event, function (e) {
				infobubble.open(data.gmap, marker);
			});
		},
		setMarkerAnimation : function (markupId, options) {
			var $this = $(this), data = $this.data('gmap');
			data.markers[markupId].setAnimation(options);
		},
		hideMarker : function(markupId) {
			var $this = $(this), data = $this.data('gmap');
			data.markers[markupId].setMap(null);
		},
		hideMarkers : function(markupId) {
			var $this = $(this), data = $this.data('gmap');
			for(var i in data.markers) {
				data.markers[i].setMap(null);
			}
			data.markers[markupId].setMap(data.gmap);
		},
		showAllMarkers : function() {
			var $this = $(this), data = $this.data('gmap');
			for(var i in data.markers) {
				data.markers[i].setMap(data.gmap);
			}
		},
		showMarker : function(markupId) {
			var $this = $(this), data = $this.data('gmap');
			data.markers[markupId].setMap(data.gmap);
		},
		clearMarkers : function() {
			var $this = $(this), data = $this.data('gmap');
			
			for(var i in data.markers) {
					data.markers[i].setMap(null);
			}
			data.markers = new Object();
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