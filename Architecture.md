# Architecture #

flixel-android is built on top of libgdx framework. Why using libgdx you might wonder. We wanted to use a framework without having to worry about OpenGL ES and C/C++. The libgdx team did a pretty good job on that. Libgdx is written in Java with some C/C++ mixed while flixel-android is written entirely in Java. As end-user of flixel-android you only need to write Java code. With libgdx as backend it allows you to write, test and debug your application on your desktop PC and use the same code on Android.
<br />

## Architecture diagram ##
![http://wingeraser.com/flixelandroid/googlecode/architecture_diagram.png](http://wingeraser.com/flixelandroid/googlecode/architecture_diagram.png)
<br><b>Note: the iOS is under development by libgdx team.</b>
<br />
<br />
<h2>Class diagram</h2>
<img src='http://wingeraser.com/flixelandroid/googlecode/class_diagram.png' />