/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees;

import java.util.Iterator;


/**
 * Returns the nodes in the tree in depth-first order.  The branches from a given node have no ordering, though, so the
 * ordering is not guaranteed; the depth-first guarantee is only that, once a node is provided, all of its descendants
 * will be provided before any of its siblings.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: DepthFirstTreeIteratorImpl.java 336 2009-04-01 00:37:25Z soergel $
 */
public class DepthFirstTreeIteratorImpl<T, I extends HierarchyNode<T, I>> implements DepthFirstTreeIterator<T, I>
	{
	I root;
	Iterator<? extends HierarchyNode<T, I>> breadthIterator = null;
	DepthFirstTreeIterator<T, I> subtreeIterator = null;

	public DepthFirstTreeIteratorImpl(I root)
		{
		this.root = root;
		}

	/**
	 * Returns <tt>true</tt> if the iteration has more elements. (In other words, returns <tt>true</tt> if <tt>next</tt>
	 * would return an element rather than throwing an exception.)
	 *
	 * @return <tt>true</tt> if the iterator has more elements.
	 */
	public boolean hasNext()
		{
		return
				// we haven't yet returned this node
				breadthIterator == null

				// there is a child that we haven't yet returned
				|| (subtreeIterator != null && subtreeIterator.hasNext())

				// the current child has pending nodes
				|| breadthIterator.hasNext();
		}

	/**
	 * Returns the next element in the iteration.  Calling this method repeatedly until the {@link #hasNext()} method
	 * returns false will return each element in the underlying collection exactly once.
	 *
	 * @return the next element in the iteration
	 * @throws java.util.NoSuchElementException
	 *          iteration has no more elements.
	 */
	public I next()
		{
		// this whole class, and especially this method, are pretty confusing.
		// expanded the logic a bit for clarity

		if (breadthIterator == null)
			{
			// we haven't yet returned this node

			// prep the iterators for the next call
			breadthIterator = root.getChildren().iterator();

			if (breadthIterator.hasNext())
				{
				subtreeIterator = breadthIterator.next().depthFirstIterator();
				}
			// else this node has no children

			return root;
			}
		else if (subtreeIterator == null)
			{
			// this node has no children, and we've already returned the node itself.
			return null;
			}
		else
			{
			// the currently selected subtree has more nodes
			if (subtreeIterator.hasNext())
				{
				return subtreeIterator.next();
				}
			else
				// there is a currently selected subtree, but it's exhausted.  Try the next one.
				{
				if (breadthIterator.hasNext())
					{
					// note the next subtreeIterator is guaranteed to have at least one node: the immediate child itself,
					// even if it has no descendants
					subtreeIterator = breadthIterator.next().depthFirstIterator();
					return subtreeIterator.next();
					}
				else
					{
					// the currently selected subtree is exhausted, and there aren't any more.
					return null;
					}
				}
			}
		}

	public void skipAllDescendants(HierarchyNode<T, I> node) throws TreeException
		{
		if (node == root)
			{
			// we want to produce the situation that causes next() to fire the breadthIterator.
			// if there is no sibling, then hasNext() will detect the same situation so the parent iterator will fire its breadthIterator, etc.

			// The conditions are:
			// the breadthIterator must exist, indicating that this node itself has been consumed.
			if (breadthIterator == null)
				{
				throw new TreeException("Can't skip descendants of a node that hasn't been returned yet");
				}

			// the subtreeIterator must equal null, making it look like the node has no children
			subtreeIterator = null;

			// the breadthIterator must be exhausted, making it look like all the subtrees have been processed

			while (breadthIterator.hasNext())// annoying way to consume the children
				{
				breadthIterator.next();
				}
			}
		else
			{
			if (subtreeIterator != null)
				{
				subtreeIterator.skipAllDescendants(node);
				}
			else
				{
				// we haven't found the requested node on the path so far, and there is no further selected subtree from here.
				throw new TreeException("Can't skip descendants of a node that is not on the current path");
				}
			}
		}


	/**
	 * Removes from the underlying collection the last element returned by the iterator (optional operation).  This method
	 * can be called only once per call to <tt>next</tt>.  The behavior of an iterator is unspecified if the underlying
	 * collection is modified while the iteration is in progress in any way other than by calling this method.
	 *
	 * @throws UnsupportedOperationException if the <tt>remove</tt> operation is not supported by this Iterator.
	 * @throws IllegalStateException         if the <tt>next</tt> method has not yet been called, or the <tt>remove</tt>
	 *                                       method has already been called after the last call to the <tt>next</tt>
	 *                                       method.
	 */
	public void remove()
		{
		throw new UnsupportedOperationException();
		}
	}
