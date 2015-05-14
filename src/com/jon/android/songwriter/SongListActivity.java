package com.jon.android.songwriter;

import android.support.v4.app.Fragment;

public class SongListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new SongListFragment();
	}
}
