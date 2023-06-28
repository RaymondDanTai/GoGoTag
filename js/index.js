/* 判斷使用者是否為登入，如未登入則需顯示登入UI和註冊UI的呼叫按鈕*/
CheckLoginStatus();
function CheckLoginStatus() {
	var userId = localStorage.getItem("userId");
	var userSession = localStorage.getItem("userSession");
	if (userId !== null && userSession !== null) {
		const formData = new FormData();
		formData.append("userId", userId);
		formData.append("userSession", userSession);
		var xhr;
		if (window.ActiveXObject) {
			xhr = new ActiveXObject("Microsoft.XMLHTTP");
		} else if (window.XMLHttpRequest) {
			xhr = new XMLHttpRequest();
		}
		xhr.open("POST", "/SocialMedia/CheckLoginStatus", true)
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4) {
				if (xhr.status == 200) {
					var jsonObj = JSON.parse(xhr.responseText);
					var isLoggedIn = jsonObj.isLoggedIn;
					if (isLoggedIn) {
						var userAvatarURL = jsonObj.userAvatarSource;
						var userName = jsonObj.firstName;
						var setLoginControlArea = document.getElementById("login-control-area");
						var setAvatarButton = document.createElement("div");
						setAvatarButton.setAttribute("class", "avatar_button d-flex align-items-center justify-content-center");
						setAvatarButton.setAttribute("onclick", "toAvatarEditro()");
						var setUserIdArea = document.createElement("div");
						setUserIdArea.setAttribute("class", "userId_area");
						var createUserAvatar = document.createElement("img");
						createUserAvatar.classList.add("avatar_img");
						createUserAvatar.src = userAvatarURL;
						var setUserAvatar = document.createElement("div");
						setUserAvatar.setAttribute("class", "avatar_button");
						setUserAvatar.append(createUserAvatar);
						var setUserName = document.createElement("div");
						setUserName.setAttribute("class", "userId_area");
						setUserName.append(userName);
						var element = document.getElementById("login-button");
						if (element) {
							while (element.firstChild) {
								element.firstChild.remove();
							}
							element.remove();
						}

						element = document.getElementById("register-button");
						if (element) {
							while (element.firstChild) {
								element.firstChild.remove();
							}
							element.remove();
						}

						setLoginControlArea.append(setUserAvatar);
						setLoginControlArea.append(setUserName);

						toHomePage();
					}
				}
			}
		}
		xhr.send(formData);
	}
}


/* 取得header的高度 */
const header = document.querySelector('header');
const main = document.querySelector('main');
main.style.setProperty('--header-height', header.offsetHeight + 'px');

/* Open or Close Form */
const closeForm1 = document.querySelector('.closeicon_div1');
const loginForm = document.querySelector('.login_form');
const loginButton = document.querySelector('.login_button');
const registerForm = document.querySelector('.register_form')
const registerButton = document.querySelector('.register_button');
const closeForm2 = document.querySelector('.closeicon_div2');

loginButton.addEventListener('click', () => {
	loginForm.classList.add('open_loginform');
	registerForm.classList.remove('open_registerform');
})

registerButton.addEventListener('click', () => {
	registerForm.classList.add('open_registerform');
	loginForm.classList.remove('open_loginform');

})

closeForm1.addEventListener('click', () => {
	loginForm.classList.remove('open_loginform');
})

closeForm2.addEventListener('click', () => {
	registerForm.classList.remove('open_registerform');
})

// /* check */
var firstName = document.getElementById("first-name");
firstName.addEventListener("input", function() {
	const firstNameValue = this.value;
	const firstNameRegex = /^[^\d\s\p{P}]+$/;
//	const firstNameRegex = /^[\u4e00-\u9fa5a-zA-Z]+$/;
	const firstNameIsValid = firstNameRegex.test(firstNameValue);

	if (firstNameIsValid) {
		this.setCustomValidity("");
	} else {
		this.setCustomValidity("Please input valid First Name");
	}
	this.reportValidity();
});
var lastName = document.getElementById("last-name");
lastName.addEventListener("input", function() {
	const lastNameValue = this.value;
	const lastNameRegex = /^[^\d\s\p{P}]+$/;
	//	const lastNameRegex = /^[\u4e00-\u9fa5a-zA-Z]+$/;
	const lastNameIsValid = lastNameRegex.test(lastNameValue);

	if (lastNameIsValid) {
		this.setCustomValidity("");
	} else {
		this.setCustomValidity("Please input valid Last Name");
	}
	this.reportValidity();
});

