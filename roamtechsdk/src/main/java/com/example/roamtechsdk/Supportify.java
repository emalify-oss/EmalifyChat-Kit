package com.example.roamtechsdk;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roamtechsdk.Model.Message;
import com.example.roamtechsdk.Model.MessagePojo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 * Created by android on 14/8/17.
 */
@Keep
public class Supportify extends AppCompatActivity {

    MessagesList messagesList;
    MessageInput input;
    public static int getResourceIdByName(String packageName, String className, String name) {
        Class r = null;
        int id = -1;
        try {
            r = Class.forName(packageName + ".R");

            Class[] classes = r.getClasses();
            Class desireClass = null;

            for (int i = 0; i < classes.length; i++) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];

                    break;
                }
            }

            if (desireClass != null) {
                id = desireClass.getField(name).getInt(desireClass);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return id;
    }


    public  void start(Activity activity){
        activity.startActivity(new Intent(activity,Supportify.class));
    }

    private String getName() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Supportify.this);
        return sp.getString("name", null);
    }

    private void setName(String name) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Supportify.this);
        sp.edit().putString("name", name).apply();

    }

    private String getEmail() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Supportify.this);
        return sp.getString("email", null);
    }

    private void setEmail(String email) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Supportify.this);
        sp.edit().putString("email", email).apply();
    }

    private boolean intialize;
    MessagesListAdapter<Message> adapter;
    private String senderId;
    int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getResourceIdByName(Supportify.this.getPackageName(), "layout", "chat_activity");
        setContentView(id);
//        WallpaperInfo context = null;
      //  int ids = getResourceIdByName(Supportify.this.getPackageName(), "ToolBar", "toolbar");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        senderId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        int idz = getResourceIdByName(Supportify.this.getPackageName(), "MessageList", "messagesList");
        messagesList = findViewById(R.id.messagesList);
        input = findViewById(R.id.input);
        adapter = new MessagesListAdapter<>(senderId, new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Glide.with(Supportify.this).load(url).into(imageView);
            }
        });
        messagesList.setAdapter(adapter);
        if (getEmail() == null) {
            inituserInfo();
        } else {
            init();
            initFirebase();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0,1,1,"Profile");
        menu.add(0,2,1,"Logout");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == 1) {
            inituserInfo();
            return true;
        } else if (id == 2) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
//        FirebaseFirestore.getInstance().collection("Chat").document(senderId).
//                delete(new DatabaseReference.CompletionListener() {
//                    @Override
//                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                        finish();
//                    }
//                });
    }
    //AlertDialog
    public void inituserInfo() {
        View view = getLayoutInflater().inflate(R.layout.supportify_dialog, null);
        final EditText name = view.findViewById(R.id.username);
        final EditText email = view.findViewById(R.id.email);
        email.setText(getEmail());
        name.setText(getName());
        new AlertDialog.Builder(this)
                .setView(view)
                .setTitle("Please provide your information.")
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setName(name.getText().toString());
                        setEmail(email.getText().toString());
                        init();
                      //  initFirebase();
//                        if (email.length() == 0) {
//                            inituserInfo();
//                        } else {
//                            init();
//                            initFirebase();
//                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).create().show();
    }

    public void initFirebase() {
        final DocumentReference query = FirebaseFirestore.getInstance().collection("Users").document(senderId);
        Map<String, Object> taskMap = new HashMap<String, Object>();
        taskMap.put("status", "ONLINE");
        taskMap.put("name", getName());
        taskMap.put("email", getEmail());
        taskMap.put("token", FirebaseInstanceId.getInstance().getToken());
        query.set(taskMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                query.update("status", FieldValue.serverTimestamp());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseFirestore.getInstance();
    }

    public void init() {
        FirebaseFirestore.getInstance()
                .collection("Chat")
                .whereEqualTo("channel",getEmail()).
                orderBy("createdAt").limit(60)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                       
                      
                            //  intialize = true;
                            // DocumentSnapshot documentSnapshot = new DocumentSnapshot();
                            //   Log.d("Here it is ********",queryDocumentSnapshots.getDocumentChanges().toString());
                            if(!queryDocumentSnapshots.isEmpty())
                            {
//                        Message chat = null;
//                                for (int i =0; 8 < i ; i++)
//                                {
//                                     chat = queryDocumentSnapshots.getDocuments().get(i).toObject(MessagePojo.class).getM();
//
//                                }
//                        if (adapter != null) {
//                            adapter.addToStart(chat, true);
//                        }
                        Message chat = queryDocumentSnapshots.getDocuments().get(1).toObject(MessagePojo.class).getM();
//                                                if (adapter != null) {
//                            adapter.addToStart(chat, true);
//                        }
                                if (e != null) {
                                    Log.w( "listen:error:  *** **", e);
                                    return;
                                }
                                // Message chat = queryDocumentSnapshots.getDocumentChanges().toObject(MessagePojo.class).getM();
//                                 for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
//                                     switch (dc.getType() ) {
//                                         case ADDED:
//                                             Message message = queryDocumentSnapshots.toObjects(MessagePojo.class).get(i).getM();
//                                             i++;
//                                             if (adapter != null) {
//                                                 adapter.addToStart(message, true);
//                                             }
//                                             if (adapter!= null)
//                                             {

//                                             }
//                                             break;
//                                     }
//                                 }
                            }
                        
                    }
                });
        //FirebaseFirestore.getInstance().collection("Chat").whereEqualTo("setID", senderId).get().
        //  final DocumentSnapshot documentSnapshot = null;
