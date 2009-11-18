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

import java.util.HashMap;
import java.util.Map;


/**
 * A factory for Strings to be used for naming unnamed internal nodes in a phylogeny.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: StringNodeNamer.java 262 2009-03-31 20:04:18Z soergel $
 */

public class StringNodeNamer implements NodeNamer<String>
	{
	// ------------------------------ FIELDS ------------------------------
	protected boolean allowNull = false;
	private String unknownBasis;
	private int currentId = 0;

	// --------------------------- CONSTRUCTORS ---------------------------

	public StringNodeNamer(String unknownBasis, boolean allowNull, boolean requireGeneratedNamesForInternalNodes)
		{
		this.unknownBasis = unknownBasis;
		this.allowNull = allowNull;
		this.requireGeneratedNamesForInternalNodes = requireGeneratedNamesForInternalNodes;
		}

	public StringNodeNamer(String unknownBasis, boolean allowNull, boolean requireGeneratedNamesForInternalNodes,
	                       int startId)
		{
		this(unknownBasis, allowNull, requireGeneratedNamesForInternalNodes);
		currentId = startId;
		}

	boolean requireGeneratedNamesForInternalNodes = false;

	public boolean requireGeneratedNamesForInternalNodes()
		{
		return requireGeneratedNamesForInternalNodes;
		}
// ------------------------ INTERFACE METHODS ------------------------


	// --------------------- Interface NodeNamer ---------------------


	/**
	 * {@inheritDoc}
	 */
	public String create(Integer s)
		{
		return s.toString();
		}

	/**
	 * {@inheritDoc}
	 */
	public String create(String s)
		{
		return s;
		}

	/**
	 * {@inheritDoc}
	 */
	public String merge(String name, String s)
		{
		return name + s;
		}

	/**
	 * {@inheritDoc}
	 */
	public String merge(String name, Integer s)
		{
		return name + s;
		}

	/*
	 public Integer merge(Integer name, Object s)
		 {
		 return null;
		 }
 */

	/*	public String nameInternal(int i)
	   {
	   return unknownBasis + i;
	   }
	   */


	/**
	 * {@inheritDoc}
	 */
	public String generate()
		{
		if (unknownBasis == null)
			{
			return null;
			}
		return unknownBasis + currentId++;
		}

	Map<String, Integer> uniqueIndexes = new HashMap<String, Integer>();

	public String uniqueify(String value)
		{
		if (value == null && allowNull)
			{
			return null;
			}
		Integer inc = uniqueIndexes.get(value);
		if (inc == null)
			{
			inc = 0;
			}
		inc++;
		uniqueIndexes.put(value, inc);
		return value + " " + inc; // + "]";
		}

	public boolean isAcceptable(String value)
		{
		return allowNull || value != null;
		}

	public String makeAggregate(String newValue, String value)
		{
		if (value == null)
			{
			return newValue;
			}
		else
			{
			return newValue + "==" + value;
			}
		}
	}
