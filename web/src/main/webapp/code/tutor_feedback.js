function formatTitle(lessonItem) {
	var message = 'Your feedback from <a onclick="client_clicked(event,{1})">{2}</a> for the lesson <span class="list_row_title">{0}</span> on {3}, {4}.';
	return otms.FormatUtil.format(message, lessonItem.title,
			lessonItem.clientId, lessonItem.clientName, otms.DateUtil
					.formattime(lessonItem.fromTime), otms.DateUtil
					.formatdate(lessonItem.date));
};

function formatDesc(feedback) {
	var completeMessage = 'The lesson is complete';
	var incompleteMessage = 'The lesson is not complete because ';
	if (feedback.success) {
		return completeMessage;
	} else {
		switch (feedback.failReason) {
		case 1:
			incompleteMessage += 'the student cannot make it';
			break;
		case 2:
			incompleteMessage += ' I cannot make it';
			break;
		case 3:
			incompleteMessage += " I didn't appear";
			break;
		default:
			break;
		}
		return incompleteMessage;
	}
};

function onload() {
	var lessonItemId = otms.getPageParam('otms.feedbackPage.itemId', false);
	if (otms.isEmpty(lessonItemId)) {
		otms.ErrorMsg.invalidAccess();
		return;
	}

	var lessonRate = new otms.ui.StarRate($('#lesson_rate'));
	lessonRate.readonly();
	var tutorRate = new otms.ui.StarRate($('#tutor_rate'));
	tutorRate.readonly();
	var tutorNoRate = new otms.ui.StarRate($('#tutor_noattend_rate'));
	tutorNoRate.readonly();

	var bm = new otms.validator.BeanManager();

	bm.reg('lessonRate', $('#lesson_rate'));
	bm.reg('tutorRate', $('#tutor_rate'));
	bm.reg('tutorNoattendRate', $('#tutor_noattend_rate'));
	bm.reg('noAttendReason', $('#select_noattend_reason'));
	bm.reg('comment', $('#comment'));

	var lessonItemId = otms.getPageParam("otms.feedbackPage.itemId", false);
	var loadFeedbackCb = function(success, data) {
		if (success) {
			var title = formatTitle(data.lessonItem);
			$('#title_panel').append(title);

			var desc = formatDesc(data.feedback);
			$('#lesson_desc_panel').append(desc);

			bm.setBean(data.feedback);
		}
	};

	FeedbackService.getFeedback(otms.auth.req({
		"lessonItemId" : lessonItemId
	}), otms.ui.MessageBox.shan(loadFeedbackCb));
};