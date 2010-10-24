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

import com.davidsoergel.stats.ContinuousDistribution1D;
import com.google.common.collect.Multiset;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * A rooted phylogeny with branch lengths and weights.  The generic parameter T specifies the type of the node IDs to be
 * used (typically String or Integer); these IDs should be unique.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: RootedPhylogeny.java 344 2009-09-18 22:37:15Z soergel $
 * @JavadocOK
 */
public interface RootedPhylogeny<T extends Serializable> //, N extends PhylogenyNode<T>>
		extends PhylogenyNode<T>, TaxonMergingPhylogeny<T>//, Serializable//, Clusterable<RootedPhylogeny<T>>

	{
	/**
	 * Finds the most recent common ancestor of a set of nodes.
	 *
	 * @param descendantIds the Set<T> of id of descendant nodes
	 * @return the T id of the most recent common ancestor
	 */
	@Nullable
	T commonAncestor(Collection<T> descendantIds) throws NoSuchNodeException;

	/**
	 * Finds the most recent common ancestor of a set of nodes, requiring only a certain proportion of the nodes to agree.
	 *
	 * @param descendantIds the Set<T> of id of descendant nodes
	 * @return the T id of the most recent common ancestor
	 */
	@Nullable
	T commonAncestor(Collection<T> descendantIds, double proportion) throws NoSuchNodeException;

	/**
	 * Finds the most recent common ancestor of two nodes.
	 *
	 * @param nameA the T id of one descendant node
	 * @param nameB the T id of another descendant node
	 * @return the T id of the most recent common ancestor
	 */
	@Nullable
	T commonAncestor(T nameA, T nameB) throws NoSuchNodeException;

	/**
	 * Tells whether one node is a descendant of another
	 *
	 * @param ancestor   the T id of the ancestor node
	 * @param descendant the T id of the query descendant node
	 * @return true if the second id descends from the first
	 */
	boolean isDescendant(T ancestor, T descendant);

	Set<T> selectAncestors(final Collection<T> labels, final T id);

	/**
	 * Tells whether one node is a descendant of another
	 *
	 * @param ancestor   the T id of the ancestor node
	 * @param descendant the T id of the query descendant node
	 * @return true if the second id descends from the first
	 */
	boolean isDescendant(PhylogenyNode<T> ancestor, PhylogenyNode<T> descendant);

	/**
	 * Computes the sum of the branch lengths along the path between two nodes
	 *
	 * @param nameA the T id of one node
	 * @param nameB the T id of another node
	 * @return the double sum of the branch lengths along the path between two nodes
	 */
	double distanceBetween(T nameA, T nameB) throws NoSuchNodeException;

	/**
	 * Computes the sum of the branch lengths along the path between two nodes
	 *
	 * @param nodeA one node
	 * @param nodeB another node
	 * @return the double sum of the branch lengths along the path between two nodes
	 */
	double distanceBetween(PhylogenyNode<T> nodeA, PhylogenyNode<T> nodeB) throws NoSuchNodeException;

	/**
	 * Returns the tree node with the given id.
	 *
	 * @param name the T identifying the node
	 * @return the PhylogenyNode<T> with that id
	 * @throws NoSuchElementException when no node has the requested id
	 */
	@NotNull
	PhylogenyNode<T> getNode(T name) throws NoSuchNodeException;

	double getWeight(T name) throws NoSuchNodeException;

	/**
	 * Gets the root node of the tree
	 *
	 * @return
	 */
	PhylogenyNode<T> getRoot();

	/**
	 * Returns all the nodes in the tree, both internal nodes and leaf nodes.
	 *
	 * @return all the nodes in the tree, both internal nodes and leaf nodes.
	 */
	Map<T, ? extends PhylogenyNode<T>> getUniqueIdToNodeMap();  //** Map<T, ? extends PhylogenyNode<T>> ??

	/**
	 * Returns all the leaf nodes of the tree.
	 *
	 * @return all the leaf nodes of the tree.
	 */
	Collection<PhylogenyNode<T>> getLeaves();

	//RootedPhylogeny<T> extractTreeWithLeaves(Collection<T> ids);

	/**
	 * Starting from the node identified by the given ID, climbs this tree towards the root until a node is found that is
	 * also present in the provided tree.  This is used for stitching together two trees, where one (the provided
	 * rootPhylogeny) provides the topology nearer the root and another (i.e. this one) provides the topology nearer the
	 * leaves.
	 *
	 * @param rootPhylogeny the RootedPhylogeny<T> in which to look for matching nodes
	 * @param leafId        the T identifying the starting node
	 * @return the T identifying the nearest ancestor of the requested node in this tree which is present in the provided
	 *         tree
	 * @throws TreeException when the requested leafId is not present in this tree, or if none of its ancestors are present
	 *                       in the rootPhylogeny.
	 */
	T nearestKnownAncestor(RootedPhylogeny<T> rootPhylogeny, T leafId) throws NoSuchNodeException;

	//T nearestAncestorWithBranchLength(T leafId) throws TreeException;

	/**
	 * Returns the IDs of all the leaf nodes of the tree.
	 *
	 * @return the IDs of all the leaf nodes of the tree.
	 */
	Set<T> getLeafValues();

	/**
	 * Returns the IDs of all the nodes in the tree, both internal nodes and leaf nodes.
	 *
	 * @return the IDs of all the nodes in the tree, both internal nodes and leaf nodes.
	 */
	Set<T> getNodeValues();

	/**
	 * Returns the sum of all branch lengths in the tree.
	 *
	 * @return the the sum of all branch lengths in the tree.
	 */
	double getTotalBranchLength();

	/**
	 * Assigns random weights to all of the leaf nodes by sampling from the given distribution.  After all the weights are
	 * sampled, normalizes them to sum to 1.
	 *
	 * @param speciesAbundanceDistribution the ContinuousDistribution1D from which to sample leaf weights.
	 */
	void randomizeLeafWeights(
			ContinuousDistribution1D speciesAbundanceDistribution);// throws TreeException;// throws DistributionException;

	/**
	 * Assigns equal weights to all of the leaf nodes, and normalizes them to sum to 1.
	 */
	void setLeafWeightsUniform() throws TreeException;// throws DistributionException;

	/**
	 * Normalizes the leaf weights to sum to one, and propagates these up the tree.
	 */
	void normalizeWeights() throws TreeException;

	/**
	 * Returns the phylogeny on which this one was immediately based, if any.
	 *
	 * @return the RootedPhylogeny<T> phylogeny on which this one was immediately based, if this tree was derived or
	 *         extracted somehow from another; returns null otherwise
	 */
	//@NotNull
	@Nullable
	RootedPhylogeny<T> getBasePhylogeny();

	/**
	 * Returns the phylogeny on which this one was ultimtely based, if any.
	 *
	 * @return the RootedPhylogeny<T> phylogeny on which this one was ultimately based, if this tree is the result of a
	 *         chain of derivations or extractions from another tree;  returns this tree otherwise
	 */
	@NotNull
	RootedPhylogeny<T> getBasePhylogenyRecursive();

	/**
	 * Builds a new phylogeny by finding all branches that ultimately lead to a least one leaf in each of two sets.  The
	 * intersection tree contains branches terminating at internal nodes that are common between the two trees, even if
	 * there are no leaves in common below that node.
	 * <p/>
	 * The weights in the resulting tree are taken from this one. but may not be normalized.
	 *
	 * @param leafValues  a Collection<T> of leaf IDs, all of which should be present in this tree
	 * @param leafValues1 another Collection<T> of leaf IDs, all of which should be present in this tree
	 * @return the RootedPhylogeny<T> describing the portion of this tree that is in common between the trees specified by
	 *         the two leaf sets
	 * @throws TreeException when anything goes wrong
	 */
	SerializableRootedPhylogeny<T> extractIntersectionTree(Collection<T> leafValues, Collection<T> leafValues1,
	                                                       NodeNamer<T> namer)
			throws TreeException, NoSuchNodeException;

	/**
	 * Builds a new phylogeny by mixing this tree with another one.  This is useful when the trees represent communities,
	 * in which case the mixed tree represents a literal mixing of the communities.  The two trees in question must have
	 * been extracted from the same underlying tree (e.g., the tree of life), to guarantee that their topologies are
	 * compatible (though of course the weights will differ).  The mixing is done at the leaves; the resulting weights are
	 * then summed up the tree as usual.
	 *
	 * @param phylogeny        the RootedPhylogeny<T> with which to mix this one
	 * @param mixingProportion the double proportion of this tree to use in the mixture; the other tree will be used with
	 *                         (1-mixingProportion)
	 * @return the RootedPhylogeny<T> the mixed phylogeny
	 * @throws TreeException when the requested proportion is not between 0 and 1, or if the two trees are not derived from
	 *                       the same base tree.
	 */
	BasicRootedPhylogeny<T> mixWith(RootedPhylogeny<T> phylogeny, double mixingProportion) throws TreeException;

	/**
	 * Assign leaf weights to this tree based on the weights present in the provided tree, smoothed by adding pseudocounts.
	 * This is useful to avoid weights of zero, in cases where that would cause division errors and such.  The pseudocount
	 * is added uniformly to every leaf of this tree, regardless of the tree topology, and whether or not the node has any
	 * weight in the other tree.  If the other tree assigns weight to nodes that are not present in this tree, those
	 * weights are silently ignored.  Finally the weights in this tree are normalized and summed up the tree as usual.
	 *
	 * @param otherTree       the RootedPhylogeny<T> from which to copy weights
	 * @param smoothingFactor the double pseudocount to add to all leaf weights
	 */
	void smoothWeightsFrom(RootedPhylogeny<T> otherTree, double smoothingFactor)
			throws NoSuchNodeException, TreeException;// throws TreeException;

	/**
	 * {@inheritDoc}
	 */
	RootedPhylogeny<T> clone();

	/**
	 * Sets the leaf weights based on the counts of the node IDs in the provided Multiset.  Normalizes and propagates the
	 * weights up the tree.
	 *
	 * @param leafWeights the Multiset providing counts for each leaf ID.
	 */
	void setLeafWeights(Multiset<T> leafWeights) throws TreeException;

	/**
	 * Sets the leaf weights based on the counts of the node IDs in the provided Multiset.  Normalizes and propagates the
	 * weights up the tree.
	 *
	 * @param leafWeights the Multiset providing counts for each leaf ID.
	 */
	void setLeafWeights(Map<T, Double> leafWeights) throws TreeException;

	Map<T, Double> getLeafWeights() throws TreeException;

	Map<T, Double> getNodeWeights() throws TreeException;


	void setAllBranchLengthsTo(Double d);

	//void saveState();

	//public RootedPhylogeny<T> convertToIDTree(RootedPhylogeny<String> stringTree, NodeNamer<T> namer,
	//                                        TaxonStringIdMapper<T> taxonomyService);

	Map<T, Double> distributeInternalWeightsToLeaves(Map<T, Double> taxIdToWeightMap) throws NoSuchNodeException;

	T getShallowestLeaf();

	PhylogenyNode<T> getFirstBranchingNode();

	T getLeafAtApproximateDistance(T aId, double minDesiredTreeDistance, double maxDesiredTreeDistance)
			throws NoSuchNodeException;
	}
