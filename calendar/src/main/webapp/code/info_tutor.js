$(function() {
	var tutorId = otms.getPageParam("otms.tutorInfoPage.tutorId", false);
	if (otms.isEmpty(tutorId)) {
		// Display invalid access
		otms.ui.MessageBox.warning($('#errmsg_panel'), 'Invalid Access');
		setTimeout(function() {
			window.location = 'index.html';
		}, 2000);
	}

	otms.namespace('otms.tutorInfoPage');

	// Initialize Tab
	otms.tutorInfoPage.tabControl = new otms.ui.tab.TabControl([ $('#tab1'),
			$('#tab2'), $('#tab3') ], [ $('#timesheet_panel'),
			$('#self_intro_panel'), $('#user_rate_panel') ]);
	// Create Timesheet
	otms.tutorInfoPage.timesheet = new otms.ui.timesheet.Timesheet(
			$('#timesheet_container'));

	var vbm = new otms.validator.BeanManager();
	vbm.reg('displayName', $('#name_span'));
	vbm.reg('username', $('#username_span'));
	vbm.reg('timezone', $('#timezone_span'));
	vbm.reg('pictureUrl', $('#profile_img'));
	vbm.reg('statement', $('#stmt_span'));
	vbm.reg('description', $('#desc_span'));
	vbm.reg('aboutMeInfo', $('#aboutme_span'));
	vbm.reg('eduInfo', $('#edu_span'));
	vbm.reg('workingInfo', $('#working_span'));
	// vbm.reg('audioText', $('#eel_span'));
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
	ProfileService.getTutorInfo(req, otms.ui.MessageBox.errhandler(
			$('#errmsg_panel'), callback));
});