/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees;

import com.davidsoergel.dsutils.ChainedRuntimeException;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: TreeRuntimeException.java 252 2008-08-29 00:52:58Z soergel $
 */
public class TreeRuntimeException extends ChainedRuntimeException
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(TreeRuntimeException.class);


	// --------------------------- CONSTRUCTORS ---------------------------

	public TreeRuntimeException(String s)
		{
		super(s);
		}

	public TreeRuntimeException(Exception e)
		{
		super(e);
		}

	public TreeRuntimeException(Exception e, String s)
		{
		super(e, s);
		}
	}
