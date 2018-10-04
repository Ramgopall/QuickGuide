First you Need Write External Storeage Permission.

//open or Create database
SQLiteDatabase sqliteDataBase = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().getAbsolutePath() + "/abc.sqlite", null);

// create table for first time only
sqliteDataBase.execSQL("Create table if not exists tbname (Name varchar, Roll_No varchar, Marks varchar);");

// insert value into table
sqliteDataBase.execSQL("insert into tbname values('" + ed1.getText().toString() + "', '" + ed2.getText().toString() + "', '" + ed3.getText().toString() + "');");

// delete value form table
sqliteDataBase.execSQL("delete from tbname where Roll_No = '" + ed2.getText().toString() + "'");

// get value from table but always remember to close the cursor in the end.
Cursor cr = sqliteDataBase.rawQuery("Select * from tbname where Roll_No= '" + ed2.getText().toString() + "'", null);

			// use this only when you have one rows data.
			cursor.moveToFirst()
			String ClientId= cursor.getString(cursor.getColumnIndex("ID"));
            String ClientName = cursor.getString(cursor.getColumnIndex("Name"));

			
			// use this only when you have more than one rows data.
			//And never use both functions together "moveToNext(),moveToFirst()"
			//
			ArrayList<ClientDetails> list = new ArrayList();
			while (cursor.moveToNext()){

                String ClientId= cursor.getString(cursor.getColumnIndex("ID"));
                String ClientName = cursor.getString(cursor.getColumnIndex("Name"));
                
				//Model Class
                ClientDetails clientDetails = new ClientDetails(ClientId,ClientName);
                
				// add value into list
				list.add(clientDetails);

            }
