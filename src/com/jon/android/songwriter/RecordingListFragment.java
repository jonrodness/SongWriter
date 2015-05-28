package com.jon.android.songwriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import com.jon.android.songwriter.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.MultiChoiceModeListener;

public class RecordingListFragment extends ListFragment {
	
	private static final String TAG = "RecordingListFragment";
	private Song mSong;
	private ImageView mPlayStatus;
	private ArrayList<Recording> mRecordings;
	private boolean mStartPlaying;
	private MediaPlayer mPlayer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		mStartPlaying = true;
		
		UUID songId = (UUID) getArguments().getSerializable(SongFragment.EXTRA_SONG_ID);
		mSong = SongLab.get(getActivity()).getSongs(songId);
		mRecordings = mSong.getRecordings();
			
		RecordingAdapter adapter = new RecordingAdapter(mRecordings);
		setListAdapter(adapter);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_recording_list, menu);
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_recording_list, parent, false);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
				
		ListView listView = (ListView) v.findViewById(android.R.id.list);
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
			// Use floating context menus on Froyo and Gingerbread
			registerForContextMenu(listView);
		} else {
			// Use contextual action bar on Honeycomb and higher
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {	
				
				public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {}
				
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.recording_list_item_context, menu);
					return true;
				}
				
				public boolean onPrepareActionMode (ActionMode mode, Menu menu) {
					return false;
					// Required, but not used in this implementation
				}
				
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch (item.getItemId()) {
						case R.id.menu_item_delete_recording:
							RecordingAdapter adapter = (RecordingAdapter) getListAdapter();
							for (int i  = adapter.getCount() - 1; i >= 0; i--) {
								if (getListView().isItemChecked(i)) {
									mSong.deleteRecording(adapter.getItem(i));
								}
							}
							mode.finish();
							adapter.notifyDataSetChanged();
							return true;
						default:
							return false;
					}
				}
				public void onDestroyActionMode(ActionMode mode) {}
			});
		}
		return v;
	}

	private class RecordingAdapter extends ArrayAdapter<Recording> {
		public RecordingAdapter(ArrayList<Recording> recordings) {
			super (getActivity(), 0, recordings);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_recording, null);
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
			mPlayStatus = (ImageView)convertView.findViewById(R.id.recording_list_item_play_status);
			mPlayStatus.setImageResource(R.drawable.ic_media_play);

			return convertView;		
		}	
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Recording r = ((RecordingAdapter)getListAdapter()).getItem(position);
		String fName = r.getFileName();
		
		onPlay(mStartPlaying, fName); 
		if (mStartPlaying) {
			mPlayStatus = (ImageView) v.findViewById(R.id.recording_list_item_play_status);
			mPlayStatus.setImageResource(R.drawable.ic_media_stop);
		}
		else {
			mPlayStatus.setImageResource(R.drawable.ic_media_play);
		}
		mStartPlaying = !mStartPlaying;
	}
	
	private void onPlay(boolean startPlaying, String fName) {
		if (startPlaying) {
			Log.e(TAG, fName);

			startPlaying(fName);
		}
		else {
			stopPlaying();
		}		
	}

	private void startPlaying(String fName) {
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(fName);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			Log.e(TAG, "prepare() play failed");
		}
	}
	
	private void stopPlaying() {
		mPlayer.release();
		mPlayer = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		((RecordingAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	public static RecordingListFragment newInstance(UUID recordingId) {
		Bundle args = new Bundle();
		args.putSerializable(SongFragment.EXTRA_SONG_ID, recordingId);
		
		RecordingListFragment fragment = new RecordingListFragment();
		fragment.setArguments(args);
		return fragment;
	}

}
