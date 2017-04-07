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
 ** $Id: GReportAction.java 969 2010-02-26 14:11:02Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.generic.action;

import com.swordlord.gozer.builder.ObjectTree;

/**
 * Action to display Report on new window
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GReportAction extends GActionBase
{
    /**
     * The report parameter
     */
    public static String ATTRIBUTE_SOURCE = "src";

    /**
     * The report title
     */
    public static String ATTRIBUTE_TITLE = "title";

    /**
     * The window width
     */
    public static String ATTRIBUTE_WINDOW_WIDTH = "width";

    /**
     * The window height
     */
    public static String ATTRIBUTE_WINDOW_HEIGHT = "height";

    
    public static String getObjectTag()
    {
        return "report-action";
    }

    /**
     * Constructor
     * 
     * @param root
     *            root element
     */
    public GReportAction(ObjectTree root)
    {
        super(root);
    }

    /**
     * The report to call
     * 
     * @return report
     */
    public String getSource()
    {
        return getAttribute(ATTRIBUTE_SOURCE);
    }

    
    /**
     * The window width in pixel
     * 
     * @return width in pixel
     */
    public String getWindowWidth()
    {
        return getAttribute(ATTRIBUTE_WINDOW_WIDTH);
    }

    /**
     * The window height in pixel
     * 
     * @return height in pixel
     */
    public String getWindowHeight()
    {
        return getAttribute(ATTRIBUTE_WINDOW_HEIGHT);
    }
    
    /**
     * The report title
     * 
     * @return title
     */
    public String getTitle()
    {
        return getAttribute(ATTRIBUTE_TITLE);
    }
}
