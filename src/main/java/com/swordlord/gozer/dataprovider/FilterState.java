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
** $Id: $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.dataprovider;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.swordlord.jalapeno.dataview.ViewFilter;
import com.swordlord.jalapeno.dataview.ViewFilterBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.model.PropertyModel;

@SuppressWarnings("serial")
public class FilterState implements Serializable, Map<String, Object>
{
    protected static final Log LOG = LogFactory.getLog(FilterState.class);

    protected HashMap<String, Object> _entries;

    public FilterState()
    {
        _entries = new HashMap<String, Object>();
    }

    public ViewFilter getFilter()
    {
        if (_entries.isEmpty())
        {
            return null;
        }
        final ViewFilterBuilder b = new ViewFilterBuilder(ViewFilterBuilder.ChainingMode.AND);
        for (Entry<String, Object> e : _entries.entrySet())
        {
            final Object value = e.getValue();
            if (value == null)
            {
                continue;
            }

            final String key = unescapePropertyPath(e.getKey());

            // TODO add code tables later
            /*
            // the type is taken from the model object
            if (value instanceof CodeDataRow)
            {
                final CodeDataRow row = (CodeDataRow) value;
                final Integer code = row.getCodeValue();
                if (code == null)
                {
                    b.eq(key, value.toString());
                }
                else
                {
                    b.eq(key, code);
                }
            }
            else
             */
            if (value instanceof Date)
            {
                b.eq(key, value);
            }
            else
            {
                b.like(key, value.toString());
            }
        }
        return b.build();
    }

    @Override
    public void clear()
    {
        _entries.clear();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return _entries.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return _entries.containsValue(value);
    }

    @Override
    public Set<Entry<String, Object>> entrySet()
    {
        return entrySet();
    }

    @Override
    public Object get(Object key)
    {
        final Object val = _entries.get(key);
        if (val != null)
            return val;
        LOG.info("KEY=" + key + " NOT FOUND IN FilterState");
        return null;
    }

    @Override
    public boolean isEmpty()
    {
        return _entries.isEmpty();
    }

    @Override
    public Set<String> keySet()
    {
        return keySet();
    }

    @Override
    public Object put(String key, Object value)
    {
        return _entries.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m)
    {
        _entries.putAll(m);
    }

    @Override
    public Object remove(Object key)
    {
        return _entries.remove(key);
    }

    @Override
    public int size()
    {
        return _entries.size();
    }

    @Override
    public Collection<Object> values()
    {
        return _entries.values();
    }

    /**
     * Unescapes a property path escaped previously with
     * {@link #escapePropertyPath(String)}.
     * 
     * @param path
     *            The escaped path
     * @return The original path
     */
    public static String unescapePropertyPath(String path)
    {
        // this is a hack since Wicket has a problem with dots in keys.
        // we replaced . with : in the GozerFilteredColumn and need to
        // replace it here again...
        return path.replace(":", ".");
    }

    /**
     * Escapes the given property path so that it can be used for
     * {@link PropertyModel}s.
     * 
     * @param path
     *            The original path
     * @return The escaped path
     */
    public static String escapePropertyPath(String path)
    {
        // this is a hack since Wicket has a problem with dots in keys.
        // we replaced . with : and will have to replace it again in
        // FitlerState.getFilter()
        return path.replace(".", ":");
    }
}
