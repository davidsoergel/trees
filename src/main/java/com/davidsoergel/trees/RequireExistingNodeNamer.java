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
 * @version $Id: RequireExistingNodeNamer.java 262 2009-03-31 20:04:18Z soergel $
 */

public class RequireExistingNodeNamer<T> implements NodeNamer<T>
	{
	// ------------------------------ FIELDS ------------------------------
	private boolean allowNull = false;

	public RequireExistingNodeNamer(boolean allowNull)
		{
		this.allowNull = allowNull;
		}

	public boolean requireGeneratedNamesForInternalNodes()
		{
		return false;
		}
	// --------------------------- CONSTRUCTORS ---------------------------

	public T create(Integer s)
		{
		throw new TreeRuntimeException("ID modification disallowed");
		}

	public T create(String s) throws TreeException
		{
		throw new TreeRuntimeException("ID modification disallowed");
		}

	public T merge(T name, String s) throws TreeException
		{
		throw new TreeRuntimeException("ID modification disallowed");
		}

	public T merge(T name, Integer s) throws TreeException
		{
		throw new TreeRuntimeException("ID modification disallowed");
		}

	public T uniqueify(T value)
		{
		if (value == null)
			{
			if (allowNull)
				{
				// allow null IDs even for "unique"
				return null;
				}
			else
				{
				throw new TreeRuntimeException("null ID disallowed");
				}
			}
		else
			{
			// assume the values are already unique
			return value;
			}

//		if (value == null && allowNull)
//			{
//			return null;
//			}
//		throw new TreeRuntimeException("ID modification disallowed");
		}

	public boolean isAcceptable(T value)
		{
		return allowNull || value != null;
		}

	public T makeAggregate(T newValue, T value)
		{
		throw new TreeRuntimeException("ID modification disallowed");
		}

	/**
	 * {@inheritDoc}
	 */
	public T generate()
		{
		throw new TreeRuntimeException("No ID found; new ID generation disallowed");
		}
	}
