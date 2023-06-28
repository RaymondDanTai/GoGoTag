/* 取得header的高度以供CSS使用 */
const header = document.querySelector('header');
const main = document.querySelector('main');
main.style.setProperty('--header-height', header.offsetHeight + 'px');

/* 判斷使用者是否為登入，如未登入則需顯示登入UI和註冊UI的呼叫按鈕*/
CheckLoginStatus();
function CheckLoginStatus() {
	console.log("開始檢查");
	var userId = localStorage.getItem("userId");
	var userSession = localStorage.getItem("userSession");
	console.log(userId);
	console.log(userSession);
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

						/* 設置導覽行的使用者頭像 */
						var setNavAvatarImage = document.createElement("img");
						setNavAvatarImage.classList.add("avatar_img");
						setNavAvatarImage.src = userAvatarURL;
						var setNavAvatarButton = document.getElementById("nav-avatar-button");
						setNavAvatarButton.append(setNavAvatarImage);

						/* 設置左側邊欄的使用者名稱 */
						var setLeftUserNameArea = document.getElementById("left-user-name-area");
						var setLeftUseName = document.createElement("div");
						setLeftUseName.append(userName);
						setLeftUserNameArea.append(setLeftUseName);

						/* 設置完畢後載入貼文 */
						getUserInformation();

					} else {

						/* 未登入跳轉回初始頁面 */
						toIndex();
					}
				}
			}
		}
		xhr.send(formData);
	}
}



function getUserInformation() {

	var userId = localStorage.getItem("userId");
	var userSession = localStorage.getItem("userSession");

	var formData = new FormData();
	formData.append("userId", userId);
	formData.append("userSession", userSession);

	var xhr;
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}

	xhr.open("POST", "/SocialMedia/GetCurrentUserInformation", true)
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var jsonObj = JSON.parse(xhr.responseText);
				var lastName = jsonObj.lastName;
				var firstName = jsonObj.firstName;
				var nickname = jsonObj.nickname;
				var email = jsonObj.email;
				var gender = jsonObj.gender;
				var phoneNumber = jsonObj.phoneNumber;
				var birthDate = jsonObj.birthDate;
				var userAvatarSource = jsonObj.userAvatarSource;

				/* 密碼更新 */
				document.getElementById("email").value = email;
				document.getElementById("lastname").value = lastName;
				document.getElementById("firstname").value = firstName;
				document.getElementById("gender").value = gender;
				document.getElementById("phone-number").value = phoneNumber;
				document.getElementById("birth-date").value = birthDate;
				document.getElementById("preview-img").setAttribute("src", userAvatarSource);
			}
		}
	}
	xhr.send(formData);
}

/* 更新密碼 */
function unlockUpdateColumn() {
	document.getElementById("original-password").readOnly = false;
	document.getElementById("new-password").readOnly = false;
	document.getElementById("new-password-confirm").readOnly = false;
	document.getElementById("update-private-information").disabled = false;
}

function updateUserPassword() {
	var userId = localStorage.getItem("userId");
	var userSession = localStorage.getItem("userSession");
	var originalUserPassword = document.getElementById("original-password").value;
	var newUserPassword = document.getElementById("new-password").value;

	var formData = new FormData();
	formData.append("userId", userId);
	formData.append("userSession", userSession);
	formData.append("originalUserPassword", originalUserPassword);
	formData.append("newUserPassword", newUserPassword);

	var xhr;
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}
	xhr.open("POST", "/SocialMedia/UpdateCurrentUserPassword", true)

	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var jsonObj = JSON.parse(xhr.responseText);
				var isPasswordCorrect = jsonObj.isPasswordCorrect;
				if (!isPasswordCorrect) {
					originalUserPassword = document.getElementById("original-password");
					originalUserPassword.focus();
				} else{
					var isUserPasswordUpdated = jsonObj.isUserPasswordUpdated;
					if (isUserPasswordUpdated) {
						toAccountEditor();
					} else {
						newUserPassword = document.getElementById("new-password");
						originalUserPassword.focus();
					}
				}
			}
		}
	}
	xhr.send(formData);
}

