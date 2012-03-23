package org.flixel;

import org.flixel.data.SystemAsset;
import org.flixel.event.AFlxReplay;
import org.flixel.system.FlxDebugger;
import org.flixel.system.FlxPause;
import org.flixel.system.FlxReplay;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * FlxGame is the heart of all flixel games, and contains a bunch of basic game loops and things.
 * It is a long and sloppy file that you shouldn't have to worry about too much!
 * It is basically only used to create your game object in the first place,
 * after that FlxG and FlxState have all the useful stuff you actually need.
 * 
 * @author	Ka Wing Chin
 */
public class FlxGame implements ApplicationListener, InputProcessor
{
	/**
	 * Tells flixel to use the default system mouse cursor instead of custom Flixel mouse cursors.
	 * @default false
	 */
	public boolean useSystemCursor;
	/**
	 * Initialize and allow the flixel debugger overlay even in release mode.
	 * Also useful if you don't use FlxPreloader!
	 * @default false
	 */
	public boolean forceDebugger;
	
	/**
	 * Current game state.
	 */
	FlxState _state;
	/**
	 * Mouse cursor.
	 */
	TextureRegion _mouse;
	
	/**
	 * Class type of the initial/first game state for the game, usually MenuState or something like that.
	 */
	Class<? extends FlxState> _iState;
	
	/**
	 * Total number of milliseconds elapsed since game start.
	 */
	protected long _total;
	/**
	 * Total number of milliseconds elapsed since last update loop.
	 * Counts down as we step through the game loop.
	 */
	protected int _accumulator;
	/**
	 * Milliseconds of time per step of the game loop.  FlashEvent.g. 60 fps = 16ms.
	 */	
	int _step;
	/**
	 * Framerate of the Flash player (NOT the game loop). Default = 30.
	 */
	int _flashFramerate;
	/**
	 * Max allowable accumulation (see _accumulator).
	 * Should always (and automatically) be set to roughly 2x the flash player framerate.
	 */
	int _maxAccumulation;
	/**
	 * If a state change was requested, the new state object is stored here until we switch to it.
	 */
	FlxState _requestedState;
	/**
	 * A flag for keeping track of whether a game reset was requested or not.
	 */
	boolean _requestedReset;
	/**
	 * The debugger overlay object.
	 */
	FlxDebugger _debugger;
	/**
	 * A handy boolean that keeps track of whether the debugger exists and is currently visible.
	 */
	boolean _debuggerUp;
	
	/**
	 * Container for a game replay object.
	 */
	FlxReplay _replay;
	/**
	 * Flag for whether a playback of a recording was requested.
	 */
	boolean _replayRequested;
	/**
	 * Flag for whether a new recording was requested.
	 */
	boolean _recordingRequested;
	/**
	 * Flag for whether a replay is currently playing.
	 */
	boolean _replaying;
	/**
	 * Flag for whether a new recording is being made.
	 */
	boolean _recording;
	/**
	 * Array that keeps track of keypresses that can cancel a replay.
	 * Handy for skipping cutscenes or getting out of attract modes!
	 */
	String[] _replayCancelKeys;
	/**
	 * Helps time out a replay if necessary.
	 */
	int _replayTimer;
	/**
	 * This function, if set, is triggered when the callback stops playing.
	 */
	AFlxReplay _replayCallback;

	
	private BitmapFont font;
		
	private GL10 gl;
	private FlxPause _pauseState;
		
	/**
	 * Instantiate a new game object.
	 * 
	 * @param	GameSizeX		The width of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	GameSizeY		The height of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 * @param	Zoom			The default level of zoom for the game's cameras (e.g. 2 = all pixels are now drawn at 2x).  Default = 1.
	 * @param	GameFramerate	How frequently the game should update (default is 30 times per second).
	 * @param	FlashFramerate	Sets the actual display framerate for Flash player (default is 30 times per second).
	 * @param	UseSystemCursor	Whether to use the default OS mouse pointer, or to use custom flixel ones.
	 */
	public FlxGame(int GameSizeX, int GameSizeY, Class<? extends FlxState> InitialState, float Zoom, int GameFramerate, int FlashFramerate, boolean UseSystemCursor)
	{
		// basic display and update setup stuff
		FlxG.init(this, GameSizeX, GameSizeY, Zoom);
		FlxG.setFramerate(GameFramerate);
		FlxG.setFlashFramerate(FlashFramerate);
		_accumulator = (int) _step;
		_total = 0;
		_state = null;
		//useSoundHotKeys = true;
		useSystemCursor = UseSystemCursor;
		//if(!useSystemCursor)
			//Gdx.input.setCursorCatched(true);
		forceDebugger = false;
		_debuggerUp = false;
		
		//replay data
		_replay = new FlxReplay();
		_replayRequested = false;
		_recordingRequested = false;
		_replaying = false;
		_recording = false;
		
		// then get ready to create the game object for real;
		_iState = InitialState;
		_requestedState = null;
		_requestedReset = true;
	}
	
