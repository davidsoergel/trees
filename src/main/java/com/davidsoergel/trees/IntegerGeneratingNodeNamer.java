/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees;


/**
 * A factory for Integers to be used for naming unnamed internal nodes in a phylogeny.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: IntegerNodeNamer.java 262 2009-03-31 20:04:18Z soergel $
 */

public class IntegerGeneratingNodeNamer extends IntegerNodeNamer
	{
	// ------------------------------ FIELDS ------------------------------

	protected int currentId;
	boolean requireGeneratedNamesForInternalNodes = false;
	// --------------------------- CONSTRUCTORS ---------------------------

	public IntegerGeneratingNodeNamer(int unknownBasis, boolean requireGeneratedNamesForInternalNodes)
		{
		super();
		this.currentId = unknownBasis;
		this.requireGeneratedNamesForInternalNodes = requireGeneratedNamesForInternalNodes;
		//	this.unknownBasis = unknownBasis;
		}

	// ------------------------ INTERFACE METHODS ------------------------


	// --------------------- Interface NodeNamer ---------------------

	/*
	 public Integer merge(Integer name, Object s)
		 {
		 return null;
		 }
 */

	/*
	public Integer nameInternal(int i)
		{
		return unknownBasis + i;
		}
	*/
	public boolean requireGeneratedNamesForInternalNodes()
		{
		return requireGeneratedNamesForInternalNodes;
		}

	/**
	 * {@inheritDoc}
	 */
	public Integer generate()
		{
		return currentId++;
		}

	public Integer uniqueify(Integer value)
		{
		return generate();
		}
	}
