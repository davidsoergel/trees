/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.trees;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * A node in a simple hierarchy, where a value of the given generic type is attached at each node.  A node may have any
 * number of children, which are also HierarchyNodes of the same generic type.  The type of Collection that holds the
 * children is up to the implementation, so they may or may not be ordered.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: HierarchyNode.java 524 2009-10-10 07:55:47Z soergel $
 * @See com.davidsoergel.runutils.HierarchicalTypedPropertyNode
 */
public interface HierarchyNode<T, I extends HierarchyNode<T, I>> extends Iterable<I>
	{

	/**
	 * Returns a Collection of nodes that are immediate children of this one.
	 *
	 * @return a Collection of nodes that are immediate children of this one.
	 */
	@NotNull
	Collection<? extends I> getChildren();//throws NoSuchNodeException;//? extends HierarchyNode<? extends T, I>>

	@NotNull
	Collection<? extends I> getDescendantLeaves();

	/**
	 * Gets the child of this node which has the given value
	 *
	 * @param id the T value to search for among the children
	 * @return the PhylogenyNode<T> child with the given value
	 * @throws NoSuchNodeException when no matching child is found
	 */
	@NotNull
	I getChildWithPayload(T id) throws NoSuchNodeException;//HierarchyNode<T, I>

	/**
	 * Tells whether this node is a leaf of the tree or not
	 *
	 * @return false if this node has any children; true otherwise
	 */
	boolean isLeaf();

	/**
	 * Returns the value contained in this node
	 *
	 * @return the value contained in this node
	 */
	T getPayload();

	/**
	 * Sets the value contained in this node
	 */
	void setPayload(T contents);

	/**
	 * Returns the immediate parent of this node in the tree
	 *
	 * @return the immediate parent of this node in the tree, or null if this node is the root.
	 */
	I getParent();//HierarchyNode<? extends T, I>


	/**
	 * Returns a List of nodes describing a path down the tree, starting with the root and ending with this node
	 *
	 * @return a List of nodes describing a path down the tree, starting with the root and ending with this node
	 */
	List<? extends HierarchyNode<T, I>> getAncestorPath();//? extends HierarchyNode<T, I>

	List<? extends HierarchyNode<T, I>> getAncestorPath(boolean includeSelf);

	List<T> getAncestorPathPayloads();

	/**
	 * Creates a new child node of the appropriate type
	 *
	 * @return the new child node
	 */
//	I newChild();//HierarchyNode<? extends T, I>

	/**
	 * Creates a new child node of the appropriate type.  Note that the payload must be provided up front; this helps the
	 * child be more immutable than it otherwise would be (e.g. newChild() followed by setPayload()).
	 *
	 * @return the new child node
	 */
	I newChild(T payload);
	//void setName(String name);


	//String getName();

	/**
	 * Sets the parent node; also informs the parent node via addChild() so that it can maintain the reverse link, if
	 * needed.
	 *
	 * @param parent
	 */
	void setParent(I parent);//HierarchyNode<T, I>

	/**
	 * Adds a child node.  Does not update the child's parent link!  The relationship is primarily owned by the child, so
	 * child.setParent() should call this method, not the other way around.
	 *
	 * @param a the child node to add
	 */
	void registerChild(I a);

	/**
	 * Removes a child node.  Does not update the child's parent link!  The relationship is primarily owned by the child,
	 * so child.setParent() should call this method, not the other way around.
	 *
	 * @param a the child node to add
	 */
	void unregisterChild(I a);

	/**
	 * Get an iterator that returns all the nodes of the tree in depth-first order.  The breadth ordering may or may not be
	 * defined, depending on the HierarchyNode implementation.  We provide this depth-first iterator explicitly even though
	 * the HierarchyNode is itself iterable, because the default iterator may be breadth-first or something else.
	 *
	 * @return the DepthFirstTreeIterator<T, I>
	 */
	DepthFirstTreeIterator<T, I> depthFirstIterator();

	/**
	 * In some cases we need to wrap a HierarchyNode in another object that provides a facade.  In this case, wthis method
	 * return the underlying object; otherwise it just returns this.
	 *
	 * @return the most raw available HierarchyNode embedded within this facade (perhps just this object itself)
	 */
	HierarchyNode<T, I> getSelfNode();//HierarchyNode<T, I>

	int countDescendantsIncludingThis();
	}
