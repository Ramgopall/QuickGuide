	// get AudioManager service.
AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

   //Type of ringer Mode:
int ringer = audioManager.getRingerMode();

		if (ringer==audioManager.RINGER_MODE_NORMAL)
        {}
        else if (ringer==audioManager.RINGER_MODE_SILENT)
        {}
        else if (ringer==audioManager.RINGER_MODE_VIBRATE)
        {}
		
		
	//Change Ringer Mode like this:	
audioManager.setRingerMode(am.RINGER_MODE_SILENT);