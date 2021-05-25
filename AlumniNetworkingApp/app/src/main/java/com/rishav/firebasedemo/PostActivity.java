package com.rishav.firebasedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.socialview.Hashtag;
import com.hendraanggrian.appcompat.widget.HashtagArrayAdapter;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.rishav.firebasedemo.Model.User;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private Uri imageUri;
    private String imageUrl;

    private ImageView close;
    private ImageView imageAdded;
    private TextView post;
    SocialAutoCompleteTextView description;
    SocialAutoCompleteTextView isJob;
    private FirebaseUser fUser;
    String profileId;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        String data = getApplicationContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId", "none");

        if (data.equals("none")) {
            profileId = fUser.getUid();
        } else {
            profileId = data;
            getApplicationContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().clear().apply();
        }



        close = findViewById(R.id.close);
        imageAdded = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);
        isJob=findViewById(R.id.isJob);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this , MainActivity.class));
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance("https://alumni-netwroking-app-default-rtdb.firebaseio.com/").getReference().child("Users").child(profileId).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user.getType_user().equals("Alumni") || user.getType_user().equals("alumni")) {
                            upload();
                        }
                        else
                        {

                            if (isJob.getText().toString().equals("Event")) {
                                upload();
                            }
                            else
                            { Toast.makeText(getApplicationContext(),isJob.getText().toString(),Toast.LENGTH_LONG).show();
                                isJob.setError("Only Alumni can post Jobs!!");


                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        CropImage.activity().start(PostActivity.this);
    }

    private void upload() {



            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Uploading");
            pd.show();

            if (imageUri != null) {
                final StorageReference filePath = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

                StorageTask uploadtask = filePath.putFile(imageUri);
                uploadtask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri downloadUri = task.getResult();
                        imageUrl = downloadUri.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance("https://alumni-netwroking-app-default-rtdb.firebaseio.com/").getReference("Posts");
                        String postId = ref.push().getKey();

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("postid", postId);
                        map.put("imageurl", imageUrl);
                        map.put("description", description.getText().toString());
                        map.put("isJob", isJob.getText().toString());
                        map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        ref.child(postId).setValue(map);

                        DatabaseReference mHashTagRef = FirebaseDatabase.getInstance("https://alumni-netwroking-app-default-rtdb.firebaseio.com/").getReference().child("HashTags");
                        List<String> hashTags = description.getHashtags();
                        if (!hashTags.isEmpty()) {
                            for (String tag : hashTags) {
                                map.clear();

                                map.put("tag", tag.toLowerCase());
                                map.put("postid", postId);

                                mHashTagRef.child(tag.toLowerCase()).child(postId).setValue(map);
                            }
                        }

                        pd.dismiss();
                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "No image was selected!", Toast.LENGTH_SHORT).show();
            }

        }


    private String getFileExtension(Uri uri) {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            imageAdded.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this , MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        final ArrayAdapter<Hashtag> hashtagAdapter = new HashtagArrayAdapter<>(getApplicationContext());

        FirebaseDatabase.getInstance("https://alumni-netwroking-app-default-rtdb.firebaseio.com/").getReference().child("HashTags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    hashtagAdapter.add(new Hashtag(snapshot.getKey() , (int) snapshot.getChildrenCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        description.setHashtagAdapter(hashtagAdapter);
    }
}
