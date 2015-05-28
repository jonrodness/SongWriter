package com.jon.android.songwriter;

import java.util.Date;

import com.jon.android.songwriter.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ChooseFragment extends DialogFragment {
	
	public static final String EXTRA_DATE = "com.jon.android.songwriter.date";
	
	private Button mDateButton;
	private Button mTimeButton;
	private Button mDateShown;
	private Date mDate;
	private static final String DIALOG_DATE = "date";
	private static final int REQUEST_DATE = 0;
	
	
	
	private void sendResult(int resultCode) {
		if (getTargetFragment() == null)
			return;
		
		Intent i = new Intent();
		i.putExtra(EXTRA_DATE, mDate);
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
		FragmentManager fm = getFragmentManager();
		fm.beginTransaction().detach(this).commit();
	}
	
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		if (requestCode == REQUEST_DATE) {
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mDate = date;
			sendResult(Activity.RESULT_OK);
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
		
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_choose, null);
		
		mDateButton = (Button) v.findViewById(R.id.date_button);
		mDateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mDate);
				dialog.setTargetFragment(ChooseFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			}
		});
		
		mTimeButton = (Button) v.findViewById(R.id.time_button);
		mTimeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				TimePickerFragment dialog = TimePickerFragment.newInstance(mDate);
				dialog.setTargetFragment(ChooseFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);				
			}
		});
		
		return new AlertDialog.Builder(getActivity())
		.setView(v)
		.setTitle(R.string.choose_title)
		.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		}).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendResult(Activity.RESULT_OK);
			}
		}).create();
	}
	
	public static ChooseFragment newInstance(Date date) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DATE, date);
		
		ChooseFragment fragment = new ChooseFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
}
