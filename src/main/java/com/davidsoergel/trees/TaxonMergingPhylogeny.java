/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * A phylogenetic tree supporting the extraction of subtrees and ignoring branches with no length
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: TaxonMergingPhylogeny.java 341 2009-09-04 21:28:09Z soergel $
 * @JavadocOK
 */
public interface TaxonMergingPhylogeny<T extends Serializable>//extends RootedPhylogeny<T>
	{
	/**
	 * Locate the node with the given id, and navigate up the tree if necessary, until a node is found that has a branch
	 * length greater than zero.
	 *
	 * @param id the T identifying the starting node
	 * @return the T identifying the most recent ancestor of the given node with a nonzero branch length (perhaps the node
	 *         itself)
	 * @throws NoSuchNodeException when the target node is not found in the tree, or when no node with a branch length is
	 *                             found
	 */
	T nearestAncestorWithBranchLength(T id) throws NoSuchNodeException;

	/**
	 * Starting from the given node, navigate up the tree if necessary) until a node is found that has a branch
	 * length greater than zero.
	 *
	 * @param id the T identifying the starting node
	 * @return the T identifying the most recent ancestor of the given node with a nonzero branch length (perhaps the node
	 *         itself)
	 * @throws TreeException when the target node is not found in the tree, or when no node with a branch length is
	 *                             found
	 */
	//PhylogenyNode<T> nearestAncestorWithBranchLength(PhylogenyNode<T> id) throws TreeException;

	/**
	 * Extract a tree which contains exactly those leaves that are requested.  I.e., prunes any branches not leading to
	 * those leaves.  Aggregates chains of nodes with exactly one child each into a single branch of the appropriate
	 * length.  Creates the extracted tree from newly instantiated nodes; does not reuse nodes from the base tree. Some of
	 * the requested leaves my turn out to be internal nodes; that's OK.
	 *
	 * @param ids the Collection<T> of leaves desired for the extracted tree
	 * @return the extracted RootedPhylogeny<T>
	 * @throws NoSuchNodeException when the given collection contains a node id that is not found in the tree
	 */
//	RootedPhylogeny<T> extractTreeWithLeafIDs(Collection<T> ids) throws NoSuchNodeException; //, TreeException;

	/**
	 * Extract a tree which contains exactly those leaves that are requested.  I.e., prunes any branches not leading to
	 * those leaves.  Aggregates chains of nodes with exactly one child each into a single branch of the appropriate
	 * length.  Creates the extracted tree from newly instantiated nodes; does not reuse nodes from the base tree. Some of
	 * the requested leaves my turn out to be internal nodes; that's OK.
	 *
	 * @param ids the Collection<T> of leaves desired for the extracted tree
	 * @return the extracted RootedPhylogeny<T>
	 * @throws TreeException when the given collection contains a node id that is not found in the tree
	 */
//	RootedPhylogeny<T> extractTreeWithLeaves(Collection<PhylogenyNode<T>> ids) throws TreeException;

	/**
	 * Extract a tree which contains exactly those leaves that are requested.  I.e., prunes any branches not leading to
	 * those leaves.  Aggregates chains of nodes with exactly one child each into a single branch of the appropriate
	 * length, if requested.  Creates the extracted tree from newly instantiated nodes; does not reuse nodes from the base
	 * tree. Some of the requested leaves my turn out to be internal nodes; that's OK.
	 *
	 * @param ids               the Collection<T> of leaves desired for the extracted tree
	 * @param ignoreAbsentNodes silently ignore requests for leaves that are not present in the tree, simply returning the
	 *                          extracted tree with those leaves that are found.  I.e., intersect the requested leaf id
	 *                          list with the available leaf ids before constructing the result tree.
	 * @param mode
	 * @return the extracted RootedPhylogeny<T>
	 * @throws NoSuchNodeException when the given collection contains a node id that is not found in the tree and
	 *                             ignoreAbsentNodes is false
	 */
	BasicRootedPhylogeny<T> extractTreeWithLeafIDs(Set<T> ids, boolean ignoreAbsentNodes,
	                                               boolean includeInternalBranches,
	                                               AbstractRootedPhylogeny.MutualExclusionResolutionMode mode)
			throws NoSuchNodeException; //, NodeNamer<T> namer


	BasicRootedPhylogeny<T> extractTreeWithLeafIDs(Set<T> ids, boolean ignoreAbsentNodes,
	                                               boolean includeInternalBranches)
			throws NoSuchNodeException; //, NodeNamer<T> namer

	@NotNull
	List<T> getAncestorPathIds(T id) throws NoSuchNodeException;

	/**
	 * Returns a List of nodes leading from the root to the leaf identified by the given ID.  Guarantees that the nodes are
	 * represented as BasicPhylogenyNodes, copying them if necessary, because we want them to be Serializable.  As a
	 * result, the child lists may not be populated.
	 *
	 * @param id
	 * @return
	 * @throws NoSuchNodeException
	 */
	@NotNull
	List<BasicPhylogenyNode<T>> getAncestorPathAsBasic(T id) throws NoSuchNodeException;

//	@NotNull
//	List<PhylogenyNode<T>> getAncestorPath(T id) throws NoSuchNodeException;
	}
