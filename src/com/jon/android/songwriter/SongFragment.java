package com.jon.android.songwriter;

import java.util.Date;
import java.util.UUID;

import com.jon.android.songwriter.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SongFragment extends Fragment {
	
	public static final String 
	EXTRA_SONG_ID = "com.jon.android.songwriter.song_id";
			
	private static final String TAG = "SongFragment";
	private Song mSong;
	private EditText mTitleField;
	private Button mNotesButton;
	private Button mRecordingsButton;
	private TextView mDateText;
	private static final int REQUEST_DATE = 0;
	
	private void updateDate() {
		mDateText.setText(android.text.format.DateFormat
		.format("MMM dd, yyyy hh:mma", mSong.getDate()));
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);		
		UUID songId = (UUID) getArguments().getSerializable(EXTRA_SONG_ID);
		mSong = SongLab.get(getActivity()).getSongs(songId);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu, inflater);
	    inflater.inflate(R.menu.fragment_song, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		case R.id.menu_item_delete_song:
			SongLab.get(getActivity()).deleteSong(mSong);
			Log.d(TAG, "Song deleted");
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
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
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_song, parent, false);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		mTitleField = (EditText) v.findViewById(R.id.song_title);
		mTitleField.setText(mSong.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence c, int start, int before, int count) {
				mSong.setTitle(c.toString());
			}
			
			public void beforeTextChanged(CharSequence c, int start, int count, int after) {}
			
			public void afterTextChanged(Editable c) {}
		});
		
		mDateText = (TextView) v.findViewById(R.id.song_date);
		updateDate();
		
		
		mNotesButton = (Button) v.findViewById(R.id.new_note_button);
		mNotesButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(getActivity(), NoteListActivity.class);
				i.putExtra(SongFragment.EXTRA_SONG_ID, mSong.getId());
				startActivity(i);			
			}
		});
		
		mRecordingsButton = (Button) v.findViewById(R.id.new_recording_button);
		mRecordingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), RecordingListActivity.class);
				i.putExtra(SongFragment.EXTRA_SONG_ID, mSong.getId());
				startActivity(i);				
			}
		});
		return v;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		SongLab.get(getActivity()).saveSongs();
	}
	
	public static SongFragment newInstance(UUID songId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_SONG_ID, songId);
		
		SongFragment fragment = new SongFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
}
