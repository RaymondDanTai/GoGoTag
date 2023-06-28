/* 取得header的高度 */
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

					} else {

						/* 未登入跳轉回初始頁面 */
						toIndex();
					}
				}
			}
		}
		xhr.send(formData);
	} else{
		toIndex();
	}
}

/* 預覽圖片 */
var postImageData = null;
function previewImage() {
	document.querySelector(".upload_file").click();
};

document.querySelector(".upload_file").addEventListener('change', function() {
	const reader = new FileReader();
	postImageData = this.files[0];
	if (postImageData) {
		reader.addEventListener('load', function() {
			document.querySelector(".preview_img").src = reader.result;
		});
		reader.readAsDataURL(postImageData);
		console.log(postImageData);
	}
});

/* 編輯新貼文 */
var confirmEdit = document.querySelector("#confirm-edit");
confirmEdit.addEventListener("click", function() {

	var postText = document.querySelector("#edit-text").value;
	var tagId = document.querySelector(".tag_selector").value;
	var posterId = localStorage.getItem("userId");
	var locationName = document.querySelector(".edit_location").value;

	var formData = new FormData();
	formData.append("postText", postText);
	formData.append("postImageData", postImageData, postImageData.name);
	formData.append("posterId", posterId);
	formData.append("tagId", tagId);
	formData.append("locationName", locationName);

	var xhr;
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}

	xhr.open("POST", "/SocialMedia/AddNewPost", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var jsonObj = JSON.parse(xhr.responseText);
				var isPostUpdated = jsonObj.isPostUpdated;
				if (isPostUpdated) {
					toHomePage();
				} else {
				}
			}
		}
	}
	xhr.send(formData);
});

/* 取消編輯 */
var cancelEdit = document.querySelector("#cancel-edit");
cancelEdit.addEventListener("click", function() {
	document.querySelector(".tag_selector").value = "0";
	document.querySelector(".edit_location").value = "";
	document.querySelector("#edit-text").value = "";
	document.querySelector("#upload-file").value = "";
	document.querySelector("#preview-img").setAttribute("src", "");
})

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
	window.location.href = "friends-wall.html";
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