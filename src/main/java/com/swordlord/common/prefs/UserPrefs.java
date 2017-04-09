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
** $Id: $
**
-----------------------------------------------------------------------------*/
package com.swordlord.common.prefs;

// IMPORTS
import java.io.File;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.ex.ConversionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User preferences
 * 
 * @author LordEidi
 * 
 */
public class UserPrefs implements IUserPrefs
{
    private static final Log LOG = LogFactory.getLog(UserPrefs.class);

    /** The default cayenne configuration file. */
    private static final String DEFAULT_CAYENNE_CONFIG = "cayenne.xml";

    // prefix for all datarow classes
    public static String APP_OBJECT_DATAROW_PACKAGE = "org.somap.repository.datarow";

	// foldername for extensions
    public static String APP_EXTENSION_FOLDER = "extensions";

	// node name for all properties
    private static String PROPERTY_KEY = "properties";

    // relative pathname for jcr folder
    public static String JCR_REPOSITORY = "jcr/";

    // relative pathname for ressources
    public static String APP_RSRC_FOLDER = "";

	// property (node) name for current language
    // TODO: this needs to be user specific!
    public static String PROPERTY_LANGUAGE = "language";

    // pathnames
    private static String APP_REPORTFRAMES_FILES_FOLDER_PREFIX = APP_RSRC_FOLDER + File.separator + "gozer" + File.separator;
    public static String APP_GOZERFRAMES_FILES_FOLDER_PREFIX = APP_RSRC_FOLDER + File.separator + "gozer" + File.separator;
    public static String APP_JCRROOT_FILES_FOLDER_PREFIX = APP_RSRC_FOLDER + File.separator + "jcr_root" + File.separator;
    public static String APP_COMMON_GOZER_FILES_FOLDER = APP_GOZERFRAMES_FILES_FOLDER_PREFIX + "common" + File.separator + "frame" + File.separator;
    public static String APP_CONTENT_GOZER_FILES_FOLDER = APP_GOZERFRAMES_FILES_FOLDER_PREFIX + "content" + File.separator + "frame" + File.separator;
    public static String APP_REPOSITORY_GOZER_FILES_FOLDER = APP_GOZERFRAMES_FILES_FOLDER_PREFIX + "repository" + File.separator + "frame" + File.separator;
    public static String APP_USER_GOZER_FILES_FOLDER = APP_GOZERFRAMES_FILES_FOLDER_PREFIX + "user" + File.separator + "frame" + File.separator;
    public static String APP_WORKFLOW_GOZER_FILES_FOLDER = APP_GOZERFRAMES_FILES_FOLDER_PREFIX + "workflow" + File.separator + "frame" + File.separator;
    public static String APP_REPORT_GOZER_FILES_FOLDER = APP_REPORTFRAMES_FILES_FOLDER_PREFIX;
    public static String APP_CUSTOMISATION_GOZER_FILES_FOLDER = APP_GOZERFRAMES_FILES_FOLDER_PREFIX + "customisation" + File.separator + "frame" + File.separator;
    public static String APP_ENVIRONMENT_GOZER_FILES_FOLDER = APP_GOZERFRAMES_FILES_FOLDER_PREFIX + "environment" + File.separator + "frame" + File.separator;
    
    // assessments
    // common methodology
    private static String APP_ASSESSMENT_METHODOLOGY_PREFIX = APP_GOZERFRAMES_FILES_FOLDER_PREFIX + "assessment" + File.separator + "methodology" + File.separator;
    // all different methodologies
    public static String APP_GAP_ANALYSIS_GOZER_FILES_FOLDER = APP_ASSESSMENT_METHODOLOGY_PREFIX + "gap_analysis" + File.separator + "frame" + File.separator;
    public static String APP_QUANTITATIVE_ANALYSIS_GOZER_FILES_FOLDER = APP_ASSESSMENT_METHODOLOGY_PREFIX + "quantitative_analysis" + File.separator + "frame" + File.separator;
    public static String APP_QUALITATIVE_ANALYSIS_GOZER_FILES_FOLDER = APP_ASSESSMENT_METHODOLOGY_PREFIX + "qualitative_analysis" + File.separator + "frame" + File.separator;

