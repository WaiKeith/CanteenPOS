package my.edu.chiawaikeith.canteenpos.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import my.edu.chiawaikeith.canteenpos.Domains.OfflineLogin;

public class BaseActivity extends AppCompatActivity {

    // This class use to share variables for reuse purpose

    public static final int REQUEST_DATE = 1;
    public static final int REQUEST_CANCEL_DATE = 2;
    public static final int REQUEST_REPLACEMENT_DATE = 3;
    public static final int REQUEST_START_TIME = 4;
    public static final int REQUEST_END_TIME = 5;

    public static final int RESPONSE_404 = 0;
    public static final int RESPONSE_SUCCESS = 1;
    public static final int RESPONSE_PASSWORD_INCORRECT = 2;

    public static final String JSON_ARRAY = "result";

    public static final String KEY_SELECTED_TYPE = "KEY_SELECTED_TYPE";

    public static final int REQUEST_STORAGE = 0;
    public static final int REQUEST_IMAGE_CAPTURE = REQUEST_STORAGE + 1;
    public static final int REQUEST_LOAD_IMAGE = REQUEST_IMAGE_CAPTURE + 1;

    public static final int REQUEST_IMAGE = 1;
    public static final int REQUEST_FIRST_IMAGE = 1;
    public static final int REQUEST_SECOND_IMAGE = REQUEST_FIRST_IMAGE + 1;
    public static final int REQUEST_THIRD_IMAGE = REQUEST_SECOND_IMAGE + 1;

    public final static String KEY_IMAGE_URLS = "ImageUrls";

    public static final String KEY_CURRENT_PASSWORD = "currentPassword";
    public static final String KEY_CONFIRM_PASSWORD = "confirmPassword";
    public static final String KEY_RESPONSE = "response";
    public static final String KEY_ACCOUNT = "Account";

    public SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    public SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    public SimpleDateFormat defaultDateFormat = new SimpleDateFormat("dd MMM yyyy");
    public SimpleDateFormat mySqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public SimpleDateFormat mySqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public Calendar calendar;

    public static final String OBJECT_OFFLINE_LOGIN = "OfflineLogin";

    public static void shortToast(Context a, String message) {
        Toast.makeText(a, message, Toast.LENGTH_SHORT).show();
    }

    public void longToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    public Intent createCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            return takePictureIntent;
        } else {
            return null;
        }
    }

    @Nullable
    public Intent createPickIntent() {
        Intent picImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (picImageIntent.resolveActivity(getPackageManager()) != null) {
            return picImageIntent;
        } else {
            return null;
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void genericError(String message) {
        Toast.makeText(this, message == null ? "Something went wrong." : message, Toast.LENGTH_SHORT).show();
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

    public void saveLoginDetail(OfflineLogin offlineLogin,Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(offlineLogin);
        prefsEditor.putString(OBJECT_OFFLINE_LOGIN, json);
        prefsEditor.commit();

    }

    public void removeLoginDetail() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.clear();
        prefsEditor.commit();
    }

}
