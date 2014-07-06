/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees;


/**
 * A factory for names to be applied to phylogeny nodes.  Generally used to add incrementing IDs to internal nodes of a
 * tree for which only the leaves have known names.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: NodeNamer.java 262 2009-03-31 20:04:18Z soergel $
 * @JavadocOK
 */
public interface NodeNamer<T>
	{
	// -------------------------- OTHER METHODS --------------------------

	/**
	 * Creates a name of the desired type based on an Integer.
	 *
	 * @param s the Integer to transform
	 * @return the new node name
	 */
	T create(Integer s);

	/**
	 * Creates a name of the desired type based on a String.
	 *
	 * @param s the String to transform
	 * @return the new node name
	 * @throws TreeException when the input String cannot be transformed into the desired type
	 */
	T create(String s) throws TreeException;


	/**
	 * Creates a name of the desired type from scratch.  In the case of an autoincrementing namer, updates the internal
	 * state.
	 *
	 * @return the new node name
	 */
	T generate();

	/**
	 * Creates a name of the desired type by appending (in some manner) a String to an existing name.
	 *
	 * @param name the base name
	 * @param s    the String to transform and append
	 * @return the new node name
	 * @throws TreeException when the input String cannot be appended to the desired type
	 */
	T merge(T name, String s) throws TreeException;

	/**
	 * Creates a name of the desired type by appending (in some manner) an Integer to an existing name.
	 *
	 * @param name the base name
	 * @param s    the Integer to transform and append
	 * @return the new node name
	 * @throws TreeException when the input Integer cannot be appended to the desired type
	 */
	T merge(T name, Integer s) throws TreeException;

	T uniqueify(T value);

	boolean isAcceptable(T value);

	T makeAggregate(T newValue, T value);

	boolean requireGeneratedNamesForInternalNodes();
	}
