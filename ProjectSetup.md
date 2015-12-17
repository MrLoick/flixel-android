# Project Setup #

Note - This guide assumes you already have Eclipse and the Android SDK installed and working on your computer. If not, follow this first: http://developer.android.com/sdk/index.html



# Using the GUI #

flixel-android now comes with a gui utility to help simplify the setup process. We would reccommend this option for most users.

## Setup ##

  1. Download the latest "setup-ui" jar from the downloads page: http://code.google.com/p/flixel-android/downloads/list
  1. Run the jar by double clicking on it.
  1. Specify your projects properties using the text fields.
  1. Click "Open the generation screen".
  1. Click "Launch" to start downloading the required libraries and automatically set up your projects.
  1. Open Eclipse and import the generated projects into your workspace:
    * File -> Import -> Existing Projects into Workspace
    * Click "Browse", select the folder containing the generated projects.
    * Make sure all the projects are checked, then click "Finish"

## Running Your Application ##

  * Desktop: Right click the desktop project, Run As -> Java Application. Select the desktop starter class (e.g. Main.java).
  * Android: make sure you have either a device connected or an emulator installed. Right click your Android project, Run As -> Android Application.

# From SVN #

If you want to ensure that you are using the very latest version of flixel-android, you will need to checkout the most recent revision from the SVN and set up your project manually.

  1. Use your preferred SVN client to checkout the repository at: http://flixel-android.googlecode.com/svn/trunk/
    * If you do not have a preferred client, a free one is available from: http://tortoisesvn.net/downloads.html
  1. Once checked out, open Eclipse and import the projects into your workspace:
    * File -> Import -> Existing Projects into Workspace
    * Click "Browse", select the folder containing the projects
    * Make sure all the projects are checked, then click "Finish"

**Note: Java 1.7 is currently problematic in combination with Android. Please make sure your project uses Java 1.6**

## Core Project Setup ##

  1. Create a new Java project in Eclipse: File -> New -> Project -> Java Project. Give it a name and click Finish.
  1. In Eclipse, right click the project -> Properties -> Java Build Path -> Projects -> Add... -> select flixel-core and click OK.

## Desktop Project Setup ##

  1. Create a new Java project in Eclipse: File -> New -> Project -> Java Project. Name it appropriately (eg, "gamename-desktop") and click Finish.
  1. In Eclipse, right click the project -> Properties -> Java Build Path -> Projects -> Add... -> select flixel-desktop AND your core project and click OK.

## Android Project Setup ##

  1. Create a new Android project in Eclipse: File -> New -> Project -> Android Project. Name it appropriately (eg, "gamename-android"). For build target, check "Android 1.5". Specify a package name (eg, "com.gamename"). Next to "Create Activity" enter "AndroidGame". Click Finish.
  1. In Eclipse, right click the project -> Properties -> Android. In the Library section click on Add..., select flixel-android and click OK.
  1. Still in Properties, go to Java Build Path -> Projects -> Add... -> check your core project and click OK.
  1. Click the Order and Export tab, check your core project.

## Asset Folder Setup ##

The Android project has a subfolder named assets, which was created automatically. Files available to the Android application must be placed here. This is problematic, because these same files must be available to the desktop application. Rather than maintain two copies of all the files, the desktop project can be configured to find the assets in the Android project:

  1. In Eclipse, right click your desktop project -> Properties -> Java Build Path -> Source tab.
  1. Click Link Source, Browse, select the "assets" folder from your Android project and click OK.
  1. Specify "assets" for the folder name and click Finish then OK.

_Note: If your desktop and Android projects are in the same parent folder, you can use "PARENT-1-PROJECT\_LOC/gamename-android/assets" for the location of the linked assets folder, where "gamename-android" is the name of your Android project. This is better than a hardcoded path if you plan on sharing your projects with others._

## Creating a Game ##

  1. In your core project, create a new class (right click the project -> New -> Class). Name it `PlayState` and specify a package. Set its superclass to `FlxState` and click OK.
  1. Create another new class. Name it `Game`, set its superclass to `FlxGame` and click OK.
  1. Add a constructor to the `Game` class so that it looks like this:

```
package com.yourname.flixelgame;

import org.flixel.FlxGame;

public class Game extends FlxGame
{
    public Game()
    {
        super(400, 240, PlayState.class, 2);
    }
}
```

## Running the Game on the Desktop ##

  1. Right click the desktop project -> New -> Class. Name it `DesktopGame` and specify a package (eg, "com.gamename"). Click OK. Make the class look like this:

```
package com.yourname.flixelgame;

import org.flixel.FlxDesktopApplication;

public class Main
{
    public static void main(String[] args) 
    {
        new FlxDesktopApplication(new FlixelGame(), 800, 480);
    }
}
```

2. To run the game on the desktop, right click the project -> Debug As -> Java Application. You should get a black window.

## Running the Game on Android ##

  1. Open the `AndroidGame` class in the Android project that was automatically created and make it look like this:

```
package com.yourname.flixelgame;

import org.flixel.FlxAndroidApplication;

public class AndroidGame extends FlxAndroidApplication 
{
    public AndroidGame()
    {
        super(new Game());
    }
}
```

2. To run the game on Android, right click the project -> Debug As -> Android Application. The screen will turn black, since the game doesn't yet do anything. If any errors occur running the application, they show up in the Logcat view, which can be seen by clicking Window -> Show View -> Other -> Android -> Logcat.