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
 ** $Id: Parser.java 1344 2011-12-28 11:23:25Z LordEidi $
 **
-----------------------------------------------------------------------------*/

package com.swordlord.gozer.builder;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.swordlord.jalapeno.DBGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import com.swordlord.gozer.components.generic.GActionBox;
import com.swordlord.gozer.components.generic.GActionToolbar;
import com.swordlord.gozer.components.generic.GDetail;
import com.swordlord.gozer.components.generic.GImage;
import com.swordlord.gozer.components.generic.GLabel;
import com.swordlord.gozer.components.generic.GList;
import com.swordlord.gozer.components.generic.GListAndDetail;
import com.swordlord.gozer.components.generic.ObjectBase;
import com.swordlord.gozer.components.generic.action.GActivateAction;
import com.swordlord.gozer.components.generic.action.GAddAction;
import com.swordlord.gozer.components.generic.action.GAddReferenceAction;
import com.swordlord.gozer.components.generic.action.GCancelAction;
import com.swordlord.gozer.components.generic.action.GCsvAction;
import com.swordlord.gozer.components.generic.action.GDefaultFrameActions;
import com.swordlord.gozer.components.generic.action.GDeleteAction;
import com.swordlord.gozer.components.generic.action.GDetailAction;
import com.swordlord.gozer.components.generic.action.GEditAction;
import com.swordlord.gozer.components.generic.action.GFirstAction;
import com.swordlord.gozer.components.generic.action.GFopAction;
import com.swordlord.gozer.components.generic.action.GGoBackAction;
import com.swordlord.gozer.components.generic.action.GLastAction;
import com.swordlord.gozer.components.generic.action.GNewAction;
import com.swordlord.gozer.components.generic.action.GNextAction;
import com.swordlord.gozer.components.generic.action.GOtherAction;
import com.swordlord.gozer.components.generic.action.GPersistAction;
import com.swordlord.gozer.components.generic.action.GPrevAction;
import com.swordlord.gozer.components.generic.action.GRemoveAction;
import com.swordlord.gozer.components.generic.action.GReportAction;
import com.swordlord.gozer.components.generic.action.GToggleAction;
import com.swordlord.gozer.components.generic.box.GFrame;
import com.swordlord.gozer.components.generic.box.GHBox;
import com.swordlord.gozer.components.generic.box.GTabbedPanel;
import com.swordlord.gozer.components.generic.box.GVBox;
import com.swordlord.gozer.components.generic.crosstab.GCrossTab;
import com.swordlord.gozer.components.generic.field.GCodeField;
import com.swordlord.gozer.components.generic.field.GField;
import com.swordlord.gozer.components.generic.field.GLibraryField;
import com.swordlord.gozer.components.generic.field.GLinkField;
import com.swordlord.gozer.components.generic.field.GLookupField;
import com.swordlord.gozer.components.generic.field.GOneToNField;
import com.swordlord.gozer.components.generic.field.GPredefinedListField;
import com.swordlord.gozer.components.generic.graph.GAreaChart;
import com.swordlord.gozer.components.generic.graph.GPieChart;
import com.swordlord.gozer.components.generic.graph.GStackedBarChart;
import com.swordlord.gozer.components.generic.report.GContextParam;
import com.swordlord.gozer.components.generic.report.GDataBindingParam;
import com.swordlord.gozer.components.generic.report.GParams;
import com.swordlord.gozer.components.generic.report.GQueries;
import com.swordlord.gozer.components.generic.report.GQuery;
import com.swordlord.gozer.components.generic.report.GReport;
import com.swordlord.gozer.components.generic.report.GReportPanel;
import com.swordlord.gozer.databinding.DataBindingContext;

/**
 * @author LordEidi
 * 
 */
public class Parser
{
    protected static final Log LOG = LogFactory.getLog(Parser.class);

    private Hashtable<String, Class<? extends ObjectBase>> _objectTags;
	private ObjectTree _objectTree;

	/**
     * Parser
     * 
     * @param _context
     */
	public Parser(DataBindingContext _context)
	{
		_objectTags = new Hashtable<String, Class<? extends ObjectBase>>();
		_objectTree = new ObjectTree(_context);

		registerObjectTags();
	}

	/**
     * Create the tree
     * 
     * @param document
     */
	public void createTree(Document document)
	{
		Element root = document.getRootElement();

		parseElement(root, null);
	}

	/**
     * Get Tree
     * 
     * @return the tree
     */
	public ObjectTree getTree()
	{
		return _objectTree;
	}

	
	private <T extends ObjectBase> T instantiateClass(Class<T> c)
	{
		T ob = null;

		if (c != null)
		{
			try
			{
				Constructor<T> constructor = c.getConstructor(new Class[] { ObjectTree.class });
				ob = constructor.newInstance(_objectTree);
			} 
			catch (InstantiationException e)
			{
				LOG.error("instantiateClass failed", e);
			} 
			catch (IllegalAccessException e)
			{
				LOG.error("instantiateClass failed", e);
			} 
			catch (NoSuchMethodException e)
			{
				LOG.error("instantiateClass failed", e);
			} 
			catch (InvocationTargetException e)
			{
				LOG.error("instantiateClass failed", e);
			}
		}

		return ob;
	}

