# Collision #
In this tutorial you’ll learn how to use basic collision in your game.  flixel-android uses the quadtree algorithm. It has great performance and it’s simple to use.

## The code ##
There are several ways to let objects collide. The first one is very simple, take a look at the code below.

```
@Override
public void update()
{
	super.update();
	FlxG.collide();
}
```
A simple call of FlxG.collide() will let all objects on screen collide each other.

Collision with single object against multiple object(s):
```
Mario _player;
private FlxGroup enemies;
…
@Override
public void update()
{
	super.update();
	FlxG.collide(_player, _enemies); // collide single object against a group
	FlxG.collide(_enemies); // collide all objects against each other that are in the group.
}
```
You can add objects to a group and then collide to the group. For maximum performance it is recommended to bundle a lot of object together using FlxGroup.

The collision happens at the bounding box of the object.
![http://wingeraser.com/wp-content/uploads/2012/08/collision_01.png](http://wingeraser.com/wp-content/uploads/2012/08/collision_01.png)
<br>You can tweak the bounding box by tweaking the dimension and offset of the object.<br>
<br>
<h2>Callback</h2>
The last parameter in FlxG::collide is the callback. See the code below how to use it.<br>
<pre><code>…<br>
FlxG.collide(_player, _enemies, doDamage);<br>
…<br>
AFlxG doDamage = new AFlxG()<br>
{<br>
	@Override<br>
	public void onNotifyCallback(FlxObject Object1, FlxObject Object2)<br>
	{<br>
		_player.hurt(1); // Object1<br>
	}<br>
};<br>
</code></pre>

<h2>Handy variables for collision</h2>
<ul><li>immovable, whether an object will move/alter position after collision.<br>
</li><li>mass, the bounciness of the object. Only affects collisions.<br>
</li><li>allowCollisions, indicates collision direction. Useful for one-way platforms.</li></ul>

<h2>Overlap</h2>
If you only want to know whether an object hit something, but not to collide, you can use the FlxG::overlap method. It works similar to FlxG::collide.<br>
<br>
<br>
<h2>What’s next?</h2>
Take a look at the <a href='http://code.google.com/p/flixel-android/source/browse/trunk/flixel-examples/src/org/flixel/examples/collision/PlayState.java'>CollisonDemo</a> and <a href='http://code.google.com/p/flixel-android/source/browse/trunk/flixel-examples/src/org/flixel/examples/flxcollisions/PlayState.java'>FlxCollission</a>.<br>
More advanced collision with allowCollisions in the next tutorial of Collision series.<br>
<br>
<br>
<br>
