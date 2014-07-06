/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees;

import java.io.Serializable;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface SerializableRootedPhylogeny<T extends Serializable> extends RootedPhylogeny<T>, Serializable
	{

	SerializablePhylogenyNode<T> getNode(T name) throws NoSuchNodeException;

	SerializablePhylogenyNode<T> getFirstBranchingNode();

	//Map<T, SerializablePhylogenyNode<T>> getUniqueIdToNodeMap();  //** Map<T, ? extends PhylogenyNode<T>> ??
	}
