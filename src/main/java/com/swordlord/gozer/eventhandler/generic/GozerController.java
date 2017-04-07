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
** $Id: GozerController.java 1333 2011-12-23 14:29:57Z LordEidi $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.eventhandler.generic;

import java.io.Serializable;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import com.swordlord.gozer.components.generic.action.GActivateAction;
import com.swordlord.gozer.components.generic.action.GCancelAction;
import com.swordlord.gozer.components.generic.action.GDeleteAction;
import com.swordlord.gozer.components.generic.action.GEditAction;
import com.swordlord.gozer.components.generic.action.GFirstAction;
import com.swordlord.gozer.components.generic.action.GLastAction;
import com.swordlord.gozer.components.generic.action.GNewAction;
import com.swordlord.gozer.components.generic.action.GNextAction;
import com.swordlord.gozer.components.generic.action.GOKAction;
import com.swordlord.gozer.components.generic.action.GPersistAction;
import com.swordlord.gozer.components.generic.action.GPrevAction;
import com.swordlord.gozer.components.generic.action.GToggleAction;
import com.swordlord.gozer.components.generic.action.IGozerAction;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingManager;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.jalapeno.datarow.DataRowBase;
import com.swordlord.jalapeno.dataview.DataViewBase;
import com.swordlord.sobf.common.i18n.Translator;

@SuppressWarnings("serial")
public class GozerController implements Serializable
{
	//private static Logger LOG = Logger.getLogger("com.swordlord.gozer.eventhandler.generic.GozerController");
	
	protected IGozerFrameExtension _fe;
	protected GozerFrameStatus _frameStatus = GozerFrameStatus.SHOW;
	protected GozerDisplayMode _displayMode = GozerDisplayMode.WEB;
	protected WebPage _webPage;
	protected GozerEventListenerList _gozerEventHandler;
	
	private Translator _translator = null;

	/**
	 * Constructor of GEventControl. Per GozerFrame one GEventControl is intended.
	 * @param dc the DataContainer for db-interaction
	 */
	public GozerController(IGozerFrameExtension fe)
	{
		_fe = fe;

		_gozerEventHandler = new GozerEventListenerList();

		initAction();
	}

	private void activateAction(DataBinding dataBinding, IGozerAction source)
	{
		_fe.onActivation(dataBinding, source);
	}

	public void addGozerEventListener(GozerEventListener gel)
	{
		_gozerEventHandler.add(GozerEventListener.class, gel);
	}

	public void cancelAction(GozerActionEvent event)
	{
		_fe.onCancel();
		_fe.getDataContainer().rollback();
		_fe.onLoad();
		
		// make sure, that the tables are re-rendered
		firePositionChangedEvent(event);

		changeState(GozerFrameStatus.SHOW);
		fireGozerUpdateUIEvent(new  GozerUpdateUIEvent(event.getSource(), null));
	}

	public void changeState(GozerFrameStatus status)
	{
		_frameStatus = status;

		GozerEvent event = new GozerEvent(this);
		this.fireGozerEvent(event);
	}

	public void deleteAction(GozerActionEvent event)
	{
		// make sure, that the tables are re-rendered
		firePositionChangedEvent(event);
		
		fireGozerUpdateUIEvent(new GozerUpdateUIEvent(event.getSource(), null));
	}

	public Page detailAction(DataRowBase row)
	{
		return _fe.onDetail(row);
	}

	public void editAction(GozerActionEvent event)
	{
		_fe.onEdit();
		changeState(GozerFrameStatus.EDIT);
		fireGozerUpdateUIEvent(new GozerUpdateUIEvent(event.getSource(), null));
	}

