otms.ui.StarRate = function(container) {
	this.container = container;

	new otms.ui.StarRateValidator(container);

	this.fixed = false;
	this.rdonly = false;
	// this.container.append("&nbsp;");
	this.container.addClass('starrate_panel');
	// this.container.addClass('starrate_0');
	this.container.prop('starRateObj', this);

	this.container.mousemove(this.refresh);
	this.container.mouseleave(this.clear);
	this.container.click(this.chooseRate);

};

otms.ui.StarRate.prototype.readonly = function() {
	this.rdonly = true;
	this.fixed = true;
}

otms.ui.StarRate.prototype.setRate = function(rate) {
	this.rate = rate;
	// var clazz = otms.FormatUtil.format('starrate_{0}', rate);
	// this.container.removeClass();
	// this.container.addClass('starrate_panel');
	// this.container.addClass(clazz);
	this.container.empty();
	var full = Math.floor(rate);
	var half = (rate - full) >= 0.5 ? 1 : 0;
	var empty = 5 - full - half;

	for (var i = 0; i < full; i++) {
		this.container.append('<i class="fa fa-star" aria-hidden="true"></i>');
	}
	for (var i = 0; i < half; i++) {
		this.container
				.append('<i class="fa fa-star-half-o" aria-hidden="true"></i>');
	}
	for (var i = 0; i < empty; i++) {
		this.container
				.append('<i class="fa fa-star-o" aria-hidden="true"></i>');
	}
	this.container.attr('title', otms.FormatUtil.format('{0}/5', rate));
};

otms.ui.StarRate.prototype.reset = function() {
	if (this.readonly)
		return;
	this.fixed = false;
	//	
	this.setRate(0);
	// this.container.removeClass();
	// this.container.addClass('starrate_panel');
	// this.container.addClass('starrate_0');
};

otms.ui.StarRate.index = function(offsetX) {
	var offset = Math.ceil(offsetX / 20);
	if (offset > 5)
		offset = 5;
	return offset;
};

otms.ui.StarRate.prototype.refresh = function(event) {
	var rateobj = this.starRateObj;
	if (rateobj.fixed == true)
		return;
	rateobj.setRate(otms.ui.StarRate.index(event.offsetX));
};

otms.ui.StarRate.prototype.clear = function(event) {
	var rateobj = this.starRateObj;
	if (rateobj.fixed == true)
		return;
	rateobj.reset();
};

otms.ui.StarRate.prototype.chooseRate = function(event) {
	var rateobj = this.starRateObj;
	if (rateobj.rdonly)
		return;
	rateobj.setRate(otms.ui.StarRate.index(event.offsetX));
	rateobj.fixed = true;
};

otms.ui.StarRateValidator = otms.extend(otms.validator.ValidatorBase, function(
		val) {
	otms.validator.ValidatorBase.call(this, val);
}, {
	trigger : function() {
		this.success = true;
	},
	value : function(val) {
		if (val === undefined) {
			var rateobj = this.control.prop('starRateObj');
			if (rateobj.fixed)
				return rateobj.rate;
			return 0;
		} else {
			return true;
		}
	},
	assign : function(newval) {
		if (otms.isEmpty(newval))
			return;
		this.control.prop('starRateObj').setRate(newval);
	}
});