/*-----------------------------------------------------------------------------
 **
 ** -Gozer is not Zuul-
 **
 ** Copyright 2017 by SwordLord - the coding crew - https://www.swordlord.com/
 ** and individual authors
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
 ** $Id: GWDetailDateFieldPanel.java 1361 2012-04-15 11:04:14Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.file;

import com.swordlord.gozer.util.ResourceLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Application;

import java.io.*;

/**
 * TODO JavaDoc for GozerFileLoader.java
 * 
 * @author ..
 * 
 */
public class GozerFileLoader
{
    private static final Log LOG = LogFactory.getLog(GozerFileLoader.class);

    public static InputStream getGozerLayout(Application app, String strGozerFileName)
    {
        // TODO check JCR or other virtual FS for gozer files
        InputStream instrGozerFile = null;

        try {
            if (instrGozerFile == null)
            {
                instrGozerFile = ResourceLoader.loadResource(app, strGozerFileName, GozerFileLoader.class);
            }
        }
        catch (IOException e)
        {
                LOG.error(e);
        }

        return instrGozerFile;
    }

    public static String convertInputStreamToString(InputStream is)
    {
        String strReturn = "";

        if (is != null)
        {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try
            {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                int n;
                while ((n = reader.read(buffer)) != -1)
                {
                    writer.write(buffer, 0, n);
                }

                reader.close();
                strReturn = writer.toString();
                writer.close();
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {

            }
        }


        return strReturn;
    }
}
