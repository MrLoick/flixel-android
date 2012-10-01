package org.flixel;

import org.flixel.event.AFlxAnim;
import org.flixel.system.FlxAnim;

import com.badlogic.gdx.graphics.ManagedTextureData;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * The main "game object" class, the sprite is a <code>FlxObject</code>
 * with a bunch of graphics options and abilities, like animation and stamping.
 * 
 * @author	Ka Wing Chin
 * @author	Thomas Weston
 */
public class FlxSprite extends FlxObject
{
	protected String ImgDefault = "org/flixel/data/pack:default";
	
	/**
	 * WARNING: The origin of the sprite will default to its center.
	 * If you change this, the visuals and the collisions will likely be
	 * pretty out-of-sync if you do any rotation.
	 */
	public FlxPoint origin;
    /**
	 * If you changed the size of your sprite object after loading or making the graphic,
	 * you might need to offset the graphic away from the bound box to center it the way you want.
	 */
	public FlxPoint offset;
	
	/**
	 * Change the size of your sprite's graphic.
	 * NOTE: Scale doesn't currently affect collisions automatically,
	 * you will need to adjust the width, height and offset manually.
	 * WARNING: scaling sprites decreases rendering performance for this sprite by a factor of 10x!
	 */
	public FlxPoint scale;
	/**
	 * Blending modes, just like Photoshop or whatever.
	 * E.g. "multiply", "screen", etc.
	 * @default null
	 */
	public int[] blend;
	/**
	 * Controls whether the object is smoothed when rotated, affects performance.
	 * @default false
	 */
	public boolean antialiasing;
	/**
	 * Whether the current animation has finished its first (or only) loop.
	 */
	public boolean finished;
	/**
	 * The width of the actual graphic or image being displayed (not necessarily the game object/bounding box).
	 * NOTE: Edit at your own risk!!  This is intended to be read-only.
	 */
	public int frameWidth;
	/**
	 * The height of the actual graphic or image being displayed (not necessarily the game object/bounding box).
	 * NOTE: Edit at your own risk!!  This is intended to be read-only.
	 */
	public int frameHeight;
	/**
	 * The total number of frames in this image.  WARNING: assumes each row in the sprite sheet is full!
	 */
	public int frames;
	/**
	 * The actual Flash <code>BitmapData</code> object representing the current display state of the sprite.
	 */
	public Sprite framePixels;
	/**
	 * Set this flag to true to force the sprite to update during the draw() call.
	 * NOTE: Rarely if ever necessary, most sprite operations will flip this flag automatically.
	 */
	public boolean dirty;
	
	/**
	 * Internal, stores all the animations that were added to this sprite.
	 */
	protected Array<FlxAnim> _animations;
	/**
	 * Internal, keeps track of whether the sprite was loaded with support for automatic reverse/mirroring.
	 */
	protected int _flipped;
	/**
	 * Internal, keeps track of the current animation being played.
	 */
	protected FlxAnim _curAnim;
	/**
	 * Internal, keeps track of the current frame of animation.
	 * This is NOT an index into the tile sheet, but the frame number in the animation object.
	 */
	protected int _curFrame;
	/**
	 * Internal, keeps track of the current index into the tile sheet based on animation or rotation.
	 */
	protected int _curIndex;
	/**
	 * Internal, used to time each frame of animation.
	 */
	protected float _frameTimer;
	/**
	 * Internal tracker for the animation callback.  Default is null.
	 * If assigned, will be called each time the current frame changes.
	 * A function that has 3 parameters: a string name, a uint frame number, and a uint frame index.
	 */
	protected AFlxAnim _callback;
	/**
	 * Internal tracker for what direction the sprite is currently facing, used with Flash getter/setter.
	 */
	protected int _facing;
	/**
	 * Internal tracker for opacity, used with Flash getter/setter.
	 */
	protected float _alpha;
	/**
	 * Internal tracker for color tint, used with Flash getter/setter.
	 */
	protected int _color;
	/**
	 * Internal tracker for how many frames of "baked" rotation there are (if any).
	 */
	protected float _bakedRotation;
	/**
	 * Internal, stores the entire source graphic (not the current displayed animation frame), used with Flash getter/setter.
	 */
	protected TextureRegion _pixels;
	/**
	 * Internal tracker for reloading the texture if its pixmap has been modified.
	 */
	protected ManagedTextureData _newTextureData;
	
	/**
	 * Creates a white 8x8 square <code>FlxSprite</code> at the specified position.
	 * Optionally can load a simple, one-frame graphic instead.
	 * 
	 * @param	X				The initial X position of the sprite.
	 * @param	Y				The initial Y position of the sprite.
	 * @param	SimpleGraphic	The graphic you want to display (OPTIONAL - for simple stuff only, do NOT use for animated images!).
	 */
	public FlxSprite(float X, float Y, String SimpleGraphic)
	{
		super(X, Y);
		
		offset = new FlxPoint();
		origin = new FlxPoint();
		
		scale = new FlxPoint(1f,1f);
		_alpha = 1f;
		_color = 0x00ffffff;
		blend = null;
		antialiasing = false;
		cameras = null;
		
		finished = false;
		_facing = RIGHT;
		_animations = new Array<FlxAnim>();
		_flipped = 0;
		_curAnim = null;
		_curFrame = 0;
		_curIndex = 0;
		_frameTimer = 0;
		
		_callback = null;
		_newTextureData = null;
		
		if(SimpleGraphic == null)
			SimpleGraphic = ImgDefault;
		loadGraphic(SimpleGraphic);
	}
		
