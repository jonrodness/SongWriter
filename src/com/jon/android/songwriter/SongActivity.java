package com.jon.android.songwriter;

import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class SongActivity extends FragmentActivity {
	public static final String 
	EXTRA_SONG_ID = "com.jon.android.songwriter.song_id";
			
	private static final String TAG = "SongFragment";
	private Song mSong;
	private EditText mTitleField;
	private Button mNotesButton;
	private Button mRecordingsButton;
	private TextView mDateText;
	private RadioButton mRecordingsRadio;
	private RadioButton mNotesRadio;
	private FrameLayout mFrameLayout;
	private static final String DIALOG_DATE = "date";
	private static final int REQUEST_DATE = 0;
	
	private void updateDate() {
		mDateText.setText(android.text.format.DateFormat
		.format("MMM dd, yyyy hh:mma", mSong.getDate()));
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
	@Override
	public void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_song); //implement
		
		ActionBar actionBar = getActionBar(); //added
		actionBar.setDisplayHomeAsUpEnabled(true); //added

		//setHasOptionsMenu(true);		
		final UUID songId = (UUID) getIntent().getSerializableExtra(EXTRA_SONG_ID); //changed
		mSong = SongLab.get(this).getSongs(songId); // changed
		
		if (mSong.getTitle() != null) {
			setTitle(mSong.getTitle());
		}
		else {
			setTitle(R.string.untitled_song);
		}
		
		
		mTitleField = (EditText) findViewById(R.id.song_title);
		mTitleField.setText(mSong.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence c, int start, int before, int count) {
				mSong.setTitle(c.toString());
			}
			
			public void beforeTextChanged(CharSequence c, int start, int count, int after) {}
			
			public void afterTextChanged(Editable c) {}
		});
		
		mDateText = (TextView) findViewById(R.id.song_date);
		updateDate();
		
		
		mNotesButton = (Button) findViewById(R.id.new_note_button);
		mNotesButton.setOnClickListener(new View.OnClickListener() {
		
		// THIS MUST CHANGE WHEN TOGGLE IS ADDED
			@Override
			public void onClick(View v) {
				
				Note n = new Note();
				n.setDate(new Date());
				mSong.addNote(n);
				Intent i = new Intent(SongActivity.this, NotePagerActivity.class);
				i.putExtra(SongFragment.EXTRA_SONG_ID, mSong.getId());
				i.putExtra(NoteFragment.EXTRA_NOTE_ID, n.getId());
				startActivity(i);
				
				
//				Intent i = new Intent(SongActivity.this, NoteListActivity.class);
//				i.putExtra(SongFragment.EXTRA_SONG_ID, mSong.getId());
//				startActivity(i);			
			}
		});
		
		mFrameLayout = (FrameLayout) findViewById(R.id.song_frame);
				
		mRecordingsButton = (Button) findViewById(R.id.new_recording_button);
		mRecordingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Recording r = new Recording();
				r.setDate(new Date());
				mSong.addRecording(r);
				Intent i = new Intent(SongActivity.this, RecordingActivity.class);
				i.putExtra(SongFragment.EXTRA_SONG_ID, mSong.getId());
				i.putExtra(RecordingActivity.EXTRA_RECORDING_ID, r.getId());
				startActivity(i);
				
				
//				Intent i = new Intent(SongActivity.this, RecordingListActivity.class);
//				i.putExtra(SongFragment.EXTRA_SONG_ID, mSong.getId());
//				startActivity(i);				
			}
		});
		
		mRecordingsRadio = (RadioButton) findViewById(R.id.radio_recordings);
		mRecordingsRadio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment recFragment = RecordingListFragment.newInstance(songId);
				
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction transaction = fm.beginTransaction();
				transaction.replace(SongActivity.this.findViewById(R.id.song_frame).getId(), recFragment);
				transaction.commit();
				
//				Intent i = new Intent(getActivity(), RecordingListActivity.class);
//				i.putExtra(SongFragment.EXTRA_SONG_ID, mSong.getId());
//				startActivity(i);				
			}
		});
		
		mNotesRadio = (RadioButton) findViewById(R.id.radio_notes);
		mNotesRadio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment noteFragment = NoteListFragment.newInstance(songId);
				
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction transaction = fm.beginTransaction();
				transaction.replace(SongActivity.this.findViewById(R.id.song_frame).getId(), noteFragment);
				transaction.commit();
				
//				Intent i = new Intent(getActivity(), RecordingListActivity.class);
//				i.putExtra(SongFragment.EXTRA_SONG_ID, mSong.getId());
//				startActivity(i);				
			}
		});
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
			mNotesRadio.setChecked(true);
			mNotesRadio.callOnClick();
		}
	}
	
	// changed
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_song, menu); //implement
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(this) != null) {
				NavUtils.navigateUpFromSameTask(this);
			}
			return true;
		case R.id.menu_item_delete_song:
			SongLab.get(this).deleteSong(mSong);
			Log.d(TAG, "Song deleted");
			if (NavUtils.getParentActivityName(this) != null) {
				NavUtils.navigateUpFromSameTask(this);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	// Only used for DatePicker
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode != Activity.RESULT_OK) return;
		if (requestCode == REQUEST_DATE) {
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

			mSong.setDate(date);
			updateDate();
		}
	}
	
//	@TargetApi(11)
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
//			Bundle savedInstanceState) {
//		View v = inflater.inflate(R.layout.fragment_song, parent, false);
//		
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//			if (NavUtils.getParentActivityName(getActivity()) != null) {
//				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
//			}
//		}
//		
//		mTitleField = (EditText) v.findViewById(R.id.song_title);
//		mTitleField.setText(mSong.getTitle());
//		mTitleField.addTextChangedListener(new TextWatcher() {
//			
//			public void onTextChanged(CharSequence c, int start, int before, int count) {
//				mSong.setTitle(c.toString());
//			}
//			
//			public void beforeTextChanged(CharSequence c, int start, int count, int after) {}
//			
//			public void afterTextChanged(Editable c) {}
//		});
//		
//		mDateText = (TextView) v.findViewById(R.id.song_date);
//		updateDate();
//		
//		
//		mNotesButton = (Button) v.findViewById(R.id.new_note_button);
//		mNotesButton.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				Intent i = new Intent(getActivity(), NoteListActivity.class);
//				i.putExtra(SongFragment.EXTRA_SONG_ID, mSong.getId());
//				startActivity(i);			
//			}
//		});
//		
//		mRecordingsButton = (Button) v.findViewById(R.id.new_recording_button);
//		mRecordingsButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent i = new Intent(getActivity(), RecordingListActivity.class);
//				i.putExtra(SongFragment.EXTRA_SONG_ID, mSong.getId());
//				startActivity(i);				
//			}
//		});
//		return v;
//	}
	
	@Override
	public void onPause() {
		super.onPause();
		SongLab.get(this).saveSongs();
	}
	
	// I think this is uneccesary
	public static SongFragment newInstance(UUID songId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_SONG_ID, songId);
		
		SongFragment fragment = new SongFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
}
