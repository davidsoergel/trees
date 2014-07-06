/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees.dhtpn;

import com.davidsoergel.trees.htpn.SerializableHierarchicalTypedPropertyNode;

import java.io.Serializable;


/**
 * Represents a detached ParameterSet that exists only in memory and has no Hibernate dependencies, making it suitable
 * for serialization to and from the client.  Also, because the internal dual-hierarchy structure can get desperately
 * confusing, we facade some methods with more sensible names.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface SerializableDoubleHierarchicalTypedProperties<S extends SerializableDoubleHierarchicalTypedProperties<S>>
		extends
		DoubleHierarchicalTypedProperties<Integer, String, String, Serializable, S, SerializableHierarchicalTypedPropertyNode<String, Serializable>>,
		Serializable


//public interface BasicDoubleHierarchicalTypedProperties extends
		//                                                      DoubleHierarchicalTypedProperties<Integer, String, String, Serializable, BasicDoubleHierarchicalTypedProperties, BasicHierarchicalTypedPropertyNode<String, Serializable>>,
		//                                                    Serializable
	{
//	@NotNull
//	Collection<? extends S> getChildren();
//	Collection<? extends DoubleHierarchicalTypedProperties<Integer, String, String, Serializable, S, SerializableHierarchicalTypedPropertyNode<String, Serializable>>> getChildren();
	}
