otms.namespace('otms.ui.timesheet');

otms.ui.timesheet.data2Str = function(data) {
	var exp = "";
	for (var i = 0; i < 7; i++) {
		for (var j = 0; j < 48; j++) {
			exp += data[i][j];
		}
	}
	return exp;
};

otms.ui.timesheet.str2data = function(str) {
	var data = [];
	for (var i = 0; i < 7; i++) {
		data[i] = [];
		for (var j = 0; j < 48; j++) {
			data[i][j] = parseInt(str.charAt(i * 48 + j));
		}
	}
	return data;
};

otms.ui.timesheet.TimesheetModel = function() {
	this.data = [];

	for (var i = 0; i < 7; i++) {
		this.data[i] = [];
	}

	this.subtitles = function() {
		return undefined;
	}

	this.get = function(i, j) {
		return this.data[i][j];
	};

	this.set = function(i, j, value) {
		this.data[i][j] = value;

		if (this.listener !== undefined) {
			this.listener();
		}
	};

	this.update = function(data) {
		this.data = data;
		if (this.view !== undefined)
			this.view.refresh();
	}
};

otms.ui.timesheet.TimesheetDateModel = function() {

	this.data = {};

	this.defaultData = undefined;

	this.currentDate = otms.DateUtil.toSunday(new Date());

	this.subtitles = function() {
		var result = [];
		for (var i = 0; i < 7; i++) {
			result[i] = otms.DateUtil.formatdate(otms.DateUtil.offset(
					this.currentDate, i));
		}
		return result;
	};

	this.currentData = function() {
		if (otms.isEmpty(this.data[this.currentDate])) {
			return this.defaultData;
		} else {
			return this.data[this.currentDate];
		}
	}

	this.get = function(i, j) {
		var curdata = this.currentData();
		return curdata[i][j];
	};

	this.set = function(i, j, value) {
		// Only update item in future time
		var date = otms.DateUtil.form(
				otms.DateUtil.offset(this.currentDate, i), j * 30);
		if (date.getTime() < Date.now())
			return;
		if (!(this.currentDate in this.data)) {
			this.data[this.currentDate] = otms.jsonClone(this.defaultData);
		}
		this.data[this.currentDate][i][j] = value;

		if (this.listener !== undefined) {
			this.listener();
		}
	};

	this.update = function(data) {
		this.data = data;
		if (this.view !== undefined)
			this.view.refresh();
	}

	this.updateDefault = function(data) {
		this.defaultData = data;
		if (this.view !== undefined)
			this.view.refresh();
	}

	this.updateDate = function(date) {
		this.currentDate = otms.DateUtil.toSunday(date);

		if (this.view !== undefined)
			this.view.refresh();
	}

	this.updateAll = function(data, dateData, date) {
		if (data !== undefined)
			this.defaultData = data;
		if (dateData !== undefined)
			this.data = dateData;
		if (date !== undefined)
			this.currentDate = date;
		if (this.view !== undefined)
			this.view.refresh();
	}
};

otms.ui.timesheet.Timesheet = function(container, withdate) {
	this.container = container;
	this.withDate = (withdate !== undefined && withdate);

	if (this.withDate)
		this.model = new otms.ui.timesheet.TimesheetDateModel();
	else
		this.model = new otms.ui.timesheet.TimesheetModel();

	this.model.view = this;

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
		var i = item.prop('timesheet_i');
		var j = item.prop('timesheet_j');
		this.model.set(i, j, value);
		this.renderer(item, this.model.get(i, j));
	}

	this.refresh = function() {
		// Update subtitles
		var subtitles = this.model.subtitles();
		if (subtitles !== undefined) {
			for (var i = 0; i < 7; i++) {
				var id = '#' + this.container.attr('id')
						+ '_timesheet_title_date_' + i;
				var item = $(id);
				item.empty();
				item.append(subtitles[i]);
			}
		}

		// Update Date Information
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
			this.update(item, item.hasClass('timesheet-item-0') ? 1 : 0);
		}
	};

	this.itemScanned = function(item) {
		this.itemClicked(item);
	}

	/**
	 * Override this to customize the renderer
	 */
	this.renderer = function(item, value) {
		item.removeClass('timesheet-item-0');
		item.removeClass('timesheet-item-1');
		item.removeClass('timesheet-item-2');
		item.removeClass('timesheet-item-3');
		item.removeClass('timesheet-item-old');
		
		// Determine date
		var i = item.prop('timesheet_i');
		var j = item.prop('timesheet_j');

		var currentSunday = this.model.currentDate;
		if (!otms.isEmpty(currentSunday)) {
			var time = otms.DateUtil.form(otms.DateUtil
					.offset(currentSunday, i), j * 30);
			if (time.getTime() < Date.now()) {
				item.addClass('timesheet-item-old');
				return;
			}
		}

		var idx = parseInt(value);
		item.addClass('timesheet-item-' + idx);
		item.prop('timesheet_value', idx);

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

			if (this.withDate) {
				var datei = $(document.createElement('div'));
				datei.addClass('timesheet-title-date');
				datei.attr('id', this.container.attr('id')
						+ '_timesheet_title_date_' + i);
				title.append(datei);
			}
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
				item.addClass('timesheet-item-0');
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

}