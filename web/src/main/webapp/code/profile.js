otms.namespace('otms.profilePage');

otms.profilePage.UploadPictureDialog = otms.extend(otms.ui.dialog.Dialog,
		function(container) {
			otms.ui.dialog.Dialog.call(this, container);
		}, {
			drawTitle : function(panel) {
				panel.append('Upload Profile Picture');
			},
			drawContent : function(panel) {
				// Construct a form for data submission
				var form = $(document.createElement('form'));
				form.attr('id', 'profile_image_form');
				form.attr('name', 'profile_image_form');
				form.attr('method', 'POST');
				form.attr('action', 'uploadfile');
				panel.append(form);

				var input = $(document.createElement('input'));
				input.attr('type', 'file');
				input.attr('name', 'image');
				form.append(input);

				var serviceName = $(document.createElement('input'));
				serviceName.attr('type', 'hidden');
				serviceName.attr('name', 'serviceName');
				serviceName.attr('value', 'profileService');
				form.append(serviceName);

				var functionName = $(document.createElement('input'));
				functionName.attr('type', 'hidden');
				functionName.attr('name', 'functionName');
				functionName.attr('value', 'uploadProfileImage');
				form.append(functionName);
			}
		});

$(function() {
	otms.namespace('otms.profilePage');
	otms.loadTzSelect($('#timezone_select'));

	new otms.validator.NonemptyValidator($('#name_input'));
	new otms.validator.EmailValidator($('#email_input'));

	var commonbm = new otms.validator.BeanManager();
	commonbm.reg('displayName', $('#name_input'));
	commonbm.reg('username', $('#username_span'));
	commonbm.reg('timezone', $('#timezone_select'));
	commonbm.reg('email', $('#email_input'));
	commonbm.reg('pictureUrl', $('#profile_img'));
	commonbm.reg('imType',$('#imtype_select'));
	commonbm.reg('im',$('#im_input'));
	commonbm.reg('phone',$('#phone_input'));
//	commonbm.reg('emailClass',$('#email_class_check'));
//	commonbm.reg('emailBooking',$('#email_booking_check'));
//	commonbm.reg('emailSchedule',$('#email_schedule_check'));
	otms.profilePage.commonbm = commonbm;

	var clientbm = new otms.validator.BeanManager();
	clientbm.reg('statement', $('#client_intro_text'));
	otms.profilePage.clientbm = clientbm;

	var tutorbm = new otms.validator.BeanManager();
	tutorbm.reg('statement', $('#tutor_stmt_input'));
	tutorbm.reg('description', $('#tutor_desc_text'));
	tutorbm.reg('videoIntroUrl', $('#tutor_video_input'));
	tutorbm.reg('eduInfo', $('#tutor_edu_text'));
	tutorbm.reg('workingInfo', $('#tutor_working_text'));
	otms.profilePage.tutorbm = tutorbm;

	$('#client_confirm_btn').click(function() {
		var pbean = otms.profilePage.preserveBean;
		var commonbean = commonbm.getBean();
		var clientbean = clientbm.getBean();
		if (commonbean != null && clientbean != null) {
			var bean = otms.merge(pbean, commonbean, clientbean);
			ProfileService.setupClient(otms.auth.req({
				"clientInfo" : bean
			}), otms.ui.MessageBox.han());
		}
	});

	$('#tutor_confirm_btn').click(function() {
		var pbean = otms.profilePage.preserveBean;
		var commonbean = commonbm.getBean();
		var tutorbean = tutorbm.getBean();
		if (commonbean != null && tutorbean != null) {
			var bean = otms.merge(pbean, commonbean, tutorbean);
			
			TutorService.setupTutor(otms.auth.req({
				"tutorInfo" : bean
			}), otms.ui.MessageBox.han());
		}
	});

	var userType = otms.auth.userType();

	switch (userType) {
	case undefined:
		otms.ui.MessageBox.warning($('#errmsg_panel'), 'You are not logged in');
		break;
	case 'client':
		$('#client_profile_panel').css('display', 'block');
		var req = otms.auth.req({
			"clientId" : otms.auth.currentUser()
		});
		var callback = function(success, data) {
			if (success) {
				var clientInfo = data.clientInfo;
				// Noneditable
				otms.profilePage.commonbm.setBean(clientInfo);
				otms.profilePage.clientbm.setBean(clientInfo);
				otms.profilePage.preserveBean = clientInfo;
			}
		};
		ProfileService.getClientInfo(req, otms.ui.MessageBox.shan(callback));
		break;
	case 'tutor':
		$('#tutor_profile_panel').css('display', 'block');
		var req = otms.auth.req({
			"tutorId" : otms.auth.currentUser()
		});
		var callback = function(success, data) {
			if (success) {
				var tutorInfo = data.tutorInfo;
				// Noneditable
				
				otms.profilePage.commonbm.setBean(tutorInfo);
				otms.profilePage.tutorbm.setBean(tutorInfo);
				otms.profilePage.preserveBean = tutorInfo;
			}
		};
		TutorService.getTutorInfo(req, otms.ui.MessageBox.shan(callback));
		break;
	}

	$('#btn_upload_image').click(
			function(event) {
				var dialog = new otms.profilePage.UploadPictureDialog(
						$('#page_content'));
				dialog.onConfirm = function() {
					var callback = function(success, data) {
						if (success) {
							$('#profile_img').attr('src', data.imageUrl);
						}
					};
					var form = $('#profile_image_form');

					otms.submitform(form,
							otms.ui.MessageBox.shan(callback).callback);
				};
				dialog.show();
			});
});