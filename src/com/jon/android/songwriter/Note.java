package com.jon.android.songwriter;

import java.util.Date;
import java.util.UUID;

public class Note {
	
	private Date mDate;
	private String mBody;
	private String mTitle;
	private UUID mId;
	
	public Note() {
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

	public String getBody() {
		return mBody;
	}

	public void setBody(String content) {
		mBody = content;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

}
