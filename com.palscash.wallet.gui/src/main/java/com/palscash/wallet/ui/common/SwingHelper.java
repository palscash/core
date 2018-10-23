package com.palscash.wallet.ui.common;

import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.TableColumn;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.palscash.wallet.ui.common.model.BooleanWrapper;
import com.palscash.wallet.ui.dialog.WaitingPanel;

public class SwingHelper {

	private static final Log log = LogFactory.getLog( SwingHelper.class );

	private static final KeyStroke escapeStroke = KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 );
	public static final String dispatchWindowClosingActionMapKey = "com.spodding.tackline.dispatch:WINDOW_CLOSING";

	public static void awesomeGraphics ( Graphics2D g2 ) {
		g2.setRenderingHint( RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY );
		g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		g2.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY );
		g2.setRenderingHint( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE );
		g2.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON );
		g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC );
		g2.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
		g2.setRenderingHint( RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE );
		g2.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

	}

	public static void installEscapeCloseOperation ( final JDialog dialog, final BooleanWrapper cancelled ) {

		Action dispatchClosing = new AbstractAction() {
			public void actionPerformed ( ActionEvent event ) {
				cancelled.set( true );
				dialog.dispatchEvent( new WindowEvent( dialog, WindowEvent.WINDOW_CLOSING ) );
			}
		};
		JRootPane root = dialog.getRootPane();
		root.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( escapeStroke, dispatchWindowClosingActionMapKey );
		root.getActionMap().put( dispatchWindowClosingActionMapKey, dispatchClosing );

	}

	public static void installEscapeCloseOperation ( final JDialog dialog ) {
		Action dispatchClosing = new AbstractAction() {
			public void actionPerformed ( ActionEvent event ) {
				dialog.dispatchEvent( new WindowEvent( dialog, WindowEvent.WINDOW_CLOSING ) );
			}
		};
		JRootPane root = dialog.getRootPane();
		root.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( escapeStroke, dispatchWindowClosingActionMapKey );
		root.getActionMap().put( dispatchWindowClosingActionMapKey, dispatchClosing );
	}

	public static void setFontForJText ( JEditorPane editorPane ) {

		Font font = UIManager.getFont( "Label.font" );
		String bodyRule = "body { font-family: " + font.getFamily() + "; " + "font-size: " + font.getSize() + "pt; }";
		( (HTMLDocument) editorPane.getDocument() ).getStyleSheet().addRule( bodyRule );

	}

	public static JScrollPane updateScrollPane ( JScrollPane sp ) {
		sp.getVerticalScrollBar().setUnitIncrement( 10 );
		return sp;
	}

	public static void setTableColumnWidth ( JTable table, int index, int width ) {
		table.getColumnModel().getColumn( index ).setPreferredWidth( width );
		table.getColumnModel().getColumn( index ).setMaxWidth( width );
		table.getColumnModel().getColumn( index ).setWidth( width );
		table.getColumnModel().getColumn( index ).setResizable( false );
	}

	public static void hideTableColumn ( JTable table, int index ) {
		TableColumn column = table.getColumnModel().getColumn( index );
		table.removeColumn( column );
	}

	public static interface ExecuteCallback {
		void execute ( );

		String getName ( );
	}

	public static abstract class SimpleExecuteCallback implements ExecuteCallback {
		@Override
		public String getName ( ) {
			return this.getClass().getCanonicalName();
		}
	}

	public static void executeOnUIThread ( final ExecuteCallback callback ) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run ( ) {
				callback.execute();
			}
		} );
	}

	public static void executeOnNonUIThread ( final ExecuteCallback callback ) {
		Thread t = new Thread( new Runnable() {
			@Override
			public void run ( ) {
				callback.execute();
			}
		} );
		t.setName( callback.getName() );
		t.start();
	}

	public static void addHyperlinkListener ( JEditorPane pane ) {

		pane.addHyperlinkListener( new HyperlinkListener() {

			@Override
			public void hyperlinkUpdate ( HyperlinkEvent e ) {

				if ( e.getEventType() == HyperlinkEvent.EventType.ACTIVATED ) {
					browseToUrl( e.getURL().toExternalForm() );
				}

			}

		} );
	}

	public static void browseToUrl ( String helpUrl ) {
		log.debug( "Open url in browser:" + helpUrl );
		if ( Desktop.isDesktopSupported() ) {
			try {
				Desktop.getDesktop().browse( new URI( helpUrl ) );
			} catch ( Exception e1 ) {
			}
		}
	}

	public static void selectTableRow ( JTable table, MouseEvent e ) {

		// get the coordinates of the mouse click
		Point p = e.getPoint();

		// get the row index that contains that coordinate
		int rowNumber = table.rowAtPoint( p );

		// Get the ListSelectionModel of the JTable
		ListSelectionModel model = table.getSelectionModel();

		// set the selected interval of rows. Using the "rowNumber"
		// variable for the beginning and end selects only that one row.
		model.setSelectionInterval( rowNumber, rowNumber );

	}

	public static void copyToClipboard ( String text ) {
		StringSelection selection = new StringSelection( text );
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents( selection, selection );
	}

	public static void addHTML ( JEditorPane editor, String html ) {
		try {

			HTMLDocument doc = (HTMLDocument) editor.getDocument();

			( (HTMLEditorKit) editor.getEditorKit() ).insertHTML( doc, doc.getLength(), html, 0, 0, null );
		} catch ( Exception e ) {
			log.error( "Error: ", e );
		}
	}

	public static double getFontHeight ( Font font ) {
		FontRenderContext frc = new FontRenderContext( font.getTransform(), true, true );
		return new TextLayout( "Frog", font, frc ).getBounds().getHeight();
	}

	public static void setupTabTraversalKeys ( JTabbedPane tabbedPane ) {
		KeyStroke ctrlTab = KeyStroke.getKeyStroke( "ctrl TAB" );
		KeyStroke ctrlShiftTab = KeyStroke.getKeyStroke( "ctrl shift TAB" );

		// Remove ctrl-tab from normal focus traversal
		Set < AWTKeyStroke > forwardKeys = new HashSet < AWTKeyStroke >( tabbedPane.getFocusTraversalKeys( KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS ) );
		forwardKeys.remove( ctrlTab );
		tabbedPane.setFocusTraversalKeys( KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys );

		// Remove ctrl-shift-tab from normal focus traversal
		Set < AWTKeyStroke > backwardKeys = new HashSet < AWTKeyStroke >( tabbedPane.getFocusTraversalKeys( KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS ) );
		backwardKeys.remove( ctrlShiftTab );
		tabbedPane.setFocusTraversalKeys( KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys );

		// Add keys to the tab's input map
		InputMap inputMap = tabbedPane.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
		inputMap.put( ctrlTab, "navigateNext" );
		inputMap.put( ctrlShiftTab, "navigatePrevious" );
	}

	public static Dimension getTextSize ( final Font font, final String text ) {

		final AffineTransform affinetransform = new AffineTransform();
		final FontRenderContext frc = new FontRenderContext( affinetransform, true, true );
		final int textwidth = (int) ( font.getStringBounds( text, frc ).getWidth() );
		final int textheight = (int) ( font.getStringBounds( text, frc ).getHeight() );

		return new Dimension( textwidth, textheight );
	}

	public static JDialog dialog ( Window frame, JComponent body ) {

		JDialog dlg = new JDialog( frame );
		dlg.setLayout( new BorderLayout() );
		dlg.setModal( true );

		dlg.add( body, BorderLayout.CENTER );
		dlg.setResizable( false );
		dlg.pack();
		dlg.setLocationRelativeTo( null );

		return dlg;
	}

	public static void async ( Window frame, String text, Runnable runnable ) {

		BooleanWrapper cancellation = new BooleanWrapper();

		WaitingPanel panel = new WaitingPanel();
		panel.text.setText( text );
		panel.btnCancel.addActionListener( e -> {
			cancellation.set( true );
		} );

		final JDialog dialog = dialog( frame, panel );
		dialog.setTitle( "Please, wait..." );
		dialog.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
		dialog.pack();

		SwingWorker < Void, Void > sw = new SwingWorker < Void, Void >() {

			@Override
			protected Void doInBackground ( ) throws Exception {

				if ( !cancellation.isTrue() && runnable != null ) {
					runnable.run();
					dialog.dispose();
				}

				return null;
			}

		};

		sw.execute();

		dialog.setVisible( true );

	}

	public static void bringToFront ( JFrame f ) {
		java.awt.EventQueue.invokeLater( new Runnable() {
			@Override
			public void run ( ) {
				f.toFront();
				f.repaint();
			}
		} );
	}

	public static BufferedImage toCompatibleImage ( BufferedImage image ) {

		// obtain the current system graphical settings
		GraphicsConfiguration gfx_config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

		/*
		 * if image is already compatible and optimized for current system 
		 * settings, simply return it
		 */
		if ( image.getColorModel().equals( gfx_config.getColorModel() ) )
			return image;

		// image is not optimized, so create a new image that is
		BufferedImage new_image = gfx_config.createCompatibleImage( image.getWidth(), image.getHeight(), image.getTransparency() );

		// get the graphics context of the new image to draw the old image on
		Graphics2D g2d = (Graphics2D) new_image.getGraphics();

		// actually draw the image and dispose of context no longer needed
		g2d.drawImage( image, 0, 0, null );
		g2d.dispose();

		// return the new optimized image
		return new_image;

	}

}