	/**
	 * Creates a white 8x8 square <code>FlxSprite</code> at the specified position.
	 * Optionally can load a simple, one-frame graphic instead.
	 * 
	 * @param	X				The initial X position of the sprite.
	 * @param	Y				The initial Y position of the sprite.
	 */
	public FlxSprite(float X, float Y)
	{
		this(X, Y, null);
	}
	
	/**
	 * Creates a white 8x8 square <code>FlxSprite</code> at the specified position.
	 * Optionally can load a simple, one-frame graphic instead.
	 * 
	 * @param	X				The initial X position of the sprite.
	 */
	public FlxSprite(float X)
	{
		this(X, 0, null);
	}
	
	/**
	 * Creates a white 8x8 square <code>FlxSprite</code> at the specified position.
	 * Optionally can load a simple, one-frame graphic instead.
	 */
	public FlxSprite()
	{
		this(0, 0, null);
	}
	
	/**
	 * Clean up memory.
	 */
	@Override
	public void destroy()
	{
		super.destroy();
		
		if(_animations != null)
		{
			FlxAnim a;
			int i = 0;
			int l = _animations.size;
			while(i < l)
			{
				a = _animations.get(i++);
				if(a != null)
					a.destroy();
			}
			_animations = null;
		}
		
		offset = null;
		origin = null;
		scale = null;
		_curAnim = null;
		_callback = null;
		_newTextureData = null;
		framePixels = null;
	}
	
