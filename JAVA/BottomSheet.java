1st XML (bottomsheet_calendar)
{
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/bottomsheet"
    android:layout_width="match_parent"
    android:layout_height="110dp"
    android:layout_alignParentBottom="true"
    android:background="#99000000"
    android:orientation="vertical"
    app:behavior_hideable="true"
    app:behavior_peekHeight="0dp"
    android:focusable="true"
    app:layout_behavior="android.support.design.widget.BottomSheetBehavior">

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        >

        <ImageView
            android:layout_width="48dp"
            android:layout_height="match_parent"
            android:padding="10dp"
            android:src="@drawable/ic_arrow_back_white_24dp"
            android:id="@+id/imageView_bottomsheet_back_arrow"
            />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:gravity="center"
            android:text="FILTER"
            android:textSize="18sp"
            android:fontFamily="@font/opensans_light"
            android:textColor="@android:color/white" />

    </RelativeLayout>


</LinearLayout>
}
----------------------------------------------------------------------------------------------------------------------------
2nd XML in Bottom include
{
    <android.support.design.widget.CoordinatorLayout 
	    xmlns:android= "http://schemas.android.com/apk/res/android"    
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <include layout="@layout/bottomsheet_calendar" />

    </android.support.design.widget.CoordinatorLayout>

}

-----------------------------------------------------------------------------------------------------------------------------
3rd Java

BottomSheetBehavior sheetBehavior;

		LinearLayout bottomsheet = findViewById(R.id.bottomsheet);

        sheetBehavior = BottomSheetBehavior.from(bottomsheet)

        button.setOnClickListener (new OnClickListener{
			
            if (sheetBehavior.getState(() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
               
            } else {
				sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED):
            }
			
        }
		
		
		/**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
												Toast.makeText(this,"hidden",Toast.LENGTHLONG).show();
												break;
                    case BottomSheetBehavior.STATE_EXPANDED: 
												Toast.makeText(this,"expanded",Toast.LENGTHLONG).show();
												break;
                    case BottomSheetBehavior.STATE_COLLAPSED: 
												Toast.makeText(this,"collapsed",Toast.LENGTHLONG).show();
												break;
                    case BottomSheetBehavior.STATE_DRAGGING:
												Toast.makeText(this,"dragging",Toast.LENGTHLONG).show();
												break;
                    case BottomSheetBehavior.STATE_SETTLING:
												Toast.makeText(this,"settling",Toast.LENGTHLONG).show();
												break;
                }
            }
 
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
 
            }
        });
