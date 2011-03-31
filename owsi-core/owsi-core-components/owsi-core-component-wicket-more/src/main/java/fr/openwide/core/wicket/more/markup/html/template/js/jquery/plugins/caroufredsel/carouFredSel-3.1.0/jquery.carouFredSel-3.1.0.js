/*	
 *	jQuery carouFredSel 3.1.0
 *	Demo's and documentation:
 *	caroufredsel.frebsite.nl
 *	
 *	Copyright (c) 2010 Fred Heusschen
 *	www.frebsite.nl
 *
 *	Dual licensed under the MIT and GPL licenses.
 *	http://en.wikipedia.org/wiki/MIT_License
 *	http://en.wikipedia.org/wiki/GNU_General_Public_License
 */

(function($) {
	$.fn.carouFredSel = function(o) {
		if (this.length == 0) return log('No element selected.');
		if (this.length > 1) {
			return this.each(function() {
				$(this).carouFredSel(o);
			});
		}

		this.init = function(o) {
			if (typeof o != 'object')					o = {};
			if (typeof o.scroll == 'number') {
				if (o.scroll <= 50)						o.scroll	= { items	: o.scroll 	};
				else									o.scroll	= { duration: o.scroll 	};
			} else {
				if (typeof o.scroll == 'string')		o.scroll	= { easing	: o.scroll 	};
			}
				 if (typeof o.items  == 'number') 		o.items		= { visible	: o.items 	};
			else if (typeof o.items  == 'string') 		o.items		= { visible	: o.items,
																		width	: o.items, 
																		height	: o.items	};
			opts = $.extend(true, {}, $.fn.carouFredSel.defaults, o);
			opts.padding = getPadding(opts.padding);
			opts.usePadding = (
				opts.padding[0] == 0 && 
				opts.padding[1] == 0 && 
				opts.padding[2] == 0 && 
				opts.padding[3] == 0
			) ? false : true;

			direction = (opts.direction == 'up' || opts.direction == 'left') ? 'next' : 'prev';

			if (opts.direction == 'right' || opts.direction == 'left') {
				opts.dimentions = ['width', 'outerWidth', 'height', 'outerHeight', 'left', 'top', 'marginRight', 'innerWidth'];
			} else {
				opts.dimentions = ['height', 'outerHeight', 'width', 'outerWidth', 'top', 'left', 'marginBottom', 'innerHeight'];
				opts.padding = [opts.padding[3], opts.padding[2], opts.padding[1], opts.padding[0]];
			}

			if (!opts.items.width)	opts.items.width 	= getItems($cfs).outerWidth(true);
			if (!opts.items.height)	opts.items.height	= getItems($cfs).outerHeight(true);

			if (opts.items.visible == 'variable') {
				if (typeof opts[opts.dimentions[0]] == 'number') {
					opts.maxDimention = opts[opts.dimentions[0]];
					opts[opts.dimentions[0]] = null;
				} else {
					opts.maxDimention = $wrp.parent()[opts.dimentions[7]]();
				}
				if (opts.items[opts.dimentions[0]] == 'variable') {
					varnumvisitem = true;
					opts.items.visible = 0;
				} else {
					opts.items.visible = Math.floor(opts.maxDimention / opts.items[opts.dimentions[0]]);
				}
			}

			if (typeof opts.items.minimum	!= 'number')	opts.items.minimum		= opts.items.visible;
			if (typeof opts.scroll.items	!= 'number')	opts.scroll.items		= opts.items.visible;
			if (typeof opts.scroll.duration	!= 'number')	opts.scroll.duration	= 500;

			opts.auto		= getNaviObject(opts.auto, false, true);
			opts.prev		= getNaviObject(opts.prev);
			opts.next		= getNaviObject(opts.next);
			opts.pagination	= getNaviObject(opts.pagination, true);

			opts.auto		= $.extend({}, opts.scroll, opts.auto);
			opts.prev		= $.extend({}, opts.scroll, opts.prev);
			opts.next		= $.extend({}, opts.scroll, opts.next);
			opts.pagination	= $.extend({}, opts.scroll, opts.pagination);

			if (typeof opts.pagination.keys				!= 'boolean')	opts.pagination.keys 			= false;
			if (typeof opts.pagination.anchorBuilder	!= 'function')	opts.pagination.anchorBuilder	= $.fn.carouFredSel.pageAnchorBuilder;
			if (typeof opts.auto.play					!= 'boolean')	opts.auto.play					= true;
			if (typeof opts.auto.nap					!= 'boolean')	opts.auto.nap					= true;
			if (typeof opts.auto.delay					!= 'number')	opts.auto.delay					= 0;
			if (typeof opts.auto.pauseDuration			!= 'number')	opts.auto.pauseDuration			= (opts.auto.duration < 10) ? 2500 : opts.auto.duration * 5;

		};	//	/init

		this.build = function() {
			$wrp.css({
				position: 'relative',
				overflow: 'hidden'
			});
			$cfs.data('cfs_origCss', {
				width	: $cfs.css('width'),
				height	: $cfs.css('height'),
				position: $cfs.css('position'),
				top		: $cfs.css('top'),
				left	: $cfs.css('left')
			}).css({
				position: 'absolute'
			});
			if (opts.usePadding) {
				getItems($cfs).each(function() {
					var m = parseInt($(this).css(opts.dimentions[6]));
					if (isNaN(m)) m = 0;
					$(this).data('cfs_origCssMargin', m);
				});
			}
			showNavi(opts, totalItems);
		};	//	/build

		this.bind_events = function() {
			$cfs.bind('pause', function(e, g) {
				if (typeof g != 'boolean') g = false;
				if (g) pausedGlobal = true;
				if (autoTimeout != null) {
					clearTimeout(autoTimeout);
				}
				if (autoInterval != null) {
					clearInterval(autoInterval);
				}
			});
			$cfs.bind('play', function(e, d, f, g) {
				$cfs.trigger('pause');
				if (opts.auto.play) {
					if (typeof g != 'boolean') {
						if (typeof f == 'boolean') 		g = f;
						else if (typeof d == 'boolean')	g = d;
						else 							g = false;
					}
					if (typeof f != 'number') {
						if (typeof d == 'number')		f = d;
						else							f = 0;
					}
					if (d != 'prev' && d != 'next')		d = direction;

					if (g) pausedGlobal = false;
					if (pausedGlobal) return;

					autoTimeout = setTimeout(function() {
						if ($cfs.is(':animated')) {
							$cfs.trigger('play', d);
						} else {
							pauseTimePassed = 0;
							$cfs.trigger(d, opts.auto);
						}
					}, opts.auto.pauseDuration + f - pauseTimePassed);
					
					if (opts.auto.pauseOnHover === 'resume') {
						autoInterval = setInterval(function() {
							pauseTimePassed += 100;
						}, 100);
					}
				}
			});
			if (varnumvisitem) {
				$cfs.bind('prev', function(e, sO, nI) {
					if ($cfs.is(':animated')) return false;

					var items = getItems($cfs),
						total = 0,
						x = 0;

					if (typeof sO == 'number') nI = sO;
					if (typeof nI != 'number') {
						for (var a = items.length-1; a >= 0; a--) {
							current = items.filter(':eq('+ a +')')[opts.dimentions[1]](true);
							if (total + current > opts.maxDimention) break;
							total += current;
							x++;
						}
						nI = x;
					}

					for (var a = items.length-nI; a < items.length; a++) {
						current = items.filter(':eq('+ a +')')[opts.dimentions[1]](true);
						if (total + current > opts.maxDimention) break;
						total += current;
						if (a == items.length-1) a = 0;
						x++;
					};
					opts.items.visible = x;
					$cfs.trigger('scrollPrev', [sO, nI]);
				});

				$cfs.bind('next', function(e, sO, nI) {
					if ($cfs.is(':animated')) return false;

					var items = getItems($cfs),
						total = 0,
						x = 0;

					if (typeof sO == 'number') nI = sO;
					if (typeof nI != 'number') nI = opts.items.visible;

					for (var a = nI; a < items.length; a++) {
						current = items.filter(':eq('+ a +')')[opts.dimentions[1]](true);
						if (total + current > opts.maxDimention) break;
						total += current;
						if (a == items.length-1) a = 0;
						x++;
					};
					opts.items.visible = x;
					$cfs.trigger('scrollNext', [sO, nI]);
				}).trigger('next', { duration: 0 });

			} else {
				$cfs.bind('prev', function(e, sO, nI) {
					$cfs.trigger('scrollPrev', [sO, nI]);
				});
				$cfs.bind('next', function(e, sO, nI) {
					$cfs.trigger('scrollNext', [sO, nI]);
				});
			}

			$cfs.bind('scrollPrev', function(e, sO, nI) {
				if ($cfs.is(':animated')) return false;
				if (opts.items.minimum >= totalItems) return log('Not enough items: not scrolling');

				if (typeof sO == 'number') nI = sO;
				if (typeof sO != 'object') sO = opts.prev;
				if (typeof nI != 'number') nI = sO.items;
				if (typeof nI != 'number') return log('Not a valid number: not scrolling');

				if (!opts.circular) {
					var nulItem = totalItems - firstItem;
					if (nulItem - nI < 0) {
						nI = nulItem;
					}
					if (firstItem == 0) {
						nI = 0;
					}
				}

				firstItem += nI;
				if (firstItem >= totalItems) firstItem -= totalItems;

				if (!opts.circular) {
					if (firstItem == 0 && nI != 0 && opts.prev.onEnd) {
						opts.prev.onEnd();
					}
					if (opts.infinite) {
						if (nI == 0) {
							$cfs.trigger('next', totalItems-opts.items.visible);
							return false;
						}
					} else {
						if (firstItem == 0 && 
							opts.prev.button) opts.prev.button.addClass('disabled');
						if (opts.next.button) opts.next.button.removeClass('disabled');
					}
				}

				if (nI == 0) {
					return false;
				}

				getItems($cfs, ':gt('+(totalItems-nI-1)+')').prependTo($cfs);
				if (totalItems < opts.items.visible + nI) getItems($cfs, ':lt('+((opts.items.visible+nI)-totalItems)+')').clone(true).appendTo($cfs);

				var c_itm = getCurrentItems($cfs, opts, nI),
					l_cur = getItems($cfs, ':nth('+(nI-1)+')'),
					l_old = c_itm[1].filter(':last'),
					l_new = c_itm[0].filter(':last');

				if (opts.usePadding) l_old.css(opts.dimentions[6], l_old.data('cfs_origCssMargin'));

				var i_siz = getSizes(opts, getItems($cfs, ':lt('+nI+')')),
					w_siz = mapWrapperSizes(getSizes(opts, c_itm[0], true), opts);

				if (opts.usePadding) l_old.css(opts.dimentions[6], l_old.data('cfs_origCssMargin') + opts.padding[1]);

				var a_cfs = {},
					a_new = {},
					a_cur = {},
					a_dur = sO.duration;

					 if (a_dur == 'auto')	a_dur = opts.scroll.duration / opts.scroll.items * nI;
				else if (a_dur <= 0)		a_dur = 0;
				else if (a_dur < 10)		a_dur = i_siz[0] / a_dur;

				if (sO.onBefore) sO.onBefore(c_itm[1], c_itm[0], w_siz, a_dur);

				if (opts.usePadding) {
					var new_m = opts.padding[3];
					a_cur[opts.dimentions[6]] = l_cur.data('cfs_origCssMargin');
					a_new[opts.dimentions[6]] = l_new.data('cfs_origCssMargin') + opts.padding[1];

					l_cur.css(opts.dimentions[6], l_cur.data('cfs_origCssMargin') + opts.padding[3]);
					l_cur.stop().animate(a_cur, {
						duration: a_dur,
						easing	: sO.easing
					});
					l_new.stop().animate(a_new, {
						duration: a_dur,
						easing	: sO.easing
					});
				} else {
					var new_m = 0;
				}
				a_cfs[opts.dimentions[4]] = new_m;

				if ((typeof opts[opts.dimentions[0]] != 'number' && typeof opts.items[opts.dimentions[0]] != 'number') ||
					(typeof opts[opts.dimentions[2]] != 'number' && typeof opts.items[opts.dimentions[2]] != 'number')
				) {
					$wrp.stop().animate(w_siz, {
						duration: a_dur,
						easing	: sO.easing
					});
				}
				$cfs.data('cfs_numItems', nI)
					.data('cfs_slideObj', sO)
					.data('cfs_oldItems', c_itm[1])
					.data('cfs_newItems', c_itm[0])
					.data('cfs_wrapSize', w_siz)
					.css(opts.dimentions[4], -i_siz[0])
					.animate(a_cfs, {
						duration: a_dur,
						easing	: sO.easing,
						complete: function() {
							if ($cfs.data('cfs_slideObj').onAfter) {
								$cfs.data('cfs_slideObj').onAfter($cfs.data('cfs_oldItems'), $cfs.data('cfs_newItems'), $cfs.data('cfs_wrapSize'));
							}
							if (totalItems < opts.items.visible + $cfs.data('cfs_numItems')) {
								getItems($cfs, ':gt('+(totalItems-1)+')').remove();
							}
							var l_itm = getItems($cfs, ':nth('+(opts.items.visible+$cfs.data('cfs_numItems')-1)+')');
							if (opts.usePadding) {
								l_itm.css(opts.dimentions[6], l_itm.data('cfs_origCssMargin'));
							}
						}
					});
				$cfs.trigger('updatePageStatus').trigger('play', a_dur);
			});
			$cfs.bind('scrollNext', function(e, sO, nI) {
				if ($cfs.is(':animated')) return false;
				if (opts.items.minimum >= totalItems) return log('Not enough items: not scrolling');
				if (typeof sO == 'number') nI = sO;
				if (typeof sO != 'object') sO = opts.next;
				if (typeof nI != 'number') nI = sO.items;
				if (typeof nI != 'number') return log('Not a valid number: not scrolling');

				if (!opts.circular) {
					if (firstItem == 0) {
						if (nI > totalItems - opts.items.visible) {
							nI = totalItems - opts.items.visible;
						}
					} else {
						if (firstItem - nI < opts.items.visible) {
							nI = firstItem - opts.items.visible;
						}
					}
				}

				firstItem -= nI;
				if (firstItem < 0) firstItem += totalItems;

				if (!opts.circular) {
					if (firstItem == opts.items.visible && nI != 0 && opts.next.onEnd) {
						opts.next.onEnd();
					}
					if (opts.infinite) {
						if (nI == 0) {
							$cfs.trigger('prev', totalItems-opts.items.visible);
							return false;
						}
					} else {
						if (firstItem == opts.items.visible &&
							opts.next.button) opts.next.button.addClass('disabled');
						if (opts.prev.button) opts.prev.button.removeClass('disabled');
					}
				}

				if (nI == 0) {
					return false;					
				}

				if (totalItems < opts.items.visible + nI) getItems($cfs, ':lt('+((opts.items.visible+nI)-totalItems)+')').clone(true).appendTo($cfs);

				var c_itm = getCurrentItems($cfs, opts, nI),
					l_cur = getItems($cfs, ':nth('+(nI-1)+')'),
					l_old = c_itm[0].filter(':last'),
					l_new = c_itm[1].filter(':last');

				if (opts.usePadding) {
					l_old.css(opts.dimentions[6], l_old.data('cfs_origCssMargin'));
					l_new.css(opts.dimentions[6], l_new.data('cfs_origCssMargin'));
				}

				var i_siz = getSizes(opts, getItems($cfs, ':lt('+nI+')')),
					w_siz = mapWrapperSizes(getSizes(opts, c_itm[1], true), opts);

				if (opts.usePadding) {
					l_old.css(opts.dimentions[6], l_old.data('cfs_origCssMargin') + opts.padding[1]);
					l_new.css(opts.dimentions[6], l_new.data('cfs_origCssMargin') + opts.padding[1]);
				}

				var a_cfs = {},
					a_old = {},
					a_cur = {},
					a_dur = sO.duration;

					 if (a_dur == 'auto')	a_dur = opts.scroll.duration / opts.scroll.items * nI;
				else if (a_dur <= 0)		a_dur = 0;
				else if (a_dur < 10)		a_dur = i_siz[0] / a_dur;

				if (sO.onBefore) sO.onBefore(c_itm[0], c_itm[1], w_siz, a_dur);

				a_cfs[opts.dimentions[4]] = -i_siz[0];

				if (opts.usePadding) {
					a_old[opts.dimentions[6]] = l_old.data('cfs_origCssMargin');
					a_cur[opts.dimentions[6]] = l_cur.data('cfs_origCssMargin') + opts.padding[3];
					l_new.css(opts.dimentions[6], l_new.data('cfs_origCssMargin') + opts.padding[1]);

					l_old.stop().animate(a_old, {
						duration: a_dur,
						easing	: sO.easing
					});
					l_cur.stop().animate(a_cur, {
						duration: a_dur,
						easing	: sO.easing
					});
				}

				if ((typeof opts[opts.dimentions[0]] != 'number' && typeof opts.items[opts.dimentions[0]] != 'number') ||
					(typeof opts[opts.dimentions[2]] != 'number' && typeof opts.items[opts.dimentions[2]] != 'number')
				) {
					$wrp.stop().animate(w_siz, {
						duration: a_dur,
						easing	: sO.easing
					});
				}
				$cfs.data('cfs_numItems', nI)
					.data('cfs_slideObj', sO)
					.data('cfs_oldItems', c_itm[0])
					.data('cfs_newItems', c_itm[1])
					.data('cfs_wrapSize', w_siz)
					.animate(a_cfs, {
						duration: a_dur,
						easing	: sO.easing,
						complete: function() {
							if ($cfs.data('cfs_slideObj').onAfter) {
								$cfs.data('cfs_slideObj').onAfter($cfs.data('cfs_oldItems'), $cfs.data('cfs_newItems'), $cfs.data('cfs_wrapSize'));
							}
							if (totalItems < opts.items.visible+$cfs.data('cfs_numItems')) {
								getItems($cfs, ':gt('+(totalItems-1)+')').remove();
							}
							var org_m = (opts.usePadding) ? opts.padding[3] : 0;
							$cfs.css(opts.dimentions[4], org_m);
							
							var l_itm = getItems($cfs, ':lt('+$cfs.data('cfs_numItems')+')').appendTo($cfs).filter(':last');
							if (opts.usePadding) {
								l_itm.css(opts.dimentions[6], l_itm.data('cfs_origCssMargin'));
							}
						}
					});
				$cfs.trigger('updatePageStatus').trigger('play', a_dur);
			});
			$cfs.bind('slideTo', function(e, num, dev, org, obj) {
					if ($cfs.is(':animated')) return false;

					num = getItemIndex(num, dev, org, firstItem, totalItems, $cfs);
					if (num == 0) return false;
					if (typeof obj != 'object') obj = false;

					if (opts.circular) {
						if (num < totalItems / 2) 	$cfs.trigger('next', [obj, num]);
						else 						$cfs.trigger('prev', [obj, totalItems-num]);
					} else {
						if (firstItem == 0 ||
							firstItem > num)		$cfs.trigger('next', [obj, num]);
						else						$cfs.trigger('prev', [obj, totalItems-num]);
					}
				})
				.bind('insertItem', function(e, itm, num, org, dev) {
					if (typeof itm == 'object' && 
						typeof itm.jquery == 'undefined')	itm = $(itm);
					if (typeof itm == 'string') 			itm = $(itm);
					if (typeof itm != 'object' || 
						typeof itm.jquery == 'undefined' || 
						itm.length == 0) return log('Not a valid object.');

					if (typeof num == 'undefined' || num == 'end') {
						$cfs.append(itm);
					} else {
							num = getItemIndex(num, dev, org, firstItem, totalItems, $cfs);
						var $cit = getItems($cfs, ':nth('+num+')');

						if ($cit.length) {
							if (num <= firstItem) firstItem += itm.length;
							$cit.before(itm);
						} else {
							$cfs.append(itm);
						}
					}
					totalItems = getItems($cfs).length;
					link_anchors('', '.caroufredsel', $cfs);
					setSizes($cfs, opts);
					showNavi(opts, totalItems);
					$cfs.trigger('updatePageStatus', true);
				})
				.bind('removeItem', function(e, num, org, dev) {
					if (typeof num == 'undefined' || num == 'end') {
						getItems($cfs, ':last').remove();
					} else {
							num = getItemIndex(num, dev, org, firstItem, totalItems, $cfs);
						var $cit = getItems($cfs, ':nth('+num+')');
						if ($cit.length){
							if (num < firstItem) firstItem -= $cit.length;
							$cit.remove();
						}
					}
					totalItems = getItems($cfs).length;
					link_anchors('', '.caroufredsel', $cfs);
					setSizes($cfs, opts);
					showNavi(opts, totalItems);
					$cfs.trigger('updatePageStatus', true);
				})
				.bind('updatePageStatus', function(e, bpa) {
					if (!opts.pagination.container) return false;
					if (typeof bpa == 'boolean' && bpa) {
						getItems(opts.pagination.container).remove();
						for (var a = 0; a < Math.ceil(totalItems/opts.items.visible); a++) {
							opts.pagination.container.append(opts.pagination.anchorBuilder(a+1));
						}
						getItems(opts.pagination.container).unbind('click').each(function(a) {
							$(this).click(function(e) {
								e.preventDefault();
								$cfs.trigger('slideTo', [a * opts.items.visible, 0, true, opts.pagination]);
							});
						});
					}
					var nr = (firstItem == 0) ? 0 : Math.round((totalItems-firstItem)/opts.items.visible);
					getItems(opts.pagination.container).removeClass('selected').filter(':nth('+nr+')').addClass('selected');
				});
		};	//	/bind_events

		this.bind_buttons = function() {
			if (opts.auto.pauseOnHover && opts.auto.play) {
				$wrp.hover(
					function() { $cfs.trigger('pause'); },
					function() { $cfs.trigger('play');	}
				);
			}
			if (opts.prev.button) {
				opts.prev.button.click(function(e) {
					$cfs.trigger('prev');
					e.preventDefault();
				});
				if (opts.prev.pauseOnHover && opts.auto.play) {
					opts.prev.button.hover(
						function() { $cfs.trigger('pause');	},
						function() { $cfs.trigger('play');	}
					);
				}
				if (!opts.circular && !opts.infinite) {
					opts.prev.button.addClass('disabled');
				}
			}
			if ($.fn.mousewheel) {
				if (opts.prev.mousewheel) {
					$wrp.mousewheel(function(e, delta) { 
						if (delta > 0) {
							e.preventDefault();
							num = (typeof opts.prev.mousewheel == 'number') ? opts.prev.mousewheel : '';
							$cfs.trigger('prev', num);
						}
					});
				}
				if (opts.next.mousewheel) {
					$wrp.mousewheel(function(e, delta) { 
						if (delta < 0) {
							e.preventDefault();
							num = (typeof opts.next.mousewheel == 'number') ? opts.next.mousewheel : '';
							$cfs.trigger('next', num);
						}
					});
				}
			}
			if (opts.next.button) {
				opts.next.button.click(function(e) {
					e.preventDefault();
					$cfs.trigger('next');
				});
				if (opts.next.pauseOnHover && opts.auto.play) {
					opts.next.button.hover(
						function() { $cfs.trigger('pause');	},
						function() { $cfs.trigger('play');	}
					)
				}
			}
			if (opts.pagination.container) {
				$cfs.trigger('updatePageStatus', true);
				if (opts.pagination.pauseOnHover && opts.auto.play) {
					opts.pagination.container.hover(
						function() { $cfs.trigger('pause');	},
						function() { $cfs.trigger('play');	}
					);
				}
			}
			if (opts.next.key || opts.prev.key) {
				$(document).keyup(function(e) {
					var k = e.keyCode;
					if (k == opts.next.key)	{
						e.preventDefault();
						$cfs.trigger('next');
					}
					if (k == opts.prev.key) {
						e.preventDefault();
						$cfs.trigger('prev');
					}
				});
			}
			if (opts.pagination.keys) {
				$(document).keyup(function(e) {
					var k = e.keyCode;
					if (k >= 49 && k < 58) {
						k = (k-49) * opts.items.visible;
						if (k <= totalItems) {
							e.preventDefault();
							$cfs.trigger('slideTo', [k, 0, true, opts.pagination]);
						}
					}
				});
			}
			if (opts.auto.play) {
				$cfs.trigger('play', opts.auto.delay);
				if ($.fn.nap && opts.auto.nap) {
					$cfs.nap('pause', 'play');
				}
			}
		};	//	/bind_buttons

		this.destroy = function() {
			$cfs.css($cfs.data('cfs_origCss'))
				.unbind('pause')
				.unbind('play')
				.unbind('prev')
				.unbind('next')
				.unbind('scrollTo')
				.unbind('slideTo')
				.unbind('insertItem')
				.unbind('removeItem')
				.unbind('updatePageStatus');

			$wrp.replaceWith($cfs);
			return this;
		};	//	/destroy

		this.configuration = function(a, b) {
			if (typeof a == 'undefined')	return opts;
			if (typeof b == 'undefined') {
				var r = eval('opts.'+a);
				if (typeof r == 'undefined') r = '';
				return r;
			}
			eval('opts.'+a+' = b');
			this.init(opts);
			setSizes($cfs, opts);
			return this;
		};	//	/configuration

		this.link_anchors = function($c, se) {
			link_anchors($c, se, $cfs);
		};	//	/link_anchors

		var $cfs 			= $(this),
			$wrp			= $(this).wrap('<div class="caroufredsel_wrapper" />').parent(),
			opts 			= {},
			totalItems		= getItems($cfs).length,
			firstItem 		= 0,
			autoTimeout		= null,
			autoInterval	= null,
			pauseTimePassed	= 0,
			pausedGlobal	= false,
			direction		= 'next',
			varnumvisitem	= false;

		this.init(o);
		this.build();
		this.bind_events();
		this.bind_buttons();
		link_anchors('', '.caroufredsel', $cfs);
		setSizes($cfs, opts);

		if (opts.items.start !== 0 && opts.items.start !== false) {
			var s = opts.items.start;
			if (opts.items.start === true) {
				s = window.location.hash;
				if (!s.length) s = 0;
			}
			$cfs.trigger('slideTo', [s, 0, true, { duration: 0 }]);
		}
		return this;
	};

	//	public
	$.fn.carouFredSel.defaults = {
		infinite	: true,
		circular	: true,
		direction	: 'left',
		padding		: 0,
		items		: {
			visible		: 5,
			start		: 0
		},
		scroll		: {
			easing		: 'swing',
			pauseOnHover: false,
			mousewheel	: false
		}
	};
	$.fn.carouFredSel.pageAnchorBuilder = function(nr) {
		return '<a href="#"><span>'+nr+'</span></a>';
	};

	//	private
	function link_anchors($c, se, $cfs) {
		if (typeof $c == 'undefined' || $c.length == 0) $c = $('body');
		else if (typeof $c == 'string') $c = $($c);
		if (typeof $c != 'object') return false;
		if (typeof se == 'undefined') se = '';
		$c.find('a'+se).each(function() {
			var h = this.hash || '';
			if (h.length > 0 && getItems($cfs).index($(h)) != -1) {
				$(this).unbind('click').click(function(e) {
					e.preventDefault();
					$cfs.trigger('slideTo', h);
				});
			}
		});
	}
	function showNavi(o, t) {
		if (o.items.minimum >= t) {
			log('Not enough items: not scrolling');
			var f = 'hide';
		} else {
			var f = 'show';
		}
		if (o.prev.button) o.prev.button[f]();
		if (o.next.button) o.next.button[f]();
		if (o.pagination.container) o.pagination.container[f]();
	}
	function getKeyCode(k) {
		if (k == 'right')	return 39;
		if (k == 'left')	return 37;
		if (k == 'up')		return 38;
		if (k == 'down')	return 40;
		return -1
	};
	function getNaviObject(obj, pagi, auto) {
		if (typeof pagi != 'boolean') pagi = false;
		if (typeof auto != 'boolean') auto = false;

		if (typeof obj == 'undefined')	obj = {};
		if (typeof obj == 'string') {
			var temp = getKeyCode(obj);
			if (temp == -1) 			obj = $(obj);
			else 						obj = temp;
		}
		if (pagi) {
			if (typeof obj.jquery 		!= 'undefined')	obj = { container: obj };
			if (typeof Object 			== 'boolean')	obj = { keys: obj };
			if (typeof obj.container	== 'string')	obj.container = $(obj.container);

		} else if (auto) {
			if (typeof obj == 'boolean')				obj = { play: obj };
			if (typeof obj == 'number')					obj = { pauseDuration: obj };

		} else {
			if (typeof obj.jquery	!= 'undefined')		obj = { button: obj };
			if (typeof obj 			== 'number')		obj = { key: obj };
			if (typeof obj.button	== 'string')		obj.button = $(obj.button);
			if (typeof obj.key		== 'string')		obj.key = getKeyCode(obj.key);
		}
		return obj;
	};
	function getItems(a, f) {
		if (typeof f != 'string') f = '';
		return $('> *'+f, a);
	};
	function getCurrentItems(c, o, n) {
		var oi = getItems(c, ':lt('+o.items.visible+')'),
			ni = getItems(c, ':lt('+(o.items.visible+n)+'):gt('+(n-1)+')');
		return [oi, ni];
	};
	function getItemIndex(num, dev, org, firstItem, totalItems, $cfs) {
		if (typeof num == 'string') {
			if (isNaN(num)) num = $(num);
			else 			num = parseInt(num);
		}
		if (typeof num == 'object') {
			if (typeof num.jquery == 'undefined') num = $(num);
			num = getItems($cfs).index(num);
			if (num == -1) num = 0;
			if (typeof org != 'boolean') org = false;
		} else {
			if (typeof org != 'boolean') org = true;
		}
		if (isNaN(num))	num = 0;
		else 			num = parseInt(num);
		if (isNaN(dev))	dev = 0;
		else 			dev = parseInt(dev);

		if (org) {
			num += firstItem;
		}
		num += dev;
		if (totalItems > 0) {
			while (num >= totalItems)	{	num -= totalItems; }
			while (num < 0)				{	num += totalItems; }
		}
		return num;
	};
	function getSizes(o, $i, wrap) {
		if (typeof wrap != 'boolean') wrap = false;
		var di = o.dimentions,
			s1 = 0,
			s2 = 0;

			 if (wrap && typeof o[di[0]] 		== 'number') 	s1 += o[di[0]];
		else if (		 typeof o.items[di[0]]	== 'number') 	s1 += o.items[di[0]] * $i.length;
		else {
			$i.each(function() { 
				s1 += $(this)[di[1]](true);
			});
		}

			 if (wrap && typeof o[di[2]] 		== 'number') 	s2 += o[di[2]];
		else if (		 typeof o.items[di[2]]	== 'number') 	s2 += o.items[di[2]];
		else {
			$i.each(function() {
				var m = $(this)[di[3]](true);
				if (s2 < m) s2 = m;
			});
		}
		return [s1, s2];
	};
	function mapWrapperSizes(ws, o) {
		var pad = (o.usePadding) ? o.padding : [0, 0, 0, 0];
		var wra = {};
			wra[o.dimentions[0]] = ws[0] + pad[1] + pad[3];
			wra[o.dimentions[2]] = ws[1] + pad[0] + pad[2];
		return wra;
	};
	function setSizes($c, o) {
		var $w = $c.parent(),
			$i = getItems($c),
			$l = $i.filter(':nth('+(o.items.visible-1)+')'),
			is = getSizes(o, $i);

		$w.css(mapWrapperSizes(getSizes(o, $i.filter(':lt('+o.items.visible+')'), true), o));
		if (o.usePadding) {
			$l.css(o.dimentions[6], $l.data('cfs_origCssMargin') + o.padding[1]);
			$c.css(o.dimentions[5], o.padding[0]);
			$c.css(o.dimentions[4], o.padding[3]);
		}
		$c.css(o.dimentions[0], is[0]*2);
		$c.css(o.dimentions[2], is[1]);
	};
	function getPadding(p) {
			 if (typeof p == 'number')	p = [p];
		else if (typeof p == 'string')	p = p.split('px').join('').split(' ');

		if (typeof p != 'object') {
			log('Not a valid value, padding set to "0".');
			p = [0];
		}
		for (i in p) {
			p[i] = parseInt(p[i]);
		}
		switch (p.length) {
			case 0:
				return [0, 0, 0, 0];
			case 1:
				return [p[0], p[0], p[0], p[0]];
			case 2:
				return [p[0], p[1], p[0], p[1]];
			case 3:
				return [p[0], p[1], p[2], p[1]];
			default:
				return p;
		}
	};
	function log(m) {
		if (typeof m == 'string') m = 'carouFredSel: ' + m;
		if (window.console && window.console.log) window.console.log(m);
		else try { console.log(m); } catch(err) { }
		return false;
	};

	$.fn.caroufredsel = function(o) {
		this.carouFredSel(o);
	};

})(jQuery);