Attach your project with firebase first then follow the below codes:
////////////////////////////////////////////////
private GoogleApiClient mGoogleApiClient;
//////////////////////
OnCreateView(){**
GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestEmail()
			.build();

	mGoogleApiClient = new GoogleApiClient.Builder(this)
			.enableAutoManage(this, this)
			.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
			.build();

	googleSignIn.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
			startActivityForResult(signInIntent, 007);
		}
	});
**}
//////////////////////////////////////////

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 007) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
      
    }

	private void handleSignInResult(GoogleSignInResult result) {
       // Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

           // Log.e(TAG, "display name: " + acct.getDisplayName());

            final String personName = acct.getDisplayName();
            final String email = acct.getEmail();
            final String session = acct.getId();

        }
    }
	
///////////////////////////////////////////////////////
	//For Logout:-
	Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {

                                    }
                                });