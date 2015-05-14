package com.jon.android.songwriter;

import java.util.UUID;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class RecordingListActivity extends SingleFragmentActivity {
	
	@Override
	protected Fragment createFragment() {
		UUID songId = (UUID) getIntent().getSerializableExtra(SongFragment.EXTRA_SONG_ID);
		RecordingListFragment fragment = RecordingListFragment.newInstance(songId);
		return fragment;
	}

}
