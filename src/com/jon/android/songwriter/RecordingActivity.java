package com.jon.android.songwriter;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.jon.android.songwriter.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RecordingActivity extends Activity {
	
	public static final String EXTRA_RECORDING_ID = "com.jon.android.songwriter.recordingactivity";
	private static final String TAG = "RecordingActivity";
	
	private MediaRecorder mRecorder;
	private boolean mStartRecording;
	private Button mRecordButton;

	private Song mSong;
	
	private MediaPlayer mPlayer;
	private boolean mStartPlaying;
	private Button mPlayButton;
	
	private Recording mRecording;
	private static String mFileName = null;
	private UUID recordingId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recording);
		
		UUID songId = (UUID) getIntent().getSerializableExtra(SongFragment.EXTRA_SONG_ID);
		mSong = SongLab.get(this).getSongs(songId);
		UUID recordingId = (UUID) getIntent().getSerializableExtra(EXTRA_RECORDING_ID);
		mRecording = mSong.getRecording(recordingId);
		
		mFileName = createFilename();
		mRecording.setFileName(mFileName);
		mStartRecording = true;
		mStartPlaying = true;
		
		setContentView(R.layout.activity_recording);
		
		mRecordButton = (Button)findViewById(R.id.record_button);
		mRecordButton.setText("Start Recording");
		mRecordButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onRecord(mStartRecording);
				if (mStartRecording) {
					mRecordButton.setText("Stop Recording");
				}
				else {
					mRecordButton.setText("Start Recording");
				}
				mStartRecording = !mStartRecording;
			}
		});
		
		mPlayButton = (Button)findViewById(R.id.play_button);
		mPlayButton.setText("Start Playing");
		mPlayButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onPlay(mStartPlaying); 
				if (mStartPlaying) {
					mPlayButton.setText("Stop Playing");
				}
				else {
					mPlayButton.setText("Start Playing");
				}
				mStartPlaying = !mStartPlaying;
				}
			});
	}

	private String createFilename() {
		String fName;
		recordingId = mRecording.getId();
		String hashId = Integer.toString(recordingId.hashCode());
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


}
