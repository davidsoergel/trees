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

import com.davidsoergel.dsutils.ContractTestAwareContractTest;
import com.davidsoergel.dsutils.TestInstanceFactory;
import org.testng.annotations.Test;

import java.util.Queue;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: LengthWeightHierarchyNodeInterfaceTest.java 328 2009-07-29 01:05:01Z soergel $
 */
public class LengthWeightHierarchyNodeInterfaceTest<T extends LengthWeightHierarchyNode>
		extends ContractTestAwareContractTest<LengthWeightHierarchyNode>
	{
	protected TestInstanceFactory<T> tif;

	// --------------------------- CONSTRUCTORS ---------------------------

	public LengthWeightHierarchyNodeInterfaceTest(TestInstanceFactory<T> tif)
		{
		this.tif = tif;
		}

	@Override
	public void addContractTestsToQueue(Queue theContractTests)
		{
		theContractTests.add(new HierarchyNodeInterfaceTest<T>(tif)

		);
		}

	/*
	 public String getTestName()
		 {
		 String result;
		 try
			 {
			 // this sucks because createInstance() may be expensive.
			 // Instead we could get the concrete class name by some other means, e.g. adding a getConcreteClassName()
			 // to the TestInterfaceFactory method.
			 // result = getClass().getSimpleName() + " -> " + tif.createInstance().getClass().getSimpleName();
			 result = tif.createInstance().getClass().getCanonicalName();
			 }
		 catch (Exception e)
			 {
			logger.error("Error", e);
			 result = getClass().getSimpleName();
			 }
		 return result;
		 }
 */
	/**
	 * In order for this test to be informative, the provided test tree must contain a confusing case, i.e. where the
	 * longest span below some node does not pass through the node itself.
	 */
	@Test
	public void largestLengthSpanIsMonotonicDownTree() throws Exception
		{
		LengthWeightHierarchyNode<String, ? extends LengthWeightHierarchyNode> testInstance = tif.createInstance();

		// yeah we calculate each span twice, but so what

		for (LengthWeightHierarchyNode n : testInstance)
			{
			double span = n.getLargestLengthSpan();
			LengthWeightHierarchyNode p = (LengthWeightHierarchyNode) n.getParent();
			if (p != null)
				{
				assert p.getLargestLengthSpan() >= span;
				}
			}
		}


	@Test
	public void distanceToRootWorks() throws Exception
		{
		LengthWeightHierarchyNode tmp = tif.createInstance();
		LengthWeightHierarchyNode t = (LengthWeightHierarchyNode) tmp.getChildWithPayload("a").getChildWithPayload("aa")
				.getChildWithPayload("aaa");
		assert t.distanceToRoot() == 30;
		}
	}
