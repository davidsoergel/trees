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
