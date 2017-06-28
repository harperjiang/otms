otms.namespace('otms.validator');

otms.validator.ValidatorBase = function(control) {
	var validator = this;
	control.prop('validator', this);
	this.control = control;
	this.checked = false;
	this.success = false;

	control.focusout(function(event) {
		validator.trigger();
	});
	control.change(function(event){
		validator.trigger();
	});
};



otms.validator.ValidatorBase.prototype.trigger = function() {
	var control = this.control;
	if (!this.check(this.extract())) {
		control.addClass('validate_fail');
		// this.oldTooltip = control.attr('title');
		control.attr('title', this.message());
		this.success = false;
	} else {
		control.removeClass('validate_fail');
		control.attr('title', '');
		this.success = true;
	}
};

otms.validator.ValidatorBase.prototype.force = function(message) {
	var control = $(this.control);
	control.addClass('validate_fail');
	// control.prop('oldTooltip', control.attr('title'));
	control.attr('title', message);
	this.success = false;
	this.message(message);
};

otms.validator.ValidatorBase.prototype.check = function(value) {
	this.checked = true;
	if (this.control.attr('type') == 'checkbox') {
		return this.value(this.control.is(':checked'));
	}
	if (this.control.attr('type') == 'radio') {
		// Find the selected one
		return this.value(this.control.filter(':checked').val());
	}
	return this.value(value);
};

otms.validator.ValidatorBase.prototype.value = function(arg) {
	if (arg === undefined) {
		if (this.checked) {
			return this.val;
		} else {
			return this.control.val();
		}
	}
	this.val = arg;
	return true;
};

otms.validator.ValidatorBase.prototype.extract = function() {
	var tagName = this.control.prop('tagName');
	if ($.inArray(tagName, [ 'INPUT', 'SELECT', 'TEXTAREA' ]) != -1) {
		if (this.control.attr('type') === 'checkbox') {
			return this.control.prop('checked');
		} else {
			return this.control.val();
		}
	} else if ($.inArray(tagName, [ 'SPAN', 'DIV' ]) != -1) {
		return this.control.text();
	} else {
		return this.control.val();
	}
}

otms.validator.ValidatorBase.prototype.assign = function(newval) {
	if (otms.isEmpty(newval))
		return;
	var tagName = this.control.prop('tagName');
	if ($.inArray(tagName, [ 'INPUT', 'SELECT', 'TEXTAREA' ]) != -1) {
		if (this.control.attr('type') === 'checkbox') {
			this.control.prop('checked', newval);
		} else {
			this.control.val(newval);
		}
	} else if ($.inArray(tagName, [ 'SPAN', 'DIV' ]) != -1) {
		this.control.empty();
		this.control.append(newval);
	} else {
		this.control.val(newval);
	}
};

otms.validator.ValidatorBase.prototype.message = function(arg) {
	if (arg === undefined)
		return this.msg;
	this.msg = arg;
	return false;
};

// Use Regular Expression to validate input
otms.validator.RegexValidator = otms.extend(otms.validator.ValidatorBase,
		function(val, exp) {
			otms.validator.ValidatorBase.call(this, val);
			this.exp = exp;
		}, {
			check : function(content) {
				this.checked = true;
				if (new RegExp(this.exp).test(content)) {
					return this.value(content);
				}
				return this.message('Incorrect data format');
			}
		});

otms.validator.EmailValidator = otms.extend(otms.validator.RegexValidator,
		function(val) {
			otms.validator.RegexValidator.call(this, val,
					'^[\\w\\d_\\.]+@[\\w\\d]+\\.[\\w]+$');
		}, {});

otms.validator.NonemptyValidator = otms.extend(otms.validator.ValidatorBase,
		function(val) {
			otms.validator.ValidatorBase.call(this, val);
		}, {
			check : function(content) {
				this.checked = true;
				if (otms.isEmpty(content)) {
					return this.message('The field cannot be left empty');
				}
				return this.value(content);
			}
		});

otms.validator.PasswordSymbol = [ '~', '`', '!', '@', '#', '$', '%', '^', '&',
		'*', '(', ')', '-', '_', '+', '=', '[', ']', '\\', '|', '/', ':', ';',
		'"', '\'', '<', '>', ',', '.', '?' ];
otms.validator.PasswordValidator = otms.extend(otms.validator.ValidatorBase,
		function(val) {
			otms.validator.ValidatorBase.call(this, val);
		}, {
			check : function(content) {
				this.checked = true;
				if (otms.isEmpty(content)) {
					return this.message('The field cannot be left empty');
				}
				if (content.length < 8) {
					return this.message('Password should contain'
							+ 'at least 8 characters');
				}
				var hasSymbol = false, hasLetter = false, hasNumber = false;
				for (var i = 0, len = content.length; i < len; i++) {
					var char = content[i];
					var charcode = content.charCodeAt(i);
					if ($.inArray(char, otms.validator.PasswordSymbol) != -1) {
						hasSymbol = true;
					}
					if (charcode <= 57 && charcode >= 48) {
						hasNumber = true;
					}
					if ((charcode >= 65 && charcode <= 90)
							|| (charcode >= 97 && charcode <= 122)) {
						hasLetter = true;
					}
				}
				if (!(hasSymbol && hasLetter && hasNumber)) {
					return this.message('Password should contain '
							+ 'letter, number and symbols');
				}
				return this.value(content);
			}
		});

