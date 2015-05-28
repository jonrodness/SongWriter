package com.jon.android.songwriter;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class SongLab {
	private static final String TAG = "SongLab";
	private static final String FILENAME = "songs.json";
	
	private ArrayList<Song> mSongs;
	private SongWriterJSONSerializer mSerializer;
	
	private static SongLab sSongLab;
	private Context mAppContext;
		
	private SongLab(Context appContext) {
		mAppContext = appContext;
		mSerializer = new SongWriterJSONSerializer(mAppContext, FILENAME);
		
		try {
			mSongs = mSerializer.loadSongs();
            Log.d(TAG, "songs loaded from file");
		} catch (Exception e) {
			mSongs = new ArrayList<Song>();
			Log.e(TAG, "Error loading  songs: ", e);
		}
	}
		
	public static SongLab get(Context c) {
		if (sSongLab == null){
			sSongLab = new SongLab(c.getApplicationContext());
		}
		return sSongLab;
	}
	
	public ArrayList<Song> getSongs() {
		return mSongs;
	}
	
	public void addSong(Song s) {
		mSongs.add(0, s);
	}
	
	public void deleteSong(Song s) {
		mSongs.remove(s);
	}
	
	public boolean saveSongs() {
        try {
            mSerializer.saveSongs(mSongs);
            Log.d(TAG, "songs saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving songs: ", e);
            return false;
        }
    }
	
	public Song getSongs(UUID id) {
		for (Song s : mSongs) {
			if (s.getId().equals(id))
				return s;
		}
		return null;
	}
	
}
