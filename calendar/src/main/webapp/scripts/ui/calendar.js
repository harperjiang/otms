/**
 * Event Example: {'id': 1, 'text':'abc'};
 */
otms.namespace('otms.ui.calendar');

otms.ui.calendar.monthName = [ 'January', 'February', 'March', 'April', 'May',
		'June', 'July', 'August', 'Sepetember', 'October', 'November',
		'December' ];
otms.ui.calendar.weekName = [ 'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat' ];

otms.ui.calendar.CalendarModel = function(calendar) {
	this.calendar = calendar;
};

/**
 * Data structure: { type, id, date, title, fromTime, toTime}
 * 
 * @see EventDto
 * @param data
 */
otms.ui.calendar.CalendarModel.prototype.setData = function(data) {
	this.store = {};
	for ( var index in data.events) {
		var item = data.events[index];
		if (this.store[item.date.toDateString()] === undefined) {
			this.store[item.date.toDateString()] = [];
		}
		this.store[item.date.toDateString()].push(item);
	}
	for ( var key in this.store) {
		this.store[key].sort(function(a, b) {
			return a.fromTime - b.fromTime;
		});
	}
	this.calendar.refresh();
};

otms.ui.calendar.CalendarModel.prototype.getEvents = function(date) {
	return this.store[date.toDateString()];
};

otms.ui.calendar.CalendarModel.prototype.load = function(fromDate, toDate) {
	// User should implement this method
};

otms.ui.calendar.CalendarModel.prototype.setDate = function(date) {
	this.date = date;
	if (date.getTime() >= this.fromDate && date.getTime() <= this.toDate)
		// Still in the range, nothing to do
		return;
	// Calculate the first date and the last date to display
	var fromdate = new Date(date);
	fromdate.setDate(1);
	// The first date of the month
	var week = fromdate.getDay();
	// Day 1 is (0, week)
	// From date is day 1 - week
	fromdate.setDate(fromdate.getDate() - week);

	var todate = new Date(fromdate);
	todate.setDate(fromdate.getDate() + 41);

	this.fromDate = otms.DateUtil.truncate(fromdate);
	this.toDate = otms.DateUtil.truncate(todate);

	// load data based on from date and to date
	this.load(this.fromDate, this.toDate);
};

otms.ui.calendar.CalendarModel.prototype.addDate = function(type, offset,
		calendar) {
	switch (type) {
	case 'month':
		var dateref = new Date(this.date);
		dateref.setMonth(dateref.getMonth() + offset);
		this.setDate(dateref);
		break;
	default:
		break;
	}
};

otms.ui.calendar.CalendarModel.prototype.getMonthName = function() {
	var month = otms.ui.calendar.monthName[this.date.getMonth()];
	var year = this.date.getFullYear();
	return month + ' ' + year;
};

otms.ui.calendar.CalendarModel.prototype.dateText = function(weekday, j) {
	var today = otms.DateUtil.truncate(new Date());
	var dateref = new Date(this.fromDate);
	var dateIndex = j * 7 + weekday;
	dateref.setDate(dateref.getDate() + dateIndex);

	return {
		'text' : dateref.getDate() + '',
		'date' : new Date(dateref),
		'notcurrent' : dateref.getMonth() != this.date.getMonth(),
		'isToday' : dateref.getTime() == today.getTime(),
		'isOld' : dateref.getTime() < today.getTime()
	};
};

otms.ui.calendar.ChooseEventDialog = otms.extend(otms.ui.dialog.Dialog,
		function(parent) {
			otms.ui.dialog.Dialog.call(this, parent);
			this.setSize(250, 0);
		}, {
			drawTitle : function(titlePanel) {
				titlePanel.append('What do you want to open?');
			},
			drawContent : function(contentPanel) {
				var label1 = $(document.createElement('label'));
				var input1 = $(document.createElement('input'));
				input1.attr('name', 'modify_type');
				input1.attr('value', 'meeting');
				input1.attr('type', 'radio');
				input1.attr('checked', 'true');
				label1.append(input1);
				label1.append('The lesson');
				contentPanel.append(label1);

				var label2 = $(document.createElement('label'));
				var input2 = $(document.createElement('input'));
				input2.attr('name', 'modify_type');
				input2.attr('value', 'item');
				input2.attr('type', 'radio');
				label2.append(input2);
				label2.append('This occurance');
				contentPanel.append(label2);
			}
		});

otms.ui.calendar.Calendar = function(container) {
	this.container = container;
	this.container.addClass('calendar-container');

	// Create Structure
	this.initialize();
	// Display the calendar
	this.model = new otms.ui.calendar.CalendarModel(this);
};

