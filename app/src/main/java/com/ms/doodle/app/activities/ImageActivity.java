package com.ms.doodle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.ms.doodle.app.adapters.GridViewImageAdapter;
import com.ms.doodle.app.R;
import com.ms.doodle.app.utils.AppConstant;
import com.ms.doodle.app.utils.Utils;
import java.util.Locale;

public class ImageActivity extends Activity
{
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
            public void onClick(View view)
            {
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
        Uri imageUri;
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
                if (resultCode == RESULT_OK)
                {
                    try
                    {
                        Bitmap image1 = (Bitmap) imageReturnedIntent.getExtras().get("uri");
                        String imgSaved = MediaStore.Images.Media.insertImage(this.getBaseContext().getContentResolver(), image1, "Do_Odle" + System.currentTimeMillis() + ".png", "drawing");

                        Intent i = new Intent(ImageActivity.this, EditImageActivity.class);
                        i.putExtra("uri", Uri.parse(imgSaved));
                        startActivity(i);

                    } catch (Exception ex)
                    {
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
