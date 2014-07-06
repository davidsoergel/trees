/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
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
