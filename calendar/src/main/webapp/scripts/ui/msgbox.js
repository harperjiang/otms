otms.namespace('otms.ui.MessageBox');

otms.ui.MessageBox.error = function(component, message) {
	otms.ui.MessageBox.showMessageWithClass(component, message, 'msgbox_error');
};

otms.ui.MessageBox.info = function(component, message) {
	otms.ui.MessageBox.showMessageWithClass(component, message, 'msgbox_info');
};

otms.ui.MessageBox.success = function(component, message) {
	otms.ui.MessageBox.showMessageWithClass(component, message,
			'msgbox_success');
};

otms.ui.MessageBox.warning = function(component, message) {
	otms.ui.MessageBox.showMessageWithClass(component, message,
			'msgbox_warning');
};

otms.ui.MessageBox.showMessageWithClass = function(component, message, style) {
	var sessionid = otms.uuid();
	component.prop('dialogSessionId', sessionid);

	component.empty();
	component.removeClass();
	component.addClass(style);

	var icondiv = $(document.createElement('div'));
	icondiv.attr('id', 'msgbox_icon');
	icondiv.addClass('msgbox_icon');
	component.append(icondiv);

	var msgdiv = $(document.createElement('div'));
	msgdiv.attr('id', 'msgbox_msg');
	msgdiv.addClass('msgbox_msg');
	msgdiv.append(message);
	component.append(msgdiv);

	component.click(function(event) {
		component.animate({
			'opacity' : '0',
			'height' : '0'
		}, 300, function() {
			component.removeClass();
			component.attr('style', '');
			component.addClass('msgbox_hidden');
			component.removeProp('dialogSessionId');
		});
	});
	/*
	 * // Hide the dialog after 5 sec if session id doesn't change
	 * setTimeout(function() { if (sessionid !=
	 * component.prop('dialogSessionId')) { return; } component.animate({
	 * 'opacity' : '0', 'height' : '0' }, 300, function() {
	 * component.removeClass(); component.attr('style', '');
	 * component.addClass('msgbox_hidden'); }); }, 3000);
	 */
};

// The slient default handler
otms.ui.MessageBox.shan = function(callback) {
	return otms.ui.MessageBox.errhandler($('#errmsg_panel'), callback);
};

otms.ui.MessageBox.han = function(callback) {
	return otms.ui.MessageBox.handler($('#errmsg_panel'), callback);
};

otms.ui.MessageBox.handler = function(container, mycallback) {
	return {
		callback : function(response) {
			console.log(response);
			if (response.success) {
				otms.ui.MessageBox.success(container, 'Operation Succeed');
				if (mycallback != undefined) {
					mycallback(true, response);
				}
			} else {
				var errorMsg = response.errorMessage;
				if (response.errorCode != undefined) {
					var lang = localStorage.getItem('otms.lang');
					errorMsg = otms.ErrorMsg.msg(response.errorCode, lang);
				}
				if (errorMsg === undefined) {
					errorMsg = 'Server Error';
				}
				otms.ui.MessageBox.error(container, errorMsg);
				if (mycallback != undefined) {
					mycallback(false);
				}
			}

		},
		errorHandler : function(errorString, exception) {
			try {
				console.log(errorString);
				console.log(exception);
				otms.ui.MessageBox.error(container,
						otms.isEmpty(errorString) ? 'Server Error'
								: errorString);
			} finally {
				if (mycallback != undefined) {
					mycallback(false);
				}
			}
		}
	};
};

otms.ui.MessageBox.errhandler = function(container, mycallback) {
	return {
		callback : function(response) {
			console.log(response);
			if (response.success) {
				if (mycallback != undefined) {
					mycallback(true, response);
				}
			} else {
				var errorMsg = response.errorMessage;
				if (response.errorCode != undefined) {
					errorMsg = otms.ErrorMsg.msg(response.errorCode);
				}
				if (errorMsg === undefined) {
					errorMsg = 'Server Error';
				}
				otms.ui.MessageBox.error(container, errorMsg);
				if (mycallback != undefined) {
					mycallback(false);
				}
			}

		},
		errorHandler : function(errorString, exception) {
			try {
				console.log(errorString);
				console.log(exception);
				otms.ui.MessageBox.error(container,
						otms.isEmpty(errorString) ? 'Server Error'
								: errorString);
			} finally {
				if (mycallback != undefined) {
					mycallback(false);
				}
			}
		}
	};
};