    public String resolveTargetUI()
    {
        // TODO use default 'getProperty' method
        String result = System.getProperty("target.ui");
        if (result == null)
        {
            LOG.warn("Couldn't determine target.ui, assume wicket");
            result = "wicket";
        }

		LOG.debug("Configuration: target.ui=" + result);
        return result;
    }

    /**
     * Returns the path of the cayenne configuration to use for the
     * {@link com.swordlord.jalapeno.DBConnection}.<br>
     * The default configuration name may be overwritten using the system
     * property <code>cayenne.config</code>.
     * 
     * @return The cayenne configuration file
     */
    public String getCayenneConfig()
    {
        // TODO use default 'getProperty' method
        return getSystemProperty("cayenne.config", DEFAULT_CAYENNE_CONFIG);
    }

    private String _sAppName;

    Configurations configs = new Configurations();
	private XMLConfiguration _config;

	//
    public UserPrefs(String sAppName)
    {
        _sAppName = sAppName;

        try {
            _config = configs.xml(_sAppName + ".appconf");
        }
        catch(ConfigurationException cex)
        {
            LOG.error(cex);
        }

    }

	// -------------------------------------------------
    public String getAppConf(String strVar, String strDefault)
    {
        return getAppConf(strVar, strDefault, "");
    }

	public String getAppConf(String strVar, String strDefault, String strSection)
    {
        String strReturn = strDefault;

		try
        {

			if ((strSection != null) && (strSection.length() > 0))
            {
                strReturn = _config.getString(strSection + "." + strVar, strDefault);
            }
            else
            {
                strReturn = _config.getString(strVar, strDefault);
            }
            strReturn = strReturn.trim();
        }
        catch (ConversionException e)
        {
            LOG.error("Property Error: strVar='" + strVar + "' strDefault='" + strDefault + "' strSection='" + strSection + "'", e);
        }
        catch (Exception e)
        {
            LOG.error("Property Error: strVar='" + strVar + "' strDefault='" + strDefault + "' strSection='" + strSection + "'", e);
        }

        LOG.debug("getAppConf(strVar='" + strVar + "' strDefault='" + strDefault + "' strSection='" + strSection + "')=" + strReturn);

		return strReturn;
    }

	public String getLanguageCode()
    {
        return getAppConf(UserPrefs.PROPERTY_LANGUAGE, "en");
    }

	public int getLanguageCodeAsInt()
    {
        String strLang = getLanguageCode();

		if (strLang.compareTo("en") == 0)
        {
            return 0;
        }
        else if (strLang.compareTo("de") == 0)
        {
            return 1;
        }

	    return -1;
    }

	// -------------------------------------------------
    // get property as string
    @SuppressWarnings("rawtypes")
    public String getProperty(String strVar, String strDefault, Class oClass)
    {
        return getProperty(strVar, strDefault, oClass.getName());
    }

    public String getProperty(String strVar, String strDefault, String strSection)
    {
        // overwriting through system properties has highest priority
        String strReturn = getSystemProperty(strSection + "." + strVar, null);

        // otherwise we check the config
        if (strReturn == null)
        {
            try
            {
                strReturn = _config.getString(PROPERTY_KEY + "." + strSection + "." + strVar, null);
            }
            catch (ConversionException e)
            {
                LOG.error(e);
            }
            catch (Exception e)
            {
                LOG.warn(e);
            }
        }

        // and as last resort we use the default value
        if (strReturn == null)
        {
            strReturn = strDefault;
            // write it out for further use
            setProperty(strVar, strDefault, strSection);
        }
        LOG.debug("getProperty(strVar='" + strVar + "' strDefault='" + strDefault + "' strSection='" + strSection + "')=" + strReturn);
        return strReturn;
    }

