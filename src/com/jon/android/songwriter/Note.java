package com.jon.android.songwriter;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Note {
	
	private static final String TAG = "Note";
	
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_BODY = "body";
	private static final String JSON_DATE = "date";
	
	private Date mDate;
	private String mBody;
	private String mTitle;
	private UUID mId;
	
	public Note() {
		mId = UUID.randomUUID();
		mDate = new Date();
	}
	
	public Note(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		mDate = new Date(json.getLong(JSON_DATE));
		if (json.has(JSON_TITLE)) {
			mTitle = json.getString(JSON_TITLE);
		}
		if (json.has(JSON_BODY)) {
			mBody = json.getString(JSON_BODY);
		}	
		Log.d(TAG, "JSON to Note successful");		
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

	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_BODY, mBody);
		json.put(JSON_DATE, mDate.getTime());
		
		Log.d(TAG, "Note to JSON successful");
		
		return json;
	}

}
