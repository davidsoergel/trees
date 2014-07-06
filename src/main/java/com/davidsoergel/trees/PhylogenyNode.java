/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * A node of a weighted phylogenetic tree.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: PhylogenyNode.java 353 2009-10-19 17:49:33Z soergel $
 * @JavadocOK
 */
public interface PhylogenyNode<T extends Serializable>
		extends Cloneable, LengthWeightHierarchyNode<T, PhylogenyNode<T>>, Serializable
		//Iterable<PhylogenyNode<T>>,
	{
	/**
	 * {@inheritDoc}
	 */
	@NotNull
	List<? extends PhylogenyNode<T>> getChildren(); // throws NoSuchNodeException; //

	// the "name" of this PhylogenyNode is the same as the "value" of the hierarchynode
	//T getName();

	/**
	 * {@inheritDoc}
	 */
	@Nullable
	PhylogenyNode<T> getParent();

	void setParent(PhylogenyNode<T> parent);

//	boolean hasValue();

	/**
	 * {@inheritDoc}
	 */
	List<? extends PhylogenyNode<T>> getAncestorPath();

	// this was a LinkedList because we need it to be Serializable for caching, but making it immutable means we can't enforce Serializable after all

	List<T> getAncestorPathPayloads();


	/**
	 * Recursively set the weight of this node, and the weights of all of its descendants, to the sum of the weights of the
	 * descendant leaves below each node.
	 */
	//void propagateWeightFromBelow();


	/**
	 * Increment the weight of this node by the given amount.  This will cause the weights of the ancestor nodes to be
	 * inconsistent, so it will likely be necessary to call propagateWeightFromBelow on theroot.
	 *
	 * @param v the double
	 */
	void incrementWeightBy(double v);

	/**
	 * {@inheritDoc}
	 */
	PhylogenyNode<T> clone();

	/**
	 * Returns the current weight without recomputing, even if it is null
	 *
	 * @return the weight
	 */
	Double getCurrentWeight();

	void appendSubtree(StringBuffer sb, String indent);

	/**
	 * Returns the most recent ancestor which has a non-null branch length, even if it is zero.
	 *
	 * @return
	 * @throws NoSuchNodeException
	 */
	PhylogenyNode<T> nearestAncestorWithBranchLength() throws NoSuchNodeException;

	PhylogenyNode<T> findRoot();

//	void addSubtreeToMap(Map<T, PhylogenyNode<T>> uniqueIdToNodeMap, NodeNamer<T> namer);

	RootedPhylogeny<T> asRootedPhylogeny();

	PhylogenyNode<T> getRandomLeafBelow();

	public void collectLeavesBelowAtApproximateDistance(final double minDesiredTreeDistance,
	                                                    final double maxDesiredTreeDistance,
	                                                    Collection<PhylogenyNode<T>> result);
	}
