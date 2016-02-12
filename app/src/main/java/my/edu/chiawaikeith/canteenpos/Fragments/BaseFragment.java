package my.edu.chiawaikeith.canteenpos.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import my.edu.chiawaikeith.canteenpos.Domains.OfflineLogin;

public class BaseFragment extends Fragment {

    public static final int REQUEST_STORAGE = 0;
    public static final int REQUEST_IMAGE_CAPTURE = REQUEST_STORAGE + 1;
    public static final int REQUEST_LOAD_IMAGE = REQUEST_IMAGE_CAPTURE + 1;

    public static final int REQUEST_IMAGE = 1;
    public static final int REQUEST_FIRST_IMAGE = 1;
    public static final int REQUEST_SECOND_IMAGE = REQUEST_FIRST_IMAGE + 1;
    public static final int REQUEST_THIRD_IMAGE = REQUEST_SECOND_IMAGE + 1;

    // MySql date format "2015-10-13 09:12:00"
    public SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    public SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    public SimpleDateFormat mySqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public SimpleDateFormat mySqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final String KEY_RESPONSE = "response";
    public int response;
    public static final String OBJECT_OFFLINE_LOGIN = "OfflineLogin";
    public static final String KEY_ACCOUNT = "Account";
    public static final String KEY_TRANSAC = "Transaction";
    public static final String JSON_ARRAY = "result";

    public DisplayImageOptions options = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Nullable
    public Intent createCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            return takePictureIntent;
        } else {
            return null;
        }
    }

    public OfflineLogin getLoginDetail(Context context) {
        OfflineLogin offlineLogin = null;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        if(prefs.getString(OBJECT_OFFLINE_LOGIN, "") != null)
        {
            String json = prefs.getString(OBJECT_OFFLINE_LOGIN, "");
            offlineLogin = gson.fromJson(json, OfflineLogin.class);
        }
        return offlineLogin;
    }

    @Nullable
    public Intent createPickIntent() {
        Intent picImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (picImageIntent.resolveActivity(getContext().getPackageManager()) != null) {
            return picImageIntent;
        } else {
            return null;
        }
    }

    public boolean checkNetworkConnectivity(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void shortToast(Context a, String message) {
        Toast.makeText(a, message, Toast.LENGTH_SHORT).show();
    }
}
