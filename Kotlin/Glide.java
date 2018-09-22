implementation 'com.github.bumptech.glide:glide:4.7.1'

---------------------------------------------------------------------------

Glide.get(this).clearMemory()
  
Glide.with(this)
	 .load(R.drawable.excellent_unselected)
     .apply(RequestOptions()
				.diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .signature(ObjectKey(System.currentTimeMillis()))
			)
     .into(imageView_profilePhoto)