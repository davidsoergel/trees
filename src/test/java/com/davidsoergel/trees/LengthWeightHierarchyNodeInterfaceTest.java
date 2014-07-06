/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
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
