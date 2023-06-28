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
						showAllPosts()

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

function showAllPosts() {

	/* 取得貼文起始時間 */
	var postTimeBaseline;
	var sqlSelect;
	if (sessionStorage.getItem("lastPost")) {
		postTimeBaseline = sessionStorage.getItem("lastPost");
		sqlSelect = 1;
	} else {
		postTimeBaseline = getNow();
		sqlSelect = 0
	}

	var xhr;
	var userId = localStorage.getItem("userId");
	var formData = new FormData();
	formData.append("userId", userId);
	formData.append("postTimeBaseline", postTimeBaseline);
	formData.append("sqlSelect", sqlSelect);
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	}
	else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}
	xhr.open("POST", "/SocialMedia/ShowAllPosts", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			var jsonArray = JSON.parse(xhr.responseText);
			for (var i = 0; i < jsonArray.length; i++) {
				var jsonObj = jsonArray[i];
				var postId = jsonObj.postId;
				var posterAvatarURL = jsonObj.posterAvatarURL;
				var posterName = jsonObj.posterName;
				var updatedDate = jsonObj.updatedDate;
				var tagName = jsonObj.tagName;
				var locationName = jsonObj.locationName;
				var postText = jsonObj.postText;
				var postImgURL = jsonObj.postImgURL;
				var likeCount = jsonObj.likeCount;
				var checkPoster = jsonObj.checkPoster;
				var isUserLiked = jsonObj.isUserLiked;
				var commentCount = jsonObj.commentCount;
				var posterId = jsonObj.posterId;
				var posterSession = jsonObj.posterSession;
				var updatedAt = jsonObj.updatedAt;
				if (i == jsonArray.length - 1) {
					sessionStorage.setItem("lastPost", updatedAt);
					console.log(updatedAt);
				}

				var setPostContent = document.createElement("div");
				setPostContent.classList.add("post_content");
				setPostContent.id = postId;

				/* 組成poster_area */
				var setPosterArea = document.createElement("div");
				setPosterArea.classList.add("poster_area");
				var setPosterAvatar = document.createElement("div");
				setPosterAvatar.classList.add("poster_avatar");
				var setPosterImg = document.createElement("img");
				setPosterImg.classList.add("poster_img");
				setPosterImg.src = posterAvatarURL; //變數
				setPosterAvatar.append(setPosterImg);
				var setPostDetail = document.createElement("div");
				setPostDetail.classList.add("post_detail");
				var setEmptyDivOne = document.createElement("div");
				var setPosterName = document.createElement("div");
				setPosterName.classList.add("poster_name");
				setPosterName.setAttribute("id", "poster-name-" + posterId);
				setPosterName.setAttribute("onclick", "toUserPersonalPage(\"" + posterSession + "\")");
				setPosterName.append(posterName); //變數
				var setPostDate = document.createElement("div");
				setPostDate.classList.add("post_date");
				setPostDate.append(updatedDate); //變數
				setEmptyDivOne.append(setPosterName);
				setEmptyDivOne.append(setPostDate);
				var setEmptyDivTwo = document.createElement("div");
				var setPostTagName = document.createElement("div");
				setPostTagName.classList.add("post_tagName");
				setPostTagName.append(tagName); //變數
				var setPostLocationName = document.createElement("div");
				setPostLocationName.classList.add("post_locationName");
				setPostLocationName.append(locationName); //變數
				setEmptyDivTwo.append(setPostTagName);
				setEmptyDivTwo.append(setPostLocationName);
				setPostDetail.append(setEmptyDivOne);
				setPostDetail.append(setEmptyDivTwo);
				var setFunctionButton = document.createElement("div");
				setFunctionButton.setAttribute("class", "function_button fa-solid fa-ellipsis");
				setFunctionButton.setAttribute("onclick", "showList(event)");
				setFunctionButton.id = postId + "-functionButton"
				setPosterArea.append(setPosterAvatar);
				setPosterArea.append(setPostDetail);
				if (checkPoster == "1") {
					var functionList = document.createElement("div");
					functionList.classList.add("function_list");
					functionList.id = postId + "-functionList";
					var functionEdit = document.createElement("div");
					functionEdit.classList.add("function_items");
					functionEdit.setAttribute("onclick", "getPreEditPost(event)");
					functionEdit.id = postId + "-edit"
					functionEdit.append("編輯");
					var functionDelete = document.createElement("div");
					functionDelete.classList.add("function_items");
					functionDelete.id = postId + "-delete"
					functionDelete.setAttribute("onclick", "createPostDeleteSection(event)");
					functionDelete.append("刪除");

					functionList.append(functionEdit);
					functionList.append(functionDelete);
					setPostContent.append(functionList);
					setPosterArea.append(setFunctionButton);
				}

				/* 組成text_area */
				var setTextArea = document.createElement("div");
				setTextArea.classList.add("text_area");
				var setTextAreaContent = document.createElement("textarea");
				setTextAreaContent.readOnly = true;
				setTextAreaContent.setAttribute("row", "5");
				setTextAreaContent.append(postText); //變數
				setTextArea.append(setTextAreaContent);

				/* 組成img_area */
				var setImgArea = document.createElement("div");
				setImgArea.classList.add("img_area");
				var setPostImg = document.createElement("img");
				setPostImg.classList.add("post_img");
				setPostImg.src = postImgURL; //變數
				setImgArea.append(setPostImg);

				/* 組成count_area */
				var setCountArea = document.createElement("div");
				setCountArea.classList.add("count_area");
				var setLikeArea = document.createElement("div");
				setLikeArea.classList.add("like_area");
				var setLikeIcon = document.createElement("div");
				setLikeIcon.classList.add("like_icon");
				var setLikeIconI = document.createElement("i");
				setLikeIcon.classList.add("fa-solid");
				setLikeIcon.classList.add("fa-thumbs-up");
				setLikeIcon.append(setLikeIconI);
				var setLikeCount = document.createElement("div");
				setLikeCount.classList.add("like_count");
				setLikeCount.setAttribute("id", postId + "-like-count");
				setLikeArea.append(setLikeIcon);
				setLikeCount.append(likeCount); //變數
				setLikeArea.append(setLikeCount);


				var setEmptyDivThree = document.createElement("div");
				var setCommentCount = document.createElement("div");
				setCommentCount.classList.add("comment_count");
				setCommentCount.setAttribute("id", postId + "-comment-count");
				setCommentCount.append(commentCount + "則留言") //變數
				/*
				var setShareCount = document.createElement("div");
				setShareCount.classList.add("share_count");
				setShareCount.append("分享348") //變數
				*/

				setEmptyDivThree.append(setCommentCount);
				/*
				setEmptyDivThree.append(setShareCount);
				*/
				setCountArea.append(setLikeArea);
				setCountArea.append(setEmptyDivThree);

				/* 組成互動區域 */
				var setActArea = document.createElement("div");
				setActArea.classList.add("act_area");
				var setLikeButton = document.createElement("button");
				setLikeButton.classList.add("like_button");
				setLikeButton.classList.add("act_button");
				setLikeButton.setAttribute("id", postId + "-like-button")
				setLikeButton.setAttribute("onclick", "checkUserLikedPost(event)")
				if (isUserLiked) {
					setLikeButton.setAttribute("style", "background-color: #2A52BE; color: #F5FFFA;")
				}
				setLikeButton.append("喜歡");
				var setCommentButton = document.createElement("button");
				setCommentButton.classList.add("comment_button");
				setCommentButton.classList.add("act_button");
				setCommentButton.id = postId + "-comment-button";
				setCommentButton.setAttribute("onclick", "createCommentsArea(event)");
				setCommentButton.append("留言");
				/*
				var setShareButton = document.createElement("button");
				setShareButton.classList.add("share_button");
				setShareButton.classList.add("act_button");
				setShareButton.append("分享");
				*/
				setActArea.append(setLikeButton);
				setActArea.append(setCommentButton);
				/*
				setActArea.append(setShareButton);
				*/

				setPostContent.append(setPosterArea);
				setPostContent.append(setTextArea);
				setPostContent.append(setImgArea);
				setPostContent.append(setCountArea);
				setPostContent.append(setActArea);

				var middleColumn = document.querySelector(".middle_column");
				middleColumn.append(setPostContent);
			}
		}
	}
	xhr.send(formData);
};

