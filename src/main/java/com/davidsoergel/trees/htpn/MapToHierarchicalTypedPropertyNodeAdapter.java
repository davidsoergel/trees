/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees.htpn;

import com.davidsoergel.dsutils.DSStringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: MapToHierarchicalTypedPropertyNodeAdapter.java 220 2008-09-23 02:20:59Z soergel $
 */

public class MapToHierarchicalTypedPropertyNodeAdapter
	{
	// ------------------------------ FIELDS ------------------------------

	private static final String PATHSEPARATOR = ".";
	private static final String PATHSEPARATORREGEX = "\\.";


	// -------------------------- STATIC METHODS --------------------------

	/**
	 * Set the values of the given HierarchicalTypedPropertyNode and its children according to this ParameterSet.  We do it
	 * this way so that the help messages and so on that are parsed from the Class are present.  If the ParameterSet
	 * contains field names that are obsolete, we still add them but mark them as such.
	 * <p/>
	 * Note that the hierarchical structure of ParameterSets represents overriding and child containment, which is
	 * completely different from the hierarchical structure of HierarchicalTypedPropertyNodes.  The latter structure should
	 * match that given by the ParameterKey hierarchy.
	 *
	 * @param rootProperties
	 */
	public static void mergeInto(HierarchicalTypedPropertyNode rootProperties, Map<String, Serializable> theMap)
			throws HierarchicalPropertyNodeException
		{
		for (String key : theMap.keySet())
			{
			String[] splitKeyArray = key.split(PATHSEPARATORREGEX);
			//List<String> splitKey = new LinkedList<String>();
			//splitKey.addAll(Arrays.asList(splitKeyArray));
			Object value = theMap.get(key);
			rootProperties.getOrCreateDescendant(splitKeyArray).setValue(value);
			}
		}

	public static Map<String, Object> asMap(HierarchicalTypedPropertyNode rootProperties)
		{
		Map<String, Object> result = new HashMap<String, Object>();

		Map<List<String>, HierarchicalTypedPropertyNode> propertyMap = rootProperties.getAllDescendants();

		for (List<String> keyList : propertyMap.keySet())
			{
			result.put(DSStringUtils.join(keyList, PATHSEPARATOR), propertyMap.get(keyList));
			}
		return result;
		}

	/*String keyPrefix;
	 Map<String, Object> backingMap;

	 public MapToHierarchicalTypedPropertyNodeAdapter(Map<String, Object> backingMap)
		 {
		 this("", backingMap);
		 }

	 public MapToHierarchicalTypedPropertyNodeAdapter(String keyPrefix, Map<String, Object> backingMap)
		 {
		 this.keyPrefix = keyPrefix;
		 this.backingMap = backingMap;
		 }

	 public Type getType()
		 {
		 Object o = backingMap.get(keyPrefix);
		 return o == null ? null : o.getClass();
		 }

	 public Object getValue()
		 {
		 return backingMap.get(keyPrefix);
		 }

	 public void addChild(HierarchicalTypedPropertyNode n)
		 {
		 //To change body of implemented methods use File | Settings | File Templates.
		 }

	 public HierarchicalTypedPropertyNode getChild(String key)
		 {
		 // could cache these
		 return new MapToHierarchicalTypedPropertyNodeAdapter(keyPrefix + PATHSEPARATOR + key, backingMap);
		 }

 */
	}
