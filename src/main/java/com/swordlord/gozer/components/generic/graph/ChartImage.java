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
 ** $Id: ChartImage.java 1361 2012-04-15 11:04:14Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.generic.graph;

import java.awt.image.BufferedImage;

import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;

/**
 * Base class for charting images
 * 
 * @author LordEidi
 * 
 */
public class ChartImage extends NonCachingImage
{
	private static final long serialVersionUID = -7165602010769784429L;

	private int width;
	private int height;
	private JFreeChart chart;
	private transient BufferedImage image;
	private transient ChartRenderingInfo renderingInfo;

    /**
     * Constructor
     * 
     * @param id
     * @param chart
     * @param width
     * @param height
     */
	public ChartImage(String id, JFreeChart chart, int width, int height)
	{
		super(id);
		this.width = width;
		this.height = height;
		this.chart = chart;
	}

    /**
     * @return
     */
	public BufferedImage createBufferedImage()
	{
		if (image == null)
		{
			renderingInfo = new ChartRenderingInfo();
			image = chart.createBufferedImage(width, height, renderingInfo);
		}
		return image;
	}

    /**
     * @return
     */
	public ChartRenderingInfo getRenderingInfo()
	{
		if (renderingInfo == null)
		{
			createBufferedImage();
		}
		return renderingInfo;
	}

	@Override
	protected IResource getImageResource()
	{
		return new DynamicImageResource()
		{
			private static final long serialVersionUID = -4386816651419227671L;

			@Override
            protected byte[] getImageData(Attributes attributes)
            {
                return toImageData(createBufferedImage());
            }
		};
	}
}