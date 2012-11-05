package org.flixel;

import android.os.Bundle;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class FlxAndroidApplication extends AndroidApplication
{
	private FlxGame _game;
	
	public FlxAndroidApplication(FlxGame Game)
	{
		_game = Game;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initialize(_game, false);		
	}
}