function createPostDeleteSection(event) {
	var postDeleteButtonId = event.target.id;

	var setBody = document.body;
	var setPostDeleteConfirmSection = document.createElement("section");
	setPostDeleteConfirmSection.setAttribute("id", "post-delete-confirm-section");
	var setPostDeleteConfirmArea = document.createElement("div");
	setPostDeleteConfirmArea.setAttribute("id", "post-delete-confirm-area");
	var setPostDeleteConfirmText = document.createElement("div");
	setPostDeleteConfirmText.setAttribute("id", "post-delete-confirm-text");
	setPostDeleteConfirmText.append("是否確認刪除貼文？")
	var setPostDeleteButtonArea = document.createElement("div");
	setPostDeleteButtonArea.setAttribute("id", "post-delete-button-area");
	var setPostDeleteYesButton = document.createElement("button");
	setPostDeleteYesButton.setAttribute("class", "post_delete_buttons");
	setPostDeleteYesButton.setAttribute("id", "post-delete-yes-button");
	setPostDeleteYesButton.setAttribute("onclick", "deletePost(\"" + postDeleteButtonId + "\")");
	setPostDeleteYesButton.append("是");
	var setPostDeleteNoButton = document.createElement("button");
	setPostDeleteNoButton.setAttribute("class", "post_delete_buttons");
	setPostDeleteNoButton.setAttribute("id", "post-delete-no-button");
	setPostDeleteNoButton.setAttribute("onclick", "removePostDeleteSection()");
	setPostDeleteNoButton.append("否");
	setPostDeleteButtonArea.append(setPostDeleteYesButton);
	setPostDeleteButtonArea.append(setPostDeleteNoButton);
	setPostDeleteConfirmArea.append(setPostDeleteConfirmText);
	setPostDeleteConfirmArea.append(setPostDeleteButtonArea);
	setPostDeleteConfirmSection.append(setPostDeleteConfirmArea);
	setBody.append(setPostDeleteConfirmSection);
}

