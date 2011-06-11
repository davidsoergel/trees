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

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A node in a simple hierarchy, where a value of the given generic type is attached at each node.  The children are
 * stored in a SortedSet and thus maintain their natural order.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: ImmutableSortedSetHierarchyNode.java 524 2009-10-10 07:55:47Z soergel $
 */
@Deprecated
public class ImmutableSortedSetHierarchyNode<T extends Comparable<T>>
		extends ImmutableHierarchyNode<T, ImmutableSortedSetHierarchyNode<T>>
		implements Comparable<ImmutableSortedSetHierarchyNode<? extends T>>
	{
	// ------------------------------ FIELDS ------------------------------

	private final SortedSet<ImmutableSortedSetHierarchyNode<T>> children =
			new TreeSet<ImmutableSortedSetHierarchyNode<T>>();


	private ImmutableSortedSetHierarchyNode<T> parent;
	private final T contents;


	public ImmutableSortedSetHierarchyNode(final T contents)
		{
		this.contents = contents;
		}

	// --------------------- GETTER / SETTER METHODS ---------------------

	@NotNull
	public SortedSet<ImmutableSortedSetHierarchyNode<T>> getChildren()
		{
		return children;
		}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public ImmutableSortedSetHierarchyNode<T> getChildWithPayload(T id) throws NoSuchNodeException
		{// We could map the children collection as a Map; but that's some hassle, and since there are generally just 2 children anyway, this is simpler

		// also, the child id is often not known when it is added to the children Set, so putting the child into a children Map wouldn't work

		for (ImmutableSortedSetHierarchyNode<T> child : children)
			{
			if (child.getPayload() == id)
				{
				return child;
				}
			}
		throw new NoSuchNodeException("No child found with payload " + id);
		}


	@NotNull
	public T getPayload()
		{
		return contents;
		}


	public ImmutableSortedSetHierarchyNode<T> getParent()
		{
		return parent;
		}

	public void setParent(ImmutableSortedSetHierarchyNode<T> parent)
		{
		if (this.parent != null)
			{
			this.parent.unregisterChild(this);
			}
		this.parent = parent;
		if (this.parent != null)
			{
			this.parent.registerChild(this);
			}
		}

	// ------------------------ INTERFACE METHODS ------------------------


	// --------------------- Interface Comparable ---------------------

	public int compareTo(ImmutableSortedSetHierarchyNode<? extends T> o)
		{
		T oValue = o.getPayload();
		if (oValue == null || contents == null)
			{
			throw new TreeRuntimeException("SortedSetHierarchyNode must contain a value");
			}
		return contents.compareTo(oValue);
		}

	public boolean equals(Object o)
		{
		if (this == o)
			{
			return true;
			}
		if (o == null || getClass() != o.getClass())
			{
			return false;
			}

		ImmutableSortedSetHierarchyNode<T> that = (ImmutableSortedSetHierarchyNode<T>) o;

		if (!CollectionUtils.isEqualCollection(children, that.children))
			{
			return false;
			}
		if (contents != null ? !contents.equals(that.contents) : that.contents != null)
			{
			return false;
			}
		if (parent != null ? !parent.equals(that.parent) : that.parent != null)
			{
			return false;
			}

		return true;
		}

	public int hashCode()
		{
		int result = 0;
		//result = children != null ? children.hashCode() : 0;
		result = 31 * result + (parent != null ? parent.hashCode() : 0);
		result = 31 * result + (contents != null ? contents.hashCode() : 0);
		return result;
		}

	// --------------------- Interface HierarchyNode ---------------------


	public ImmutableSortedSetHierarchyNode<T> newChild(final T value)
		{
		ImmutableSortedSetHierarchyNode<T> child = new ImmutableSortedSetHierarchyNode<T>(value);
		children.add(child);
		child.parent = this;
		//child.setParent(this);
		return child;
		}


	public boolean isLeaf()
		{
		return children == null || children.isEmpty();
		}

	public List<ImmutableSortedSetHierarchyNode<T>> getAncestorPath()
		{
		return getAncestorPath(true);
		}

	public List<ImmutableSortedSetHierarchyNode<T>> getAncestorPath(boolean includeSelf)
		{
		List<ImmutableSortedSetHierarchyNode<T>> result = new LinkedList<ImmutableSortedSetHierarchyNode<T>>();
		ImmutableSortedSetHierarchyNode<T> trav = includeSelf ? this : getParent();

		while (trav != null)
			{
			result.add(0, trav);
			trav = trav.getParent();
			}

		return result;
		}

	public List<T> getAncestorPathPayloads()
		{
		List<T> result = new LinkedList<T>();
		ImmutableSortedSetHierarchyNode<T> trav = this;

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
	public Iterator<ImmutableSortedSetHierarchyNode<T>> iterator()
		{
		return new DepthFirstTreeIteratorImpl(this);
		}

	public DepthFirstTreeIterator<T, ImmutableSortedSetHierarchyNode<T>> depthFirstIterator()
		{
		return new DepthFirstTreeIteratorImpl(this);
		}


	public ImmutableSortedSetHierarchyNode<T> getSelfNode()
		{
		return this;
		}

	public int countDescendantsIncludingThis()
		{
		throw new NotImplementedException();
		}
	}
