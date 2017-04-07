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
package com.swordlord.gozer.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.Application;
import org.apache.wicket.core.util.resource.locator.IResourceStreamLocator;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

/**
 * Helper class for loading resources.
 * 
 * @author LordEidi
 */
public final class ResourceLoader
{
    /**
     * Hidden constructor.
     */
    private ResourceLoader()
    {
    }

    /**
     * Loads a resource using the {@link Application}s
     * {@link IResourceStreamLocator}.
     * 
     * @param application
     *            The application
     * @param filename
     *            The name/path of the file to load
     * @param clazz
     *            The class loader for delegating the loading of the resource
     * @return The input stream
     * @throws IOException
     *             If no such resource could be found
     */
    public static InputStream loadResource(Application application, String filename, Class clazz) throws IOException
    {
        return loadResource0(application, prepareFileName(filename), clazz);
    }

    /**
     * Loads a resource using the {@link Application}s
     * {@link IResourceStreamLocator}.
     * 
     * @param application
     *            The application
     * @param filename
     *            The normalized name/path of the file to load (after being
     *            passed to {@link #prepareFileName(String)})
     * @param clazz
     *            The class loader for delegating the loading of the resource
     * @return The input stream
     * @throws IOException
     *             If no such resource could be found
     */
    private static InputStream loadResource0(Application application, String filename, Class clazz) throws IOException
    {
        final IResourceStreamLocator locator = application.getResourceSettings().getResourceStreamLocator();
        final IResourceStream resource = locator.locate(clazz, filename);
        if (resource == null)
        {
            throw new IOException("Error while loading " + filename + " from classpath - no such entry found");
        }
        try
        {
            return resource.getInputStream();
        }
        catch (ResourceStreamNotFoundException e)
        {
            throw new IOException("Error while loading " + filename + " from classpath", e);
        }
    }

    /**
     * Makes the given filename/path {@link ClassLoader} conform.
     * 
     * @param filename
     *            A file name
     * @return The classloader conform name
     */
    private static String prepareFileName(String filename)
    {
        final char firstChar = filename.charAt(0);
        // strip leading slash - according to the specification you must not
        // pass a leading slash for getResourceAsStream
        if (firstChar == '/' || firstChar == '\\')
            return filename.substring(1);
        return filename;
    }
}
