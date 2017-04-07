/*-----------------------------------------------------------------------------
 **
 ** -Gozer is not Zuul-
 **
 ** Copyright 2007 by SOMAP.org
 ** http://www.somap.org
 **
 ** This program is free software; you can redistribute it and/or modify it
 ** under the terms of the GNU Affero General Public License as published by the Free
 ** Software Foundation; either version 2 of the License, or (at your option)
 ** any later version.
 **
 ** This program is distributed WITHOUT ANY WARRANTY; without even the implied
 ** warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 ** GNU Affero General Public License for more details. If you live in a country where
 ** the GPL is not valid, you may either NOT USE this program or licence it
 ** under the SWORDLORD PUBLIC SOURCE LICENCE. Contact the authors for further
 ** details.
 **
 ** You should have received a copy of the GNU Affero General Public License along with
 ** this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 ** Place - Suite 330, Boston, MA 02111-1307, USA.
 **
 **-----------------------------------------------------------------------------
 **
 ** $Id: FrameRendererBase.java 1170 2011-10-07 16:24:10Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.renderer.generic;

import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.frame.IGozerFrameExtension;

/**
 * @author LordEidi
 * 
 */
public class FrameRendererBase extends AbstractRenderer
{
	private IGozerFrameExtension _gfe;

	public FrameRendererBase(ObjectTree ot, IGozerFrameExtension gfe)
	{
		super(ot);

		_gfe = gfe;
	}

	public IGozerFrameExtension getGozerFrameExtension()
	{
		return _gfe;
	}
}