/* 更新個人資訊 */
function updateUserInformation() {
	var userId = localStorage.getItem("userId");
	var userSession = localStorage.getItem("userSession");
	var maintenanceAction = "updateUserInformation";
	var lastname = document.getElementById("lastname").value;
	var firstname = document.getElementById("firstname").value;
	var gender = document.getElementById("gender").value;
	var phoneNumber = document.getElementById("phone-number").value;
	var birthDate = document.getElementById("birth-date").value;
	var xhr;
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}
	xhr.open("POST", "/SocialMedia/UpdateCurrentUserInformation", true)
	var formData = new FormData();
	formData.append("userId", userId);
	formData.append("userSession", userSession);
	formData.append("maintenanceAction", maintenanceAction);
	formData.append("lastname", lastname);
	formData.append("firstname", firstname);
	formData.append("gender", gender);
	formData.append("phoneNumber", phoneNumber);
	formData.append("birthDate", birthDate);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var jsonObj = JSON.parse(xhr.responseText);
				var isUserInformationUpdated = jsonObj.isUserInformationUpdated;
				console.log(isUserInformationUpdated);
				if (isUserInformationUpdated == true) {
					toAccountEditor();
				} else {
				}
			}
		}
	}
	xhr.send(formData);
}

/* 更改頭像 */
/* 預覽圖片 */
var userAvatarData = null;
function previewImage() {
	document.querySelector(".upload_file").click();
};
document.querySelector(".upload_file").addEventListener('change', function() {
	const reader = new FileReader();
	userAvatarData = this.files[0];
	if (userAvatarData) {
		reader.addEventListener('load', function() {
			document.querySelector(".preview_img").src = reader.result;
		});
		reader.readAsDataURL(userAvatarData);
		console.log(userAvatarData);
	}
});

function updateAvatar() {
	var userId = localStorage.getItem("userId");
	var userSession = localStorage.getItem("userSession");
	var userAvatar = document.querySelector(".preview_img").src;
	var formData = new FormData();
	formData.append("userId", userId);
	formData.append("userSession", userSession);
	if (userAvatarData != null) {
		formData.append("userAvatarData", userAvatarData, userAvatarData.name);
		sendFormData(formData);
	} else {
		fetch(userAvatar)
			.then(response => response.blob())
			.then(blob => {
				const fileName = userAvatar.substring(userAvatar.lastIndexOf("/") + 1);
				formData.append("userAvatarData", blob, fileName);
				sendFormData(formData);
			})
			.catch(error => {
				console.error("發生錯誤:", error);
			});
	}
	function sendFormData(formData) {
		var xhr;
		if (window.ActiveXObject) {
			xhr = new ActiveXObject("Microsoft.XMLHTTP");
		} else if (window.XMLHttpRequest) {
			xhr = new XMLHttpRequest();
		}
		xhr.open("POST", "/SocialMedia/UpdateUserAvatarURL", true);
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4) {
				if (xhr.status == 200) {
					var jsonObj = JSON.parse(xhr.responseText);
					var isUserAvatarUpdated = jsonObj.isUserAvatarUpdated;
					if (isUserAvatarUpdated == true) {
						toAccountEditor();
					} else {
					}
				}
			}
		}
		xhr.send(formData);
	}
};


/* 本頁跳轉 */
function toUpdatePassword() {
	document.getElementById("user-private").style.display = "flex";
	document.getElementById("user-information").style.display = "none";
	document.getElementById("user-avatar").style.display = "none";
}

function toUpdateInformation() {
	document.getElementById("user-private").style.display = "none";
	document.getElementById("user-information").style.display = "flex";
	document.getElementById("user-avatar").style.display = "none";
}

function toUpdateAvatar() {
	document.getElementById("user-private").style.display = "none";
	document.getElementById("user-information").style.display = "none";
	document.getElementById("user-avatar").style.display = "flex";
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

/* 朋友牆 */
function toFriendsWall() {
	window.location.href = "/SocialMedia/friends-wall.html";
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