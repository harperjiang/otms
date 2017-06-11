function refreshList() {
	var callback = function(success, data) {
		if (success) {
			// Update data to page
			otms.reqLessonPage.list.model.setData(data.lessons);
		}
	};
	LessonService.getRequestedLessons(otms.auth.req({}), otms.ui.MessageBox
			.shan(callback));
};

function popupRenderer(popupDiv) {
	var confirmBtn = $(document.createElement('button'));
	confirmBtn.addClass('btn_small');
	confirmBtn.attr('id', 'confirm_btn');
	confirmBtn.css('position', 'relative');
	confirmBtn.css('left', '10px');
	confirmBtn.css('top', '20px');
	confirmBtn.append("Accept");
	popupDiv.append(confirmBtn);

	var rejectBtn = $(document.createElement('button'));
	rejectBtn.addClass('btn_small_danger');
	rejectBtn.attr('id', 'reject_btn');
	rejectBtn.css('position', 'relative');
	rejectBtn.css('left', '20px');
	rejectBtn.css('top', '20px');
	rejectBtn.append("Reject");
	popupDiv.append(rejectBtn);

	popupDiv.prop('width', 160);
	// Only display Cancel button for client
	if (otms.auth.userType() == 'client') {
		confirmBtn.css('display', 'none');
		rejectBtn.empty();
		rejectBtn.append("Cancel");
		popupDiv.prop('width', 90);
	}

	var callback = function(success, data) {
		if (success) {
			refreshList();
		}
	};

	$(document).on(
			'click',
			'#confirm_btn',
			function() {
				var list = otms.reqLessonPage.list;
				var item = list.currentItem;
				list.hidePopup();
				var req = otms.auth.req({
					"lessonId" : item.lessonId,
					"toStatus" : 'VALID'
				});
				LessonService.changeLessonStatus(req, otms.ui.MessageBox
						.han(callback));
			});

	$(document).on(
			'click',
			'#reject_btn',
			function() {
				var list = otms.reqLessonPage.list;
				var item = list.currentItem;
				list.hidePopup();
				var req = otms.auth.req({
					"lessonId" : item.lessonId,
					"toStatus" : 'INVALID'
				});
				LessonService.changeLessonStatus(req, otms.ui.MessageBox
						.han(callback));
			});
	// Layout
	popupDiv.layout = function(height) {
		confirmBtn.css('top', height / 2 - 12);
		rejectBtn.css('top', height / 2 - 12);
	};
};