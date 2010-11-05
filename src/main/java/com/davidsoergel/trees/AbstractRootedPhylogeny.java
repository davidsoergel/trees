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

import com.davidsoergel.dsutils.collections.ConcurrentHashWeightedSet;
import com.davidsoergel.dsutils.collections.DSCollectionUtils;
import com.davidsoergel.dsutils.collections.MutableWeightedSet;
import com.davidsoergel.stats.ContinuousDistribution1D;
import com.google.common.collect.Multiset;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;


/**
 * Abstract implementation of the RootedPhylogeny interface, providing all required functionality that is not
 * implementation-specific.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: AbstractRootedPhylogeny.java 354 2009-10-19 19:01:44Z soergel $
 */

public abstract class AbstractRootedPhylogeny<T extends Serializable> implements RootedPhylogeny<T>
	{
	private static final Logger logger = Logger.getLogger(AbstractRootedPhylogeny.class);
	protected transient RootedPhylogeny<T> basePhylogeny = null;

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public T commonAncestor(Collection<T> knownMergeIds) throws NoSuchNodeException
		{
		return commonAncestor(knownMergeIds, 1.0);
		}

	public RootedPhylogeny<T> asRootedPhylogeny()
		{
		return this;
		}

	private String name;

	public String getName()
		{
		return name;
		}

	public void setName(final String name)
		{
		this.name = name;
		}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public T commonAncestor(Collection<T> knownMergeIds, double proportion) throws NoSuchNodeException
		{
		Set<List<PhylogenyNode<T>>> theDisposableAncestorLists = new HashSet<List<PhylogenyNode<T>>>();
		for (T id : knownMergeIds)
			{
			try
				{
				PhylogenyNode<T> node = getNode(id);
				theDisposableAncestorLists.add(new ArrayList<PhylogenyNode<T>>(node.getAncestorPath()));
				}
			catch (NoSuchNodeException e)
				{
				logger.debug("Node not found with id " + id + " when looking for common ancestor; ignoring");
				}
			}

		int numberThatMustAgree = (int) Math.ceil(theDisposableAncestorLists.size() * proportion);

		PhylogenyNode<T> commonAncestor = null;

		try
			{
			while (true)
				{
				commonAncestor = DSCollectionUtils.getDominantFirstElement(theDisposableAncestorLists,
				                                                           numberThatMustAgree);  // throws NoSuchElementException
				theDisposableAncestorLists =
						DSCollectionUtils.filterByAndRemoveFirstElement(theDisposableAncestorLists, commonAncestor);
				}
			}
		catch (NoSuchElementException e)
			{
			// good, broke the loop, leaving commonAncestor and theAncestorLists in the most recent valid state
			}


		/*
		while (DSCollectionUtils.allFirstElementsEqual(theAncestorLists))
			{
			commonAncestor = DSCollectionUtils.removeAllFirstElements(theAncestorLists);
			}
*/

		if (commonAncestor == null)
			{
			throw new NoSuchNodeException("Nodes have no common ancestor");
			//return null;
			}

		return commonAncestor.getPayload();
		}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public T commonAncestor(T nameA, T nameB) throws NoSuchNodeException
		{
		//commonAncestorCache.get(nameA, nameB)
		PhylogenyNode<T> a = getNode(nameA);
		PhylogenyNode<T> b = getNode(nameB);
		return commonAncestor(a, b).getPayload();
		}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public PhylogenyNode<T> commonAncestor(@NotNull PhylogenyNode<T> a, @NotNull PhylogenyNode<T> b)
			throws NoSuchNodeException
		{
		List<PhylogenyNode<T>> ancestorsA = new LinkedList<PhylogenyNode<T>>(a.getAncestorPath());
		List<PhylogenyNode<T>> ancestorsB = new LinkedList<PhylogenyNode<T>>(b.getAncestorPath());

		PhylogenyNode<T> commonAncestor = null;
		while (ancestorsA.size() > 0 && ancestorsB.size() > 0 && ancestorsA.get(0).equals(ancestorsB.get(0)))
			{
			commonAncestor = ancestorsA.remove(0);
			ancestorsB.remove(0);
			}

		if (commonAncestor == null)
			{
			throw new NoSuchNodeException("Nodes have no common ancestor");
			}

		return commonAncestor;
		}

	public boolean isDescendant(T ancestor, T descendant)
		{
		try
			{
			//** depends on PhylogenyNode.equals() working right.  Does it?
			List<T> ancestorPath = getAncestorPathIds(descendant);
			if (ancestorPath == null)
				{
				return false;
				}
			return ancestorPath.contains(ancestor);

			// lame
			//return ancestor.equals(commonAncestor(ancestor, descendant));
			}
		catch (NoSuchNodeException e)
			{
			return false;
			}
		}

	public Set<T> selectAncestors(final Collection<T> labels, final T id)
		{
		try
			{
			List<T> ancestorPath = getAncestorPathIds(id);
			return DSCollectionUtils.intersectionSet(ancestorPath, labels);
			}
		catch (NoSuchNodeException e)
			{
			return new HashSet<T>();
			}
		}


	public boolean isDescendant(PhylogenyNode<T> ancestor, PhylogenyNode<T> descendant)
		{
		try
			{
			return ancestor.equals(commonAncestor(ancestor, descendant));
			}
		catch (NoSuchNodeException e)
			{
			return false;
			}
		}

	/**
	 * {@inheritDoc}
	 */
/*	@NotNull
	public RootedPhylogeny<T> extractTreeWithLeafIDs(Collection<T> ids) throws NoSuchNodeException
		{
		return extractTreeWithLeafIDs(ids, false, false);
		}*/

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public BasicRootedPhylogeny<T> extractTreeWithLeafIDs(Set<T> ids, boolean ignoreAbsentNodes,
	                                                      boolean includeInternalBranches)
			throws NoSuchNodeException //, NodeNamer<T> namer
		{
		return extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches,
		                              MutualExclusionResolutionMode.EXCEPTION);
		}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public BasicRootedPhylogeny<T> extractTreeWithLeafIDs(Set<T> ids, boolean ignoreAbsentNodes,
	                                                      boolean includeInternalBranches,
	                                                      MutualExclusionResolutionMode mode)
			throws NoSuchNodeException //, NodeNamer<T> namer

		{
		/*try
			{
			if (getLeafValues().equals(ids) && includeInternalBranches)
				{
				return this;
				}
			}
		catch (TreeRuntimeException e)
			{
			// the actual tree is expensive to load (e.g. NcbiTaxonomyService) so getLeafValues is a bad idea
			// OK, just do the explicit extraction anyway then
			}
*/
		/*
		List<PhylogenyNode<T>> theLeaves = idsToLeaves(ids, ignoreAbsentNodes);


		if (theLeaves.isEmpty())
			{
			throw new NoSuchNodeException("No leaves found for ids: " + ids);
			}

		RootedPhylogeny<T> result = extractTreeWithLeaves(theLeaves, includeInternalBranches, mode); */
		Set<List<? extends PhylogenyNode<T>>> theDisposableLeafPaths =
				idsToDisposableBasicLeafPaths(ids, ignoreAbsentNodes);


		if (theDisposableLeafPaths.isEmpty())
			{
			throw new NoSuchNodeException("No leaves found for ids: " + ids);
			}

		BasicRootedPhylogeny<T> result =
				extractTreeWithLeafPaths(theDisposableLeafPaths, includeInternalBranches, mode);

		Collection<T> gotLeaves = result.getLeafValues();

		Collection<T> gotNodes = result.getNodeValues();

		// all the leaves that were found were leaves that were requested
		assert ids.containsAll(gotLeaves);

		// BAD confusing interaction between all three parameters
		//if (includeInternalBranches && !ignoreAbsentNodes) //(mode == MutualExclusionResolutionMode.LEAF || mode == MutualExclusionResolutionMode.BOTH))
		if (!ignoreAbsentNodes)
			{
			// some requested leaves may turn out to be internal nodes, but at least they should all be accounted for
			assert gotNodes.containsAll(ids);
			}


		/*	if (!ignoreAbsentNodes)
			{
			// any requested leaves that turned out to be internal nodes should have had a phantom leaf added
			assert gotLeaves.containsAll(ids);
			}
		*/
		return result;
		}

