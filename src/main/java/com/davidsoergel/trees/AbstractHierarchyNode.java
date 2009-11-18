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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// TODO make ListHierarchyNode, etc. extend this

/**
 * Abstract implementation of some of the most basic HierarchyNode functionality.  Concrete classes extending this need
 * implement only getChildren() and newChild(), because they must choose what kind of Collection to use for the
 * children.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: AbstractHierarchyNode.java 524 2009-10-10 07:55:47Z soergel $
 */
public abstract class AbstractHierarchyNode<KV, H extends HierarchyNode<KV, H>> implements HierarchyNode<KV, H>
	{
	// ------------------------------ FIELDS ------------------------------

	protected H parent;//HierarchyNode<? extends T, I>
	protected KV payload;

	protected AbstractHierarchyNode(KV payload)
		{
		this.payload = payload;
		}

	protected AbstractHierarchyNode()
		{
		}


	// --------------------- GETTER / SETTER METHODS ---------------------

	/**
	 * {@inheritDoc}
	 */
	public KV getPayload()
		{
		return payload;
		}

	/**
	 * {@inheritDoc}
	 */
	public void setPayload(KV payload)
		{
		this.payload = payload;
		}

	/**
	 * {@inheritDoc}
	 */
	public H getParent()//HierarchyNode<? extends T, I>
		{
		return parent;
		}

	public void setParent(H parent)
		{
		if (this.parent == parent)
			{
			return;
			}

		if (this.parent != null)
			{
			this.parent.unregisterChild((H) this);
			}
		this.parent = parent;
		if (this.parent != null)
			{
			this.parent.registerChild((H) this);
			}
		//	parent.getChildren().add((I) this);
		}


	// ------------------------ INTERFACE METHODS ------------------------


	// --------------------- Interface HierarchyNode ---------------------

	//private Collection<HierarchyNode<T>> children;

	/**
	 * {@inheritDoc}
	 */
	public abstract Collection<? extends H> getChildren();


	// -------------------------- OTHER METHODS --------------------------

	/**
	 * {@inheritDoc}
	 */
	/*	public void addChild(HierarchyNode<? extends T, I> child)
	   {
	   getChildren().add(child);
	   // NO!
	   // child.setParent(this);
	   }*/
	//public abstract I newChild(T contents);


	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public H getChildWithPayload(KV payload) throws NoSuchNodeException
		{// We could map the children collection as a Map; but that's some hassle, and since there are generally just 2 children anyway, this is simpler

		// also, the child id is often not known when it is added to the children Set, so putting the child into a children Map wouldn't work

		for (H child : getChildren())
			{
			final KV cp = child.getPayload();
			if ((cp == null && payload == null) || cp.equals(payload))
				{
				return child;
				}
			}
		throw new NoSuchNodeException();
		}


	// ------------------------ INTERFACE METHODS ------------------------


	// --------------------- Interface HierarchyNode ---------------------


	public boolean isLeaf()
		{
		Collection<? extends H> children = getChildren();
		return children == null || children.isEmpty();
		}

	public List<HierarchyNode<KV, H>> getAncestorPath()
		{
		return getAncestorPath(true);
		}

	public List<HierarchyNode<KV, H>> getAncestorPath(boolean includeSelf)
		{
		List<HierarchyNode<KV, H>> result = new LinkedList<HierarchyNode<KV, H>>();
		HierarchyNode<KV, H> trav = includeSelf ? this : getParent();

		while (trav != null)
			{
			result.add(0, trav);
			trav = trav.getParent();
			}

		return result;
		}

	public List<KV> getAncestorPathPayloads()
		{
		List<KV> result = new LinkedList<KV>();
		HierarchyNode<KV, H> trav = this;

		while (trav != null)
			{
			result.add(0, trav.getPayload());
			trav = trav.getParent();
			}

		return result;
		}


	/**
	 * Returns an iterator over a set of elements of type T.
	 *
	 * @return an Iterator.
	 */
	public Iterator<H> iterator()
		{
		return new DepthFirstTreeIteratorImpl(this);
		}

	public DepthFirstTreeIterator<KV, H> depthFirstIterator()
		{
		return new DepthFirstTreeIteratorImpl(this);
		}

	/*
	public HierarchyNode<T, I> getSelf()
		{
		return this;
		}*/

	public HierarchyNode<KV, H> getSelfNode()
		{
		return this;
		}

	public int countDescendantsIncludingThis()
		{
		int result = 1;
		for (H c : getChildren())
			{
			result += c.countDescendantsIncludingThis();
			}
		return result;
		}
	}
