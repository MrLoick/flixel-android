# Animation #
In this tutorial you will learn how to create an animation with flixel-android. Before that you must know how an animation works.

## Sprite sheet ##
A sprite sheet is a layout of individual sprites or frames. The sprites are placed one after the other. An animation can be achieved by playing each frame by an interval. The following image shows Mario from the NES. As you can see it contains, jumping, running, ducking animation in different Mario sizes. To make it clearer, take a look at the following image. Each box represents a frame.

![http://wingeraser.com/wp-content/uploads/2012/08/tut_animation01.png](http://wingeraser.com/wp-content/uploads/2012/08/tut_animation01.png)


## Create animation ##
In flixel-android we don’t need a sprite that facing to both sides (left and right). You can simply flip the sprite. To keep your project simple, create all sprites facing to the right. The top left corner is frame 0. Mario’s dimension is 16 x 32 px, a total of 12 frames.

![http://wingeraser.com/wp-content/uploads/2012/08/tut_animation02.png](http://wingeraser.com/wp-content/uploads/2012/08/tut_animation02.png)

## Setup frame ##
```
public class Mario extends FlxSprite
{
	protected String ImgMario = "data/pack:mario";
	
	public Mario(int X, int Y)
	{
		super(X, Y);
		loadGraphic(ImgMario, true, true, 16, 32);
		width = 16;
		height = 32;
		
		addAnimation("idle_big", new int[]{6});
		addAnimation("run_big", new int[]{7,8}, 5);
		addAnimation("break_big", new int[]{9});
		addAnimation("jump_big", new int[]{10});
		addAnimation("duck_big", new int[]{11});
		
		play("run_big");
	}
}
```
The code only shows the animation of big Mario. Let’s dissect the code.
loadGraphic, load an image from embedded graphic file.
We want it animated and reversible. Mario got the width of 16 and his height is 32 when he is in big size. While the small Mario got the height of 16.

addAnimation, adds new animation to the sprite.
Give the animation a name, the frames that belongs to the animation, the framerate and whether it needs to be looped or not. As you can see, there are two running animations of big Mario: 7 and 8.
To play the animation, simply call play(“animation name”).

## Different size ##
When we play the small Mario size, the bounding box is incorrect.

![http://wingeraser.com/wp-content/uploads/2012/08/tut_animation04.png](http://wingeraser.com/wp-content/uploads/2012/08/tut_animation04.png)

To fix this, there is a variable called offset. When you play the small animation, be sure to offset of y is set to -16.

![http://wingeraser.com/wp-content/uploads/2012/08/tut_animation03.png](http://wingeraser.com/wp-content/uploads/2012/08/tut_animation03.png)

Code of Mario class.
```
public class Mario extends FlxSprite
{
	protected String ImgMario = "data/pack:mario";
	
	public Mario(int X, int Y)
	{
		super(X, Y);
		loadGraphic(ImgMario, true, true, 16, 32);
		width = 16;
		height = 16;
		
		addAnimation("idle_small", new int[]{0});
		addAnimation("run_small", new int[]{1,2}, 5);
		addAnimation("break_small", new int[]{4});
		addAnimation("jump_small", new int[]{5}, 5);

		addAnimation("idle_big", new int[]{6});
		addAnimation("run_big", new int[]{7,8}, 5);
		addAnimation("break_big", new int[]{9});
		addAnimation("jump_big", new int[]{10}, 5);
		addAnimation("duck_big", new int[]{11}, 5);
	}
	
	public void grow()
	{
		y -= 16;
		height = 32;
		offset.y = 0;
		play("run_big");
	}
	
	public void shrink()
	{
		height = 16;
		offset.y = 16;
		play("run_small");
	}
}
```

## Callback ##
addAnimationCallback
Pass in a function to be called whenever this sprite’s animation changes.
```
_mario.addAnimationCallback(AnimationCallback);
…
AFlxSprite AnimationCallback = new AFlxSprite()
{
	@Override
	public void onAnimate(String curAnim, int curFrame, int curIndex)
	{
		// Do something
	}
};
```

## What’s next? ##
Take look at the demos [AnimationDemo](http://code.google.com/p/flixel-android/source/browse/trunk/flixel-examples/src/org/flixel/examples/animation/PlayState.java) and [Mode](http://code.google.com/p/flixel-android/source/browse/trunk/flixel-examples/src/org/flixel/examples/mode/Player.java).