function removePostDeleteSection() {
	var element = document.getElementById("post-delete-confirm-section");
	if (element) {
		while (element.firstChild) {
			element.firstChild.remove();
		}
		element.remove();
	}
}

/* 刪除貼文 */
function deletePost(postDeleteButtonId) {

	removePostDeleteSection()

	/*
	var functionListId = event.target.parentElement.id;
	var calledFunctionList = document.getElementById(postDeleteButtonId);
	calledFunctionList.classList.remove("show_functionList");
	var deleteButtonId = event.target.id;
	*/

	var postId = document.getElementById(postDeleteButtonId).parentElement.parentElement.id;
	var formData = new FormData();
	formData.append("postId", postId);

	var xhr;
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}

	xhr.open("POST", "/SocialMedia/DeletePost", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			var jsonObj = JSON.parse(xhr.responseText);
			var isPostDeleted = jsonObj.isPostDeleted;

			if (isPostDeleted) {

				var element = document.querySelector(".middle_column");
				if (element) {
					while (element.firstChild) {
						element.firstChild.remove();
					}
				}

				sessionStorage.removeItem("lastPost");

				showAllPosts();

			} else {

			}
		}
	}
	xhr.send(formData);
}

/* 貼文的留言維護 */

/* 產出留言區域 */
function createCommentsArea(event) {
	var commentButtonId = event.target.id;
	var commentedPostId = document.getElementById(commentButtonId).parentElement.parentElement.id;
	var userId = localStorage.getItem("userId");

	var currentCommentArea = document.getElementById("comment-area");
	if ((currentCommentArea !== null) && (currentCommentArea.parentElement.id == commentedPostId)) {
		/* 如果留言區域已經存在，並且點擊對對象為同一留言按鈕則清空　*/
		var element = document.getElementById("comment-area");
		if (element) {
			while (element.firstChild) {
				element.firstChild.remove();
			}
			element.remove();
		}
		return;
	} else if (currentCommentArea !== null) {
		/* 如果留言區域已經存在，則清空既有的留言區域並於指定點重新產出留言區　*/
		var element = document.getElementById("comment-area");
		if (element) {
			while (element.firstChild) {
				element.firstChild.remove();
			}
			element.remove();
		}
	}

	var formData = new FormData();
	formData.append("postId", commentedPostId);
	formData.append("userId", userId);

	var xhr;
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}

	xhr.open("POST", "/SocialMedia/GetPostComments", true);

	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			var jsonArray = JSON.parse(xhr.responseText);

			/* 刻劃其他使用者留言區域 */
			var setOtherUsersCommentList = document.createElement("div");
			setOtherUsersCommentList.setAttribute("class", "other_users_comment_list");
			for (var i = 0; i < jsonArray.length; i++) {
				var jsonObj = jsonArray[i];
				var commentId = jsonObj.commentId;
				var commenterId = jsonObj.commenterId;
				var commenterAvatarSource = jsonObj.commenterAvatarSource;
				var commenterName = jsonObj.commenterName;
				var commentText = jsonObj.commentText;
				var isCommenter = jsonObj.isCommenter;
				var setOtherUserCommentArea = document.createElement("div");
				setOtherUserCommentArea.setAttribute("class", "other_user_comment_area");
				setOtherUserCommentArea.id = commentId + "-" + commenterId + "-comment";
				var setCommenterInformationArea = document.createElement("div");
				setCommenterInformationArea.setAttribute("class", "commenter_information_area");
				var setCommenterAvatarArea = document.createElement("div");
				setCommenterAvatarArea.setAttribute("class", "commenter_avatar_area");
				var setOtherUserAvatarImage = document.createElement("img");
				setOtherUserAvatarImage.setAttribute("class", "other_user_avatar_image");
				setOtherUserAvatarImage.src = commenterAvatarSource;
				setCommenterAvatarArea.append(setOtherUserAvatarImage);
				setCommenterInformationArea.append(setCommenterAvatarArea);
				var setCommenterName = document.createElement("div");
				setCommenterName.setAttribute("class", "commenter_name");
				setCommenterName.append(commenterName);
				setCommenterInformationArea.append(setCommenterName);
				var setCommentDeleteButton;
				if (isCommenter) {
					setCommentDeleteButton = document.createElement("button");
					setCommentDeleteButton.setAttribute("class", "comment_delete_button");
					setCommentDeleteButton.setAttribute("id", commentId + "-comment-delete-button");
					setCommentDeleteButton.setAttribute("onclick", "createCommentDeleteSection(event)");
					setCommentDeleteButton.append("刪除");
					setCommenterInformationArea.append(setCommentDeleteButton);
				}
				var setOtherUserCommentText = document.createElement("textArea");
				setOtherUserCommentText.setAttribute("class", "other_user_comment_text");
				setOtherUserCommentText.setAttribute("readonly", "true");
				setOtherUserCommentText.value = commentText;
				setOtherUserCommentArea.append(setCommenterInformationArea);
				setOtherUserCommentArea.append(setOtherUserCommentText);
				setOtherUsersCommentList.append(setOtherUserCommentArea);
			}

			/* 刻劃使用者撰寫留言區域 */
			var setCurrentUserCommentArea = document.createElement("div");
			setCurrentUserCommentArea.classList.add("current_user_comment_area");
			setCurrentUserCommentArea.id = localStorage.getItem("userId");
			var setCurrentUserAvatarArea = document.createElement("div");
			setCurrentUserAvatarArea.classList.add("current_user_avatar_area");
			var setCurrentUserAvatarImage = document.createElement("img");
			setCurrentUserAvatarImage.classList.add("current_user_avatar_image");
			setCurrentUserAvatarImage.src = document.querySelector(".avatar_img").src;
			var setCurrentUserCommentText = document.createElement("textarea");
			setCurrentUserCommentText.classList.add("current_user_comment_text");
			setCurrentUserCommentText.setAttribute("placeholder", "我看你還是說點什麼吧…")
			setCurrentUserCommentText.setAttribute("id", commentedPostId + "-comment-text");
			var setCommentTextSendButton = document.createElement("button");
			setCommentTextSendButton.setAttribute("class", "comment_text_send_button fa-solid fa-paper-plane");
			setCommentTextSendButton.setAttribute("id", commentedPostId + "-commenttext-send-button");
			setCommentTextSendButton.setAttribute("onclick", "addPostComment(event)");
			setCurrentUserAvatarArea.append(setCurrentUserAvatarImage);
			setCurrentUserCommentArea.append(setCurrentUserAvatarArea);
			setCurrentUserCommentArea.append(setCurrentUserCommentText);
			setCurrentUserCommentArea.append(setCommentTextSendButton);

			/* 刻劃整個留言區域 */
			var setCommentsArea = document.createElement("div");
			setCommentsArea.classList.add("comments_area");
			setCommentsArea.setAttribute("id", "comment-area");
			setCommentsArea.append(setOtherUsersCommentList);
			setCommentsArea.append(setCurrentUserCommentArea);

			/* 將刻畫好的留言區域新增至貼文底下 */
			var commentedPost = document.getElementById(commentedPostId);
			commentedPost.append(setCommentsArea);
		}
	}
	xhr.send(formData);
}


