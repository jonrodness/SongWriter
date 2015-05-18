package com.jon.android.songwriter;

import java.util.Date;
import java.util.UUID;

import com.jon.android.songwriter.R;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.TextView;

public class NoteFragment extends Fragment {

	public static final String EXTRA_NOTE_ID = "com.jon.android.songwriter.notelistfragment";
	private static final String TAG = "NoteFragment";
	private Song mSong;
	private Note mNote;
	private TextView mDate;
	private EditText mTitleField;
	private EditText mBodyField;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);

		UUID songId = (UUID) getArguments().getSerializable(SongFragment.EXTRA_SONG_ID);
		mSong = SongLab.get(getActivity()).getSongs(songId);
		UUID noteId = (UUID) getArguments().getSerializable(EXTRA_NOTE_ID);
		mNote = mSong.getNote(noteId);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_note, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				if (NavUtils.getParentActivityName(getActivity()) != null) {
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			case R.id.menu_item_delete_note:
				mSong.deleteNote(mNote);
				Log.d(TAG, "Note deleted");
				if (NavUtils.getParentActivityName(getActivity()) != null) {				
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
		
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_note, parent, false);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		mDate = (TextView) v.findViewById(R.id.note_date);
		mDate.setText(android.text.format.DateFormat.format("MMM, dd, yyyy hh:mma", mNote.getDate()));
		
		mTitleField = (EditText) v.findViewById(R.id.note_title);
		mTitleField.setText(mNote.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mNote.setTitle(s.toString());
			}	
		});
		
		mBodyField = (EditText) v.findViewById(R.id.note_body);
		mBodyField.setText(mNote.getBody());
		mBodyField.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mNote.setBody(s.toString());
			}	
		});
		
		return v;
	}
	
	public static NoteFragment newInstance (UUID songId, UUID noteId) {
		NoteFragment fragment = new NoteFragment(); 
		Bundle args = new Bundle();
		fragment.setArguments(args);
		args.putSerializable(SongFragment.EXTRA_SONG_ID, songId);
		args.putSerializable(NoteFragment.EXTRA_NOTE_ID, noteId);
				
		return fragment;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		SongLab.get(getActivity()).saveSongs();
	}

}
