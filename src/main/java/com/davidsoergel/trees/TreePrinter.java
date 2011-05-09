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
