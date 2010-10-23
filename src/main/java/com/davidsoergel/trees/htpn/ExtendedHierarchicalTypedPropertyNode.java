package com.davidsoergel.trees.htpn;

import com.davidsoergel.dsutils.increment.Incrementor;

import java.util.SortedSet;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface ExtendedHierarchicalTypedPropertyNode<K extends Comparable, V, H extends ExtendedHierarchicalTypedPropertyNode<K, V, H>>
		extends HierarchicalTypedPropertyNode<K, V, H>
	{
	//Map<K, H> getChildrenByName();

	V getDefaultValue();

	String getHelpmessage();

	SortedSet<Class> getPluginOptions(Incrementor incrementor);

	void setDefaultAndNullable(V defaultValue, boolean isNullable) throws HierarchicalPropertyNodeException;

	boolean useDefaultValueIfNeeded() throws HierarchicalPropertyNodeException;

	void defaultValueSanityChecks() throws HierarchicalPropertyNodeException;

	void copyFrom(ExtendedHierarchicalTypedPropertyNode<K, V, ?> extendedTree);

	boolean isEditable();

	boolean isNullable();

	boolean isObsolete();

	boolean isChanged();

//	boolean isDefault();

//	boolean isInherited();

	void setChanged(boolean changed);

	//void setDefaultValue(V defaultValue) throws HierarchicalPropertyNodeException;

	void setEditable(boolean editable);

	void setHelpmessage(String helpmessage);

//	void setNullable(boolean nullable) throws HierarchicalPropertyNodeException;

	void setObsolete(boolean b);

	//void setParent(HierarchicalTypedPropertyNode<S, T> key);

	void removeObsoletes();

	void appendToStringBuffer(StringBuffer sb, int i);

	void obsoleteChildren();

	H getChild(K key);

//	void setNonDefault();

//	K getKeyForChild(ExtendedHierarchicalTypedPropertyNode<K, V, ?> child);
	}
