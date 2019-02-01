mavenCentral()
implementation 'com.facebook.android:facebook-android-sdk:4.28.0'
-----------------------------------------------------------------------------------
In Menifest:-

<meta-data android:name="com.facebook.sdk.ApplicationId"
            android:value="@string/facebook_app_id"/>
-------------------------------------------------------------------------------------

OnCreateView :-

 try {
            PackageInfo info = getPackageManager().getPackageInfo("com.duke.myapplication", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("aaaaaaaaaaaaaaaaaaa", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
-------------------------------------------------------------------------------------		