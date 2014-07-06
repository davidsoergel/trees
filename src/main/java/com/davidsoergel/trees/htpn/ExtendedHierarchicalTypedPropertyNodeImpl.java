/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees.htpn;

import com.davidsoergel.dsutils.collections.OrderedPair;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class ExtendedHierarchicalTypedPropertyNodeImpl<K extends Comparable, V>
		extends AbstractExtendedHierarchicalTypedPropertyNode<K, V, ExtendedHierarchicalTypedPropertyNodeImpl<K, V>>
		implements ExtendedHierarchicalTypedPropertyNode<K, V, ExtendedHierarchicalTypedPropertyNodeImpl<K, V>>
	{
	/*	public ExtendedHierarchicalTypedPropertyNodeImpl<K, V> newChild()
			 {
			 ExtendedHierarchicalTypedPropertyNodeImpl<K, V> result = new ExtendedHierarchicalTypedPropertyNodeImpl<K, V>();
			 //children.add(result);  // setParent calls registerChild
			 result.setParent(this);
			 return result;
			 }
	 */
	public ExtendedHierarchicalTypedPropertyNodeImpl<K, V> newChild(final OrderedPair<K, V> payload)
		{
		ExtendedHierarchicalTypedPropertyNodeImpl<K, V> result = new ExtendedHierarchicalTypedPropertyNodeImpl<K, V>();
		//children.add(result);  // setParent calls registerChild
		result.setPayload(payload);
		result.setParent(this);
		return result;
		}
/*
	public ExtendedHierarchicalTypedPropertyNodeImpl<K, V> newChildCopyingSubtree(
			final HierarchicalTypedPropertyNode<K, V, ExtendedHierarchicalTypedPropertyNodeImpl<K, V>> copyFrom)
		{
		asdf
		}
*/
	}
