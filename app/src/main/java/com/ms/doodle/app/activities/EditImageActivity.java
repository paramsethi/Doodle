package com.ms.doodle.app.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ms.doodle.app.R;
import com.ms.doodle.app.views.DrawingView;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class EditImageActivity extends Activity implements OnClickListener {
    Uri imageUri;
    private DrawingView drawView;
    private ImageButton paintButton, drawButton, saveButton, mustacheButton;
    private float smallBrush, mediumBrush, largeBrush;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        InputStream imageStream = null;
        try {
            imageUri = (Uri) this.getIntent().getParcelableExtra("uri");
            imageStream = getContentResolver().openInputStream(imageUri);
        } catch (FileNotFoundException fex) {
            fex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        drawView = (DrawingView) findViewById(R.id.drawing);

        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.colors1);

        paintButton = (ImageButton) paintLayout.getChildAt(0);
        paintButton.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawButton = (ImageButton) findViewById(R.id.brush_btn);
        drawButton.setOnClickListener(this);

        drawView.setBrushSize(mediumBrush);
        if (imageStream != null) {
            drawView.animate();
            drawView.setBackground(Drawable.createFromStream(imageStream, "myImg"));
        }

        // save button
        saveButton = (ImageButton) findViewById(R.id.save_btn);
        saveButton.setOnClickListener(this);

        mustacheButton = (ImageButton) findViewById(R.id.mustache);
        mustacheButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.brush_btn:
                onClickForBrushButton();
                break;
            case R.id.save_btn:
                onClickForSaveButton();
                break;
            case R.id.mustache:
                onClickforMustacheButton();
                break;
        }
    }

    private void onClickforMustacheButton() {
        // Create a dialog of different mustaches.
        final Dialog mustacheDialog = new Dialog(this);
        mustacheDialog.setTitle(getResources().getString(R.string.pick_mustache));
        mustacheDialog.setContentView(R.layout.mustache_chooser);

        ImageButton mustacheChooser = (ImageButton) mustacheDialog.findViewById(R.id.mustache_clipArt);
        mustacheChooser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                mustacheDialog.dismiss();
            }
        });

        mustacheDialog.show();
    }

    private void onClickForSaveButton() {
        drawView.setDrawingCacheEnabled(true);
        try {
            Bitmap bitmap = drawView.getDrawingCache();
            String imgSaved = MediaStore.Images.Media.insertImage(this.getBaseContext().getContentResolver(), bitmap, "Do_Odle" + System.currentTimeMillis() + ".png", "drawing");
            Toast toast;
            Intent i = new Intent(EditImageActivity.this, ImageActivity.class);
            if (imgSaved != null) {
                toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.imageSaved), Toast.LENGTH_SHORT);
                toast.show();
                // Navigate to Home Screen.
                i.putExtra("success", true);

            } else {
                toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.imageSaveError), Toast.LENGTH_SHORT);
                toast.show();
                i.putExtra("success", false);
            }
            drawView.destroyDrawingCache();
            startActivity(i);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private void onClickForBrushButton() {
        // Create a dialog of different brush sizes.
        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle(getResources().getString(R.string.brushSizeTitle));

        // Brush dialog associates with Brush Chooser which is another layout to choose sizes.
        brushDialog.setContentView(R.layout.brush_chooser);

        ImageButton smallButton = (ImageButton) brushDialog.findViewById(R.id.small);
        smallButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                drawView.setBrushSize(smallBrush);
                drawView.setLastBrushSize(smallBrush);
                brushDialog.dismiss();
            }
        });

        ImageButton mediumButton = (ImageButton) brushDialog.findViewById(R.id.medium);
        mediumButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                drawView.setBrushSize(mediumBrush);
                drawView.setLastBrushSize(mediumBrush);
                brushDialog.dismiss();
            }
        });

        ImageButton largeButton = (ImageButton) brushDialog.findViewById(R.id.large);
        largeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                drawView.setBrushSize(largeBrush);
                drawView.setLastBrushSize(largeBrush);
                brushDialog.dismiss();
            }
        });

        brushDialog.show();
    }

    public void paintClicked(View view) {
        if (view != paintButton) {


            // Set the brush size to the last known one.
            drawView.setBrushSize(drawView.getLastBrushSize());

            // Set current color according to the button user clicked on.
            ImageButton newPaintButton = (ImageButton) view;
            String color = view.getTag().toString();
            drawView.setColor(color);

            // Change the clicked button to paint_pressed xml.
            newPaintButton.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

            // Change the previously selected button to paint xml.
            paintButton.setImageDrawable((getResources().getDrawable(R.drawable.paint)));

            // Save the current button selection
            paintButton = (ImageButton) view;
        }
    }
}
