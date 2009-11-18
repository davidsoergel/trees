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

import java.io.Serializable;
import java.util.Map;

/**
 * A node in a simple hierarchy, where a Map from Strings to Objects is attached at each node.  Implements the Map
 * interface and simply delegates to the contained Map.   The type of Collection that holds the children is up to the
 * implementation, so they may or may not be ordered.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: HierarchicalStringObjectMap.java 427 2009-07-29 01:04:57Z soergel $
 */
public interface HierarchicalStringObjectMap<C extends HierarchyNode<Map<String, Object>, C>>
		extends HierarchyNode<Map<String, Object>, C>, Map<String, Object>, Serializable
	{

/*	private String name;

	public String getName()
		{
		return name;
		}

	public void setName(final String name)
		{
		this.name = name;
		}*/
	// ------------------------ INTERFACE METHODS ------------------------


	// --------------------- Interface HierarchyNode ---------------------

	/**
	 * {@inheritDoc}
	 */
//	public abstract HierarchicalStringObjectMap newChild();


	// --------------------- Interface Map ---------------------


	// transactions required for these

	/*
	 public Object put(String s, Object o)
		 {
		 return getContents().put(s, o);
		 }

	 public Object remove(Object o)
		 {
		 return getContents().remove(o);
		 }

	 public void putAll(Map<? extends String, ? extends Object> map)
		 {
		 getContents().putAll(map);
		 }

	 public void clear()
		 {
		 getContents().clear();
		 }
 */


	// -------------------------- OTHER METHODS --------------------------

//	public abstract void merge();


	/**
	 * {@inheritDoc}
	 */
/*	public boolean isLeaf()
		{
		Collection<? extends HierarchyNode<? extends Map<String, Object>, HierarchicalStringObjectMap>> children =
				getChildren();
		return children == null || children.isEmpty();
		}*/

	/**
	 * {@inheritDoc}
	 */
//	public abstract HierarchicalStringObjectMap getParent();

	/**
	 * {@inheritDoc}
	 */
/*	public List<HierarchicalStringObjectMap> getAncestorPath()
		{
		List<HierarchicalStringObjectMap> result = new LinkedList<HierarchicalStringObjectMap>();
		HierarchicalStringObjectMap trav = this;

		while (trav != null)
			{
			result.add(0, trav);
			trav = trav.getParent();
			}

		return result;
		}

	public HierarchicalStringObjectMap getSelfNode()//HierarchyNode<Map<String, Object>,
		{
		return this;
		}*/
	}