function addPostComment(event) {
	/* 取得預增加留言區的貼文id */
	var currentUserCommentTextId = event.target.id;
	var commentedPostId = "";
	for (var i = 0; i < currentUserCommentTextId.indexOf("-"); i++) {
		commentedPostId += currentUserCommentTextId[i];
	}
	var commentText = document.getElementById(commentedPostId + "-comment-text").value;
	var commenterId = localStorage.getItem("userId")
	var formData = new FormData();
	formData.append("commentText", commentText);
	formData.append("commentedPostId", commentedPostId);
	formData.append("commenterId", commenterId);
	var xhr;
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}
	xhr.open("POST", "/SocialMedia/AddPostComment", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			document.getElementById(commentedPostId + "-comment-text").value = "";
			var clickedButton = document.getElementById(commentedPostId + "-comment-button");
			var clickEvent = new MouseEvent("click", {
				bubbles: true,
				cancelable: true,
				view: window
			});
			clickedButton.dispatchEvent(clickEvent);
			createCommentsArea(clickEvent);
			resetCommentCount(commentedPostId);
		}
	}
	xhr.send(formData);
}

function createCommentDeleteSection(event) {
	var commentButtonId = event.target.id;

	var setBody = document.body;
	var setCommentDeleteConfirmSection = document.createElement("section");
	setCommentDeleteConfirmSection.setAttribute("id", "comment-delete-confirm-section");
	var setCommentDeleteConfirmArea = document.createElement("div");
	setCommentDeleteConfirmArea.setAttribute("id", "comment-delete-confirm-area");
	var setCommentDeleteConfirmText = document.createElement("div");
	setCommentDeleteConfirmText.setAttribute("id", "comment-delete-confirm-text");
	setCommentDeleteConfirmText.append("是否確認刪除留言？")
	var setCommentDeleteButtonArea = document.createElement("div");
	setCommentDeleteButtonArea.setAttribute("id", "comment-delete-button-area");
	var setCommentDeleteYesButton = document.createElement("button");
	setCommentDeleteYesButton.setAttribute("class", "comment_delete_buttons");
	setCommentDeleteYesButton.setAttribute("id", "comment-delete-yes-button");
	setCommentDeleteYesButton.setAttribute("onclick", "deleteComment(\"" + commentButtonId + "\")");
	setCommentDeleteYesButton.append("是");
	var setCommentDeleteNoButton = document.createElement("button");
	setCommentDeleteNoButton.setAttribute("class", "comment_delete_buttons");
	setCommentDeleteNoButton.setAttribute("id", "comment-delete-no-button");
	setCommentDeleteNoButton.setAttribute("onclick", "removeCommentDeleteSection()");
	setCommentDeleteNoButton.append("否");
	setCommentDeleteButtonArea.append(setCommentDeleteYesButton);
	setCommentDeleteButtonArea.append(setCommentDeleteNoButton);
	setCommentDeleteConfirmArea.append(setCommentDeleteConfirmText);
	setCommentDeleteConfirmArea.append(setCommentDeleteButtonArea);
	setCommentDeleteConfirmSection.append(setCommentDeleteConfirmArea);
	setBody.append(setCommentDeleteConfirmSection);
}

