/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.trees;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A node in a simple hierarchy, where a value of the given generic type is attached at each node.  The children are
 * stored in a List and are thus ordered.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: ListHierarchyNode.java 439 2009-08-06 15:12:26Z soergel $
 */
public class ListHierarchyNode<T> extends AbstractHierarchyNode<T, ListHierarchyNode<T>>
		implements Serializable //implements HierarchyNode<T, ListHierarchyNode<T>>
	{
	private List<ListHierarchyNode<T>> children = new ArrayList<ListHierarchyNode<T>>();

	public ListHierarchyNode(T contents)
		{
		super(contents);
		}

	public ListHierarchyNode()
		{
		super();
		}

	public void registerChild(ListHierarchyNode<T> child)
		{
		children.add(child);
		}

	public void unregisterChild(ListHierarchyNode<T> child)
		{
		children.remove(child);
		}

	@NotNull
	public List<? extends ListHierarchyNode<T>> getChildren()
		{
		return children;
		}

	/*	public ListHierarchyNode<T> newChild()
		 {
		 ListHierarchyNode<T> result = new ListHierarchyNode<T>();
		 //children.add(result);  // setParent calls registerChild
		 result.setParent(this);
		 return result;
		 }
 */
	public ListHierarchyNode<T> newChild(T payload)
		{
		ListHierarchyNode<T> result = new ListHierarchyNode<T>();
		//children.add(result);  // setParent calls registerChild
		result.setPayload(payload);
		result.setParent(this);
		return result;
		}

	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException
		{
		payload = (T) stream.readObject();
		children = (List<ListHierarchyNode<T>>) stream.readObject();

		for (ListHierarchyNode<T> child : children)
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
