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

import org.jetbrains.annotations.NotNull;
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
	void setWeight(@NotNull Double d);

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
