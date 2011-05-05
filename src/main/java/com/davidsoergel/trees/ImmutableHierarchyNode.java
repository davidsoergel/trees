package com.davidsoergel.trees;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: ImmutableHierarchyNode.java 439 2009-08-06 15:12:26Z soergel $
 */
public abstract class ImmutableHierarchyNode<T, I extends ImmutableHierarchyNode<T, I>> implements HierarchyNode<T, I>
	{
	/**
	 * {@inheritDoc}
	 */
	/*	public I newChild()//HierarchyNode<? extends T, ? extends I>
		 {
		 throw new TreeRuntimeException("Can't create an immutable node without providing a value");
		 }
 */

	/**
	 * {@inheritDoc}
	 */
	public void setPayload(T contents)
		{
		throw new TreeRuntimeException("Node is immutable");
		}

	public void registerChild(I contents)
		{
		throw new TreeRuntimeException("Node is immutable");
		}

	public void unregisterChild(I contents)
		{
		throw new TreeRuntimeException("Node is immutable");
		}

	/**
	 * Creates a new child node of the appropriate type
	 *
	 * @param value the value to assign to the node
	 * @return the new child node
	 */
	public abstract I newChild(final T value);

	public void setParent(final I parent)
		{
		}


	@NotNull
	public Collection<? extends I> getDescendantLeaves()
		{
		Set<I> result = new HashSet<I>();
		for (I n : this)
			{
			if (n.isLeaf())
				{
				result.add(n);
				}
			}
		return result;
		}
	}
