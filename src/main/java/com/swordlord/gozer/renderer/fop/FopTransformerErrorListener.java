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
** $Id: FopTransformerErrorListener.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.renderer.fop;

import java.text.MessageFormat;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author LordEidi
 *
 */
public class FopTransformerErrorListener implements ErrorListener
{
	protected static final Log LOG = LogFactory.getLog(FopTransformerErrorListener.class);
	
	public void error(TransformerException arg0) throws TransformerException 
	{
		LOG.error(MessageFormat.format("Transformation error: {0}", arg0));
	}

	public void fatalError(TransformerException arg0) throws TransformerException 
	{
		// TODO Auto-generated method stub	
		LOG.error(MessageFormat.format("Transformation fatal error: {0}", arg0));
	}

	public void warning(TransformerException arg0) throws TransformerException 
	{
		// TODO Auto-generated method stub	
		LOG.warn(MessageFormat.format("Transformation warning: {0}", arg0));
	}

}
