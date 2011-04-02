package com.davidsoergel.trees;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class IntegerNodeNamer implements NodeNamer<Integer>
	{
	//private int unknownBasis;


	public IntegerNodeNamer()
		{

		}

	public boolean requireGeneratedNamesForInternalNodes()
		{
		return false;
		}

	/**
	 * {@inheritDoc}
	 */
	public Integer merge(Integer name, String s) throws TreeException
		{
		throw new TreeException("Cannot merge integer IDs");
		}

	/**
	 * {@inheritDoc}
	 */
	public Integer merge(Integer name, Integer s) throws TreeException
		{
		throw new TreeException("Cannot merge integer IDs");
		}

	/**
	 * {@inheritDoc}
	 */
	public Integer create(Integer s)
		{
		return s;
		}

	/**
	 * {@inheritDoc}
	 */
	public Integer create(String s) throws TreeException
		{
		try
			{
			return new Integer(s);
			}
		catch (NumberFormatException e)
			{
			throw new TreeException(e, "Non-integer ID found.");
			}
		}

	public boolean isAcceptable(Integer value)
		{
		return value != null;
		}

	public Integer makeAggregate(Integer newValue, Integer value)
		{
		throw new TreeRuntimeException("Can't aggregate Integer IDs");
		}

	public Integer generate()
		{
		throw new TreeRuntimeException("ID modification disallowed");
		}

	public Integer uniqueify(final Integer value)
		{
		throw new TreeRuntimeException("ID modification disallowed");
		}
	}
