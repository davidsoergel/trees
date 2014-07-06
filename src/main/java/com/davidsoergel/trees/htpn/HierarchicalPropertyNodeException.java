/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package com.davidsoergel.trees.htpn;

import com.davidsoergel.dsutils.ChainedException;
import org.apache.log4j.Logger;

/**
 * @version $Id: HierarchicalPropertyNodeException.java 221 2008-09-24 22:07:44Z soergel $
 */
public class HierarchicalPropertyNodeException extends ChainedException
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(HierarchicalPropertyNodeException.class);


	// --------------------------- CONSTRUCTORS ---------------------------

	public HierarchicalPropertyNodeException(Throwable e)
		{
		super(e);
		}

	public HierarchicalPropertyNodeException(String e)
		{
		super(e);
		}
	}