	/**
	 * Load an image from an embedded graphic file.
	 * 
	 * @param	Graphic		The image you want to use.
	 * @param	Animated	Whether the Graphic parameter is a single sprite or a row of sprites.
	 * @param	Reverse		Whether you need this class to generate horizontally flipped versions of the animation frames.
	 * @param	Width		Optional, specify the width of your sprite (helps FlxSprite figure out what to do with non-square sprites or sprite sheets).
	 * @param	Height		Optional, specify the height of your sprite (helps FlxSprite figure out what to do with non-square sprites or sprite sheets).
	 * @param	Unique		Optional, whether the graphic should be a unique instance in the graphics cache.  Default is false.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite loadGraphic(String Graphic,boolean Animated,boolean Reverse,int Width,int Height,boolean Unique)
	{
		_bakedRotation = 0;
		_pixels = FlxG.addBitmap(Graphic,Reverse,Unique);
		if(Reverse)
			_flipped = _pixels.getRegionWidth()>>1;
		else
			_flipped = 0;
		if(Width == 0)
		{
			if(Animated)
				Width = _pixels.getRegionHeight();
			else
				Width = _pixels.getRegionWidth();
		}
		width = frameWidth = Width;
		if(Height == 0)
		{
			if(Animated)
				Height = (int) width;
			else
				Height = _pixels.getRegionHeight();
		}
		height = frameHeight = Height;
		resetHelpers();
		return this;
	}	
	
	/**
	 * Load an image from an embedded graphic file.
	 * 
	 * @param	Graphic		The image you want to use.
	 * @param	Animated	Whether the Graphic parameter is a single sprite or a row of sprites.
	 * @param	Reverse		Whether you need this class to generate horizontally flipped versions of the animation frames.
	 * @param	Width		Optional, specify the width of your sprite (helps FlxSprite figure out what to do with non-square sprites or sprite sheets).
	 * @param	Height		Optional, specify the height of your sprite (helps FlxSprite figure out what to do with non-square sprites or sprite sheets).
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite loadGraphic(String Graphic,boolean Animated,boolean Reverse,int Width,int Height)
	{
		return loadGraphic(Graphic, Animated, Reverse, Width, Height, false);
	}
	
	/**
	 * Load an image from an embedded graphic file.
	 * 
	 * @param	Graphic		The image you want to use.
	 * @param	Animated	Whether the Graphic parameter is a single sprite or a row of sprites.
	 * @param	Reverse		Whether you need this class to generate horizontally flipped versions of the animation frames.
	 * @param	Width		Optional, specify the width of your sprite (helps FlxSprite figure out what to do with non-square sprites or sprite sheets).
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite loadGraphic(String Graphic,boolean Animated,boolean Reverse,int Width)
	{
		return loadGraphic(Graphic, Animated, Reverse, Width, 0, false);
	}
		
	/**
	 * Load an image from an embedded graphic file.
	 * 
	 * @param	Graphic		The image you want to use.
	 * @param	Animated	Whether the Graphic parameter is a single sprite or a row of sprites.
	 * @param	Reverse		Whether you need this class to generate horizontally flipped versions of the animation frames.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite loadGraphic(String Graphic,boolean Animated,boolean Reverse)
	{
		return loadGraphic(Graphic, Animated, Reverse, 0, 0, false);
	}
	
	/**
	 * Load an image from an embedded graphic file.
	 * 
	 * @param	Graphic		The image you want to use.
	 * @param	Animated	Whether the Graphic parameter is a single sprite or a row of sprites.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite loadGraphic(String Graphic,boolean Animated)
	{
		return loadGraphic(Graphic, Animated, false, 0, 0, false);
	}
		
	/**
	 * Load an image from an embedded graphic file.
	 * 
	 * @param	Graphic		The image you want to use.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite loadGraphic(String Graphic)
	{
		return loadGraphic(Graphic, false, false, 0, 0, false);
	}
	
	/**
	 * Create a pre-rotated sprite sheet from a simple sprite.
	 * This can make a huge difference in graphical performance!
	 * 
	 * @param	Graphic			The image you want to rotate and stamp.
	 * @param	Rotations		The number of rotation frames the final sprite should have.  For small sprites this can be quite a large number (360 even) without any problems.
	 * @param	Frame			If the Graphic has a single row of square animation frames on it, you can specify which of the frames you want to use here.  Default is -1, or "use whole graphic."
	 * @param	AntiAliasing	Whether to use high quality rotations when creating the graphic.  Default is false.
	 * @param	AutoBuffer		Whether to automatically increase the image size to accommodate rotated corners.  Default is false.  Will create frames that are 150% larger on each axis than the original frame or graphic.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite loadRotatedGraphic(String Graphic, int Rotations, int Frame, boolean AntiAliasing, boolean AutoBuffer)
	{
		_bakedRotation = 0;
		_pixels = FlxG.addBitmap(Graphic);
		if(Frame >= 0)
		{
			width = frameWidth = _pixels.getRegionHeight();
			int rx = (int) (Frame*width);
			int ry = 0;
			int fw = _pixels.getRegionWidth();
			if(rx >= fw)
			{
				ry = (int) ((rx/fw)*width);
				rx %= fw;
			}
			_pixels.setRegion(rx + _pixels.getRegionX(), ry + _pixels.getRegionY(), (int) width, (int) width);
		}
		else
			width = frameWidth = _pixels.getRegionWidth();
		
		height = frameHeight = _pixels.getRegionHeight();
		resetHelpers();
		
		return this;
	}
		
	/**
	 * Create a pre-rotated sprite sheet from a simple sprite.
	 * This can make a huge difference in graphical performance!
	 * 
	 * @param	Graphic			The image you want to rotate and stamp.
	 * @param	Rotations		The number of rotation frames the final sprite should have.  For small sprites this can be quite a large number (360 even) without any problems.
	 * @param	Frame			If the Graphic has a single row of square animation frames on it, you can specify which of the frames you want to use here.  Default is -1, or "use whole graphic."
	 * @param	AntiAliasing	Whether to use high quality rotations when creating the graphic.  Default is false.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite loadRotatedGraphic(String Graphic, int Rotations, int Frame, boolean AntiAliasing)
	{
		return loadRotatedGraphic(Graphic, Rotations, Frame, AntiAliasing, false);
	}
	
	/**
	 * Create a pre-rotated sprite sheet from a simple sprite.
	 * This can make a huge difference in graphical performance!
	 * 
	 * @param	Graphic			The image you want to rotate and stamp.
	 * @param	Rotations		The number of rotation frames the final sprite should have.  For small sprites this can be quite a large number (360 even) without any problems.
	 * @param	Frame			If the Graphic has a single row of square animation frames on it, you can specify which of the frames you want to use here.  Default is -1, or "use whole graphic."
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite loadRotatedGraphic(String Graphic, int Rotations, int Frame)
	{
		return loadRotatedGraphic(Graphic, Rotations, Frame, false, false);
	}
	
	/**
	 * Create a pre-rotated sprite sheet from a simple sprite.
	 * This can make a huge difference in graphical performance!
	 * 
	 * @param	Graphic			The image you want to rotate and stamp.
	 * @param	Rotations		The number of rotation frames the final sprite should have.  For small sprites this can be quite a large number (360 even) without any problems.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite loadRotatedGraphic(String Graphic, int Rotations)
	{
		return loadRotatedGraphic(Graphic, Rotations, -1, false, false);
	}
	
	/**
	 * Create a pre-rotated sprite sheet from a simple sprite.
	 * This can make a huge difference in graphical performance!
	 * 
	 * @param	Graphic			The image you want to rotate and stamp.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite loadRotatedGraphic(String Graphic)
	{
		return loadRotatedGraphic(Graphic, 16, -1, false, false);
	}
	
	/**
	 * This function creates a flat colored square image dynamically.
	 * 
	 * @param	Width		The width of the sprite you want to generate.
	 * @param	Height		The height of the sprite you want to generate.
	 * @param	Color		Specifies the color of the generated block.
	 * @param	Unique		Whether the graphic should be a unique instance in the graphics cache.  Default is false.
	 * @param	Key			Optional parameter - specify a string key to identify this graphic in the cache.  Trumps Unique flag.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite makeGraphic(int Width, int Height, int Color, boolean Unique, String Key)
	{
		_bakedRotation = 0;
		_pixels = FlxG.createBitmap(Width,Height,Color,Unique,Key);
		width = frameWidth = Width;
		height = frameHeight = Height;
		resetHelpers();
		return this;
	}
	
	/**
	 * This function creates a flat colored square image dynamically.
	 * 
	 * @param	Width		The width of the sprite you want to generate.
	 * @param	Height		The height of the sprite you want to generate.
	 * @param	Color		Specifies the color of the generated block.
	 * @param	Unique		Whether the graphic should be a unique instance in the graphics cache.  Default is false.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite makeGraphic(int Width, int Height, int Color, boolean Unique)
	{
		return makeGraphic(Width, Height, Color, Unique, null);
	}
	
	/**
	 * This function creates a flat colored square image dynamically.
	 * 
	 * @param	Width		The width of the sprite you want to generate.
	 * @param	Height		The height of the sprite you want to generate.
	 * @param	Color		Specifies the color of the generated block.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite makeGraphic(int Width, int Height, int Color)
	{
		return makeGraphic(Width, Height, Color, false, null);
	}
	
	/**
	 * This function creates a flat colored square image dynamically.
	 * 
	 * @param	Width		The width of the sprite you want to generate.
	 * @param	Height		The height of the sprite you want to generate.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite makeGraphic(int Width, int Height)
	{
		return makeGraphic(Width, Height, 0xffffffff, false, null);
	}
	
	/**
	 * Resets some important variables for sprite optimization and rendering.
	 */
	protected void resetHelpers()
	{			
		if(framePixels == null)
			framePixels = new Sprite();
		
		framePixels.setRegion(_pixels, 0, 0, frameWidth, frameHeight);
		framePixels.setSize(frameWidth, frameHeight);
		framePixels.flip(false, true);
		origin.make(frameWidth*0.5f,frameHeight*0.5f);
		frames = (int) ((_pixels.getRegionWidth() / frameWidth) * (_pixels.getRegionHeight() / frameHeight));
		_curIndex = 0;
	}
	
