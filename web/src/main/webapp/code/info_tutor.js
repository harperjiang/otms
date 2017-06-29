function refreshTimesheet(date) {
	var tutorId = otms.getPageParam("otms.tutorInfoPage.tutorId", false);
	var tsctrl = otms.tutorInfoPage.timesheet;
	var request = otms.auth.req({
		'tutorId' : tutorId,
		'weekStart' : date
	});

	var loadTsCallback = function(success, data) {
		if (success) {
			var timesheet = data.timesheet;
			var specificValues = {};

			for (var i = 0; i < timesheet.refDates.length; i++) {
				var refDate = timesheet.refDates[i];
				var refValue = timesheet.values[i];

				specificValues[refDate] = otms.ui.timesheet.str2data(refValue);
			}
			tsctrl.model
					.updateAll(timesheet.defaultValue, specificValues, date);
		}
	};

	TutorService.getTimesheet(request, otms.ui.MessageBox.shan(loadTsCallback));
};

function setupTimesheet() {
	// Create Timesheet
	otms.tutorInfoPage.timesheet = new otms.ui.timesheet.Timesheet(
			$('#timesheet_container'), true);
	var ts = otms.tutorInfoPage.timesheet;
	ts.readonly(true);

	$('#pastweek_btn').click(function(event) {
		var curSunday = otms.DateUtil.toSunday(new Date());
		var date = otms.DateUtil(ts.model.currentDate, -7);
		if (date.getTime() >= curSunday) {
			refreshTimesheet(date);
		}
	});

	$('#today_btn').click(function(event) {
		var curSunday = otms.DateUtil.toSunday(new Date());
		refreshTimesheet(cursunday);
	});

	$('#nextweek_btn').click(function(event) {
		var date = otms.DateUtil(ts.model.currentDate, 7);
		refreshTimesheet(date);
	});

	otms.tutorInfoPage.timesheet.itemClicked = function(item) {
		if (item.prop('timesheet_value') != 1) {
			return;
		}
		// Set content
		var dataBean = otms.tutorInfoPage.viewbm.getBean();
		var tutorName = dataBean.username;
		var itemI = item.prop('timesheet_i');
		var itemJ = item.prop('timesheet_j');
		var weekdayNames = otms.i18n.get('weekday', otms.lang.current());
		var timeName = otms.DateUtil.formattime(itemJ * 30);

		var curdate = otms.tutorInfoPage.timesheet.model.currentDate;
		var date = otms.DateUtil.form(otms.DateUtil.offset(curdate, itemI),
				itemJ * 30);
		if (date.getTime() < Date.now()) {
			return;
		}

		otms.tutorInfoPage.selectedItem = item;

		$('#create_lesson_dialog_content').empty();

		var msg = otms.FormatUtil.format(
				"Schedule lesson with {0} on {1} at {2}?", tutorName,
				weekdayNames[itemI], timeName);
		$('#create_lesson_dialog_content').append(msg);

		$('#create_lesson_dialog').modal('show');

	};

	$('#create_lesson_dialog_confirm_btn').click(
			function(event) {
				$('#create_lesson_dialog').modal('hide');

				var item = otms.tutorInfoPage.selectedItem;
				var dataBean = otms.tutorInfoPage.viewbm.getBean();
				var tutorName = dataBean.username;

				// Jump to lesson class
				otms.setPageParam('otms.lessonPage.tutorName', tutorName);
				otms.setPageParam('otms.lessonPage.startTime', item
						.prop('timesheet_j') * 30);
				window.location = 'lesson.html';
			});

	otms.tutorInfoPage.itemScanned = function(item) {
		// Disable the scan function
	}

	refreshTimesheet(otms.DateUtil.toSunday(new Date()));

};

function setupRatingList() {
	var tutorId = otms.getPageParam("otms.tutorInfoPage.tutorId", false);
	var list = new otms.ui.list.List($('#rating_list_container'));
	list.setRenderer(function(container, item) {

//		var lessonRateSpan = $(document.createElement('span'));
//		var lessonRateCtrl = new otms.ui.StarRate(lessonRateSpan);
//		lessonRateCtrl.setRate(item.lessonRate);
//		lessonRateCtrl.readonly();
//		container.append(lessonRateSpan);

//		var tutorRateLabel = $(document.createElement('label'));
//		tutorRateLabel.append('')
		
		var tutorRateSpan = $(document.createElement('span'));
		var tutorRateCtrl = new otms.ui.StarRate(tutorRateSpan);
		tutorRateCtrl.setRate(item.tutorRate);
		tutorRateCtrl.readonly();
		container.append(tutorRateSpan);

		container.append(otms.FormatUtil.format('By {0} at {1}',
				item.clientName, otms.DateUtil.formatdate(item.createTime)));

		var commentDiv = $(document.createElement('div'));
		commentDiv.append(item.comment);
		container.append(commentDiv);

	});
	list.rowClicked = function(event, item) {

	};

	var listLoadCallback = function(success, data) {
		if (success) {
			list.model.setData(data.feedbacks);
		}
	};

	FeedbackService.getTutorFeedbacks({
		'tutorId' : tutorId,
		'limit' : 10
	}, otms.ui.MessageBox.shan(listLoadCallback));
}

$(function() {
	var tutorId = otms.getPageParam("otms.tutorInfoPage.tutorId", false);
	if (otms.isEmpty(tutorId)) {
		// Display invalid access
		otms.ui.MessageBox.warning($('#errmsg_panel'), 'Invalid Access');
		setTimeout(function() {
			window.location = 'index.html';
		}, 2000);
		return;
	}

	otms.namespace('otms.tutorInfoPage');

	// Star Rate
	var usrRate = new otms.ui.StarRate($('#rate_span'));
	usrRate.readonly();

	// Initialize Tab
	otms.tutorInfoPage.tabControl = new otms.ui.tab.TabControl([ $('#tab1'),
			$('#tab2'), $('#tab3') ], [ $('#timesheet_panel'),
			$('#self_intro_panel'), $('#user_rate_panel') ]);

	setupTimesheet();
	setupRatingList();

	var vbm = new otms.validator.BeanManager();
	vbm.reg('displayName', $('#name_span'));
	vbm.reg('username', $('#username_span'));
	vbm.reg('timezone', $('#timezone_span'));
	vbm.reg('pictureUrl', $('#profile_img'));
	vbm.reg('rating', $('#rate_span'));

	vbm.reg('description', $('#desc_span'));

	vbm.reg('statement', $('#stmt_span'));
	vbm.reg('eduInfo', $('#edu_span'));
	vbm.reg('workingInfo', $('#working_span'));
	vbm.reg('videoIntroUrl', $('#video_frame'));
	otms.tutorInfoPage.viewbm = vbm;

	var req = otms.auth.req({
		'tutorId' : tutorId
	});
	var callback = function(success, data) {
		if (success) {
			var tutorInfo = data.tutorInfo;
			// Noneditable
			otms.tutorInfoPage.viewbm.setBean(tutorInfo);
		}
	};
	TutorService.getTutorInfo(req, otms.ui.MessageBox.shan(callback));

});