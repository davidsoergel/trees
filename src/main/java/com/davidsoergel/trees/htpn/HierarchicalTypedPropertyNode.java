/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees.htpn;

import com.davidsoergel.dsutils.collections.OrderedPair;
import com.davidsoergel.trees.HierarchyNode;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * Describes a node in a tree containing a key-value pair (at this node) and a set of children.  The types of both the
 * key and the value are generic.  The children can be retrieved by key, Map-style.  Also stores various flags (e.g.,
 * nullable, editable, etc.) to support the @Property framework.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: HierarchicalTypedPropertyNode.java 250 2009-07-29 01:05:06Z soergel $
 */
public interface HierarchicalTypedPropertyNode<K extends Comparable, V, H extends HierarchicalTypedPropertyNode<K, V, H>>
		extends Comparable,
		        HierarchyNode<OrderedPair<K, V>, H> //, Serializable//extends HierarchyNode<Map<String, Object>>, Map<String, Object>//extends TypedProperties
	{
	//H init(H parent, K childKey, V childValue, Type aClass); //throws HierarchicalPropertyNodeException;

	void copyFrom(HierarchicalTypedPropertyNode<K, V, ?> extendedTree);//throws HierarchicalPropertyNodeException;

//	void copyChildrenFrom(HierarchicalTypedPropertyNode<K, V, ?> inheritedNode)
//			throws HierarchicalPropertyNodeException;

	void updateTypeIfNeeded(V v);


	public enum PropertyConsumerFlags
		{
			INHERITED
		}
	// -------------------------- OTHER METHODS --------------------------

	//void addChild(HierarchicalTypedPropertyNode<S, T> n);

	void addChild(K key, V value);

	void addChild(K[] keyPath, V value);

	void addChild(K[] keyPath, K leafKey, V value);


	//void addChild(S[] keyPath, T value);

	void collectDescendants(Map<K[], HierarchicalTypedPropertyNode<K, V, H>> result);

	Map<K[], HierarchicalTypedPropertyNode<K, V, H>> getAllDescendants();

	HierarchicalTypedPropertyNode<K, V, H> getChild(K key);

	Collection<H> getChildNodes();

	Map<K, H> getChildrenByName();
	//String getDefaultValueString();

	Object[] getEnumOptions();

	// V getInheritedValue(K name);

	HierarchicalTypedPropertyNode<K, V, H> getInheritedNode(K name);

	K getKey();

	H getOrCreateDescendant(K[] keys);

	H getOrCreateChild(K childKey);

	H updateOrCreateChild(@NotNull K childKey, V childValue);

	H getParent();


	/**
	 * Returns the runtime type of the value contained in this node.  Naturally this must be assignable to the generic
	 * type
	 *
	 * @return
	 */
	Type getType();

	V getValue();

	void inheritValueIfNeeded() throws HierarchicalPropertyNodeException;


	//boolean isNamesSubConsumer();

	boolean isClassBoundPlugin();

	//** All PluginMap functionality needs revisiting
	//boolean isPluginMap();


	K[] getKeyPath();

	void removeChild(K key);


	void setKey(K newKey);


	void setType(Type type);

	//int compareTo(T o);


	//	public void updateValuesFromDefaults() throws PropertyConsumerNodeException;

	//	void updateDynamicConsumerNodesFromDefaults() throws PropertyConsumerNodeException;

	void setValue(V value);// throws HierarchicalPropertyNodeException;

	/**
	 * Test whether this node has had any fields set yet.  Returns true only if the node is completely empty of interesting
	 * contents.
	 *
	 * @return
	 */
	//	boolean isNew();

	//void sanityCheck();

//	void putClassBoundPluginSingleton(Object instance);

//	Object getClassBoundPluginSingleton() throws HierarchicalPropertyNodeException;


	/**
	 * Creates a new child node of the appropriate type.  Note that the payload must be provided up front; this helps the
	 * child be more immutable than it otherwise would be (e.g. newChild() followed by setPayload()).
	 *
	 * @return the new child node
	 */
//	H newChildCopyingSubtree(HierarchicalTypedPropertyNode<K,V,H> copyFrom);
	}
