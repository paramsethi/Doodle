package com.ms.doodle.app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Locale;

public class ImageActivity extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    private final int SELECT_PHOTO = 1;
    private final int CAPTURE_PHOTO = 0;
    private ImageView imageView;

    public Cursor cc = null;
    public static Uri[] mUrls = new Uri[8];
    public static String[] strUrls = new String[8];
    public static String[] mNames = new String[8];
    GridView gridView;
    private static ImageView[] images = null;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

       /* gridView = (GridView) findViewById(R.id.gridView1);
        // Get images from gallery and add to "images"

        ArrayAdapter<ImageView> adapter = new ArrayAdapter<ImageView>(this, android.R.layout.simple_list_item_1, images);
        gridView.setAdapter(adapter);
*/
        String[] projection = { MediaStore.Images.Media.DATA };
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String path = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera";
        String id = String.valueOf(path.toLowerCase().hashCode());
        String[] selectionArgs = { id };
        cc = this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );
        cc.moveToFirst();
        String data = cc.getString(0);
        Uri example = Uri.parse(data);
        ImageView image = new ImageView(this);
        image.setImageURI(example);
        images = new ImageView[] {image};
        Uri[] uris = new Uri[] {example};
        try{
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), example);
            Bitmap[] bitmaps = new Bitmap[]{bitmap};
            ArrayAdapter<Bitmap> adapter = new ArrayAdapter<Bitmap>(this, android.R.layout.simple_gallery_item, bitmaps);
            gridView = (GridView) findViewById(R.id.gridView1);
            gridView.setAdapter(adapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

/*
        if (cc != null)
        {
            try
            {
                cc.moveToFirst();
                for (int i = 0; i > cc.getCount(); i++)
                {
                    cc.moveToPosition(i);
                    mUrls[i] = Uri.parse(cc.getString(1));
                    strUrls[i] = cc.getString(1);
                    mNames[i] = cc.getString(3);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        ImageView iv = new ImageView(this);
        try {
        iv.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(mUrls[0])));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //iv.setImageURI(mUrls[0]);
        images = new ImageView[] {iv};

        gridView = (GridView) findViewById(R.id.gridView1);

        ArrayAdapter<ImageView> adapter = new ArrayAdapter<ImageView>(this, android.R.layout.simple_list_item_1, images);
        gridView.setAdapter(adapter);
  */
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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Uri imageUri;
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        imageUri = imageReturnedIntent.getData();
                       /* final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);*/
                        Intent i = new Intent(ImageActivity.this, EditImageActivity.class);
                        i.putExtra("data", imageUri);
                        startActivity(i);
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }

                }
                break;
            case CAPTURE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {

                        imageUri = imageReturnedIntent.getData();
                       /* final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);*/
                        Intent i = new Intent(ImageActivity.this, EditImageActivity.class);
                        i.putExtra("data", imageUri);
                        startActivity(i);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_activity_image).toUpperCase(l);
                case 1:
                    return getString(R.string.title_activity_image_picker).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_image, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

}
