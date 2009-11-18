package com.davidsoergel.trees.htpn;

import com.davidsoergel.dsutils.collections.OrderedPair;
import com.davidsoergel.dsutils.collections.SerializableOrderedPair;

import java.io.Serializable;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class SerializableHierarchicalTypedPropertyNode<K extends Comparable<K> & Serializable, V extends Serializable>

		extends AbstractHierarchicalTypedPropertyNode<K, V, SerializableHierarchicalTypedPropertyNode<K, V>>
	{
	/*	public BasicHierarchicalTypedPropertyNode<S, T> newChild()
		 {
		 BasicHierarchicalTypedPropertyNode<S, T> result = new SerializableHierarchicalTypedPropertyNodeImpl<S, T>();
		 //children.add(result);  // setParent calls registerChild
		 result.setParent(this);
		 return result;
		 }
 */
	public SerializableHierarchicalTypedPropertyNode<K, V> newChild(final OrderedPair<K, V> payload)
		{
		SerializableHierarchicalTypedPropertyNode<K, V> result = new SerializableHierarchicalTypedPropertyNode<K, V>();
		//children.add(result);  // setParent calls registerChild
		result.setPayload(payload);
		result.setParent(this);
		return result;
		}


	/**
	 * Set the value of this node.  If this node expects a plugin and the value is a Class (or a String naming a Class),
	 * then create the children array if necessary and populate it with defaults.  Act similarly for a PluginMap.
	 * <p/>
	 * If the children array already exists, and the value was previously null or the same class, then we merge the
	 * defaults non-destructively.
	 *
	 * @param value
	 * @throws HierarchicalPropertyNodeException
	 *
	 */
	public void setValue(V value) //throws HierarchicalPropertyNodeException
		{

		/*setValueForce(value);
		}

	protected void setValueForce(V value) //throws HierarchicalPropertyNodeException
		{*/
		/*	boolean destructive = true;
		  T currentValue = getValue();
		  if (currentValue == null || currentValue == value)
			  {
			  destructive = false;
			  }
  */
		// do any required string parsing, including class names
		/*	if (value instanceof String)
		   {
		   value = parseValueString((String) value);
		   }*/
		//this.value = value;
		payload = new SerializableOrderedPair<K, V>(getKey(), value);

		// REVIEW possible hack re setting PluginMap values on an HTPN


//** All PluginMap functionality needs revisiting
		/*
		if (isPluginMap() && value != null)// value instanceof PluginMap
			{
			throw new HierarchicalPropertyNodeException(
					"Can't set a plugin value on a regular HTPN; need to use the StringNamed version");
			}
		else */
		if (isClassBoundPlugin() && value != null)
			{
			throw new Error( //HierarchicalPropertyNodeException(
			                 "Can't set a plugin value on a regular HTPN; need to use the StringNamed version");
			}
		else
			{
			clearChildren();
			}

		// if the value is a String giving a class name that is not currently loadable, we may yet add children later
		}


	@Override
	public void registerChild(final SerializableHierarchicalTypedPropertyNode<K, V> a)
		{
		//assert a instanceof SerializableHierarchicalTypedPropertyNode;
		super.registerChild(a);
		}

	@Override
	public void unregisterChild(final SerializableHierarchicalTypedPropertyNode<K, V> a)
		{
		//assert a instanceof SerializableHierarchicalTypedPropertyNode;
		super.unregisterChild(a);
		}
	}
