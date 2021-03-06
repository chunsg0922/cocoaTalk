package com.samil.cocoatalk.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    int REQUEST_IMAGE_CODE = 1001; // ???????????? ???????????? ?????? ?????? ??????
    int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1002; // ?????? ????????? ?????? ?????? ??????

    ImageView imageView;
    GradientDrawable drawable;
    private StorageReference mStorageRef;
    String id, uid, profile, msg;
    Button gallery, save, msgbtn;
    EditText msg_edit;
    Uri image;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        // SharedPreferences ????????? ????????? ????????? ??????
        // ??????????????? ???????????? email ????????? ????????????.
        SharedPreferences sharedPref = getActivity().getSharedPreferences("save", Context.MODE_PRIVATE);
        id = sharedPref.getString("id", id);
        uid = sharedPref.getString("uid", uid);
        Log.d(TAG, "ID ?????? : " + id);

        mStorageRef = FirebaseStorage.getInstance().getReference(); // ?????????????????? ??????????????? ??????????????? ???????????? StorageReference ????????????
        imageView = root.findViewById(R.id.profile_image);

        // ???????????? ???????????? ?????? ????????? ???????????? if???
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

        imageView.setClipToOutline(true);

        Glide.with
                (this)
                .load(profile)
                .centerCrop()
                .apply(new RequestOptions().bitmapTransform(new RoundedCorners(15)))
                .into(imageView);
        msg_edit = (EditText) root.findViewById(R.id.msg_edit);
//        if(!msg.equals("")){
//            msg_edit.setHint(msg);
//        } else {
//
//        }

        // ?????????????????? ???????????? ???????????? ?????????
        msgbtn = (Button) root.findViewById(R.id.msgbtn);
        msgbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showDialog(view.getContext());

            }
        });

        // '?????? ??????' ????????? ???????????? ???????????? ?????????
        gallery = (Button)root.findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ?????? ???????????? ?????? ???????????? ????????????.
                Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(in, REQUEST_IMAGE_CODE); //
            }
        });

        // '????????????' ????????? ???????????? ???????????? ?????????
        save = (Button)root.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ???????????? ???????????? ????????? ???????????? ??????
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

                                        Toast.makeText(getContext(), "????????? ?????????????????????", Toast.LENGTH_SHORT).show();
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
        });

        return root;
    }

    // ??????????????? ???????????? ????????? ????????? ?????????????????? ??????????????? ??????????????? ?????? ?????????
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE) {
            image = data.getData(); // ??????????????? ????????? ???????????? uri??? ????????????.
            try {
                // ????????? ???????????? ??????????????? ???????????? ???????????? ??????
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
                Glide.with
                        (getContext())
                        .load(image)
                        .apply(new RequestOptions().bitmapTransform(new RoundedCorners(20)))
                        .into(imageView);
               // imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // ?????????????????? ?????????????????? ?????? ?????????????????? ????????? ?????????
    void showDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_comment, null);
        EditText editText = (EditText) view.findViewById(R.id.commmentDialog_edittext);
        builder.setView(view).setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, Object> stringObjectMap = new HashMap<>();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                stringObjectMap.put("msg", editText.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).updateChildren(stringObjectMap);

            }
        }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }
}