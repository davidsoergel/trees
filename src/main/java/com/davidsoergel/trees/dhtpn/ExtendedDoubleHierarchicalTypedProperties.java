package com.davidsoergel.trees.dhtpn;

import com.davidsoergel.trees.htpn.ExtendedHierarchicalTypedPropertyNode;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;


/**
 * Represents a detached ParameterSet that exists only in memory and has no Hibernate dependencies, making it suitable
 * for serialization to and from the client.  Also, because the internal dual-hierarchy structure can get desperately
 * confusing, we facade some methods with more sensible names.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface ExtendedDoubleHierarchicalTypedProperties<S extends ExtendedDoubleHierarchicalTypedProperties<S, H>, H extends ExtendedHierarchicalTypedPropertyNode<String, Serializable, H>>
		extends DoubleHierarchicalTypedProperties<Integer, String, String, Serializable, S, H>, Serializable


//public interface BasicDoubleHierarchicalTypedProperties extends
		//                                                      DoubleHierarchicalTypedProperties<Integer, String, String, Serializable, BasicDoubleHierarchicalTypedProperties, BasicHierarchicalTypedPropertyNode<String, Serializable>>,
		//                                                    Serializable
	{
	@NotNull
	Collection<? extends S> getChildren();
	}
