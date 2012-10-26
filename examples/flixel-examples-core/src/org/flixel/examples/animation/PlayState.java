package org.flixel.examples.animation;

import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.event.AFlxAnim;

/**
 * A simple demo to show how animation works.
 * 
 * @author Ka Wing Chin
 */
public class PlayState extends FlxState
{
	static public final String ImgFlixelLogo = "examples/animation/pack:flixellogo";
	static public final String ImgZombie = "examples/animation/pack:zombietxai"; // by Txai Viegas
	public static final String ImgDroid = "examples/animation/pack:droid";
	
	private FlxSprite _zombie;

	@Override
	public void create()
	{
		FlxG.setBgColor(0xFF333333);
		
		// Shiny flixel logo.
		FlxSprite s = new FlxSprite(20, 20);
		s.loadGraphic(ImgFlixelLogo, true);
		s.addAnimation("shine", new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,4}, 14);
		s.play("shine");
		add(s);
		
		
		// NOTE: the zombie image is facing to the left. Normally you should stick sprites facing to the right.
		s = new FlxSprite(20, 60).loadGraphic(ImgZombie, true, true, 36, 41);
		s.addAnimation("walk", new int[]{0, 1, 2, 3, 4, 5}, 12);
		s.setFacing(FlxSprite.RIGHT);
		s.play("walk");
		add(s);
		
		
		// Zombie with more frames added to slow down an animation.
		s = new FlxSprite(100, 60).loadGraphic(ImgZombie, true, true, 36, 41);
		s.addAnimation("head bounce", new int[]{7, 7, 8, 9, 10, 11, 11, 11, 10, 10, 10, 9, 9, 8, 8, 7, 7, 7}, 10);
		s.play("head bounce");
		add(s);
		
		
		// Another zombie, but this can move on the x-axis and there is also animation callback.
		_zombie = new FlxSprite(20, 120).loadGraphic(ImgZombie, true, true, 36, 41);
		_zombie.addAnimation("walk", new int[]{0, 1, 2, 3, 4, 5}, 2);
		_zombie.setFacing(FlxSprite.LEFT);
		_zombie.velocity.x = 5;
		_zombie.play("walk");
		_zombie.addAnimationCallback(AnimationCallback);
		add(_zombie);
		
		
		// Droid
		s = new FlxSprite(220, 120).loadGraphic(ImgDroid, true, false, 14, 14);
		s.addAnimation("walk", new int[]{0,1,2,3}, 20, true);
		s.play("walk");
		add(s);
	}

	
	AFlxAnim AnimationCallback = new AFlxAnim()
	{
		@Override
		public void callback(String curAnim, int curFrame, int curIndex)
		{
			if(curFrame == 5)
			{
				if(_zombie.getFacing() == FlxSprite.LEFT)
				{
					_zombie.setFacing(FlxSprite.RIGHT);
					_zombie.velocity.x = -5;
				}
				else
				{
					_zombie.setFacing(FlxSprite.LEFT);
					_zombie.velocity.x = 5;
				}
			}
		}
	};
}