	/**
	 * Instantiate a new game object.
	 * 
	 * @param	GameSizeX		The width of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	GameSizeY		The height of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 * @param	Zoom			The default level of zoom for the game's cameras (e.g. 2 = all pixels are now drawn at 2x).  Default = 1.
	 * @param	GameFramerate	How frequently the game should update (default is 30 times per second).
	 * @param	FlashFramerate	Sets the actual display framerate for Flash player (default is 30 times per second).
	 */
	public FlxGame(int GameSizeX, int GameSizeY, Class<? extends FlxState> InitialState, float Zoom, int GameFramerate, int FlashFramerate)
	{
		this(GameSizeX, GameSizeY, InitialState, Zoom, GameFramerate, FlashFramerate, false);
	}
	
	/**
	 * Instantiate a new game object.
	 * 
	 * @param	GameSizeX		The width of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	GameSizeY		The height of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 * @param	Zoom			The default level of zoom for the game's cameras (e.g. 2 = all pixels are now drawn at 2x).  Default = 1.
	 * @param	GameFramerate	How frequently the game should update (default is 30 times per second).
	 */
	public FlxGame(int GameSizeX, int GameSizeY, Class<? extends FlxState> InitialState, float Zoom, int GameFramerate)
	{
		this(GameSizeX, GameSizeY, InitialState, Zoom, GameFramerate, 30, false);
	}
	
	/**
	 * Instantiate a new game object.
	 * 
	 * @param	GameSizeX		The width of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	GameSizeY		The height of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 * @param	Zoom			The default level of zoom for the game's cameras (e.g. 2 = all pixels are now drawn at 2x).  Default = 1.
	 */
	public FlxGame(int GameSizeX, int GameSizeY, Class<? extends FlxState> InitialState, float Zoom)
	{
		this(GameSizeX, GameSizeY, InitialState, Zoom, 30, 30, false);
	}
	
	/**
	 * Instantiate a new game object.
	 * 
	 * @param	GameSizeX		The width of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	GameSizeY		The height of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 */
	public FlxGame(int GameSizeX, int GameSizeY, Class<? extends FlxState> InitialState)
	{
		this(GameSizeX, GameSizeY, InitialState, 1, 30, 30, false);
	}
	
	/**
	 * Internal event handler for input and focus.
	 * 
	 * @param	KeyCode	libgdx key code.
	 */
	@Override
	public boolean keyUp(int KeyCode)
	{
		if(KeyCode == Keys.F2)
			FlxG.visualDebug = !FlxG.visualDebug;
		/*
		if(_debuggerUp && _debugger.watch.editing)
			return;
		
		if((_debugger != null) && ((FlashEvent.keyCode == 192) || (FlashEvent.keyCode == 220)))
		{
			_debugger.visible = !_debugger.visible;
			_debuggerUp = _debugger.visible;
			if(_debugger.visible)
				flash.ui.Mouse.show();
			else if(!useSystemCursor)
				flash.ui.Mouse.hide();
			//_console.toggle();
			return;
		}
		*/
		if(_replaying)
			return true;
		
		FlxG.keys.handleKeyUp(KeyCode);
		
		return true;
	}
	
