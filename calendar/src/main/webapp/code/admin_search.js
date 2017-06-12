$(function() {

	$('#datefrom_input').datepicker();
	$('#dateto_input').datepicker();

	new otms.validator.NonemptyValidator($('#tutor_input'));
	new otms.validator.NonemptyValidator($('#client_input'));
	new otms.validator.DateValidator($('#datefrom_input'));
	new otms.validator.DateValidator($('#dateto_input'));

	var beanManager = new otms.validator.BeanManager();
	beanManager.reg('tutorName', $('#tutor_input'));
	beanManager.reg('clientName', $('#client_input'));
	beanManager.reg('fromDate', $('#datefrom_input'));
	beanManager.reg('toDate', $('#dateto_input'));

	var eventList = new otms.ui.list.List($('#event_list_container'));
	eventList.setRenderer(lessonEventListRenderer());
	eventList.rowClicked = function(event, itemdata) {
		sessionStorage.setItem("otms.adminLessonPage.id", itemdata.id);
		window.location = 'admin_lesson.html';
	};

	var callback = function(success, data) {
		$('#btn_search').removeAttr('disabled');
		if (success) {
			eventList.model.setData(data.events);
		}
	}

	$('#btn_search').click(
			function(event) {
				var bean = beanManager.getBean();
				if (!otms.isEmpty(bean)) {
					var req = otms.auth.req(beanManager.getBean());
					$('#btn_search').attr('disabled', 'disabled');
					AdminService.searchLessons(req, otms.ui.MessageBox
							.shan(callback));
				} else {
					otms.ui.MessageBox.warning($('#errmsg_panel'),
							'Validation Failed');
				}
			});

});