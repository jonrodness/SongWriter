package com.bignerdranch.android.songwriter;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class SongPagerActivity extends FragmentActivity {
	
	private ViewPager mViewPager;
	private ArrayList<Song> mSongs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mSongs = SongLab.get(this).getSongs();
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return mSongs.size();
			}
			
			@Override
			public Fragment getItem(int pos) {
				Song song = mSongs.get(pos);
				return SongFragment.newInstance(song.getId());
			}
		});
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			public void onPageScrollStateChanged(int state) {}
			
			public void onPageScrolled(int pos, float posOffset, int poOffsetPixels) {}
			
			public void onPageSelected(int pos) {
				Song song = mSongs.get(pos);
				if(song.getTitle() != null) {
					setTitle(song.getTitle());		
				}
			}
		});
		
		
		UUID songId = (UUID)getIntent().getSerializableExtra(SongFragment.EXTRA_SONG_ID);
		for (int i = 0; i < mSongs.size(); i++){
			if (mSongs.get(i).getId().equals(songId)){
				mViewPager.setCurrentItem(i);
				break;
			}	
		}
	}

}