otms.ui.calendar.Calendar.prototype.initialize = function() {
	var calendar = this;
	var titleDiv = $(document.createElement("div"));
	titleDiv.attr('itemId', 'titleDiv');
	titleDiv.addClass('calendar-title');

	var buttonBar = $(document.createElement('div'));
	buttonBar.addClass('btn-group');
	buttonBar.addClass('calendar-btnbar');
	buttonBar.addClass('mr-auto');
	titleDiv.append(buttonBar);

	var prevButton = $(document.createElement('button'));
	prevButton.append('<i class="fa fa-chevron-left" aria-hidden="true"></i>');
	prevButton.append('&nbsp;')
	prevButton.append('Last Month');
	prevButton.addClass('calendar-btn');
	prevButton.addClass('btn');
	prevButton.addClass('btn-link');
	prevButton.addClass('btn-sm');
	prevButton.click(function() {
		calendar.model.addDate("month", -1, calendar);
	});
	buttonBar.append(prevButton);

	var todayButton = $(document.createElement('button'));
	todayButton.append('Today');
	todayButton.addClass('calendar-btn');
	todayButton.addClass('btn');
	todayButton.addClass('btn-link');
	todayButton.addClass('btn-sm');
	todayButton.click(function() {
		calendar.model.setDate(new Date(), calendar);
	});
	buttonBar.append(todayButton);

	var nextButton = $(document.createElement('button'));
	nextButton.append("Next Month");
	nextButton.append('&nbsp;')
	nextButton.append('<i class="fa fa-chevron-right" aria-hidden="true"></i>');
	nextButton.addClass('calendar-btn');
	nextButton.addClass('btn');
	nextButton.addClass('btn-link');
	nextButton.addClass('btn-sm');

	nextButton.click(function() {
		calendar.model.addDate("month", 1, calendar);
	});
	buttonBar.append(nextButton);

	var titleLabel = $(document.createElement('span'));
	titleLabel.attr('itemId', 'titleLabel');
	titleDiv.append(titleLabel);

	var addButton = $(document.createElement('button'));
	addButton.append('<i class="fa fa-plus" aria-hidden="true"></i>');
	addButton.addClass('calendar-btn');
	addButton.addClass('btn');
	addButton.addClass('btn-sm');
	addButton.addClass('btn-link');
	addButton.css('float', 'right');
	addButton.attr('title', 'Create New Event');
	addButton.click(function(event) {
		calendar.onAddButton();
	});
	titleDiv.append(addButton);

	this.container.append(titleDiv);

	// Generate Column Headers
	for (var i = 0; i < 7; i++) {
		var columnDiv = $(document.createElement('div'));
		columnDiv.attr('itemId', 'column_' + i);
		columnDiv.addClass('calendar-column');
		this.container.append(columnDiv);

		var columnTitleDiv = $(document.createElement('div'));
		columnTitleDiv.addClass('calendar-column_header');
		columnTitleDiv.append(otms.ui.calendar.weekName[i]);
		columnDiv.append(columnTitleDiv);

		if (i == 6) {
			columnTitleDiv.addClass('calendar-item_end');
		}

		for (var j = 0; j < 6; j++) {
			var itemDiv = $(document.createElement('div'));
			itemDiv.addClass('calendar-item');
			itemDiv.attr('itemId', 'item_' + i + '_' + j);
			itemDiv.prop('calendar', this);
			var itemDateDiv = $(document.createElement('div'));
			itemDateDiv.addClass('calendar-item_date');

			itemDateDiv.attr('itemId', 'dateDiv');

			itemDiv.append(itemDateDiv);
			if (i == 6) {
				itemDiv.addClass('calendar-item_end');
			}

			columnDiv.append(itemDiv);
		}
	}
	this.popupDiv = $(document.createElement('div'));
	this.popupDiv.addClass('calendar-popup');
	this.popupDiv.attr('itemId', 'popupDiv');
	this.popupDiv.css('z-index', '1');
	this.popupDiv.css('visibility', 'hidden');
	this.container.append(this.popupDiv);

	var popupTitleDiv = $(document.createElement('div'));
	popupTitleDiv.attr('itemId', 'popupTitleDiv');
	popupTitleDiv.addClass('calendar-popup_title');
	popupTitleDiv.append('Abc');
	this.popupDiv.append(popupTitleDiv);

	var popupContentDiv = $(document.createElement('div'));
	popupContentDiv.attr('itemId', 'popupContentDiv');
	popupContentDiv.addClass('calendar-popup_content');
	this.popupDiv.append(popupContentDiv);
};

