;
(function ($, window, document, undefined) {
    var pluginName = 'mateHover',
        defaults = {
            autoSize: 'off',
            inhiritPadding: 'on',
            position: 'y',
            overlayStyle: 'classic',
            rollingPosition: 'top',
            doublePosition: 'vertical',
            fourSpeedIn0: 200,
            fourSpeedOut0: 200,
            fourSpeedIn1: 800,
            fourSpeedOut1: 800,
            fourSpeedIn2: 300,
            fourSpeedOut2: 300,
            fourSpeedIn3: 800,
            fourSpeedOut3: 800,
            overlayBg: '#000',
            overlaySpeedIn: 500,
            overlaySpeedOut: 500,
            overlayOpacity: 0.4,
            overlayEasing: 'linear',
            popupSpeedIn: 1000,
            popupSpeedOut: 500,
            popupEasing: 'swing',
            between: 10,
            popup2SpeedIn: 800,
            popup2SpeedOut: 800,
            popup2Easing: 'swing'
        };

    function Mate(element, options) {
        this.element = $(element);
        var ele = this;
        this.options = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this.init(ele)
    };
    Mate.prototype.onResize = function () {
        var ele = this;
        this.img = this.element.children('img');
        this.popup = this.element.find('[data-zl-popup]');
        this.imgWidth = this.img.innerWidth();
        this.imgHeight = this.img.innerHeight();
        if (this.options.autoSize === 'on') {
            this.element.css({
                'width': this.imgWidth,
                'height': this.imgHeight
            })
        };
        if (this.options.inhiritPadding === 'on') {
            this.divWidth = this.element.innerWidth();
            this.divHeight = this.element.innerHeight()
        } else {
            this.divWidth = this.element.width();
            this.divHeight = this.element.height()
        };
        this.calDivWidth = this.divWidth * 2;
        this.calDivHeight = this.divHeight * 2;
        this.divCalWidth = this.divWidth / 2;
        this.divCalHeight = this.divHeight / 2;
        this.divCalWidth2 = this.divWidth / 4;
        this.divCalHeight2 = this.divHeight / 4;
        this.popupWidth = this.popup.outerWidth();
        this.popupHeight = this.popup.outerHeight();
        this.calPopWidth = this.popupWidth * 2;
        this.calPopHeight = this.popupHeight * 2;
        this.coorW = this.divCalWidth - this.popupWidth / 2;
        this.coorH = this.divCalHeight - this.popupHeight / 2;
        this.count_pop = this.popup.length;
        this.count_pop_label = 0;
        if (this.count_pop > 1) {
            this.count_pop_label = 1;
            this.popup1 = this.popup.eq(0);
            this.popup2 = this.popup.eq(1);
            this.popup1Width = this.popup1.outerWidth();
            this.popup2Width = this.popup2.outerWidth();
            this.popup1Height = this.popup1.outerHeight();
            this.popup2Height = this.popup2.outerHeight();
            this.calPop1Width = this.popup1Width * 2;
            this.calPop1Height = this.popup1Height * 2;
            this.calPop2Width = this.popup2Width * 2;
            this.calPop2Height = this.popup2Height * 2;
            this.coor1W = this.divCalWidth - this.popup1Width / 2 - this.popup2Width / 2 - this.options.between;
            this.coor1H = this.divCalHeight - this.popup1Height / 2;
            this.coor2W = this.divCalWidth - this.popup2Width / 2 - this.popup1Width / 2 - this.options.between;
            this.coor2H = this.divCalHeight - this.popup2Height / 2
        };
        this.over_pos = {
            plus: {},
            minus: {}
        };
        this.over_pos.plus[this.options.rollingPosition] = (this.options.rollingPosition === 'top' || this.options.rollingPosition === 'bottom') ? -this.divHeight - 10 : -this.divWidth - 10;
        this.over_pos.minus[this.options.rollingPosition] = 0;
        this.over_double_pos = {
            position0: {},
            position1: {},
            minus: {},
            fly_coor: {
                top_left: {},
                bottom_right: {}
            }
        };
        this.over_fly_out = {
            fly_coor: {
                top_left: {},
                bottom_right: {}
            }
        };
        if (this.options.doublePosition === 'vertical') {
            this.over_double_pos.position0['top'] = -this.divHeight;
            this.over_double_pos.position1['bottom'] = -this.divHeight;
            this.over_double_pos.minus['left'] = 0;
            this.over_double_pos.position0['height'] = '50%';
            this.over_double_pos.position0['width'] = '100%';
            this.over_double_pos.position1['height'] = '50%';
            this.over_double_pos.position1['width'] = '100%';
            this.over_double_pos.fly_coor.top_left['top'] = 0;
            this.over_double_pos.fly_coor.bottom_right['bottom'] = 0;
            this.over_fly_out.fly_coor.top_left['top'] = -this.divHeight;
            this.over_fly_out.fly_coor.bottom_right['bottom'] = -this.divHeight
        } else {
            this.over_double_pos.position0['left'] = -this.divWidth;
            this.over_double_pos.position1['right'] = -this.divWidth;
            this.over_double_pos.minus['top'] = 0;
            this.over_double_pos.position0['height'] = '100%';
            this.over_double_pos.position0['width'] = '50%';
            this.over_double_pos.position1['height'] = '100%';
            this.over_double_pos.position1['width'] = '50%';
            this.over_double_pos.fly_coor.top_left['left'] = 0;
            this.over_double_pos.fly_coor.bottom_right['right'] = 0;
            this.over_fly_out.fly_coor.top_left['left'] = -this.divWidth;
            this.over_fly_out.fly_coor.bottom_right['right'] = -this.divWidth
        }; if (this.options.overlayStyle === 'four') {
            this.over_four = {
                inn: {},
                out: {}
            };
            for (var speed_count_inout = 0; speed_count_inout <= 3; speed_count_inout++) {
                this.over_four.inn['speed' + speed_count_inout] = this.options['fourSpeedIn' + speed_count_inout];
                this.over_four.out['speed' + speed_count_inout] = this.options['fourSpeedOut' + speed_count_inout]
            }
        };
        switch (this.options.position) {
            case 'y':
                this.startPosition('y', this.count_pop_label);
                break;
            case 'y-reverse':
                this.startPosition('y-reverse', this.count_pop_label);
                break;
            case 'x':
                this.startPosition('x', this.count_pop_label);
                break;
            case 'x-reverse':
                this.startPosition('x-reverse', this.count_pop_label);
                break;
            case 'y+i':
                this.startPosition('y+i', this.count_pop_label);
                break;
            case 'y+i-reverse':
                this.startPosition('y+i-reverse', this.count_pop_label);
                break;
            case 'x+i':
                this.startPosition('x+i', this.count_pop_label);
                break;
            case 'x+i-reverse':
                this.startPosition('x+i-reverse', this.count_pop_label);
                break;
            default:
                console.log('Wrong position properties(START POPUP POSITION)');
                break
        };
        this.pos = {};
        switch (this.options.position) {
            case 'y':
                this.flyPosition('y', this.count_pop_label);
                break;
            case 'y-reverse':
                this.flyPosition('y-reverse', this.count_pop_label);
                break;
            case 'y+i':
                this.flyPosition('y+i', this.count_pop_label);
                break;
            case 'y+i-reverse':
                this.flyPosition('y+i-reverse', this.count_pop_label);
                break;
            case 'x':
                this.flyPosition('x', this.count_pop_label);
                break;
            case 'x+i':
                this.flyPosition('x+i', this.count_pop_label);
                break;
            case 'x-reverse':
                this.flyPosition('x-reverse', this.count_pop_label);
                break;
            case 'x+i-reverse':
                this.flyPosition('x+i-reverse', this.count_pop_label);
                break;
            default:
                console.log('Wrong position properties(FLY POPUP POSITION)');
                break
        };
        var general_overlay, left_or_top, left_or_top_double;
        (this.options.rollingPosition === 'top' || this.options.rollingPosition === 'bottom') ? left_or_top = 'left' : left_or_top = 'top';
        (this.options.doublePosition === 'vertical') ? left_or_top_double = 'left' : left_or_top_double = 'top';
        this.element.find('[data-zl-overlay],[data-zl-ovrolling],[data-zl-ovdouble0],[data-zl-ovdouble1],[data-zl-ovzoom0],[data-zl-ovzoom1],[data-zl-ovzoom2],[data-zl-ovzoom3]').remove();
        switch (this.options.overlayStyle) {
            case 'classic':
                general_overlay = $('<div data-zl-overlay="zl_overlay_' + ele.element.attr('data-zlname') + '"></div>').css('background', ele.options.overlayBg);
                ele.element.prepend(general_overlay);
                break;
            case 'four':
                for (var overlay_count = 0; overlay_count <= 3; overlay_count++) {
                    general_overlay = $('<div data-zl-ovzoom' + overlay_count + '="zl_overlay_' + ele.element.attr('data-zlname') + '"></div>').css({
                        'background': ele.options.overlayBg,
                        'top': -this.divHeight,
                        'left': this.divCalWidth2 * overlay_count
                    }).fadeTo(100, this.options.overlayOpacity);
                    ele.element.prepend(general_overlay)
                };
                break;
            case 'rolling':
                general_overlay = $('<div data-zl-ovrolling="zl_overlay_' + ele.element.attr('data-zlname') + '" style="background:' + ele.options.overlayBg + ';' + left_or_top + ':0;"></div>').css(this.over_pos.plus).fadeTo(100, this.options.overlayOpacity);
                ele.element.prepend(general_overlay);
                break;
            case 'double':
                for (var overlay_count_d = 0; overlay_count_d <= 1; overlay_count_d++) {
                    general_overlay = $('<div data-zl-ovdouble' + overlay_count_d + '="zl_overlay_' + ele.element.attr('data-zlname') + '" style="background:' + ele.options.overlayBg + ';' + left_or_top_double + ':0;"></div>').css(this.over_double_pos['position' + overlay_count_d]).fadeTo(100, this.options.overlayOpacity);
                    ele.element.prepend(general_overlay)
                };
                break
        }
    };
    Mate.prototype.startPosition = function (x_or_y, count_pop_label) {
        if (x_or_y === 'y' && count_pop_label === 0 || x_or_y === 'y+i' && count_pop_label === 0) {
            this.popup.css({
                'left': this.coorW,
                'top': -this.calPopHeight
            })
        } else if (x_or_y === 'y-reverse' && count_pop_label === 0 || x_or_y === 'y+i-reverse' && count_pop_label === 0) {
            this.popup.css({
                'left': this.coorW,
                'bottom': -this.calPopHeight
            })
        } else if (x_or_y === 'x-reverse' && count_pop_label === 0 || x_or_y === 'x+i-reverse' && count_pop_label === 0) {
            this.popup.css({
                'top': this.coorH,
                'right': -this.calPopWidth
            })
        } else if (x_or_y === 'x' && count_pop_label === 0 || x_or_y === 'x+i' && count_pop_label === 0) {
            this.popup.css({
                'top': this.coorH,
                'left': -this.calPopWidth
            })
        } else if (x_or_y === 'y' && count_pop_label === 1 || x_or_y === 'y+i' && count_pop_label === 1) {
            this.popup1.css({
                'left': this.coor1W,
                'top': -this.calPop1Height
            });
            this.popup2.css({
                'right': this.coor2W,
                'top': -this.calPop2Height
            })
        } else if (x_or_y === 'y-reverse' && count_pop_label === 1 || x_or_y === 'y+i-reverse' && count_pop_label === 1) {
            this.popup1.css({
                'left': this.coor1W,
                'bottom': -this.calPop1Height
            });
            this.popup2.css({
                'right': this.coor2W,
                'bottom': -this.calPop2Height
            })
        } else if (x_or_y === 'x-reverse' && count_pop_label === 1 || x_or_y === 'x+i-reverse' && count_pop_label === 1) {
            this.popup1.css({
                'top': this.coor1H,
                'right': -this.calPop1Width
            });
            this.popup2.css({
                'top': this.coor2H,
                'left': -this.calPop2Width
            })
        } else if (x_or_y === 'x' && count_pop_label === 1 || x_or_y === 'x+i' && count_pop_label === 1) {
            this.popup1.css({
                'top': this.coor1H,
                'left': -this.calPop1Width
            });
            this.popup2.css({
                'top': this.coor2H,
                'right': -this.calPop2Width
            })
        }
    };
    Mate.prototype.flyPosition = function (x_or_y, count_pop_label) {
        if (x_or_y === 'y' && count_pop_label === 0) {
            this.pos.anime_enter = {
                top: this.coorH
            }, this.pos.anime_leave = {
                top: -this.calPopHeight
            }, this.pos.back_css = {
                'display': 'none',
                'top': -this.calPopHeight
            }
        } else if (x_or_y === 'y-reverse' && count_pop_label === 0) {
            this.pos.anime_enter = {
                bottom: this.coorH
            }, this.pos.anime_leave = {
                bottom: -this.calPopHeight
            }, this.pos.back_css = {
                'display': 'none',
                'bottom': -this.calPopHeight
            }
        } else if (x_or_y === 'y+i-reverse' && count_pop_label === 0) {
            this.pos.anime_enter = {
                bottom: this.coorH
            }, this.pos.anime_leave = {
                bottom: this.calDivHeight
            }, this.pos.back_css = {
                'display': 'none',
                'bottom': -this.calPopHeight
            }
        } else if (x_or_y === 'y+i' && count_pop_label === 0) {
            this.pos.anime_enter = {
                top: this.coorH
            }, this.pos.anime_leave = {
                top: this.calDivHeight
            }, this.pos.back_css = {
                'display': 'none',
                'top': -this.calPopHeight
            }
        } else if (x_or_y === 'x' && count_pop_label === 0) {
            this.pos.anime_enter = {
                left: this.coorW
            }, this.pos.anime_leave = {
                left: -this.calPopWidth
            }, this.pos.back_css = {
                'display': 'none',
                'left': -this.calPopWidth
            }
        } else if (x_or_y === 'x+i' && count_pop_label === 0) {
            this.pos.anime_enter = {
                left: this.coorW
            }, this.pos.anime_leave = {
                left: this.calDivWidth
            }, this.pos.back_css = {
                'display': 'none',
                'left': -this.calPopWidth
            }
        } else if (x_or_y === 'x-reverse' && count_pop_label === 0) {
            this.pos.anime_enter = {
                right: this.coorW
            }, this.pos.anime_leave = {
                right: -this.calPopWidth
            }, this.pos.back_css = {
                'display': 'none',
                'right': -this.calPopWidth
            }
        } else if (x_or_y === 'x+i-reverse' && count_pop_label === 0) {
            this.pos.anime_enter = {
                right: this.coorW
            }, this.pos.anime_leave = {
                right: this.calDivWidth
            }, this.pos.back_css = {
                'display': 'none',
                'right': -this.calPopWidth
            }
        } else if (x_or_y === 'y' && count_pop_label === 1) {
            this.pos.anime_enter = {
                top: this.coor1H
            }, this.pos.anime_leave = {
                top: -this.calPop1Height
            }, this.pos.back_css = {
                'display': 'none',
                'top': -this.calPop1Height
            };
            this.pos.anime_enter2 = {
                top: this.coor2H
            }, this.pos.anime_leave2 = {
                top: -this.calPop2Height
            }, this.pos.back_css2 = {
                'display': 'none',
                'top': -this.calPop2Height
            }
        } else if (x_or_y === 'y-reverse' && count_pop_label === 1) {
            this.pos.anime_enter = {
                bottom: this.coor1H
            }, this.pos.anime_leave = {
                bottom: -this.calPop1Height
            }, this.pos.back_css = {
                'display': 'none',
                'bottom': -this.calPop1Height
            };
            this.pos.anime_enter2 = {
                bottom: this.coor2H
            }, this.pos.anime_leave2 = {
                bottom: -this.calPop2Height
            }, this.pos.back_css2 = {
                'display': 'none',
                'bottom': -this.calPop2Height
            }
        } else if (x_or_y === 'y+i-reverse' && count_pop_label === 1) {
            this.pos.anime_enter = {
                bottom: this.coor1H
            }, this.pos.anime_leave = {
                bottom: this.calDivHeight
            }, this.pos.back_css = {
                'display': 'none',
                'bottom': -this.calPop1Height
            };
            this.pos.anime_enter2 = {
                bottom: this.coor2H
            }, this.pos.anime_leave2 = {
                bottom: this.calDivHeight
            }, this.pos.back_css2 = {
                'display': 'none',
                'bottom': -this.calPop2Height
            }
        } else if (x_or_y === 'y+i' && count_pop_label === 1) {
            this.pos.anime_enter = {
                top: this.coor1H
            }, this.pos.anime_leave = {
                top: this.calDivHeight
            }, this.pos.back_css = {
                'display': 'none',
                'top': -this.calPop1Height
            };
            this.pos.anime_enter2 = {
                top: this.coor2H
            }, this.pos.anime_leave2 = {
                top: this.calDivHeight
            }, this.pos.back_css2 = {
                'display': 'none',
                'top': -this.calPop2Height
            }
        } else if (x_or_y === 'x' && count_pop_label === 1) {
            this.pos.anime_enter = {
                left: this.coor1W
            }, this.pos.anime_leave = {
                left: -this.calPop1Width
            }, this.pos.back_css = {
                'display': 'none',
                'left': -this.calPop1Width
            };
            this.pos.anime_enter2 = {
                right: this.coor2W
            }, this.pos.anime_leave2 = {
                right: -this.calPop2Width
            }, this.pos.back_css2 = {
                'display': 'none',
                'right': -this.calPop2Width
            }
        } else if (x_or_y === 'x-reverse' && count_pop_label === 1) {
            this.pos.anime_enter = {
                right: this.coor1W
            }, this.pos.anime_leave = {
                right: -this.calPop1Width
            }, this.pos.back_css = {
                'display': 'none',
                'right': -this.calPop1Width
            };
            this.pos.anime_enter2 = {
                left: this.coor2W
            }, this.pos.anime_leave2 = {
                left: -this.calPop2Width
            }, this.pos.back_css2 = {
                'display': 'none',
                'left': -this.calPop2Width
            }
        } else if (x_or_y === 'x+i' && count_pop_label === 1) {
            this.pos.anime_enter = {
                left: this.coor1W
            }, this.pos.anime_leave = {
                left: this.calDivWidth
            }, this.pos.back_css = {
                'display': 'none',
                'left': -this.calPop1Width
            };
            this.pos.anime_enter2 = {
                right: this.coor2W
            }, this.pos.anime_leave2 = {
                right: this.calDivWidth
            }, this.pos.back_css2 = {
                'display': 'none',
                'right': -this.calPop2Width
            }
        } else if (x_or_y === 'x+i-reverse' && count_pop_label === 1) {
            this.pos.anime_enter = {
                right: this.coor1W
            }, this.pos.anime_leave = {
                right: this.calDivWidth
            }, this.pos.back_css = {
                'display': 'none',
                'right': -this.calPop1Width
            };
            this.pos.anime_enter2 = {
                left: this.coor2W
            }, this.pos.anime_leave2 = {
                left: this.calDivWidth
            }, this.pos.back_css2 = {
                'display': 'none',
                'left': -this.calPop2Width
            }
        }
    };
    Mate.prototype.overlayGet = function (ele, overlayStyle, speed, opacity, over_pos) {
        switch (overlayStyle) {
            case 'classic':
                ele.element.children('[data-zl-overlay]').stop(true).fadeTo(speed, opacity, ele.options.overlayEasing);
                break;
            case 'four':
                var obj_count = 0;
                for (var obj_proper in speed) {
                    ele.element.children('[data-zl-ovzoom' + obj_count + ']').stop(true).animate({
                        top: over_pos
                    }, speed[obj_proper], ele.options.overlayEasing);
                    obj_count++
                };
                break;
            case 'rolling':
                ele.element.children('[data-zl-ovrolling]').css('display', 'block').stop(true).animate(over_pos, speed, ele.options.overlayEasing);
                break;
            case 'double':
                ele.element.children('[data-zl-ovdouble0]').css('display', 'block').stop(true).animate(over_pos.top_left, speed, ele.options.overlayEasing);
                ele.element.children('[data-zl-ovdouble1]').css('display', 'block').stop(true).animate(over_pos.bottom_right, speed, ele.options.overlayEasing);
                break
        }
    };
    Mate.prototype.hover = function (ele, count_pop_label) {
        this.element.on({
            mouseenter: function () {
                switch (count_pop_label) {
                    case 0:
                        ele.popup.css(ele.pos.back_css).css('display', 'block').stop(true).animate(ele.pos.anime_enter, ele.options.popupSpeedIn, ele.options.popupEasing);
                        switch (ele.options.overlayStyle) {
                            case 'classic':
                                ele.overlayGet(ele, 'classic', ele.options.overlaySpeedIn, ele.options.overlayOpacity, 0);
                                break;
                            case 'four':
                                ele.overlayGet(ele, 'four', ele.over_four.inn, 0, ele.divHeight);
                                break;
                            case 'rolling':
                                ele.overlayGet(ele, 'rolling', ele.options.overlaySpeedIn, ele.options.overlayOpacity, ele.over_pos.minus);
                                break;
                            case 'double':
                                ele.overlayGet(ele, 'double', ele.options.overlaySpeedIn, ele.options.overlayOpacity, ele.over_double_pos.fly_coor);
                                break
                        };
                        break;
                    case 1:
                        ele.popup1.css(ele.pos.back_css).css('display', 'block').stop(true).animate(ele.pos.anime_enter, ele.options.popupSpeedIn, ele.options.popupEasing).siblings('[data-zl-popup]').css(ele.pos.back_css2).css('display', 'block').stop(true).animate(ele.pos.anime_enter2, ele.options.popup2SpeedIn, ele.options.popup2Easing);
                        switch (ele.options.overlayStyle) {
                            case 'classic':
                                ele.overlayGet(ele, 'classic', ele.options.overlaySpeedIn, ele.options.overlayOpacity, 0);
                                break;
                            case 'four':
                                ele.overlayGet(ele, 'four', ele.over_four.inn, 0, ele.divHeight);
                                break;
                            case 'rolling':
                                ele.overlayGet(ele, 'rolling', ele.options.overlaySpeedIn, ele.options.overlayOpacity, ele.over_pos.minus);
                                break;
                            case 'double':
                                ele.overlayGet(ele, 'double', ele.options.overlaySpeedIn, ele.options.overlayOpacity, ele.over_double_pos.fly_coor);
                                break
                        };
                        break
                }
            },
            mouseleave: function () {
                switch (count_pop_label) {
                    case 0:
                        ele.popup.stop(true).animate(ele.pos.anime_leave, ele.options.popupSpeedOut, ele.options.popupEasing).children('input').blur();
                        switch (ele.options.overlayStyle) {
                            case 'classic':
                                ele.overlayGet(ele, 'classic', ele.options.overlaySpeedOut, 0, 0);
                                break;
                            case 'four':
                                ele.overlayGet(ele, 'four', ele.over_four.out, 0, -ele.divHeight);
                                break;
                            case 'rolling':
                                ele.overlayGet(ele, 'rolling', ele.options.overlaySpeedOut, 0, ele.over_pos.plus);
                                break;
                            case 'double':
                                ele.overlayGet(ele, 'double', ele.options.overlaySpeedOut, 0, ele.over_fly_out.fly_coor);
                                break
                        };
                        break;
                    case 1:
                        ele.popup1.stop(true, true).animate(ele.pos.anime_leave, ele.options.popupSpeedOut, ele.options.popupEasing).children('input').blur().end().siblings('[data-zl-popup]').stop(true, true).animate(ele.pos.anime_leave2, ele.options.popup2SpeedOut, ele.options.popup2Easing).children('input').blur();
                        switch (ele.options.overlayStyle) {
                            case 'classic':
                                ele.overlayGet(ele, 'classic', ele.options.overlaySpeedOut, 0, 0);
                                break;
                            case 'four':
                                ele.overlayGet(ele, 'four', ele.over_four.out, 0, -ele.divHeight);
                                break;
                            case 'rolling':
                                ele.overlayGet(ele, 'rolling', ele.options.overlaySpeedOut, 0, ele.over_pos.plus);
                                break;
                            case 'double':
                                ele.overlayGet(ele, 'double', ele.options.overlaySpeedOut, 0, ele.over_fly_out.fly_coor);
                                break
                        };
                        break
                }
            }
        })
    };
    Mate.prototype.init = function (ele) {
        $(window).resize($.proxy(this, 'onResize'));
        this.onResize();
        this.hover(ele, this.count_pop_label)
    };
    $.fn[pluginName] = function (options) {
        return this.each(function () {
            if (!$.data(this, 'plugin_' + pluginName)) {
                $.data(this, 'plugin_' + pluginName, new Mate(this, options))
            }
        })
    }
})(jQuery, window, document);