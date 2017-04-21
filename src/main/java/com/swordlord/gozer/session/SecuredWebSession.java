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
** $Id: $
**
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.session;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.request.Request;

/**
 * Subclass of WebSession which contains authentication mechanisms based on Apache Shiro.
 * 
 * @author LordEidi
 */ 
@SuppressWarnings("serial")
public class SecuredWebSession extends AuthenticatedWebSession implements IGozerSessionInfo
{
	private static final Log LOG_SEC = LogFactory.getLog(SecuredWebSession.class);

	private String _currentUserShortName;
	private Date _dtmLastLogin;
	
	private UUID _mountPoint;
	private UUID _selectedMenu;
	
	private SimpleAuthorizationInfo _ai;
	
	/**
	 * @return Current authenticated web session
	 */
	public static SecuredWebSession get()
	{
		return (SecuredWebSession)Session.get();
	}

	/**
	 * Construct.
	 * 
	 * @param request
	 *            The current request object
	 */
	public SecuredWebSession(Request request)
	{
		super(request);
	}

	/**
	 * Authenticates this session using the given username and password
	 * 
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @return True if the user was authenticated successfully
	 */
	@Override
    public boolean authenticate(final String username, final String password)
	{
		//importDemoData();
    	
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		try
		{
			currentUser.login(token);
			
			// if we come this far, the login worked!
	    	DataContainer dc = new DataContainer();

			SubjectDataTable dt = new SubjectDataTable(dc);
			SubjectDataRow row = dt.fillByName(username);

			if(row == null)
			{
				LOG_SEC.error(MessageFormat.format("Something terrible happened with: User {0}", username));
				return false;
			}

			SubjectDataRowKey keySubject = row.getKey();

	    	setCurrentUser(row.getShortName());
	    	setCurrentAssessment(keySubject);

	    	_dtmLastLogin = row.getLastLoginDate();

	    	row.setLastLoginDate(new Date());
			dc.persist();
			
			LOG_SEC.info(MessageFormat.format("Login successfull: User {0} with token {1}", username, token));
			
			return true;

			// the following exceptions are just a few you can catch and handle accordingly. See the
			// AuthenticationException JavaDoc and its subclasses for more.
		}
		catch (IncorrectCredentialsException ice)
		{
			LOG_SEC.info(ice.getMessage());
			error("Invalid username and/or password.");
		}
		catch (UnknownAccountException uae)
		{
			LOG_SEC.info(uae.getMessage());
			error("There is no account with that username.");
		}
		catch (AuthenticationException ae)
		{
			LOG_SEC.info(ae.getMessage());
			error("Invalid username and/or password.");
		}
		catch( Exception ex ) 
		{
			LOG_SEC.info(ex.getMessage());
			error("Login failed");
		}
		
		return false;

	}

	/**
	 * @return Get the roles that this session can play
	 */
	@Override
    public org.apache.wicket.authroles.authorization.strategies.role.Roles getRoles()
	{
        if(_ai == null)
        {
        	return null;
        }
        
        Set<String> setRoles = _ai.getRoles();
        final String[] arrRoles = (String[]) setRoles.toArray();
        
        return new org.apache.wicket.authroles.authorization.strategies.role.Roles(arrRoles);
    }
	
    /**
     * @return
     */
	public AuthorizationInfo getAuthorizationInfo()
	{
		return _ai;
	}
	
    /**
     * @return
     */
	public boolean hasAuthorizationInfo()
	{
		return _ai != null;
	}
	
    /**
     * @param ai
     */
	public void setAuthorizationInfo(SimpleAuthorizationInfo ai)
	{
		_ai = ai;
	}
	
    /**
     * @param roles
     */
	public void setRoles(Set<String> roles)
	{
		if(_ai == null)
		{
			_ai = new SimpleAuthorizationInfo();
		}
		
		_ai.setRoles(roles);
		
	}

    /* (non-Javadoc)
     * @see org.somap.gozer.session.IGozerSessionInfo#hasRole(java.lang.String)
     */
    @Override
    public boolean hasRole(String strRoleIdentifier)
    {
    	Subject currentUser = SecurityUtils.getSubject();
    	return currentUser.hasRole(strRoleIdentifier);
    }
    
    /**
     * @param strPermissionIdentifier
     * @return
     */
    @Override
    public boolean isPermitted(String strPermissionIdentifier)
    {
    	Subject currentUser = SecurityUtils.getSubject();
    	return currentUser.isPermitted(strPermissionIdentifier);
    }

	/**
	 * @return True if the user is signed in to this session
	 */
	@Override
    public boolean isAuthenticated()
	{
		return SecurityUtils.getSubject().isAuthenticated();
	}

	/**
	 * Sign the user out.
	 */
	@Override
    public void signOut()
	{
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		_ai = null;
	}

	@Override
    public SubjectDataRowKey getCurrentUser()
	{
		Subject currentUser = SecurityUtils.getSubject();
		return (SubjectDataRowKey) currentUser.getPrincipal();
	}

    /**
     * @return
     */
	public String getCurrentUserShortName()
	{
		return _currentUserShortName;
	}

	@Override
    public Date getLastLoginDate()
	{
		return _dtmLastLogin;
	}

    /**
     * @param currentUserShortName
     */
	public void setCurrentUser(String currentUserShortName)
	{
		this._currentUserShortName = currentUserShortName;
	}

	@Override
    public UUID getMountPoint() 
	{
		return _mountPoint;
	}

	@Override
    public void setMountPoint(UUID uuid) 
	{
		_mountPoint = uuid;	
	}

	@Override
    public UUID getSelectedMenu() 
	{
		return _selectedMenu;
	}

	@Override
    public void setSelectedMenu(UUID uuid) 
	{
		_selectedMenu = uuid;
	}

    /**
     * Authenticates this session using the given token
     * 
     * @param token
     *            The Technical token
     * @return True if the user was authenticated successfully
     */
	public boolean authenticate(final TechnicalToken token)
	{
		Subject currentUser = SecurityUtils.getSubject();
		try
		{
			currentUser.login(token);
			
			// if we come this far, the login worked!
	    	DataContainer dc = new DataContainer();

			SubjectDataTable dt = new SubjectDataTable(dc);
			SubjectDataRow row = dt.fillByName(token.getUsername());

			if(row == null)
			{
				LOG_SEC.error(MessageFormat.format("Something terrible happened with: User {0}", token.getUsername()));
				return false;
			}

			SubjectDataRowKey keySubject = row.getKey();

	    	setCurrentUser(row.getShortName());
	    	setCurrentAssessment(keySubject);

	    	_dtmLastLogin = row.getLastLoginDate();

	    	row.setLastLoginDate(new Date());
			dc.persist();
			
			LOG_SEC.info(MessageFormat.format("Login successfull: User {0} with token {1}", token.getUsername(), token));
			
			return true;

			// the following exceptions are just a few you can catch and handle accordingly. See the
			// AuthenticationException JavaDoc and its subclasses for more.
		}
		catch (IncorrectCredentialsException ice)
		{
			LOG_SEC.info(ice.getMessage());
			error("Invalid username and/or password.");
		}
		catch (UnknownAccountException uae)
		{
			LOG_SEC.info(uae.getMessage());
			error("There is no account with that username.");
		}
		catch (AuthenticationException ae)
		{
			LOG_SEC.info(ae.getMessage());
			error("Invalid username and/or password.");
		}
		catch( Exception ex ) 
		{
			LOG_SEC.info(ex.getMessage());
			error("Login failed");
		}
		
		return false;

	}
}