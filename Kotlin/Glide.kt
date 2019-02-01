implementation 'com.github.bumptech.glide:glide:4.7.1'

---------------------------------------------------------------------------

Glide.get(this).clearMemory()

---------------------------------------------------------------------------
val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
  
Glide.with(this)
	 .load(R.drawable.excellent_unselected)
     .apply(RequestOptions()
				.diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
				.placeholder(circularProgressDrawable)
                .signature(ObjectKey(System.currentTimeMillis()))
			)
     .into(imageView_profilePhoto)
	