	/**
	 * Automatically called after update() by the game loop,
	 * this function just calls updateAnimation().
	 */
	@Override
	public void postUpdate()
	{		
		super.postUpdate();
		updateAnimation();
	}
	
	/**
	 * Called by game loop, updates then blits or renders current frame of animation to the screen
	 */
	@Override
	public void draw()
	{
		if(_flickerTimer != 0)
		{
			_flicker = !_flicker;
			if(_flicker)
				return;
		}
		
		if(dirty)	//rarely 
			calcFrame();
		
		if(_newTextureData != null)	//even more rarely
		{
			_pixels.getTexture().load(_newTextureData);
			_newTextureData = null;
		}
		
		FlxCamera camera = FlxG._activeCamera;
		
		if (cameras != null && !cameras.contains(camera, true))
			return;
		
		if (!onScreen(camera))
			return;
		_point.x = x - (camera.scroll.x * scrollFactor.x) - offset.x;
		_point.y = y - (camera.scroll.y * scrollFactor.y) - offset.y;
		_point.x += (_point.x > 0) ? 0.0000001f : -0.0000001f;
		_point.y += (_point.y > 0) ? 0.0000001f : -0.0000001f;
		
		//tinting
		int tintColor = FlxU.multiplyColors(_color, camera.getColor());
		framePixels.setColor(((tintColor >> 16) & 0xFF) * 0.00392f, ((tintColor >> 8) & 0xFF) * 0.00392f, (tintColor & 0xFF) * 0.00392f, _alpha);

		if(((angle == 0) || (_bakedRotation > 0)) && (scale.x == 1) && (scale.y == 1) && (blend == null))
		{ 	//Simple render
			framePixels.setPosition(_point.x, _point.y);
			framePixels.draw(FlxG.batch);			
		}
		else
		{ 	//Advanced render
			framePixels.setOrigin(origin.x, origin.y);
			framePixels.setScale(scale.x, scale.y);
			if((angle != 0) && (_bakedRotation <= 0))
				framePixels.setRotation(angle);
			framePixels.setPosition(_point.x, _point.y);
			if(blend != null)
			{
				FlxG.batch.setBlendFunction(blend[0], blend[1]);
				framePixels.draw(FlxG.batch);
				FlxG.batch.setBlendFunction(0x0302, 0x0303);
			}
			else
			{
				framePixels.draw(FlxG.batch);
			}
		}
		
		_VISIBLECOUNT++;
		if(FlxG.visualDebug && !ignoreDrawDebug)
				drawDebug(camera);
	}
	
	/**
	 * This function draws or stamps one <code>FlxSprite</code> onto another.
	 * This function is NOT intended to replace <code>draw()</code>!
	 * 
	 * @param	Brush		The image you want to use as a brush or stamp or pen or whatever.
	 * @param	X			The X coordinate of the brush's top left corner on this sprite.
	 * @param	Y			The Y coordinate of the brush's top left corner on this sprite.
	 */
	public void stamp(FlxSprite Brush, int X, int Y)
	{			
		Brush.drawFrame();
		
		TextureData brushTextureData = Brush.framePixels.getTexture().getTextureData();
		
		if(!brushTextureData.isPrepared())
			brushTextureData.prepare();
		
		Pixmap brushPixmap = brushTextureData.consumePixmap();

		stamp(brushPixmap, Brush.framePixels.getRegionX(), Brush.framePixels.getRegionY() - Brush.frameHeight, Brush.frameWidth, Brush.frameHeight, X + _pixels.getRegionX(), Y + _pixels.getRegionY());
		
		if (brushTextureData.disposePixmap())
			brushPixmap.dispose();
	}
	
