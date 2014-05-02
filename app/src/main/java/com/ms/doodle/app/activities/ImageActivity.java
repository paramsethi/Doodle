package com.ms.doodle.app.activities;
/**
 * ImageActivity For showing image gallery
 *
 */

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.ms.doodle.app.R;
import com.ms.doodle.app.adapters.GridViewImageAdapter;
import com.ms.doodle.app.utils.AppConstant;
import com.ms.doodle.app.utils.Utils;

public class ImageActivity extends Activity {
    private Utils utils;
    private GridView gridView;
    private int columnWidth;
    private final int SELECT_PHOTO = 1;
    private final int CAPTURE_PHOTO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        gridView = (GridView) findViewById(R.id.grid_view);

        utils = new Utils(this);

        // Initilizing Grid View
        InitilizeGridLayout();

        // setting grid view adapter
        gridView.setAdapter(new GridViewImageAdapter(ImageActivity.this, utils.getFilePaths(),
                columnWidth));

        Button pickImage = (Button) findViewById(R.id.btn_pick);
        pickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        Button clickImage = (Button) findViewById(R.id.btn_click);
        clickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoClickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //photoClickerIntent.setType("image/*");
                startActivityForResult(photoClickerIntent, CAPTURE_PHOTO);
            }
        });

        Button createImage = (Button) findViewById(R.id.btn_create);
        createImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(ImageActivity.this, EditImageActivity.class);
                startActivity(i);
            }
        });

    }

    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Intent i = new Intent(ImageActivity.this, EditImageActivity.class);
                        i.putExtra("uri", imageReturnedIntent.getData());
                        startActivity(i);
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }

                }
                break;
            case CAPTURE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap image1 = (Bitmap) imageReturnedIntent.getExtras().get("data");
                        String imgSaved = MediaStore.Images.Media.insertImage(this.getBaseContext().getContentResolver(), image1, "Do_Odle" + System.currentTimeMillis() + ".png", "drawing");

                        Intent i = new Intent(ImageActivity.this, EditImageActivity.class);
                        i.putExtra("uri", Uri.parse(imgSaved));
                        startActivity(i);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
        }
    }
}
