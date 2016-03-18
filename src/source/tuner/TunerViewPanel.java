package source.tuner;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import source.tuner.TunerEvent.Event;

import com.jidesoft.swing.JideSplitPane;

public class TunerViewPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private final static Logger mLog = LoggerFactory.getLogger( TunerViewPanel.class );

	private TunerModel mTunerModel;
	private JTable mTunerTable;
	private JideSplitPane mSplitPane;
	private TunerEditor mTunerEditor;
	
	public TunerViewPanel( TunerModel tunerModel )
	{
		mTunerModel = tunerModel;
		mTunerEditor = new TunerEditor( mTunerModel.getTunerConfigurationModel() );
		
		init();
	}
	
	private void init()
	{
		setLayout( new MigLayout( "insets 0 0 0 0", "[fill,grow]", "[fill,grow]" ) );

		mTunerTable = new JTable( mTunerModel );
		mTunerTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		mTunerTable.getSelectionModel().addListSelectionListener( new ListSelectionListener()
		{
			@Override
			public void valueChanged( ListSelectionEvent event )
			{
				if( !event.getValueIsAdjusting() )
				{
					int row = mTunerTable.getSelectedRow();
					int modelRow = mTunerTable.convertRowIndexToModel( row );

					mTunerEditor.setItem( mTunerModel.getTuner( modelRow ) );
				}
			}
		} );
		mTunerTable.addMouseListener( new MouseAdapter()
		{
			@Override
			public void mouseClicked( MouseEvent e )
			{
				int column = mTunerTable.columnAtPoint( e.getPoint() );

				if( column == TunerModel.SPECTRAL_DISPLAY_MAIN )
				{
					int tableRow = mTunerTable.rowAtPoint( e.getPoint() );
					int modelRow = mTunerTable.convertRowIndexToModel( tableRow );
					
					Tuner tuner = mTunerModel.getTuner( modelRow );
					
					if( tuner != null )
					{
						mTunerModel.broadcast( new TunerEvent( tuner, 
								Event.REQUEST_MAIN_SPECTRAL_DISPLAY ) );
					}
				}
				else if( column == TunerModel.SPECTRAL_DISPLAY_NEW )
				{
					int tableRow = mTunerTable.rowAtPoint( e.getPoint() );
					int modelRow = mTunerTable.convertRowIndexToModel( tableRow );
					
					Tuner tuner = mTunerModel.getTuner( modelRow );
					
					if( tuner != null )
					{
						mTunerModel.broadcast( new TunerEvent( tuner, 
								Event.REQUEST_NEW_SPECTRAL_DISPLAY ) );
					}
				}
			}
		} );
		
		TableCellRenderer renderer = new LinkCellRenderer();
		
		mTunerTable.getColumnModel().getColumn( 5 ).setCellRenderer( renderer );
		mTunerTable.getColumnModel().getColumn( 6 ).setCellRenderer( renderer );
		
		JScrollPane listScroller = new JScrollPane( mTunerTable );
		listScroller.setPreferredSize( new Dimension( 400, 20 ) );

		JScrollPane editorScroller = new JScrollPane( mTunerEditor );
		editorScroller.setPreferredSize( new Dimension( 400, 80 ) );
		
		mSplitPane = new JideSplitPane();
		mSplitPane.setOrientation( JideSplitPane.VERTICAL_SPLIT );
		mSplitPane.add( listScroller );
		mSplitPane.add( editorScroller );
		
		add( mSplitPane );
	}
	
	public class LinkCellRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent( JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column )
		{
			JLabel label = (JLabel)super.getTableCellRendererComponent( table, 
				value, isSelected, hasFocus, row, column );
			
			label.setForeground( Color.BLUE.brighter() );
			
			if( column == TunerModel.SPECTRAL_DISPLAY_MAIN )
			{
				label.setToolTipText( "Show this tuner in the main spectral display" );
			}
			else if( column == TunerModel.SPECTRAL_DISPLAY_NEW )
			{
				label.setToolTipText( "Show this tuner in a new spectral display" );
			}
			
			return label;
		}
	}
}