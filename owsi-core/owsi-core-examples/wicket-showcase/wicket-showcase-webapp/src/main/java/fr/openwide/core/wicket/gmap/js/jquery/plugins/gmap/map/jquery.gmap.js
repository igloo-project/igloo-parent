function getReverseGeocodingResult(result, callbackUrl) {
	var coreGeocoderResult = new Object();
	
	coreGeocoderResult['formatted_address'] = result.formatted_address;
	coreGeocoderResult['address_components'] = result.address_components;
	coreGeocoderResult['types'] = result.types;
	coreGeocoderResult['geometry'] = new Object();
	coreGeocoderResult.geometry['location'] = new Object();
	coreGeocoderResult.geometry.location['lat'] = result.geometry.location.Pa;
	coreGeocoderResult.geometry.location['lng'] = result.geometry.location.Qa;
	coreGeocoderResult.geometry['location_type'] = result.geometry.location_type;
	coreGeocoderResult['viewport'] = new Object();
	coreGeocoderResult.viewport['southwest'] = new Object();
	coreGeocoderResult.viewport['northeast'] = new Object();
	coreGeocoderResult.viewport.southwest['lat'] = result.geometry.viewport['$'].b;
	coreGeocoderResult.viewport.southwest['lng'] = result.geometry.viewport['$'].d;
	coreGeocoderResult.viewport.northeast['lat'] = result.geometry.viewport['Y'].b;
	coreGeocoderResult.viewport.northeast['lng'] = result.geometry.viewport['Y'].d;
	
	callWicket(callbackUrl, coreGeocoderResult);
}


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
				if (typeof marker != undefined) {
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
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				//Ajout du Marker à la carte
				var marker = new google.maps.Marker(options);
				
				//Ajout du Marker dans la HashMap
				data.markers[markerId] = marker;
				
				if (autofit) {
					var bounds = data.bounds;
					if (typeof bounds == undefined) {
						bounds = new google.maps.LatLngBounds();
					}
					bounds.extend(marker.getPosition());
					data.gmap.fitBounds(bounds);
					data.gmap.setCenter(bounds.getCenter());
					data.bounds = bounds;
				}
				$this.data('gmap', data);
			});
		},
		addMarkerOnMapEvent : function(markerId, event, callback, callbackUrl, optionsMarker) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var marker = new google.maps.Marker(optionsMarker);
				marker.infowindow = new InfoBubble();
				marker.setVisible(false);
				data.markers[markerId] = marker;
				$this.data('gmap', data);
				
				google.maps.event.addListener(data.gmap, event, function(e) {
					marker.setPosition(e.latLng);
					marker.setVisible(true);
					$this.data('gmap', data);
					
					$this.gmap(callback, markerId, callbackUrl);
				});
				
				$this.gmap('updatePositionFromDragMove', markerId, callback, callbackUrl);
			});
		},
		addInfoBubble : function(markerId, event, options){
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var marker = data.markers[markerId];
				if (typeof marker != undefined) {
					marker.infowindow = new InfoBubble(options);
					
					google.maps.event.addListener(marker, event, function (e) {
						marker.infowindow.open(data.gmap, marker);
					});
				}
			});
		},
		setMarkerAnimation : function (markerId, options) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var marker = data.markers[markerId];
				if (typeof marker != undefined) {
					marker.setAnimation(options);
				}
			});
		},
		hideMarker : function(markerId) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var marker = data.markers[markerId];
				if (typeof marker != undefined) {
					marker.setMap(null);
				}
			});
		},
		hideAllMarkersExcept : function(markerId) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				for(var i in data.markers) {
					data.markers[i].setMap(null);
				}
				var marker = data.markers[markerId];
				if (typeof marker != undefined) {
					marker.setMap(data.gmap);
				}
			});
		},
		showAllMarkers : function() {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				for(var i in data.markers) {
					data.markers[i].setMap(data.gmap);
				}
			});
		},
		showMarker : function(markerId) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var marker = data.markers[markerId];
				if (typeof marker != undefined) {
					marker.setMap(data.gmap);
				}
			});
		},
		clearMarkers : function() {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				
				for(var i in data.markers) {
					data.markers[i].setMap(null);
				}
				data.markers = new Object();
				
				$this.data('gmap', data);
			});
		},
		autofit : function() {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var bounds = data.bounds;
				if (typeof bounds == undefined) {
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
			});
		},
		updatePositionFromDragMove : function(markerId, callback, callbackUrl) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var marker = data.markers[markerId];
				if (typeof marker != undefined) {
					// Fermeture de la infowindow
					google.maps.event.addListener(marker, "dragstart", function() {
						marker.infowindow.close();
					});
					
					google.maps.event.addListener(marker, "dragend", function() {
						// Mise à jour de la position
						data.markers[markerId].setPosition(marker.getPosition());
						$this.data('gmap', data);
						if (typeof callback != undefined) {
							$this.gmap(callback, markerId, callbackUrl);
						}
					});
				}
			});
		},
		geocoding : function (address, callback) {
			return this.each(function() {
				var geocoder = new google.maps.Geocoder();
				geocoder.geocode({'address': address}, function(results, status) {
					if (status == google.maps.GeocoderStatus.OK) {
						return callback(results[0].geometry.location);
					} else {
						Wicket.Log.info(status);
					}
				});
			});
		},
		reverseGeocoding : function(latLng, markerId, callback, callbackUrl) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var geocoder = new google.maps.Geocoder();
				var marker = data.markers[markerId];
				geocoder.geocode({'latLng': latLng}, function(results, status) {
					if (status == google.maps.GeocoderStatus.OK) {
						if (results[0]) {
							if (typeof marker != undefined) {
								marker.infowindow.setContent(results[0].formatted_address);
								marker.infowindow.open(data.gmap, marker);
							}
							if(typeof callbackUrl == undefined) {
								callback(results[0]);
							} else {
								callback(results[0], callbackUrl);
							}
						}
					} else {
						Wicket.Log.info(status);
					}
				});
			});
		},
		reverseGeocodingOnMarker : function (markerId, callbackUrl) {
			var $this = $(this), data = $this.data('gmap');
			var marker = data.markers[markerId];
			if (typeof marker != undefined) {
				$this.gmap('reverseGeocoding', marker.getPosition(), markerId, getReverseGeocodingResult, callbackUrl);
			}
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