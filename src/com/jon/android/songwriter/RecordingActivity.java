package com.jon.android.songwriter;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.jon.android.songwriter.R;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RecordingActivity extends Activity {
	
	public static final String EXTRA_RECORDING_ID = "com.jon.android.songwriter.recordingactivity";
	private static final String TAG = "RecordingActivity";
	
	private MediaRecorder mRecorder;
	private boolean mStartRecording;
	private Button mRecordButton;
	private EditText mTitleField;
	
	private Song mSong;
	
	private MediaPlayer mPlayer;
	private boolean mStartPlaying;
	private Button mPlayButton;
	
	private Recording mRecording;
	private static String mFileName = null;
	private UUID recordingId;
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recording);
		
		setTitle(R.string.new_recording);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		UUID songId = (UUID) getIntent().getSerializableExtra(SongFragment.EXTRA_SONG_ID);
		mSong = SongLab.get(this).getSongs(songId);
		UUID recordingId = (UUID) getIntent().getSerializableExtra(EXTRA_RECORDING_ID);
		mRecording = mSong.getRecording(recordingId);
		
		mFileName = createFilename();
		mRecording.setFileName(mFileName);
		mStartRecording = true;
		mStartPlaying = true;
				
		mTitleField = (EditText)findViewById(R.id.recording_title);
		mTitleField.setText(mRecording.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mRecording.setTitle(s.toString());
			}
		});
		
		mRecordButton = (Button)findViewById(R.id.record_button);
		mRecordButton.setText("Start Recording");
		mRecordButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_notification_overlay, 0, 0);
		mRecordButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mStartPlaying) {
					onRecord(mStartRecording);
					if (mStartRecording) {
						mRecordButton.setText("Stop Recording");
						mRecordButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_media_stop, 0, 0);
					}
					else {
						mRecordButton.setText("Start Recording");
						mRecordButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_notification_overlay, 0, 0);
					}
					mStartRecording = !mStartRecording;
				}
				else {
					Toast toast = Toast.makeText(RecordingActivity.this, R.string.record_not_allowed, Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
		
		mPlayButton = (Button)findViewById(R.id.play_button);
		mPlayButton.setText("Start Playing");
		mPlayButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_media_play, 0, 0);
		mPlayButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mStartRecording) {
					onPlay(mStartPlaying);
					if (mStartPlaying) {
						mPlayButton.setText("Stop Playing");
						mPlayButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_media_stop, 0, 0);
					}
					else {
						mPlayButton.setText("Start Playing");
						mPlayButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_media_play, 0, 0);
					}
					mStartPlaying = !mStartPlaying;
				}
				else {
					Toast toast = Toast.makeText(RecordingActivity.this, R.string.play_not_allowed, Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_recording, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				if (NavUtils.getParentActivityName(this) != null) {
					NavUtils.navigateUpFromSameTask(this);
				}
				return true;
			case R.id.menu_item_delete_recording:
				mSong.deleteRecording(mRecording);
				Log.d(TAG, "Recording deleted");
				if (NavUtils.getParentActivityName(this) != null) {				
					NavUtils.navigateUpFromSameTask(this);
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private String createFilename() { // test
		String fName;
		int hashId = mRecording.getId().hashCode();
		if (hashId < 0) {
			hashId *= -1;
		}
		fName = Environment.getExternalStorageDirectory().getAbsolutePath();
		fName += "/" + hashId + ".3gp";
		return fName;
	}

	private void onRecord(boolean startRecording) {
		if (startRecording) {
			startRecording();
		}
		else {
			stopRecording();
		}
	}

	private void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}

	private void startRecording() {
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e(TAG, "prepare() record failed");
		}	
		mRecorder.start();
	}
	
	protected void onPlay(boolean startPlaying) {
		if (startPlaying) {
			startPlaying();
		}
		else {
			stopPlaying();
		}
	}

	private void startPlaying() {
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(mFileName);
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
	public void onPause() {
		super.onPause();
		SongLab.get(this).saveSongs();
	}

}
