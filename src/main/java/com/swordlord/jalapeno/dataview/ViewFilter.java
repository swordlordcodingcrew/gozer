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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter object for {@link DataViewBase}.<br>
 * Use {@link ViewFilterBuilder} to create instances of this class.
 * 
 * @author LordEidi
 */
@SuppressWarnings("serial")
public class ViewFilter implements Serializable
{
    /** The filter string. */
    private final String _filter;

    /** The arguments for the filter string. */
    private final Map<String, Object> _arguments;

    /**
     * Default constructor.
     * 
     * @param filter
     *            The filter string (not empty!)
     * @param arguments
     *            The filter arguments or null
     * @throws IllegalArgumentException
     *             If the passed filter string is null or empty
     */
    ViewFilter(String filter, Map<String, Object> arguments)
    {
        if (filter == null || filter.isEmpty())
        {
            throw new IllegalArgumentException("filter must be not null and not empty!");
        }
        if (arguments == null || arguments.isEmpty())
        {
            _arguments = Collections.<String, Object> emptyMap();
        }
        else
        {
            _arguments = Collections.unmodifiableMap(new HashMap<String, Object>(arguments));
        }
        _filter = filter;
    }

    /**
     * Returns the filter string.
     * 
     * @return The filter string
     */
    public String getFilter()
    {
        return _filter;
    }

    /**
     * Returns the filter arguments.
     * 
     * @return The filter arguments
     */
    public Map<String, Object> getArguments()
    {
        return _arguments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + _arguments.hashCode();
        result = prime * result + _filter.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof ViewFilter))
        {
            return false;
        }
        final ViewFilter other = (ViewFilter) obj;
        if (!_arguments.equals(other._arguments))
        {
            return false;
        }
        return _filter.equals(other._filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        // output the 'resolved' filter string
        final StringBuffer str = new StringBuffer();
        final Pattern p = Pattern.compile("\\$\\w\\S*");
        final Matcher m = p.matcher(_filter);
        while (m.find())
        {
            final String key = m.group(0);
            final Object val = _arguments.get(key);
            m.appendReplacement(str, Matcher.quoteReplacement(val != null ? val.toString() : key));
        }
        m.appendTail(str);
        return str.toString();
    }
}