otms.ui.calendar.Calendar.prototype.eventRenderer = function(eventDiv, eventObj) {
	/*
	 * eventContent = otms.DateUtil.formattime(eventObj.fromTime);
	 * eventDiv.append(eventContent); eventDiv.prop('title', eventContent);
	 */
};

otms.ui.calendar.Calendar.prototype.refresh = function() {
	var calendar = this;
	var title = this.model.getMonthName();
	var titleLabel = this.container.find('[itemId="titleLabel"]');
	titleLabel.empty();
	titleLabel.append(title);

	for (var i = 0; i < 7; i++) {
		for (var j = 0; j < 6; j++) {
			var dtext = this.model.dateText(i, j);
			var itemDiv = this.container.find('[itemId="item_' + i + '_' + j
					+ '"]');
			var dateTextDiv = itemDiv.find('[itemId="dateDiv"]');
			dateTextDiv.empty();
			dateTextDiv.append(dtext.text);
			itemDiv.prop('date', dtext.date);

			itemDiv.empty();
			itemDiv.append(dateTextDiv);

			// Draw events
			var todayEvents = this.model.getEvents(dtext.date);
			if (todayEvents == undefined || todayEvents == null
					|| todayEvents.length == 0) {
				itemDiv.append('&nbsp;');
			} else {
				var max = Math.min(3, todayEvents.length);

				for (var e = 0; e < max; e++) {
					var eventObj = todayEvents[e];
					var eventDiv = $(document.createElement('div'));
					eventDiv.prop('eventData', eventObj);
					eventDiv.addClass('calendar-event');
					eventDiv.click(function(event) {
						calendar.onEventClick(this, event);
					});
					calendar.eventRenderer(eventDiv, eventObj);
					// For event in the past, update background color
					if (eventObj.past) {
						eventDiv.addClass('calendar-event-past');
					}
					itemDiv.append(eventDiv);
				}
				// Add a 'View More' Link
				if (max < todayEvents.length) {
					var viewLink = $(document.createElement('a'));
					viewLink.append('+View ' + (todayEvents.length - max)
							+ ' More');
					viewLink.prop('calendarItem', itemDiv);
					viewLink.click(function(event) {
						calendar.onItemClick(this.calendarItem, event);
					});
					itemDiv.append(viewLink);
				}
			}

			// Not current month
			if (dtext.notcurrent) {
				dateTextDiv.addClass('calendar-item_notcurrent');
			} else {
				dateTextDiv.removeClass('calendar-item_notcurrent');
			}
			// Today
			if (dtext.isToday) {
				itemDiv.addClass('calendar-item_today');
			} else {
				itemDiv.removeClass('calendar-item_today');
			}
			// Old days
			if (dtext.isOld) {
				itemDiv.addClass('calendar-item_old');
			} else {
				itemDiv.removeClass('calendar-item_old');
			}
		}
	}
	this.popupDiv.css('visibility', 'hidden');
};

otms.ui.calendar.Calendar.prototype.onItemClick = function(jqobj, event) {
	var obj = jqobj.get()[0];
	// Display the popup div at the location of current
	// item
	var visible = this.popupDiv.css('visibility');
	var calendar = this;
	if (visible == 'visible') {
		this.popupDiv.css('visibility', 'hidden');
	} else {
		var popupDiv = this.popupDiv;

		// Move to the clicked item
		popupDiv.css('left', obj.offsetLeft);
		popupDiv.css('top', obj.offsetTop);
		var titleDiv = popupDiv.find('[itemId="popupTitleDiv"]');
		var contentDiv = popupDiv.find('[itemId="popupContentDiv"]');

		titleDiv.empty();
		titleDiv.append(obj.date.toDateString());

		contentDiv.empty();
		var todayEvents = this.model.getEvents(obj.date);
		if (todayEvents.length == 0) {
			contentDiv.append('&nbsp;');
		} else {
			for (var e = 0; e < todayEvents.length; e++) {
				var eventObj = todayEvents[e];
				var eventDiv = $(document.createElement('div'));
				eventDiv.addClass('calendar-event');
				eventDiv.append(eventObj.text);
				eventDiv.prop('eventId', eventObj.id);
				eventDiv.click(function(event) {
					calendar.onEventClick(this, event);
				});
				contentDiv.append(eventDiv);
			}
		}
		popupDiv.css('visibility', 'visible');
	}
};

otms.ui.calendar.Calendar.prototype.onEventClick = function(obj, event) {

};

otms.ui.calendar.Calendar.prototype.onAddButton = function() {

};