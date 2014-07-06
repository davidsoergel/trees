/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;


/**
 * A node of a tree with an associated branch length and weight.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: LengthWeightHierarchyNode.java 352 2009-10-14 07:19:16Z soergel $
 * @JavadocOK
 */
public interface LengthWeightHierarchyNode<T, I extends LengthWeightHierarchyNode<T, I>> extends HierarchyNode<T, I>
		//public interface LengthWeightHierarchyNode<T> extends HierarchyNode<T, LengthWeightHierarchyNode<T>>
	{

	/**
	 * Returns the length
	 *
	 * @return the length
	 */
	@Nullable
	Double getLength();

	/**
	 * Sets the length
	 *
	 * @param d the length
	 */
	void setLength(Double d);

	/**
	 * Returns the weight, recomputing it if necessary
	 *
	 * @return the weight
	 */
	@Nullable
	Double getWeight();// throws TreeException;

	/**
	 * Sets the weight
	 *
	 * @param d the weight
	 */
	void setWeight(@Nullable Double d);

	/**
	 * Returns the largest tree distance between any pair of leaves descending from this node.
	 *
	 * @return
	 */
	//@Nullable

	double getLargestLengthSpan();

	/**
	 * Returns the largest tree distance from this node to any leaf.
	 *
	 * @return
	 */
	//@Nullable

	double getGreatestBranchLengthDepthBelow();

	/**
	 * Returns the smallest tree distance from this node to any leaf.
	 *
	 * @return
	 */
	//@Nullable

	double getLeastBranchLengthDepthBelow();


	/**
	 * Returns the total branch length between the root and this node.
	 *
	 * @return the sum of the branch lengths associated with all the nodes on the ancestor path from this node to the root,
	 *         inclusive.
	 */
	double distanceToRoot();

	/**
	 * {@inheritDoc}
	 */
	//	@NotNull
	//	LengthWeightHierarchyNode<T> getChild(T id);


	/**
	 * {@inheritDoc}
	 */
	//	Collection<? extends LengthWeightHierarchyNode<T>> getChildren();


	void toNewick(Writer out, String prefix, String tab, int minClusterSize, double minLabelProb) throws IOException;
	}
