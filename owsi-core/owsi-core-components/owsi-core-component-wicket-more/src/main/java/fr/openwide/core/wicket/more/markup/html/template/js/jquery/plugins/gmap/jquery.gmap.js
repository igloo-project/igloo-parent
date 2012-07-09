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
/* ne prend pas en compte les KmlLayers */
function getShapes(callbackUrl, polygons, polylines) {
	var corePolygonResult = "";
	var corePolylineResult = "";
	
	var polygonsCount = 0;
	for (var i in polygons) {
		polygonsCount++;
	}
	var polylinesCount = 0;
	for (var i in polylines) {
		polylinesCount++;
	}
	
	if (polygonsCount > 0) {
		corePolygonResult = cutShapeResult(corePolygonResult, polygons, true);
	}
	if (polylinesCount > 0) {
		corePolylineResult = cutShapeResult(corePolylineResult, polylines, false);
	}
	if (polygonsCount > 0 || polylinesCount > 0) {
		callWicketFromDrawingShapes(callbackUrl, corePolygonResult, corePolylineResult);
	}
}

function cutShapeResult(result, parameters, polygon) {
	var parametersCount = 0;
	for (var i in parameters) {
		parametersCount++;
	}
	
	if (polygon) {
		result = result + "MULTIPOLYGON(";
	} else {
		if (parametersCount > 1) {
			result = result + "MULTILINESTRING(";
		} else {
			result = result + "LINESSTRING";
		}
	}
	var first_point;

	for (var i in parameters) {
		shape = parameters[i];
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
	
	if (!window.console) {
		var console = { log: function() {} };
	} else {
		var console = window.console;
	}
	
	var methods = {
		init : function(options) {
			options = $.extend({}, $.fn.gmap.defaults, options);
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
					
					// create Array of circles
					var circles = new Array();
					
					// create Array of kmlLayers
					var kmlLayers = new Array();
					
					// create Array of infoWindows
					var infoWindows = new Array();
					
					// Create bounds
					var bounds = new google.maps.LatLngBounds();
					
					// Create Direction Service
					var directionsService = new google.maps.DirectionsService();
					
					// Create Direction Renderer
					var directionsDisplay = new google.maps.DirectionsRenderer();
					
					// Create Colors
					var COLORS = [["red", "#ff0000"], ["orange", "#ff8800"], ["green","#008000"], ["blue", "#000080"], ["skyblue", "#3BB9FF"]];
					var colorIndex = 0;
					
					google.maps.event.addListener(gmap, 'bounds_changed', $.proxy(function() {
						$this.trigger("boundschanged", $this.data('gmap').gmap);
					}, this));
					
					google.maps.event.addListener(gmap, 'maptypeid_changed', $.proxy(function() {
						$this.trigger("maptypeidchanged", $this.data('gmap').gmap);
					}, this));
					
					$this.data('gmap', {
						target : $this,
						gmap : gmap,
						markers : markers,
						polygons : polygons,
						polylines : polylines,
						kmlLayers : kmlLayers,
						infoWindows : infoWindows,
						circles : circles,
						bounds : bounds,
						colors : COLORS,
						colorIndex : colorIndex,
						directionsService : directionsService,
						directionsDisplay : directionsDisplay,
						drawingManager : undefined,
						pluginDefaultCenter: options.pluginDefaultCenter,
						pluginDefaultZoom: options.pluginDefaultZoom,
						pluginZoomOneMarker: options.pluginZoomOneMarker
					});
					// événement qui permet aux autres éléments d'une page de commencer à
					// manipuler la carte (ajout de markers, de formes, ...)
					$this.trigger("gmap.initialized");
				}
			});
		},
		setDefaultCenterZoom: function() {
			var $this = $(this), data = $this.data('gmap');
			if (data.gmap.pluginDefaultCenter && data.gmap.pluginDefaultZoom) {
				data.gmap.setCenter(data.gmap.pluginDefaultCenter);
				data.gmap.setZoom(data.gmap.pluginDefaultZoom);
			}
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
		getBounds : function() {
			return $(this).data('gmap').gmap.getBounds();
		},
		setZoom : function(zoom) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				data.gmap.setZoom(zoom);
			});
		},
		getZoom : function() {
			return $(this).data('gmap').gmap.getZoom();
		},
		fitBounds : function(bounds) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				data.gmap.fitBounds(bounds);
			});
		},
		addKmlLayer: function(layerId, url, options) { /* use options { preserveViewport: true } to disable autofit */
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var layer = new google.maps.KmlLayer(url, options);
				layer.setMap(data.gmap);
				data.kmlLayers[layerId] = layer;
				
				$this.data('gmap', data);
			});
		},
		addMarker : function(markerId, options, autofit) {
			var $this = $(this), data = $this.data('gmap');
			//Ajout du Marker à la carte
			var marker = new google.maps.Marker(options);
			marker.setMap(data.gmap);
			
			google.maps.event.addListener(marker, 'click', function() {
				// fermeture des infoWindows existantes (sauf si c'est celle qu'on veut afficher)
				for (identifier in data.infoWindows) {
					if (identifier != markerId) {
						data.infoWindows[identifier].close();
						delete data.infoWindows[identifier];
					}
				}
				// récupération ou création de l'infoWindow et affichage
				var infoWindow;
				if (data.infoWindows[markerId]) {
					infoWindow = data.infoWindows[markerId];
				} else {
					infoWindow = new google.maps.InfoWindow();
					if (options.url) {
						infoWindow.setContent(
								$("<div><div>").append(
									$("<div class='gmap-infowindow'></div>").append(
											$("<a></a>").attr("href", options.url).text(options.title)
									)
								).html()
						);
					} else {
						infoWindow.setContent($("<div><div>").append($("<div class='gmap-infowindow'></div>").text(options.title)).html());
					}
				}
				infoWindow.open(data.gmap, marker);
				// enregistrement de l'infoWindow
				data.infoWindows[markerId] = infoWindow;
			});
			
			//Ajout du Marker dans la HashMap
			data.markers[markerId] = marker;
			
			if (autofit && marker.getVisible()) {
				$this.gmap("autofit");
			}
			
			$this.data('gmap', data);
			return marker;
		},
		addMarkersFromMarkup : function(selector, options, autofit, geocode, geocodeSelector, geocodeZoom) {
			return this.each(function() {
				// Dans le cas où on a plusieurs fois le même indicateur à installer, on place deux indicateurs
				// et un seul marqueur. Le lien du marqueur sur la carte fait un scroll jusqu'au premier marqueur
				// de la sélection.
				var $map = $(this);
				var $elements = $(selector);
				var objects = Array();
				$elements.each(function(index) {
					var $this = $(this);
					var latitude = $this.attr("data-gmap-latitude");
					var longitude = $this.attr("data-gmap-longitude");
					var object = {
						elements: [ $(this) ],
						identifier: $this.attr("data-gmap-identifier"),
						titre: $this.attr("data-gmap-titre"),
						icon: $this.attr("data-gmap-icon"),
						iconHeight: parseInt($this.attr("data-gmap-icon-height")),
						iconWidth: parseInt($this.attr("data-gmap-icon-width")),
						iconXOffset: parseInt($this.attr("data-gmap-icon-x-offset")),
						iconYOffset: parseInt($this.attr("data-gmap-icon-y-offset")),
						iconXPointer: parseInt($this.attr("data-gmap-icon-x-pointer")),
						iconYPointer: parseInt($this.attr("data-gmap-icon-y-pointer")),
						updateIndicatorSelector: $this.attr("data-gmap-update-indicator-selector"),
						position: new google.maps.LatLng(latitude, longitude),
					};
					
					var existingObject = null;
					for (var objectIndex in objects) {
						var otherObject = objects[objectIndex];
						if (otherObject.identifier == object.identifier) {
							existingObject = otherObject;
						}
					}
					
					if (existingObject) {
						existingObject.elements.push($this);
					} else {
						objects.push(object);
					}
					console.log(object.identifier, object.titre, latitude, longitude, object.position);
				});
				
				jQuery.each(objects, function(index) {
					var object = this;
					// si l'élément n'est pas déjà sur la carte
					if (!$map.data('gmap').markers || !$map.data('gmap').markers[object.identifier]) {
						markerOptions = { jqueryGmapIndex: index };
						$.extend(markerOptions, options, {
							position : object.position,
							title : object.titre,
							icon : new google.maps.MarkerImage(object.icon,
									new google.maps.Size(object.iconWidth, object.iconHeight),
									new google.maps.Point(index * object.iconXOffset, object.iconYOffset),
									new google.maps.Point(object.iconXPointer, object.iconYPointer)
							)
						});
						var marker = $map.gmap("addMarker", object.identifier, markerOptions, false);
					} else {
						var marker = $map.data('gmap').markers[identifier];
					}
					
					if (object.updateIndicatorSelector != null && object.updateIndicatorSelector != '') {
						for (var elementIndex in object.elements) {
							var element = object.elements[elementIndex];
							element.css({ visibility: "visible", backgroundPosition: "" + - (object.iconXOffset * index).toString() + "px" + " " + object.iconYOffset.toString() + "px"});
							element.on("click.gmap", function() {
								$map.gmap("setMarkerAnimation", object.identifier, google.maps.Animation.BOUNCE, 2000);
							});
						}
						
						google.maps.event.addListener(marker, "click", function() {
							if($.fn.scrollTo) {
								$.fn.scrollTo(object.elements[0]);
							}
						});
					}
				});
				
				if (autofit && $elements.size() > 0) {
					$map.gmap("autofit");
				} else if(geocodeSelector && geocodeZoom && autofit && $elements.size() == 0) {
					var adresse = $(geocodeSelector).attr("data-gmap-geocode-adresse");
					var geocoder = new google.maps.Geocoder();
					geocoder.geocode(
						{
							address: adresse,
							region: "FR"
						},
						function(results, status) {
							if (results && results.length > 0) {
								var data = $map.data("gmap");
								data.gmap.setCenter(
									new google.maps.LatLng(
										results[0].geometry.location.lat(),
										results[0].geometry.location.lng()
									)
								);
								data.gmap.setZoom(geocodeZoom);
							}
						}
					);
				}
			});
		},
		removeMarkersFromMarkup : function (selector, autofit) {
			return this.each(function() {
				var $map = $(this);
				var $elements = $(selector);
				$elements.each(function(index) {
					var $this = $(this);
					var identifier = $this.attr("data-gmap-identifier");
					var updateIndicatorSelector = $this.attr("data-gmap-update-indicator-selector");
					$map.gmap("removeMarker", identifier);
					
					if (updateIndicatorSelector != null && updateIndicatorSelector != '') {
						$this.off("click.gmap");
						$this.css({ backgroundPosition: null, visibility: "hidden" });
					}
				});
				
				if (autofit && $elements.size() > 0) {
					$map.gmap("autofit");
				}
			});
		},
		toggleMarkersFromMarkup : function(selector, show, options, autofit) {
			return this.each(function() {
				var $map = $(this);
				if (show) {
					$map.gmap("addMarkersFromMarkup", selector, options, autofit);
				} else {
					$map.gmap("removeMarkersFromMarkup", selector, autofit);
				}
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
		setMarkerAnimation : function (markerId, options, timeout) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var marker = data.markers[markerId];
				if (typeof marker != undefined) {
					marker.setAnimation(options);
				}
				if (timeout != null) {
					setTimeout(function() {
						$this.gmap("setMarkerAnimation", markerId, null);
					}, timeout);
				}
			});
		},
		hideMarker : function(markerId) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var marker = data.markers[markerId];
				if (typeof marker != undefined) {
					marker.setVisible(false);
				}
			});
		},
		hideAllMarkersExcept : function(markerId) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				for(var i in data.markers) {
					data.markers[i].setVisible(false);
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
					data.markers[i].setVisible(true);
				}
			});
		},
		showMarker : function(markerId) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var marker = data.markers[markerId];
				if (typeof marker != undefined) {
					marker.setVisible(true);
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
		/* NOTA : les kmlLayers ne sont pas pris en compte par le mécanisme autofit */
		autofit : function() {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var bounds = new google.maps.LatLngBounds();
				var onlyOneMarker = true;
				
				var markerNumber = 0;
				for(var i in data.markers) {
					if (data.markers[i].getMap() == data.gmap && data.markers[i].getVisible()) {
						bounds.extend(data.markers[i].getPosition());
						markerNumber++;
					}
				}
				if (markerNumber == 0 || markerNumber > 1) {
					onlyOneMarker = false;
				}
				
				for (var i in data.polygons) {
					onlyOneMarker = false;
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
					onlyOneMarker = false;
					data.polylines[i].getPath().forEach(function(latlng) { bounds.extend(latlng); } ); 
				}
				for (var i in data.circles) {
					onlyOneMarker = false;
					bounds.union(data.circles[i].getBounds());
				}
				
				if (onlyOneMarker) {
					console.log("autofit: onlyOneMarker");
					/* on itère mais on sait qu'on a un seul élément */
					for(var i in data.markers) {
						if (data.markers[i].getMap() == data.gmap && data.markers[i].getVisible()) {
							data.gmap.setCenter(data.markers[i].getPosition());
							data.gmap.setZoom(data.gmap.pluginZoomOneMarker);
							data.bounds = data.gmap.getBounds();
						}
					}
				} else if (bounds.isEmpty()) {
					console.log("aucun élément, autofit sur la zone par défaut");
					data.gmap.setCenter(data.gmap.pluginDefaultCenter);
					data.gmap.setZoom(data.gmap.pluginDefaultZoom);
					data.bounds = data.gmap.getBounds();
				} else {
					console.log("autofit");
					console.log(bounds);
					
					zoomChangeBoundsListener = google.maps.event.addListenerOnce(data.gmap, 'bounds_changed', function(event) {
						if (this.getZoom() > data.gmap.pluginZoomOneMarker){
							console.log("zoom maximum dépassé, changement du niveau de zoom.");
							this.setZoom(data.gmap.pluginZoomOneMarker);
						}
					});
					setTimeout(function(){google.maps.event.removeListener(zoomChangeBoundsListener)}, 2000);
					
					data.gmap.fitBounds(bounds);
					data.gmap.setCenter(bounds.getCenter());
					data.bounds = bounds;
				}
				
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
					
					google.maps.event.addListener(drawingManager, 'overlaycomplete', $.proxy(function(event) {
						var $this = $(this);
						$this.gmap('overlayCompleteAction', event);
					}, this));
					
					data.drawingManagerOptions = options;
					data.drawingManager = drawingManager;
					data._currentShapeIndex = 1;
					$this.data('gmap', data);
					$this.gmap('_swapColor');
				} else {
					$.error('Drawing Library is not loaded, you must load it to be able to create a drawing manager');
				}
				// événement qui permet aux autres éléments d'une page de commencer à
				// manipuler la carte (ajout de markers, de formes, ...)
				$this.trigger("gmap.drawingManagerInitialized");
			});
		},
		overlayCompleteAction : function (event) {
			var $this = $(this);
			var data = $this.data("gmap");
			var drawingManagerOptions = data.drawingManagerOptions;
			
			// Polygon
			if (event.type == google.maps.drawing.OverlayType.POLYGON) {
				// on ignore les polygones sans surface
				if (event.overlay.getPath().length >= 3) {
					$this.gmap('_swapColor');
					var shapeIndex = data._currentShapeIndex++;
					var identifier = "POLYGON " + shapeIndex;
					data.polygons[identifier] = event.overlay;
					$(this).trigger("polygoncomplete", { gmapevent: event, identifier: identifier, index: shapeIndex });
				} else {
					// les polygones null (point ou droite) sont supprimés
					event.overlay.setMap(null);
				}
			}
			// Polyline
			if (event.type == google.maps.drawing.OverlayType.POLYLINE) {
				$this.gmap('_swapColor');
				var shapeIndex = data._currentShapeIndex++;
				var identifier = "POLYLINE " + shapeIndex;
				data.polylines[identifier] = event.overlay;
				$(this).trigger("polylinecomplete", { gmapevent: event, identifier: identifier, index: shapeIndex });
			}
			$this.data('gmap', data);
		},
		setDrawingManagerOptions : function(options) {
			var $this = $(this), data = $this.data('gmap');
			if (data.drawingManager) {
				$.extend(true, data.drawingManagerOptions, options);
				
				data.drawingManager.setOptions(data.drawingManagerOptions);
				
				$this.data('gmap', data);
			}
		},
		getDrawingManagerOptions : function() {
			var $this = $(this), data = $this.data('gmap');
			return data.drawingManagerOptions;
		},
		setMapEnabledMove : function(enabled) {
			return this.each(function() {
				var $this = $(this);
				$this.gmap("setOptions", { 
					draggable: enabled, 
					panControl: enabled, 
				});
			});
		},
		setMapEnabledZoom : function(enabled) {
			return this.each(function() {
				var $this = $(this);
				$this.gmap("setOptions", { 
					zoomControl: enabled, 
					scrollwheel: enabled,
					disableDoubleClickZoom: !enabled
				});
			});
		},
		setMapEnabled : function(enabled) {
			return this.each(function() {
				var $this = $(this);
				$this.gmap("setMapEnabledMove", enabled);
				$this.gmap("setMapEnabledZoom", enabled);
			});
		},
		setShapesEditable : function(editable) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				var polygons = $this.gmap('getPolygons');
				for (polygon in polygons) {
					polygons[polygon].setEditable(editable);
				}
			});
		},
		/* les kmlLayers ne sont pas pris en compte */
		submitShapes : function (callbackUrl) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				getShapes(callbackUrl, data.polygons, data.polylines);
			});
		},
		/* les kmlLayers ne sont pas pris en compte */
		clearShapes : function () {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				for(var i in data.polygons) {
					data.polygons[i].setMap(null);
				}
				for(var i in data.polylines) {
					data.polylines[i].setMap(null);
				}
				for(var i in data.circles) {
					data.circles[i].setMap(null);
				}
				
				data.polygons = new Array();
				data.polylines = new Array();
				data.circles = new Array();
				
				// on enlève le mode de dessin pour pouvoir repartir de zéro
				// c'est notamment utile quand on a dessiné des polygones et
				// qu'on utilise la complétion d'adresse : ça dessine un cercle
				// qu'il faut pouvoir redimensionner
				if (data.drawingManager) {
					data.drawingManager.setDrawingMode(null);
				}
				
				data._currentShapeIndex = 1;
				$this.data('gmap', data);
				
				$this.gmap('_resetColor');
				$this.trigger("gmap.shapesCleared");
			});
		},
		removeMarker: function(markerId) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				if (data.markers[markerId]) {
					data.markers[markerId].setMap(null);
					delete data.markers[markerId];
				}
				$this.data('gmap', data);
			});
		},
		removeKmlLayer: function(layerId) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				if (data.kmlLayers[layerId]) {
					data.kmlLayers[layerId].setMap(null);
					delete data.kmlLayers[layerId];
				}
				$this.data('gmap', data);
			});
		},
		removePolygon : function (polygonId) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				data.polygons[polygonId].setMap(null);
				delete data.polygons[polygonId];
				
				$this.data('gmap', data);
			});
		},
		removePolyline : function (polylineId) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				data.polylines[polylineId].setMap(null);
				delete data.polylines[polylineId];
				
				$this.data('gmap', data);
			});
		},
		removeCircle : function (circleId) {
			return this.each(function() {
				var $this = $(this), data = $this.data('gmap');
				data.circles[circleId].setMap(null);
				delete data.circles[circleId];
				
				$this.data('gmap', data);
			});
		},
		createPolygon : function (paths, extraData) {
			var $this = $(this), data = $this.data('gmap');
			
			var polygonOptions = data.drawingManagerOptions.polygonOptions;
			// on ne peut pas se contenter du swapColor habituel, car il semble y avoir
			// une notion asynchrone dans la modification du drawingManager. A l'initialisation
			// on force donc la couleur manuellement, puis on fait le swap color de manière à être
			// synchronisé.
			var color = $this.gmap('_getColor');
			polygonOptions = $.extend({}, polygonOptions, { paths: paths, strokeColor: color, fillColor: color });
			var polygon = new google.maps.Polygon(polygonOptions);
			polygon.setMap(data.gmap);
			
			var overlayCompleteEvent = new Object();
			overlayCompleteEvent.overlay = polygon;
			overlayCompleteEvent.type = google.maps.drawing.OverlayType.POLYGON;
			
			$this.gmap('_swapColor');
			
			var shapeIndex = data._currentShapeIndex++;
			var identifier = "POLYGON " + shapeIndex;
			data.polygons[identifier] = polygon;
			
			$this.data('gmap', data);
			$(this).trigger("polygoncomplete", {
				gmapevent: overlayCompleteEvent,
				identifier: identifier,
				index: shapeIndex,
				extraData: extraData
			});
			
			return polygon;
		},
		getPolygon : function(polygonId) {
			var $this = $(this), data = $this.data('gmap');
			return data.polygons[polygonId];
		},
		getPolygons : function() {
			var $this = $(this), data = $this.data('gmap');
			return data.polygons;
		},
		getPolygonsString : function() {
			var $this = $(this), data = $this.data('gmap');
			var corePolygonResult = "";
			var polygons = data.polygons;
			
			corePolygonResult = cutShapeResult(corePolygonResult, polygons, true);
			
			return corePolygonResult;
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
		createCircle : function (circleId, options) {
			var $this = $(this), data = $this.data('gmap');
			var circle = new google.maps.Circle(options);
			circle.setMap(data.gmap);
			
			data.circles[circleId] = circle;
			$this.data('gmap', data);
			
			return circle;
		},
		_getColor : function (named) {
			var $this = $(this), data = $this.data('gmap');
			$this.data('gmap', data);
			return data.colors[(data.colorIndex) % data.colors.length][named ? 0 : 1];
		},
		_resetColor : function () {
			var $this = $(this), data = $this.data('gmap');
			data.colorIndex = 0;
			$this.gmap("_swapColor");
		},
		_swapColor : function () {
			var $this = $(this), data = $this.data('gmap');
			
			var drawingManager = data.drawingManager;
			var options = data.drawingManagerOptions;
			
			data.colorIndex++;
			var color = $this.gmap('_getColor', false);
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
			data.drawingManagerOptions = options;
			$this.data('gmap', data);
		},
		getPolygonNextColor : function() {
			var $this = $(this), data = $this.data('gmap');
			var options = data.drawingManagerOptions;
			return options.polygonOptions['strokeColor'];
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

	$.fn.gmap.defaults = {
		pluginDefaultCenter: null,
		pluginDefaultZoom: null,
		pluginZoomOneMarker: 15
	}

}) (jQuery);