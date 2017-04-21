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
 ** $Id:  $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.frame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.swordlord.common.prefs.UserPrefs;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.report.*;
import com.swordlord.gozer.databinding.DataBindingContext;
import com.swordlord.gozer.databinding.DataBindingMember;
import com.swordlord.gozer.eventhandler.generic.GozerActionEvent;
import com.swordlord.gozer.session.IGozerSessionInfo;
import com.swordlord.jalapeno.businessobject.BusinessObjectBase;
import com.swordlord.jalapeno.datacontainer.DataContainer;
import com.swordlord.jalapeno.datarow.DataRowBase;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.SQLTemplate;

/**
 * @author awiesmann
 * 
 */
@SuppressWarnings("serial")
public class DefaultReportPanelExtension extends GozerReportPanelExtensionBase
{
    private DataContainer _dc;
    private String _targetId;
    private UUID _foreignKey;
    private RootPage _rootPage;

    /**
     * @param sessionInfo
     * @param gozerLayoutFileName
     * @param parentContext
     */
    public DefaultReportPanelExtension(IGozerSessionInfo sessionInfo, String gozerLayoutFileName, DataBindingContext parentContext)
    {
        super(sessionInfo, gozerLayoutFileName, parentContext);
        if (parentContext.getGozerFrameExtension() instanceof GenericReportFrameExtension)
        {
            GenericReportFrameExtension grfe = (GenericReportFrameExtension) parentContext.getGozerFrameExtension();
            if (grfe.getForeignKey() != null)
                _foreignKey = grfe.getForeignKey();
        }
    }

    /**
     * Constructor.
     * 
     * @param sessionInfo
     * @param gozerLayoutFileName
     * @param parentContext
     * @param targetId
     *            Id for the call from one graph to another graph.
     */
    public DefaultReportPanelExtension(IGozerSessionInfo sessionInfo, String gozerLayoutFileName, DataBindingContext parentContext, String targetId)
    {
        super(sessionInfo, gozerLayoutFileName, parentContext);
        _targetId = targetId;
    }