	/**
	 * This function draws or stamps one <code>FlxSprite</code> onto another.
	 * This function is NOT intended to replace <code>draw()</code>!
	 * 
	 * @param	Brush		The image you want to use as a brush or stamp or pen or whatever.
	 * @param	X			The X coordinate of the brush's top left corner on this sprite.
	 */
	public void stamp(FlxSprite Brush, int X)
	{		
		stamp(Brush, X, 0);
	}
	
	/**
	 * This function draws or stamps one <code>FlxSprite</code> onto another.
	 * This function is NOT intended to replace <code>draw()</code>!
	 * 
	 * @param	Brush		The image you want to use as a brush or stamp or pen or whatever.
	 */
	public void stamp(FlxSprite Brush)
	{		
		stamp(Brush, 0, 0);
	}
	
	/**
	 * This function draws or stamps one <code>FlxSprite</code> onto another.
	 * This function is NOT intended to replace <code>draw()</code>!
	 * 
	 * @param	Brush			The image you want to use as a brush or stamp or pen or whatever.
	 * @param	SourceX			The X coordinate of the brush's top left corner.
	 * @param	SourceY			They Y coordinate of the brush's top left corner.
	 * @param	SourceWidth		The brush's width.
	 * @param	SourceHeight	The brush's height.
	 * @param	DestinationX	The X coordinate of the brush's top left corner on this sprite.
	 * @param	DestinationY	The Y coordinate of the brush's top right corner on this sprite.
	 */
	public void stamp(Pixmap Brush, int SourceX, int SourceY, int SourceWidth, int SourceHeight, int DestinationX, int DestinationY)
	{	
		Pixmap.setBlending(Pixmap.Blending.SourceOver);
		Pixmap.setFilter(Pixmap.Filter.NearestNeighbour);
		
		TextureData textureData = null;
		if (_newTextureData != null)
			textureData = _newTextureData;
		else
			textureData = _pixels.getTexture().getTextureData();
		
		if(!textureData.isPrepared())
			textureData.prepare();
		
		Pixmap pixmap = textureData.consumePixmap();

		pixmap.drawPixmap(Brush, SourceX, SourceY, SourceWidth, SourceHeight, DestinationX, DestinationY, SourceWidth, SourceHeight);
		
		_newTextureData = new ManagedTextureData(pixmap);
	}
	
	/**
	 * This function draws a line on this sprite from position X1,Y1
	 * to position X2,Y2 with the specified color.
	 * 
	 * @param	StartX		X coordinate of the line's start point.
	 * @param	StartY		Y coordinate of the line's start point.
	 * @param	EndX		X coordinate of the line's end point.
	 * @param	EndY		Y coordinate of the line's end point.
	 * @param	Color		The line's color.
	 * @param	Thickness	How thick the line is in pixels (default value is 1).
	 */
	public void drawLine(float StartX, float StartY, float EndX, float EndY, int Color, int Thickness)
	{		
		Pixmap.setBlending(Pixmap.Blending.SourceOver);
		Pixmap.setFilter(Pixmap.Filter.NearestNeighbour);
		
		TextureData textureData = null;
		if (_newTextureData != null)
			textureData = _newTextureData;
		else
			textureData = _pixels.getTexture().getTextureData();
		
		if(!textureData.isPrepared())
			textureData.prepare();
		
		int rx = _pixels.getRegionX();
		int ry = _pixels.getRegionY();
		
		Pixmap pixmap = textureData.consumePixmap();			
		pixmap.setColor(FlxU.argbToRgba(Color));
		pixmap.drawLine((int) (rx + StartX), (int) (ry + StartY), (int) (rx + EndX), (int) (ry + EndY));
		
		_newTextureData = new ManagedTextureData(pixmap);
	}
	
	/**
	 * This function draws a line on this sprite from position X1,Y1
	 * to position X2,Y2 with the specified color.
	 * 
	 * @param	StartX		X coordinate of the line's start point.
	 * @param	StartY		Y coordinate of the line's start point.
	 * @param	EndX		X coordinate of the line's end point.
	 * @param	EndY		Y coordinate of the line's end point.
	 * @param	Color		The line's color.
	 */
	public void drawLine(float StartX, float StartY, float EndX, float EndY, int Color)
	{
		drawLine(StartX, StartY, EndX, EndY, Color, 1);
	}
	
	/**
	 * Fills this sprite's graphic with a specific color.
	 *
	 * @param	Color		The color with which to fill the graphic, format 0xAARRGGBB.
	 */
	public void fill(int Color)
	{		
		Pixmap.setBlending(Pixmap.Blending.SourceOver);
		Pixmap.setFilter(Pixmap.Filter.NearestNeighbour);
		
		TextureData textureData = null;
		if (_newTextureData != null)
			textureData = _newTextureData;
		else
			textureData = _pixels.getTexture().getTextureData();
		
		if(!textureData.isPrepared())
			textureData.prepare();
		
		Pixmap pixmap = textureData.consumePixmap();
		pixmap.setColor(FlxU.argbToRgba(Color));
		pixmap.fillRectangle(_pixels.getRegionX(), _pixels.getRegionY(), _pixels.getRegionWidth(), _pixels.getRegionHeight());

		_newTextureData = new ManagedTextureData(pixmap);
	}
	
