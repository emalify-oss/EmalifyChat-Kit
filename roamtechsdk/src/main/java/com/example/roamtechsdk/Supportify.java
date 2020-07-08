package com.example.roamtechsdk;

import android.Manifest;
import android.app.Activity;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roamtechsdk.Model.Message;
import com.example.roamtechsdk.Model.MessagePojo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 * Created by dennis on 16/3/20.
 */
@Keep
public class Supportify extends AppCompatActivity {

    MessagesList messagesList;
    private FirebaseAuth mAuth;
    MessageInput input;

    public static void start(Activity activity) {
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
    String Key_2 = "";

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
    protected void  onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:677565980513:android:1342f1bc8ac6f756ddaf20")
                .setApiKey("AIzaSyBFliq3oTDIVSFrnrFQjqcLO5IHLvBPhgo")
                .setDatabaseUrl("https://emalifychat.firebaseio.com/")
                .setProjectId("emalifychat")
                .setStorageBucket("emalifychat.appspot.com")
                .build();
        try{
            FirebaseApp.initializeApp(this /* Context */, options, "emalifychat");
            FirebaseApp app =  FirebaseApp.initializeApp(this /* Context */, options, "emalifychat");
            mAuth = FirebaseAuth.getInstance(app);
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //  Log.d( "signInAnonymously:success");
                                Toast.makeText(getApplicationContext(), "Authentication successful.",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                //  updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                //  Log.w(TAG, "signInAnonymously:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //  updateUI(null);
                            }
                        }
                    });

        }catch (IllegalStateException e)
        {
            FirebaseInstanceId.getInstance(FirebaseApp.getInstance("emalifychat"));
        }
        setContentView(R.layout.chat_activity);
        Intent intent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(intent.getStringExtra("Title"));
        toolbar.setBackgroundColor(Color.parseColor(intent.getStringExtra("ToolBar_Color")));
        changeStatusBarColor(intent.getStringExtra("StatusBar_Color"));
        String Name = intent.getStringExtra("name");
        String Email = intent.getStringExtra("email");
        if(Name == null || Email == null)
        {
            Toast.makeText(getApplicationContext(),"Error occurred. Check the Name or email.",Toast.LENGTH_SHORT).show();
        }
        else
            {
                setName(Name);
                setEmail(Email);
            }

        Key = intent.getStringExtra("Key_1");
        Key_2 = intent.getStringExtra("Key_2");


        senderId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        messagesList = findViewById(R.id.messagesList);
        //find background image path.
        RelativeLayout free = findViewById(R.id.relative);
        int resId = this.getResources().getIdentifier("back", "drawable", this.getPackageName());
               free.setBackgroundResource(intent.getIntExtra("image_id_resource",0 ));
        input = findViewById(R.id.input);
        adapter = new MessagesListAdapter<>(senderId, new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Glide.with(Supportify.this).load(url).into(imageView);
            }
        });
        messagesList.setAdapter(adapter);
        //check for the Key.
        FirebaseApp secondApp = FirebaseApp.getInstance("emalifychat");
        FirebaseFirestore sec = FirebaseFirestore.getInstance(secondApp);
        sec.collection("applications").whereEqualTo("app_id", Key)
                .whereEqualTo("customer_account",Integer.parseInt(Key_2))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                       {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                   if (getEmail() == null)
                                                   {
                                                       Toast.makeText(getApplicationContext(),"Error occurred. Check the Name or email.",Toast.LENGTH_SHORT).show();
                                                       onBackPressed();
                                                   }
                                                   else
                                                   {
                                                       init();
                                                       initFirebase();
                                                   }
                                           }
                                       }
                );
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == 1) {
//            inituserInfo();
//            return true;
//        } else if (id == 2) {
//            //logout();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//    //AlertDialog
//    private void inituserInfo() {
//        View view = getLayoutInflater().inflate(R.layout.supportify_dialog, null);
//        final EditText name = view.findViewById(R.id.username);
//        final EditText email = view.findViewById(R.id.email);
//        email.setText(getEmail());
//        name.setText(getName());
//        new AlertDialog.Builder(this)
//                .setView(view)
//                .setTitle("Please provide your information.")
//                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //first check the email before being able to continue.
//                        setName(name.getText().toString());
//                        setEmail(email.getText().toString());
//                        if (email.length() == 0) {
//                            inituserInfo();
//                        } else {
//                            init();
//                            initFirebase();
//                        }
//                    }
//                })
//                .create().show();
//    }


    //init to fetch data and display.
    private void initFirebase() {

        FirebaseApp secondApp = FirebaseApp.getInstance("emalifychat");
        FirebaseFirestore sec = FirebaseFirestore.getInstance(secondApp);
        final DocumentReference query = sec.collection("contacts").document(senderId);
        final Map<String, Object> taskMap = new HashMap<String, Object>();
        taskMap.put("status", "ONLINE");
        taskMap.put("name", getName());
        taskMap.put("channel", "mobilechat");
        taskMap.put("Ip", senderId);
        taskMap.put("id",senderId);
        taskMap.put("contact_id", senderId+Key_2);
        taskMap.put("customer_account", Integer.parseInt(Key_2));
        taskMap.put("Org","");
        taskMap.put("created_at", String.valueOf(Calendar.getInstance().getTime().getTime()).substring(0,10));
        taskMap.put("updated_at", String.valueOf(Calendar.getInstance().getTime().getTime()).substring(0,10));
        taskMap.put("timezone",TimeZone.getDefault().getID());
        taskMap.put("page","");
        taskMap.put("opened","true");
        taskMap.put("idle_time",0);
        taskMap.put("max_idle_time",0);
        taskMap.put("names","Emalify-mobile-sdk");
        taskMap.put("meta",  String.valueOf(Calendar.getInstance().getTime().getTime()).substring(0,10));
        taskMap.put("email", getEmail());

        query.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                    }
                    else {
                        query.set(taskMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                query.update("status", FieldValue.serverTimestamp());
                            }
                        });
                    }
                } else {

                }
            }
        });

    }
    //listens to the input
    private void init() {
        FirebaseApp secondApp = FirebaseApp.getInstance("emalifychat");
        final FirebaseFirestore sec = FirebaseFirestore.getInstance(secondApp);
        sec.collection("messages")
                .whereEqualTo("contact_id",senderId+Key_2).
                orderBy("message_id").limit(150)
                .addSnapshotListener(
                        new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                                //check whether the database is empty.
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
                                                try {
                                                    Message message = queryDocumentSnapshots.toObjects(MessagePojo.class).get(i).getM();
                                                    if(adapter != null)
                                                    {
                                                        adapter.addToStart(message, true);
                                                    }
                                                    break;
                                                }
                                                catch (IllegalAccessError w)
                                                {
                                                    Toast.makeText(getApplicationContext(),"Error occurred.",Toast.LENGTH_SHORT).show();
                                                }
                                        }
                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Welcome,type something to begin chat.",Toast.LENGTH_SHORT).show();
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
    private void sendMessage(String message, boolean isImage) {

        //get timestamp
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss ");
        String format = simpleDateFormat.format(new Date());
        FirebaseApp secondApp = FirebaseApp.getInstance("emalifychat");
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
        boolean intialize = false;
        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        data.put("name", getName());
        data.put("email", getEmail());
        data.put("channel", "mobilechat");
        data.put("contact_id", senderId+Key_2);
        data.put("message", test);
        data.put("text", test);
        data.put("file", "");
        data.put("url",tests);
        data.put("image",tests);
        data.put("meta", String.valueOf(Calendar.getInstance().getTimeInMillis()).substring(0,10));
        data.put("date", format);
        data.put("isAdmin",  intialize );
        data.put("message_id", String.valueOf(Calendar.getInstance().getTimeInMillis()).substring(0,10));
        data.put("type",  type);
        data.put("author",getName());
        data.put("customer_account",  Integer.parseInt(Key_2));
        data.put("created_at", String.valueOf(Calendar.getInstance().getTimeInMillis()).substring(0,10));
        data.put("createdAt", String.valueOf(Calendar.getInstance().getTimeInMillis()).substring(0,10));
        data.put("id", senderId);
        reference.document().set(data);



    }
    //to send a picture to database.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9632 && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            FirebaseApp secondApp = FirebaseApp.getInstance("emalifychat");
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
