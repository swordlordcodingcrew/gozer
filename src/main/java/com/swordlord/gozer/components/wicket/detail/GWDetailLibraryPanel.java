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
 ** $Id: GWDetailLibraryPanel.java 1363 2012-10-19 15:22:22Z LordEidi $
 **
-----------------------------------------------------------------------------*/
package com.swordlord.gozer.components.wicket.detail;

import java.text.MessageFormat;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import com.swordlord.gozer.ui.icons.Icons;
import com.swordlord.gozer.components.generic.field.GLibraryField;
import com.swordlord.gozer.components.wicket.GWPanel;
import com.swordlord.gozer.components.wicket.action.button.GWFKeyButton;
import com.swordlord.gozer.databinding.DataBinding;
import com.swordlord.gozer.databinding.DataBindingField;
import com.swordlord.gozer.databinding.DataBindingModel;
import com.swordlord.gozer.eventhandler.generic.GozerController;
import com.swordlord.gozer.eventhandler.generic.GozerFrameStatus;
import com.swordlord.jalapeno.datarow.DataRowKeyBase;
import com.swordlord.repository.datarow.Library.LibraryDataRow;
import com.swordlord.repository.datarow.LibraryDetail.LibraryDetailDataRow;
import com.swordlord.repository.datarow.LibraryReference.LibraryReferenceDataRow;
import com.swordlord.repository.datatable.LibraryReference.LibraryReferenceDataTable;
import com.swordlord.sobf.wicket.ui.gozerframe.GWContext;
import com.swordlord.sobf.wicket.ui.library.ReferencesModalWindow;

/**
 * TODO JavaDoc for GWDetailLibraryPanel.java
 * 
 * @author LordEidi
 * 
 */
@SuppressWarnings("serial")
public class GWDetailLibraryPanel extends GWPanel
{
	//private ListView<Object> _libraryListView;
	private Image _imageLink;
	private ReferencesModalWindow _modal;
	private GWFKeyButton _btnFKey;
	
	private DataBinding _binding;
	
	private WebMarkupContainer _rv;
	
    /**
	 * 
	 */
	protected boolean _isReadOnly;

    /**
     * @param id
     * @param model
     * @param fieldForm
     * @param detailPanel
     */
	public GWDetailLibraryPanel(String id, IModel<?> model, GLibraryField fieldForm, final GWDetailPanel detailPanel)
	{
		super(id, model);

		_isReadOnly = fieldForm.isReadOnly();
		_binding = fieldForm.getDataBinding();

		final String caption = translateCaption(fieldForm.getDataBinding().getDataBindingMember().getDataBindingTableName(),  fieldForm.getCaption());

		DataBindingField field = _binding.getDataBindingField();
		
		Label label = new Label("label", caption + (field.isMandatory() ? " *" : ""));
		add(label);

		// TODO: Check, could probably done with RefreshingView?
		_rv = new WebMarkupContainer("root");
		add(_rv);
		
        _imageLink = new Image("image", new PackageResourceReference("img"));
		
		// Add: First Selected Row (active fkey)
		_modal = new ReferencesModalWindow("divLibrary", _binding);
        add(_modal);

        _modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
        {
             @Override
            public void onClose(AjaxRequestTarget target)
             {
            	 // just make a refresh. the update was done by the fkey itself.
                target.add(detailPanel);
             }
        });

        GWContext context = getGWContext();
        GozerController controller = context.getFrameExtension().getGozerController();

        _btnFKey = new GWFKeyButton("library", controller, getGWContext(), _binding, new PackageResourceReference("img"), _modal);
        add(_btnFKey);
	}
	
	private void refreshRepeatingView()
	{
		DataRowKeyBase key = _binding.getCurrentRow().getKey();
		
		LibraryReferenceDataTable tabLibraryReference = new LibraryReferenceDataTable(_binding.getDataBindingManager().getDataContainer());
		List<LibraryReferenceDataRow> libraryRefList = tabLibraryReference.findByTableRefId(key);
		
		RepeatingView rv = new RepeatingView("root");
		rv.setRenderBodyOnly(true);
		_rv.replaceWith(rv);
		
		for(LibraryReferenceDataRow row : libraryRefList)
		{
			WebMarkupContainer root = new WebMarkupContainer(rv.newChildId());
			root.setRenderBodyOnly(true);
			rv.add(root);
			
			LibraryDetailDataRow rowDetail = row.getRel_library_reference2library_detail();
			LibraryDataRow rowLibrary = rowDetail.getRel_library_detail2library();

			final ExternalLink link = new ExternalLink("link", rowDetail.getPath());
			final Label linkText = new Label("linkText", MessageFormat.format("{0} ({1}); ", rowLibrary.getName(), rowDetail.getVersion()));
			linkText.setRenderBodyOnly(true);
			
			root.add(link);
			link.add(linkText);
		}
		
		_rv = rv;
	}

	@Override
	public void onBeforeRender()
	{
		super.onBeforeRender();
		
		refreshRepeatingView();

		GozerFrameStatus status = getFrameExtension().getGozerController().getFrameStatus();

        PageParameters pp = new PageParameters();
        pp.add("id", Icons.ICON_TABLE_RELATIONSHIP);

        if (((status == GozerFrameStatus.EDIT) || (status == GozerFrameStatus.NEW)) && !_isReadOnly && _imageLink.getDefaultModel() instanceof DataBindingModel
                && ((DataBindingModel) _imageLink.getDefaultModel()).hasRows())
        {
            _imageLink.setImageResourceReference(new PackageResourceReference("img"), pp);

			_btnFKey.setEnabled(true);
        }
        else
		{
            pp.add("inactive", 1); // add the inactivity filter
            _imageLink.setImageResourceReference(new PackageResourceReference("img"), pp);

			_btnFKey.setEnabled(false);
		}
	}

}