	/**
	 * Internal event handler for input and focus.
	 * 
	 * @param	KeyCode	libgdx key code.
	 */
	@Override
	public boolean keyDown(int KeyCode)
	{
		if(KeyCode == Keys.MENU || KeyCode == Keys.F1)
		{
			if(!FlxG.paused)
				onFocusLost();
			else
				onFocus();			
		}
		
		//if(_debuggerUp && _debugger.watch.editing)
			//return;
		if(_replaying && (_replayCancelKeys != null) && (_debugger == null) /*&& (KeyCode != 192)*/ && (KeyCode != Keys.BACKSLASH))
		{
			String replayCancelKey;
			int i = 0;
			int l = _replayCancelKeys.length;
			while(i < l)
			{
				replayCancelKey = _replayCancelKeys[i++];
				if((replayCancelKey == "ANY") || (FlxG.keys.getKeyCode(replayCancelKey) == KeyCode))
				{
					if(_replayCallback != null)
					{
						_replayCallback.onFinished();
						_replayCallback = null;
					}
					else
						FlxG.stopReplay();
					break;
				}
			}
			return true;
		}
		
		FlxG.keys.handleKeyDown(KeyCode);
		return true;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	/**
	 * Internal event handler for input and focus.
	 */
	@Override
	public boolean touchDown(int X, int Y, int Pointer, int Button)
	{
		if(_debuggerUp)
		{
			//if(_debugger.hasMouse)
				//return;
			//if(_debugger.watch.editing)
				//_debugger.watch.submit();
		}
		if(_replaying && (_replayCancelKeys != null))
		{
			String replayCancelKey;
			int i = 0;
			int l = _replayCancelKeys.length;
			while(i < l)
			{
				replayCancelKey = _replayCancelKeys[i++];
				if((replayCancelKey == "MOUSE") || (replayCancelKey == "ANY"))
				{
					if(_replayCallback != null)
					{
						_replayCallback.onFinished();
						_replayCallback = null;
					}
					else
						FlxG.stopReplay();
					break;
				}
			}
			return true;
		}
		
		FlxG.mouse.handleMouseDown(X, Y, Pointer, Button);
		return true;
	}

	/**
	 * Internal event handler for input and focus.
	 */
	@Override
	public boolean touchUp(int X, int Y, int Pointer, int Button)
	{
		if(/*(_debuggerUp && _debugger.hasMouse) ||*/ _replaying)
			return true;
		FlxG.mouse.handleMouseUp(X, Y, Pointer, Button);
		return true;
	}

	@Override
	public boolean touchDragged(int X, int Y, int Pointer)
	{
		return false;
	}

	@Override
	public boolean touchMoved(int X, int Y)
	{
		return false;
	}

	/**
	 * Internal event handler for input and focus..
	 */
	@Override
	public boolean scrolled(int Amount)
	{
		if(/*(_debuggerUp && _debugger.hasMouse) ||*/ _replaying)
			return true;
		FlxG.mouse.handleMouseWheel(Amount);
		return true;
	}

	/**
	 * Handles the render call and figures out how many updates and draw calls to do.
	 */
	@Override
	public void render()
	{
		long mark = System.currentTimeMillis();
		long elapsedMS = mark - _total;
		_total = mark;
		
		if((_debugger != null) /*&& _debugger.vcr.paused*/)
		{
			//if(_debugger.vcr.stepRequested)
			//{
				//_debugger.vcr.stepRequested = false;
				//step();
			//}
		}
		else
		{
			_accumulator += elapsedMS;
			if(_accumulator > _maxAccumulation)
				_accumulator = _maxAccumulation;
			while(_accumulator >= _step)
			{
				step();
				_accumulator = _accumulator - _step; 
			}
		}
			
		FlxBasic._VISIBLECOUNT = 0;
		draw();
			
		if(_debuggerUp)
		{
			//_debugger.perf.flash(elapsedMS);
			//_debugger.perf.visibleObjects(FlxBasic._VISIBLECOUNT);
			//_debugger.perf.update();
			//_debugger.watch.update();
		}
	}
	
	/**
	 * If there is a state change requested during the update loop,
	 * this function handles actual destroying the old state and related processes,
	 * and calls creates on the new state and plugs it into the game object.
	 */
	private void switchState()
	{
		//Basic reset stuff
		FlxG.resetCameras();
		FlxG.resetInput();
		FlxG.destroySounds();
		FlxG.clearBitmapCache();
		
		// Clear the debugger overlay's Watch window
		//if(_debugger != null)
			//_debugger.watch.removeAll();
		
		// Clear any timers left in the timer manager
		//TimerManager timerManager = FlxTimer.manager;
		//if(timerManager != null)
			//timerManager.clear();
		
		//Destroy the old state (if there is an old state)
		if(_state != null)
		{
			//_state.remove(_pauseState, true);
			_state.destroy();
		}
		
		//Finally assign and create the new state
		_state = _requestedState;
		_state.create();
		_state.add(_pauseState);
	}
	
	/**
	 * This is the main game update logic section.
	 * The onEnterFrame() handler is in charge of calling this
	 * the appropriate number of times each frame.
	 * This block handles state changes, replays, all that good stuff.
	 */
	protected void step()
	{
		// handle game reset request
		if(_requestedReset)
		{
			_requestedReset = false;
			try
			{
				_requestedState = _iState.newInstance();
			}
			catch (Exception e)
			{
				FlxG.log("FlxGame", e.getMessage());
			}
			_replayTimer = 0;
			_replayCancelKeys = null;
			FlxG.reset();			
		}
		
		//handle replay-related requests
		if(_recordingRequested)
		{
			_recordingRequested = false;
			_replay.create(FlxG.globalSeed);
			_recording = true;
			if(_debugger != null)
			{
				//_debugger.vcr.recording();
				FlxG.log("FLIXEL: starting new flixel gameplay record.");
			}
		}
		else if(_replayRequested)
		{
			_replayRequested = false;
			_replay.rewind();
			FlxG.globalSeed = _replay.seed;
			//if(_debugger != null)
				//_debugger.vcr.playing();
			_replaying = true;
		}
		
		// handle state switching requests
		if(_state != _requestedState)
			switchState();
		
		//finally actually step through the game physics
		FlxBasic._ACTIVECOUNT = 0;
		if(_replaying)
		{
			_replay.playNextFrame();
			if(_replayTimer > 0)
			{
				_replayTimer -= _step;
				if(_replayTimer <= 0)
				{
					if(_replayCallback != null)
					{
						_replayCallback.onFinished();
						_replayCallback = null;
					}
					else
						FlxG.stopReplay();
				}
			}
			if(_replaying && _replay.finished)
			{
				FlxG.stopReplay();
				if(_replayCallback != null)
				{
					_replayCallback.onFinished();
					_replayCallback = null;
				}
			}
			//if(_debugger != null)
				//_debugger.vcr.updateRuntime(_step);
		}
		else
			FlxG.updateInput();
		if(_recording)
		{
			_replay.recordFrame();
			//if(_debugger != null)
				//_debugger.vcr.updateRuntime(_step);
		}
		
		update();
		FlxG.mouse.wheel = 0;
		//if(_debuggerUp)
			//_debugger.perf.activeObjects(FlxBasic._ACTIVECOUNT);
	}
	
	/**
	 * This function is called by step() and updates the actual game state.
	 * May be called multiple times per "frame" or draw call.
	 */
	protected void update()
	{
		//long mark = System.currentTimeMillis();
		
		FlxG.elapsed = FlxG.timeScale*(_step/1000.f);
				
		if(FlxG.paused)
		{
			_pauseState.update();
			return;
		}
		
		FlxG.updateSounds();
		FlxG.updatePlugins();
		_state.update();
		FlxG.updateCameras();
		
		//if(_debuggerUp)
			//_debugger.perf.flixelUpdate(System.currentTimeMillis()-mark);
	}
	
	/**
	 * Goes through the game state and draws all the game objects and special effects.
	 */
	private void draw()
	{
		//long mark = System.currentTimeMillis();
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT); // Clear the screen. It's not needed because of FlxG.lockCameras().	
		float[] rgba = FlxU.getRGBA(FlxG.getBgColor());
		gl.glClearColor(rgba[0], rgba[1], rgba[2], rgba[3]);
		
		FlxG.camera.glCamera.update(false);
		FlxG.batch.setProjectionMatrix(FlxG.camera.glCamera.combined);
		FlxG.batch.begin();
					
		_state.draw();
			
		font.draw(FlxG.batch, "fps:"+Gdx.graphics.getFramesPerSecond(), FlxG.width - 45, 0);		
	
		FlxG.camera.drawFX();
		
		FlxG.batch.end();
		
		//if(_debuggerUp)
			//_debugger.perf.flixelDraw(System.currentTimeMillis()-mark);
	}
	
