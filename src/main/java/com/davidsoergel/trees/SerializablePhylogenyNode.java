/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface SerializablePhylogenyNode<T extends Serializable> extends PhylogenyNode<T>, Serializable
	{
	SerializableRootedPhylogeny<T> asRootedPhylogeny();

	@NotNull
	List<? extends SerializablePhylogenyNode<T>> getChildren(); // throws NoSuchNodeException; //
	}
