function getReverseGeocodingResult(result, callbackUrl) {
	var coreGeocoderResult = new Object();
		
	coreGeocoderResult['formatted_address'] = result.formatted_address;
	coreGeocoderResult['address_components'] = result.address_components;
	coreGeocoderResult['types'] = result.types;
	
	coreGeocoderResult['geometry'] = new Object();
	coreGeocoderResult.geometry['location'] = new Object();
	var googleLocation = result.geometry.location.valueOf();
	coreGeocoderResult.geometry.location['lat'] = googleLocation.lat();
	coreGeocoderResult.geometry.location['lng'] = googleLocation.lng();
	coreGeocoderResult.geometry['location_type'] = result.geometry.location_type;
	
	coreGeocoderResult.geometry['viewport'] = new Object();
	var googleViewport = result.geometry.viewport.valueOf();
	var southwest = googleViewport.getSouthWest();
	var northeast = googleViewport.getNorthEast();
	coreGeocoderResult.geometry.viewport['southwest'] = new Object();
	coreGeocoderResult.geometry.viewport['northeast'] = new Object();
	coreGeocoderResult.geometry.viewport.southwest['lat'] = southwest.lat();
	coreGeocoderResult.geometry.viewport.southwest['lng'] = southwest.lng();
	coreGeocoderResult.geometry.viewport.northeast['lat'] = northeast.lat();
	coreGeocoderResult.geometry.viewport.northeast['lng'] = northeast.lng();
	
	callWicket(callbackUrl, coreGeocoderResult);
}

function getDirectionsResult(result, callbackUrl) {
	var coreDirectionsResult = new Object();
	
	coreDirectionsResult['polyline'] = result.routes[0].overview_polyline.points.valueOf();
	coreDirectionsResult['status'] = result.status;
	
	callWicket(callbackUrl, coreDirectionsResult);
}

function getShapes(callbackUrl, polygons, polylines) {
	var corePolygonResult = "";
	var corePolylineResult = "";
	
	if (polygons.length > 0) {
		corePolygonResult = cutShapeResult(corePolygonResult, polygons, true);
	}
	if (polylines.length > 0) {
		corePolylineResult = cutShapeResult(corePolylineResult, polylines, false);
	}
	if (polygons.length > 0 || polylines.length > 0) {
		callWicketFromDrawingShapes(callbackUrl, corePolygonResult, corePolylineResult);
	}
}

