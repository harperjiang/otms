function snapshot_mode() {
	$('#view_panel').css('display', 'block');
	$('#modify_panel').css('display', 'none');
};

function onload() {
	// Initialize to lesson_item mode
	otms.namespace('otms.lessonItemPage');
	otms.lessonItemPage.snapshotMode = false;

	$('#date_input').datepicker();
	
	// Install validator
	new otms.validator.TimeValidator($('#timefrom_input'));
	new otms.validator.TimeValidator($('#timeto_input'));
	new otms.validator.DateValidator($('#date_input'));

	new otms.validator.TimeValidator($('#timefrom_span'));
	new otms.validator.TimeValidator($('#timeto_span'));
	new otms.validator.DateValidator($('#date_span'));

	// Install BeanManager
	var mbm = new otms.validator.BeanManager();
	// mbm.reg('title', $('#title_input'));
	mbm.reg('tutorName', $('#tutor_input'));
	mbm.reg('description', $('#desc_text'));
	mbm.reg('date', $('#date_input'));
	mbm.reg('fromTime', $('#timefrom_input'));
	mbm.reg('toTime', $('#timeto_input'));

	otms.lessonItemPage.modifybm = mbm;

	var vbm = new otms.validator.BeanManager();
	// vbm.reg('title', $('#title_span'));
	vbm.reg('tutorName', $('#tutor_span'));
	vbm.reg('description', $('#desc_span'));
	vbm.reg('date', $('#date_span'));
	vbm.reg('fromTime', $('#timefrom_span'));
	vbm.reg('toTime', $('#timeto_span'));
	otms.lessonItemPage.viewbm = vbm;

	// Check storage for input parameter
	var eventId = otms.getPageParam('otms.lessonItemPage.id', false);
	var lessonId = otms.getPageParam("otms.lessonItemPage.lessonId", false);

	var date = new Date(parseInt(otms.getPageParam(
			"otms.lessonItemPage.lessonDate", false)));

	if (!otms.isEmpty(eventId)) {
		// Load data from server
		var req = otms.auth.req({
			'lessonItemId' : eventId
		});
		var callback = function(success, data) {
			if (success) {
				if (data.lessonItem.status == 'SNAPSHOT') {
					vbm.setBean(data.lessonItem);
					snapshot_mode();
				} else {
					mbm.setBean(data.lessonItem);
				}
			}
		};
		LessonService.getLessonItem(req, otms.ui.MessageBox.shan(callback));
	} else if (!otms.isEmpty(lessonId)) {
		// Load lesson and create a new lesson item
		var req = otms.auth.req({
			'lessonId' : lessonId
		});
		var callback = function(success, data) {
			if (success) {
				// Create an lesson item from the lesson bean
				var lesson = data.lesson;
				var itemBean = {
					"title" : lesson.title,
					"tutorName" : lesson.tutorName,
					"description" : lesson.description,
					"date" : date,
					"fromTime" : lesson.fromTime,
					"toTime" : lesson.toTime
				};
				mbm.setBean(itemBean);
			}
		};

		LessonService.getLesson(req, otms.ui.MessageBox.shan(callback));
	} else {
		// Display warning
		otms.ui.MessageBox.warning($('#errmsg_panel'),
				'Invalid access. Will now go to front page');
		setTimeout(function() {
			window.location = 'index.html';
		}, 1000);
	}

	$('#confirm_btn').click(function(event) {
		debugger;
		var req = otms.auth.req({
			"lessonItem" : mbm.getBean(),
			"lessonId" : lessonId,
			"lessonItemId" : eventId
		});
		var callback = function(success, data) {
			if (success) {
				// Retrieve the generated item id if so
				otms.setPageParam('otms.lessonItemPage.id', data.lessonItemId);
			}
		};
		LessonService.makeLessonItem(req, otms.ui.MessageBox.han(callback));
	});

	$('#delete_btn').click(function(event) {

	});

	$('#feedback_btn').click(function(event) {

	});
};