function removeCommentDeleteSection() {
	var element = document.getElementById("comment-delete-confirm-section");
	if (element) {
		while (element.firstChild) {
			element.firstChild.remove();
		}
		element.remove();
	}
}

/* 刪除留言 */
function deleteComment(commentButtonId) {
	removeCommentDeleteSection();

	var commentDeleteButton = document.getElementById(commentButtonId);
	var commentedPostId = commentDeleteButton.parentElement.parentElement.parentElement.parentElement.parentElement.id;
	var commentId = "";
	for (var i = 0; i < commentButtonId.indexOf("-"); i++) {
		commentId += commentButtonId[i];
	}

	var formData = new FormData();
	formData.append("commentId", commentId);

	var xhr;
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}

	xhr.open("POST", "/SocialMedia/DeletePostComment", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			var jsonObj = JSON.parse(xhr.responseText);
			var isPostCommentdeleted = jsonObj.isPostCommentdeleted;
			if (isPostCommentdeleted) {
				var clickedButton = document.getElementById(commentedPostId + "-comment-button");
				console.log("clickedButton=" + clickedButton)
				var clickEvent = new MouseEvent("click", {
					bubbles: true,
					cancelable: true,
					view: window
				});
				clickedButton.dispatchEvent(clickEvent);
				createCommentsArea(clickEvent);
				resetCommentCount(commentedPostId);
			}
		}
	}
	xhr.send(formData);
}

