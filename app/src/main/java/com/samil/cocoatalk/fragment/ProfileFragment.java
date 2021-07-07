package com.samil.cocoatalk.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.samil.cocoatalk.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    int REQUEST_IMAGE_CODE = 1001; // 갤러리에 접근하기 위한 상수 코드
    int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1002; // 권한 설정을 위한 상수 코드

    ImageView imageView;
    private StorageReference mStorageRef;
    String id, uid;
    File localFile;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        // SharedPreferences 객체로 저장된 데이터 전달
        // 로그인했던 사용자의 email 계정을 가져온다.
        SharedPreferences sharedPref = getActivity().getSharedPreferences("save", Context.MODE_PRIVATE);
        id = sharedPref.getString("id", id);
        uid = sharedPref.getString("uid", uid);
        Log.d(TAG, "ID 정보 : " + id);

        mStorageRef = FirebaseStorage.getInstance().getReference(); // 파이어베이스 스토리지를 초기화하여 참조하는 StorageReference 인스턴스

        // 갤러리에 접근하기 위해 권한을 부여하는 if문
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE_PERMISSION);


            }
        } else {
        }

        imageView = root.findViewById(R.id.profile_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭 리스너를 통해 갤러리를 실행한다.
                Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(in, REQUEST_IMAGE_CODE);
            }
        });
        return root;
    }

    // 갤러리에서 사용자가 선택한 사진을 파이어베이스 스토리지에 저장되도록 하는 메소드
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE) {
            Uri image = data.getData(); // 갤러리에서 선택한 이미지의 uri를 추출한다.
            try {
                // 선택한 이미지를 비트맵으로 생성하여 처리하는 부분
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 이미지가 저장되는 파이어 스토리지 경로
            StorageReference riversRef = mStorageRef.child("users").child(id).child("profile.jpg");

            riversRef.putFile(image)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Log.d(TAG, taskSnapshot.toString());

                            FirebaseStorage.getInstance().getReference().child("users").child(id).child("profile.jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                                    Uri downUri = task.getResult();
                                    String memberProfile = downUri.toString();

                                    Map<String, Object> update = new HashMap<>();
                                    update.put("profile", memberProfile);

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("Users");
                                    reference.child(uid).updateChildren(update);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

//    public String getPath(Uri uri){
//        String[] imgUri = {MediaStore.Images.Media.DATA};
//        CursorLoader cursorLoader = new CursorLoader(this, uri, imgUri, null, null,null);
//
//        Cursor cursor = cursorLoader.loadInBackground();
//        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//        cursor.moveToFirst();
//
//        return cursor.getString(index);
//    }

}