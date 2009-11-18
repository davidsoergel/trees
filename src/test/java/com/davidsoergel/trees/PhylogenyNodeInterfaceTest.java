package com.davidsoergel.trees;

import com.davidsoergel.dsutils.ContractTestAwareContractTest;
import com.davidsoergel.dsutils.TestInstanceFactory;
import com.davidsoergel.dsutils.math.MathUtils;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.util.Queue;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: PhylogenyNodeInterfaceTest.java 328 2009-07-29 01:05:01Z soergel $
 */
public class PhylogenyNodeInterfaceTest<T extends PhylogenyNode>
		extends ContractTestAwareContractTest<PhylogenyNode<String>>
	{
	private TestInstanceFactory<T> tif;


	// --------------------------- CONSTRUCTORS ---------------------------

	public PhylogenyNodeInterfaceTest(TestInstanceFactory<T> tif)
		{
		this.tif = tif;
		}

	@Override
	public void addContractTestsToQueue(Queue theContractTests)
		{
		theContractTests.add(new LengthWeightHierarchyNodeInterfaceTest<T>(tif));
		}

	@Test
	public void getChildWorksIfChildIsPresent() throws Exception
		{
		PhylogenyNode tmp = tif.createInstance();
		tmp.getChildWithPayload("a");
		}


	@Test(expectedExceptions = NoSuchNodeException.class)
	public void getChildThrowsExceptionIfChildIsAbsent() throws Exception
		{
		PhylogenyNode tmp = tif.createInstance();
		tmp.getChildWithPayload("Node Absent");
		}


	@Test
	public void getWeightPropagatesWeightFromBelowIfNeeded() throws Exception
		{
		PhylogenyNode<Serializable> tmp = tif.createInstance();
		for (LengthWeightHierarchyNode n : tmp)
			{
			if (!n.isLeaf())
				{
				n.setWeight(null);
				}
			}
		assert MathUtils.equalWithinFPError(tmp.getWeight(), 1.);// root is always 1
		/*	for (LengthWeightHierarchyNode n : tmp)
			{
			assert n.isLeaf() || n.getWeight() == null;
			}
		tmp.propagateWeightFromBelow();*/
		for (LengthWeightHierarchyNode n : tmp)
			{
			assert n.getWeight() != 0;
			}
		}
	}

