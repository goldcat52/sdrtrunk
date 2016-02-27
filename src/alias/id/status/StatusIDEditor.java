/*******************************************************************************
 *     SDR Trunk 
 *     Copyright (C) 2014-2016 Dennis Sheirer
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 ******************************************************************************/
package alias.id.status;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import net.miginfocom.swing.MigLayout;
import alias.AliasID;
import alias.ComponentEditor;

public class StatusIDEditor extends ComponentEditor<AliasID>
{
    private static final long serialVersionUID = 1L;

    private static final String HELP_TEXT = "Status numbers are used in some"
			+ " protocols like Fleetsync.  The status number is assigned a meaning"
    		+ " in the radio.  You can assign a 3 digit status code (use leading zeros) to an"
    		+ " alias where the alias contains the status meaning.";

    private JTextField mTextField;

	public StatusIDEditor( AliasID aliasID )
	{
		super( aliasID );
		
		initGUI();
		
		setComponent( aliasID );
	}
	
	private void initGUI()
	{
		setLayout( new MigLayout( "fill,wrap 2", "[right][left]", "[][][grow]" ) );

		add( new JLabel( "Status:" ) );

		MaskFormatter formatter = null;

		try
		{
			//Mask: 3 digits
			formatter = new MaskFormatter( "###" );
		}
		catch( Exception e )
		{
			//Do nothing, the mask was invalid
		}
		
		mTextField = new JFormattedTextField( formatter );
		mTextField.getDocument().addDocumentListener( this );
		add( mTextField, "growx,push" );

		JTextArea helpText = new JTextArea( HELP_TEXT );
		helpText.setLineWrap( true );
		helpText.setBackground( getBackground() );
		add( helpText, "span,grow,push" );
	}
	
	public StatusID getStatusID()
	{
		if( getComponent() instanceof StatusID )
		{
			return (StatusID)getComponent();
		}
		
		return null;
	}

	@Override
	public void setComponent( AliasID aliasID )
	{
		mComponent = aliasID;
		
		StatusID statusID = getStatusID();
		
		if( statusID != null )
		{
			mTextField.setText( String.valueOf( statusID.getStatus() ) );
		}
		
		setModified( false );
		
		repaint();
	}

	@Override
	public void save()
	{
		StatusID statusID = getStatusID();
		
		if( statusID != null )
		{
			int status = 0;
			
			try
			{
				status = Integer.valueOf( mTextField.getText() );
			}
			catch( Exception e )
			{
				//Do nothing, we couldn't parse the value
			}
			
			statusID.setStatus( status );
		}
		
		setModified( false );
	}
}