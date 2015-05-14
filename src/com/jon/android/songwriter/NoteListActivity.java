package com.jon.android.songwriter;

import java.util.UUID;

import android.support.v4.app.Fragment;

public class NoteListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		UUID songId = (UUID) getIntent().getSerializableExtra(SongFragment.EXTRA_SONG_ID);
		NoteListFragment fragment = NoteListFragment.newInstance(songId);
		return fragment;
	}

}
