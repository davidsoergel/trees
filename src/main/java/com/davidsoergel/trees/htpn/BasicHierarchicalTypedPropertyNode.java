/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees.htpn;

import com.davidsoergel.dsutils.collections.OrderedPair;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: HierarchicalTypedPropertyNodeImpl.java 255 2009-08-01 22:53:03Z soergel $
 */

public class BasicHierarchicalTypedPropertyNode<K extends Comparable, V>
		extends AbstractHierarchicalTypedPropertyNode<K, V, BasicHierarchicalTypedPropertyNode<K, V>>
	{

	/*	public BasicHierarchicalTypedPropertyNode<K, V> newChild()
		 {
		 BasicHierarchicalTypedPropertyNode<K, V> result = new BasicHierarchicalTypedPropertyNode<K, V>();
		 //children.add(result);  // setParent calls registerChild
		 result.setParent(this);
		 return result;
		 }
 */
	public BasicHierarchicalTypedPropertyNode<K, V> newChild(final OrderedPair<K, V> payload)
		{
		BasicHierarchicalTypedPropertyNode<K, V> result = new BasicHierarchicalTypedPropertyNode<K, V>();
		//children.add(result);  // setParent calls registerChild
		result.setPayload(payload);
		result.setParent(this);
		return result;
		}
	}
