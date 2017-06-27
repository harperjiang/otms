$(function() {

	otms.namespace('otms.setting.timesheetPage');

	otms.setting.timesheetPage.mainTab = new otms.ui.tab.TabControl([
			$('#tab1'), $('#tab2') ], [ $('#regular_panel'),
			$('#specify_panel') ]);

	otms.setting.timesheetPage.regularTs = new otms.ui.timesheet.Timesheet(
			$('#regular_container'));
	otms.setting.timesheetPage.specifyTs = new otms.ui.timesheet.Timesheet(
			$('#specify_container'), true);
	var regTs = otms.setting.timesheetPage.regularTs;
	var speTs = otms.setting.timesheetPage.specifyTs;

	regTs.model.listener = function() {
		speTs.model.updateDefault(regTs.model.data);
	};

	// Load data
	var loadCallback = function(success, data) {
		var timesheet = data.timesheet;
		var defaultValue = otms.ui.timesheet.str2data(timesheet.defaultValue);
		var specificValues = {};

		for (var i = 0; i < timesheet.refDates.length; i++) {
			var refDate = timesheet.refDates[i];
			var refValue = timesheet.values[i];

			specificValues[refDate] = otms.ui.timesheet.str2data(refValue);
		}

		regTs.model.update(defaultValue);
		speTs.model.updateDefault(defaultValue);
		speTs.model.update(specificValues);
	};

	TutorService.getTimesheet(otms.auth.req({
		'tutorId' : otms.auth.currentUser()
	}), otms.ui.MessageBox.shan(loadCallback));

	$('#past_week_btn').click(function(event) {
		var curSunday = otms.DateUtil.toSunday(new Date());
		var date = otms.DateUtil(speTs.model.currentDate, -7);
		if (date.getTime() >= curSunday) {
			speTs.model.updateDate(date);
		}
	});
	$('#next_week_btn').click(function(event) {
		var newDate = otms.DateUtil.offset(speTs.model.currentDate, 7);
		speTs.model.updateDate(newDate);
	});
	$('#today_btn').click(function(event) {
		var newDate = new Date();
		speTs.model.updateDate(newDate);
	});
	$('#reset_btn').click(function(event) {
		var data = speTs.model.data;
		delete data[speTs.model.currentDate];
		ts.model.update(data);
	});

	var saveCallback = function(success, data) {

	};

	$('#save_btn').click(function(event) {
		debugger;
		var defaultValue = regTs.model.data;
		var datedValues = speTs.model.data;

		var dates = [];
		var values = [];

		for ( var d in datedValues) {
			dates.push(new Date(d));
			values.push(otms.ui.timesheet.data2Str(datedValues[d]));
		}

		var ts = {
			'defaultValue' : otms.ui.timesheet.data2Str(defaultValue),
			'refDates' : dates,
			'values' : values
		};

		TutorService.setupTimesheet(otms.auth.req({
			'timesheet' : ts
		}), otms.ui.MessageBox.han(saveCallback));
	});
});