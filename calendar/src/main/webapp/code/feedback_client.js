function formatTitle(lessonItem) {
	var message = 'Please leave your feedback to the lesson with <a onclick="tutor_clicked(event,{1})">{2}</a> on {3}, {4}.';
	return otms.FormatUtil.format(message, lessonItem.title,
			lessonItem.tutorId, lessonItem.tutorName, otms.DateUtil
					.formattime(lessonItem.fromTime), otms.DateUtil
					.formatdate(lessonItem.date));
};

function reset() {
	$('input:radio[name=attend]').filter('[value=yes]').prop('checked', true);
	$('#attend_panel').css('display', 'block');
	$('#noattend_panel').css('display', 'none');
	lessonRate.reset();
	tutorRate.reset();
	tutorNoRate.reset();
	$('#select_noattend_reason').val(1);
	$('#comment').val('');
};

function onload() {
	var lessonItemId = otms.getPageParam('otms.feedbackPage.itemId', false);
	if (otms.isEmpty(lessonItemId)) {
		otms.ErrorMsg.invalidAccess();
		return;
	}

	var lessonRate = new otms.ui.StarRate($('#lesson_rate'));
	var tutorRate = new otms.ui.StarRate($('#tutor_rate'));
	var tutorNoRate = new otms.ui.StarRate($('#tutor_noattend_rate'));

	$('input[type=radio][name=attend]').change(function() {
		if (this.value == 'yes') {
			$('#attend_panel').css('display', 'block');
			$('#noattend_panel').css('display', 'none');
		} else if (this.value == 'no') {
			$('#attend_panel').css('display', 'none');
			$('#noattend_panel').css('display', 'block');
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
		if (success) {
			$('#operation_panel').css('display', 'none');
			$('#complete_panel').css('display', 'block');
			otms.clearPageParam('otms.feedbackPage.itemId');
		} else {
			$('#btn_submit').removeProp('disabled');
		}
	};

	$('#btn_submit').click(
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
	$('#btn_reset').click(reset);

	$('#btn_dashboard').click(function() {
		window.location = 'dashboard.html';
	})

	var lessonItemId = otms.getPageParam("otms.feedbackPage.itemId", false);
	var loadLessonCb = function(success, data) {
		if (success) {
			var title = formatTitle(data.lessonItem);
			$('#title_panel').append(title);
		}
	};

	LessonService.getLessonItem(otms.auth.req({
		"lessonItemId" : lessonItemId
	}), otms.ui.MessageBox.shan(loadLessonCb));
};

$(onload);