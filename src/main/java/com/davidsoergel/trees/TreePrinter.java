/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: TreePrinter.java 249 2008-08-28 20:50:12Z soergel $
 */
public class TreePrinter
	{

	public static <T, I extends HierarchyNode<T, I>> String prettyPrint(@NotNull HierarchyNode<T, I> root)
		{
		StringBuffer sb = new StringBuffer();
		Iterator<I> iterator = root.iterator();// depth first
		while (iterator.hasNext())
			{
			I n = iterator.next();
			int level = n.getAncestorPath().size();
			for (int i = 1; i < level; i++)
				{
				sb.append("\t|");
				}
			sb.append(" ").append(n).append("\n");
			}
		return sb.toString();
		}

	/*
   public static void appendToStringBuffer(HierarchyNode n, StringBuffer sb, int indentLevel)
	   {
	   for (int i = 0; i < indentLevel; i++)
		   {
		   sb.append("\t");
		   }
	   sb.append(getKey().getKey()).append(" = ").append(value).append("\n");
	   indentLevel++;

	   for (HierarchicalTypedPropertyNode<HierarchicalTypedPropertyNode<String, Object>, ParameterSetValueOrRange> child : children
			   .values())
		   {
		   ((WrappedHierarchicalTypedPropertyNode) child).appendToStringBuffer(sb, indentLevel);
		   }
	   }*/
	}
