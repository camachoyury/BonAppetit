package org.camachoyury.bonappetit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.Manifest;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import org.camachoyury.bonappetit.mvp.activities.PostActivity;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;


/**
 * Created by yury on 3/29/17.
 */

public class WallFragment extends Fragment implements  EasyPermissions.PermissionCallbacks{

    private static final int PERMISSION_REQUEST_CAMERA = 0;
    public static final String TAG = "WallFragment";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String PROVIDER_URI = "org.camachoyury.bonappetit.fileprovider";
    private ImageView pic;
    private FloatingActionButton fab;
    View rootView;
    String captureImagePath;
    private Uri uriFoodPhoto;
    private static final int CAMERA_REQUEST = 1888;
    private static final int RC_STORAGE_PERMS = 102;
    ImageView imageView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.content_main,container, false);
        imageView = (ImageView)rootView.findViewById(R.id.pic);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        return rootView;
    }



    void takePicture(){
        requestPermission();
        createFile();
        startCamera();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {}

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {}



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST ) {
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

                Intent intent = new Intent(getActivity(), PostActivity.class);
                intent.putExtra("photo_path",captureImagePath);
                startActivity(intent);

            }
        }
    }

    private void startCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoodPhoto);
        startActivityForResult(takePictureIntent, CAMERA_REQUEST);
    }

    private void createFile(){
        File dir = new File(Environment.getExternalStorageDirectory() + "/photos");
        File file = new File(dir, new Date().getTime() + ".jpg");
        try {
            if (!dir.exists()) {
                dir.mkdir();
            }
            boolean created = file.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, "file.createNewFile" + file.getAbsolutePath() + ":FAILED", e);
        }

        captureImagePath = file.getAbsolutePath();

        uriFoodPhoto = FileProvider.getUriForFile(getActivity(),PROVIDER_URI, file);
    }

    private void requestPermission(){

        //obteniendo permisos
        String[] perms = {android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(getActivity(), perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage),
                    RC_STORAGE_PERMS, perms);
            return;
        }
    }

}
