		Calendar calendar = Calendar.getInstance();
             int  day	  = calendar.get(Calendar.DAY_OF_MONTH);
             int  month   = calendar.get(Calendar.MONTH);
             int  year 	  = calendar.get(Calendar.YEAR);
             int  hour 	  = calendar.get(Calendar.HOUR_OF_DAY);
             int  minute  = calendar.get(Calendar.MINUTE);
			 
	------------------------------------
	//DatePicker
	{
		DatePickerDialog dp = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yr, int m, int d) {

                    tv3.setText(d+"-"+(m+1)+"-"+yr);
                   

                }
            },year,month,day);

            calendar.set(year,month,day);
            dp.getDatePicker().setMinDate(calendar.getTimeInMillis());
            dp.show();
	}
	------------------------------------
	//TimePicker
	{
	TimePickerDialog tp = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {

                    if (i>=12)
                    {
                        if (i>12)
                        {
                        int hou = (i%12);
                        tv4.setText(hou+":"+i1+"pm");
                    }
                    else {
                            tv4.setText(i+":"+i1+"pm");

                        }

                    }
                    else {if (i==00)
                    {
                        tv4.setText("12:"+i1+"am");
                    }
                    else
                        tv4.setText(i+":"+i1+"am");
                    }


                }
            },hour,minute,false);

            tp.show();
	}