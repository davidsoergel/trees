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
		if (value == null && allowNull)
			{
			return null;
			}
		throw new TreeRuntimeException("ID modification disallowed");
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