	public void fireGozerEvent(GozerEvent event)
	{
		Object[] listeners = _gozerEventHandler.getListenerList();		
		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == GozerEventListener.class)
			{
				((GozerEventListener)listeners[i+1]).gozerEventPerformed(event);
			}
		}
	}

	public void fireGozerSelectionEvent(GozerSelectionEvent gse)
	{
		Object[] listeners = _gozerEventHandler.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == GozerEventListener.class)
			{
				((GozerEventListener)listeners[i+1]).gozerSelectionPerformed(gse);
			}
		}
	}

	public void fireGozerUpdateUIEvent(GozerUpdateUIEvent gui)
	{
		Object[] listeners = _gozerEventHandler.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == GozerEventListener.class)
			{
				((GozerEventListener)listeners[i+1]).gozerUpdateUIPerformed(gui);
			}
		}
	}
	
	public void fireGozerFeebackInfoEvent(GozerFeedbackInfoEvent gui, String message)
	{
		Object[] listeners = _gozerEventHandler.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == GozerEventListener.class)
			{
				((GozerEventListener)listeners[i+1]).gozerFeedbackInfoPerformed(gui, message);
			}
		}
	}


	/**
	 * Selects the first record of the given table
	 * @param strTable   the table name
	 * @param source     the originator of the action (needed for the event)
	 * @param bFireEvent if true a gozerSelectionEvent is fired
	 */
	public void firstAction(DataBinding dataBinding)
	{
		dataBinding.moveFirst();
	}

	public void genericActionPerformed(GozerActionEvent event)
	{
		// this method checks the action to perform
		// - UI actions are handled directly in this class
		// - transitional actions are handed to the FrameExtension to be handled there
		String strEvent = event.getActionID();

		if(strEvent.compareToIgnoreCase(GCancelAction.getObjectTag()) == 0)
		{
			cancelAction(event);
		}
		else if(strEvent.compareToIgnoreCase(GOKAction.getObjectTag()) == 0)
		{
			okAction(event);
		}
		else if(strEvent.compareToIgnoreCase(GDeleteAction.getObjectTag()) == 0)
		{
			deleteAction(event);
		}
		// TODO Detail Button is missing
		else if(strEvent.compareToIgnoreCase(GEditAction.getObjectTag()) == 0)
		{
			editAction(event);
		}
		else if(strEvent.compareToIgnoreCase(GFirstAction.getObjectTag()) == 0)
		{
			firstAction(event.getDataBinding());
		}
		else if(strEvent.compareToIgnoreCase(GLastAction.getObjectTag()) == 0)
		{
			lastAction(event.getDataBinding());
		}
		else if(strEvent.compareToIgnoreCase(GNewAction.getObjectTag()) == 0)
		{
			newAction(event);
		}
		else if(strEvent.compareToIgnoreCase(GNextAction.getObjectTag()) == 0)
		{
			nextAction(event.getDataBinding());
		}
		else if(strEvent.compareToIgnoreCase(GPersistAction.getObjectTag()) == 0)
		{
			persistAction(event);
		}
		else if(strEvent.compareToIgnoreCase(GPrevAction.getObjectTag()) == 0)
		{
			prevAction(event.getDataBinding());
		}
		else if(strEvent.compareToIgnoreCase(GToggleAction.getObjectTag()) == 0)
		{
			toggleAction(event.getDataBinding(), event.getSource());
		}
		else if(strEvent.compareToIgnoreCase(GActivateAction.getObjectTag()) == 0)
		{
			activateAction(event.getDataBinding(), event.getSource());
		}
		else
		{
			unhandledEvent(event);
		}
	}

	public DataContainer getDataContainer()
	{
		return _fe.getDataContainer();
	}

	public GozerFrameStatus getFrameStatus()
	{
		return _frameStatus;
	}

	public IGozerFrameExtension getGozerFrameExtension()
	{
		return _fe;
	}
	
	public void unhandledEvent(GozerActionEvent event)
	{
		_fe.onUnhandledEvent(event);
		fireGozerUpdateUIEvent(new GozerUpdateUIEvent(event.getSource(), null));
	}

	public void initAction()
	{
		_fe.onInit();
		_fe.onLoad();
	}

	public boolean isModal()
	{
		return (_displayMode == GozerDisplayMode.WEB_MODAL) || (_displayMode == GozerDisplayMode.SWING_MODAL);
	}

	/**
	 * Selects the last record of the given table
	 * @param strTable the table name
	 * @param source     the originator of the action (needed for the event)
	 * @param bFireEvent if true a gozerSelectionEvent is fired
	 */
	public void lastAction(DataBinding dataBinding)
	{
		dataBinding.moveLast();
	}

	public void newAction(GozerActionEvent event)
	{
		// make sure, that the tables are re-rendered
		DataBinding db = event.getDataBinding();
		_fe.onNew(db.getDataBindingMember());
		firePositionChangedEvent(event);
		
		changeState(GozerFrameStatus.NEW);
		fireGozerUpdateUIEvent(new GozerUpdateUIEvent(event.getSource(), event.getDataBinding()));
	}

	public void nextAction(DataBinding dataBinding)
	{
		dataBinding.moveNext();
	}

	public void okAction(GozerActionEvent event)
	{
		//if(_isModal)
		//{
		//	_fe.o
		//}
	}

	public void persistAction(GozerActionEvent event)
	{
		_fe.onPrePersist();
		if(_fe.getDataContainer().persist())
		{
			_fe.onPostPersist(event);
			_fe.onLoad();
			changeState(GozerFrameStatus.SHOW);
			
			if (_translator == null)
				_translator = new Translator();
			
			fireGozerFeebackInfoEvent(new GozerFeedbackInfoEvent(event.getSource(), null), _translator.getString("system.message.", "dataSaved"));
		}
		else
		{
			// An error happened, show the error and don't change state
		}
		fireGozerUpdateUIEvent(new GozerUpdateUIEvent(event.getSource(), null));
	}

	/**
	 * Selects the previous record of the given table.
	 * If the first record is already selected, nothing will happen.
	 * @param strTable   the table name
	 * @param source     the originator of the action (needed for the event)
	 * @param bFireEvent if true a gozerSelectionEvent is fired
	 */
	public void prevAction(DataBinding dataBinding)
	{
		dataBinding.movePrevious();
	}

	public void removeGozerEventListener(GozerEventListener gel)
	{
		_gozerEventHandler.remove(GozerEventListener.class, gel);
	}

	public void setDisplayMode(GozerDisplayMode displayMode)
	{
		_displayMode = displayMode;
	}

			//fireGozerSelectionEvent(new GozerSelectionEvent(source, strTable, recordNum, recordNum));

	public void toggleAction(DataBinding dataBinding, Object source)
	{
		fireGozerUpdateUIEvent(new GozerUpdateUIEvent(source, dataBinding));
	}
	
	protected void firePositionChangedEvent(GozerActionEvent event)
	{
		DataBinding db = event.getDataBinding();
		DataBindingManager dbm = db.getDataBindingManager();

		DataViewBase dv = dbm.getDataView(db.getDataBindingMember());
		if(dv != null) 
		{
			dv.fireCurrentRowChangedEvent();
		}
	}
}
