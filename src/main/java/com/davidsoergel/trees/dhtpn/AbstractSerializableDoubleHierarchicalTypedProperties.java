/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees.dhtpn;

import com.davidsoergel.trees.htpn.SerializableHierarchicalTypedPropertyNode;

import java.io.Serializable;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public abstract class AbstractSerializableDoubleHierarchicalTypedProperties<S extends SerializableDoubleHierarchicalTypedProperties<S>>
		extends
		AbstractDoubleHierarchicalTypedProperties<Integer, String, String, Serializable, S, SerializableHierarchicalTypedPropertyNode<String, Serializable>>
		implements SerializableDoubleHierarchicalTypedProperties<S>
	{
	}
