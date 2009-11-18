package com.davidsoergel.trees.dhtpn;

import com.davidsoergel.trees.HierarchyNode;
import com.davidsoergel.trees.htpn.HierarchicalTypedPropertyNode;

/**
 * A data structure consisting of an outer tree, each node of which contains an inner tree.
 * <p/>
 * The outer tree has two IDs of types I and J.
 * <p/>
 * The inner tree is of type H.  Each node of the inner tree contains a key-value pair of types K and V.
 * <p/>
 * The type C is a concrete type that implements this interface, indicating what types can be legally added as
 * children.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface DoubleHierarchicalTypedProperties<I, J, K extends Comparable, V, C extends DoubleHierarchicalTypedProperties<I, J, K, V, C, H>, H extends HierarchicalTypedPropertyNode<K, V, H>>

		extends HierarchyNode<HierarchicalTypedPropertyNode<K, V, H>, C>
	{
	I getId1();

	J getId2();

	void setId1(I id1);

	void setId2(J id2);

	H newPayload();

	int countInnerValues();
	}
