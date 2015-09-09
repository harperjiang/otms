function loginSuccess(data) {
	localStorage.setItem("otms.token", otms.json({
		"userId" : data.userId,
		"username" : data.username,
		"type" : data.type,
		"token" : data.token
	}));
	window.location = "dashboard.html";
};

function onGoogleSignin(googleUser) {
	debugger;
	var basicProfile = googleUser.getBasicProfile();
	var regCallback = function(success, data) {
		if (success) {
			loginSuccess(data);
			window.location = 'login_add.html';
		}
	};

	var loginCallback = function(data) {
		if (data.success) {
			loginSuccess(data);
			gapi.auth2.getAuthInstance().signOut();
		} else if (data.errorCode == 107) {
			// User not linked, should create new user
			ProfileService.linkUser({
				'displayName' : basicProfile.getName(),
				'email' : basicProfile.getEmail(),
				'pictureUrl' : basicProfile.getImageUrl(),
				'type' : 'client',
				'sourceSystem' : 'Google',
				'sourceId' : googleUser.getAuthResponse().id_token,
				'linkUser' : true
			}, otms.ui.MessageBox.shan(regCallback));
		} else {
			// Error that should not happen
			otms.ui.MessageBox.error($('#errmsg_panel'), otms.ErrorMsg
					.msg(data.errorCode));
		}

	};
	AuthService.login({
		'linkLogin' : true,
		'sourceSystem' : 'Google',
		'sourceId' : googleUser.getAuthResponse().id_token
	}, loginCallback);
}

function onload() {

	otms.namespace('otms.loginPage');

	new otms.validator.NonemptyValidator($('#username'));
	new otms.validator.NonemptyValidator($('#password'));

	var bm = new otms.validator.BeanManager();
	bm.reg('username', $('#username'));
	bm.reg('password', $('#password'));

	otms.loginPage.bm = bm;

	var callback = function(success, data) {
		if (success) {
			// Put token in local storage
			loginSuccess(data);
		} else {
			// Reset Captcha
			grecaptcha.reset();
		}
	};
	var login = function() {
		var bean = bm.getBean();
		if (null != bean) {
			// gcaptcha
			var captcharesp = grecaptcha.getResponse();
			bean['captcha'] = captcharesp;
			AuthService.login(bean, otms.ui.MessageBox.han(callback));
		}
	};

	$('#signin_btn').click(login);

	$('#password').keydown(function(e) {
		if (e.keyCode == 13) {
			login();
		}
	})
};