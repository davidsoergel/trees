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

import com.davidsoergel.dsutils.AtomicContractTest;
import com.davidsoergel.dsutils.TestInstanceFactory;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: HierarchyNodeInterfaceTest.java 439 2009-08-06 15:12:26Z soergel $
 */
public class HierarchyNodeInterfaceTest<T extends HierarchyNode> extends AtomicContractTest
	{
	private TestInstanceFactory<T> tif;


	// --------------------------- CONSTRUCTORS ---------------------------

	public HierarchyNodeInterfaceTest()
		{
		}

	public HierarchyNodeInterfaceTest(TestInstanceFactory<T> tif)
		{
		this.tif = tif;
		}

	// oops, there is no addChild()

	/*
	@Test
	public void addedChildIsIncludedInChildArray() throws Exception
		{
		HierarchyNode n = tif.createInstance();
		HierarchyNode c = tif.createInstance();
		n.addChild(c);
		assert n.getChildren().contains(c);
		}

	@Test
	public void addChildSetsChildParentLink()
		{
		assert false;
		}
		*/

	@Test
	public void newChildIsIncludedInChildArray() throws Exception
		{
		T n = tif.createInstance();
		HierarchyNode c = n.newChild("a");
		assert n.getChildren().contains(c);
		}

	@Test
	public void newChildSetsChildParentLink() throws Exception
		{
		T n = tif.createInstance();
		HierarchyNode c = n.newChild("a");
		assert c.getParent() == n.getSelfNode();
		}

	@Test
	public void ancestorPathIncludesSelfNode() throws Exception
		{
		T n = tif.createInstance();
		assert n.getAncestorPath().contains(n.getSelfNode());
		}

	/*	@Test
   public void ancestorPathDoesNotIncludeThis() throws Exception
	   {
	   HierarchyNode n = tif.createInstance();
	   assert !n.getAncestorPath().contains(n);
	   }*/

	@Test
	public void ancestorPathExtendsFromRootToImmediateParent() throws Exception
		{
		T n = tif.createInstance();
		List path = new ArrayList(n.getAncestorPath());
		Collections.reverse(path);
		HierarchyNode p = n.getSelfNode();
		while (p != null)
			{
			HierarchyNode p2 = p.getParent();

			assert p == path.get(0);
			path.remove(p);
			p = p2;
			}
		assert path.isEmpty();
		}

	@Test
	public void providesDepthFirstIterator() throws Exception
		{
		T n = tif.createInstance();
		assert n.depthFirstIterator() != null;
		}
	}