	/**
	 * Internal function for updating the sprite's animation.
	 * Useful for cases when you need to update this but are buried down in too many supers.
	 * This function is called automatically by <code>FlxSprite.postUpdate()</code>.
	 */
	protected void updateAnimation()
	{
		if(_bakedRotation > 0)
		{
			int oldIndex = _curIndex;
			int angleHelper = (int) (angle%360);
			if(angleHelper < 0)
				angleHelper += 360;
			_curIndex = (int) (angleHelper/_bakedRotation + 0.5f);
			if(oldIndex != _curIndex)
				dirty = true;
		}
		else if((_curAnim != null) && (_curAnim.delay > 0) && (_curAnim.looped || !finished))
		{
			_frameTimer += FlxG.elapsed;
			while(_frameTimer > _curAnim.delay)
			{
				_frameTimer = _frameTimer - _curAnim.delay;
				if(_curFrame == _curAnim.frames.size-1)
				{
					if(_curAnim.looped)
						_curFrame = 0;
					finished = true;
				}
				else
					_curFrame++;
				_curIndex = _curAnim.frames.get(_curFrame);
				dirty = true;
			}
		}
		
		if(dirty)
			calcFrame();
	}
	
	/**
	 * Request (or force) that the sprite update the frame before rendering.
	 * Useful if you are doing procedural generation or other weirdness!
	 * 
	 * @param	Force	Force the frame to redraw, even if its not flagged as necessary.
	 */
	public void drawFrame(boolean Force)
	{
		if(Force || dirty)
			calcFrame();
	}
	
	/**
	 * Request (or force) that the sprite update the frame before rendering.
	 * Useful if you are doing procedural generation or other weirdness!
	 */
	public void drawFrame()
	{
		drawFrame(false);
	}
	
	/**
	 * Adds a new animation to the sprite.
	 * 
	 * @param	Name		What this animation should be called (e.g. "run").
	 * @param	Frames		An array of numbers indicating what frames to play in what order (e.g. 1, 2, 3).
	 * @param	FrameRate	The speed in frames per second that the animation should play at (e.g. 40 fps).
	 * @param	Looped		Whether or not the animation is looped or just plays once.
	 */
	public void addAnimation(String Name, int[] Frames, int FrameRate, boolean Looped)
	{
		_animations.add(new FlxAnim(Name,Frames,FrameRate,Looped));
	}
	
	/**
	 * Adds a new animation to the sprite.
	 * 
	 * @param	Name		What this animation should be called (e.g. "run").
	 * @param	Frames		An array of numbers indicating what frames to play in what order (e.g. 1, 2, 3).
	 * @param	FrameRate	The speed in frames per second that the animation should play at (e.g. 40 fps).
	 */
	public void addAnimation(String Name, int[] Frames, int FrameRate)
	{
		addAnimation(Name, Frames, FrameRate, true);
	}
	
	/**
	 * Adds a new animation to the sprite.
	 * 
	 * @param	Name		What this animation should be called (e.g. "run").
	 * @param	Frames		An array of numbers indicating what frames to play in what order (e.g. 1, 2, 3).
	 */
	public void addAnimation(String Name, int[] Frames)
	{
		addAnimation(Name, Frames, 0, true);
	}
	
	/**
	 * Pass in a function to be called whenever this sprite's animation changes.
	 * 
	 * @param	AnimationCallback		A function that has 3 parameters: a string name, a uint frame number, and a uint frame index.
	 */
	public void addAnimationCallback(AFlxAnim AnimationCallback)
	{
		_callback = AnimationCallback;
	}
	
	/**
	 * Plays an existing animation (e.g. "run").
	 * If you call an animation that is already playing it will be ignored.
	 * 
	 * @param	AnimName	The string name of the animation you want to play.
	 * @param	Force		Whether to force the animation to restart.
	 */
	public void play(String AnimName, boolean Force)
	{
		if(!Force && (_curAnim != null) && (AnimName.equals(_curAnim.name)) && (_curAnim.looped || !finished)) return;
		_curFrame = 0;
		_curIndex = 0;
		_frameTimer = 0;
		int i = 0;
		int l = _animations.size;
		while(i < l)
		{
			if(_animations.get(i).name.equals(AnimName))
			{
				_curAnim = _animations.get(i);
				if(_curAnim.delay <= 0)
					finished = true;
				else
					finished = false;
				_curIndex = _curAnim.frames.get(_curFrame);
				dirty = true;
				return;
			}
			i++;
		}
		FlxG.log("FlxSprite", "WARNING: No animation called \""+AnimName+"\"");
	}
	
	/**
	 * Plays an existing animation (e.g. "run").
	 * If you call an animation that is already playing it will be ignored.
	 * 
	 * @param	AnimName	The string name of the animation you want to play.
	 */
	public void play(String AnimName)
	{
		play(AnimName, false);
	}
	
