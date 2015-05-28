package com.jon.android.songwriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;







import com.jon.android.songwriter.R;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.TextView;

public class NoteListFragment extends ListFragment {
	
	private Song mSong;
	private ArrayList<Note> mNotes;
	private ArrayList<Song> mSongs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		mSongs = SongLab.get(getActivity()).getSongs();
		UUID songId = (UUID) getArguments().getSerializable(SongFragment.EXTRA_SONG_ID);
		
		for (int i = 0; i < mSongs.size(); i++){
			if (mSongs.get(i).getId().equals(songId)){
				mSong = mSongs.get(i);
				break;
			}
			mSong = null;
		}		
		mNotes = mSong.getNotes();
		
		NoteAdapter adapter = new NoteAdapter(mNotes);
		setListAdapter(adapter);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_note_list, menu);
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_note_list, parent, false);
	
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
			
			// ActionMode.Callback methods
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.note_list_item_context, menu);
				return true;
			}
			
			public boolean onPrepareActionMode (ActionMode mode, Menu menu) {
				return false;
			}
			
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
					case R.id.menu_item_delete_note:
						NoteAdapter adapter = (NoteAdapter) getListAdapter();
						for (int i  = adapter.getCount() - 1; i >= 0; i--) {
							if (getListView().isItemChecked(i)) {
								mSong.deleteNote(adapter.getItem(i));
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
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Note n = ((NoteAdapter)getListAdapter()).getItem(position);
		
		Intent i = new Intent(getActivity(), NotePagerActivity.class);
		i.putExtra(SongFragment.EXTRA_SONG_ID, mSong.getId());
		i.putExtra(NoteFragment.EXTRA_NOTE_ID, n.getId());
		startActivity(i);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((NoteAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	public static NoteListFragment newInstance(UUID songId) {
		Bundle args = new Bundle();
		args.putSerializable(SongFragment.EXTRA_SONG_ID, songId);
		
		NoteListFragment fragment = new NoteListFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	private class NoteAdapter extends ArrayAdapter<Note> {
		public NoteAdapter(ArrayList<Note> notes) {
			super (getActivity(), 0, notes);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) { 
			if (convertView == null) { 
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_note, null);
			}
			
			Note n = getItem(position);
			
			TextView titleTextView = (TextView)convertView.findViewById(R.id.note_list_item_titleTextView);
			if (n.getTitle() != null) {
				titleTextView.setText(n.getTitle());
			} else
				titleTextView.setText(R.string.untitled_note);
			TextView dateTextView = (TextView)convertView.findViewById(R.id.note_list_item_dateTextView);
			dateTextView.setText(android.text.format.DateFormat.format("MMM dd,  yyyy hh:mma", n.getDate()));
			
			return convertView;
		}
	}

}