// Accepted time format:
// 0 12 13 0:30 3am 5pm 3:01pm

otms.validator.TimeValidator = otms.extend(otms.validator.ValidatorBase,
		function(val) {
			otms.validator.ValidatorBase.call(this, val);
		}, {
			check : function(content) {
				this.checked = true;
				if (otms.isEmpty(content)) {
					return this.message('The field cannot be left empty');
				}
				var apmMatch = content.toLowerCase().match('(am|pm)$');
				var withApm = (apmMatch != null);
				var am = false;
				if (withApm) {
					am = apmMatch[0] == 'am';
					content = content.substr(0, content.length - 2);
				} else {
					am = undefined;
				}
				var single = new RegExp('^(\\d{1,2})$');
				var twoparts = new RegExp('^(\\d{1,2}):(\\d{2})');

				var smresult = content.match(single);
				if (smresult != null) {
					var num = parseInt(smresult[1]);
					if (withApm ? (num <= 12) : (num <= 23)) {
						// Form a time
						return this.value(otms.DateUtil.time(num, 0, am));
					} else {
						return this.message('Incorrect time value');
					}
				}

				var tmresult = content.match(twoparts);
				if (tmresult != null) {
					var hour = parseInt(tmresult[1]);
					var min = parseInt(tmresult[2]);
					if ((withApm ? (hour <= 12) : (hour <= 23)) && (min < 60)) {
						// Form a time
						return this.value(otms.DateUtil.time(hour, min, am));
					} else {
						return this.message('Incorrect time value');
					}
				}
				return this.message('Incorrect time format');
			},
			assign : function(value) {
				if (otms.isEmpty(value))
					return;
				otms.validator.ValidatorBase.prototype.assign.call(this,
						otms.DateUtil.formattime(value));
			}
		});

// Accept 5/23 3/24/15 05/12/2015
otms.validator.DateValidator = otms.extend(otms.validator.ValidatorBase,
		function(control) {
			otms.validator.ValidatorBase.call(this, control);
		}, {
			check : function(content) {
				this.checked = true;
				if (otms.isEmpty(content)) {
					return this.message('The field cannot be left empty');
				}
				var withyear = new RegExp(
						'^(\\d{1,2})/(\\d{1,2})/(\\d{2}|\\d{4})$');
				var monthdate = new RegExp('^(\\d{1,2})/(\\d{1,2})');

				var wyresult = content.match(withyear);
				if (wyresult != null) {
					var month = parseInt(wyresult[1]);
					var date = parseInt(wyresult[2]);
					var year = parseInt(wyresult[3]);

					if (month >= 13 && date >= 32) {
						return this.message('Incorrect date value');
					}
					// Form a date
					var result = otms.DateUtil.date(month, date, year);
					if (result == null)
						return this.message('Incorrect date value');
					return this.value(result);
				}
				var mdresult = content.match(monthdate);
				if (mdresult != null) {
					var month = parseInt(mdresult[1]);
					var date = parseInt(mdresult[2]);

					if (month >= 13 && date >= 32) {
						return this.message('Incorrect date value');
					}
					var result = otms.DateUtil.date(month, date);
					if (result == null)
						return this.message('Incorrect date value');
					return this.value(result);
				}

				return this.message('Incorrect date format');
			},
			assign : function(value) {
				if (otms.isEmpty(value))
					return;
				otms.validator.ValidatorBase.prototype.assign.call(this,
						otms.DateUtil.formatdate(value));
			}
		});

otms.validator.BeanManager = function() {
	this.components = {};
};

otms.validator.BeanManager.prototype.reg = function(key, component) {
	this.components[key] = component;
	if (component.prop('validator') == undefined) {
		// Install an empty validator
		new otms.validator.ValidatorBase(component);
	}
};

otms.validator.BeanManager.prototype.validate = function(bean, vresult) {
	for ( var key in vresult) {
		if (!vresult[key])
			return false;
	}
	return true;
};

otms.validator.BeanManager.prototype.fail = function(keys, message) {
	for ( var key in keys)
		this.components[keys[key]].prop('validator').force(message);
};

otms.validator.BeanManager.prototype.getBean = function() {
	if (this.bean === undefined) {
		this.bean = {};
	}
	var bean = this.bean;
	var vresult = {};
	// Collect data from each validator
	for ( var key in this.components) {
		var comp = this.components[key];
		debugger;
		if (otms.UIUtil.hidden(comp.val()))
			continue;
		var validator = comp.prop('validator');
		if (!validator.checked) {
			validator.trigger();
		}
		otms.set(bean, key, validator.value());
		vresult[key] = validator.success;
	}
	return (this.validate(bean, vresult) ? bean : null);
};

otms.validator.BeanManager.prototype.setBean = function(bean) {
	this.bean = otms.clone(bean);
	// Remove unused data and set data to each validator
	for ( var key in this.bean) {
		if (this.components[key] === undefined) {
			delete this.bean[key];
		} else {
			var comp = this.components[key];
			if (comp.val() === undefined) {
				console.log('Invalid component found:' + key);
			} else {
				if (otms.UIUtil.hidden(comp.val()))
					continue;
				var validator = comp.prop('validator');
				validator.assign(otms.get(this.bean, key));
			}
		}
	}
};