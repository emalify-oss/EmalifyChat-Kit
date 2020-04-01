# RoamChat-Kit
RoamChat library with emalify.
## First time integration 
#### Quick Steps
Project gradle file **build.gradle**
```
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

App Module gradle file **(app/build.gradle)** 
```
dependencies {
     implementation 'com.github.emalify-oss:RoamChat-Kit:0.0.4'
}
```
## Updating to newer versions of SDK
### [Changelog](https://github.com/emalify-oss/RoamChat-Kit/blob/master/CHANGELOG.mb)
## How to call the library in code.
#### Java Example
```
//Define your background image for the chat.
int resId = getResources().getIdentifier("image name", "drawable", getPackageName());
//Supportify is the library class name
Intent myIntent = new Intent( MainActivity.this, Supportify.class );
                //Add Title to the app bar
                myIntent.putExtra("Title", "Example");
                //Key_1 is the app_id (from Emalify)
                myIntent.putExtra("Key_1", "app_id is written here");
                //Key_2 is the customer_account (from Emalify)
                myIntent.putExtra("Key_2", "customer_account is written here");
                //Add the background image variable
                myIntent.putExtra("image_id_resource", resId );
                //Add the Status bar Colors
                myIntent.putExtra("StatusBar_Color", "#FF4081");
                //Add the Toolbar colors
                myIntent.putExtra("ToolBar_Color", "#FF4081");
                //Add the username
                myIntent.putExtra("name", "name here");
                //add the users email
                myIntent.putExtra("email", "email here");
                startActivity(myIntent);
```
