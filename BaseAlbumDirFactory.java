package com.example.zhangwenqiang.rjks_final_pro;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

	// Standard storage location for digital camera files
	private static final String CAMERA_DIR = "/DCIM/";

	@Override
	public File getAlbumStorageDir(String albumName) {
		Log.d("base", "was called");
		return new File ( Environment.getExternalStorageDirectory() + CAMERA_DIR + albumName
		);
	}
}
