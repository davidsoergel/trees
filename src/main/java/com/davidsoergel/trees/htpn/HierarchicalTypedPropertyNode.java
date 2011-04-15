/*
 * Copyright (c) 2001-2008 David Soergel
 * 418 Richmond St., El Cerrito, CA  94530
 * dev@davidsoergel.com
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the author nor the names of any contributors may
 *       be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.davidsoergel.trees.htpn;

import com.davidsoergel.dsutils.collections.OrderedPair;
import com.davidsoergel.trees.HierarchyNode;

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

	H updateOrCreateChild(K childKey, V childValue);

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
