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
 ** $Id: GWAbstractAction.java 1361 2012-04-15 11:04:14Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.action.button;

import java.io.InputStream;
import java.text.MessageFormat;

import com.swordlord.common.i18n.ITranslator;
import com.swordlord.common.i18n.TranslatorFactory;
import com.swordlord.gozer.file.GozerFileLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.form.ImageButton;
import org.apache.wicket.request.resource.ResourceReference;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import com.swordlord.gozer.builder.ObjectTree;
import com.swordlord.gozer.builder.Parser;
import com.swordlord.gozer.components.generic.action.GActionBase;
import com.swordlord.gozer.components.generic.action.IGozerAction;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.eventhandler.generic.GozerActionEvent;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.gozer.frame.IGozerFrameExtension;
import com.swordlord.gozer.session.IGozerSessionInfo;


/**
 * Base class for all action items based on an image. These are usually commands
 * on top of a form.
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public abstract class GWAbstractAction extends ImageButton implements IGozerAction
{
    protected static final Log LOG = LogFactory.getLog(GWAbstractAction.class);

    protected GozerController _gc;
    protected GActionBase _actionBase;
    private ITranslator _tr;

    /**
     * @param id
     * @param gc
     * @param actionBase
     * @param resource
     */
    public GWAbstractAction(String id, GozerController gc, GActionBase actionBase, ResourceReference resource)
    {
        super(id, resource);

        _actionBase = actionBase;

        _gc = gc;
    }

    /**
     * Helper methods that both checks whether the link is enabled and whether
     * the action ENABLE is allowed.
     * 
     * @return whether the link should be rendered as enabled
     */
    protected final boolean isButtonEnabled()
    {
        return isEnabled() && isEnableAllowed();
    }

    @Override
    public DataBinding getDataBinding()
    {
        return _actionBase.getDataBinding();
    }

    @Override
    public DataBinding getDataBindingTwo()
    {
        return _actionBase.getDataBindingTwo();
    }

    public String getIdentifier()
    {
        return _actionBase.getIdentifier();
    }

    public GozerController getGozerController()
    {
        return _gc;
    }

    public IGozerSessionInfo getGozerSession()
    {
        return (IGozerSessionInfo) getSession();
    }

    @Override
    public void onSubmit()
    {
        GozerActionEvent event = new GozerActionEvent(this);
        _gc.genericActionPerformed(event);

        // remain on this page, hence do not set response page
    }

    protected ITranslator getTranslator()
    {
        if (_tr == null)
        {
            _tr = TranslatorFactory.getTranslator();
        }

        return _tr;
    }
    
    protected String getTranslationName()
    {
        String permission = this.getClass().getName();
        permission = permission.toLowerCase();
        permission = permission.replace("com.swordlord.gozer.components.wicket.action.", "");
        permission = permission.substring(0, permission.lastIndexOf("."));

        return permission;
    }

    /**
     * Loads the ObjectTree from the given GozerFrameExtension. Checks JCR first
     * and then loads from the HD afterwards.
     * 
     * @param gfe
     *            GozerFrameExtension to load tree from.
     * @return Returns loaded ObjectTree
     */
    protected ObjectTree getObjectTree(IGozerFrameExtension gfe)
    {
        try
        {
            String layoutFileName = gfe.getGozerLayoutFileName();

            // Search the gozer configuration file first in the JCR Repository
            // then on the file system.
            InputStream inputStream = GozerFileLoader.getGozerLayout(getApplication(), layoutFileName);

            SAXBuilder sb = new SAXBuilder();
            Document document = sb.build(inputStream);

            Parser parser = new Parser(gfe.getDataBindingContext());
            parser.createTree(document);
            ObjectTree ot = parser.getTree();

            return ot;
        }
        catch (JDOMException e)
        {
            String error = MessageFormat.format("Error when rendering ObjectTree with GozerFile: {0}, Error: {1}, Cause: {2}",
                    gfe.getGozerLayoutFileName(), e, e.getCause());
            LOG.error(error);
        }
        catch (NullPointerException e)
        {
            String error = MessageFormat.format("Error when rendering ObjectTree with GozerFile: {0}, Error: {1}, Cause: {2}",
                    gfe.getGozerLayoutFileName(), e, e.getCause());
            LOG.error(error);
            e.printStackTrace();
        }
        catch (Exception e)
        {
            String error = MessageFormat.format("Error when rendering ObjectTree with GozerFile: {0}, Error: {1}, Cause: {2}",
                    gfe.getGozerLayoutFileName(), e,  e.getCause());
            LOG.error(error);
        }

        return null;
    }
}