	/**
	 * Used to instantiate the guts of the flixel game object once we have a valid reference to the root. 
	 */
	@Override
	public void create()
	{
		_total = System.currentTimeMillis();
		
		SystemAsset.createSystemAsset();
		Gdx.input.setInputProcessor(this);
		
		FlxG.resWidth = Gdx.graphics.getWidth();
		FlxG.resHeight = Gdx.graphics.getHeight();
		FlxG.difWidth = ((float)FlxG.resWidth / FlxG.width);
		FlxG.difHeight = ((float)FlxG.resHeight / FlxG.height);
		if(FlxG.flashGfx == null)
			FlxG.flashGfx = new ShapeRenderer();
		_pauseState = new FlxPause();
		
		gl = Gdx.gl10;
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		
		FlxG.batch = new SpriteBatch();
		font = SystemAsset.system;
		
		//Debugger overlay
		//if(FlxG.debug || forceDebugger)
		//{
			//_debugger = new FlxDebugger(FlxG.width*FlxCamera.defaultZoom,FlxG.height*FlxCamera.defaultZoom);
			//addChild(_debugger);
		//}
	}

	@Override
	public void resize(int width, int height)
	{
//		float aspectRatio = (float) width / (float) height;
//		camera = new OrthographicCamera(2f * aspectRatio, 2f);
//		camera = new OrthographicCamera(480, 320);
//		camera.translate(480/2, 320/2, 1);		
//		camera.update();
	}	
	
	protected void onFocusLost()
	{
		FlxG.paused = true;
		_pauseState.visible = true;
		FlxG.pauseSounds();
	}
	
	protected void onFocus()
	{
		FlxG.paused = false;
		_pauseState.visible = false;
		FlxG.resumeSounds();
	}
	
	
	@Override
	public void pause()
	{
		FlxG.log("pause");
		onFocusLost();
	}

	@Override
	public void resume()
	{
		FlxG.log("resume");
	}
	
	@Override
	public void dispose()
	{
		FlxG.log("dispose");
	}
}