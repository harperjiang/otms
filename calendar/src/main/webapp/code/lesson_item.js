function snapshot_mode() {
	$('#view_panel').css('display', 'block');
	$('#modify_panel').css('display', 'none');
};

function switch_mode() {
	switch (otms.auth.userType()) {
	case 'client':
		$('#client_input').css('display', 'none');
		$('#client_row').css('display', 'none');
		break;
	case 'tutor':
		$('#tutor_input').css('display', 'none');
		$('#tutor_row').css('display', 'none');
		break;
	case 'admin':
		break;
	}
}

$(function() {
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
	mbm.reg('clientName', $('#client_input'));
	mbm.reg('description', $('#desc_text'));
	mbm.reg('date', $('#date_input'));
	mbm.reg('fromTime', $('#timefrom_input'));
	mbm.reg('toTime', $('#timeto_input'));

	otms.lessonItemPage.modifybm = mbm;

	var vbm = new otms.validator.BeanManager();
	// vbm.reg('title', $('#title_span'));
	vbm.reg('tutorName', $('#tutor_span'));
	vbm.reg('clientName', $('#client_span'));
	vbm.reg('description', $('#desc_span'));
	vbm.reg('date', $('#date_span'));
	vbm.reg('fromTime', $('#timefrom_span'));
	vbm.reg('toTime', $('#timeto_span'));
	otms.lessonItemPage.viewbm = vbm;

	switch_mode();
	// Check storage for input parameter
	var eventId = otms.getPageParam('otms.lessonItemPage.id');
	var lessonId = otms.getPageParam("otms.lessonItemPage.lessonId");
	var date = new Date(parseInt(otms
			.getPageParam("otms.lessonItemPage.lessonDate")));

	if (!otms.isEmpty(eventId)) {
		// Load data from server
		var req = otms.auth.req({
			'lessonItemId' : eventId
		});
		var callback = function(success, data) {
			if (success) {
				if (data.lessonItem.status == 'SNAPSHOT') {
					snapshot_mode();
					vbm.setBean(data.lessonItem);
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
				// Create an lesson item from the lesson
				var lesson = data.lesson;
				var itemBean = {
					"title" : lesson.title,
					"tutorName" : lesson.tutorName,
					"clientName" : lesson.clientName,
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

	$('#confirm_btn').click(
			function(event) {
				debugger;
				var bean = mbm.getBean();
				if (otms.isEmpty(bean)) {
					otms.ui.MessageBox.warning($('#errmsg_panel'),
							'Validation Failed');
					return;
				}
				var req = otms.auth.req({
					"lessonItem" : mbm.getBean(),
					"lessonId" : lessonId,
					"lessonItemId" : otms.isEmpty(eventId) ? -1 : eventId
				});
				var callback = function(success, data) {
					if (success) {
						// Retrieve the generated item id if so
						otms.setPageParam('otms.lessonItemPage.id',
								data.lessonItemId);
						$('#modify_panel').css('display', 'none');
						$('#confirm_panel').css('display', 'block');
					}
				};
				LessonService.makeLessonItem(req, otms.ui.MessageBox
						.han(callback));
			});

	$('#delete_btn').click(function(event) {
		// TODO Cancel Event Item
	});

	$('#feedback_btn').click(function(event) {
		// TODO Leave feedback
	});

	$('#calendar_btn').click(function(event) {
		window.location = 'calendar.html';
	});
});