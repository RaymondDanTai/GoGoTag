/* 進入頁面需先清除原本的時間基準點*/
sessionStorage.removeItem("lastPost");

/* 取得header的高度以供CSS使用 */
const header = document.querySelector('header');
const main = document.querySelector('main');
main.style.setProperty('--header-height', header.offsetHeight + 'px');

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
						diceANewUser()

					} else {

						/* 未登入跳轉回初始頁面 */
						toIndex();
					}
				}
			}
		}
		xhr.send(formData);
	} else {
		toIndex();
	}
}

function diceANewUser() {
	var userId = localStorage.getItem("userId");

	const formData = new FormData();
	formData.append("userId", userId);

	var xhr;
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}

	xhr.open("POST", "/SocialMedia/DiceRandomUsers", true)
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var jsonArray = JSON.parse(xhr.responseText);
				for (var i = 0; i < jsonArray.length; i++) {
					var jsonObj = jsonArray[i];
					var userSession = jsonObj.userSession;
					var userAvatarURL = jsonObj.userAvatarURL
					var setUserAvatarFlashImages = document.createElement("img");
					setUserAvatarFlashImages.setAttribute("class", "user_avatar_flash_images");
					setUserAvatarFlashImages.setAttribute("id", userSession + "avatar-flash-image");
					setUserAvatarFlashImages.setAttribute("onclick", "toUserPersonalPage(\"" + userSession + "\")");
					setUserAvatarFlashImages.setAttribute("src", userAvatarURL);
					document.getElementById("user-avatar-flash").append(setUserAvatarFlashImages);
				}
			}
		}
	}
	xhr.send(formData);
}

let index = 0;
function refresh() {
	if (index < 0) {
		index = 5
	} else if (index > 5) {
		index = 0
	}

	let slideShadowImg = document.querySelector(".slideshadow_outter");

	let width = getComputedStyle(slideShadowImg).width
	width = Number(width.slice(0, -2))

	slideShadowImg.querySelector("#user-avatar-flash").style.left = index * width * -1 + "px"
}

  // 每 200 毫秒觸發一次，即每秒觸發五次
var count = 0;
function changeRefresh() {
	index++;
	count++;
	if (count == 33) {
		clearInterval(interval);
		document.getElementById("dice-new-user").innerHTML = "點擊頭像";
	}
	refresh();
}

var interval ;
function showRandomUser() {
	interval = setInterval(changeRefresh, 100);
	document.getElementById("dice-new-user").setAttribute("disabled", "true");
}

/*
function showRandomUser() {
	document.querySelector(".user_avatar_flash_images").setAttribute("style", "display:flex;");
}
*/

/* 取得好友列表 */
getFriendList()
function getFriendList() {

	var currentUserId = localStorage.getItem("userId");

	var formData = new FormData();
	formData.append("currentUserId", currentUserId);

	var xhr;
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}

	xhr.open("POST", "/SocialMedia/GetCurrentUserFriends", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var jsonArray = JSON.parse(xhr.responseText);
				for (var i = 0; i < jsonArray.length; i++) {
					var jsonObj = jsonArray[i];
					var friendId = jsonObj.friendId;
					var friendAvatar = jsonObj.userAvatarURL;
					var setFriendAvatar = document.createElement("img");
					setFriendAvatar.setAttribute("class", "right_friends_avatar");
					setFriendAvatar.setAttribute("src", friendAvatar);
					var friendName = jsonObj.userName;
					var setFriendName = document.createElement("div");
					setFriendName.setAttribute("class", "right_friends_name");
					setFriendName.append(friendName);
					var friendSession = jsonObj.friendSession;
					var setFriendInfoArea = document.createElement("div");
					setFriendInfoArea.setAttribute("class", "right_friends_info_area");
					setFriendInfoArea.setAttribute("id", "friend-info-area-" + friendId);
					setFriendInfoArea.setAttribute("onclick", "toFriendPersonalPage(\"" + friendSession + "\")");
					setFriendInfoArea.append(setFriendAvatar);
					setFriendInfoArea.append(setFriendName);
					document.getElementById("friendList-contains").append(setFriendInfoArea);
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

/* 20230621新增 */

/* 取得現在時間 */
function getNow() {
	const now = new Date();
	const year = now.getFullYear();
	const month = String(now.getMonth() + 1).padStart(2, '0');
	const day = String(now.getDate()).padStart(2, '0');
	const hour = String(now.getHours()).padStart(2, '0');
	const minute = String(now.getMinutes()).padStart(2, '0');
	const second = String(now.getSeconds()).padStart(2, '0');
	const currentTime = `${year}-${month}-${day} ${hour}:${minute}:${second}`;
	return currentTime;
}
