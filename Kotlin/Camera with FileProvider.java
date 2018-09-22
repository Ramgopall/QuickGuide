1st In Manifest;-
{
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

<provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="<package name>"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/path" />
        </provider>
}

------------------------------------
2nd Create a XMl Folder in Resource and add path.xml file in it and insert below code:-
{
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-path
        name="my_images"
        path="."
        />
</paths>
}

------------------------------------

3rd kotlin/java
{
	var imageFilePath:String? = null

    var compressedfilefront:File? = null

    var photoFile:File? = null
	----------
			val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (pictureIntent.resolveActivity(getPackageManager()) != null) {


                try {
                    photoFile = createImageFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                    return@setOnClickListener
                }

                val photoUri = FileProvider.getUriForFile(this, "com.clikzop.insurance", photoFile!!)
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(pictureIntent, 2)
            }
	----------
	@Throws(IOException::class)
    private fun createImageFile(): File? {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "IMG_" + timeStamp + "_"
            val storageDir =  getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val image = File.createTempFile(imageFileName, ".jpg", storageDir)
            imageFilePath = image.absolutePath
            return image

        }
	-----------
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 2 && resultCode == RESULT_OK){

            try {
					// media scanner start scanning all file so that new files will be updates in gallery apps.
				sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(compressedfilefront)))

				// show image in imageview using glide.
                Glide.with(this@DocumentActivity).load(photoFile).into(imageView_recyclerView_document)
                
				//image path
				textView_recyclerView_document.text = compressedfilefront!!.path

				
                val newFile = File(applicationContext.cacheDir,"myFile")
                newFile.createNewFile()

                val scaled = BitmapFactory.decodeFile(compressedfilefront!!.path)

				// changing image resolution
                val bitmap = Bitmap.createScaledBitmap(scaled, 1000, 720, true)
                val boos = ByteArrayOutputStream()
				// to compress image size always use jpeg hint.
                bitmap.compress(Bitmap.CompressFormat.JPEG, 45, boos)
                val byteArray = boos.toByteArray()

                val fos = FileOutputStream(newFile)
                fos.write(byteArray)
                fos.flush()
                fos.close()

                compressedfilefront = newFile
              
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
        else if (data == null) {
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
        }
    }
	
}	