function cutShapeResult(result, parameters, polygon) {
	if (polygon) {
		result = result + "MULTIPOLYGON(";
	} else {
		if (parameters.length > 1) {
			result = result + "MULTILINESTRING(";
		} else {
			result = result + "LINESSTRING";
		}
	}
	var first_point;
	for (var i = 0; i < parameters.length; i++) {

		if (i > 0) {
			result = result + ",(";
		} else {
			result = result + "(";
		}
		var shape = parameters[i];
		if (polygon) {
			result = result + "(";
		}
		shape.latLngs.getArray().forEach(function(elem, index) {
			if (elem.getLength() > 0) {
				result = result + elem.getAt(0).lng() + " "+ elem.getAt(0).lat();
				first_point = elem.getAt(0);
				for(var j = 1; j < elem.getLength(); j++) {
					result = result + "," + elem.getAt(j).lng() + " " + elem.getAt(j).lat();
				}
				if (polygon) {
					result = result + "," + first_point.lng() + " " + first_point.lat(); 
				}
			}
		});
		if (polygon) {
			result = result + ")";
		}
		result = result + ")";
	}
	if (polygon) {
		result = result + ")";
	} else {
		if (parameters.length > 1) {
			result = result + ")";
		}
	}
	return result;
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
					
					// Create Array of polygons
					var polygons = new Array();
					
					// Create Array of polylines
					var polylines = new Array();
					
					// Create bounds
					var bounds = new google.maps.LatLngBounds();
					
					// Create Direction Service
					var directionsService = new google.maps.DirectionsService();
					
					// Create Direction Renderer
					var directionsDisplay = new google.maps.DirectionsRenderer();
					
					// Create Colors
					var COLORS = [["red", "#ff0000"], ["orange", "#ff8800"], ["green","#008000"], ["blue", "#000080"], ["yellow", "#ffff00"]];
					var colorIndex = 0;
					
					$this.data('gmap', {
						target : $this,
						gmap : gmap,
						markers : markers,
						polygons : polygons,
						polylines : polylines,
						bounds : bounds,
						colors : COLORS,
						colorIndex : colorIndex,
						directionsService : directionsService,
						directionsDisplay : directionsDisplay,
						drawingManager : undefined,
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
				data.markers[markerId] = marker;
				data.markers[markerId].infowindow = new InfoBubble();
				data.markers[markerId].setVisible(false);
				$this.data('gmap', data);
				
				google.maps.event.addListener(data.gmap, event, function(e) {
					data.markers[markerId].setPosition(e.latLng);
					data.markers[markerId].setVisible(true);
					$this.data('gmap', data);
					
					$this.gmap(callback, markerId, callbackUrl);
				});
				$this.gmap('updatePositionFromDragMove', markerId, callback, callbackUrl);
				
				google.maps.event.addListener(data.markers[markerId], event, function(e) {
					data.markers[markerId].infowindow.close();
					data.markers[markerId].setVisible(false);
					$this.data('gmap', data);
				});
			});
		},
		addInfoBubble : function(markerId, event, options, content) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var marker = data.markers[markerId];
				if (typeof marker != undefined) {
					marker.infowindow = new InfoBubble(options);
					marker.infowindow.setContent(content.html());
					
					google.maps.event.addListener(marker, event, function (e) {
						marker.infowindow.open(data.gmap, marker);
					});
					$this.data('gmap', data);
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
					if (data.markers[i].getMap() == data.gmap) {
						bounds.extend(data.markers[i].getPosition());
					}
				}
				for (var i in data.polygons) {
					var polygon = data.polygons[i];
					var paths = polygon.getPaths();
					for (var p = 0; p < paths.getLength(); p++) {
						path = paths.getAt(p);
						for (var j = 0; j < path.getLength(); j++) {
							bounds.extend(path.getAt(j));
						}
					}
				}
				for (var i in data.polylines) {
					data.polylines[i].getPath().forEach(function(latlng) { bounds.extend(latlng); } ); 
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
						$.error('Geocoding: ' + status);
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
						$.error('ReverseGeocoding: ' + status);
					}
				});
			});
		},
		reverseGeocodingOnMarker : function (markerId, callbackUrl) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var marker = data.markers[markerId];
				if (typeof marker != undefined) {
					$this.gmap('reverseGeocoding', marker.getPosition(), markerId, getReverseGeocodingResult, callbackUrl);
				}
			});
		},
		route : function(request, callback, callbackUrl) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				if (typeof data.directionsService != undefined) {
					data.directionsService.route(request, function(response, status) {
						if (status == google.maps.DirectionsStatus.OK) {
							$this.gmap(callback, response, callbackUrl);
						} else {
							$.error('Route: ' + status);
						}
					});
				}
			});
		},
		routeDisplay : function(response, callbackUrl) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				data.directionsDisplay.setMap(data.gmap);
				
				data.directionsDisplay.setDirections(response);
				$this.data('gmap', data);
				
				getDirectionsResult(response, callbackUrl);
			});
		},
		clearRouteDisplay : function () {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				data.directionsDisplay.setMap(null);
				$this.data('gmap', data);
			});
		},
		createDrawingManager : function (options) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				// Create DrawingManager
				var drawingManager = undefined;
				if (google.maps.drawing) {
					drawingManager = new google.maps.drawing.DrawingManager(options);
					drawingManager.setMap(data.gmap);
					
					google.maps.event.addListener(drawingManager, 'overlaycomplete', function(event) {
						// Next Colors
						var color = $this.gmap('getColor', false);
						var polygonOptions;
						var polylineOptions;
						if (options.polygonOptions != undefined) {
							polygonOptions = options.polygonOptions;
						} else {
							polygonOptions = new Object();
						}
						if (options.polylineOptions != undefined) {
							polylineOptions = options.polylineOptions;
						} else {
							polylineOptions = new Object();
						}
						
						polygonOptions['fillColor'] = color;
						polygonOptions['strokeColor'] = color;
						polylineOptions['strokeColor'] = color;
						
						options['polygonOptions'] = polygonOptions;
						options['polylineOptions'] = polylineOptions;
						drawingManager.setOptions(options);
						
						// Polygon
						if (event.type == google.maps.drawing.OverlayType.POLYGON) {
							data.polygons[data.polygons.length] = event.overlay;
							
							$this.data('gmap', data);
						}
						// Polyline
						if (event.type == google.maps.drawing.OverlayType.POLYLINE) {
							data.polylines[data.polylines.length] = event.overlay;
							$this.data('gmap', data);
						}
					});
					
					data.drawingManager = drawingManager;
					$this.data('gmap', data);
				} else {
					$.error('Drawing Library is not loaded, you must load it to be able to create a drawing manager');
				}
			});
		},
		submitShapes : function (callbackUrl) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				getShapes(callbackUrl, data.polygons, data.polylines);
			});
		},
		clearShapes : function () {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				for(var i in data.polygons) {
					data.polygons[i].setMap(null);
				}
				for(var i in data.polylines) {
					data.polylines[i].setMap(null);
				}
				
				data.polygons = new Array();
				data.polylines = new Array();
				
				$this.data('gmap', data);
			});
		},
		createPolygon : function (polygonId, options) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var polygon = new google.maps.Polygon(options);
				polygon.setMap(data.gmap);
				
				data.polygons[polygonId] = polygon;
				$this.data('gmap', data);
			});
		},
		createPolyline : function (polylineId, options) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var polyline = new google.maps.Polyline(options);
				polyline.setMap(data.gmap);
				
				data.polylines[polylineId] = polyline;
				$this.data('gmap', data);
			});
		},
		getColor : function (named) {
			var $this = $(this), data = $this.data('gmap');
			data.colorIndex++;
			$this.data('gmap', data);
			return data.colors[(data.colorIndex) % data.colors.length][named ? 0 : 1];
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