    /**
     * Constructor.
     * 
     * @param sessionInfo
     * @param gozerLayoutFileName
     * @param parentContext
     * @param foreignKey
     *            Id for the call with a foreign key
     */
    public DefaultReportPanelExtension(IGozerSessionInfo sessionInfo, String gozerLayoutFileName, DataBindingContext parentContext, UUID foreignKey)
    {
        super(sessionInfo, gozerLayoutFileName, parentContext);
        _foreignKey = foreignKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoad()
    {
        super.onLoad();

        final GQueries queries = getQueries();

        Class<?> rootObjectClass = null;
        String queryType = null;
        String sql = null;
        final Map<String, Object> params = new HashMap<String, Object>();

        if (queries == null)
        {
            LOG.error("Something really wrong happened. Queries are empty in DefaultReportPanelExtension.");
            return;
        }

        for (ObjectBase child : queries.getChildren())
        {
            if (child instanceof GQuery)
            {
                final GQuery query = (GQuery) child;
                rootObjectClass = resolveRootObjectClass(query.getAttribute(GQuery.ATTRIBUTE_ROOTOBJECT));
                queryType = query.getAttribute(GQuery.ATTRIBUTE_TYPE);
                sql = query.getContent();
            }
            else if (child instanceof GParams)
            {
                resolveParams((GParams) child, params);
            }
        }

        if ("SQL".equals(queryType))
        {
            executeQuery(rootObjectClass, sql, params, (DataContext) getDataContainer().getObjectContext());
        }
        else
        {
            LOG.warn("Query type is not supported: " + queryType);
        }
    }

    /**
     * Executes the specified SQL statement.
     * 
     * @param rootObjectClass
     *            The root object class
     * @param sql
     *            The sql statement
     * @param params
     *            The parameters for the sql statement
     * @param ctxt
     *            The data context to use for executing the statement and
     *            placing the results
     */
    @SuppressWarnings("unchecked")
    private void executeQuery(Class<?> rootObjectClass, String sql, Map<String, Object> params, DataContext ctxt)
    {
        final SQLTemplate q = new SQLTemplate(rootObjectClass, sql);
        q.setFetchingDataRows(true);
        // q.setCacheStrategy(QueryCacheStrategy.NO_CACHE);

        try
        {
            final List<Map<String, Object>> result;
            if (!params.isEmpty())
            {
                result = ctxt.performQuery(q.createQuery(params));
            }
            else
            {
                result = ctxt.performQuery(q);
            }

            for (Map<String, Object> rowMap : result)
            {
                final DataRowBase row = (DataRowBase) ctxt.newObject(rootObjectClass);
                for (Entry<String, Object> entry : rowMap.entrySet())
                {
                    row.setProperty(entry.getKey(), entry.getValue());
                }
            }
        }
        catch (Exception ex)
        {
            LOG.error("Query in Report Panel crashed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Resolves the real class for the given root object name.
     * 
     * @param name
     *            The root object name (as hold in
     *            {@link GQuery#ATTRIBUTE_ROOTOBJECT})
     * @return The class or null if it could not be resolved
     */
    private Class<?> resolveRootObjectClass(String name)
    {
        final String rootObjectFullClassName = UserPrefs.APP_OBJECT_DATAROW_PACKAGE + "." + name.substring(0, name.indexOf("DataRow")) + "." + name;
        try
        {
            return Class.forName(rootObjectFullClassName);
        }
        catch (ClassNotFoundException e)
        {
            LOG.error(rootObjectFullClassName + " does not appear to be a valid class");
            LOG.debug(e.getMessage());
        }
        return null;
    }

    /**
     * Resolves the values for the parameter in the given object and stores the
     * result in the passed map.
     * 
     * @param rawParams
     *            The raw parameter element as obtained by the parser
     * @param resolvedParams
     *            The result map were to put the resolved parameters
     */
    private void resolveParams(GParams rawParams, final Map<String, Object> resolvedParams)
    {
        for (ObjectBase param : rawParams.getChildren())
        {
            if (param instanceof GContextParam)
            {
                resolveContextParam((GContextParam) param, resolvedParams);
            }
            else if (param instanceof GDataBindingParam)
            {
                resolveBindingParameter((GDataBindingParam) param, resolvedParams);
            }
            else
            {
                LOG.warn("Parameter type is not supported: " + param.getClass().getName());
            }
        }
    }

    /**
     * Resolves the given context parameter.
     * 
     * @param rawParam
     *            The raw parameter to resolve
     * @param resolvedParams
     *            The result map were to put the resolved parameters
     */
    private void resolveContextParam(GContextParam rawParam, Map<String, Object> resolvedParams)
    {
        final String ident = rawParam.getParamIdent();
        // these are predefined parameters which need to be registered
        // and which are then filled by the repository automatically
        if ("target_id".equals(ident))
        {
            // this parameter is set by the drill down mechanism within
            // the ReportController
            resolvedParams.put(ident, _targetId);
        }
        else if ("language_code".equals(ident))
        {
            resolvedParams.put(ident, UserPrefs.instance().getLanguageCode());
        }
        else if ("foreign_key".equals(ident))
        {
            resolvedParams.put(ident, _foreignKey.toString());
        }
        else
        {
            LOG.warn("Unknown context parameter: " + ident);
        }
    }

    /**
     * Resolves the given parameter using the it's data binding attribute (
     * {@link GDataBindingParam#ATTRIBUTE_BINDING_MEMBER}).
     * 
     * @param rawParam
     *            The raw parameter to resolve
     * @param resolvedParams
     *            The result map were to put the resolved parameters
     */
    private void resolveBindingParameter(GDataBindingParam rawParam, Map<String, Object> resolvedParams)
    {
        // all other parameters are taken from the binding information from the
        // parent panel
        final String binding = rawParam.getAttribute(GParam.ATTRIBUTE_BINDING_MEMBER);
        final DataBindingMember dbMember = new DataBindingMember(binding);
        final DataRowBase row = getParentDataBindingContext().getDataBindingManager(dbMember).getCurrentRow(dbMember);
        if (row == null)
        {
            return;
        }
        final Object property = row.getProperty(dbMember.getDataBindingFieldName());
        if (property != null)
        {
            resolvedParams.put(rawParam.getAttribute(GParam.ATTRIBUTE_PARAM_IDENT), property);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataContainer getDataContainer()
    {
        if (_dc == null)
        {
            _dc = new DataContainer();
        }
        return _dc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BusinessObjectBase getBusinessObject()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getFrameId()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getHelpId()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RootPage getPage()
    {
        return _rootPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPage(RootPage page)
    {
        _rootPage = page;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPostPersist(GozerActionEvent event)
    {
    }
}
