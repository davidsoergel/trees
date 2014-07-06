/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees.dhtpn;

/**
 * A container for all of the results produced by a JobThread.  The outer hierarchy layer corresponds to the
 * ParameterSet hierarchy. The idea is that a single JobThread may produce a number of ParameterSet nodes, each
 * corresponding to a replicate experiment, or to a different value of some parameter that is being swept over, etc. The
 * inner hierarchy layer corresponds to the set of key-value pairs within a single ParameterSet.  The keys may be
 * hierarchically organized for symmetry with the input side, but in practice that will rarely be necessary.
 * <p/>
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface ResultCollector extends SerializableDoubleHierarchicalTypedProperties<ResultCollector>
		//                        DoubleHierarchicalTypedProperties<Integer, String, String, Serializable, ResultCollector, SerializableHierarchicalTypedPropertyNode<String, Serializable>>
		//BasicDoubleHierarchicalTypedProperties<ResultCollector>
	{
	void commit();
	}