//        FirebaseFirestore.getInstance()
//                .collection("Chat").
//                        document(senderId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(!intialize)
//                {
//                    intialize = true;
//                    // DocumentSnapshot documentSnapshot = new DocumentSnapshot();
//                    Log.w("Here it is ********",documentSnapshot.getData().toString());
//                    if(documentSnapshot.exists())
//                    {
//                        Message chat = documentSnapshot.toObject(MessagePojo.class).getM();
//                        if (adapter != null) {
//                            adapter.addToStart(chat, true);
//                        }
//                    }
//                }
//            }
//        });
//        (new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//
//            }
//        });
        input.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                sendMessage(input.toString(), false);
                return true;
            }
        });

        input.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {
                //select attachments

                if (ContextCompat.checkSelfPermission(Supportify.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(Supportify.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(Supportify.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                8888);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    Matisse.from(Supportify.this)
                            .choose(MimeType.allOf())
                            .countable(true)
                            .maxSelectable(1)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .forResult(9632);
                }
            }
        });
    }


    public void sendMessage(String message, boolean isImage) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Chat");
        //text
        String test = message;
        //image
        String tests = message;
        if(isImage)
        {
            test = null;
        }
        else
        {
            tests = null;
        }
        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        data.put("name", getName());
        data.put("channel", getEmail());
        data.put("contact_id", getName());
        data.put("text", test);
        data.put("file", "");
        data.put("image",tests);
      //  data.put("type", message);
      //  data.put("meta",   Calendar.getInstance().getTime().getTime()  );
        data.put("createdAt", Calendar.getInstance().getTime().getTime());
        data.put("id", senderId);
        reference.document().set(data);
//        init();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9632 && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            final UploadTask uploadTask;
            uploadTask =  FirebaseStorage.getInstance().getReference("uploads/"+senderId).putFile(mSelected.get(0));
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download_url = uri.toString();
                            sendMessage(download_url, true);
                        }
                    });
                }
            });
//            FirebaseStorage.getInstance().getReference("uploads/"+senderId).putFile(
//                    mSelected.get(0)
//            ).addOnCompleteListener(
//                    new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        sendMessage(task.getResult().getDownloadUrl().toString(), true);
//                    }
//                }
//            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 8888: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Matisse.from(Supportify.this)
                            .choose(MimeType.allOf())
                            .countable(true)
                            .maxSelectable(1)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .forResult(9632);
                } else {
                    Toast.makeText(this, "Please approve permission to user this feature.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