/* 重計留言 */
function resetCommentCount(commentedPostId) {

	var formData = new FormData();
	formData.append("commentedPostId", commentedPostId);

	var xhr;
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}

	xhr.open("POST", "/SocialMedia/GetCommentCount", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			var jsonObj = JSON.parse(xhr.responseText);
			var commentCount = jsonObj.commentCount;

			var element = document.getElementById(commentedPostId + "-comment-count");
			if (element) {
				while (element.firstChild) {
					element.firstChild.remove();
				}
			}

			document.getElementById(commentedPostId + "-comment-count").append(commentCount + "則留言")
		}
	}
	xhr.send(formData);
}

/* 展開貼文功能列表 */
function showList(event) {

	var clickedButtonId = event.target.id;

	if (event.target.id == "") {
		var clickedButtonId = event.target.parentNode.id;
	}

	var getPostId = "";

	for (let i = 0; i < clickedButtonId.indexOf("-"); i++) {
		getPostId += clickedButtonId[i];
	}

	var functionListId = getPostId + "-functionList";
	var calledFunctionList = document.getElementById(functionListId);

	if (calledFunctionList.classList.contains("show_functionList")) {
		calledFunctionList.classList.remove("show_functionList");
	} else {
		calledFunctionList.classList.add("show_functionList");
	}
}

/* 呼叫預編輯之貼文編輯器 */
function getPreEditPost(event) {

	/* 關閉貼文功能列表 */
	var functionListId = event.target.parentElement.id;
	var calledFunctionList = document.getElementById(functionListId);
	calledFunctionList.classList.remove("show_functionList");

	/* 取得預編輯貼文之id */
	var clickedButtonId = event.target.id;
	var postId = document.getElementById(clickedButtonId).parentElement.parentNode.id;

	var formData = new FormData;
	formData.append("postId", postId);

	var xhr;
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}

	xhr.open("POST", "/SocialMedia/GetPreEditPost", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var jsonObj = JSON.parse(xhr.responseText);
				var postId = jsonObj.postId;
				var posterAvatar = jsonObj.posterAvatar;
				var tagsId = jsonObj.tagsId;
				var locationName = jsonObj.locationName;
				var postText = jsonObj.postText;
				var postImg = jsonObj.postImg;
				var posterName = jsonObj.posterName;
				document.querySelector(".post_editor").id = postId;
				document.getElementById("editorAvatar-img").src = posterAvatar;
				document.querySelector(".editor_name").append(posterName);
				document.querySelector(".tag_selector").value = tagsId;
				document.querySelector(".edit_location").value = locationName;
				document.getElementById("edit-text").value = postText;
				document.querySelector(".preview_img").src = postImg
				document.getElementById("hided-area").style.display = "flex";
			}
		}
	}
	xhr.send(formData);
}

/* 貼文編輯器預覽圖片 */
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

/* 確認完成編輯 */
var confirmEdit = document.querySelector("#confirm-edit");
confirmEdit.addEventListener("click", function() {
	var postId = document.querySelector(".post_editor").id;
	var tagId = document.querySelector(".tag_selector").value;
	var postText = document.querySelector("#edit-text").value;
	var locationName = document.querySelector(".edit_location").value;
	var postImg = document.querySelector(".preview_img").src;
	var formData = new FormData();

	if (postImageData != null) {
		formData.append("postImageData", postImageData, postImageData.name);
		sendFormData(formData);
	} else {
		fetch(postImg)
			.then(response => response.blob())
			.then(blob => {
				const fileName = postImg.substring(postImg.lastIndexOf("/") + 1);
				formData.append("postImageData", blob, fileName);
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
		formData.append("postId", postId);
		formData.append("postText", postText);
		formData.append("tagId", tagId);
		formData.append("locationName", locationName);
		xhr.open("POST", "/SocialMedia/EditPost", true);
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4) {
				if (xhr.status == 200) {
					var jsonObj = JSON.parse(xhr.responseText);
					var isEditPostSuccessful = jsonObj.isEditPostSuccessful;
					if (isEditPostSuccessful) {

						document.getElementById("hided-area").setAttribute("style", "display: none;");

						var element = document.querySelector(".middle_column");
						if (element) {
							while (element.firstChild) {
								element.firstChild.remove();
							}
						}

						sessionStorage.removeItem("lastPost");

						showAllPosts();
					}
				}
			}
		}
		xhr.send(formData);
	}
});

