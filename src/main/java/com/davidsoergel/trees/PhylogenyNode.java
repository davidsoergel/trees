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

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
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
	}
