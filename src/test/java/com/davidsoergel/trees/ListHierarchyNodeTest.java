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
 * @version $Id: ListHierarchyNodeTest.java 427 2009-07-29 01:04:57Z soergel $
 */
public class ListHierarchyNodeTest extends ContractTestAware<ListHierarchyNode>
		implements TestInstanceFactory<ListHierarchyNode<Object>>
	{

	/**
	 * {@inheritDoc}
	 */
	public ListHierarchyNode<Object> createInstance() throws Exception
		{
		ListHierarchyNode<Object> root = new ListHierarchyNode<Object>("root");
		ListHierarchyNode<Object> a = root.newChild("a");
		ListHierarchyNode<Object> b = root.newChild("b");
		ListHierarchyNode<Object> c = root.newChild("c");
		ListHierarchyNode<Object> d = a.newChild("d");
		ListHierarchyNode<Object> e = a.newChild("e");
		ListHierarchyNode<Object> f = b.newChild("f");
		ListHierarchyNode<Object> g = b.newChild("g");
		ListHierarchyNode<Object> h = b.newChild("h");
		ListHierarchyNode<Object> i = c.newChild("i");
		ListHierarchyNode<Object> j = g.newChild("j");
		ListHierarchyNode<Object> k = g.newChild("k");
		ListHierarchyNode<Object> l = k.newChild("l");
		ListHierarchyNode<Object> m = l.newChild("m");
		return root;
		}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addContractTestsToQueue(Queue theContractTests)
		{
		theContractTests.add(new HierarchyNodeInterfaceTest<ListHierarchyNode<Object>>(this));
		}

	@Factory
	public Object[] instantiateAllContractTests()
		{
		return super.instantiateAllContractTestsWithName(ListHierarchyNode.class.getCanonicalName());
		}


	@Test
	public void childrenAreInAdditionOrderAfterNewChild() throws Exception
		{
		ListHierarchyNode<Object> n = createInstance();
		n.newChild("p");
		n.newChild("o");
		n.newChild("n");

		Iterator<? extends ListHierarchyNode<Object>> l = n.getChildren().iterator();
		assert l.next().getPayload().equals("a");
		assert l.next().getPayload().equals("b");
		assert l.next().getPayload().equals("c");
		assert l.next().getPayload().equals("p");
		assert l.next().getPayload().equals("o");
		assert l.next().getPayload().equals("n");
		}
	}
