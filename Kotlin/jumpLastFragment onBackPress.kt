Activity:-

override fun onBackPressed() {
        val count = fragmentManager?.backStackEntryCount

        if (count == 0) {
            val builder = AlertDialog.Builder(this@HomeActivity)
            builder.setMessage("Are you sure you want to Exit?")
            builder.setPositiveButton("Yes") { dialog, which ->
                android.os.Process.killProcess(android.os.Process.myPid())
                System.exit(1)
            }
            builder.setNegativeButton("No") { dialog, which -> }
            builder.create().show()
        } else {
            val f = supportFragmentManager.findFragmentById(R.id.frameLayout_home)
            if (f is EditDesiredJobFragment) {
                val manager = supportFragmentManager
                manager.popBackStack(manager.getBackStackEntryAt(manager.backStackEntryCount -2).id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            } else {
                fragmentManager?.popBackStack()
            }
        }
    }

--------------------------------------------------------------------------------------------------------------------------------------------

Fragment Icon click:-

val manager = activity!!.supportFragmentManager
manager.popBackStack(manager.getBackStackEntryAt(manager.backStackEntryCount -2).id, FragmentManager.POP_BACK_STACK_INCLUSIVE)