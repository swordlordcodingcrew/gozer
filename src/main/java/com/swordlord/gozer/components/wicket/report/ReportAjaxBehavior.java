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
 ** $Id: ReportAjaxBehavior.java 1363 2012-10-19 15:22:22Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.report;

import org.apache.commons.lang.NullArgumentException;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * TODO JavaDoc for ReportAjaxBehavior.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class ReportAjaxBehavior extends AbstractDefaultAjaxBehavior
{
	private GWReport _report;
	
    /**
     * @param report
     */
	public ReportAjaxBehavior(GWReport report)
	{
		if(report == null) throw new NullArgumentException("GWReport");
		
		_report = report;
	}
	
    @Override
    protected void respond(AjaxRequestTarget target) 
    {
        Component component = _report.getLazyLoadComponent(GWReport.LAZY_LOAD_COMPONENT_ID);
        _report.replace(component);
        //_report.replace(component.setRenderBodyOnly(true));
        target.add(_report);
        _report.setState((byte)2);
    }

    @Override
	public boolean isEnabled(Component component)
	{
		return _report.getState() < 2;
	}
}