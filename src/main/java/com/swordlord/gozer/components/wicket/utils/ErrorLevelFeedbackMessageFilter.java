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
** $Id: ErrorLevelFeedbackMessageFilter.java 1170 2011-10-07 16:24:10Z LordEidi $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.utils;

import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;

/**
 * Filter for accepting only feedback messages with this error level.
 * The difference between this class and the Wicket class of same name
 * is that this class accepts multiple filters while the Wicket class 
 * works with filterLevel >
 */
@SuppressWarnings("serial")
public class ErrorLevelFeedbackMessageFilter implements IFeedbackMessageFilter
{
	/** The minimum error level */
	private int[] _filteredErrorLevels;

	/**
	 * Constructor
	 *
	 * @param filteredErrorLevels FeedbackMessages with these error levels will not be shown.
	 */
	public ErrorLevelFeedbackMessageFilter(int[] filteredErrorLevels)
	{
		_filteredErrorLevels = filteredErrorLevels;
	}

	/**
     * Returns if a FeedbackMessage can be displayed or should be filtered.
     *
     * @param message of type FeedbackMessage
     * @return boolean can this message be displayed with the current filters set
     */
    public boolean accept(FeedbackMessage message)
    {
        for (int errorLevel : _filteredErrorLevels) 
        {
            if (message.getLevel() == errorLevel) 
            {
                return false;
            }
        }

        return true;
	}
}