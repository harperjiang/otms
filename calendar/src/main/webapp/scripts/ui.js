if (window.otms === undefined) {
	window.otms = {};
}

otms.loadScript = function(url) {
	var head = document.getElementsByTagName('head')[0];
	var script = document.createElement('script');
	script.type = 'text/javascript';
	script.src = url;
	head.appendChild(script);
};

otms.namespace = function(namespace) {
	var parts = namespace.split('.');
	var current = window;
	for (var i = 0; i < parts.length; i++) {
		if (current[parts[i]] === undefined) {
			current[parts[i]] = {};
		}
		current = current[parts[i]];
	}
};

otms.namespace('otms.ui');

otms.extend = function(base, cons, extend) {
	cons.prototype = Object.create(base.prototype);
	cons.prototype.constructor = cons;
	for ( var key in extend) {
		cons.prototype[key] = extend[key];
	}
	return cons;
};

otms.isEmpty = function(str) {
	return str == undefined || str == null || str === '';
};

otms.get = function(object, key) {
	var parts = key.split('.');
	var current = object;
	for (var i = 0; i < parts.length; i++) {
		var part = parts[i];
		var index = parseInt(part);
		if (current === undefined)
			return undefined;
		// In get value case, keep going down
		if (isNaN(index)) {
			current = current[part];
		} else {
			current = current[index];
		}
	}
	return current;
};

otms.set = function(object, key, value) {
	// debugger;
	var parts = key.split('.');
	var current = object;
	for (var i = 0; i < parts.length; i++) {
		var part = parts[i];

		var index = parseInt(part);

		if (i == parts.length - 1) {
			// Set value and coming to the last one
			if (isNaN(index)) {
				current[part] = value;
			} else {
				current[index] = value;
			}
		} else {
			// In get value case, keep going down
			var next = isNaN(index) ? current[part] : current[index];
			if (next == undefined) {
				if (isNaN(parseInt(parts[i + 1]))) {
					// Next is a key
					next = {};
				} else {
					next = [];
				}
				if (isNaN(index)) {
					current[part] = next;
				} else {
					current[index] = next;
				}
			}
			current = next;
		}
	}
};

otms.uuid = function() {
	function s4() {
		return Math.floor((1 + Math.random()) * 0x10000).toString(16)
				.substring(1);
	}
	return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4()
			+ s4() + s4();
};

otms.UIUtil = {};

otms.UIUtil.hidden = function(element) {
	return (element.offsetParent === null);

};

otms.FormatUtil = {};

otms.FormatUtil.padding = function(input, padding, length) {
	var string = String(input);
	while (string.length < length) {
		string = padding + string;
	}
	return string;
};

otms.DateUtil = {};

otms.DateUtil.time = function(hour, min, am) {
	if (am != undefined) {
		if (hour == 12 && am) {
			hour = 0;
		} else if (!am) {
			hour += 12;
		}
	}
	return {
		'hour' : hour,
		'minute' : min,
		'total' : hour * 60 + min
	};
};

otms.DateUtil.formattime = function(time) {
	var hour = 0;
	var min = 0;
	switch (typeof (time)) {
	case "number":
		hour = Math.round(time / 60);
		min = time % 60;
		break;
	case "object":
		hour = time.hour;
		min = time.minute;
		break;
	default:
		break;
	}

	var am = true;
	if (hour >= 12) {
		am = false;
	}
	if (hour > 12) {
		hour -= 12;
	}
	if (hour == 0) {
		hour = 12;
	}
	var minstr = (min == 0) ? "" : otms.FormatUtil.padding(min, "0", 2);
	if (minstr.length > 0) {
		minstr = ":" + minstr;
	}
	return hour + minstr + (am ? "am" : "pm");
};

otms.DateUtil.date = function(month, date, year) {
	if (year === undefined) {
		year = new Date().getFullYear();
	}
	return new Date(year, month - 1, date);
};

otms.DateUtil.formatdate = function(date) {
	return otms.FormatUtil.padding((date.getMonth() + 1), "0", 2) + "/"
			+ otms.FormatUtil.padding(date.getDate(), "0", 2) + "/"
			+ date.getFullYear();
};

otms.DateUtil.truncate = function(input) {
	var date = new Date(input);
	date.setHours(0);
	date.setMinutes(0);
	date.setSeconds(0);
	date.setMilliseconds(0);
	return date;
};

otms.DateUtil.weekdayAbb = [ 'Su', 'M', 'Tu', 'W', 'Th', 'F', 'Sa' ];

otms.DateUtil.formatweekday = function(input) {
	var sum = 0;
	for (var i = 0; i < 7; i++) {
		sum += ((input[i] ? 1 : 0) << i);
	}
	if (sum == 65) {
		return "weekend";
	}
	if (sum == 62) {
		return "weekday";
	}
	if (sum == 42) {
		return "MWF";
	}
	if (sum == 20) {
		return "Tu,Th";
	}
	var val = "";
	for (var i = 0; i < 7; i++) {
		if (input[i]) {
			val += otms.DateUtil.weekdayAbb[i];
			val += ",";
		}
	}
	return val.slice(0, val.length - 1);
};