package com.example.roamtechsdk;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roamtechsdk.Model.Message;
import com.example.roamtechsdk.Model.MessagePojo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
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
        int id = 0;
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


    public void start(Activity activity) {
        activity.startActivity(new Intent(activity, Supportify.class));
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

    private boolean intialize = false;
    String Key = "";

    public void changeStatusBarColor(String hexColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(hexColor));
        }
    }

    MessagesListAdapter<Message> adapter;
    private String senderId;
    int i = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:554064363401:android:061abd6d925301edad6b58")
                .setApiKey("AIzaSyC04d9IK6E1juoKjs8YlqbXKpw_qbh-9pQ")
                .setDatabaseUrl("https://summer-branch-251314.firebaseio.com/")
                .setProjectId("summer-branch-251314")
                .setStorageBucket("summer-branch-251314.appspot.com")
                .build();
        try{
            FirebaseApp.initializeApp(this /* Context */, options, "summer-branch-251314");
        }catch (IllegalStateException e)
        {
            FirebaseInstanceId.getInstance(FirebaseApp.getInstance("summer-branch-251314"));
        }
        int id = getResourceIdByName(Supportify.this.getPackageName(), "layout", "chat_activity");
        setContentView(id);
        Intent intent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(intent.getStringExtra("Title"));
        toolbar.setBackgroundColor(Color.parseColor(intent.getStringExtra("ToolBar_Color")));
        changeStatusBarColor(intent.getStringExtra("StatusBar_Color"));
        Key = intent.getStringExtra("Key");
        senderId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        messagesList = findViewById(R.id.messagesList);
        input = findViewById(R.id.input);
        adapter = new MessagesListAdapter<>(senderId, new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Glide.with(Supportify.this).load(url).into(imageView);
            }
        });
        messagesList.setAdapter(adapter);
        //check for the Key.
        FirebaseApp secondApp = FirebaseApp.getInstance("summer-branch-251314");
        FirebaseFirestore sec = FirebaseFirestore.getInstance(secondApp);
        sec.collection("Key").whereEqualTo("key1", Key).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //IF KEY IS  FOUND...
           if(task.getResult().size() != 0 )
           {
               if (getEmail() == null)
               {
                   inituserInfo();
               }
               else {

                   init();
                   initFirebase();
               }
           }
           else
               {
                   Toast.makeText(getApplicationContext(),"This chat is Disabled.",Toast.LENGTH_SHORT).show();
               }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0,1,1,"Profile");
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
                      //  init();
                    //    initFirebase();
                        if (email.length() == 0) {
                            inituserInfo();
                        } else {
                            init();
                            initFirebase();
                        }
                    }
                })
                .create().show();
    }

    //init to fetch data and display.
    public void initFirebase() {
        TimeZone tz =  TimeZone.getDefault();
        // FirebaseApp.initializeApp(Supportify.this, options, "second_database_name");
        FirebaseApp secondApp = FirebaseApp.getInstance("summer-branch-251314");
        FirebaseFirestore sec = FirebaseFirestore.getInstance(secondApp);
        final DocumentReference query = sec.collection("Contacts").document(senderId);
        Map<String, Object> taskMap = new HashMap<String, Object>();
        taskMap.put("status", "ONLINE");
        taskMap.put("name", getName());
        taskMap.put("Ip", senderId);
        taskMap.put("id",senderId);
        taskMap.put("Org","");
        taskMap.put("timezone",tz.getID());
        taskMap.put("page","");
        taskMap.put("opened","true");
        taskMap.put("idle_time",0);
        taskMap.put("max_idle_time",0);
        taskMap.put("names","Emalify-mobile-sdk");
        taskMap.put("meta", Calendar.getInstance().getTime().getTime());
        taskMap.put("email", getEmail());
        taskMap.put("token", FirebaseInstanceId.getInstance(FirebaseApp.getInstance("summer-branch-251314")).getToken());
        query.set(taskMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                query.update("status", FieldValue.serverTimestamp());
            }
        });
    }
//listens to the input
    public void init() {
        FirebaseApp secondApp = FirebaseApp.getInstance("summer-branch-251314");
        FirebaseFirestore sec = FirebaseFirestore.getInstance(secondApp);
        sec.collection("messages")
                .whereEqualTo("channel","mobile-sdk").
                whereEqualTo("email", getEmail()).
                orderBy("created_at").limit(250)
                .addSnapshotListener(
                        new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                                if(!intialize)
                                {
                                    //  intialize = true;
                                    if(!queryDocumentSnapshots.isEmpty())
                                    {
                                        if (e != null) {
                                            Log.w( "listen:error:  *** **", e);
                                            return;
                                        }
                                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                                            switch (dc.getType() ) {
                                                case ADDED:
                                                    i++;
                                                    Message message = queryDocumentSnapshots.toObjects(MessagePojo.class).get(i).getM();
                                                    if (adapter != null) {
                                                        adapter.addToStart(message, true);
                                                    }
                                                    break;
                                            }
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Welcome,type something to begin chat.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
        input.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                sendMessage(input.toString(), false);
                return true;
            }
        });
//listens for any attachments
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

//to send a message to the database
    public void sendMessage(String message, boolean isImage) {

        //get timestamp
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss ");
        String format = simpleDateFormat.format(new Date());
        FirebaseApp secondApp = FirebaseApp.getInstance("summer-branch-251314");
        FirebaseFirestore sec = FirebaseFirestore.getInstance(secondApp);
        CollectionReference reference =sec.collection("messages");
        //text
        String test = message;
        //image
        String tests = message;
        //type selection
         String type = "";
        if(isImage)
        {
            test = null;
            type = "image";
        }
        else
        {
            tests = null;
            type = "text";
        }
        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        data.put("name", getName());
        data.put("email", getEmail());
        data.put("channel", "mobile-sdk");
        data.put("contact_id", getName());
        data.put("message", test);
        data.put("text", test);
        data.put("file", "");
        data.put("image",tests);
        data.put("meta",   Calendar.getInstance().getTime().getTime());
        data.put("date",  format);
        data.put("isAdmin",  "false");
        data.put("message_id",  Calendar.getInstance().getTime().getTime());
        data.put("type",  type);
        data.put("author",getName());
        data.put("contact_id",  "");
        data.put("customer_account",  "");
        data.put("created_at", Calendar.getInstance().getTime().getTime());
        data.put("createdAt", Calendar.getInstance().getTime().getTime());
        data.put("id", senderId);
        reference.document().set(data);

    }
//to send a picture to database.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9632 && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);

            FirebaseApp secondApp = FirebaseApp.getInstance("summer-branch-251314");
            FirebaseStorage ces = FirebaseStorage.getInstance(secondApp);
            final UploadTask uploadTask;
            uploadTask =  ces.getReference("uploads/"+senderId+Calendar.getInstance().getTime().getTime()).putFile(mSelected.get(0));
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
        }
    }

    //request for accessing the mobile local gallery
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
