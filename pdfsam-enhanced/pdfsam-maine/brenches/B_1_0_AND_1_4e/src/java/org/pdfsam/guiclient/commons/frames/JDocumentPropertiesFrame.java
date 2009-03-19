/*
 * Created on 18-Mar-2009
 * Copyright (C) 2009 by Andrea Vacondio.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation; 
 * either version 2 of the License.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the Free Software Foundation, Inc., 
 *  59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.pdfsam.guiclient.commons.frames;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DefaultEditorKit;

import org.apache.log4j.Logger;
import org.pdfsam.guiclient.configuration.Configuration;
import org.pdfsam.guiclient.dto.PdfSelectionTableItem;
import org.pdfsam.i18n.GettextResource;
/**
 * Frame that showes document properties
 * @author Andrea Vacondio
 *
 */
public class JDocumentPropertiesFrame extends JFrame  implements MouseListener{

	private static final long serialVersionUID = -3836869268177748519L;

	private static final Logger log = Logger.getLogger(JDocumentPropertiesFrame.class.getPackage().getName());
	
	private static JDocumentPropertiesFrame instance = null;
	private final JPanel mainPanel = new JPanel();
	private JScrollPane mainScrollPanel;
	private JTextPane textInfoArea;
	private JPopupMenu jPopupMenu = new JPopupMenu();
	
	private JDocumentPropertiesFrame(){
		initialize();
	}	
	
	public static synchronized JDocumentPropertiesFrame getInstance(){
		if(instance == null){
			instance = new JDocumentPropertiesFrame();
		}
		return instance;
	}
	
	public synchronized void showProperties(PdfSelectionTableItem props){
		if(props!=null){
			String propsText = "<html><head></head><body>";
			propsText += 
				"<b>"+GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"File name")+":</b> "+props.getInputFile().getAbsolutePath()+"<br>"
				+"<b>"+GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Number of pages")+":</b> "+props.getPagesNumber()+"<br>"
				+"<b>"+GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"File size")+":</b> "+props.getFileSize()+"B<br>"
				+"<b>"+GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Pdf version")+":</b> "+props.getPdfVersionDescription()+"<br>"
				+"<b>"+GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Encryption")+":</b> "+(props.isEncrypted()? props.getEncryptionAlgorithm(): GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Not encrypted"))+"<br>";
				if(props.isEncrypted()){
					propsText +="<b>"+GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Permissions")+":</b> "+props.getPermissions()+"<br>";
				}
			
			if(props.getDocumentInfo() != null){
				propsText += 
					"<b>"+GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Title")+":</b> "+props.getDocumentInfo().getTitle()+"<br>"
	        		+"<b>"+GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Author")+":</b> "+props.getDocumentInfo().getAuthor()+"<br>"
	        		+"<b>"+GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Subject")+":</b> "+props.getDocumentInfo().getSubject()+"<br>"
	        		+"<b>"+GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Producer")+":</b> "+props.getDocumentInfo().getProducer()+"<br>"
	        		+"<b>"+GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Creator")+":</b> "+props.getDocumentInfo().getCreator()+"<br>"
	        		+"<b>"+GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Creation date")+":</b> "+props.getDocumentInfo().getCreationDate()+"<br>"
	        		+"<b>"+GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Modification date")+":</b> "+props.getDocumentInfo().getModificationDate()+"<br>"
	        		+"<b>"+GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Keywords")+":</b> "+props.getDocumentInfo().getKeywords()+"<br>";
			}
			propsText += "</body></html>";
			textInfoArea.setMargin(new Insets(5, 5, 5, 5));
	        textInfoArea.setText(propsText);
			setVisible(true);
		}
	}
	
	private void initialize() {
		try{	
			URL iconUrl = this.getClass().getResource("/images/info.png");
			setTitle(GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Document properties"));
			setIconImage(new ImageIcon(iconUrl).getImage());
	        setSize(640, 480);
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

			JMenuBar menuBar = new JMenuBar();
			JMenu menuFile = new JMenu();
			menuFile.setText(GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"File"));
			menuFile.setMnemonic(KeyEvent.VK_F);
			
			JMenuItem closeItem = new JMenuItem();
			closeItem.setText(GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Close"));
			closeItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					setVisible(false);					
				}				
			});
							
			menuFile.add(closeItem);			
			menuBar.add(menuFile);
			getRootPane().setJMenuBar(menuBar);
			

			textInfoArea = new JTextPane();
			textInfoArea.setFont(new Font("Dialog", Font.PLAIN, 9));
			textInfoArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			textInfoArea.setContentType("text/html");
			textInfoArea.setEditable(false);

			mainPanel.add(textInfoArea);
			mainScrollPanel = new JScrollPane(textInfoArea);
			
			getContentPane().add(mainScrollPanel);
	        
			JMenuItem menuCopy = new JMenuItem(new DefaultEditorKit.CopyAction());
	        menuCopy.setText(GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Copy"));
	        menuCopy.setIcon(new ImageIcon(this.getClass().getResource("/images/edit-copy.png")));
	        jPopupMenu.add(menuCopy);
	        
	        textInfoArea.addMouseListener(this);
	        
	        addKeyListener(new KeyListener(){
				public void keyPressed(KeyEvent e) {
					 if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
			               setVisible(false);
			         }
				}
				public void keyReleased(KeyEvent e) {					
				}

				public void keyTyped(KeyEvent e) {
				}
	        });
	        
		}catch(Exception e){
			log.error(GettextResource.gettext(Configuration.getInstance().getI18nResourceBundle(),"Error creating properties panel."),e);
		}
	}
	
	 public void mouseReleased(MouseEvent e){
			if(e.isPopupTrigger()){
				jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
			}		
		}
		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent e) {
			if(e.isPopupTrigger()){
				jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
}