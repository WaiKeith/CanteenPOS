package my.edu.chiawaikeith.canteenpos.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.ImagePickerSheetView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.software.shell.fab.ActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import my.edu.chiawaikeith.canteenpos.Domains.Accounts;
import my.edu.chiawaikeith.canteenpos.Fragments.ProfileFragment;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;

public class EditProfile extends BaseActivity implements View.OnClickListener {
    private static final String UPDATE_URL = "http://dinpos.comlu.com/Accounts/Students/update_account.php";
    private static final String KEY_ACCOUNT_ID = "acc_id";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_PASSWORD = "acc_password";
    private static final String KEY_IMAGE_PATH = "profile_image_path";
    private static final String KEY_BITMAP = "profile_image_bitmap";

    private Toolbar toolBar;
    private ActionButton actionButton;
    private EditText editTextName, editTextPW;
    String imgDecodableString;
    Uri selectedImageUri;
    private String selectedImagePath;

    ImageView pImage,mImageProfile,ImageView2,ImageView3;
    BottomSheetLayout mBottomSheetImage;
    private FloatingActionButton fabSave;
    private Bitmap mBitmapImage1;
    private Uri mCameraImageUri = null;
    private int mLoadImageNo;
    private Accounts account;
    private int acc_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Bitmap bm = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_profile_pic);

        toolBar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.title_edit_profile);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fabSave = (FloatingActionButton)findViewById(R.id.fab_save);
        fabSave.setOnClickListener(this);

        mImageProfile = (ImageView)findViewById(R.id.image_1);
        mImageProfile.setOnClickListener(this);

        editTextName = (EditText)findViewById(R.id.editName);
        editTextPW = (EditText)findViewById(R.id.editPassword);

        mBottomSheetImage = (BottomSheetLayout)findViewById(R.id.bottom_sheet_image);
        actionButton = (ActionButton) findViewById(R.id.action_button);

        account = (Accounts) getIntent().getSerializableExtra(ProfileFragment.KEY_ACCOUNT);

//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
//                .defaultDisplayImageOptions(defaultOptions)
//                .denyCacheImageMultipleSizesInMemory()
//                .build();
//        ImageLoader.getInstance().init(config);

        initValues();
    }

    public void initValues() {

        acc_id = account.getAcc_id();
        editTextName.setHint(account.getUser_name());
        editTextPW.setHint(account.getAcc_password());

        if(account.getProfile_image_path() != ""){
            ImageLoader.getInstance().displayImage(account.getProfile_image_path(), mImageProfile, options);}

        calendar = Calendar.getInstance();
    }

    public void onClick(View view) {

        int id = view.getId();
        switch (id) {

            case R.id.image_1:
                mLoadImageNo = REQUEST_FIRST_IMAGE;
                showSheetView();
                break;

            case R.id.fab_save:
                updateAccount();

                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }


    private void updateAccount() {
        account.setUser_name(editTextName.getText().toString());
        account.setAcc_password(editTextPW.getText().toString());
        account.setProfileImageBitmap(mBitmapImage1);

        new updateAccount(account).execute();
    }

    public class updateAccount extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        Accounts accounts;

        public updateAccount(Accounts account) {
            this.accounts = account;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loading = ProgressDialog.show(EditAccountActivity.this, "Uploading...", null, true, true);
            //UIUtils.getProgressDialog(EditAccountActivity.this, "ON");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //loading.dismiss();
            //UIUtils.getProgressDialog(EditAccountActivity.this, "OFF");
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            finish();
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<>();
            data.put(KEY_ACCOUNT_ID, String.valueOf(acc_id));
            data.put(KEY_USERNAME, accounts.getUser_name());
            data.put(KEY_PASSWORD, accounts.getAcc_password());
            data.put(KEY_IMAGE_PATH, accounts.getProfile_image_path());
            Log.d("bitmap", getStringImage(accounts.getProfileImageBitmap()));
            if(accounts.getProfileImageBitmap() != null)
                data.put(KEY_BITMAP,getStringImage(accounts.getProfileImageBitmap()));
            else
                data.put(KEY_BITMAP,"");

            return requestHandler.sendPostRequest(UPDATE_URL, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSheetView() {

        ImagePickerSheetView sheetView = new ImagePickerSheetView.Builder(this)
                .setMaxItems(30)
                .setShowCameraOption(createCameraIntent() != null)
                .setShowPickerOption(createPickIntent() != null)
                .setImageProvider(new ImagePickerSheetView.ImageProvider() {
                    @Override
                    public void onProvideImage(ImageView imageView, Uri imageUri, int size) {
                        Glide.with(EditProfile.this)
                                .load(imageUri)
                                .centerCrop()
                                .crossFade()
                                .into(imageView);
                    }
                })
                .setOnTileSelectedListener(new ImagePickerSheetView.OnTileSelectedListener() {
                    @Override
                    public void onTileSelected(ImagePickerSheetView.ImagePickerTile selectedTile) {
                        mBottomSheetImage.dismissSheet();
                        if (selectedTile.isCameraTile()) {
                            dispatchTakePictureIntent();
                        } else if (selectedTile.isPickerTile()) {
                            startActivityForResult(createPickIntent(), REQUEST_LOAD_IMAGE);
                        } else if (selectedTile.isImageTile()) {
                            showSelectedImage(selectedTile.getImageUri());
                        } else {
                            //  genericError();
                        }
                    }
                })
                .setTitle("Choose an image...")
                .create();

        mBottomSheetImage.showWithSheetView(sheetView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage = null;
            if (requestCode == REQUEST_LOAD_IMAGE && data != null) {
                selectedImage = data.getData();
                if (selectedImage == null) {
                    // genericError();
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Do something with imagePath
                selectedImage = mCameraImageUri;
            }

            if (selectedImage != null) {
                showSelectedImage(selectedImage);
            } else {
                //      genericError();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = createCameraIntent();
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent != null) {
            // Create the File where the photo should go
            try {
                File imageFile = createImageFile();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException e) {
                // Error occurred while creating the Fil
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCameraImageUri = Uri.fromFile(imageFile);
        return imageFile;
    }

    private void showSelectedImage(Uri selectedImageUri) {

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
            int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

            if (mLoadImageNo == 1) {
                mImageProfile.setImageDrawable(null);
                mImageProfile.setScaleType(ImageView.ScaleType.FIT_XY);
                mImageProfile.setImageBitmap(scaled);
                mBitmapImage1 = scaled;
                //ImageView2.setVisibility(View.VISIBLE);
            } else
                genericError(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}