	/**
	 * Tell the sprite to change to a random frame of animation
	 * Useful for instantiating particles or other weird things.
	 */
	public void randomFrame()
	{
		_curAnim = null;
		_curIndex = (int) (FlxG.random()*(_pixels.getRegionWidth()/frameWidth));
		dirty = true;
	}
	
	/**
	 * Helper function that just sets origin to (0,0)
	 */
	public void setOriginToCorner()
	{
		origin.x = origin.y = 0;
	}
	
	/**
	 * Helper function that adjusts the offset automatically to center the bounding box within the graphic.
	 * 
	 * @param	AdjustPosition		Adjusts the actual X and Y position just once to match the offset change. Default is false.
	 */
	public void centerOffsets(boolean AdjustPosition)
	{
		offset.x = (frameWidth-width)*0.5f;
		offset.y = (frameHeight-height)*0.5f;
		if(AdjustPosition)
		{
			x += offset.x;
			y += offset.y;
		}
	}
	
	/**
	 * Helper function that adjusts the offset automatically to center the bounding box within the graphic.
	 */ 
	public void centerOffsets()
	{
		centerOffsets(false);
	}
	
	public Array<FlxPoint> replaceColor(int Color, int NewColor, boolean FetchPositions)
	{		
		Array<FlxPoint> positions = null;
		if(FetchPositions)
			positions = new Array<FlxPoint>();
		
		Color = FlxU.argbToRgba(Color);
		NewColor = FlxU.argbToRgba(NewColor);
		
		int row = _pixels.getRegionY();
		int column;
		int rows = _pixels.getRegionHeight() + row;
		int columns = _pixels.getRegionWidth() + _pixels.getRegionX();
		
		Pixmap.setBlending(Pixmap.Blending.None);
		Pixmap.setFilter(Pixmap.Filter.NearestNeighbour);
		
		TextureData textureData = null;
		if (_newTextureData != null)
			textureData = _newTextureData;
		else
			textureData = _pixels.getTexture().getTextureData();
		
		if(!textureData.isPrepared())
			textureData.prepare();
		
		Pixmap pixmap = textureData.consumePixmap();
	
		while(row < rows)
		{
			column = _pixels.getRegionX();
			while(column < columns)
			{
				if(pixmap.getPixel(column,row) == Color)
				{
					pixmap.drawPixel(column,row,NewColor);
					if(FetchPositions)
						positions.add(new FlxPoint(column - _pixels.getRegionX(),row - _pixels.getRegionY()));
				}
				column++;
			}
			row++;
		}		
		
		_newTextureData = new ManagedTextureData(pixmap);
		return positions;
	}
	
	public Array<FlxPoint> replaceColor(int Color, int NewColor)
	{
		return replaceColor(Color, NewColor, false);
	}
	
	/**
	 * Set <code>pixels</code> to any <code>TextureRegion</code> object.
	 * Automatically adjust graphic size and render helpers.
	 */
	public TextureRegion getPixels()
	{
		if(_newTextureData != null)
		{
			_pixels.getTexture().load(_newTextureData);
		}
		return _pixels;
	}

	/**
	 * @private
	 */
	public void setPixels(TextureRegion Pixels)
	{
		_pixels = Pixels;
		width = frameWidth = _pixels.getRegionWidth();
		height = frameHeight = _pixels.getRegionHeight();
		resetHelpers();
	}
	
	/**
	 * Set <code>facing</code> using <code>FlxSprite.LEFT</code>,<code>RIGHT</code>,
	 * <code>UP</code>, and <code>DOWN</code> to take advantage of
	 * flipped sprites and/or just track player orientation more easily.
	 */
	public int getFacing()
	{
		return _facing;
	}

	/**
	 * @private
	 */
	public void setFacing(int Direction)
	{
		if(_facing != Direction)
			dirty = true;
		_facing = Direction;
	}
	
	/**
	 * Set <code>alpha</code> to a number between 0 and 1 to change the opacity of the sprite.
	 */
	public float getAlpha()
	{
		return _alpha;
	}
	
	/**
	 * @private
	 */
	public void setAlpha(float Alpha)
	{
		if(Alpha > 1)
			Alpha = 1;
		if(Alpha < 0)
			Alpha = 0;
		if(Alpha == _alpha)
			return;
		_alpha = Alpha;
		setColor(_color);
	}
	
	/**
	 * Set <code>color</code> to a number in this format: 0xRRGGBB.
	 * <code>color</code> IGNORES ALPHA.  To change the opacity use <code>alpha</code>.
	 * Tints the whole sprite to be this color (similar to OpenGL vertex colors).
	 */
	public int getColor()
	{
		return _color;
	}
	
	/**
	 * @private
	 */
	public void setColor(int Color)
	{
		Color &= 0x00FFFFFF;
		_color = Color;		
	}
	
	/**
	 * Tell the sprite to change to a specific frame of animation.
	 * 
	 * @param	Frame	The frame you want to display.
	 */
	public int getFrame()
	{
		return _curIndex;
	}
	
	/**
	 * @private
	 */
	public void setFrame(int Frame)
	{
		_curAnim = null;
		_curIndex = Frame;
		dirty = true;
	}
	
