package com.jon.android.songwriter;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateFormat;


public class Song {
	
	private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_FAVOURITE = "favourite";
    private static final String JSON_DATE = "date";
    private static final String JSON_NOTES = "notes";
    private static final String JSON_RECORDINGS = "recordings";

	
	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mFavourite = false;
	private ArrayList<Note> mNotes;
	private ArrayList<Recording> mRecordings;
	
	public Song() {
		mId = UUID.randomUUID();
		mDate = new Date();
		mNotes = new ArrayList<Note>();
		mRecordings = new ArrayList<Recording>();
	}
	
	public Song(JSONObject json) throws JSONException {
        JSONArray notes = new JSONArray();
        JSONArray recordings = new JSONArray();
		
		mId = UUID.fromString(json.getString(JSON_ID));
        mTitle = json.getString(JSON_TITLE);
        mFavourite = json.getBoolean(JSON_FAVOURITE);
        mDate = new Date(json.getLong(JSON_DATE));
        
        mNotes = new ArrayList<Note>();
        
        if (json.has(JSON_NOTES)) {
        	notes = json.getJSONArray(JSON_NOTES);
            for (int i = 0; i < notes.length(); i++) {
            	Note note = new Note (notes.getJSONObject(i));
            	mNotes.add(note);
            }
        }
        
        
        mRecordings = new ArrayList<Recording>();
        
        if (json.has(JSON_RECORDINGS)){
	        recordings = json.getJSONArray(JSON_RECORDINGS);
	        for (int i = 0; i < recordings.length(); i++) {
	        	Recording rec = new Recording (recordings.getJSONObject(i));
	        	mRecordings.add(rec);
	        }
        }
    }
	
	public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        JSONArray notes = new JSONArray();
        JSONArray recordings = new JSONArray();
        
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_FAVOURITE, mFavourite);
        json.put(JSON_DATE, mDate.getTime());
        
        for (Note note:mNotes) {
        	notes.put(note.toJSON());
            json.put(JSON_NOTES, notes);
        }
        
        for (Recording rec:mRecordings) {
        	recordings.put(rec.toJSON());
        	json.put(JSON_RECORDINGS, recordings);
        }
        
        return json;
    }
	
	public void addRecording(Recording recording) {
		mRecordings.add(recording);
	}
	
	public void deleteRecording (Recording recording) {
		File targetFile = new File (recording.getFileName());
		targetFile.delete();
		mRecordings.remove(recording);
	}
	
	public ArrayList<Recording> getRecordings() {
		return mRecordings;
	}
	
	public Recording getRecording(UUID recordingId) { // test
		Recording mRecording = null;
		for (int i = 0; i < mRecordings.size(); i++) {
			if (mRecordings.get(i).getId().equals(recordingId)) {
				mRecording = mRecordings.get(i);
			}
		}
		return mRecording;
	}
	
	public void addNote(Note note) {
		mNotes.add(note);
	}
	
	public void deleteNote (Note note) {
		mNotes.remove(note);
	}
	
	public Note getNote(UUID noteId) { // test
		Note mNote = null;
		for (int i = 0; i < mNotes.size(); i++) {
			if (mNotes.get(i).getId().equals(noteId)) {
				mNote = mNotes.get(i);
			}
		}
		return mNote;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public boolean isFavourite() {
		return mFavourite;
	}

	public void setFavourited(boolean favourite) {
		mFavourite = favourite;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public UUID getId() {
		return mId;
	}
	
	public ArrayList<Note> getNotes() {
		return mNotes;
	}
	
	@Override
	public String toString() {
		return mTitle;
	}
}