/*	private List<PhylogenyNode<T>> idsToLeaves(Set<T> ids, boolean ignoreAbsentNodes) throws NoSuchNodeException
		{
		// don't use HashSet, to avoid calling hashcode since that requires a transaction
		//Set<PhylogenyNode<T>> theLeaves = new HashSet<PhylogenyNode<T>>();
		List<PhylogenyNode<T>> theLeaves = new ArrayList<PhylogenyNode<T>>();
		for (T id : ids)
			{
			try
				{
				PhylogenyNode<T> n = getNode(id);
				theLeaves.add(n);
				}
			catch (NoSuchNodeException e)
				{
				if (!ignoreAbsentNodes)
					{
					throw new NoSuchNodeException("Can't extract tree; requested node " + id + " not found");
					}
				}
			}

		return theLeaves;
		}*/


	/**
	 * @param ids
	 * @param ignoreAbsentNodes
	 * @return
	 * @throws NoSuchNodeException
	 */
	protected Set<List<? extends PhylogenyNode<T>>> idsToDisposableBasicLeafPaths(Set<T> ids, boolean ignoreAbsentNodes)
			throws NoSuchNodeException
		{
		// don't use HashSet, to avoid calling hashcode since that requires a transaction
		//Set<PhylogenyNode<T>> theLeaves = new HashSet<PhylogenyNode<T>>();
		Set<List<? extends PhylogenyNode<T>>> theLeafPaths = new HashSet<List<? extends PhylogenyNode<T>>>();
		for (T id : ids)
			{
			try
				{
				List<? extends PhylogenyNode<T>> safecopy = new ArrayList<PhylogenyNode<T>>(getAncestorPathAsBasic(id));
				theLeafPaths.add(safecopy);
				}
			catch (NoSuchNodeException e)
				{
				if (!ignoreAbsentNodes)
					{
					throw new NoSuchNodeException("Can't extract tree; requested node " + id + " not found");
					}
				}
			}

		return theLeafPaths;
		}


	@NotNull
	public BasicRootedPhylogeny<T> extractTreeWithLeaves(Collection<? extends PhylogenyNode<T>> leaves,
	                                                     boolean includeInternalBranches,
	                                                     MutualExclusionResolutionMode mode) //, NodeNamer<T> namer)
		{
		// leaves must be unique, so we really wanted a Set, but we had to use a List in idsToLeaves above to avoid calling hashcode.
		// Still, the ids were in a Set to begin with, so uniqueness should be guaranteed anyway.
		// assert DSCollectionUtils.setOfLastElements(leaves).size() == leaves.size();

		// we're going to destroy the ancestorlists in the process of extracting the tree, so make copies first

		final Set<List<? extends PhylogenyNode<T>>> theDisposableAncestorLists =
				new HashSet<List<? extends PhylogenyNode<T>>>(leaves.size());
		for (final PhylogenyNode<T> leaf : leaves)
			{
			theDisposableAncestorLists.add(new ArrayList<PhylogenyNode<T>>(leaf.getAncestorPath()));
			}

		return extractTreeWithLeafPaths(theDisposableAncestorLists, includeInternalBranches, mode);
		}


	@NotNull
	public BasicRootedPhylogeny<T> extractTreeWithLeafPaths(
			final Set<List<? extends PhylogenyNode<T>>> theDisposableAncestorLists,
			final boolean includeInternalBranches, final MutualExclusionResolutionMode mode)
		{
		BasicPhylogenyNode<T> commonAncestor = null;
		try
			{
			commonAncestor =
					extractSubtreeWithLeafPaths(theDisposableAncestorLists, includeInternalBranches, mode); //, namer);
			}
		catch (NoSuchNodeException e)
			{
			logger.error("Error", e);
			throw new TreeRuntimeException(e);
			}

		// always use the same root, even if it has only one child
		BasicRootedPhylogeny<T> newTree = new BasicRootedPhylogeny<T>(this.getPayload());

		if (!commonAncestor.getPayload().equals(this.getPayload()))
			{
			// add a single branch descending from the root to the common ancestor
			//** this will have a length of zero, I think, but that's OK ??
			commonAncestor.setParent(newTree.getRoot());
			//newRoot = new BasicPhylogenyNode<T>(newRoot, commonAncestor.getValue(), commonAncestor.getLength());
			}
		else
			{
			newTree.setRoot(commonAncestor);
			}

		newTree.assignUniqueIds(new RequireExistingNodeNamer<T>(true));
		newTree.setBasePhylogeny(this);

		//		assert newTree.getNodes().containsAll(leaves);
		//		assert CollectionUtils.isEqualCollection(newTree.getLeaves(),leaves);

		return newTree;
		}

