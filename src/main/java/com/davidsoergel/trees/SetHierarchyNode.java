/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.trees;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A node in a simple hierarchy, where a value of the given generic type is attached at each node.  The children are
 * stored in a Set and are thus unordered.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: SetHierarchyNode.java 439 2009-08-06 15:12:26Z soergel $
 */
public class SetHierarchyNode<T extends Serializable> extends AbstractHierarchyNode<T, SetHierarchyNode<T>>
		implements Serializable
	{
	// ------------------------------ FIELDS ------------------------------

	protected Set<SetHierarchyNode<T>> children = new HashSet<SetHierarchyNode<T>>();


	public SetHierarchyNode(T contents)
		{
		super(contents);
		}

	public SetHierarchyNode()
		{
		super();
		}

	public void registerChild(SetHierarchyNode<T> child)
		{
		children.add(child);
		}

	public void unregisterChild(SetHierarchyNode<T> child)
		{
		children.remove(child);
		}

	@NotNull
	public Set<SetHierarchyNode<T>> getChildren()
		{
		return children;
		}

	/*	public SetHierarchyNode<T> newChild()
		 {
		 SetHierarchyNode<T> result = new SetHierarchyNode<T>();
		 //children.add(result);  // setParent calls registerChild
		 result.setParent(this);
		 return result;
		 }
 */
	public SetHierarchyNode<T> newChild(T payload)
		{
		SetHierarchyNode<T> result = new SetHierarchyNode<T>();
		//children.add(result);  // setParent calls registerChild
		result.setPayload(payload);
		result.setParent(this);
		return result;
		}

	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException
		{
		payload = (T) stream.readObject();
		children = (Set<SetHierarchyNode<T>>) stream.readObject();

		for (SetHierarchyNode<T> child : children)
			{
			child.setParent(this);
			}
		}

	private void writeObject(java.io.ObjectOutputStream stream) throws IOException
		{
		stream.writeObject(payload);
		stream.writeObject(children);
		}
	}
