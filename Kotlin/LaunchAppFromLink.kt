In Manifest:-
			<activity android:name=".MainActivity">
			<intent-filter>
                <action android:name="android.intent.action.VIEW"/>

                <category android:name="android.intent.category.DEFAULT"/>
                <category android:name="android.intent.category.BROWSABLE"/>

                <data   android:host="www.google.com"
                        android:scheme="http" />
            </intent-filter>
			</activity>
			
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
In MainActivity:-
		
	class MainActivity : AppCompatActivity() {
  
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
			
		val data = intent.data
        if (data != null) {
            Toast.makeText(this, "" + data, Toast.LENGTH_LONG).show()
        }
	}