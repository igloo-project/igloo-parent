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
					
					// Create bounds
					var bounds = new google.maps.LatLngBounds();
					
					$this.data('gmap', {
						target : $this,
						gmap : gmap,
						markers : markers,
						bounds : bounds
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
		panToMarker : function(markerId) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var marker = data.markers[markerId];
				if (typeof marker != 'undefined') {
					data.gmap.panTo(marker.getPosition());
				}
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
		addMarker : function(markerId, options, autofit) {
			var $this = $(this), data = $this.data('gmap');
			//Ajout du Marker à la carte
			var marker = new google.maps.Marker(options);
			
			//Ajout du Marker dans la HashMap
			data.markers[markerId] = marker;
			
			if (autofit) {
				var bounds = data.bounds;
				if (typeof bounds == 'undefined') {
					bounds = new google.maps.LatLngBounds();
				}
				bounds.extend(marker.getPosition());
				data.gmap.fitBounds(bounds);
				data.gmap.setCenter(bounds.getCenter());
				data.bounds = bounds;
			}
			$this.data('gmap', data);
		},
		addMarkerOnMapEvent : function(event, optionsMarker, autofit) {
			var $this = $(this), data = $this.data('gmap');
			
			google.maps.event.addListener(data.gmap, event, function(e) {
				optionsMarker['position'] = e.latLng;
				$this.gmap('addMarker','createByEvent', optionsMarker, autofit);
			});
		},
		addInfoBubble : function(markerId, event, options){
			var $this = $(this), data = $this.data('gmap');
			var marker = data.markers[markerId];
			if (typeof marker != 'undefined') {
				marker.infowindow = new InfoBubble(options);
				
				google.maps.event.addListener(marker, event, function (e) {
					marker.infowindow.open(data.gmap, marker);
				});
			}
		},
		setMarkerAnimation : function (markerId, options) {
			var $this = $(this), data = $this.data('gmap');
			var marker = data.markers[markerId];
			if (typeof marker != 'undefined') {
				marker.setAnimation(options);
			}
		},
		hideMarker : function(markerId) {
			var $this = $(this), data = $this.data('gmap');
			var marker = data.markers[markerId];
			if (typeof marker != 'undefined') {
				marker.setMap(null);
			}
		},
		hideAllMarkersExcept : function(markerId) {
			var $this = $(this), data = $this.data('gmap');
			for(var i in data.markers) {
				data.markers[i].setMap(null);
			}
			var marker = data.markers[markerId];
			if (typeof marker != 'undefined') {
				marker.setMap(data.gmap);
			}
		},
		showAllMarkers : function() {
			var $this = $(this), data = $this.data('gmap');
			for(var i in data.markers) {
				data.markers[i].setMap(data.gmap);
			}
		},
		showMarker : function(markerId) {
			var $this = $(this), data = $this.data('gmap');
			var marker = data.markers[markerId];
			if (typeof marker != 'undefined') {
				marker.setMap(data.gmap);
			}
		},
		clearMarkers : function() {
			var $this = $(this), data = $this.data('gmap');
			
			for(var i in data.markers) {
				data.markers[i].setMap(null);
			}
			data.markers = new Object();
			
			$this.data('gmap', data);
		},
		autofit : function() {
			var $this = $(this), data = $this.data('gmap');
			var bounds = data.bounds;
			if (typeof bounds == 'undefined') {
				bounds = new google.maps.LatLngBounds();
			}
			for(var i in data.markers) {
				if (data.markers[i].getMap() == data.gmap){
					bounds.extend(data.markers[i].getPosition());
				}
			}
			data.gmap.fitBounds(bounds);
			data.gmap.setCenter(bounds.getCenter());
			data.bounds = bounds;
			
			$this.data('gmap', data);
		},
		getPositionFromDragMove : function(markerId) {
			var $this = $(this), data = $this.data('gmap');
			var marker = data.markers[markerId];
			if (typeof marker != 'undefined') {
				// Close the InfoWindow
				google.maps.event.addListener(marker, "dragstart", function() {
					marker.infowindow.close();
				});
				
				google.maps.event.addListener(marker, "dragend", function() {
					alert(marker.getPosition());
					data.markers[markerId].setPosition(marker.getPosition());
				});
			}
			$this.data('gmap', data);
		},
		geocoding : function (address) {
			var geocoder = new google.maps.Geocoder();
			geocoder.geocode({'address': address}, function(results, status) {
				if (status == google.maps.GeocoderStatus.OK) {
					return results[0].geometry.location;
				} else {
					return status;
				}
			});
		},
		reverseGeocoding : function(latLng) {
			var geocoder = new google.maps.Geocoder();
			geocoder.geocode({'latLng': latlng}, function(results, status) {
				if (status == google.maps.GeocoderStatus.OK) {
					return results;
//					if (results[1]) {
//						map.setZoom(11);
//						marker = new google.maps.Marker({
//							position : latlng,
//							map : map
//						});
//						infowindow.setContent(results[1].formatted_address);
//						infowindow.open(map, marker);
//					} else {
//						alert("No results found");
//					}
				} else {
					return status;
					alert("Geocoder failed due to: " + status);
				}
			});
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