/*
   private void deepCopy(PhylogenyNode<T> from, BasicPhylogenyNode<T> to)
	   {
	   for (PhylogenyNode<T> fromChild : from.getChildren())
		   {
		   BasicPhylogenyNode<T> toChild = new BasicPhylogenyNode<T>(to, fromChild);// may produce ClassCastException
		   deepCopy(fromChild, toChild);
		   //child.setParent(newRoot);
		   }
	   }
   */

	public List<T> getAncestorPathIds(final T id) throws NoSuchNodeException
		{
		return getNode(id).getAncestorPathPayloads();
		}

	public List<? extends PhylogenyNode<T>> getAncestorPath(final T id) throws NoSuchNodeException
		{
		return getNode(id).getAncestorPath();
		}

	@NotNull
	public List<BasicPhylogenyNode<T>> getAncestorPathAsBasic(final T id) throws NoSuchNodeException
		{
		List<? extends PhylogenyNode<T>> orig = getNode(id).getAncestorPath();

		ArrayList<BasicPhylogenyNode<T>> result = new ArrayList<BasicPhylogenyNode<T>>();
		BasicPhylogenyNode<T> parent = null;
		for (PhylogenyNode<T> origNode : orig)
			{
			BasicPhylogenyNode<T> convertedNode;
			if (origNode instanceof BasicPhylogenyNode)
				{
				convertedNode = (BasicPhylogenyNode<T>) origNode;
				}
			else
				{
				convertedNode = new BasicPhylogenyNode<T>(parent, origNode);
				}
			result.add(convertedNode);
			parent = convertedNode;
			}
		return Collections.unmodifiableList(result);
		}

	/**
	 * When we request extraction of a tree with a bunch of nodes, and one of those nodes is an ancestor of the other, do
	 * we include only the leaf, only the ancestor, both, or throw an exception?  BOTHNOBRANCHLENGTH is a compromise
	 * between ANCESTOR and BOTH; both nodes are provided, but all branch lengths below the ancestor are set to zero.
	 */
	public enum MutualExclusionResolutionMode
		{
			LEAF, ANCESTOR, BOTH, EXCEPTION  //BOTHNOBRANCHLENGTH,
		}

	/**
	 * Builds a fresh tree containing all of the requested leaves, which are the last elements in the provided
	 * AncestorLists. Each AncestorList describes the path from the root to one of the leaves.  The roots (the first
	 * element of each list) must be equal; a copy of that root provides the root of the newly built tree. If
	 * includeInternalBranches is set, then all elements of the AncestorLists will be included in the resulting tree even
	 * if there is no branching at that node.
	 *
	 * @param theDisposableAncestorLists
	 * @return
	 * @throws TreeException
	 */
	@NotNull
	protected BasicPhylogenyNode<T> extractSubtreeWithLeafPaths(
			Set<List<? extends PhylogenyNode<T>>> theDisposableAncestorLists, boolean includeInternalBranches,
			MutualExclusionResolutionMode mode) throws NoSuchNodeException  //, NodeNamer<T> namer)
		{
		BasicPhylogenyNode<T> result;

		// this was spaghetti before when I tried to handle both modes together
		if (includeInternalBranches)
			{
			result = extractSubtreeWithLeafPathsIncludingInternal(theDisposableAncestorLists, mode);
			}
		else
			{
			result = extractSubtreeWithLeafPathsExcludingInternal(theDisposableAncestorLists, mode);
			}

		// currently we deal with MutualExclusionResolutionMode (in the form of allowRequestingInternal nodes) in the course of the tree-building recursion above,
		// but, would it be easier to build the whole tree and then postprocess?
/*
		for (PhylogenyNode<T> node : result)
			{
			gah
			}
*/
		return result;
		}

	private BasicPhylogenyNode<T> extractSubtreeWithLeafPathsExcludingInternal(
			Set<List<? extends PhylogenyNode<T>>> theDisposableAncestorLists, MutualExclusionResolutionMode mode)
			throws NoSuchNodeException
		{
		double accumulatedLength = 0;

		// use this as a marker to test that the provided lists were actually consistent
		PhylogenyNode<T> commonAncestor = null;
		BasicPhylogenyNode<T> bottomOfChain = null;

		// first consume any common prefix on the ancestor lists
		do
			{
			while (DSCollectionUtils.allFirstElementsEqual(theDisposableAncestorLists))
				{
				commonAncestor = DSCollectionUtils.removeAllFirstElements(theDisposableAncestorLists);

				Double d = commonAncestor.getLength();

				if (d == null)
					{
					//logger.warn("Ignoring null length at node " + commonAncestor);
					}
				else
					{
					accumulatedLength += d;
					}
				}
			}
		while (!resolveMutualExclusion(theDisposableAncestorLists, mode));
		// that returns false only if we're in LEAF mode, i.e. so far we found a requested ancestor node but we want to ignore it

		// if includeInternalBranches is off, and BOTH mode is requested, and the requested ancestor node happens to be a branch point anyway, then that's OK.
		// But if BOTH mode requires including a node that is disallowed because includeInternalBranches is off... well, just include it anyway.


		// now the lists must differ in their first position, and commonAncestor is set to the immediate parent of whatever the list heads are

		if (commonAncestor == null)  // only possible if allFirstElementsEqual == false on the first attempt
			{
			throw new NoSuchNodeException("Provided ancestor lists do not have a common root");
			}

		// since we are not including internal branches, we now need to create the branching node

		BasicPhylogenyNode<T> node = new BasicPhylogenyNode<T>();
		node.setLength(accumulatedLength);

		// the commonAncestor is now the most recent one, so that's the most sensible name for the new node
		node.setPayload(commonAncestor.getPayload());
		node.setWeight(commonAncestor.getWeight());
		bottomOfChain = node;


		// split the ancestor lists into sets with a common head

		Collection<Set<List<? extends PhylogenyNode<T>>>> childAncestorLists =
				separateFirstAncestorSets(theDisposableAncestorLists);
		assert childAncestorLists.size() != 1; // otherwise there should be no branch here

		// recurse

		for (Set<List<? extends PhylogenyNode<T>>> childAncestorList : childAncestorLists)
			{
			PhylogenyNode<T> child = extractSubtreeWithLeafPathsExcludingInternal(childAncestorList, mode);
			child.setParent(bottomOfChain);
			}

		return bottomOfChain.findRoot();
		}


	private BasicPhylogenyNode<T> extractSubtreeWithLeafPathsIncludingInternal(
			Set<List<? extends PhylogenyNode<T>>> theDisposableAncestorLists, MutualExclusionResolutionMode mode)
			throws NoSuchNodeException
		{
		// use this as a marker to test that the provided lists were actually consistent
		PhylogenyNode<T> commonAncestor = null;
		BasicPhylogenyNode<T> bottomOfChain = null;

		// first consume any common prefix on the ancestor lists

		while (DSCollectionUtils.allFirstElementsEqual(theDisposableAncestorLists))
			{
			commonAncestor = DSCollectionUtils.removeAllFirstElements(theDisposableAncestorLists);

			// copy the common ancestor to the new tree

			BasicPhylogenyNode<T> node = new BasicPhylogenyNode<T>();
			node.setLength(commonAncestor.getLength());
			node.setPayload(commonAncestor.getPayload());

			//** avoid isLeaf due to ncbi lazy initialization issue
			//if (commonAncestor.isLeaf())
			//	{
			// don't bother with internal weights; they'll get recalculated on demand anyway
			if (bottomOfChain != null)
				{
				bottomOfChain.setWeight(null); // just to be sure
				}
			try
				{
				node.setWeight(commonAncestor.getWeight());
				}
			catch (NotImplementedException e)
				{
				node.setWeight(1.0);
				}

			node.setParent(bottomOfChain);
			bottomOfChain = node;


			// we don't react to the result of resolveMutualExclusion, but we have to run it anyway in case we're in ANCESTOR mode and should prune a subtree
			resolveMutualExclusion(theDisposableAncestorLists, mode);
			/*if(resolveMutualExclusion(theAncestorLists, mode))
				{
				// we wanted to include this node anyway since we're in ANCESTOR or BOTH mode; no problem
				}
			else
				{
				// we requested an ancestor but we are in LEAF mode.  If we weren't including internal branches, we'd want to ignore this node.
				// since we are, though, we'll include it anyway (i.e., if we just ignored the "ancestor" path, this node would still be on the
				// "leaf" path, so we'd want to include it.
				}
			*/
			}

		// now the lists must differ in their first position, and commonAncestor is set to the immediate parent of whatever the list heads are

		if (commonAncestor == null)  // only possible if allFirstElementsEqual == false on the first attempt
			{
			throw new NoSuchNodeException("Provided ancestor lists do not have a common root");
			}

		// split the ancestor lists into sets with a common head

		Collection<Set<List<? extends PhylogenyNode<T>>>> childAncestorLists =
				separateFirstAncestorSets(theDisposableAncestorLists);

		// recurse

		for (Set<List<? extends PhylogenyNode<T>>> childAncestorList : childAncestorLists)
			{
			PhylogenyNode<T> child = extractSubtreeWithLeafPathsIncludingInternal(childAncestorList, mode);
			child.setParent(bottomOfChain);
			}

		return bottomOfChain.findRoot();
		}


