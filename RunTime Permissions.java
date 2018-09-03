
    private val PERMISSION_CALLBACK_CONSTANT = 100
    var permissionsRequired = arrayOf(Manifest.permission.CALL_PHONE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
--------------------------------------------------
	
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Multiple Runtime Permission
        if (ActivityCompat.checkSelfPermission(this@MainActivity, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this@MainActivity, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, permissionsRequired[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, permissionsRequired[1])) {
                ActivityCompat.requestPermissions(this@MainActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            } else {
                ActivityCompat.requestPermissions(this@MainActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            }
        }
	}
---------------------------------------------------------

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            var allgranted = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true
                } else {
                    allgranted = false
                    break
                }
            }

            if (allgranted) {

            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, permissionsRequired[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, permissionsRequired[1])) {

                ActivityCompat.requestPermissions(this@MainActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            }
        }
    }