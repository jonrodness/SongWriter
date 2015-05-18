package com.jon.android.songwriter;

import java.util.ArrayList;

import com.jon.android.songwriter.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class SongListFragment extends ListFragment {
	
	private static final String TAG = "SongListFragment";
	private ArrayList<Song> mSongs;
	private Button mAddSongButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		getActivity().setTitle(R.string.songs_title);
		mSongs = SongLab.get(getActivity()).getSongs();
		SongAdapter adapter = new SongAdapter(mSongs);
		setListAdapter(adapter);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu, inflater);
	    inflater.inflate(R.menu.fragment_song_list, menu);
	}
	
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menu_item_new_song:
	            Song s = new Song();
	            SongLab.get(getActivity()).addSong(s);
	            Intent i = new Intent(getActivity(), SongPagerActivity.class);
	            i.putExtra(SongFragment.EXTRA_SONG_ID, s.getId());
	            startActivityForResult(i, 0);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.song_list_item_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
	    int position = info.position;
	    SongAdapter adapter = (SongAdapter)getListAdapter();
	    Song song = adapter.getItem(position);

	    switch (item.getItemId()) {
	        case R.id.menu_item_delete_song:
	            SongLab.get(getActivity()).deleteSong(song);
	            adapter.notifyDataSetChanged();
	            return true;
	    }
	    return super.onContextItemSelected(item);
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_song_list, parent, false);
		
		mAddSongButton = (Button) v.findViewById(R.id.add_song_button);
		mAddSongButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Song s = new Song();
	            SongLab.get(getActivity()).addSong(s);
	            Intent i = new Intent(getActivity(), SongPagerActivity.class);
	            i.putExtra(SongFragment.EXTRA_SONG_ID, s.getId());
	            startActivity(i);				
			}
		});
		
		ListView listView = (ListView)v.findViewById(android.R.id.list);
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
			// Use floating context menus on Froyo and Gingerbread
			registerForContextMenu(listView);
		} else {
			// Use contextual action bar on Honeycomb and higher
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {	
				
				public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
					//Required, but not used in this implementation
				}
				
				// ActionMode.Callback methods
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.song_list_item_context, menu);
					return true;
				}
				
				public boolean onPrepareActionMode (ActionMode mode, Menu menu) {
					return false;
					// Required, but not used in this implementation
				}
				
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch (item.getItemId()) {
						case R.id.menu_item_delete_song:
							SongAdapter adapter = (SongAdapter) getListAdapter();
							SongLab songLab = SongLab.get(getActivity());
							for (int i  = adapter.getCount() - 1; i >= 0; i--) {
								if (getListView().isItemChecked(i)) {
									songLab.deleteSong(adapter.getItem(i));
								}
							}
							mode.finish();
							adapter.notifyDataSetChanged();
							return true;
						default:
							return false;
					}
				}
				
				public void onDestroyActionMode(ActionMode mode) {
				// Required, but not used in this implementation
				}
			});
		}				
		return v;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Song s = ((SongAdapter)getListAdapter()).getItem(position);
		
		// Start SongPagerActivity with this song
		Intent i = new Intent(getActivity(), SongPagerActivity.class);
		i.putExtra(SongFragment.EXTRA_SONG_ID, s.getId());
		startActivity(i);
	}
	
	private class SongAdapter extends ArrayAdapter<Song> {
		public SongAdapter(ArrayList<Song> songs) {
			super(getActivity(), 0, songs);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// If we weren't given a view, inflate one
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_song, null);
			}
			
			// Configure the view for this crime
			Song s = getItem(position);
			
			TextView titleTextView = (TextView)convertView.findViewById(R.id.song_list_item_titleTextView);
			if (s.getTitle() != null) {
				titleTextView.setText(s.getTitle());
			} else
				titleTextView.setText(R.string.untitled_song);
			TextView dateTextView = (TextView)convertView.findViewById(R.id.song_list_item_dateTextView);
			dateTextView.setText(android.text.format.DateFormat.format("MMM dd, yyyy hh:mma", s.getDate()));
			
			return convertView;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((SongAdapter)getListAdapter()).notifyDataSetChanged();
	}
	


}
