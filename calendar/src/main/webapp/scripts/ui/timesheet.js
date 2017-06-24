otms.namespace('otms.ui.timesheet');

otms.ui.timesheet.TimesheetModel = function() {

	this.data = [];
	for (var i = 0; i < 7; i++) {
		this.data[i] = [];
	}

	this.get = function(i, j) {
		return this.data[i][j];
	};

	this.set = function(i, j, value) {
		this.data[i][j] = value;
	};

	this.setWithKey = function(key, value) {
		var i = parseInt(key.substr(0, 1));
		var j = parseInt(key.substr(2));
		this.set(i, j, value);
	};

	// Convert data to string expressions
	this.getData = function() {
		var exps = [];
		for (var i = 0; i < 7; i++) {
			var exp = "";
			for (var j = 0; j < 48; j++) {
				exp += get(i, j);
			}
			exps[i] = exp;
		}
		return exps;
	};

	// Load data from string expressions
	this.setData = function(exps) {
		for (var i = 0; i < 7; i++) {
			for (var j = 0; j < 48; j++) {
				this.set(i, j, parseInt(exps[i].charAt(j)));
			}
		}
	};
};

otms.ui.timesheet.Timesheet = function(container) {
	this.container = container;
	this.model = new otms.ui.timesheet.TimesheetModel();

	this.timeFormat = function(input) {
		var am = input < 12;
		var display = input % 12;
		if (display == 0)
			display = 12;
		return {
			'display' : display,
			'am' : am
		};
	};

	this.item = function(i, j) {
		var itemId = this.container.attr('id') + "_timesheet_item_" + i + "_"
				+ j;
		return $('#' + itemId);
	}

	this.update = function(item, value) {
		this.renderer(item, value);
		this.model.set(item.prop('timesheet_i'), item.prop('timesheet_j'),
				value);
	}

	this.refresh = function() {
		for (var i = 0; i < 7; i++) {
			for (var j = 0; j < 48; j++) {
				this.renderer(this.item(i, j), this.model.get(i, j));
			}
		}
	};

	this.readonly = function(val) {
		this._readonly = val ? true : false;
	};

	/**
	 * Override these two methods to react to mouse behavior
	 */
	this.itemClicked = function(item) {
		if (!this._readonly) {
			this.update(item, item.hasClass('timesheet-item-na'));
		}
	};

	this.itemScanned = function(item) {
		this.itemClicked(item);
	}

	/**
	 * Override this to customize the renderer
	 */
	this.renderer = function(item, value) {
		item.removeClass('timesheet-item-a');
		item.removeClass('timesheet-item-na');
		if (value) {
			item.addClass('timesheet-item-a');
		} else {
			item.addClass('timesheet-item-na');
		}
	};

	// Initialize
	this.initialize = function() {

		var self = this;
		this.container.addClass("timesheet-container");

		var header = $(document.createElement('div'));
		header.addClass('timesheet-header');
		container.append(header);

		var topleft = $(document.createElement('div'));
		topleft.addClass('timesheet-topleft');
		topleft.append('&nbsp;');
		header.append(topleft);

		// Make content
		var weekday = [ 'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat' ];

		for (var i = 0; i < 7; i++) {
			var column = $(document.createElement('div'));
			column.addClass('timesheet-column');
			header.append(column);

			var title = $(document.createElement('div'));
			title.addClass('timesheet-title');
			if (i == 6) {
				title.addClass('timesheet-end');
			}
			column.append(title);

			var weeki = $(document.createElement('div'));
			weeki.addClass('timesheet-title-week');
			weeki.append(weekday[i]);
			title.append(weeki);

			var datei = $(document.createElement('div'));
			datei.addClass('timesheet-title-date');
			datei.attr('id', this.container.attr('id')
					+ '_timesheet_title_date_' + i);
			datei.append('01/21/2014');
			// title.append(datei);
		}

		var panel = $(document.createElement("div"));
		panel.addClass('timesheet-frame');
		container.append(panel);

		var leftcol = $(document.createElement("div"));
		leftcol.addClass('timesheet-leftcol');
		panel.append(leftcol);

		for (var i = 0; i < 24; i++) {
			var timeDiv = $(document.createElement('div'));
			timeDiv.addClass('timesheet-lefttime');

			var timeFormat = this.timeFormat(i);

			var am = timeFormat.am;
			var display = timeFormat.display;
			timeDiv.append('<span>' + display + '</span>');

			var flag = $(document.createElement('span'));
			flag.addClass('timesheet-leftflag');
			flag.append(am ? 'AM' : 'PM');
			timeDiv.append(flag);
			leftcol.append(timeDiv);
		}

		for (var i = 0; i < 7; i++) {
			var column = $(document.createElement('div'));
			column.addClass('timesheet-column');
			panel.append(column);

			for (var j = 0; j < 48; j++) {
				var item = $(document.createElement('div'));
				item.addClass('timesheet-item');
				item.attr('id', this.container.attr('id') + '_timesheet_item_'
						+ i + '_' + j);

				var timeFormat = this.timeFormat(((j / 2) | 0));
				item.addClass('timesheet-item-na');
				item.attr('title', weekday[i] + ' ' + timeFormat.display
						+ (j % 2 == 1 ? ':30' : ':00')
						+ (timeFormat.am ? 'AM' : 'PM'));
				// add this to forcefully disable the unwanted drag
				item.on('dragstart', function(event) {
					return false;
				});
				item.prop('timesheet', this);
				item.prop('timesheet_i', i);
				item.prop('timesheet_j', j);

				item.click(function(event) {
					self.itemClicked($(this));
				});
				item.mouseleave(function(event) {
					if (event.which != 0) {
						self.itemScanned($(this));
					}

				});

				column.append(item);
			}
		}
	};

	this.initialize();
	this.refresh();

}