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
