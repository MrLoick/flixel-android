# Graphic #
In this tutorial you’ll learn how to load a graphic and show it on the screen.

## Texture atlas ##
Texture atlas is a large image which contains many smaller sub-images. For example, below you can see an example of texture atlas.

![http://wingeraser.com/wp-content/uploads/2012/08/graphic_01.png](http://wingeraser.com/wp-content/uploads/2012/08/graphic_01.png)
<br><i>Texture atlas from Mode</i>

There are two main reasons to use a texture atlas. First, it can increase rendering speed by allowing you to batch more objects into a single draw call. For example, if all the plants in the game use just one texture atlas, then you can draw them all at the same time. Otherwise you would have to draw one group of plants, switch texture state, draw another group of plants, and so on.<br>
<br>
Second, it allows you to use unusually-shaped textures. Most graphics cards are designed to use textures that are square and have dimensions that are powers of two, such as 256x256, 512x512, and so on. But what if you have a graphic that is 550x130? You would have to put it in the middle of a 1024x1024 texture, and waste all the extra space. Alternately, you could pack a lot of unusually-shaped textures into one square texture atlas, and hardly waste any space at all!<br>
Source: <a href='http://blog.wolfire.com/2010/03/Using-texture-atlases'>http://blog.wolfire.com/2010/03/Using-texture-atlases</a> by David Rosen<br>
<br>
For the mobile platform you are limited to the size of the texture atlas. The common texture size is 1024 x 1024 px. Some high-end devices can handle 2048 x 2048 pixels. It’s going to take pretty much of the memory. It’s better to use the common size if you want your game to run on almost all devices. Checkout out <a href='http://www.glbenchmark.com'>http://www.glbenchmark.com</a> for OpenGL environment and performance details for mobile devices.<br>
<br>
<h2>Create texture atlas</h2>
Manually setting every sprite on a texture atlas takes a long time. Fortunately there are many tools that packs sprites into texture atlas(es). An open source program TexturePacker-Gui is especially developed for libgdx and is used in this article.<br>
When a texture atlas is generated you will also get a text file with the extension named “pack”. In the pack the region of packed images are described and provides information about the original image before it was packed.<br>
<br>
<h2>Use texture atlas in your project</h2>
If you’re developing for multiple platforms you don’t want duplicated assets in different project folders. Store the assets in the Android project and link the other projects to the asset folder. See the code below how to use this in flixel-android.<br>
<pre><code>protected String Img = "[url+flixel]:[texture name]";<br>
</code></pre>
The first part of the String, before the colon, must be the url and the second part is the name of the texture. Below you’ll see an example of the code to load an image.<br>
<br>
<pre><code>public class Player extends FlxSprite<br>
{<br>
	protected String ImgSpaceman = "examples/mode/pack:spaceman";<br>
	public Player()<br>
	{<br>
		super(X,Y);<br>
		loadGraphic(ImgSpaceman,true,true,8);<br>
	}<br>
}<br>
</code></pre>

<h2>What’s next?</h2>
More information about texture atlas<br>
<a href='http://www.codeandweb.com/what-is-a-sprite-sheet'>http://www.codeandweb.com/what-is-a-sprite-sheet</a>