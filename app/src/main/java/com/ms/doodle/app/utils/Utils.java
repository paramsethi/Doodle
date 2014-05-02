
/**
 * Created by parseth on 5/1/14.
 */

package com.ms.doodle.app.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Stack;

public class Utils {

    private Context _context;

    // constructor
    public Utils(Context context) {
        this._context = context;
    }

    // Reading file paths from SDCard
    public ArrayList<String> getFilePaths() {
        ArrayList<String> filePaths = new ArrayList<String>();

        File directory = new File(
                android.os.Environment.getExternalStorageDirectory().getPath());

        // check for directory
        if (directory.isDirectory()) {
            Stack<File> fileStack = new Stack<File>();
            fileStack.push(directory);

            while(!fileStack.isEmpty()){
                File dir = fileStack.pop();

                // getting list of file paths
                File[] listFiles = dir.listFiles();
                // Check for count
                if (listFiles.length > 0) {
                    // loop through all files
                    for (int i = 0; i < listFiles.length; i++) {
                        if(listFiles[i].getName().startsWith(".")){
                            continue;
                        }
                        if(listFiles[i].isDirectory()){
                            fileStack.push(listFiles[i]);
                        }else{
                        // get file path
                        String filePath = listFiles[i].getAbsolutePath();

                        // check for supported file extension
                        if (IsSupportedFile(filePath)) {
                            // Add image path to array list
                            filePaths.add(filePath);
                        }
                        }
                    }
                }
            }
        }
        return filePaths;
    }

    // Check supported file extensions
    private boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
                filePath.length());

        if (AppConstant.FILE_EXTN
                .contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;

    }

    /*
     * getting screen width
     */
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }
}