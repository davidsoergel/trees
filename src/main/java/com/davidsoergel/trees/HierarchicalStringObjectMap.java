/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
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
