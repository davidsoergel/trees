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
