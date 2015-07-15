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
	component.empty();
	component.removeClass();
	component.addClass(style);

	var icondiv = $(document.createElement('div'));
	icondiv.attr('id', 'msgbox_icon');
	icondiv.addClass('msgbox_icon');
	component.append(icondiv);

	component.append(message);

	component.click(function(event) {
		component.animate({
			'height' : '0px'
		}, 300, function() {
			component.removeClass();
			component.attr('style', '');
			component.addClass('msgbox_hidden');
		});
	});
	// Hide the dialog after 5 sec
	setTimeout(function() {
		component.animate({
			'height' : '0px'
		}, 300, function() {
			component.removeClass();
			component.attr('style', '');
			component.addClass('msgbox_hidden');
		});
	}, 5000);
};

otms.ui.MessageBox.handler = function(container) {
	return {
		callback : function(data) {
			debugger;
			console.log(response);
			otms.ui.MessageBox.success(container, 'Operation Succeed');
		},
		errorHandler : function(errorString, exception) {
			otms.ui.MessageBox.error(container,
					otms.isEmpty(errorString) ? 'Server Error' : errorString);
		}
	};
};