/* 取消編輯 */
var cancelEdit = document.querySelector("#cancel-edit");
cancelEdit.addEventListener("click", function() {
	document.getElementById("hided-area").style.display = "none";
})

/* 按讚 分享 打開小鈴鐺 */

/* 點擊喜歡時先檢查是否已經按讚 */
function checkUserLikedPost(event) {
	var clickedButtonId = event.target.id;
	var userId = localStorage.getItem("userId");
	var postId = document.getElementById(clickedButtonId).parentElement.parentElement.id;
	var likeCountElement = document.getElementById(postId + "-like-count");
	var likeCount = parseInt(likeCountElement.innerText);

	var formData = new FormData();
	formData.append("postId", postId);
	formData.append("userId", userId);

	var xhr;
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}

	xhr.open("POST", "/SocialMedia/CheckUserLikedPost", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var jsonObj = JSON.parse(xhr.responseText);
				var wasUserLikePost = jsonObj.wasUserLikePost;
				if (wasUserLikePost) {
					/* 如使用者已經點讚，呼叫取消點讚的函式 */
					cancelLiked(userId, postId, clickedButtonId, likeCountElement, likeCount);
				} else {
					/* 如使用者尚未點讚，呼叫點讚的函式 */
					thumbLike(userId, postId, clickedButtonId, likeCountElement, likeCount);
				}
			}
		}
	};
	xhr.send(formData);
}

/* 取消點讚 */
function cancelLiked(userId, postId, clickedButtonId, likeCountElement, likeCount) {

	var clickedButton = document.getElementById(clickedButtonId);

	var formData = new FormData();
	formData.append("postId", postId);
	formData.append("userId", userId);

	var xhr;
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}
	xhr.open("POST", "/SocialMedia/CancelLiked", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var jsonObj = JSON.parse(xhr.responseText);
				var islikeCanceled = jsonObj.islikeCanceled;
				if (islikeCanceled) {
					clickedButton.setAttribute("style", "background-color: #FFFFFF; color: #2A52BE;");
					likeCount--;
					likeCountElement.innerText = likeCount;
				}
			}
		}
	};
	xhr.send(formData);
}

/* 點擊按讚 */
function thumbLike(userId, postId, clickedButtonId, likeCountElement, likeCount) {

	var clickedButton = document.getElementById(clickedButtonId);

	var formData = new FormData();
	formData.append("postId", postId);
	formData.append("userId", userId);

	var xhr;
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}

	xhr.open("POST", "/SocialMedia/ThumbLike", true);
	xhr.onreadystatechange = function() {

		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var jsonObj = JSON.parse(xhr.responseText);
				var isLikeThumbed = jsonObj.isLikeThumbed;
				if (isLikeThumbed) {
					clickedButton.setAttribute("style", "background-color: #2A52BE; color: #FFFFFF;");
					likeCount++;
					likeCountElement.innerText = likeCount;
				}
			}
		}
	};
	xhr.send(formData);
}

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

function toGoodLuck(){
	window.location.href = "good-luck.html";
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

/* 20230620新增 */

/* 卷軸事件 */
var middleColumn = document.querySelector(".middle_column");
var isScrolling = false;

middleColumn.addEventListener('scroll', function() {
	// 設置延遲時間
	clearTimeout(isScrolling);
	isScrolling = setTimeout(function() {
		// 判斷是否已經抵達底部
		if (middleColumn.scrollTop > (middleColumn.scrollHeight - middleColumn.offsetHeight - 10)) {
			showAllPosts();
		}
	}, 100);
});

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

/* RWD */
function showPageArea() {
	var setPageArea = document.getElementById("page-area");
	if (setPageArea.classList.contains("show_page_area")) {
		setPageArea.classList.remove("show_page_area");
	} else {
		setPageArea.classList.add("show_page_area");
	}
}