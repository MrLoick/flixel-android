# Text Display "hello flixel" #

In this tutorial you’re going to learn how to setup starter classes and writing a very simple flixel program that displays “hello flixel”.


## Project setup ##

Follow the steps in Project setup. In this tutorial the following names are used:
Application name: helloflixel
Package name: org.flixel.helloflixel
Game class: HelloFlixel

If you used the libGDX setup UI tool, then you’ll see three java files: HelloFlixel, Main and MainActivity.

We will deal with HelloFlixel first. Delete all code inside the class and extends FlxGame.

```
package org.flixel.helloflixel;

import org.flixel.FlxGame;

public class HelloFlixel extends FlxGame 
{
	public HelloFlixel() 
	{
		super(480, 320, PlayState.class, 2);
	}
}
```
FlxGame is the heart of all flixel games, and contains a bunch of basic game loops and things. It is a long and sloppy file that you shouldn't have to worry about too much! It is basically only used to create your game object in the first place, after that FlxG and FlxState have all the useful stuff you actually need.

Now we need a state to create and orient all the important game objects. Create a PlayState class that extends FlxState.

```
package org.flixel.helloflixel;

import org.flixel.FlxState;

public class PlayState extends FlxState 
{

	@Override
	public void create() 
	{

	}
}
```

You’ll be forced to override the create method. A common mistake is that objects are created in the constructor instead of the create method.
Now we only need to display a text on the screen. Inside the create method, add this line:
```
add(new FlxText(20, 20, 100, "hello flixel"));
```
You’re done with your core project. Head over to the desktop’s starter class.
If you used the libGDX setup UI tool, then everything is already set correctly and you can launch the application. If not, here is the code of Main:

```
package org.flixel.helloflixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main 
{
	public static void main(String[] args) 
	{
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "helloflixel";
		cfg.useGL20 = false;
		cfg.width = 480;
		cfg.height = 320;
		
		new LwjglApplication(new HelloFlixel(), cfg);
	}
}
```

The last part, the Android project. Open the AndroidManifest.xml:
```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.flixel.helloflixel"
    android:versionCode="1"
    android:versionName="1.0" >

    <uses-sdk android:minSdkVersion="5" android:targetSdkVersion="15" />

    <application
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name" >
        <activity
            android:name=".MainActivity"
            android:label="@string/app_name"
            android:screenOrientation="landscape"
            android:configChanges="keyboard|keyboardHidden|orientation|screenSize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

.MainActivity is name of the start class. If you change it, don’t forget to change it in the manifest too. Please refer to the Android Developer Guide for more information about the manifest.
The start class:
```
package org.flixel.helloflixel;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication 
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
        initialize(new HelloFlixel(), cfg);
    }
}
```

Create an Android Vritual Device. flixel-android supports Android 1.5+. Run the Android project and you just finished hello flixel tutorial.