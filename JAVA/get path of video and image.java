
Uri image_uri = data.getData();

Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Audio.Media.DATA};

            cursor = this.getContentResolver().query(image_uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }