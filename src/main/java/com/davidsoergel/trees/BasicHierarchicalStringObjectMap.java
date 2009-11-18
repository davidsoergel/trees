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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A node in a simple hierarchy, where a Map from Strings to Objects is attached at each node.  Implements the Map
 * interface and simply delegates to the contained Map.  The children are stored in a List and are thus ordered.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: BasicHierarchicalStringObjectMap.java 439 2009-08-06 15:12:26Z soergel $
 */
public class BasicHierarchicalStringObjectMap extends ListHierarchyNode<Map<String, Object>>
		implements HierarchicalStringObjectMap<ListHierarchyNode<Map<String, Object>>>
	{
	// ------------------------------ FIELDS ------------------------------

	//Map<String, Object> contents = new HashMap<String, Object>();

//	private List<HierarchicalStringObjectMap> children = new ArrayList<HierarchicalStringObjectMap>();
//HierarchyNode<Map<String, Object>, HierarchicalStringObjectMap>


	// --------------------- GETTER / SETTER METHODS ---------------------


	/**
	 * {@inheritDoc}
	 */
/*	public List<HierarchicalStringObjectMap> getChildren()//HierarchyNode<Map<String, Object>, HierarchicalStringObjectMap>
		{
		return children;
		}
*/


	// ------------------------ INTERFACE METHODS ------------------------


	// --------------------- Interface HierarchyNode ---------------------

	/**
	 * {@inheritDoc}
	 */
	/*public ListHierarchyNode<Map<String, Object>> newChild()
		{
		//ListHierarchyNode<Map<String, Object>> result = new ListHierarchyNode<Map<String, Object>>();
		BasicHierarchicalStringObjectMap child = new BasicHierarchicalStringObjectMap();
		child.setParent(this);
		//result.setContents(contents);
		//children.add(result);
		return child;
		}
*/
	public ListHierarchyNode<Map<String, Object>> newChild(Map<String, Object> payload)
		{
		//ListHierarchyNode<Map<String, Object>> result = new ListHierarchyNode<Map<String, Object>>();
		BasicHierarchicalStringObjectMap child = new BasicHierarchicalStringObjectMap();
		setPayload(payload);
		child.setParent(this);
		//children.add(result);
		return child;
		}

	// --------------------- Interface Map ---------------------

	/**
	 * {@inheritDoc}
	 */
	public Object put(String s, Object o)
		{
		return getPayload().put(s, o);
		}

	/**
	 * {@inheritDoc}
	 */
	public Object remove(Object o)
		{
		return getPayload().remove(o);
		}

	/**
	 * {@inheritDoc}
	 */
	public void putAll(Map<? extends String, ? extends Object> map)
		{
		getPayload().putAll(map);
		}

	/**
	 * {@inheritDoc}
	 */
	public void clear()
		{
		getPayload().clear();
		}

	// -------------------------- OTHER METHODS --------------------------

	/**
	 * {@inheritDoc}
	 */
/*	@Override
	public void merge()
		{
		//To change body of implemented methods use File | Settings | File Templates.
		}*/

	/**
	 * {@inheritDoc}
	 */
	public int size()
		{
		return getPayload().size();
		}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty()
		{
		return getPayload().isEmpty();
		}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsKey(Object o)
		{
		return getPayload().containsKey(o);
		}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsValue(Object o)
		{
		return getPayload().containsValue(o);
		}

	/**
	 * {@inheritDoc}
	 */
	public Object get(Object o)
		{
		return getPayload().get(o);
		}

	/**
	 * {@inheritDoc}
	 */
	public Set<String> keySet()
		{
		return getPayload().keySet();
		}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Object> values()
		{
		return getPayload().values();
		}

	/**
	 * {@inheritDoc}
	 */
	public Set<Entry<String, Object>> entrySet()
		{
		return getPayload().entrySet();
		}
	}
