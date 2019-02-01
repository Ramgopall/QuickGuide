
    // Insert krne ke liye.
    var preferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
	preferences = getSharedPreferences("planetBazar", MODE_PRIVATE)
	editor = preferences?.edit()
    editor?.putString("loggedIn", "yes")
    editor?.apply()
 
 
 
    //Get krne ke liye
    val preferences = getSharedPreferences("planetBazar", MODE_PRIVATE)
    val id = preferences.getString("loggedIn","null")