var email = document.getElementById("email");
email.addEventListener("input", function() {
	const emailValue = this.value;
	const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
	const emailIsValid = emailRegex.test(emailValue);

	if (emailIsValid) {
		this.setCustomValidity("");
	} else {
		this.setCustomValidity("Please input valid Email");
	}
	this.reportValidity();
});
var password = document.getElementById("password");
password.addEventListener("input", function() {
	const passwordValue = this.value;
	const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d).{8,}$/;
	const passwordIsValid = passwordRegex.test(passwordValue);

	if (passwordIsValid) {
		this.setCustomValidity("");
	} else {
		this.setCustomValidity("Password must be at least 8 characters long and contain both letters and numbers.");
	}
	this.reportValidity();
});
var idNumbers = document.getElementById("id-numbers");
var gender = document.getElementById("gender");
var birthDate = document.getElementById("birth-date");
var phoneNumber = document.getElementById("phone-number");
phoneNumber.addEventListener("input", function() {
	const phoneNumberValue = this.value;
	const phoneNumberRegex = /^09\d{8}$/;
	const phoneNumberIsValid = phoneNumberRegex.test(phoneNumberValue);

	if (phoneNumberIsValid) {
		this.setCustomValidity("");
	} else {
		this.setCustomValidity("Please input valid Phone Number");
	}
	this.reportValidity();
});
var passwordCheck = document.getElementById("password-check");
passwordCheck.addEventListener("input", function() {
	const passwordValue = document.getElementById("password").value;
	const passwordCheckValue = this.value;

	if (passwordValue === passwordCheckValue) {
		this.setCustomValidity("");
	} else {
		this.setCustomValidity("Passwords do not match");
	}
	this.reportValidity();
});

/* 註冊區域 */

var registerFormForKey = document.getElementById("register-form");
registerFormForKey.addEventListener("keyup", function(event) {
	console.log("放開鍵盤");
	var keyCode = event.keyCode || event.which;
	if (keyCode == 13) {
		registerAccount();
	}
});

function registerAccount() {
	var xhr;
	var firstName = document.getElementById("first-name").value;
	var lastName = document.getElementById("last-name").value;
	var email = document.getElementById("email").value;
	var password = document.getElementById("password").value;
	var gender = document.getElementById("gender").value;
	var birthDate = document.getElementById("birth-date").value;
	var phoneNumber = document.getElementById("phone-number").value;
	var formData = new FormData();
	formData.append("firstName", firstName);
	formData.append("lastName", lastName);
	formData.append("email", email);
	formData.append("password", password);
	formData.append("gender", gender);
	formData.append("birthDate", birthDate);
	formData.append("phoneNumber", phoneNumber);

	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	}
	else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}

	xhr.open("POST", "http://localhost:8080/SocialMedia/AccountRegister", true);

	xhr.onreadystatechange = function get_data() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var jsonObj = JSON.parse(xhr.responseText);
				var isRegisterSuccessful = jsonObj.isRegisterSuccessful;
				if (isRegisterSuccessful) {
					var registerForm = document.querySelector(".register_form");
					registerForm.classList.remove("open_registerform");
					var setAlertDialog = document.createElement("div");
					setAlertDialog.setAttribute("id", "alert-dialog");
					var setAlertDialogText = document.createElement("div");
					setAlertDialogText.setAttribute("id", "alert-dialog-text");
					setAlertDialogText.append("您已註冊成功，請進行登入");
					setAlertDialog.append(setAlertDialogText);
					var setContainerDiv = document.querySelector(".container_div");
					setContainerDiv.append(setAlertDialog);
					setTimeout(function() {
						var element = document.getElementById("login-error-message-text");
					if (element) {
						while (element.firstChild) {
							element.firstChild.remove();
						}
						element.remove();
					}
						toIndex();
					}, 1000);
				} else {
					var element = document.getElementById("register-error-message-text");
					if (element) {
						while (element.firstChild) {
							element.firstChild.remove();
						}
						element.remove();
					}
					var registerForm = document.querySelector(".register_form");
					registerForm.classList.add("open_registerform");
					var registerErrorMessage = document.getElementById("register-error-message");
					var setErrorMessageText = document.createElement("div");
					setErrorMessageText.setAttribute("id", "register-error-message-text");
					setErrorMessageText.setAttribute("style", "color: red; font-weight: bold");
					setErrorMessageText.append("註冊失敗");
					registerErrorMessage.append(setErrorMessageText)
				}
			}
		}
	}
	xhr.send(formData);
}


