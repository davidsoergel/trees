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

import com.davidsoergel.dsutils.collections.DSCollectionUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * A basic implementation of the PhylogenyNode interface, where the generic type indicates the type of node IDs that are
 * used (typically Strings or Integers).
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: BasicPhylogenyNode.java 353 2009-10-19 17:49:33Z soergel $
 */

public class BasicPhylogenyNode<T extends Serializable>
		implements SerializablePhylogenyNode<T>//, HierarchyNode<T, BasicPhylogenyNode<T>>
		// really T should extend Serializable here but let's see if we can get away without it
	{

	private static final long serialVersionUID = 20090720L;

	private static final Logger logger = Logger.getLogger(BasicPhylogenyNode.class);
	// ------------------------------ FIELDS ------------------------------

	protected transient BasicPhylogenyNode<T> parent;

	// maintain a known order.  This was previously a Set; now we have to be careful not to add the same node twice, etc.
	protected List<BasicPhylogenyNode<T>> children = new ArrayList<BasicPhylogenyNode<T>>();

	// BAD this should be final, but that requires substantial refactoring
	protected T value;// = null;
	protected Double length;// = null;// distinguish null from zero
	protected Double weight;// = null;// distinguish null from zero
	protected Double bootstrap;// = null;

	private String name;

	public String getName()
		{
		return name;
		}

	public void setName(final String name)
		{
		this.name = name;
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

/*	@Override
	public boolean equals(final Object o)
		{
		if (this == o)
			{
			return true;
			}
		if (o == null || getClass() != o.getClass())
			{
			return false;
			}

		final BasicPhylogenyNode that = (BasicPhylogenyNode) o;

		if (value == null && that.value == null)
			{
			// BAD hack
			return this == that;
			}

		if (value != null ? !value.equals(that.value) : that.value != null)
			{
			return false;
			}

		if (bootstrap != null ? !bootstrap.equals(that.bootstrap) : that.bootstrap != null)
			{
			throw new TreeRuntimeException(
					"Two BasicPhylogenyNodes have the same id, but different bootstrap values; that shouldn't happen");
			//	return false;
			}
		if (length != null ? !length.equals(that.length) : that.length != null)
			{
			throw new TreeRuntimeException(
					"Two BasicPhylogenyNodes have the same id, but different lengths; that shouldn't happen");

			//return false;
			}
		if (parent == this && that.parent != that)
			{
			throw new TreeRuntimeException(
					"Two BasicPhylogenyNodes have the same id, but different parents; that shouldn't happen");

			//	return false;
			}
		if (parent != null ? !parent.equals(that.parent) : that.parent != null)
			{
			throw new TreeRuntimeException(
					"Two BasicPhylogenyNodes have the same id, but different parents; that shouldn't happen");

			//return false;
			}

		if (weight != null ? !weight.equals(that.weight) : that.weight != null)
			{
			throw new TreeRuntimeException(
					"Two BasicPhylogenyNodes have the same id, but different weights; that shouldn't happen");

			//return false;
			}

		return true;
		}

	@Override
	public int hashCode()
		{
		return value != null ? value.hashCode() : 0;
		}
*/

	/*	@Override
   public int hashCode()
	   {
	   int result = parent != null ? parent.hashCode() : 0;
	   result = 31 * result + (value != null ? value.hashCode() : 0);
	   result = 31 * result + (length != null ? length.hashCode() : 0);
	   result = 31 * result + (weight != null ? weight.hashCode() : 0);
	   result = 31 * result + (bootstrap != null ? bootstrap.hashCode() : 0);
	   return result;
	   }*/

	public void toNewick(StringBuffer sb, String prefix, String tab, int minClusterSize, double minLabelProb)
		{
		// (children)name:length

		if (prefix != null)
			{
			sb.append(prefix);
			}

		if (!children.isEmpty())
			{
			String childPrefix = prefix == null ? null : prefix + tab;
			sb.append("(");
			Iterator<BasicPhylogenyNode<T>> i = children.iterator();
			while (i.hasNext())
				{
				i.next().toNewick(sb, childPrefix, tab, minClusterSize, minLabelProb);
				//sb.append(i.next());
				if (i.hasNext())
					{
					sb.append(",");
					}
				}
			if (prefix != null)
				{
				sb.append(prefix);
				}
			sb.append(")");
			if (prefix != null)
				{
				sb.append(prefix);
				}
			}
		String n = value.toString();
		n = n.replaceAll(" ", "_");
		sb.append(n);
		if (length != null) // && length != 0)
			{
			sb.append(String.format(":%.5f", length));
			}
		if (bootstrap != null) // && bootstrap != 0)
			{
			sb.append("[").append(bootstrap).append("]");
			}
		}

	// --------------------------- CONSTRUCTORS ---------------------------

	public BasicPhylogenyNode()
		{
		}

	public BasicPhylogenyNode(T value)
		{
		this();
		setPayload(value);
		}

	public BasicPhylogenyNode(BasicPhylogenyNode<T> parent)
		{
		this();
		setParent(parent);
		}

	public BasicPhylogenyNode(BasicPhylogenyNode<T> parent, double length)
		{
		this(parent);
		this.length = length;
		}

	public BasicPhylogenyNode(BasicPhylogenyNode<T> parent, T value, double length)
		{
		this(parent);
		this.value = value;
		this.length = length;
		}

	public BasicPhylogenyNode(BasicPhylogenyNode<T> parent, T value)
		{
		this(parent);
		this.value = value;
		}

	public BasicPhylogenyNode(BasicPhylogenyNode<T> parent, T value, Double length, Double weight) //, double bootstrap)
		{
		this(parent);
		this.value = value;
		this.length = length;
		this.weight = weight;
		//this.bootstrap = bootstrap;
		}

	public BasicPhylogenyNode(final BasicPhylogenyNode<T> parent, final PhylogenyNode<T> copyFrom)
		{
		this(parent, copyFrom.getPayload(), copyFrom.getLength(), copyFrom.getWeight());
		}

	/*
	 public BasicPhylogenyNode(BasicPhylogenyNode<T> parent, PhylogenyNode<T> child)
		 {
		 this(parent, child.getValue(), child.getLength());
		 }
 */
	// --------------------- GETTER / SETTER METHODS ---------------------

	/**
	 * {@inheritDoc}
	 */
	public List<BasicPhylogenyNode<T>> getChildren()
		{
		return children;
		}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public PhylogenyNode<T> getChildWithPayload(T id) throws NoSuchNodeException
		{
		// We could map the children collection as a Map; but that's some hassle, and since there are generally just 2 children anyway, this is simpler

		// also, the child id is often not known when it is added to the children Set, so putting the child into a children Map wouldn't work

		for (PhylogenyNode<T> child : children)
			{
			if (child.getPayload() == id)
				{
				return child;
				}
			}
		throw new NoSuchNodeException("Could not find child: " + id);
		//throw new NoSuchElementException();
		}

	/**
	 * Note this samples from the distribution of leaves weighted by the tree structure, i.e. uniformly _per level_, not
	 * uniformly from the set of leaves.  Basically, leaves with fewer siblings and cousins are more likely to be chosen.
	 *
	 * @return
	 */
	public PhylogenyNode<T> getRandomLeafBelow()
		{
		// iterate, don't recurse, in case the tree is deep
		PhylogenyNode<T> trav = this;
		List<? extends PhylogenyNode<T>> travChildren = trav.getChildren();

		while (!travChildren.isEmpty())
			{
			trav = DSCollectionUtils.chooseRandom(travChildren);
			travChildren = trav.getChildren();
			}

		return trav;
		}

	/**
	 * {@inheritDoc}
	 */
	public boolean isLeaf()
		{
		return children.size() == 0;
		}

	/*	public void appendToName(int i)
	   {
	   name = name + i;
	   }*/

	/**
	 * {@inheritDoc}
	 */
	public Double getLength()
		{
		return length;
		}

	/**
	 * {@inheritDoc}
	 */
	public void setLength(Double length)
		{
		this.length = length;
		if (parent != null)
			{
			parent.invalidateAggregatedChildInfo();
			}
		}

	/**
	 * {@inheritDoc}
	 */
	public void setWeight(Double weight)
		{
		this.weight = weight;
		if (parent != null)
			{
			parent.invalidateAggregatedChildInfo();
			}
		}

	/**
	 * {@inheritDoc}
	 */
	@Nullable
	public Double getWeight()// throws TreeException
		{
		if (weight == null)
			{
			if (isLeaf())
				{
				return null;
				//throw new TreeException("Node has undefined weight: " + getValue());
				}
			propagateWeightFromBelow();
			}
		return weight;
		}

	/**
	 * {@inheritDoc}
	 */
	public Double getCurrentWeight()
		{
		return weight;
		}

	/**
	 * {@inheritDoc}
	 */
	public void incrementWeightBy(double v)
		{
		weight = weight == null ? v : weight + v;
		}

	/**
	 * {@inheritDoc}
	 */
	private void propagateWeightFromBelow() //throws TreeException
		{
		if (!isLeaf())
			{
			weight = 0.;
			for (PhylogenyNode<T> child : children)
				{
				//child.propagateWeightFromBelow();
				Double w = child.getWeight();
				if (w == null)
					{
					throw new TreeRuntimeException("Can't propagate undefined weight");
					// undefined
					//		weight = null;
					//		return;
					}
				incrementWeightBy(w);
				}
			}
		}

	/**
	 * {@inheritDoc}
	 */
	public double distanceToRoot()
		{
		return (length == null ? 0 : length) + (parent == null ? 0 : parent.distanceToRoot());
		}

	/**
	 * {@inheritDoc}
	 */
	public T getPayload()
		{
		return value;
		}

	/**
	 * {@inheritDoc}
	 */
	public void setPayload(T value)
		{
		if (value != null && value.equals(new Integer(-1)))
			{
			logger.error("wtf");
			}
		this.value = value;
		}

	/**
	 * {@inheritDoc}
	 */
	public BasicPhylogenyNode<T> getParent()
		{
		return parent;
		}

	public BasicPhylogenyNode<T> findRoot()
		{
		if (parent == null)
			{
			return this;
			}
		else
			{
			return parent.findRoot();
			}
		}

	public BasicRootedPhylogeny<T> asRootedPhylogeny()
		{
		return new BasicRootedPhylogeny<T>(this);
		}

	/**
	 * {@inheritDoc}
	 */
	/*	public BasicPhylogenyNode<T> newChild()
		 {
		 BasicPhylogenyNode<T> child = new BasicPhylogenyNode<T>();
		 child.setParent(this);
		 //addChild(child);
		 return child;
		 }
 */

	/**
	 * {@inheritDoc}
	 */
	public PhylogenyNode<T> newChild(T payload)
		{
		BasicPhylogenyNode<T> child = new BasicPhylogenyNode<T>();
		child.setPayload(payload);
		child.setParent(this);
		return child;
		}


	/**
	 * {@inheritDoc}
	 */

	public final void setParent(PhylogenyNode<T> parent)//BasicPhylogenyNode parent)
		{
		if (this.parent != null)
			{
			this.parent.unregisterChild(this);
			}
		this.parent = (BasicPhylogenyNode<T>) parent;// may produce ClassCastException
		if (this.parent != null)
			{
			this.parent.registerChild(this);
			}
		}

	public void setBootstrap(double bootstrap)
		{
		this.bootstrap = bootstrap;
		}

	// -------------------------- OTHER METHODS --------------------------

	public int addSubtreeToMap(Map<T, BasicPhylogenyNode<T>> nodes, @NotNull NodeNamer<T> namer,
	                           int stackDepth)// throws TreeException
		{
		int addedInternalNodes = 0;
		if (value != null)
			{
			// check if the node is uniquely named
			if (nodes.get(value) != null)
				{
				// if a node name is nonunique, then after the first time it's
				// seen the name is thrown out and replaced by a name generated by the namer

				final T uniqueValue = namer.uniqueify(value);
				logger.warn("Name " + value + " not unique, substituting " + uniqueValue);
				value = uniqueValue;
				}

			// if there is already a value, then store it, whether or not the namer finds it acceptable
			nodes.put(value, this);
			}

		assert children != null;

		// if a node has no name, or the namer doesn't like it, we assign one
		if (!namer.isAcceptable(value))
			{
			assignGeneratedName(nodes, namer);
			}
		else if (!children.isEmpty() && namer.requireGeneratedNamesForInternalNodes())  //children != null &&
			{
			addedInternalNodes++;

			logger.info("Adding phantom leaf for " + value);

			// if a node has a name but we insist on generating all internal node names, then create a new zero-length child to deal with that
			BasicPhylogenyNode<T> child = new BasicPhylogenyNode<T>();
			child.setPayload(value);
			child.setLength(0.0);
			child.setWeight(weight);

			// set the parent last to avoid a long invalidateAggregatedChildInfo issue
			//child.setParent(this);  // this should invalidate the parent weight?

			// no, set the parent manually to avoid invalidateAggregatedChildInfo
			child.parent = this;
			children.add(child);


			// the name no longer refers to this node

			nodes.remove(value);
			assert nodes.get(value) == null;
			setPayload(null);

			// the child recursion will add the child name to the map

			//	nodes.put(child.value, child);  // ** not sure if the depth-first iterator will catch this

			// now rename the current node
			assignGeneratedName(nodes, namer);

			// the bootstrap should stay attached to the current node anyway

			// the weight, if any, may now be inconsistent but should later be repropagated from the nodes
			//setWeight(0.0);
			}

		// this recursion produces stack depth problems


		// do the children first so that we don't create a uniqueness problem if we push down the name below //?
		for (BasicPhylogenyNode<T> n : children)
			{
			//** temp test
			//assert nodes.get(n.getValue()) == null;
			//int stackdepth = Thread.currentThread().getStackTrace().length;
			if (stackDepth > 2000)
				{
				logger.warn("Stack depth = " + stackDepth + " at node " + value);
				}

			addedInternalNodes += n.addSubtreeToMap(nodes, namer, stackDepth + 1);
			}
		return addedInternalNodes;
		}

	private void assignGeneratedName(Map<T, BasicPhylogenyNode<T>> nodes, NodeNamer<T> namer)
		{
		T newValue = namer.generate();

		if (nodes.get(newValue) != null)
			{
			throw new TreeRuntimeException("Generated ID collided with a preexisting ID");
			}

		// store that too
		nodes.put(newValue, this);

		// ** nope, just forget the old name
		//	value = namer.makeAggregate(newValue, value);
		value = newValue;
		}

	/**
	 * {@inheritDoc}
	 */
/*	public boolean hasValue()
		{
		return value != null;// && !name.equals("");
		}*/

	/*	public void setName(String name)
	   {
	   this.name = name;
	   }*/
	public void appendToValue(String s, NodeNamer<T> namer) throws TreeException
		{
		if (value == null)
			{
			value = namer.create(s);
			}
		else
			{
			value = namer.merge(value, s);
			}
		}

	public void appendToValue(Integer s, NodeNamer<T> namer) throws TreeException
		{
		if (value == null)
			{
			value = namer.create(s);
			}
		else
			{
			value = namer.merge(value, s);
			}
		}

	// PERF caching this here and in the CacheManager accumulatingMap is redundant
	private List<PhylogenyNode<T>> ancestorPath = null;

	public List<? extends PhylogenyNode<T>> getAncestorPath()
		{
		return getAncestorPath(true);
		}

	/**
	 * {@inheritDoc}
	 */
	public List<PhylogenyNode<T>> getAncestorPath(boolean includeSelf)
		{
		if (ancestorPath == null)
			{
			List<PhylogenyNode<T>> result = new LinkedList<PhylogenyNode<T>>();
			BasicPhylogenyNode<T> trav = includeSelf ? this : getParent();

			while (trav != null)
				{
				result.add(0, trav);
				trav = trav.getParent();
				}

			ancestorPath = Collections.unmodifiableList(result);
			}
		return ancestorPath;
		}


	// PERF caching this here and in the CacheManager accumulatingMap is redundant
	private List<T> ancestorPathIds = null;

	/**
	 * {@inheritDoc}
	 */
	public List<T> getAncestorPathPayloads()
		{
		if (ancestorPathIds == null)
			{
			List<T> result = new LinkedList<T>();
			BasicPhylogenyNode<T> trav = this;

			while (trav != null)
				{
				result.add(0, trav.getPayload());
				trav = trav.getParent();
				}

			ancestorPathIds = Collections.unmodifiableList(result);
			}
		return ancestorPathIds;
		}


	/**
	 * {@inheritDoc}
	 */
	public Iterator<PhylogenyNode<T>> iterator()
		{
		return new DepthFirstTreeIteratorImpl(this);
		}

	/**
	 * {@inheritDoc}
	 */
	public DepthFirstTreeIterator<T, PhylogenyNode<T>> depthFirstIterator()
		{
		return new DepthFirstTreeIteratorImpl<T, PhylogenyNode<T>>(this);
		}


	Double greatestBranchLengthDepth = null;
	Double leastBranchLengthDepth = null;
	Integer greatestNodeDepth = null;
	Double secondGreatestDepth = null;
	Double largestLengthSpan = null;


	/**
	 * {@inheritDoc}
	 */
	public double getLargestLengthSpan()
		{
		computeDepthsIfNeeded();
		return largestLengthSpan;
		}

	/**
	 * {@inheritDoc}
	 */
	public double getGreatestBranchLengthDepthBelow()
		{
		computeDepthsIfNeeded();
		return greatestBranchLengthDepth;
		}

	/**
	 * {@inheritDoc}
	 */
	public double getLeastBranchLengthDepthBelow()
		{
		computeDepthsIfNeeded();
		return leastBranchLengthDepth;
		}


	/**
	 * {@inheritDoc}
	 */
	public int getGreatestNodeDepthBelow()
		{
		computeDepthsIfNeeded();
		return greatestNodeDepth;
		}

	private void computeDepthsIfNeeded()
		{
		if (greatestBranchLengthDepth == null)
			{
			greatestBranchLengthDepth = 0.;
			leastBranchLengthDepth = Double.MAX_VALUE;
			secondGreatestDepth = 0.;
			largestLengthSpan = 0.;
			greatestNodeDepth = 0;

			// if there are no children, then both depths and the span are just 0

			// note we want the greatest and second-greatest depths to be on different branches from this node!
			// if they were on the same branch, then we'd double-count the common portion in computing the span.


			for (BasicPhylogenyNode<T> child : children)
				{
				int nodesBelow = child.getGreatestNodeDepthBelow() + 1;
				if (nodesBelow > greatestNodeDepth)
					{
					greatestNodeDepth = nodesBelow;
					}

				if (child.length != null)
					{
					child.computeDepthsIfNeeded();

					// least depth is easy
					if (child.length + child.leastBranchLengthDepth < leastBranchLengthDepth)
						{
						leastBranchLengthDepth = child.length + child.leastBranchLengthDepth;
						}

					// case 1: the child replaces the greatest depth

					if (child.length + child.greatestBranchLengthDepth > greatestBranchLengthDepth)
						{
						secondGreatestDepth = greatestBranchLengthDepth;// must be from a different child, or 0
						greatestBranchLengthDepth = child.length + child.greatestBranchLengthDepth;
						}

					// case 2: the child replaces the second-greatest depth

					else if (child.length + child.greatestBranchLengthDepth > secondGreatestDepth)
						{
						secondGreatestDepth = child.length + child.greatestBranchLengthDepth;
						}

					// the child's second-greatest depth should never figure in to the second-greatest depth at this level,
					// because of the different-branches constraint!
					// however, it may contribute to the maximum span.

					// assume by default that the maximum span spans branches
					// need to take the max in case it's already been overridden by the spanViaChild on a previous branch
					largestLengthSpan = Math.max(largestLengthSpan, greatestBranchLengthDepth + secondGreatestDepth);

					// then check if this child overrides it, counting the common portion only once

					double spanViaChild = child.length + child.largestLengthSpan;

					largestLengthSpan = Math.max(largestLengthSpan, spanViaChild);
					}
				}
			if (isLeaf())
				{
				leastBranchLengthDepth = 0.;
				}
			}

		// if there is exactly one child, then
		// greatestDepth = child.length + child.greatestDepth;
		// secondGreatestDepth = 0;
		// largestLengthSpan = child.length + child.greatestDepth;
		}

	public void removeChild(BasicPhylogenyNode<T> child)
		{
		children.remove(child);
		child.setParent(null);
		invalidateAggregatedChildInfo();
		}


	/**
	 * {@inheritDoc}
	 */
	public void registerChild(PhylogenyNode<T> child)
		{
		if (!children.contains(child))
			{
			children.add((BasicPhylogenyNode<T>) child);
			//((BasicPhylogenyNode<T>) child).setParent(this);
			invalidateAggregatedChildInfo();
			}
		}

	/**
	 * {@inheritDoc}
	 */
	public void unregisterChild(PhylogenyNode<T> child)
		{
		children.remove((BasicPhylogenyNode<T>) child);
		//((BasicPhylogenyNode<T>) child).setParent(this);
		invalidateAggregatedChildInfo();
		}

	private void invalidateAggregatedChildInfo()
		{
		assert !children.isEmpty();

		if (greatestBranchLengthDepth != null || secondGreatestDepth != null || largestLengthSpan != null
		    || weight != null)
			{
			greatestBranchLengthDepth = null;
			secondGreatestDepth = null;
			largestLengthSpan = null;
			weight = null;

			if (parent != null)
				{
				parent.invalidateAggregatedChildInfo();
				}
			}
		// else all the ancestors must be invalidated already
		}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
		{
		return value == null ? "null" : value.toString();
		}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public BasicPhylogenyNode<T> clone()
		{
		BasicPhylogenyNode<T> result = null;// new BasicPhylogenyNode<T>();
		try
			{
			result = (BasicPhylogenyNode<T>) super.clone();


			parent = null;
			children = new ArrayList<BasicPhylogenyNode<T>>();
			// note the value is shallow-copied, not cloned

			//result.setValue(value);//** value.clone() ??

			for (BasicPhylogenyNode<T> child : children)
				{
				child.clone().setParent(result);
				}

			// reset weight after children, since it got wiped
			result.setWeight(weight);

			// we don't set the parent here; addChild takes care of that, except for the root, where the parent is null anyway

			return result;
			}
		catch (CloneNotSupportedException e)
			{
			logger.error("Error", e);
			throw new Error("cloneability required");
			}
		}

	/**
	 * {@inheritDoc}
	 */
	public BasicPhylogenyNode<T> getSelfNode()
		{
		return this;
		}


	public void appendSubtree(StringBuffer sb, String indent)
		{


		sb.append(indent + "\n");
		sb.append(indent + "---" + weight + " " + length + "     " + value + "\n");
		indent += "   |";
		for (PhylogenyNode n : getChildren())
			{
			n.appendSubtree(sb, indent);
			}
		}


	/**
	 * {@inheritDoc}
	 */
	/* recursive solution
	 public PhylogenyNode<T> nearestAncestorWithBranchLength() throws TreeException
		 {
		 if (getLength() != null)
			 {
			 return this;
			 }

		 PhylogenyNode<T> n = getParent();
		 if (n == null)
			 {
			 throw new Error("should be impossible?");
			 // arrived at root, too bad
			 throw new TreeException("No ancestor of " + leafId + " has a branch length.");
			 }
		 return n.nearestAncestorWithBranchLength();
		 }
 */

	/**
	 * {@inheritDoc}
	 */
// iterative solution
	public PhylogenyNode<T> nearestAncestorWithBranchLength() throws NoSuchNodeException
		{
		PhylogenyNode<T> n = this;

		while (n.getLength() == null) // || n.getLength() == 0.0)
			{
			n = n.getParent();
			if (n == null)
				{
				// arrived at root, too bad
				throw new NoSuchNodeException("No ancestor of " + getPayload() + " has a branch length.");
				}
			}

		return n; //.getValue();
		}
/*
	public void addSubtreeToMap(Map<T, PhylogenyNode<T>> uniqueIdToNodeMap, NodeNamer<T> namer)
		{
		DepthFirstTreeIterator<T, PhylogenyNode<T>> iterator = depthFirstIterator();

		while (iterator.hasNext())
			{
			PhylogenyNode<T> n = iterator.next();
			n.addToMap(uniqueIdToNodeMap, namer);
			}
		}*/

	public void collectLeavesBelowAtApproximateDistance(final double minDesiredTreeDistance,
	                                                    final double maxDesiredTreeDistance,
	                                                    Collection<PhylogenyNode<T>> result)
		{
		if (maxDesiredTreeDistance < getLeastBranchLengthDepthBelow()
		    || getGreatestBranchLengthDepthBelow() < minDesiredTreeDistance)
			{
			return;
			}
		if (isLeaf())
			{
			// the greatest and least distances were 0, so we already tested that this leaf is in the desired range
			result.add(this);
			}

		// PERF Parallel.forEach?
		for (PhylogenyNode<T> child : getChildren())
			{
			double l = child.getLength();
			child.collectLeavesBelowAtApproximateDistance(minDesiredTreeDistance - l, maxDesiredTreeDistance - l,
			                                              result);
			}
		}
	}


