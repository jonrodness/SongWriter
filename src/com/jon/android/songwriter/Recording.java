package com.jon.android.songwriter;

import java.util.Date;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Recording {
	
	private static final String TAG = "Recording";
	
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_DATE = "date";
	private static final String JSON_FNAME = "filename";
	
	private Date mDate;
	private String mTitle;
	private UUID mId;
	private String mFileName = null;
	
	public Recording() {
		mId = UUID.randomUUID();
		mDate = new Date();
	}
	
	public Recording(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		mDate = new Date(json.getLong(JSON_DATE));
		mTitle = json.getString(JSON_TITLE);
		mFileName = json.getString(JSON_FNAME);
		
		Log.d(TAG, "JSON to Recording successful");		
	}
	
	public String getFileName() {
		return mFileName;
	}
	
	public void setFileName(String fName) {
		mFileName = fName;
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

	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		if (json.has(JSON_TITLE)) {
			json.put(JSON_TITLE, mTitle);
		}
		json.put(JSON_FNAME, mFileName);
		json.put(JSON_ID, mId.toString());
		json.put(JSON_DATE, mDate.getTime());
		
		Log.d(TAG, "Recording to JSON successful");		

		return json;
	}

}
