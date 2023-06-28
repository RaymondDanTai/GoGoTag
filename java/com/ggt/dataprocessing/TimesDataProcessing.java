package com.ggt.dataprocessing;

import java.time.LocalDateTime;

public class TimesDataProcessing {

	public String getCurTime() {
		LocalDateTime now = LocalDateTime.now();

		/* 組成年月日時分秒 */
		int[] curTimeAry = new int[] { now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(),
				now.getMinute(), now.getSecond() };
		StringBuilder strBuild = new StringBuilder();
		for (int i = 0; i < curTimeAry.length; i++) {
			strBuild.append(curTimeAry[i]);
		}
		String curTime = strBuild.toString();
		return curTime;
	}

	public String getUpdatedDate(String updatedAt) {
		String updatedDate = "";
		for (int i = 0; i < 11; i++) {
			char ignoreChar = updatedAt.charAt(i);
			if (i == 4) {
				updatedDate += "年";
			} else if (i == 7) {
				updatedDate += "月";
			} else if (i == 10) {
				updatedDate += "日";
			} else {
				updatedDate += updatedAt.charAt(i);
			}
		}
		return updatedDate;
	}
}
