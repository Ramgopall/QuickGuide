
// Insert krne ke liye.
 SharedPreferences.Editor editor = getSharedPreferences("educationHub", MODE_PRIVATE).edit();
 editor.putString("id", id);
 editor.putString("name", name);
 editor.apply();
 
 
 
 //Get krne ke liye
 SharedPreferences sp = getSharedPreferences("educationHub", MODE_PRIVATE);
 String temp = sp.getString("id", "null");