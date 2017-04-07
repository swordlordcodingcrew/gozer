/*-----------------------------------------------------------------------------
 **
 ** -Gozer is not Zuul-
 **
 ** Copyright 2017 by SwordLord - the coding crew - https://www.swordlord.com/
 **
 ** This program is free software; you can redistribute it and/or modify it
 ** under the terms of the GNU Affero General Public License as published by the Free
 ** Software Foundation, either version 3 of the License, or (at your option)
 ** any later version.
 **
 ** This program is distributed in the hope that it will be useful, but WITHOUT
 ** ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 ** FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for
 ** more details.
 **
 ** You should have received a copy of the GNU Affero General Public License along
 ** with this program. If not, see <http://www.gnu.org/licenses/>.
 **
 **-----------------------------------------------------------------------------
 **
 ** $Id: LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.jalapeno.dataview;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder for {@link ViewFilter}s.
 * 
 * @author LordEidi
 */
public class ViewFilterBuilder
{
    /** Statement chaining mode. */
    public enum ChainingMode
    {
        /** And chaining. */
        AND(" and "),
        /** Or chaining. */
        OR(" or ");

        /** The value to append. */
        private final String _value;

        /**
         * Default constructor.
         * 
         * @param value
         *            The value
         */
        private ChainingMode(String value)
        {
            _value = value;
        }

        /**
         * Appends the chaining operator if necessary.
         * 
         * @param s
         *            The string
         */
        void append(StringBuilder s)
        {
            if (s.length() > 0)
            {
                s.append(_value);
            }
        }
    }

    /** The filter string. */
    private final StringBuilder _filter = new StringBuilder();

    /** The arguments for the filter string. */
    private final Map<String, Object> _arguments = new HashMap<String, Object>();

    /** The current chaining type. */
    private ChainingMode _chainingType;

    /** The number of the next parameter. */
    private int _paramNr;

    /**
     * Default constructor.
     * 
     * @param chaining
     *            The chaining type to use
     */
    public ViewFilterBuilder(ChainingMode chaining)
    {
        _chainingType = chaining;
    }

    /**
     * Creates a simple filter string without any argument objects.
     * 
     * @param filter
     *            A filter string
     * @return The view filter
     */
    public static ViewFilter createSimple(String filter)
    {
        return new ViewFilter(filter, null);
    }

    /**
     * Adds a like expression.
     * 
     * @param path
     *            The property path of the value to check
     * @param value
     *            The expected value
     * @return This builder
     */
    public ViewFilterBuilder like(String path, String value)
    {
        _chainingType.append(_filter);
        _filter.append(path).append(" like ").append('$').append(addParam(value));
        return this;
    }

    /**
     * Adds an equals expression.<br>
     * 
     * @param path
     *            The property path of the value to check
     * @param value
     *            The expected value
     * @return This builder
     */
    public ViewFilterBuilder eq(String path, Object value)
    {
        _chainingType.append(_filter);
        _filter.append(path).append(" = ").append('$').append(addParam(value));
        return this;
    }

    /**
     * Adds the given value to the parameter map.
     * 
     * @param value
     *            A value
     * @return The key in the {@link #_arguments} map
     */
    private String addParam(Object value)
    {
    	//as the cayenne parser does not like fully numeric parameter names, we must prepend an letter
        final String key = "A" + _paramNr++;
        _arguments.put(key, value);
        return key;
    }

    /**
     * Builds a {@link ViewFilter} for the expression in this builder.
     * 
     * @return The view filter or null for an empty filter
     */
    public ViewFilter build()
    {
        if (_filter.length() == 0)
        {
            return null;
        }
        return new ViewFilter(_filter.toString(), _arguments);
    }
}
