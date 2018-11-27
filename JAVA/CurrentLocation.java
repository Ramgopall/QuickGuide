	<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.INTERNET" />
-------------------------------------------------------
{**
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        if (location==null)
        {
           location =locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (location==null)
        {
            Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
        }
        else {
           


            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
			
            Geocoder geocoder = new Geocoder(this);

            try {
                
				List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
                
				Address locationAddress = addressList.get(0);
				
            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();
                
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
		
**}		