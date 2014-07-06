/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
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
