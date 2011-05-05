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