	/**
	 * Check and see if this object is currently on screen.
	 * Differs from <code>FlxObject</code>'s implementation
	 * in that it takes the actual graphic into account,
	 * not just the hitbox or bounding box or whatever.
	 * 
	 * @param	Camera		Specify which game camera you want.  If null getScreenXY() will just grab the first global camera.
	 * 
	 * @return	Whether the object is on screen or not.
	 */
	@Override
	public boolean onScreen(FlxCamera Camera)
	{
		if(Camera == null)
			Camera = FlxG.camera;
		
		getScreenXY(_point,Camera);
		_point.x = _point.x - offset.x;
		_point.y = _point.y - offset.y;

		if(((angle == 0) || (_bakedRotation > 0)) && (scale.x == 1) && (scale.y == 1))
			return ((_point.x + frameWidth > 0) && (_point.x < Camera.width) && (_point.y + frameHeight > 0) && (_point.y < Camera.height));
		
		float halfWidth = (float)frameWidth/2;
		float halfHeight = (float)frameHeight/2;
		float absScaleX = (scale.x>0)?scale.x:-scale.x;
		float absScaleY = (scale.y>0)?scale.y:-scale.y;
		float radius = (float) (Math.sqrt(halfWidth*halfWidth+halfHeight*halfHeight)*((absScaleX >= absScaleY)?absScaleX:absScaleY));
		_point.x += halfWidth;
		_point.y += halfHeight;
		return ((_point.x + radius > 0) && (_point.x - radius < Camera.width) && (_point.y + radius > 0) && (_point.y - radius < Camera.height));
	}
	
	/**
	 * Check and see if this object is currently on screen.
	 * Differs from <code>FlxObject</code>'s implementation
	 * in that it takes the actual graphic into account,
	 * not just the hitbox or bounding box or whatever.
	 * 
	 * @return	Whether the object is on screen or not.
	 */
	@Override
	public boolean onScreen()
	{
		return onScreen(null);
	}
	
	/**
	 * Checks to see if a point in 2D world space overlaps this <code>FlxSprite</code> object's current displayed pixels.
	 * This check is ALWAYS made in screen space, and always takes scroll factors into account.
	 * 
	 * @param	Point		The point in world space you want to check.
	 * @param	Mask		Used in the pixel hit test to determine what counts as solid.
	 * @param	Camera		Specify which game camera you want.  If null getScreenXY() will just grab the first global camera.
	 * 
	 * @return	Whether or not the point overlaps this object.
	 */
	//TODO: pixel hittest. Loading the pixmap will be too slow to be usable, is there another way?
	public boolean pixelsOverlapPoint(FlxPoint Point,int Mask,FlxCamera Camera)
	{
		if(Camera == null)
			Camera = FlxG.camera;
		getScreenXY(_point,Camera);
		_point.x = _point.x - offset.x;
		_point.y = _point.y - offset.y;
		//_flashPoint.x = (int) ((Point.x - Camera.scroll.x) - _point.x);
		//_flashPoint.y = (int) ((Point.y - Camera.scroll.y) - _point.y);
		return false;//framePixels.hitTest(_flashPointZero,Mask,_flashPoint);
	}
	
	/**
	 * Checks to see if a point in 2D world space overlaps this <code>FlxSprite</code> object's current displayed pixels.
	 * This check is ALWAYS made in screen space, and always takes scroll factors into account.
	 * 
	 * @param	Point		The point in world space you want to check.
	 * @param	Mask		Used in the pixel hit test to determine what counts as solid.
	 * 
	 * @return	Whether or not the point overlaps this object.
	 */
	public boolean pixelsOverlapPoint(FlxPoint Point,int Mask)
	{
		return pixelsOverlapPoint(Point, Mask, null);
	}
	
	/**
	 * Checks to see if a point in 2D world space overlaps this <code>FlxSprite</code> object's current displayed pixels.
	 * This check is ALWAYS made in screen space, and always takes scroll factors into account.
	 * 
	 * @param	Point		The point in world space you want to check.
	 * 
	 * @return	Whether or not the point overlaps this object.
	 */
	public boolean pixelsOverlapPoint(FlxPoint Point)
	{
		return pixelsOverlapPoint(Point, 0xFF, null);
	}
	
	/**
	 * Internal function to update the current animation frame.
	 */
	protected void calcFrame()
	{
		int indexX = _curIndex*frameWidth;
		int indexY = 0;
		
		//Handle sprite sheets		
		int widthHelper = _pixels.getRegionWidth();
		int heightHelper = _pixels.getRegionHeight();
		if(indexX >= widthHelper)
		{
			indexY = (int)(indexX/widthHelper)*frameHeight;
			indexX %= widthHelper;
		}
				
		if(indexY >= heightHelper)
		{
			indexY = heightHelper - frameHeight;
			indexX = widthHelper - frameWidth;
		}
		
		//Update display bitmap		
		framePixels.setRegion(indexX+_pixels.getRegionX(), indexY+_pixels.getRegionY(), frameWidth, frameHeight);

		//handle reversed sprites.
		if(_flipped > 0 && _facing == LEFT)
			framePixels.flip(true, true);
		else
			framePixels.flip(false, true);

		if(_callback != null)
			_callback.callback(((_curAnim != null)?(_curAnim.name):null),_curFrame,_curIndex);
		dirty = false;
	}
}