/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees;

import com.davidsoergel.dsutils.AtomicContractTest;
import com.davidsoergel.dsutils.TestInstanceFactory;
import com.davidsoergel.dsutils.collections.DSCollectionUtils;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/**
 * Tests instances of TaxonMergingPhylogeny.  The instances created by the provided factory must have certain properties
 * which we'll then test for; see the comments on each test.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: TaxonMergingPhylogenyInterfaceTest.java 328 2009-07-29 01:05:01Z soergel $
 */
public class TaxonMergingPhylogenyInterfaceTest<T extends TaxonMergingPhylogeny> extends AtomicContractTest
	{
	private TestInstanceFactory<T> tif;


	// --------------------------- CONSTRUCTORS ---------------------------

	public TaxonMergingPhylogenyInterfaceTest(TestInstanceFactory<T> tif)
		{
		this.tif = tif;
		}

	@Test
	public void findsNearestAncestorWithBranchLength() throws Exception
		{
		TaxonMergingPhylogeny tmp = tif.createInstance();
		assert tmp.nearestAncestorWithBranchLength("aaaa").equals("aa");
		}

	@Test
	public void extractsTreeCorrectlyGivenBaseLeaves() throws Exception
		{
		TaxonMergingPhylogeny<Serializable> tmp = tif.createInstance();
		Set leafIDs = DSCollectionUtils.setOf("baa", "bbba", "ca", "cb");

		RootedPhylogeny<Serializable> result = tmp.extractTreeWithLeafIDs(leafIDs, false, false);

		assert result.getUniqueIdToNodeMap().size() == 7;

		Collection<String> okNodes = Arrays.asList("baa", "bbba", "b", "ca", "cb", "c", "root");

		for (LengthWeightHierarchyNode n : result)
			{
			String s = (String) n.getPayload();
			assert okNodes.contains(s);
			}
		}

	@Test
	public void extractsTreeCorrectlyGivenInternalLeaves() throws Exception
		{
		TaxonMergingPhylogeny<String> tmp = tif.createInstance();
		Set leafIDs = DSCollectionUtils.setOf(new Object[]{"ba", "bb", "c", "a"});

		RootedPhylogeny<String> result = tmp.extractTreeWithLeafIDs(leafIDs, false, false);

		assert result.getUniqueIdToNodeMap().size() == 6;

		Collection<String> okNodes = Arrays.asList("ba", "bb", "b", "c", "a", "root");

		for (PhylogenyNode<String> n : result)
			{
			String s = n.getPayload();
			assert okNodes.contains(s);
			}
		}

	@Test(expectedExceptions = NoSuchNodeException.class)
	public void treeExtractionThrowsExceptionOnLeafNotFound() throws Exception
		{
		TaxonMergingPhylogeny tmp = tif.createInstance();
		Set leafIDs = DSCollectionUtils.setOf(new Object[]{"ba", "bb", "Node Absent", "a"});

		RootedPhylogeny result = tmp.extractTreeWithLeafIDs(leafIDs, false, false);
		}
	}
