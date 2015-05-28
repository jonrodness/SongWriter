package com.jon.android.songwriter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.jon.android.songwriter.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;


public class TimePickerFragment extends DialogFragment {
	
	public static final String EXTRA_DATE = "com.jon.android.songwriter.date";
	private Date mDate; 
	
	private void sendResult(int resultCode) {
		if (getTargetFragment() == null)
			return;
		
		Intent i = new Intent();
		i.putExtra(EXTRA_DATE, mDate);
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}
		
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
		
		// Create a Calendar to get the year, month, and day
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
				
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
		
		TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_time_timePicker);
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(mDate);
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				
				GregorianCalendar gCal = new GregorianCalendar(year, month, day);
				gCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
				gCal.set(Calendar.MINUTE, minute);
				
				mDate = gCal.getTime();
				// Update argument to preserve selected value on rotation 
				getArguments().putSerializable(EXTRA_DATE, mDate);				
			}
		});
		
		return new AlertDialog.Builder(getActivity())
		.setView(v)
		.setTitle(R.string.date_picker_title)
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendResult(Activity.RESULT_OK);
			}
		}).create();
	} 
		 
	public static TimePickerFragment newInstance (Date date) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DATE, date);
		
		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
}
