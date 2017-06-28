function new_mode() {
	$('#confirm_btn').css('display', 'block');
	$('#delete_btn').css('display', 'none');
}

function modify_mode() {
	$('#confirm_btn').css('display', 'block');
	$('#delete_btn').css('display', 'block');
	$('#feedback_btn').css('display', 'none');
}

function snapshot_mode(fbstatus) {
	$('#client_input').attr('readonly', 'readonly');
	$('#tutor_input').attr('readonly', 'readonly');
	$('#desc_text').attr('readonly', 'readonly');
	$('#timefrom_input').attr('readonly', 'readonly');
	$('#timeto_input').attr('readonly', 'readonly');
	$('#date_input').attr('readonly', 'readonly');
	$('#confirm_btn').css('display', 'none');
	$('#delete_btn').css('display', 'none');
	if (otms.auth.userType() == 'client' && fbstatus == 'NO_FEEDBACK') {
		$('#feedback_panel').css('display', 'block');
	} else {
		$('#feedback_panel').css('display', 'none');
	}
};

function switch_viewer() {
	switch (otms.auth.userType()) {
	case 'client':
		$('#client_ig').css('display', 'none');
		break;
	case 'tutor':
		$('#tutor_ig').css('display', 'none');
		break;
	case 'admin':
		break;
	}
}

function feedback_reset() {
	$('input:radio[name=attend]').filter('[value=yes]').prop('checked', true);
	$('#attend_panel').css('display', 'block');
	$('#noattend_panel').css('display', 'none');
	lessonRate.reset();
	tutorRate.reset();
	tutorNoRate.reset();
	$('#select_noattend_reason').val(1);
	$('#comment').val('');
};

function init_feedback_panel() {
	var lessonItemId = otms.getPageParam("otms.lessonItemPage.id", false);

	var lessonRate = new otms.ui.StarRate($('#lesson_rate'));
	var tutorRate = new otms.ui.StarRate($('#tutor_rate'));
	var tutorNoRate = new otms.ui.StarRate($('#tutor_noattend_rate'));

	$('input[type=radio][name=attend]').change(function() {
		if (this.value == 'yes') {
			$('#fb_attend_panel').css('display', 'block');
			$('#fb_noattend_panel').css('display', 'none');
		} else if (this.value == 'no') {
			$('#fb_attend_panel').css('display', 'none');
			$('#fb_noattend_panel').css('display', 'block');
		}
	});

	var bm = new otms.validator.BeanManager();

	bm.reg('lessonSuccess', $('input[type=radio][name=attend][value=yes]'));
	bm.reg('lessonRate', $('#lesson_rate'));
	bm.reg('tutorRate', $('#tutor_rate'));
	bm.reg('tutorNoattendRate', $('#tutor_noattend_rate'));
	bm.reg('noAttendReason', $('#select_noattend_reason'));
	bm.reg('comment', $('#comment'));

	var callback = function(success, data) {
		debugger;
		$('#btn_fb_submit').prop('disabled', false);
		if (success) {
			$('#feedback_panel').css('display', 'none');
			$('#feedback_confirm_panel').css('display', 'block');
		}
	};

	$('#btn_fb_submit').click(
			function() {
				$(this).prop('disabled', 'disabled');
				var bean = bm.getBean();
				if (bean != null) {
					bean.lessonSuccess = (bean.lessonSuccess == 'yes');
					bean.lessonItemId = lessonItemId;
					FeedbackService.clientFeedback(otms.auth.req(bean),
							otms.ui.MessageBox.han(callback));
				}
			});
	$('#btn_fb_reset').click(feedback_reset);

	$('#btn_dashboard').click(function() {
		window.location = 'dashboard.html';
	})

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

	switch_viewer();

	// Init feedback panel
	init_feedback_panel();

	// Check storage for input parameter
	var eventId = otms.getPageParam('otms.lessonItemPage.id', false);
	var lessonId = otms.getPageParam("otms.lessonItemPage.lessonId", false);
	var date = new Date(parseInt(otms
			.getPageParam("otms.lessonItemPage.lessonDate")));

	if (!otms.isEmpty(eventId)) {
		modify_mode();
		// Load data from server
		var req = otms.auth.req({
			'lessonItemId' : eventId
		});
		var callback = function(success, data) {
			if (success) {
				if (data.lessonItem.status == 'SNAPSHOT') {
					snapshot_mode(data.lessonItem.feedbackStatus);
				}
				mbm.setBean(data.lessonItem);
			}
		};
		LessonService.getLessonItem(req, otms.ui.MessageBox.shan(callback));
	} else if (!otms.isEmpty(lessonId)) {
		new_mode();
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
		// Cancel Lesson Item
		var req = otms.auth.req({
			"lessonItemId" : eventId
		});
		var callback = function(success, data) {
			if (success) {
				$('#modify_panel').css('display', 'none');
				$('#confirm_panel').css('display', 'block');
			}
		};
		LessonService.cancelLessonItem(req, otms.ui.MessageBox.shan(callback));
	});

	$('#calendar_btn').click(function(event) {
		window.location = 'calendar.html';
	});
});