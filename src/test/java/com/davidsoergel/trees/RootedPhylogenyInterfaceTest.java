package com.davidsoergel.trees;

import com.davidsoergel.dsutils.ContractTestAwareContractTest;
import com.davidsoergel.dsutils.TestInstanceFactory;
import com.davidsoergel.dsutils.collections.DSCollectionUtils;
import com.davidsoergel.dsutils.math.MathUtils;
import com.davidsoergel.stats.UniformDistribution;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.commons.collections15.CollectionUtils;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.util.Queue;
import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: RootedPhylogenyInterfaceTest.java 333 2009-08-06 15:12:28Z soergel $
 */
public class RootedPhylogenyInterfaceTest<T extends RootedPhylogeny>
		extends ContractTestAwareContractTest<RootedPhylogeny>
	{
	private TestInstanceFactory<T> tif;


	// --------------------------- CONSTRUCTORS ---------------------------

	@Override
	public void addContractTestsToQueue(Queue theContractTests)
		{
		theContractTests.add(new PhylogenyNodeInterfaceTest<T>(tif));
		theContractTests.add(new TaxonMergingPhylogenyInterfaceTest<T>(tif));
		}

	// --------------------------- CONSTRUCTORS ---------------------------

	public RootedPhylogenyInterfaceTest(TestInstanceFactory<T> tif)
		{
		this.tif = tif;
		}

	@Test
	public void findsCommonAncestorOfTwoNodes() throws Exception
		{
		T tmp = tif.createInstance();
		assert tmp.commonAncestor("baa", "bbba").equals("b");
		}

	@Test
	public void findsCommonAncestorOfManyNodes() throws Exception
		{
		T tmp = tif.createInstance();
		Set nodeSet = DSCollectionUtils.setOf("baa", "ba", "bba", "bbba");
		assert tmp.commonAncestor(nodeSet).equals("b");
		}

	@Test
	public void computesDistanceBetweenTwoNodes() throws Exception
		{
		T tmp = tif.createInstance();
		assert tmp.distanceBetween("baa", "cb") == 11.2;
		}

	@Test
	public void returnsAllNodes() throws Exception
		{
		T tmp = tif.createInstance();
		assert tmp.getUniqueIdToNodeMap().size() == 17;
		}

	@Test
	public void returnsAllLeaves() throws Exception
		{
		T tmp = tif.createInstance();
		assert tmp.getLeaves().size() == 8;
		}

	@Test
	public void returnsAllNodeValues() throws Exception
		{
		T tmp = tif.createInstance();
		assert tmp.getNodeValues().size() == 17;
		}

	@Test
	public void returnsAllLeafValues() throws Exception
		{
		T tmp = tif.createInstance();
		assert tmp.getLeaves().size() == 8;
		}

	@Test
	public void computesTotalBranchLength() throws Exception
		{
		T tmp = tif.createInstance();
		assert MathUtils.equalWithinFPError(tmp.getTotalBranchLength(), 87);
		}

	@Test
	public void randomizedLeafWeightsAreNormalized() throws Exception
		{
		RootedPhylogeny<Serializable> tmp = tif.createInstance();
		tmp.randomizeLeafWeights(new UniformDistribution(0, 1));
		assert MathUtils.equalWithinFPError(tmp.getWeight(), 1);

		double leafSum = 0;
		for (PhylogenyNode n : tmp.getLeaves())
			{
			leafSum += n.getWeight();
			}

		assert MathUtils.equalWithinFPError(leafSum, 1);
		}

	@Test
	public void findsNearestKnownAncestorInAnotherTree() throws Exception
		{
		T mainTree = tif.createInstance();

		BasicRootedPhylogeny rootPhylogeny = new BasicRootedPhylogeny<String>("root");
		rootPhylogeny.newChild("a");
		rootPhylogeny.newChild("b");
		rootPhylogeny.newChild("c");
		rootPhylogeny.assignUniqueIds(new StringNodeNamer("bogus", false, false));

		Object found = mainTree.nearestKnownAncestor(rootPhylogeny, "bbba");

		assert found == "b";
		}

	@Test
	public void intersectionTreeFromTwoLeafSetsSkipsInternalNodes() throws Exception
		{
		T mainTree = tif.createInstance();

		RootedPhylogeny extractedTree =
				mainTree.extractIntersectionTree(DSCollectionUtils.setOf("aaaa", "baa", "bba", "bbba"),
				                                 DSCollectionUtils.setOf("bab", "bba", "bbba", "ca", "cb"), null);

		assert extractedTree.getUniqueIdToNodeMap().size() == 6;
		assert extractedTree.getLeaves().size() == 3;
		assert CollectionUtils
				.isEqualCollection(extractedTree.getLeafValues(), DSCollectionUtils.setOf("ba", "bba", "bbba"));
		assert CollectionUtils.isEqualCollection(extractedTree.getNodeValues(),
		                                         DSCollectionUtils.setOf("ba", "bba", "bbba", "bb", "b", "root"));
		}

	@Test
	public void providesWeightMixedTree() throws Exception
		{
		RootedPhylogeny<String> baseTree = tif.createInstance();

		RootedPhylogeny<String> tree1 = baseTree.extractTreeWithLeafIDs(
				DSCollectionUtils.setOf("aaaa", "ab", "baa", "bab", "bba", "bbba", "ca", "cb"), false, false,
				AbstractRootedPhylogeny.MutualExclusionResolutionMode.EXCEPTION);
		tree1.randomizeLeafWeights(new UniformDistribution(0, 1));

		RootedPhylogeny<String> tree2 = baseTree.extractTreeWithLeafIDs(
				DSCollectionUtils.setOf("aaaa", "ab", "baa", "bab", "bba", "bbba", "ca", "cb"), false, false,
				AbstractRootedPhylogeny.MutualExclusionResolutionMode.EXCEPTION);
		tree2.randomizeLeafWeights(new UniformDistribution(0, 1));

		RootedPhylogeny<String> tree3 = tree1.mixWith(tree2, 0.1);

		assert MathUtils.equalWithinFPError(tree3.getNode("a").getWeight(), tree1.getNode("a").getWeight() * 0.1
		                                                                    + tree2.getNode("a").getWeight() * 0.9);

		assert MathUtils.equalWithinFPError(tree3.getNode("bb").getWeight(), tree1.getNode("bb").getWeight() * 0.1
		                                                                     + tree2.getNode("bb").getWeight() * 0.9);

		assert MathUtils.equalWithinFPError(tree3.getNode("ca").getWeight(), tree1.getNode("ca").getWeight() * 0.1
		                                                                     + tree2.getNode("ca").getWeight() * 0.9);
		}

	@Test(expectedExceptions = TreeException.class)
	public void weightMixingRequiresBaseTree() throws Exception
		{
		RootedPhylogeny<Serializable> tree1 = tif.createInstance();
		tree1.randomizeLeafWeights(new UniformDistribution(0, 1));

		RootedPhylogeny<Serializable> tree2 = tif.createInstance();
		tree2.randomizeLeafWeights(new UniformDistribution(0, 1));

		RootedPhylogeny<Serializable> tree3 = tree1.mixWith(tree2, 0.1);
		}

	@Test(expectedExceptions = TreeException.class)
	public void weightMixingRequiresSameBaseTree() throws Exception
		{
		RootedPhylogeny<Serializable> tree1 = tif.createInstance();
		tree1.randomizeLeafWeights(new UniformDistribution(0, 1));
		tree1 = tree1.extractTreeWithLeafIDs(tree1.getLeafValues(), false, false);

		RootedPhylogeny<Serializable> tree2 = tif.createInstance();
		tree2.randomizeLeafWeights(new UniformDistribution(0, 1));
		tree2 = tree1.extractTreeWithLeafIDs(tree2.getLeafValues(), false, false);

		RootedPhylogeny<Serializable> tree3 = tree1.mixWith(tree2, 0.1);
		}

	@Test
	public void copiesWeightsFromAnotherTreeWithPseudocounts() throws Exception
		{
		T mainTree = tif.createInstance();

		Multiset<String> m = HashMultiset.create();
		m.add("aaaa", 4);
		m.add("ab", 16);
		m.add("baa", 10);
		//m.add("bab", 10);
		m.add("bba", 20);
		m.add("bbba", 8);
		m.add("ca", 42);
		//m.add("cb", 0);

		mainTree.setLeafWeights(m);

		RootedPhylogeny<Serializable> tree2 = tif.createInstance();

		tree2.smoothWeightsFrom(mainTree, .01);

		assert MathUtils.equalWithinFPError(tree2.getNode("a").getWeight(), (0.2 + 0.02) / 1.08);
		assert MathUtils.equalWithinFPError(tree2.getNode("b").getWeight(), (0.38 + 0.04) / 1.08);
		assert MathUtils.equalWithinFPError(tree2.getNode("bab").getWeight(), (0.0 + 0.01) / 1.08);
		assert MathUtils.equalWithinFPError(tree2.getNode("c").getWeight(), (0.42 + 0.02) / 1.08);
		assert MathUtils.equalWithinFPError(tree2.getNode("cb").getWeight(), (0.0 + 0.01) / 1.08);
		}

	@Test
	public void setsAndNormalizesLeafWeightsFromMultiset() throws Exception
		{
		T mainTree = tif.createInstance();

		Multiset<String> m = HashMultiset.create();
		m.add("aaaa", 4);
		m.add("ab", 16);
		m.add("baa", 10);
		//m.add("bab", 10);
		m.add("bba", 20);
		m.add("bbba", 8);
		m.add("ca", 42);
		//m.add("cb", 0);

		mainTree.setLeafWeights(m);

		assert mainTree.getNode("a").getWeight() == 0.2;
		assert mainTree.getNode("b").getWeight() == 0.38;
		assert mainTree.getNode("bab").getWeight() == 0;
		assert mainTree.getNode("c").getWeight() == 0.42;
		assert mainTree.getNode("cb").getWeight() == 0;
		}
	}
