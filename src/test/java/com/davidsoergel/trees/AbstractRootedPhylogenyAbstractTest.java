package com.davidsoergel.trees;

import com.davidsoergel.dsutils.ContractTestAwareContractTest;
import com.davidsoergel.dsutils.TestInstanceFactory;

import java.util.Queue;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: AbstractRootedPhylogenyAbstractTest.java 201 2008-09-20 01:43:33Z soergel $
 */
public class AbstractRootedPhylogenyAbstractTest<T extends AbstractRootedPhylogeny>
		extends ContractTestAwareContractTest<AbstractRootedPhylogeny>

	{
	private TestInstanceFactory<T> tif;

	public AbstractRootedPhylogenyAbstractTest(TestInstanceFactory<T> tif)
		{
		this.tif = tif;
		}

	@Override
	public void addContractTestsToQueue(Queue theContractTests)
		{
		theContractTests.add(new RootedPhylogenyInterfaceTest<T>(tif));
		}
	}
