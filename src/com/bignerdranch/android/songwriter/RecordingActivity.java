package com.bignerdranch.android.songwriter;

// majority of code taken from: http://developer.android.com/guide/topics/media/audio-capture.html

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
	
	private static final String TAG = "RecordingActivity";
	
	private MediaRecorder mRecorder;
	private boolean mStartRecording;
	private Button mRecordButton;


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
		
		mRecording = new Recording();	
		createFilename();
		mStartRecording = true;
		mStartPlaying = true;
		
		setContentView(R.layout.activity_recording);
		
		mRecordButton = (Button)findViewById(R.id.record_button);
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
		
	private void createFilename() {
		recordingId = mRecording.getId();
		String hashId = Integer.toString(recordingId.hashCode());
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileName += "/" + hashId + ".3gp";
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
