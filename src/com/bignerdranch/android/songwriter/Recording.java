package com.bignerdranch.android.songwriter;

import java.util.Date;
import java.util.UUID;

public class Recording {
	private Date mDate;
	private String mTitle;
	private UUID mId;
	
	public Recording() {
		mId = UUID.randomUUID();
		mDate = new Date();
	}
	
	public UUID getId() {
		return mId;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

}