/*
	private void addPhantomLeafIfNeeded(Set<List<PhylogenyNode<T>>> theAncestorLists, BasicPhylogenyNode<T> node, NodeNamer<T> namer)
		{
		// check if we need a leaf node here
		boolean needStubLeafNode = false;
		for (List<PhylogenyNode<T>> ancestorList : theAncestorLists)
			{
			if (ancestorList.isEmpty())
				{
				needStubLeafNode = true;
				break;
				}
			}

		if (needStubLeafNode)
			{
			// an internal node was requested as a leaf.
			// add a phantom leaf to honor the request, and then continue with the other paths

			BasicPhylogenyNode<T> leaf = new BasicPhylogenyNode<T>();
			leaf.setLength(0.0);
			leaf.setValue(node.getValue());
			leaf.setParent(node);

			//** changing the ID of the internal node may cause trouble later, e.g. when trying to make an intersection tree
			node.setValue(namer.uniqueify(node.getValue()));
			// note we leave bottomOfChain intact
			}
		}
*/

	/**
	 * Returns true if the current node should be included; false if it should be ignored
	 *
	 * @param theDisposableAncestorLists
	 * @param mode
	 * @return
	 */
	private boolean resolveMutualExclusion(Set<List<? extends PhylogenyNode<T>>> theDisposableAncestorLists,
	                                       MutualExclusionResolutionMode mode)
		{
		// if there is only one list left, and it's empty, that's OK, we just finished a branch
		if (theDisposableAncestorLists.size() == 1)
			{
			return true;
			}
		assert theDisposableAncestorLists.size() > 1;


		// but if there's more than one, and one of them is empty, then we asked for a node as a leaf that turns out to be an ancestor of another leaf.
		// if we give the same path twice, that causes a failure here.  Note leaf id uniqueness constraints above.

		Iterator<List<? extends PhylogenyNode<T>>> iterator = theDisposableAncestorLists.iterator();
		while (iterator.hasNext())
			{
			List<? extends PhylogenyNode<T>> ancestorList = iterator.next();

			// there can be at most one empty list here due to leaf id uniqueness, so it's safe to return immediately rather than testing the rest
			if (ancestorList.isEmpty())
				{
				if (mode == MutualExclusionResolutionMode.LEAF)
					{
					// don't include this node (we're currently at the ancestor)
					iterator.remove();
					return false;
					}
				if (mode == MutualExclusionResolutionMode.ANCESTOR)
					{
					// remove all paths extending below this.

					// this would cause ConcurrentModificationException except that we return and never touch the iterator again
					theDisposableAncestorLists.clear();
					theDisposableAncestorLists
							.add(new ArrayList<PhylogenyNode<T>>()); // the ancestor itself.  Maybe not strictly necessary, but for consistency anyway
					return true;
					}
				if (mode == MutualExclusionResolutionMode.BOTH)
					{
					iterator.remove();
					return true;
					}
				else // if (mode == EXCEPTION)
					{
					throw new TreeRuntimeException("Requested extraction of an internal node as a leaf");
					}
				}
			}

		/*for (List<PhylogenyNode<T>> ancestorList : theAncestorLists)
					   {
					   if (ancestorList.isEmpty())
						   {
						   throw new TreeRuntimeException("Requested extraction of an internal node as a leaf");
						   }
					   }*/

		// if we got here, then there are multiple ancestor lists and none of them is empty, so this is a branch point.
		return true;
		}


	private Collection<Set<List<? extends PhylogenyNode<T>>>> separateFirstAncestorSets(
			Set<List<? extends PhylogenyNode<T>>> theAncestorLists)
		{
		// assert allFirstElementsEqual(theAncestorLists);

		Map<PhylogenyNode<T>, Set<List<? extends PhylogenyNode<T>>>> theSeparatedSets =
				new HashMap<PhylogenyNode<T>, Set<List<? extends PhylogenyNode<T>>>>();

		for (List<? extends PhylogenyNode<T>> theAncestorList : theAncestorLists)
			{
			if (theAncestorList.isEmpty())
				{
				//we've arrived at one of the originally requested nodes.

				// if it's a leaf, then theAncestorLists should contain only one (empty) list.
				// no problem, we just return an empty set since there are no children.

				// if it's an internal node, we can just ignore it since it's already accounted for in the subtree extraction.
				// we do want to process any descendants though.

				// in either case, we just ignore this situation.
				}
			else
				{
				PhylogenyNode<T> commonAncestor = theAncestorList.get(0);
				Set<List<? extends PhylogenyNode<T>>> theChildList = theSeparatedSets.get(commonAncestor);
				if (theChildList == null)
					{
					theChildList = new HashSet<List<? extends PhylogenyNode<T>>>();
					theSeparatedSets.put(commonAncestor, theChildList);
					}
				theChildList.add(theAncestorList);
				}
			}

		return theSeparatedSets.values();
		}


	/**
	 * {@inheritDoc}
	 */
	public double distanceBetween(T nameA, T nameB) throws NoSuchNodeException
		{
		PhylogenyNode a = getNode(nameA);
		PhylogenyNode b = getNode(nameB);
		return distanceBetween(a, b);
		}

	/**
	 * {@inheritDoc}
	 */
	public double distanceBetween(PhylogenyNode<T> a, PhylogenyNode<T> b) throws NoSuchNodeException
		{
		// PERF might be a better way to do this than copy + remove nodes?
		List<PhylogenyNode<T>> ancestorsA = new ArrayList<PhylogenyNode<T>>(a.getAncestorPath());
		List<PhylogenyNode<T>> ancestorsB = new ArrayList<PhylogenyNode<T>>(b.getAncestorPath());

		int commonAncestors = 0;

		while (!ancestorsA.isEmpty() && !ancestorsB.isEmpty() && ancestorsA.get(0).equals(ancestorsB.get(0)))
			{
			ancestorsA.remove(0);
			ancestorsB.remove(0);
			commonAncestors++;
			}

		if (commonAncestors == 0)
			{
			throw new NoSuchNodeException("Can't compute distance between nodes with no common ancestor");
			}

		double dist = 0;
		for (PhylogenyNode<T> n : ancestorsA)
			{
			dist += n.getLength();
			}
		for (PhylogenyNode<T> n : ancestorsB)
			{
			dist += n.getLength();
			}

		return dist;
		}

	/**
	 * {@inheritDoc}
	 */
	public double getTotalBranchLength()
		{
		double result = 0;
		for (PhylogenyNode<T> node : getUniqueIdToNodeMap().values())
			{
			if (node.getLength() != null)// count null length as zero
				{
				result += node.getLength();
				}
			}
		return result;
		}

	/**
	 * {@inheritDoc}
	 */
	public void setAllBranchLengthsTo(Double d)
		{
		for (PhylogenyNode<T> node : getUniqueIdToNodeMap().values())
			{
			node.setLength(d);
			}
		}

	/**
	 * {@inheritDoc}
	 */
	public void setLeafWeightsRandom(
			ContinuousDistribution1D speciesAbundanceDistribution) //throws TreeException//throws DistributionException
		{
		for (PhylogenyNode<T> leaf : getLeaves())
			{
			leaf.setWeight(speciesAbundanceDistribution.sample());
			}

		try
			{
			normalizeWeights();
			}
		catch (TreeException e)
			{
			logger.error("Error", e);
			throw new Error("Impossible");
			}
		}

	/**
	 * {@inheritDoc}
	 */
	public void setLeafWeightsUniform() // throws TreeException//throws DistributionException
		{
		for (PhylogenyNode<T> leaf : getLeaves())
			{
			leaf.setWeight(1.);
			}

		try
			{
			normalizeWeights();
			}
		catch (TreeException e)
			{
			logger.error("Error", e);
			throw new Error("Impossible");
			}
		}

	public Map<T, Double> distributeInternalWeightsToLeaves(Map<T, Double> taxIdToWeightMap) throws NoSuchNodeException
		{
		MutableWeightedSet<T> result = new ConcurrentHashWeightedSet<T>();
		for (Map.Entry<T, Double> entry : taxIdToWeightMap.entrySet())
			{
			T id = entry.getKey();
			Double weight = entry.getValue();
			try
				{
				PhylogenyNode<T> n = getNode(id);
				distributeWeight(n, weight, result);
				}
			catch (NoSuchNodeException e)
				{
				// this can only happen if we already issued a warning about "node not found"
				logger.warn("Requested member weight dropped: " + id + " " + weight);
				}
			}
		return result.getItemNormalizedMap();
		}

	private void distributeWeight(PhylogenyNode<T> n, Double weight, MutableWeightedSet<T> result)
			throws NoSuchNodeException
		{
		if (n.isLeaf())
			{
			result.add(n.getPayload(), weight, 1);
			//result.incrementItems();
			}
		else
			{
			List<? extends PhylogenyNode<T>> children = n.getChildren();
			double childWeight = weight / children.size();
			for (PhylogenyNode<T> child : children)
				{
				distributeWeight(child, childWeight, result);
				}
			}
		}

	/**
	 * {@inheritDoc}
	 */
	public void setLeafWeights(Multiset<T> leafWeights) throws TreeException
		{
		for (PhylogenyNode<T> leaf : getLeaves())
			{
			int value = leafWeights.count(leaf.getPayload());
			leaf.setWeight(new Double(value));
			}

		normalizeWeights();
		}

	/**
	 * {@inheritDoc}
	 */
	public void setLeafWeights(Map<T, Double> leafWeights) throws TreeException
		{
		for (PhylogenyNode<T> leaf : getLeaves())
			{
			Double value = leafWeights.get(leaf.getPayload());
			if (value == null)
				{
				throw new TreeException("No leaf weight provided for " + leaf);
				}
			leaf.setWeight(value);
			}

		normalizeWeights();
		}

	public Map<T, Double> getLeafWeights() //throws TreeException
		{
		Map<T, Double> result = new HashMap<T, Double>();
		for (PhylogenyNode<T> leaf : getLeaves())
			{
			result.put(leaf.getPayload(), leaf.getWeight());
			}
		return result;
		}

	public Map<T, Double> getNodeWeights() //throws TreeException
		{
		Map<T, Double> result = new HashMap<T, Double>();
		for (PhylogenyNode<T> node : this)
			{
			result.put(node.getPayload(), node.getWeight());
			}
		return result;
		}

	/**
	 * {@inheritDoc}
	 */
	public void normalizeWeights() throws TreeException
		{
		// first normalize at the leaves
		double total = 0;

		for (PhylogenyNode<T> leaf : getLeaves())
			{
			Double w = leaf.getWeight();
			if (w == null)
				{
				throw new TreeException("Can't normalize when a leaf weight is null");
				}
			total += w;
			}

		for (PhylogenyNode<T> leaf : getLeaves())
			{
			leaf.setWeight(leaf.getWeight() / total);
			}

		// then propagate up

		//propagateWeightFromBelow();
		}


	/**
	 * {@inheritDoc}
	 */
	public RootedPhylogeny<T> getBasePhylogeny()
		{
		return basePhylogeny;
		}

	/**
	 * {@inheritDoc}
	 */
	public RootedPhylogeny<T> getBasePhylogenyRecursive()
		{
		if (basePhylogeny == null)
			{
			return this;
			}
		return basePhylogeny.getBasePhylogenyRecursive();
		}

	public void setBasePhylogeny(RootedPhylogeny<T> basePhylogeny)
		{
		this.basePhylogeny = basePhylogeny;
		}

	/**
	 * {@inheritDoc}
	 */
	public BasicRootedPhylogeny<T> extractIntersectionTree(Collection<T> leafIdsA, Collection<T> leafIdsB,
	                                                       NodeNamer<T> namer) throws NoSuchNodeException, TreeException
		{
		Set<PhylogenyNode<T>> allTreeNodesA = new HashSet<PhylogenyNode<T>>();
		for (T id : leafIdsA)
			{
			allTreeNodesA.addAll(getNode(id).getAncestorPath());
			}

		Set<PhylogenyNode<T>> allTreeNodesB = new HashSet<PhylogenyNode<T>>();
		for (T id : leafIdsB)
			{
			allTreeNodesB.addAll(getNode(id).getAncestorPath());
			}

		allTreeNodesA.retainAll(allTreeNodesB);

		// now allTreeNodesA contains all nodes that are in common between the two input leaf sets, including internal nodes

		// remove internal nodes
		for (PhylogenyNode<T> node : new HashSet<PhylogenyNode<T>>(allTreeNodesA))
			{
			allTreeNodesA.remove(node.getParent());
			}

		return extractTreeWithLeaves(allTreeNodesA, false, MutualExclusionResolutionMode.EXCEPTION);
		}

	/**
	 * {@inheritDoc}
	 */
	public BasicRootedPhylogeny<T> mixWith(RootedPhylogeny<T> otherTree, double mixingProportion) throws TreeException
		//NoSuchNodeException
		{
		if (mixingProportion < 0 || mixingProportion > 1)
			{
			throw new TreeException("Mixing proportion must be between 0 and 1");
			}

		//RootedPhylogeny<T> theBasePhylogeny = getBasePhylogeny();
		if (basePhylogeny == null || basePhylogeny != otherTree.getBasePhylogeny())
			{
			throw new TreeException(
					"Phylogeny mixtures can be computed only between trees extracted from the same underlying tree");
			}

		try
			{
			Set<T> unionLeaves = new HashSet<T>();
			unionLeaves.addAll(getLeafValues());
			unionLeaves.addAll(otherTree.getLeafValues());


			BasicRootedPhylogeny<T> unionTree = basePhylogeny
					.extractTreeWithLeafIDs(unionLeaves, false, false, MutualExclusionResolutionMode.EXCEPTION);
			for (PhylogenyNode<T> node : getLeaves())
				{
				unionTree.getNode(node.getPayload()).setWeight(node.getWeight() * mixingProportion);
				}

			for (PhylogenyNode<T> node : otherTree.getLeaves())
				{
				unionTree.getNode(node.getPayload()).incrementWeightBy(node.getWeight() * (1. - mixingProportion));
				}
			unionTree.normalizeWeights();
			return unionTree;
			}
		catch (NoSuchNodeException e)
			{
			logger.error("Error", e);
			throw new TreeRuntimeException(e);
			}
		}


	/**
	 * {@inheritDoc}
	 */
	public void smoothWeightsFrom(RootedPhylogeny<T> otherTree, double smoothingFactor)
			throws TreeException //throws TreeException
		{
		/*RootedPhylogeny<T> theBasePhylogeny = getBasePhylogeny();
				 if (theBasePhylogeny != otherTree.getBasePhylogeny())
					 {
					 throw new TreeException(
							 "Phylogeny mixtures can be computed only between trees extracted from the same underlying tree");
					 }
		 */

		//** if the otherTree has leaves that are not present in this tree, we'll ignore them and never know.
		// That circumstance should probably throw an exception, but it's a bit of a drag to test for it.

		try
			{
			for (PhylogenyNode<T> leaf : getLeaves())//theBasePhylogeny.getLeaves())
				{
				T leafId = leaf.getPayload();
				PhylogenyNode<T> otherLeaf = null;
				final PhylogenyNode<T> node = getNode(leafId);
				try
					{
					otherLeaf = otherTree.getNode(leafId);
					node.setWeight(otherLeaf.getWeight() + smoothingFactor);
					}
				catch (NoSuchNodeException e)
					{
					node.setWeight(smoothingFactor);
					}
				}
			}
		catch (NoSuchNodeException e)
			{
			logger.error("Error", e);
			throw new TreeRuntimeException(e);
			}

		normalizeWeights();
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract RootedPhylogeny<T> clone();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()

		{
		StringBuffer sb = new StringBuffer("\n");

		appendSubtree(sb, "");
		return sb.toString();
		}
/*
	public void saveState()
		{
		}
*/

	@NotNull
	public T getShallowestLeaf()
		{
		try
			{
			T shallowestId = null;
			double shallowestDepth = Double.POSITIVE_INFINITY;

			for (PhylogenyNode<T> n : getLeaves())
				{
				//PhylogenyNode<Integer> n = theIntegerTree.getNode(id);
				double depth = distanceBetween(getRoot(), n);
				T nId = n.getPayload();

				// BAD if two depths are exactly equal, then the result is nondeterministic

				// try to impose a deterministic order using the id hashcodes
				if (depth < shallowestDepth || (depth == shallowestDepth && nId.hashCode() < shallowestId.hashCode()))
					{
					shallowestDepth = depth;
					shallowestId = nId;
					}
				}
			return shallowestId;
			}
		catch (NoSuchNodeException e)
			{
			throw new Error("Impossible");
			}
		}

	public PhylogenyNode<T> getFirstBranchingNode()
		{
		PhylogenyNode<T> r = getRoot();
		while (r.getChildren().size() == 1)
			{
			r = r.getChildren().iterator().next();
			}
		return r;
		}

	public PhylogenyNode<T> getRandomLeafBelow()
		{
		return getRoot().getRandomLeafBelow();
		}


	//private static final int MAX_SEARCH_ITERATIONS = 1000;

	public T getLeafAtApproximateDistance(final T aId, final double minDesiredTreeDistance,
	                                      final double maxDesiredTreeDistance) throws NoSuchNodeException
		{
		// we want to select a bunch of random nodes and then pick the one closest to the desired distance
		// but doing this over the whole tree is inefficient; we can constrain the search to the subtree that can possibly be within that distance

		double distanceToSubtreeRoot = 0;
		final PhylogenyNode<T> queryNode = getNode(aId);
		PhylogenyNode<T> p = queryNode;
		PhylogenyNode<T> root = getRoot();
		Map<PhylogenyNode<T>, Double> candidateRoots = new ConcurrentSkipListMap<PhylogenyNode<T>, Double>();

		while (distanceToSubtreeRoot <= maxDesiredTreeDistance && p != root)
			{
			candidateRoots.put(p, distanceToSubtreeRoot);
			distanceToSubtreeRoot += p.getLength();
			p = p.getParent();
			}
		candidateRoots.remove(queryNode);

		// now p is the root of the subtree that can possibly contain the node we want, and candidateRoots contains all the nodes along the ancestor path to it

		Collection<PhylogenyNode<T>> candidates = new ConcurrentSkipListSet<PhylogenyNode<T>>();

		// PERF Parallel.forEach
		for (Map.Entry<PhylogenyNode<T>, Double> entry : candidateRoots.entrySet())
			{
			PhylogenyNode<T> candidateRoot = entry.getKey();
			double candidateRootHeight = entry.getValue();
			candidateRoot.collectLeavesBelowAtApproximateDistance(minDesiredTreeDistance - candidateRootHeight,
			                                                      maxDesiredTreeDistance - candidateRootHeight,
			                                                      candidates);
			}

		// now all of the candidates meet the criteria.

		if (candidates.isEmpty())
			{
			throw new NoSuchNodeException();
			}

		// just pick one
		// ** could try to pick the closest to the request, i.e. the middle of the range?
		return DSCollectionUtils.chooseRandom(candidates).getPayload();

//		for (int i = 0; i < MAX_SEARCH_ITERATIONS; i++)
//			{
//			//** Note getRandomLeafBelow is weighted by tree structure (uniform at each node on the path, not uniform over leaves)
//			PhylogenyNode<T> candidate = p.getRandomLeafBelow();
//			double candidateDistance = distanceBetween(queryNode, candidate);
//			if (candidateDistance >= minDesiredTreeDistance && candidateDistance <= maxDesiredTreeDistance)
//				{
//				return candidate.getPayload();
//				}
//			}
//
//		throw new NoSuchNodeException(
//				"Could not find a node in the requested distance range (" + minDesiredTreeDistance + " - "
//				+ maxDesiredTreeDistance + " from " + aId + ") after " + MAX_SEARCH_ITERATIONS + " attempts");
		}


	public int countDescendantsIncludingThis()
		{
		int result = 1;
		for (PhylogenyNode<T> c : getChildren())
			{
			result += c.countDescendantsIncludingThis();
			}
		return result;
		}
	}
