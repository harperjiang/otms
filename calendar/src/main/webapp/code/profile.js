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
				serviceName.attr('type','hidden');
				serviceName.attr('name','serviceName');
				serviceName.attr('value','profileService');
				form.append(serviceName);
				
				var functionName = $(document.createElement('input'));
				functionName.attr('type','hidden');
				functionName.attr('name','functionName');
				functionName.attr('value','uploadProfileImage');
				form.append(functionName);
			}
		});