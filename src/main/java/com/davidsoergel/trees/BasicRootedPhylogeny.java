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

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Really this wants to extend BasicPhylogenyNode, but we can't (multiple inheritance, etc.), so we just facade a root
 * node for many methods.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: BasicRootedPhylogeny.java 350 2009-10-10 07:55:49Z soergel $
 * @Author David Soergel
 * @Version 1.0
 */
public class BasicRootedPhylogeny<T extends Serializable> extends AbstractRootedPhylogeny<T>
		implements SerializableRootedPhylogeny<T>
	{
	private static final long serialVersionUID = 20090904L;

	private static final Logger logger = Logger.getLogger(BasicRootedPhylogeny.class);
	// ------------------------------ FIELDS ------------------------------

	transient private Map<T, BasicPhylogenyNode<T>> uniqueIdToNodeMap;
	BasicPhylogenyNode<T> root;


	// -------------------------- OTHER METHODS --------------------------

	public BasicRootedPhylogeny()
		{
		root = new BasicPhylogenyNode<T>();
		}

	public BasicRootedPhylogeny(T rootValue)
		{
		root = new BasicPhylogenyNode<T>(null, rootValue, 0);
		}


	public BasicRootedPhylogeny(BasicPhylogenyNode<T> root)
		{
		this.root = root;
		// ** the wrong place for this?
		assignUniqueIds(new RequireExistingNodeNamer<T>(true));
		}

	public void toNewick(StringBuffer sb, String prefix, String tab, int minClusterSize, double minLabelProb)
		{
		root.toNewick(sb, prefix, tab, minClusterSize, minLabelProb);
		sb.append(";\n");
		}

	@Override
	public String toString()
		{
		return "BasicRootedPhylogeny{" + root + '}';
		} /**
	 * Make a complete copy of the provided phylogeny using BasicPhylogenyNodes.  It's probably better to use clone() when
	 * possible, but this copy constructor allows translating from phylogenies with different node types.
	 *
	 * @param original
	 */
	/*	public BasicRootedPhylogeny(HybridRootedPhylogeny<T> original)
	   {
root = new BasicPhylogenyNode<T>(original.);
	   }*/

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public BasicPhylogenyNode<T> getNode(T name) throws NoSuchNodeException
		{
		BasicPhylogenyNode<T> result = uniqueIdToNodeMap.get(name);
		if (result == null)
			{
			throw new NoSuchNodeException("Node not found: " + name);
			}
		return result;
		}

	public double getWeight(T id) throws NoSuchNodeException
		{
		Double w;
		try
			{
			PhylogenyNode<T> aNode = getNode(id);
			w = aNode.getWeight();
			}
		catch (NoSuchNodeException e)
			{
			// no problem, aWeight = 0
			return 0.0;
			}
		if (w == null)
			{
			throw new NoSuchNodeException("The node " + id + " exists, but has unspecified weight");
			}
		return w;
		}

	/**
	 * {@inheritDoc}
	 */
	public Map<T, ? extends PhylogenyNode<T>> getUniqueIdToNodeMap()
		{
		return uniqueIdToNodeMap;//.values();
		}

	public void putUniqueIdToNode(T id, BasicPhylogenyNode<T> node)
		{
		uniqueIdToNodeMap.put(id, node);
		}

	//** make weak
	private Set<PhylogenyNode<T>> leafSet = null;
	//** make weak
	private Set<T> leafIds = null;


	/**
	 * {@inheritDoc}
	 */
	public Collection<PhylogenyNode<T>> getLeaves()
		{
		if (leafSet == null)
			{
			leafSet = new HashSet<PhylogenyNode<T>>();
			leafIds = new HashSet<T>();
			for (T t : uniqueIdToNodeMap.keySet())
				{
				PhylogenyNode<T> node = uniqueIdToNodeMap.get(t);
				if (node.isLeaf())
					{
					leafSet.add(node);
					leafIds.add(t);
					}
				}
			}
		return leafSet;
		}

	/**
	 * {@inheritDoc}
	 */
	public Set<T> getLeafValues()
		{
		if (leafIds == null)
			{
			getLeaves();
			}
		return leafIds;
		}

	public void addNode(BasicPhylogenyNode<T> n) throws TreeException
		{

		if (uniqueIdToNodeMap.get(n.getPayload()) != null)
			{
			throw new TreeException("Node id not unique");
			}
		uniqueIdToNodeMap.put(n.getPayload(), n);
		leafIds = null;
		leafSet = null;
		}

	/**
	 * {@inheritDoc}
	 */
	public Set<T> getNodeValues()
		{
		return uniqueIdToNodeMap.keySet();
		}


	/**
	 * Insure that every node has a unique ID.  We can't do this while building, since the names might change.  Also,
	 * establishes uniqueIdToNodeMap, which we need even if the nodes all already have names
	 *
	 * @param namer
	 * @throws TreeException
	 */
	public void assignUniqueIds(@NotNull NodeNamer<T> namer) //throws TreeException
		{
		uniqueIdToNodeMap = new HashMap<T, BasicPhylogenyNode<T>>();

		// this recursion produces stack depth problems on some systems; try -Xss8m or larger
		int addedInternalNodes = root.addSubtreeToMap(uniqueIdToNodeMap, namer, 1);

		if (logger.isDebugEnabled() && addedInternalNodes != 0)
			{
			logger.debug("Added " + addedInternalNodes + " internal nodes to satisfy namer requirement");
			}

		// but, so does this, with additional difficulties due to concurrent modification of the tree
		/*
		DepthFirstTreeIterator<T, PhylogenyNode<T>> iterator = depthFirstIterator();

		while (iterator.hasNext())
			{
			PhylogenyNode<T> n = iterator.next();
			n.addToMap(uniqueIdToNodeMap, namer);
			}*/
		}


	/**
	 * {@inheritDoc}
	 */
	public List<BasicPhylogenyNode<T>> getChildren()
		{
		return root.getChildren();
		}


	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public PhylogenyNode<T> getChildWithPayload(T id) throws NoSuchNodeException
		{
		return root.getChildWithPayload(id);
		}

	/**
	 * {@inheritDoc}
	 */
	public T getPayload()
		{
		return root.getPayload();
		}

	/**
	 * {@inheritDoc}
	 */
	@Nullable
	public PhylogenyNode getParent()
		{
		return null;
		}

	/**
	 * {@inheritDoc}
	 */
	/*	public PhylogenyNode<T> newChild()
		 {
		 return root.newChild();
		 }
 */
	/**
	 * {@inheritDoc}
	 */
	public PhylogenyNode<T> newChild(T payload)
		{
		return root.newChild(payload);
		}

	/**
	 * {@inheritDoc}
	 */
	public void setPayload(T contents)
		{
		root.setPayload(contents);
		}

	/**
	 * {@inheritDoc}
	 */
	public void setParent(PhylogenyNode<T> parent)
		{
		logger.error("Can't set the parent of the root node");
		}

	/**
	 * {@inheritDoc}
	 */
/*	public boolean hasValue()
		{
		return root.hasValue();
		}*/

	/**
	 * {@inheritDoc}
	 */
	public List<PhylogenyNode<T>> getAncestorPath(boolean includeSelf)
		{
		// this is the root node
		List<PhylogenyNode<T>> result = new LinkedList<PhylogenyNode<T>>();

		if (includeSelf)
			{
			result.add(0, root);
			}

		return Collections.unmodifiableList(result);
		}

	public List<PhylogenyNode<T>> getAncestorPath()
		{
		return getAncestorPath(true);
		}

	public BasicPhylogenyNode<T> getFirstBranchingNode()
		{
		BasicPhylogenyNode<T> r = getRoot();
		while (r.getChildren().size() == 1)
			{
			r = r.getChildren().iterator().next();
			}
		return r;
		}

	/**
	 * {@inheritDoc}
	 */
	public List<T> getAncestorPathPayloads()
		{
		// this is the root node
		List<T> result = new LinkedList<T>();

		result.add(0, getRoot().getPayload());

		return Collections.unmodifiableList(result);
		}

	/**
	 * {@inheritDoc}
	 */
	public Double getLength()
		{
		return 0.;
		}

	/**
	 * {@inheritDoc}
	 */
	public void setLength(Double d)
		{
		logger.error("Can't set length of the root node");
		}

	/**
	 * {@inheritDoc}
	 */
	public double getLargestLengthSpan()
		{
		return root.getLargestLengthSpan();
		}

	/**
	 * {@inheritDoc}
	 */
	public double getGreatestBranchLengthDepthBelow()
		{
		return root.getGreatestBranchLengthDepthBelow();
		}


	public int getGreatestNodeDepthBelow()
		{
		return root.getGreatestNodeDepthBelow();
		}

	/**
	 * {@inheritDoc}
	 */
	public void registerChild(PhylogenyNode<T> a)
		{
		root.registerChild(a);
		}

	/**
	 * {@inheritDoc}
	 */
	public void unregisterChild(PhylogenyNode<T> a)
		{
		root.unregisterChild(a);
		}


	/**
	 * Returns an iterator over a set of elements of type T.
	 *
	 * @return an Iterator.
	 */
	public Iterator<PhylogenyNode<T>> iterator()
		{
		return root.iterator();
		}

	/**
	 * {@inheritDoc}
	 */
	public DepthFirstTreeIterator<T, PhylogenyNode<T>> depthFirstIterator()
		{
		return root.depthFirstIterator();
		}

	/**
	 * {@inheritDoc}
	 */
	public T nearestKnownAncestor(RootedPhylogeny<T> rootPhylogeny, T leafId) throws NoSuchNodeException
		{
		T result = null;//nearestKnownAncestorCache.get(leafId);
		if (result == null)
			{
			PhylogenyNode<T> n = getNode(leafId);

			/*if (n == null)
				{
				throw new NoSuchNodeException("Leaf phylogeny does not contain node " + leafId + ".");
				}*/

			//while (rootPhylogeny.getNode(n.getValue()) == null)
			while (true)
				{
				try
					{
					rootPhylogeny.getNode(n.getPayload());

					// if we got here then we found a node
					break;
					}
				catch (NoSuchNodeException e)
					{
					n = n.getParent();
					if (n == null)
						{
						// arrived at root, too bad
						throw new NoSuchNodeException("Taxon " + leafId + " not found in tree.");
						}
					//ncbiDb.getEntityManager().refresh(n);
					}
				}
			result = n.getPayload();
			//	nearestKnownAncestorCache.put(leafId, result);
			}
		//return n.getId();
		return result;
		}

	/**
	 * {@inheritDoc}
	 */
	public T nearestAncestorWithBranchLength(T leafId) throws NoSuchNodeException //throws TreeException
		{
		PhylogenyNode<T> n = getNode(leafId);

		return n.nearestAncestorWithBranchLength().getPayload();
		}


	/**
	 * {@inheritDoc}
	 */
	public BasicPhylogenyNode<T> getRoot()
		{
		return root;
		}

	public BasicPhylogenyNode<T> findRoot()
		{
		return root;
		}

	/**
	 * {@inheritDoc}
	 */
	public boolean isLeaf()
		{
		return root.isLeaf();
		}

	/**
	 * {@inheritDoc}
	 */
	public Double getWeight()
		{
		return 1.;
		}

	/**
	 * {@inheritDoc}
	 */
	public Double getCurrentWeight()
		{
		return 1.;
		}

	/**
	 * {@inheritDoc}
	 */
	public void setWeight(Double v)
		{
		if (v != 1.)
			{
			throw new Error("Can't set root weight to anything other than 1");
			}
		}

	/*	public void setWeight(double v)
		 {
		 if (v != 1.)
			 {
			 throw new Error("Can't set root weight to anything other than 1");
			 }
		 }
 */

	/**
	 * {@inheritDoc}
	 */
	public void incrementWeightBy(double v)
		{
		throw new Error("Can't increment root weight");
		}


	/**
	 * {@inheritDoc}
	 */
	/*	public void propagateWeightFromBelow()
	   {
	   root.propagateWeightFromBelow();
	   }*/

	/**
	 * {@inheritDoc}
	 */
	public double distanceToRoot()
		{
		return 0;
		}

	public void setRoot(BasicPhylogenyNode<T> root)
		{
		this.root = root;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BasicRootedPhylogeny<T> clone()
		{

		BasicRootedPhylogeny<T> result = new BasicRootedPhylogeny<T>();
		result.setRoot(root.clone());
		result.setBasePhylogeny(getBasePhylogeny());
		result.assignUniqueIds(null);
		return result;
		}

	/**
	 * Test whether the given object is the same as this one.  Differs from equals() in that implementations of this
	 * interface may contain additional state which make them not strictly equal; here we're only interested in whether
	 * they're equal as far as this interface is concerned, i.e., for purposes of clustering.
	 *
	 * @param other The clusterable object to compare against
	 * @return True if they are equivalent, false otherwise
	 */
	public boolean equalValue(RootedPhylogeny<T> other)
		{
		throw new NotImplementedException();
		}

	/**
	 * Returns a String identifying this object.  Ideally each clusterable object being analyzed should have a unique
	 * identifier.
	 *
	 * @return a unique identifier for this object
	 */
	public String getId()
		{
		return root.getPayload().toString();
		}

	/**
	 * Get the primary classification label, if available (optional operation)
	 *
	 * @return a Strings describing this object
	 */
	/*	public String getLabel()
	   {
	   return null;
	   }*/
	public BasicPhylogenyNode<T> getSelfNode()
		{
		return root;
		}

	public void appendSubtree(StringBuffer sb, String indent)
		{
		root.appendSubtree(sb, indent);
		}


/*	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException
		{
		root = (BasicPhylogenyNode<T>) stream.readObject();

		uniqueIdToNodeMap = new HashMap<T, BasicPhylogenyNode<T>>();


		// populate the nodes map
		assignUniqueIds(new RequireExistingNodeNamer<T>(false));  // all the nodes should have ids already


		for (PhylogenyNode<T> p : uniqueIdToNodeMap.values())
			{
			for (PhylogenyNode<T> c : p.getChildren())
				{
				c.setParent(p);//c.parent = p;
				}
			}
		}*/

	private Object readResolve() throws ObjectStreamException
		{
		uniqueIdToNodeMap = new HashMap<T, BasicPhylogenyNode<T>>();

		// populate the nodes map
		assignUniqueIds(new RequireExistingNodeNamer<T>(false));  // all the nodes should have ids already

		for (PhylogenyNode<T> p : uniqueIdToNodeMap.values())
			{
			for (PhylogenyNode<T> c : p.getChildren())
				{
				c.setParent(p);//c.parent = p;
				}
			}
		return this;
		}

/*	private Object writeReplace() throws ObjectStreamException
		{
return this;
		}

	private void writeObject(java.io.ObjectOutputStream stream) throws IOException
		{
		stream.writeObject(root);
		}
		*/

	/*	private void readObjectNoData() throws ObjectStreamException
		 {
		 throw new NotSerializableException();
		 }
 */

	public PhylogenyNode<T> nearestAncestorWithBranchLength() throws NoSuchNodeException
		{
		throw new NoSuchNodeException("Root doesn't have a branch length.");
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
//		try
//			{
		if (getLeafValues().equals(ids) && includeInternalBranches)
			{
			return this;
			}
//			}
//		catch (TreeRuntimeException e)
//			{
		// the actual tree is expensive to load (e.g. NcbiTaxonomyService) so getLeafValues is a bad idea
		// OK, just do the explicit extraction anyway then
//			}
		return super.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches, mode);
		}

	/*
	public TaxonomyService<T> asService()
		{
		return new RootedPhylogenyAsService<T>(this);
		}
		*/
	}



