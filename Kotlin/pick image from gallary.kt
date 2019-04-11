 val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
 startActivityForResult(i, 1)
						
----------------------------------------------------------------------------------------------

 override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            picturePath1 = getImagePath(imageUri!!)
            Glide.with(this@AddNewsActivity).load(picturePath1).into(iv_addNewsActivity_photo1)
        }
	}
	
---------------------------------------------------------------------------------------------


    fun getImagePath(imageUri: Uri): String? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        // TRY getActivity() as well if not work
        val cursor = contentResolver.query(imageUri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val picturePath = cursor?.getString(columnIndex!!)
        cursor?.close()
        return picturePath
    }
