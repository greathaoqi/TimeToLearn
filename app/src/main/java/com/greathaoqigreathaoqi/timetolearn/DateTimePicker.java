package com.greathaoqigreathaoqi.timetolearn;

import android.content.Context;
import android.text.format.DateFormat;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

import java.util.Calendar;

public class DateTimePicker extends FrameLayout
{
	private final NumberPicker mHourSpinner;
	private final NumberPicker mMinuteSpinner;
	private Calendar mDate;
    private int mHour,mMinute;
    private OnDateTimeChangedListener mOnDateTimeChangedListener;
    
    public DateTimePicker(Context context)
	{
    	super(context);
    	 mDate = Calendar.getInstance();
    	 
         mHour=mDate.get(Calendar.HOUR_OF_DAY);
         mMinute=mDate.get(Calendar.MINUTE);
    	 
    	 inflate(context, R.layout.datedialog, this);

    	 
    	 mHourSpinner=(NumberPicker)this.findViewById(R.id.np_hour);
    	 mHourSpinner.setMaxValue(23);
    	 mHourSpinner.setMinValue(0);
    	 mHourSpinner.setValue(mHour);
    	 mHourSpinner.setOnValueChangedListener(mOnHourChangedListener);
    	 
    	 mMinuteSpinner=(NumberPicker)this.findViewById(R.id.np_minute);
    	 mMinuteSpinner.setMaxValue(59);
    	 mMinuteSpinner.setMinValue(0);
    	 mMinuteSpinner.setValue(mMinute);
    	 mMinuteSpinner.setOnValueChangedListener(mOnMinuteChangedListener);
	}


    private NumberPicker.OnValueChangeListener mOnHourChangedListener=new OnValueChangeListener()
	{
		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal)
		{
			mHour=mHourSpinner.getValue();
			onDateTimeChanged();
		}
	};

	  private NumberPicker.OnValueChangeListener mOnMinuteChangedListener=new OnValueChangeListener()
		{
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal)
			{
				mMinute=mMinuteSpinner.getValue();
				onDateTimeChanged();
			}
		};

	
	  public interface OnDateTimeChangedListener 
	  {
	        void onDateTimeChanged(DateTimePicker view, int year, int month, int day, int hour, int minute);
	  }
	
	  public void setOnDateTimeChangedListener(OnDateTimeChangedListener callback) 
	  {
	        mOnDateTimeChangedListener = callback;
	   }
	  
	  private void onDateTimeChanged() 
	  {
	        if (mOnDateTimeChangedListener != null)
	        {
	            mOnDateTimeChangedListener.onDateTimeChanged(this, mDate.get(Calendar.YEAR),
	            		mDate.get(Calendar.MONTH), mDate.get(Calendar.DAY_OF_MONTH),mHour, mMinute);
	        }
	    }
}
