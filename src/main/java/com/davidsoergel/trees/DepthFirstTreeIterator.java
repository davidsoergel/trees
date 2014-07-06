/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees;

import java.util.Iterator;


/**
 * An Iterator that provides all of the nodes of a tree in depth-first order.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: DepthFirstTreeIterator.java 269 2008-09-23 02:20:39Z soergel $
 */
public interface DepthFirstTreeIterator<T, I extends HierarchyNode<T, I>> extends Iterator<I>
	{
	/**
	 * Skip all descendants of the given node.  After calling this method, the next node returned by the iterator will be
	 * the next sibling (or uncle, etc.) of the given node.  The given node must be on the path between the root and the
	 * current node.  This is useful when performing some kinds of searches, where an entire subtree can be pruned once
	 * some condition is met.
	 *
	 * @param node the HierarchyNode<T, I> whose descendants are to be skipped.
	 * @throws TreeException when the node is not on the current tree path.
	 */
	void skipAllDescendants(HierarchyNode<T, I> node) throws TreeException;
	}
