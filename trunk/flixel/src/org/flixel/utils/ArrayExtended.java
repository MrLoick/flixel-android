package org.flixel.utils;

import com.badlogic.gdx.utils.Array;

/**
 * Just an extension of Array. It fixed the maxSize for flixel. And Array.resize(..) is set to protected. 
 * Can't make it public with out modifying libgdx.
 * 
 * @author Ka Wing Chin
 */

/**
 * @param <T>
 */
public class ArrayExtended<T> extends Array<T>
{
	public T[] setMaxSize(int Size)
	{
		return resize(Size + 1);
	}
}
