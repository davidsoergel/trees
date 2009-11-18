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
