$(function() {
	// Load timezone data
	otms.loadTzSelect($('#timezone_select'));

	// Setup Validator
	new otms.validator.NonemptyValidator($('#name_input'));
	new otms.validator.NonemptyValidator($('#display_input'));
	new otms.validator.EmailValidator($('#email_input'));
	new otms.validator.PasswordValidator($('#password_input'));

	window.beanManager = new otms.validator.BeanManager();
	var bm = window.beanManager;
	bm.reg('username', $('#name_input'));
	bm.reg('displayName', $('#display_input'));
	bm.reg('email', $('#email_input'));
	bm.reg('password', $('#password_input'));
	bm.reg('timezone', $('#timezone_select'));
	bm.reg('type', $('input[name="role"]'));
	bm.reg('agree', $('#agree_checkbox'));
	bm.validate = function(bean, vresult) {
		if (!bean.agree) {
			this.fail([ 'agree' ], 'Please accept the agreement');
			otms.ui.MessageBox.warning($('#errmsg_panel'),
					'Please accept the agreement');
			return false;
		}
		return otms.validator.BeanManager.prototype.validate.call(this, bean,
				vresult);
	};

	var callback = function(success) {
		if (success) {
			// Switch to confirm page
			$('#signup_info_panel').css('display', 'none');
			$('#email_span').append($('#email_input').val());
			$('#signup_confirm_panel').css('display', "block");
		} else {
			$('#signup_btn').removeAttr('disabled');
			grecaptcha.reset();
		}
	};

	$('#signup_btn').click(
			function(event) {
				$(this).attr('disabled', 'true');
				var bean = window.beanManager.getBean();
				if (bean != null) {
					// gcaptcha
					var captcharesp = grecaptcha.getResponse();
					bean['captcha'] = captcharesp;
					ProfileService.registerUser(otms.auth.req(bean),
							otms.ui.MessageBox.shan(callback));
				} else {
					$(this).removeAttr('disabled');
				}
			});
	$('#switch_btn').click(function(event) {
		$('#signup_info_panel').css('display', 'none');
		$('#email_span').append($('#email_input').val());
		$('#signup_confirm_panel').css('display', "block");
	});
});