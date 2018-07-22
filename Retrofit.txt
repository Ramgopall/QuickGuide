implementation 'com.squareup.retrofit2:retrofit:2.3.0'
implementation 'com.squareup.retrofit2:converter-gson:2.3.0'

--------------------------------------------------------------------------------------------------------------------------------

// Api Service Type 1 POST
{**
						@FormUrlEncoded
						@POST("api/waiter_login")
						fun loginStaff(@Field("user_name") username: String,
									   @Field("password") password: String)
								: Call<Login_Model>
			
			                -------------------------------------------------------
			
				val gson = GsonBuilder().setLenient().create()
				val retrofit = Retrofit.Builder()
									   .baseUrl(Constants.BASE_URL)
									   .addConverterFactory(GsonConverterFactory.create(gson))
									   .build()
				val apiService = retrofit.create<ApiService>(ApiService::class.java)

				val call = apiService.loginStaff(username,password)

                call.enqueue(object : Callback<Login_Model> {

                override fun onResponse(call: Call<Login_Model>?, response: Response<Login_Model>?) {

                    if (response!!.body() != null && response.isSuccessful) {
//                        progressbar_login.visibility = View.GONE
//                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        if (!response.body()!!.error!!) {
                            
                        } else {
//                            progressbar_login.visibility = View.GONE
//                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                            Toast.makeText(this@MainActivity, "" + response.body()!!.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
//                        progressbar_login.visibility = View.GONE
//                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        Toast.makeText(this@MainActivity, "No Response from server!", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<Login_Model>?, t: Throwable?) {
//                    progressbar_login.visibility = View.GONE
//                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    val status = NetworkUtil.getConnectivityStatusString(this@MainActivity)
                    if (status == "Not connected to Internet") {
                        Snackbar.make(findViewById(android.R.id.content), status, Snackbar.LENGTH_LONG).show()
                    }
                    else {
                        Snackbar.make(findViewById(android.R.id.content), "Please Check your connection or try again later!", Snackbar.LENGTH_LONG).show()
                    }
                }
            })
 
 
 **}
 
 
-------------------------------------------------------------------------------------------------------------------------------
// Api Service Type 2 Multipart
{**
								@Multipart
									@POST("api/waiter_image")
									fun updateProfile(
											@Part("api_key") api_key: RequestBody,
											@Part image_url: MultipartBody.Part
									): Call<UpdateProfile_Model>
							------------------------------------------------

		val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
							   .baseUrl(Constants.BASE_URL)
							   .addConverterFactory(GsonConverterFactory.create(gson))
							   .build()

        val apiService = retrofit.create<ApiService>(ApiService::class.java)

        val apikey_requestbody = RequestBody.create(MediaType.parse("text/plain"), api_key)

        val profilephotoStaff = RequestBody.create(MediaType.parse("image/*"), img)
        val IdentityProofback2_body = MultipartBody.Part.createFormData("image_url", "ok.jpg", profilephotoStaff)


        val call = apiService.updateProfile(apikey_requestbody, IdentityProofback2_body)
        call.enqueue(object : Callback<UpdateProfile_Model> {

            override fun onResponse(call: Call<UpdateProfile_Model>?, response: Response<UpdateProfile_Model>?) {

                if (response!!.body() != null && response.isSuccessful) {
                    progressbar_account.visibility = View.GONE
                    if (!response.body()!!.error!!) {
                        Toast.makeText(this@AccountActivity, "" + response.body()!!.message, Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this@AccountActivity, "" + response.body()!!.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    progressbar_account.visibility = View.GONE
                    Toast.makeText(this@AccountActivity, "Please Check your connection or try again later!", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<UpdateProfile_Model>?, t: Throwable?) {
                progressbar_account.visibility = View.GONE
                Toast.makeText(this@AccountActivity, "" + t.toString(), Toast.LENGTH_SHORT).show()

            }
        })							
**}


-------------------------------------------------------------------------------------------------------------------------------
// Api Service Type 3 Multiple upload
{**
								@POST("upload_multiple.php")
								fun uploadInitialFiles(@Body files: RequestBody): Call<LoginModel>
								
								----------------------------------------------------------
		val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
							   .baseUrl(Constant.BASE_URL2)
							   .addConverterFactory(GsonConverterFactory.create(gson))
							   .build()

        val apiService = retrofit.create<ApiService>(ApiService::class.java)

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("directory",directoryName)
        builder.addFormDataPart("folder_name",folderName)

        for( i in 0 until imagesfiles!!.size)
        {  val imageFileName = i.toString()+"_"+SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            builder.addFormDataPart("file[]", ""+imageFileName+".jpg", RequestBody.create(MediaType.parse("image/*"), imagesfiles!![i]))
        }

        val requestBody = builder.build()
        call = apiService.uploadInitialFiles(requestBody)
        call!!.enqueue(object : Callback<LoginModel> {
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
               
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                progressbar_initial.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
**}

	
--------------------------------------------------------------------------------------------------------------------------------
// Api Service Type 4 Get
{**
								@GET("?op=getallUsers")
								fun allUsers(): Call<AllUserModel>
						-----------------------------------------------------
				
			val gson = GsonBuilder().setLenient().create()
			val retrofit = Retrofit.Builder()
								   .baseUrl(Constant.BASE_URL)
								   .addConverterFactory(GsonConverterFactory.create(gson))
								   .build()
			val apiService = retrofit.create<ApiService>(ApiService::class.java)

			call = apiService.viewCase()

			call!!.enqueue(object : Callback<ViewCaseModel> {

            override fun onResponse(call: Call<ViewCaseModel>?, response: Response<ViewCaseModel>?) {

                if (response!!.body() != null && response.isSuccessful) {
                   // progressbar_login.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    if (!response.body()!!.error!!) {

                    }
                } else {
                    //progressbar_login.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Toast.makeText(this@ViewCaseActivity, "No Response from server!", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ViewCaseModel>?, t: Throwable?) {
                //progressbar_login.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                Snackbar.make(findViewById(android.R.id.content), "Please Check your connection or try again later!", Snackbar.LENGTH_LONG).show()

            }
        })	
**}