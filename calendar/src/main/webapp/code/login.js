function onGoogleSignin(googleUser) {
	var basicProfile = googleUser.getBasicProfile();

	otms.loginPage.userinfo = {
		'displayName' : basicProfile.getName(),
		'email' : basicProfile.getEmail(),
		'pictureUrl' : basicProfile.getImageUrl(),
		'type' : 'client',
		'sourceSystem' : 'Google',
		'sourceId' : googleUser.getAuthResponse().id_token,
		'linkUser' : true,
		'verifyEmail' : false
	// Email is provided by Google
	};
	var needRegCallback = function() {
		$('#main_panel').css('display', 'none');
		$('#google_panel').css('display', 'block');
		gapi.auth2.getAuthInstance().signOut();
	};
	var successCallback = function() {
		gapi.auth2.getAuthInstance().signOut();
		window.location = 'dashboard.html';
	};

	signIn(otms.loginPage.userinfo, successCallback, needRegCallback);
};

function googleReg() {
	var bean = otms.loginPage.googleBm.getBean();
	if (bean != null) {
		var userinfo = otms.loginPage.userinfo;
		userinfo = otms.merge(userinfo, bean);

		ProfileService.linkUser(userinfo, otms.ui.MessageBox.shan(onSubmit));
	}
}

function onQQSignIn(success, qqUserInfo) {
	if (!success) {
		otms.ui.MessageBox
				.warn('Failed to obtain information from QQ. Please retry');
		return;
	}
	otms.loginPage.userinfo = qqUserInfo;
	var needRegCallback = function() {
		$('#main_panel').css('display', 'none');
		$('#qq_panel').css('display', 'block');
		QC.Login.signOut();
	};
	var successCallback = function() {
		QC.Login.signOut();
	};

	signIn(otms.loginPage.userinfo, successCallback, needRegCallback);
};

function qqReg() {
	var bean = otms.loginPage.qqBm.getBean();
	if (bean != null) {
		var userinfo = otms.loginPage.userinfo;
		userinfo = otms.merge(userinfo, bean);

		ProfileService.linkUser(userinfo, otms.ui.MessageBox.shan(onSubmit));
	}
}

function linkLoginCallback(success, data) {
	if (success) {
		loginSuccess(data);
	}
};

function signIn(userinfo, successCallback, needRegCallback) {
	var callback = function(data) {
		if (data.success) {
			loginSuccess(data);
			successCallback();
		} else if (data.errorCode == 107) {
			// User not linked, should create new user
			needRegCallback();
		} else {
			// Error that should not happen
			otms.ui.MessageBox.error($('#errmsg_panel'), otms.ErrorMsg
					.msg(data.errorCode));
		}
	};

	AuthService.login({
		'linkLogin' : true,
		'sourceSystem' : userinfo.sourceSystem,
		'sourceId' : userinfo.sourceId
	}, callback);
};

function onSubmit(success, data) {
	if (success) {
		debugger;
		if (data.needActivate) {
			$('#google_panel').css('display', 'none');
			$('#qq_panel').css('display', 'none');
			$('#activate_panel').css('display', 'block');
		} else {
			// Login again
			var userinfo = otms.loginPage.userinfo;
			AuthService.login({
				'linkLogin' : true,
				'sourceSystem' : userinfo.sourceSystem,
				'sourceId' : userinfo.sourceId
			}, otms.ui.MessageBox.shan(linkLoginCallback));
		}
	}
};

function loginSuccess(data) {
	debugger;
	otms.auth.token(data);
	window.location = "dashboard.html";
};

function onload() {

	otms.namespace('otms.loginPage');

	new otms.validator.NonemptyValidator($('#username'));
	new otms.validator.NonemptyValidator($('#password'));

	var bm = new otms.validator.BeanManager();
	bm.reg('username', $('#username'));
	bm.reg('password', $('#password'));

	otms.loginPage.bm = bm;

	otms.loadTzSelect($('#google_timezone_select'));
	new otms.validator.NonemptyValidator($('#google_username'));
	var googleBm = new otms.validator.BeanManager();
	googleBm.reg('username', $('#google_username'));
	googleBm.reg('timezone', $('#google_timezone_select'));
	otms.loginPage.googleBm = googleBm;

	otms.loadTzSelect($('#qq_timezone_select'));
	new otms.validator.NonemptyValidator($('#qq_username'));
	new otms.validator.EmailValidator($('#qq_email'));
	var qqBm = new otms.validator.BeanManager();
	qqBm.reg('username', $('#qq_username'));
	qqBm.reg('email', $('#qq_email'));
	qqBm.reg('timezone', $('#qq_timezone_select'));
	otms.loginPage.qqBm = qqBm;

	var callback = function(success, data) {
		debugger;
		if (success) {
			// Put token in local storage
			loginSuccess(data);
		} else {
			$('#signin_btn').removeAttr('disabled');
			// Reset Captcha
			// grecaptcha.reset();
		}
	};
	var login = function() {
		debugger;
		$('#signin_btn').attr('disabled', 'disabled');
		var bean = bm.getBean();
		if (null != bean) {
			// gcaptcha
			// var captcharesp = grecaptcha.getResponse();
			// bean['captcha'] = captcharesp;
			AuthService.login(bean, otms.ui.MessageBox.han(callback));
		}
	};

	$('#signin_btn').click(login);
	$('#google_submit').click(googleReg);
	$('#qq_submit').click(qqReg);

	$('#password').keydown(function(e) {
		if (e.keyCode == 13) {
			login();
		}
	});

	QC.Login({
		btnId : "qqLoginBtn", // 插入按钮的节点id
		size : 'A_L'
	});
};