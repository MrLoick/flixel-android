package org.flixel;

import com.badlogic.gdx.utils.Array;

/**
 * This is a useful "generic" Flixel object.
 * Both <code>FlxObject</code> and <code>FlxGroup</code> extend this class,
 * as do the plugins.  Has no size, position or graphical data.
 * 
 * @author Ka Wing Chin
 */
public class FlxBasic
{

	static int _ACTIVECOUNT;
	static int _VISIBLECOUNT;
		
	/**
	 * IDs seem like they could be pretty useful, huh?
	 * They're not actually used for anything yet though.
	 */
	public int ID;
	/**
	 * Controls whether <code>update()</code> and <code>draw()</code> are automatically called by FlxState/FlxGroup.
	 */
	public boolean exists;
	/**
	 * Controls whether <code>update()</code> is automatically called by FlxState/FlxGroup.
	 */
	public boolean active;
	/**
	 * Controls whether <code>draw()</code> is automatically called by FlxState/FlxGroup.
	 */
	public boolean visible;
	/**
	 * Useful state for many game objects - "dead" (!alive) vs alive.
	 * <code>kill()</code> and <code>revive()</code> both flip this switch (along with exists, but you can override that).
	 */
	public boolean alive;
	/**
	 * An array of camera objects that this object will use during <code>draw()</code>.
	 * This value will initialize itself during the first draw to automatically
	 * point at the main camera list out in <code>FlxG</code> unless you already set it.
	 * You can also change it afterward too, very flexible!
	 */
	public Array<FlxCamera> cameras;
	/**
	 * Setting this to true will prevent the object from appearing
	 * when the visual debug mode in the debugger overlay is toggled on.
	 */
	public boolean ignoreDrawDebug;
	
	/**
	 * Instantiate the basic flixel object.
	 */
	public FlxBasic()
	{
		ID = -1;
		exists = true;
		active = true;
		visible = true;
		alive = true;
		ignoreDrawDebug = false;
	}
	
	/**
	 * Override this function to null out variables or manually call
	 * <code>destroy()</code> on class members if necessary.
	 * Don't forget to call <code>super.destroy()</code>!
	 */
	public void destroy(){}
	
	/**
	 * Pre-update is called right before <code>update()</code> on each object in the game loop.
	 */
	public void preUpdate()
	{
		_ACTIVECOUNT++;
	}
	
	/**
	 * Override this function to update your class's position and appearance.
	 * This is where most of your game rules and behavioral code will go.
	 */
	public void update()
	{
	}
	
	/**
	 * Post-update is called right after <code>update()</code> on each object in the game loop.
	 */
	public void postUpdate()
	{
	}
	
	/**
	 * Override this function to control how the object is drawn.
	 * Overriding <code>draw()</code> is rarely necessary, but can be very useful.
	 * @param camera 
	 */
	public void draw(FlxCamera Camera)
	{
		_VISIBLECOUNT++;
		if(FlxG.visualDebug && !ignoreDrawDebug)
			drawDebug(Camera);
	}
	
	/**
	 * Override this function to draw custom "debug mode" graphics to the
	 * specified camera while the debugger's visual mode is toggled on.
	 * 
	 * @param	Camera	Which camera to draw the debug visuals to.
	 */
	public void drawDebug(FlxCamera Camera)
	{
	}

	
	/**
	 * Override this function to draw custom "debug mode" graphics to the
	 * specified camera while the debugger's visual mode is toggled on.
	 */
	public void drawDebug()
	{
	}
	
	/**
	 * Handy function for "killing" game objects.
	 * Default behavior is to flag them as nonexistent AND dead.
	 * However, if you want the "corpse" to remain in the game,
	 * like to animate an effect or whatever, you should override this,
	 * setting only alive to false, and leaving exists true.
	 */
	public void kill()
	{
		alive = exists = false;
	}
	
	/**
	 * Handy function for bringing game objects "back to life". Just sets alive and exists back to true.
	 * In practice, this function is most often called by <code>FlxObject.reset()</code>.
	 */
	public void revive()
	{
		alive = exists = true;
	}
	
	/**
	 * Convert object to readable string name.  Useful for debugging, save games, etc.
	 */
	public String toString()
	{
		return FlxU.getClassName(this.getClass());
	}
}