	@SuppressWarnings("unchecked")
	private void parseElement(Element element, ObjectBase parent)
	{
		if (!_objectTags.containsKey(element.getName()))
		{
			String msg = MessageFormat.format("Element {0} unknown, parsing aborted.", element.getName());
			LOG.error(msg);
			return;
		}

		ObjectBase ob = instantiateClass(_objectTags.get(element.getName()));
		if (ob == null)
		{
			String msg = MessageFormat.format("Class for {0} could not be instantiated, parsing aborted.", element);
			LOG.error(msg);
			return;
		}

		if (element.getText() != null)
		{
			ob.setContent(element.getText());
		}
		List attributes = element.getAttributes();
		Iterator itAttributes = attributes.iterator();
		while (itAttributes.hasNext())
		{
			Attribute attr = (Attribute) itAttributes.next();

			ob.putAttribute(attr.getName(), attr.getValue());
		}

		if (parent != null)
		{
			ob.inheritParent(parent);
			parent.putChild(ob);
		} 
		else
		{
			_objectTree.setRoot(ob);
		}

		List children = element.getChildren();
		Iterator itChildren = children.iterator();
		while (itChildren.hasNext())
		{
			parseElement((Element) itChildren.next(), ob);
		}
	}
	
	
	/**
     * set link to parent
     * 
     * @param parent
     * @param ob
     */
	public static void inherit(ObjectBase parent, ObjectBase ob)
	{
		if (parent != null)
		{
			ob.inheritParent(parent);
			parent.putChild(ob);
		}
	}

	private void registerObjectTags()
	{
		// Components
		_objectTags.put(GActionBox.getObjectTag(), GActionBox.class);
		_objectTags.put(GActionToolbar.getObjectTag(), GActionToolbar.class);
		// _objectTags.put(GBox.getObjectTag(), GBox.class);
		_objectTags.put(GDetail.getObjectTag(), GDetail.class);
		_objectTags.put(GField.getObjectTag(), GField.class);
		_objectTags.put(GCodeField.getObjectTag(), GCodeField.class);
		_objectTags.put(GLookupField.getObjectTag(), GLookupField.class);
		_objectTags.put(GFrame.getObjectTag(), GFrame.class);
		_objectTags.put(GHBox.getObjectTag(), GHBox.class);
		_objectTags.put(GVBox.getObjectTag(), GVBox.class);
		_objectTags.put(GImage.getObjectTag(), GImage.class);
		_objectTags.put(GLabel.getObjectTag(), GLabel.class);
		_objectTags.put(GLinkField.getObjectTag(), GLinkField.class);
		_objectTags.put(GList.getObjectTag(), GList.class);
		_objectTags.put(GListAndDetail.getObjectTag(), GListAndDetail.class);
		_objectTags.put(GTabbedPanel.getObjectTag(), GTabbedPanel.class);
		_objectTags.put(GLibraryField.getObjectTag(), GLibraryField.class);
		_objectTags.put(GOneToNField.getObjectTag(), GOneToNField.class);
		_objectTags.put(GPredefinedListField.getObjectTag(), GPredefinedListField.class);		

		// Graph
		_objectTags.put(GPieChart.getObjectTag(), GPieChart.class);
		_objectTags.put(GAreaChart.getObjectTag(), GAreaChart.class);
		_objectTags.put(GStackedBarChart.getObjectTag(), GStackedBarChart.class);

		// Reports
		_objectTags.put(GReport.getObjectTag(), GReport.class);
		_objectTags.put(GReportPanel.getObjectTag(), GReportPanel.class);
		_objectTags.put(GQueries.getObjectTag(), GQueries.class);
		_objectTags.put(GQuery.getObjectTag(), GQuery.class);
		_objectTags.put(GParams.getObjectTag(), GParams.class);
        _objectTags.put(GDataBindingParam.getObjectTag(), GDataBindingParam.class);
        _objectTags.put(GContextParam.getObjectTag(), GContextParam.class);
		
		// CrossTab
		_objectTags.put(GCrossTab.getObjectTag(), GCrossTab.class);

		// Actions
		_objectTags.put(GCancelAction.getObjectTag(), GCancelAction.class);
		_objectTags.put(GDeleteAction.getObjectTag(), GDeleteAction.class);
		_objectTags.put(GDetailAction.getObjectTag(), GDetailAction.class);
		_objectTags.put(GDefaultFrameActions.getObjectTag(), GDefaultFrameActions.class);
		_objectTags.put(GEditAction.getObjectTag(), GEditAction.class);
		_objectTags.put(GFirstAction.getObjectTag(), GFirstAction.class);
		_objectTags.put(GFopAction.getObjectTag(), GFopAction.class);
		_objectTags.put(GReportAction.getObjectTag(), GReportAction.class);
		_objectTags.put(GCsvAction.getObjectTag(), GCsvAction.class);
		_objectTags.put(GGoBackAction.getObjectTag(), GGoBackAction.class);
		_objectTags.put(GLastAction.getObjectTag(), GLastAction.class);
		_objectTags.put(GNewAction.getObjectTag(), GNewAction.class);
		_objectTags.put(GNextAction.getObjectTag(), GNextAction.class);
		_objectTags.put(GPersistAction.getObjectTag(), GPersistAction.class);
		_objectTags.put(GPrevAction.getObjectTag(), GPrevAction.class);
		_objectTags.put(GToggleAction.getObjectTag(), GToggleAction.class);
		_objectTags.put(GActivateAction.getObjectTag(), GActivateAction.class);
		_objectTags.put(GAddAction.getObjectTag(), GAddAction.class);
		_objectTags.put(GAddReferenceAction.getObjectTag(), GAddReferenceAction.class);
		_objectTags.put(GRemoveAction.getObjectTag(), GRemoveAction.class);
		_objectTags.put(GOtherAction.getObjectTag(), GOtherAction.class);
	}

