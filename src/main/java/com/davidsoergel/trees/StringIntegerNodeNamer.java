package com.davidsoergel.trees;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class StringIntegerNodeNamer extends StringNodeNamer
	{
	public StringIntegerNodeNamer(String unknownBasis, boolean allowNull, boolean requireGeneratedNamesForInternalNodes)
		{
		super(unknownBasis, allowNull, requireGeneratedNamesForInternalNodes);
		}

	public StringIntegerNodeNamer(String unknownBasis, boolean allowNull, int startId,
	                              boolean requireGeneratedNamesForInternalNodes)
		{
		super(unknownBasis, allowNull, requireGeneratedNamesForInternalNodes, startId);
		}

	public boolean isAcceptable(String value)
		{
		if (value == null)
			{
			return allowNull;
			}

		try
			{
			Integer.parseInt(value);
			return true;
			}
		catch (NumberFormatException e)
			{
			return false;
			}
		}
	}
