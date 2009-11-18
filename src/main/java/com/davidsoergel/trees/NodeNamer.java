/*
 * Copyright (c) 2001-2008 David Soergel
 * 418 Richmond St., El Cerrito, CA  94530
 * dev@davidsoergel.com
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the author nor the names of any contributors may
 *       be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