	@SuppressWarnings("rawtypes")
    public boolean getPropertyAsBoolean(String strVar, boolean bDefault, Class oClass)
    {
        return Boolean.parseBoolean(getProperty(strVar, Boolean.toString(bDefault), oClass.getName()));
    }

	// -------------------------------------------------
    // get property as int
    @SuppressWarnings("rawtypes")
    public int getPropertyAsInt(String strVar, int nDefault, Class oClass)
    {
        return getPropertyAsInt(strVar, nDefault, oClass.getName());
    }

	public int getPropertyAsInt(String strVar, int nDefault, String strSection)
    {
        String strValue = getProperty(strVar, Integer.toString(nDefault), strSection);

		int nReturn = nDefault;
        try
        {
            // if found, overwrite the default value
            nReturn = Integer.parseInt(strValue);
        }
        catch (Exception e)
        {
            LOG.info(e);
        }

		return nReturn;
    }

    /**
     * Returns a system property or a default value.
     * 
     * @param property
     *            The property name
     * @param defaultValue
     *            The default value
     * @return The property value or the passed default if no such property was
     *         defined
     */
    private static String getSystemProperty(String property, String defaultValue)
    {
        final String cfg = System.getProperty(property);
        return cfg != null ? cfg : defaultValue;
    }

	public String getVersion()
    {
        return getAppConf("version", "0.0.0");
    }

	public boolean isDebugMode()
    {
        return getAppConf("debug", "no").equals("yes");
    }

	// -----------------------------------------------------------------------------
    // saves the XML config file back to the filesystem
    public void save()
    {
        String targetUI = resolveTargetUI();

		if (targetUI.equals("wicket"))
        {
            LOG.info("save(): do not save in wicket mode.");
            return;
        }

		try
        {
            //_config.save();
            // TODO config store missing
            LOG.error("Not implemented yet");
        }
        catch (Exception e)
        {
            LOG.error(e);
        }
    }

	// internal method to set the PROPERTY tags
    // does update or add newly. either or :)
    private void setAppConfInternal(String strKey, String strValue)
    {
        if (strValue == null)
        {
            return;
        }
        _config.setProperty(strKey, strValue);
        save();
    }

	public void setLanguageCode(String strLanguageCode)
    {
        setAppConfInternal(UserPrefs.PROPERTY_LANGUAGE, strLanguageCode);
    }

    @SuppressWarnings("rawtypes")
    public void setProperty(String strVar, boolean b, Class oClass)
    {
        setProperty(strVar, new Boolean(b).toString(), oClass.getName());
    }

	// -------------------------------------------------
    // set property as string
    @SuppressWarnings("rawtypes")
    public void setProperty(String strVar, String strDefault, Class oClass)
    {
        setProperty(strVar, strDefault, oClass.getName());
    }

	public void setProperty(String strVar, String strDefault, String strSection)
    {
        setPropertyInternal(strSection, strVar, strDefault);
    }

	// -------------------------------------------------
    // set property as in
    @SuppressWarnings("rawtypes")
    public void setPropertyAsInt(String strVar, int nDefault, Class oClass)
    {
        setPropertyAsInt(strVar, nDefault, oClass.getName());
    }

	public void setPropertyAsInt(String strVar, int nDefault, String strSection)
    {
        setPropertyInternal(strSection, strVar, Integer.toString(nDefault));
    }

	// -------------------------------------------------
    // internal method to set the PROPERTY tags
    // does update or add newly. either or :)
    private void setPropertyInternal(String strSection, String strVarName, String strValue)
    {
        if (strValue == null)
        {
            return;
        }

		String key = PROPERTY_KEY + "." + strSection + "." + strVarName;
        String property = _config.getString(key);
        if ((property == null) || (!property.equals(strValue)))
        {
            _config.setProperty(key, strValue);
            save();
        }
    }
}
// eof