    /**
     * Helper class for getClasses and getPackageNames (using Delegation)
     * 
     * @param pckgname
     * @return
     * @throws ClassNotFoundException
     */
	private static File getClassesHelper(String pckgname) throws ClassNotFoundException
	{
		// Get a File object for the package
		File directory = null;
		try
		{
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null)
			{
				throw new ClassNotFoundException("Can't get class loader.");
			}
			String path = pckgname.replace('.', '/');
			URL resource = cld.getResource(path);
			if (resource == null)
			{
				throw new ClassNotFoundException("No resource for " + path);
			}
			directory = new File(resource.getFile());
			return directory;
		} 
		catch (NullPointerException x)
		{
			throw new ClassNotFoundException(pckgname + " (" + directory + ") does not appear to be a valid package");
		}
	}

    /**
     * Retrieve a Class
     * 
     * @param pckgname
     * @param skipDollarClasses
     * @return the class
     */
	public static Class<?>[] getClasses(String pckgname, boolean skipDollarClasses)
	{
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

		try
		{
			File directory = getClassesHelper(pckgname);

			if (directory.exists())
			{
				// Get the list of the files contained in the package
				String[] files = directory.list();
				for (int i = 0; i < files.length; i++)
				{
					// we are only interested in .class files
					if (files[i].endsWith(".class"))
					{
						// get rid of the ".class" at the end
						String withoutclass = pckgname + '.' + files[i].substring(0, files[i].length() - 6);

						// in case we don't want $1 $2 etc. endings (i.e. common
						// in GUI classes)
						if (skipDollarClasses)
						{
							int dollar_occurence = withoutclass.indexOf("$");
							if (dollar_occurence != -1)
							{
								withoutclass = withoutclass.substring(0, dollar_occurence);
							}
						}

						// add this class to our list but avoid duplicates
						boolean already_contained = false;
						for (Class<?> c : classes)
						{
							if (c.getCanonicalName().equals(withoutclass))
							{
								already_contained = true;
							}
						}
						if (!already_contained)
						{
							classes.add(Class.forName(withoutclass));
						}
						// REMARK this kind of checking is quite slow using
						// reflection, it would be better
						// to do the class.forName(...) stuff outside of this
						// method and change the method
						// to only return an ArrayList with fqcn Strings. Also
						// in reality we have the $1 $2
						// etc. classes in our packages, so we are skipping some
						// "real" classes here
					}
				}
			} else
			{
				throw new ClassNotFoundException(pckgname + " does not appear to be a valid package");
			}
		} 
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		Class<?>[] classesA = new Class[classes.size()];
		classes.toArray(classesA);
		return classesA;
	}

	/**
     * Retrieve the package name
     * 
     * @param basepkgname
     * @return
     */
    public static List<String> getPackageNames(String basepkgname)
	{
		ArrayList<String> packages = new ArrayList<String>();
		try
		{
			File directory = getClassesHelper(basepkgname);
			if (directory.isDirectory() && directory.exists())
			{
				for (File f : directory.listFiles())
				{
					if (f.isDirectory())
					{
						packages.add(basepkgname + "." + f.getName());
					}
				}
			}
			return packages;
		} 
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * public static void main(String[] args) { try { // test getting classes
	 * Class[] ddd = getClasses("yourpackage.yoursubpackage", true); for (int
	 * i=0; i<ddd.length; i++) { System.out.println(ddd[i].getCanonicalName());
	 * }
	 * 
	 * // test getting packages ArrayList<String> packs =
	 * getPackageNames("yourpackage.yoursubpackage"); for (String s:packs) {
	 * System.out.println(s); } } catch (Exception ex) { ex.printStackTrace(); }
	 * }
	 */
}
