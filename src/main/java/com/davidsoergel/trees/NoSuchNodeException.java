/*
 * Copyright (c) 2009-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.davidsoergel.trees;

import com.davidsoergel.dsutils.ChainedException;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class NoSuchNodeException extends ChainedException
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(NoSuchNodeException.class);

	// --------------------------- CONSTRUCTORS ---------------------------

/*	public NoSuchNodeException()
		{
		super();
		}*/

	public NoSuchNodeException(String s)
		{
		super(s);
		}

	public NoSuchNodeException(Exception e)
		{
		super(e);
		}

	public NoSuchNodeException(Exception e, String s)
		{
		super(e, s);
		}
	}