/* 登入介面 */

var loginFormForKey = document.getElementById("login-form");
loginFormForKey.addEventListener("keyup", function(event) {
	console.log("放開鍵盤");
	var keyCode = event.keyCode || event.which;
	if (keyCode == 13) {
		submitLogin();
	}
});

function submitLogin() {
	var xhr;
	var loginAccount = document.getElementById("login-account").value;
	var loginPassword = document.getElementById("login-password").value;
	var formData = new FormData();
	formData.append("loginAccount", loginAccount);
	formData.append("loginPassword", loginPassword);
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	}
	else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}
	xhr.open("POST", "http://localhost:8080/SocialMedia/Login", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var jsonObj = JSON.parse(xhr.responseText);
				var userId = jsonObj.userId;
				var userSession = jsonObj.userSession;
				var isLoginSuccessful = jsonObj.isLoginSuccessful;

				if (isLoginSuccessful) {
					var loginForm = document.querySelector(".login_form");
					loginForm.classList.remove("open_loginform");
					var setAlertDialog = document.createElement("div");
					setAlertDialog.setAttribute("id", "alert-dialog");
					var setAlertDialogText = document.createElement("div");
					setAlertDialogText.setAttribute("id", "alert-dialog-text");
					setAlertDialogText.append("您已登入成功");
					setAlertDialog.append(setAlertDialogText);
					var setContainerDiv = document.querySelector(".container_div");
					setContainerDiv.append(setAlertDialog);
					localStorage.setItem("userId", userId);
					localStorage.setItem("userSession", userSession);
					setTimeout(function() {
						toHomePage();
					}, 1000);

				} else {
					var element = document.getElementById("login-error-message-text");
					if (element) {
						while (element.firstChild) {
							element.firstChild.remove();
						}
						element.remove();
					}
					var loginForm = document.querySelector(".login_form");
					loginForm.classList.add("open_loginform");
					var loginErrorMessage = document.getElementById("login-error-message");
					var setErrorMessageText = document.createElement("div");
					setErrorMessageText.setAttribute("id", "login-error-message-text");
					setErrorMessageText.setAttribute("style", "color: red; font-weight: bold");
					setErrorMessageText.append("登入失敗，帳號或密碼錯誤");
					loginErrorMessage.append(setErrorMessageText)
				}

			}
		}
	}
	xhr.send(formData);
}

/* 通用樣式　*/

/* 透過好友列表跳轉好友個人頁面*/
function toFriendPersonalPage(friendSession) {
	console.log(friendSession);
	window.location.href = "/SocialMedia/users/" + friendSession + "/personal-page.html";
}

/* 透過貼文跳轉至貼文者頁面 */
function toUserPersonalPage(posterSession) {
	window.location.href = "/SocialMedia/users/" + posterSession + "/personal-page.html";
}

function toIndex() {
	window.location.href = "/SocialMedia/index.html";
}

/* 跳轉至首頁 */
function toHomePage() {
	window.location.href = "/SocialMedia/homepage.html";
}

/* 跳轉至現使用者個人頁面 */
function toCurrentUserPersonalPage() {
	var dirName = localStorage.getItem("userSession");
	window.location.href = "/SocialMedia/users/" + dirName + "/personal-page.html";
}

/* 跳轉至編輯頁面 */
function toPostEditor() {
	window.location.href = "/SocialMedia/posteditor.html";
}

/* 跳轉至帳號設定 */
function toAccountEditor() {
	window.location.href = "/SocialMedia/accounteditor.html";
}

/* 登出頁面 */
function logout() {
	localStorage.clear();
	toHomePage();
}

/* 個人頁面 */
function toPersonalPage() {
	window.location.href = "PersonalPage.html";
}

/* 帳號控制按鈕 */
function showAccountSettingList() {
	var accountList = document.querySelector(".account_list");
	if (accountList.style.display == "flex") {
		accountList.style.display = "none";
	} else {
		accountList.style.display = "flex";
	}

}

/* 照片輪播 */
let index = 0;
function refresh() {
	if (index < 0) {
		index = 0
	} else if (index > 2) {
		index = 2
	}

	let slideShadowImg = document.querySelector(".slideshadow_outter");

	let width = getComputedStyle(slideShadowImg).width
	width = Number(width.slice(0, -2))

	slideShadowImg.querySelector(".slideshadow_inner").style.left = index * width * -1 + "px"
}

function leftShift() {
	index--
	refresh()
}

function rightShift() {
	index++
	refresh()
}

refresh()



/* 警示 */
