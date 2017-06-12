function switch_calendar(checkbox) {
	if (checkbox.checked) {
		$('#repeat_option_div').css('display', 'block');
		$('#oneoff_option_div').css('display', 'none');
	} else {
		$('#repeat_option_div').css('display', 'none');
		$('#oneoff_option_div').css('display', 'block');
	}
};

function tutor_view() {
	$('#client_input').css('display', 'inline');
	$('#tutor_input').css('display', 'none');
}

function client_view() {
	$('#tutor_input').css('display', 'inline');
	$('#client_input').css('display', 'none');
}

function admin_view() {
	$('#tutor_input').css('display', 'inline');
	$('#client_input').css('display', 'inline');
}

function mode_modify() {
	$('#form_title_div').empty();
	$('#form_title_div').append('Modify Lesson');
	$('#delete_btn').css('display', 'inline');
	$('#tutor_input').attr('readonly', 'readonly');
	$('#tutor_input').attr('disabled', 'disabled');
}

function mode_readonly() {
	$('#title_input').attr('readonly', 'readonly');
	$('#tutor_input').attr('readonly', 'readonly');
	$('#meetingdesc_text').attr('readonly', 'readonly');
	$('#timefrom_input').attr('readonly', 'readonly');
	$('#timeto_input').attr('readonly', 'readonly');
	$('#repeat_checkbox').attr('readonly', 'readonly');

	$('#repeat_datefrom_input').attr('readonly', 'readonly');
	$('#repeat_dateto_input').attr('readonly', 'readonly');

	$('#repeat_sunday_check').attr('readonly', 'readonly');
	$('#repeat_monday_check').attr('readonly', 'readonly');
	$('#repeat_tuesday_check').attr('readonly', 'readonly');
	$('#repeat_wednesday_check').attr('readonly', 'readonly');
	$('#repeat_thursday_check').attr('readonly', 'readonly');
	$('#repeat_friday_check').attr('readonly', 'readonly');
	$('#repeat_saturday_check').attr('readonly', 'readonly');

	$('#oneoff_date_input').attr('readonly', 'readonly');
}

