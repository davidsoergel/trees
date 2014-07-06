/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
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
	public Collection values()
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
