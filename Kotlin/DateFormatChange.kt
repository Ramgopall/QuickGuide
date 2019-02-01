		
		//input: 11/Nov/2018
		String date= "11/Nov/2018";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MMM/yyyy");
       
	   Date newDate= null;
        try {
            newDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
		
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = simpleDateFormat.format(newDate);
		
		//output: 11-11-2018