$(function() {

	otms.namespace('otms.lessonPage');

	$('#repeat_datefrom_input').datepicker();
	$('#repeat_dateto_input').datepicker();
	$('#oneoff_date_input').datepicker();

	// Install validator
	// new otms.validator.NonemptyValidator($('#title_input'));
	new otms.validator.NonemptyValidator($('#tutor_input'));
	new otms.validator.TimeValidator($('#timefrom_input'));
	new otms.validator.TimeValidator($('#timeto_input'));
	new otms.validator.DateValidator($('#repeat_datefrom_input'));
	new otms.validator.DateValidator($('#repeat_dateto_input'));
	new otms.validator.DateValidator($('#oneoff_date_input'));

	// Install Bean Manager
	var bm = new otms.validator.BeanManager();
	// bm.reg('title', $('#title_input'));
	bm.reg('tutorName', $('#tutor_input'));
	bm.reg('clientName', $('#client_input'));
	bm.reg('description', $('#meetingdesc_text'));
	bm.reg('fromTime', $('#timefrom_input'));
	bm.reg('toTime', $('#timeto_input'));
	bm.reg('repeat', $('#repeat_checkbox'));

	bm.reg('repeatFromDate', $('#repeat_datefrom_input'));
	bm.reg('repeatToDate', $('#repeat_dateto_input'));

	bm.reg('weekrepeat.0', $('#repeat_sunday_check'));
	bm.reg('weekrepeat.1', $('#repeat_monday_check'));
	bm.reg('weekrepeat.2', $('#repeat_tuesday_check'));
	bm.reg('weekrepeat.3', $('#repeat_wednesday_check'));
	bm.reg('weekrepeat.4', $('#repeat_thursday_check'));
	bm.reg('weekrepeat.5', $('#repeat_friday_check'));
	bm.reg('weekrepeat.6', $('#repeat_saturday_check'));

	bm.reg('oneoffDate', $('#oneoff_date_input'));

	otms.lessonPage.bm = bm;

	bm.validate = function(bean, vresult) {
		// First check vresult
		for ( var key in vresult) {
			if (vresult[key] == false) {
				if (key == 'oneoffDate' && bean.repeat) {
					continue;
				}
				if (!bean.repeat
						&& $.inArray(key, [ "repeatFromDate", "repeatToDate" ]) != -1) {
					continue;
				}
				return false;
			}
		}

		// From time must be smaller than to time
		if (bean.fromTime.total >= bean.toTime.total) {
			this.fail([ 'fromTime', 'toTime' ],
					'From time must be earlier than to time');
			return false;
		}
		var today = new Date();
		if (bean.repeat) {
			var firstRun = otms.DateUtil.form(bean.repeatFromDate,
					bean.fromTime);
			// If from date is older than today
			if (firstRun.getTime() <= today.getTime()) {
				this.fail([ 'repeatFromDate' ], 'Please specify a future time');
				return false;
			}
			// From date must be smaller than to date
			if (bean.repeatFromDate.getTime() >= bean.repeatToDate.getTime()) {
				this.fail([ 'repeatFromDate', 'repeatToDate' ],
						'From time must be earlier than to time');
				return false;
			}
			// Check at least one day when repeat

			var res = 0;
			res += bean.repeatSunday;
			res += bean.repeatMonday;
			res += bean.repeatTuesday;
			res += bean.repeatWednesday;
			res += bean.repeatThursday;
			res += bean.repeatFriday;
			res += bean.repeatSaturday;

			if (res == 0) {
				otms.ui.MessageBox.warning($('#errmsg_panel'),
						'Choose at least one day to repeat');
				return false;
			}
		} else {
			var firstRun = otms.DateUtil.form(bean.oneoffDate, bean.fromTime);
			if (firstRun <= today.getTime()) {
				this.fail([ 'oneoffDate' ], 'Please specify a future time');
				return false;
			}
		}

		return true;
	};

	// Check storage for input parameter
	var lessonId = otms.getPageParam('otms.lessonPage.id');
	if (lessonId != undefined) {
		mode_modify();
		// Load data from server
		var callback = function(success, data) {
			if (success) {
				bm.setBean(data.lesson);
				// Manually trigger calendar flip
				switch_calendar($('#repeat_checkbox').get()[0]);
				if (data.lesson.status == 'INVALID') {
					mode_readonly();
				}
			}
		};
		LessonService.getLesson(otms.auth.req({
			"lessonId" : lessonId
		}), otms.ui.MessageBox.shan(callback));
	} else {
		// If tutor name is from other pages
		var tutorName = otms.getPageParam('otms.lessonPage.tutorName');
		if (tutorName != undefined) {
			// Preset tutorName, not modifiable
			$('#tutor_input').val(tutorName);
			$('#tutor_input').attr('readonly', 'readonly');
		}
	}

	switch (otms.auth.userType()) {
	case 'admin':
		admin_view();
		break;
	case 'tutor':
		tutor_view();
		break;
	case 'client':
		client_view();
		break;
	default:
		break;
	}

	var confirmCallback = function(success) {
		if (success) {
			// Display a confirmation page
			$('#main_panel').css('display', 'none');
			$('#confirm_panel').css('display', 'block');
		} else {
			$('#confirm_btn').removeAttr('disabled');
		}
	};
	$('#confirm_btn').click(
			function(event) {
				var bean = bm.getBean();
				// Call Server
				if (bean != null) {
					$(this).attr('disabled', 'disabled');

					switch (otms.auth.userType()) {
					case 'admin':
						AdminService.scheduleLesson(otms.auth.req({
							"lesson" : bean
						}), otms.ui.MessageBox.han(confirmCallback));
						break;
					default:
						LessonService.setupLesson(otms.auth.req({
							"lesson" : bean
						}), otms.ui.MessageBox.han(confirmCallback));
						break;
					}
				} else {
					otms.ui.MessageBox.warning($('#errmsg_panel'),
							'Validation Failed');
				}
			});

	var deleteCallback = function(success) {
		if (success) {
			// Display a confirmation page
			$('#main_panel').css('display', 'none');
			$('#cancel_panel').css('display', 'block');
		} else {
			$('#delete_btn').removeAttr('disabled');
		}
	};

	$('#delete_btn').click(
			function(event) {
				var req = otms.auth.req({
					"lessonId" : lessonId,
					"toStatus" : 'INVALID'
				});
				LessonService.changeLessonStatus(req, otms.ui.MessageBox
						.shan(deleteCallback));
			});

	$('#req_lesson_btn').click(function(event) {
		window.location = 'lesson_requested.html';
	});
	$('#calendar_btn').click(function(event) {
		window.location = 'calendar.html';
	});
});