/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees;

import com.davidsoergel.dsutils.ContractTestAware;
import com.davidsoergel.dsutils.TestInstanceFactory;
import org.testng.annotations.Factory;

import java.io.Serializable;
import java.util.Queue;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: SetHierarchyNodeTest.java 439 2009-08-06 15:12:26Z soergel $
 */

public class SetHierarchyNodeTest extends ContractTestAware<SetHierarchyNode>
		implements TestInstanceFactory<SetHierarchyNode<Serializable>>
	{
	/**
	 * {@inheritDoc}
	 */
	public SetHierarchyNode<Serializable> createInstance() throws Exception
		{
		SetHierarchyNode<Serializable> root = new SetHierarchyNode<Serializable>();
		SetHierarchyNode<Serializable> a = root.newChild("a");
		SetHierarchyNode<Serializable> b = root.newChild("b");
		SetHierarchyNode<Serializable> c = root.newChild("c");
		SetHierarchyNode<Serializable> d = a.newChild("d");
		SetHierarchyNode<Serializable> e = a.newChild("e");
		SetHierarchyNode<Serializable> f = b.newChild("f");
		SetHierarchyNode<Serializable> g = b.newChild("g");
		SetHierarchyNode<Serializable> h = b.newChild("h");
		SetHierarchyNode<Serializable> i = c.newChild("i");
		SetHierarchyNode<Serializable> j = g.newChild("j");
		SetHierarchyNode<Serializable> k = g.newChild("k");
		SetHierarchyNode<Serializable> l = k.newChild("l");
		SetHierarchyNode<Serializable> m = l.newChild("m");
		return root;
		}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addContractTestsToQueue(Queue theContractTests)
		{
		theContractTests.add(new HierarchyNodeInterfaceTest<SetHierarchyNode<Serializable>>(this)

		);
		}

	@Factory
	public Object[] instantiateAllContractTests()
		{
		return super.instantiateAllContractTestsWithName(SetHierarchyNode.class.getCanonicalName());
		}
	}
