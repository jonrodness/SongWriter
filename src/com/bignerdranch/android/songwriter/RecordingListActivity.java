package com.bignerdranch.android.songwriter;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RecordingListActivity extends Activity {
	
	//private ArrayList<Song> mSongs;
	private Song mSong;
	private EditText mTitleField;
	private TextView mDateText;
	private Button mNewRecordingButton;
	private ArrayList<Recording> mRecordings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.recordings_title);
		
		//mSongs = SongLab.get(this).getSongs();
		UUID songId = (UUID) getIntent().getSerializableExtra(SongFragment.EXTRA_SONG_ID);
		mSong = SongLab.get(this).getSongs(songId);
		mRecordings = mSong.getRecordings();
		
		RecordingAdapter adapter = new RecordingAdapter(mRecordings);
		setListAdapter(adapter);
		
		setContentView(R.layout.activity_recording_list);	
		setupMNewRecordingButton();
	}

	private void setupMNewRecordingButton() {
		mNewRecordingButton = (Button) findViewById(R.id.new_note_button);
		mNewRecordingButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(RecordingListActivity.this, NoteListActivity.class);
				i.putExtra(SongFragment.EXTRA_SONG_ID, mSong.getId());
				startActivity(i);		
			}
		});		
	}

	private class RecordingAdapter extends ArrayAdapter<Recording> {
		public RecordingAdapter(ArrayList<Recording> recordings) {
			super (RecordingListActivity.this, 0, recordings);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = RecordingListActivity.this.getLayoutInflater().inflate(R.layout.list_item_recording, null);
			}
			
			Recording r = getItem(position);
			
			TextView titleTextView = (TextView)convertView.findViewById(R.id.recording_list_item_titleTextView);
			if (r.getTitle() != null) {
				titleTextView.setText(r.getTitle());
			} else {
				titleTextView.setText(R.string.untitled_recording);
			}
			TextView dateTextView = (TextView) convertView.findViewById(R.id.recording_list_item_dateTextView);
			dateTextView.setText(android.text.format.DateFormat.format("MMM dd, yyyy hh:mma", r.getDate()));
			
			return convertView;		
		}
		
	}

}
