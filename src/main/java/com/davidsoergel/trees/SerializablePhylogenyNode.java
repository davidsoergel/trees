package com.davidsoergel.trees;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface SerializablePhylogenyNode<T extends Serializable> extends PhylogenyNode<T>, Serializable
	{
	SerializableRootedPhylogeny<T> asRootedPhylogeny();

	List<? extends SerializablePhylogenyNode<T>> getChildren(); // throws NoSuchNodeException; //
	}
