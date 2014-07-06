/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees;

import com.davidsoergel.dsutils.ContractTestAware;
import com.davidsoergel.dsutils.TestInstanceFactory;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.Queue;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: ImmutableSortedSetHierarchyNodeTest.java 427 2009-07-29 01:04:57Z soergel $
 */
public class ImmutableSortedSetHierarchyNodeTest extends ContractTestAware<ImmutableSortedSetHierarchyNode>
		implements TestInstanceFactory<ImmutableSortedSetHierarchyNode<? extends Comparable>>
	{
	/**
	 * {@inheritDoc}
	 */
	public ImmutableSortedSetHierarchyNode<? extends Comparable> createInstance() throws Exception
		{
		ImmutableSortedSetHierarchyNode root = new ImmutableSortedSetHierarchyNode("root");
		ImmutableSortedSetHierarchyNode a = root.newChild("a");
		ImmutableSortedSetHierarchyNode b = root.newChild("b");
		ImmutableSortedSetHierarchyNode c = root.newChild("c");
		ImmutableSortedSetHierarchyNode d = a.newChild("d");
		ImmutableSortedSetHierarchyNode e = a.newChild("e");
		ImmutableSortedSetHierarchyNode f = b.newChild("f");
		ImmutableSortedSetHierarchyNode g = b.newChild("g");
		ImmutableSortedSetHierarchyNode h = b.newChild("h");
		ImmutableSortedSetHierarchyNode i = c.newChild("i");
		ImmutableSortedSetHierarchyNode j = g.newChild("j");
		ImmutableSortedSetHierarchyNode k = g.newChild("k");
		ImmutableSortedSetHierarchyNode l = k.newChild("l");
		ImmutableSortedSetHierarchyNode m = l.newChild("m");
		return root;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addContractTestsToQueue(Queue theContractTests)
		{
		theContractTests.add(new ImmutableHierarchyNodeInterfaceTest<ImmutableSortedSetHierarchyNode>(this)


		);
		}

	@Factory
	public Object[] instantiateAllContractTests()
		{
		return super.instantiateAllContractTestsWithName(ImmutableSortedSetHierarchyNode.class.getCanonicalName());
		}


	@Test
	public void childrenAreInSortedOrderAfterNewChild() throws Exception
		{
		ImmutableSortedSetHierarchyNode n = createInstance();
		n.newChild("p");
		n.newChild("o");
		n.newChild("n");

		Iterator<HierarchyNode<String, ImmutableSortedSetHierarchyNode<String>>> l = n.getChildren().iterator();
		assert l.next().getPayload().equals("a");
		assert l.next().getPayload().equals("b");
		assert l.next().getPayload().equals("c");
		assert l.next().getPayload().equals("n");
		assert l.next().getPayload().equals("o");
		assert l.next().getPayload().equals("p");
		}
	}
