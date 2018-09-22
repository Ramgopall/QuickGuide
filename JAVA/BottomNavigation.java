1st XML
{
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:background="#ffffff"
    android:clickable="true"
    android:focusable="true"
    android:orientation="vertical">

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/frameLayout_receiveloan"
        android:layout_above="@+id/bottom_navigation_receiveloan"
        />

    <android.support.design.widget.BottomNavigationView
        android:id="@+id/bottom_navigation_receiveloan"
        android:layout_width="match_parent"
        android:layout_height="75dp"
        android:background="@color/colorPrimary"
        app:itemBackground="@color/colorPrimary"
        app:itemIconTint="@android:color/white"
        app:itemTextColor="@android:color/white"
        app:menu="@menu/receivepayloan_menu"
        app:labelVisibilityMode ="labeled"
        android:layout_alignParentBottom="true"
        />



</RelativeLayout>
}

-------------------------------------------------------------------------------------------------------
2nd Menu Xml
{
	<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item
        android:id="@+id/menu_receive_payloan_list"
        android:enabled="true"
        android:icon="@drawable/adduserwhite"
        android:title="Pending"
        app:showAsAction="ifRoom" />
    <item
        android:id="@+id/menu_receive_payloan_editlist"
        android:enabled="true"
        android:icon="@drawable/adduserwhite"
        android:title="Edit"
        app:showAsAction="ifRoom" />

    <item
        android:id="@+id/menu_receive_payloan_outdatedlist"
        android:enabled="true"
        android:icon="@drawable/adduserwhite"
        android:title="Outdated"
        app:showAsAction="ifRoom" />

    <item
        android:id="@+id/menu_receive_payloan_completedlist"
        android:enabled="true"
        android:icon="@drawable/ic_format_list_bulleted_black_24dp"
        android:title="Completed"
        app:showAsAction="ifRoom" />


</menu>
}

--------------------------------------------------------------------------------------------------------
3rd Kotlin/java
{
	BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
	
	----------------
	
		bottomNavigationView = view.findViewById(R.id.bottom_navigation_receiveloan);
        fragmentManager = getFragmentManager();

        if (savedInstanceState == null){
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout_receiveloan, new PendingReceivePayLoanFragment());
            fragmentTransaction.commit();
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_receive_payloan_list:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout_receiveloan, new PendingReceivePayLoanFragment());
                        fragmentTransaction.commit();
                        return true;

                    case R.id.menu_receive_payloan_editlist:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout_receiveloan, new EditReceivePayLoanFragment());
                        fragmentTransaction.commit();

                        return true;

                    case R.id.menu_receive_payloan_completedlist:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout_receiveloan, new CompletedReceivePayLoanFragment());
                        fragmentTransaction.commit();

                        return true;

                    case R.id.menu_receive_payloan_outdatedlist:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout_receiveloan, new OutDatedFragment());
                        fragmentTransaction.commit();

                        return true;
                }

                return false;
            }
        });


        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                Toast.makeText(getActivity(), "Reselected", Toast.LENGTH_SHORT).show();
            }
        });
}