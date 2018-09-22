implementation 'com.squareup.retrofit2:retrofit:2.3.0'
implementation 'com.squareup.retrofit2:converter-gson:2.3.0'

--------------------------------------------------------------------------------------------------------------------------------

// Api Service Type 1 POST
{**
						@FormUrlEncoded
						@POST("?op=loginUser")
						Call<Login_Model> loginUser(
								@Field("username") String email,
								@Field("password") String password
						);
			
			                -------------------------------------------------------
			
						Gson gson = new GsonBuilder().setLenient().create();

						OkHttpClient client = new OkHttpClient();
					
						//building retrofit object
						Retrofit retrofit = new Retrofit.Builder()
								.baseUrl(BaseURL)
								.client(client)
								.addConverterFactory(GsonConverterFactory.create(gson))
								.build();
					
						//Defining retrofit api service
						RetrofitApiResponse service = retrofit.create(RetrofitApiResponse.class);
					
					
						//defining the call
						Call<Login_Model> call = service.loginUser(
								email,password
						);
					
					
						call.enqueue(new Callback<Login_Model>() {
							@Override
							public void onResponse(Call<Login_Model> call, Response<Login_Model> response) {
							
								if (response.body() != null && response.isSuccessful()) {
					
								}
								else
								{
									Toast.makeText(context, "No Response from Server!", Toast.LENGTH_SHORT).show();
								}
							}
					
							@Override
							public void onFailure(Call<Login_Model> call, Throwable t) {
								Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
							}
						});
 **}
 
 
-------------------------------------------------------------------------------------------------------------------------------
// Api Service Type 2 Multipart
{**
								 @Multipart
								 @POST("?op=addforeignerBooking")
								 Call<BookNow_Model> uploadFiles(
														@Part("name") RequestBody Name,
														@Part MultipartBody.Part IdentityProof1_Front,
    
													);
							------------------------------------------------

		Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApiResponse retrofitApiResponse = retrofit.create(RetrofitApiResponse.class);


        
        RequestBody name_requestbody = RequestBody.create(MediaType.parse("text/plain"), "Ramgopal");

        RequestBody IdentityProofback1_requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part   IdentityProofback1_body = MultipartBody.Part.createFormData("identityback1", pic_name + ".png", IdentityProofback1_requestFile);
       
      
        Call<BookNow_Model> call = retrofitApiResponse.uploadFiles(

                name_requestbody,
                IdentityProofback1_body
        );

        call.enqueue(new Callback<BookNow_Model>() {
            @Override
            public void onResponse(Call<BookNow_Model> call, retrofit2.Response<BookNow_Model> response) {
                if (response.body() != null && response.isSuccessful()) {
                

                } else {
                    Toast.makeText(context, "Server is down!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookNow_Model> call, Throwable t) {
                Toast.makeText(context, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });					
**}


-------------------------------------------------------------------------------------------------------------------------------
// Api Service Type 3 Get
{**

								 @GET("?op=addforeignerBooking")
								 Call<BookNow_Model> getALlUser();
								
						-----------------------------------------------------
						Gson gson = new GsonBuilder().setLenient().create();

						OkHttpClient client = new OkHttpClient();
					
						//building retrofit object
						Retrofit retrofit = new Retrofit.Builder()
								.baseUrl(BaseURL)
								.client(client)
								.addConverterFactory(GsonConverterFactory.create(gson))
								.build();
					
						//Defining retrofit api service
						RetrofitApiResponse service = retrofit.create(RetrofitApiResponse.class);
					
					
						//defining the call
						Call<BookNow_Model> call = service.getALlUser();
					
					
						call.enqueue(new Callback<BookNow_Model>() {
							@Override
							public void onResponse(Call<BookNow_Model> call, Response<BookNow_Model> response) {
								
								if (response.body() != null && response.isSuccessful()) {
					
								}
								else
								{
									Toast.makeText(context, "No Response from Server!", Toast.LENGTH_SHORT).show();
								}
							}
					
							@Override
							public void onFailure(Call<BookNow_Model> call, Throwable t) {
								Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
							}
						});
			
**}