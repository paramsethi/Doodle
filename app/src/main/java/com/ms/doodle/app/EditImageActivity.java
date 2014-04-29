package com.ms.doodle.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import android.app.Dialog;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.widget.Toast;
import java.util.UUID;


public class EditImageActivity extends ActionBarActivity {
    private ImageView imageView;

    private DrawingView drawView;
    private ImageButton paintButton, drawButton, eraseButton, saveButton;
    private float smallBrush, mediumBrush, largeBrush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        imageView = (ImageView) findViewById(R.id.editImageView);
        try {
            Uri imageUri = (Uri) this.getIntent().getParcelableExtra("data");
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            imageView.setImageBitmap(selectedImage);
        } catch (FileNotFoundException fex) {
            fex.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }

        drawView = (DrawingView) findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.colors1);

        paintButton = (ImageButton) paintLayout.getChildAt(0);
        paintButton.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawButton = (ImageButton)findViewById(R.id.brush_btn);
       // drawButton.setOnClickListener(this);

        drawView.setBrushSize(mediumBrush);

        // save button
        saveButton = (ImageButton) findViewById(R.id.save_btn);
       // saveButton.setOnClickListener(this);
    }

    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.brush_btn:
                onClickForBrushButton();
                break;
            case R.id.save_btn:
                onClickForSaveButton();
                break;
        }
    }

    private void onClickForSaveButton()
    {
        drawView.setDrawingCacheEnabled(true);
        String imgSaved = MediaStore.Images.Media.insertImage(getContentResolver(), drawView.getDrawingCache(), UUID.randomUUID().toString()+".png", "drawing");
        Toast toast;
        if (imgSaved != null)
        {
            toast= Toast.makeText(getApplicationContext(), "Image Saved to Gallery!", Toast.LENGTH_SHORT);
            toast.show();
            // Navigate to Home Screen.
        }
        else
        {
            toast = Toast.makeText(getApplicationContext(), "Sorry, Image could not be saved", Toast.LENGTH_SHORT);
            toast.show();
        }
        drawView.destroyDrawingCache();
    }

    private void onClickForBrushButton()
    {
        // Create a dialog of different brush sizes.
        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle("Brush sizes:");

        // Brush dialog associates with Brush Chooser which is another layout to choose sizes.
        brushDialog.setContentView(R.layout.brush_chooser);

        ImageButton smallButton = (ImageButton) brushDialog.findViewById(R.id.small);
        smallButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setEraseMode(false);
                drawView.setBrushSize(smallBrush);
                drawView.setLastBrushSize(smallBrush);
                brushDialog.dismiss();
            }
        });

        ImageButton mediumButton = (ImageButton) brushDialog.findViewById(R.id.medium);
        mediumButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setEraseMode(false);
                drawView.setBrushSize(mediumBrush);
                drawView.setLastBrushSize(mediumBrush);
                brushDialog.dismiss();
            }
        });

        ImageButton largeButton = (ImageButton) brushDialog.findViewById(R.id.large);
        largeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setEraseMode(false);
                drawView.setBrushSize(largeBrush);
                drawView.setLastBrushSize(largeBrush);
                brushDialog.dismiss();
            }
        });

        brushDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void paintClicked(View view)
    {
        if (view!=paintButton)
        {
            // Get rid of the erase mode.
            drawView.setEraseMode(false);

            // Set the brush size to the last known one.
            drawView.setBrushSize(drawView.getLastBrushSize());

            // Set current color according to the button user clicked on.
            ImageButton newPaintButton = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);

            // Change the clicked button to paint_pressed xml.
            newPaintButton.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

            // Change the previously selected button to paint xml.
            paintButton.setImageDrawable((getResources().getDrawable(R.drawable.paint)));

            // Save the current button selection
            paintButton=(ImageButton)view;
        }
    }
}
