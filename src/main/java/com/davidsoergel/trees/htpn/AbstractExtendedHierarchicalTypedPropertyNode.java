package com.davidsoergel.trees.htpn;

import com.davidsoergel.dsutils.DSArrayUtils;
import com.davidsoergel.dsutils.DSClassUtils;
import com.davidsoergel.dsutils.GenericFactory;
import com.davidsoergel.dsutils.PluginManager;
import com.davidsoergel.dsutils.TypeUtils;
import com.davidsoergel.dsutils.collections.OrderedPair;
import com.davidsoergel.dsutils.increment.Incrementor;
import com.davidsoergel.dsutils.stringmapper.StringMapperException;
import com.davidsoergel.dsutils.stringmapper.TypedValueStringMapper;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public abstract class AbstractExtendedHierarchicalTypedPropertyNode<K extends Comparable, V, H extends ExtendedHierarchicalTypedPropertyNode<K, V, H>>
		extends AbstractHierarchicalTypedPropertyNode<K, V, H> implements ExtendedHierarchicalTypedPropertyNode<K, V, H>
	{
	private static final Logger logger = Logger.getLogger(AbstractExtendedHierarchicalTypedPropertyNode.class);

	protected V defaultValue;

	private String helpmessage;
	//private String defaultValueString;

	//private Set<String> contextNames;
	//private boolean trackContextName;

	//private boolean namesSubConsumer;
	private boolean isNullable = true;
	// support assigning variables that don't have @Property, e.g. when inherited from a non-Jandified package

	// allow for locking the value
	protected boolean editable = true;

	//private HierarchicalTypedPropertyNode<S, T> parent;

	private boolean obsolete = false;
	private boolean changed = false;


	public void copyFrom(
			final ExtendedHierarchicalTypedPropertyNode<K, V, ?> node) //throws HierarchicalPropertyNodeException
	{
	//setKey(node.getKey());
	//setValue(node.getValue());
	setType(node.getType());

	try
		{
		setDefaultAndNullable(node.getDefaultValue(), node.isNullable());  // applies inheritance and default if needed
		}
	catch (HierarchicalPropertyNodeException e)
		{
		logger.error("Error", e);
		throw new Error(e);
		}

	helpmessage = node.getHelpmessage();
	editable = node.isEditable();
	obsolete = node.isObsolete();
	changed = node.isChanged();
//		isDefault = node.isDefault();

	clearChildren();

	for (HierarchicalTypedPropertyNode<K, V, ?> child : node.getChildNodes()) //getChildren())
		{
		newChild(child.getPayload()).copyFrom(child);
		}
	}

/*	public ExtendedHierarchicalTypedPropertyNodeImpl<K, V> init(ExtendedHierarchicalTypedPropertyNodeImpl<K, V> parent,
																K key, V value, Type type, String helpmessage)
			throws HierarchicalPropertyNodeException
		{
		super.init(parent, key, value, type);
		this.helpmessage = helpmessage;
		return this;
		}
*/

	/*	public ExtendedHierarchicalTypedPropertyNodeImpl<K, V> init(ExtendedHierarchicalTypedPropertyNodeImpl<K, V> parent,
																K key, V value, Type type, String helpmessage,
																V defaultValue,
																//Set<String> contextNames,
																// boolean trackContextName,
																//boolean namesSubConsumer,
																boolean isNullable)
			throws HierarchicalPropertyNodeException
		{
		init(parent, key, value, type, helpmessage);
		//	this.defaultValueString = defaultValueString;
	*/

	public void setDefaultAndNullable(V defaultValue, boolean isNullable) throws HierarchicalPropertyNodeException
		{
		if (type == null)
			{
			// this is a root node of unknown ROOTCONTEXT
			// or it's an obsolete entry from the database
			this.isNullable = true;
			}
		else
			{
			//setDefaultValue(defaultValue);
			//setNullable(isNullable);// also sets the value from the default, if needed
			this.defaultValue = defaultValue;
			this.isNullable = isNullable;

			// inheritValueIfNeeded();

			// don't do these here; the inheritance/defaults pass will catch them
			//	useDefaultValueIfNeeded();
			//	defaultValueSanityChecks();
			}
		}

	// --------------------- GETTER / SETTER METHODS ---------------------
	/*
	public Map<K, H> getChildrenByName()
		{
		return childrenByName;
		}*/

	public V getDefaultValue()
		{
		return defaultValue;
		}

/*	public String getDefaultValueString()
		{
		return defaultValueString;
		}*/

	public String getHelpmessage()
		{
		return helpmessage;
		}

	/*	public boolean isNamesSubConsumer()
		 {
		 return namesSubConsumer;
		 }
 */

	public void setHelpmessage(String helpmessage)
		{
		this.helpmessage = helpmessage;
		}

	// BAD editable is in place only for the sake of jandyApp2Tier

	public boolean isEditable()
		{
		return editable;
		}

	public void setEditable(boolean editable)
		{
		this.editable = editable;
		}


	public void setObsolete(boolean obsolete)
		{
		this.obsolete = obsolete;
		//** obsoleteChildren();
		}

	public void obsoleteChildren()
		{
		for (H child : childrenByName.values())
			{
			child.setObsolete(true);
			//	child.obsoleteChildren();
			}
		}

	public void setChanged(boolean changed)
		{
		this.changed = changed;
		for (ExtendedHierarchicalTypedPropertyNode<K, V, H> child : childrenByName.values())
			{
			child.setChanged(changed);
			}
		}


	public SortedSet<Class> getPluginOptions(Incrementor incrementor)
		{
		//try
		V value = getValue();

		if (!(value == null || value instanceof GenericFactory || value instanceof Class))
			{
			logger.warn("Can't get plugin options for a value " + value + " of type " + value.getClass());
			}
		//Class c = theField.getType();
		//Type genericType = theField.getGenericType();


// if this is a GenericFactory, then we need the parameter type

		Type thePluginType = type;
		if (TypeUtils.isAssignableFrom(GenericFactory.class, type))
			{
			if (type instanceof Class)
				{
				// it's a type that extends GenericFactory; just leave it alone then
				thePluginType = type;
				}
			else
				{
				thePluginType = ((ParameterizedType) type).getActualTypeArguments()[0];
				}
			}


		java.util.SortedSet<Class> result = new TreeSet<Class>(new Comparator<Class>()
		{
		public int compare(Class o1, Class o2)
			{
			if (o1 == null)
				{
				return -1;
				}
			else if (o2 == null)
				{
				return 1;
				}
			else
				{
				return o1.getSimpleName().compareTo(o2.getSimpleName());
				}
			}
		});
		if (isNullable)
			{
			result.add(null);
			}
		try
			{
			PluginManager.registerPluginsFromDefaultPackages(thePluginType, incrementor);
			}
		catch (IOException e)
			{
			logger.error("Error", e);
			throw new Error(e);
			}

		result.addAll(PluginManager.getPlugins(thePluginType));
		return result;
		//			}
		//		catch (PluginException e)
		//			{
		//			return null;
		//			}
		}


	public boolean isNullable()
		{
		return isNullable;
		}

	public boolean isObsolete()
		{
		return obsolete || (parent != null && parent.isObsolete());
		}

	public boolean isChanged()
		{
		return changed;
		}


	protected boolean valueEquals(V newValue)
		{
		final V value = getValue();
		if (value == null && newValue == null)
			{
			return true;
			}
		else
			{
			return value != null && value.equals(newValue);
			}
		}

	/**
	 * Set the value of this node.  If this node expects a plugin and the value is a Class (or a String naming a Class),
	 * then create the children array if necessary and populate it with defaults.  Act similarly for a PluginMap.
	 * <p/>
	 * If the children array already exists, and the value was previously null or the same class, then we merge the
	 * defaults non-destructively.
	 *
	 * @param newValue
	 * @throws HierarchicalPropertyNodeException
	 *
	 */
	public void setValue(V newValue) //throws HierarchicalPropertyNodeException
		{
		// if this node was set to the default (and all of its descendants were too) then ditch the descendants
/*		if (isDefault)
			{
			childrenByName.clear();
			}
		setNonDefault();// even if we're explicitly setting what the default value was
*/		/*	if (!editable)
		   {
		   throw new HierarchicalPropertyNodeException("Node is locked: " + this);
		   }
		   */

		// if the value hasn't changed, do nothing

		if (!valueEquals(newValue))
			{
			// setChanged must happen at the end for the sake of new plugin children

			obsoleteChildren();  // save current values in obsolete state

			payload = new OrderedPair<K, V>(getKey(), newValue);
			//	super.setValue(newValue);

			final V value = getValue();
			updateTypeIfNeeded(value);
			//	type = value.getClass();

			/*	try
			   {
			   updatePluginChildNodes();
			   }
		   catch (HierarchicalPropertyNodeException e)
			   {
			   logger.error("Error", e);
			   throw new Error(e);
			   }*/

			setChanged(true);
			}
		}

/*	public void setNonDefault()
		{
		isDefault = false;
		H p = getParent();
		if (p != null)
			{
			p.setNonDefault();
			}
		}
		*/
/*
	protected void setValueForce(V value) //throws HierarchicalPropertyNodeException
		{
		payload = new OrderedPair<K, V>(getKey(), value);

		// REVIEW possible hack re setting PluginMap values on an HTPN

		obsoleteChildren();

		//	clearChildren();
		}
*/

	/*	public void setDefaultValue(V defaultValue) throws HierarchicalPropertyNodeException
			 {
			 this.defaultValue = defaultValue;
			 }
	 */


	public void defaultValueSanityChecks() throws HierarchicalPropertyNodeException
		{

		// some sanity checks
		if (defaultValue == null)
			{
			if (!isNullable)
				{
				logger.error("Node is not nullable and has no default value: " + this);
				throw new HierarchicalPropertyNodeException("Node is not nullable and has no default value: " + this);

				//throw new HierarchicalPropertyNodeException("Node is not nullable and has no default value.");

				}
			}
		else
			{

			/*	else if (TypeUtils.isAssignableFrom(PluginDoubleMap.class, type))
							   {
							   if (!defaultValue.getClass().equals(PluginDoubleMap.class))
								   {
								   throw new HierarchicalPropertyNodeException(
										   "Node is a PluginMap, but the default value is not a PluginMap");
								   }
							   }*/

//** All PluginMap functionality needs revisiting
			/*
				   else if (defaultValue.getClass().equals(PluginMap.class))
					   {
					   // if the default value is a PluginMap, then we can leave it alone
					   }*/
			if (defaultValue.getClass().equals(Class.class))
				{
				// if the default value is a Class, then this node represents a plugin, so we can leave the value alone
				}
			else if (!(type instanceof Class))// && ! this.defaultValue.getClass().equals(Class.class) implicit
				{
				// type must be ParameterizedType
				throw new HierarchicalPropertyNodeException(
						"Node has generic type, but the default value is not a plugin class");
				}
			else if (type instanceof Class && ((Class) type).isPrimitive() && DSClassUtils
					.isAssignable(defaultValue.getClass(), DSClassUtils.primitiveToWrapper((Class) type)))
				{
				// non-generic case; allows for primitives
				}
			else if (!TypeUtils.isAssignableFrom(type, defaultValue.getClass()))
				{
				// does not allow primitives, but does deal with generics, though that doesn't apply here anyway... hmmm.
				throw new HierarchicalPropertyNodeException(
						"Can't assign a default value of type " + defaultValue.getClass() + " to a node requiring "
						+ type);
				}
			}
		}

	/**
	 * Sets the nullable status of this node, and sets the value to the defaultValue if necessary.  If a node is nullable,
	 * and is null, then leave it that way; don't force the default value
	 * <p/>
	 * //@param nullable
	 *
	 * @throws HierarchicalPropertyNodeException
	 *
	 */
	/*public void setNullable(boolean nullable) throws HierarchicalPropertyNodeException
		{
		isNullable = nullable;
		}
*/
/*	protected boolean isDefault;

	public boolean isDefault()
		{
		return isDefault;
		}
*/
	public boolean useDefaultValueIfNeeded() throws HierarchicalPropertyNodeException
		{
		if (getValue() == null && !isNullable)
			{
			setValue(defaultValue);
			return true;
//			isDefault = true;
			}
		return false;
		}


	public H updateOrCreateChild(@NotNull K childKey, V childValue)
		{
		H child = getChild(childKey);
		if (child == null)
			{
			// BAD hack: payload should be final?
			child = newChild(new OrderedPair<K, V>(childKey, childValue));
			/*
			  if (keys.size() == 0)
				  {
				  // this is the leaf node, so we give it the requested type
				  child = new HierarchicalTypedPropertyNodeImpl<S, T>()
						  .init(this, childKey, type, null, null, true);
				  }
			  else
				  {
				  // this is an intermediate node, so we give it type Class
				  child = new HierarchicalTypedPropertyNodeImpl<S, T>()
						  .init(this, childKey, Class.class, null, null, true);
				  }*/

			child.updateTypeIfNeeded(childValue);
			// if the node didn't already exist, then the referenced field is no longer in the class that was parsed
			child.setObsolete(true);
			child.obsoleteChildren();

			//addChild(child); // implicit in child creation
			}
		return child;
		}


	public void removeObsoletes()
		{
		HashSet<H> set = new HashSet<H>(getChildNodes());
		for (H n : set)
			{
			if (n.isObsolete())
				{
				removeChild(n.getKey());
				}
			else
				{
				n.removeObsoletes();
				}
			}
		}

	public void appendToStringBuffer(StringBuffer sb, int indentLevel)
		{
		for (int i = 0; i < indentLevel; i++)
			{
			sb.append("\t");
			}
		sb.append(getKey()).append(" = ").append(getValue()).append("\n");
		indentLevel++;

		for (ExtendedHierarchicalTypedPropertyNode child : childrenByName.values())
			{
			child.appendToStringBuffer(sb, indentLevel);
			}
		}

	public int getTotalRuns()
		{
		throw new NotImplementedException();
		}


	public void setValueFromString(String string) throws HierarchicalPropertyNodeException
		{

		try
			{
			setValue((V) TypedValueStringMapper.get(getType()).parse(string));
			//	setValue(SerializableValueOrRangeFactory.newTransientFromString(getType(), string));
			}
		catch (StringMapperException e)
			{
			logger.error("Error", e);
			throw new HierarchicalPropertyNodeException(e);
			}
		//		}
		}

	public V getValueForDescendant(final K[] keyList)
		{
		final ExtendedHierarchicalTypedPropertyNode<K, V, H> desc = getDescendant(keyList);
		return desc == null ? null : desc.getValue();
		}

	public ExtendedHierarchicalTypedPropertyNode<K, V, H> getDescendant(final K[] keyList)
		{
		ExtendedHierarchicalTypedPropertyNode<K, V, H> trav = this;

		for (K k : keyList)
			{
			trav = trav.getChild(k);
			if (trav == null)
				{
				return null;
				}
			}
		return trav;

		// recursive method requires copying/modifying the kryList, yuck
		/*
		if (keyList.isEmpty())
			{
			return null;
			}

		K firstKey = keyList.remove(0);

		if (keyList.isEmpty() && firstKey.equals(getKey()))
			{
			return getValue();
			}
		return getC
		return getPayload().
		*/
		}

	public boolean isPlottable()
		{
		final V o = getValue();
		return o instanceof Number || DSArrayUtils.isNumberArray(o) || DSArrayUtils.isPrimitiveArray(o);
		}
	}
