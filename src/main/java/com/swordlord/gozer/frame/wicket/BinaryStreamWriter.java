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
** $Id: BinaryStreamWriter.java 1363 2012-10-19 15:22:22Z LordEidi $
**
-----------------------------------------------------------------------------*/
 
package com.swordlord.gozer.frame.wicket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.IResourceStreamWriter;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;


/**
 * TODO JavaDoc for BinaryStreamWriter.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class BinaryStreamWriter implements IResourceStreamWriter
{
	protected static final Log LOG = LogFactory.getLog(BinaryStreamWriter.class);
	
	private ByteArrayOutputStream _stream;
	private String _strContentType;
	private Locale _locale;
	
    /**
     * @param stream
     * @param strContentType
     */
	public BinaryStreamWriter(ByteArrayOutputStream stream, String strContentType)
	{
		if(stream == null)
		{
			throw new IllegalStateException("stream may not be null");
		}
		
		_stream = stream;
		_strContentType = strContentType;
	}

	@Override
    public void write(OutputStream output) 
	{
		try 
		{
			_stream.writeTo(output);
		} 
		catch (IOException e) 
		{
			LOG.error(MessageFormat.format("Error during write in BinaryStreamWriter: {0}", e.getLocalizedMessage()));
		}
	}
	
	@Override
    public void close() throws IOException 
	{
		_stream.close();	
	}

	@Override
    public String getContentType() 
	{
		return _strContentType;
	}

	@Override
    public InputStream getInputStream() throws ResourceStreamNotFoundException 
	{
		throw new IllegalStateException("getInputStream is not used with IResourceStreamWriter");
	}

	@Override
    public Locale getLocale() 
	{
		return _locale;
	}

    @Override
    public Bytes length()
	{
        return Bytes.bytes(_stream.size());
	}

	@Override
    public void setLocale(Locale locale) 
	{
		_locale = locale;
	}

	@Override
    public Time lastModifiedTime() 
	{
		return Time.now();
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyle()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVariation()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyle(String arg0)
    {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVariation(String arg0)
    {
        // TODO Auto-generated method stub

    }
}
