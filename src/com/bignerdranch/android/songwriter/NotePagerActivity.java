package com.bignerdranch.android.songwriter;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class NotePagerActivity extends FragmentActivity {
	
	private ViewPager mViewPager;
	private ArrayList<Note> mNotes;
	private ArrayList<Song> mSongs;
	private Song mSong;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		UUID songId = (UUID)getIntent().getSerializableExtra(SongFragment.EXTRA_SONG_ID);
		mSongs = SongLab.get(this).getSongs();
		
		for (int i = 0; i < mSongs.size(); i++) {
			if (mSongs.get(i).getId().equals(songId)) {
				mSong = mSongs.get(i);
				break;
			}
			mSong = null;			
		}
		
		mNotes = mSong.getNotes();
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

			@Override
			public Fragment getItem(int pos) {
				Note note = mNotes.get(pos);
				// is note id necessary?
				return NoteFragment.newInstance(mSong.getId(), note.getId());
			}

			@Override
			public int getCount() {
				return mNotes.size();
			}
		});
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				Note note = mNotes.get(pos);
				if(note.getTitle() != null) {
					setTitle(note.getTitle());
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
		
		UUID noteId = (UUID)getIntent().getSerializableExtra(NoteFragment.EXTRA_NOTE_ID);
		for (int i = 0; i < mNotes.size(); i++) {
			if (mNotes.get(i).getId().equals(noteId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}		
	}
}
