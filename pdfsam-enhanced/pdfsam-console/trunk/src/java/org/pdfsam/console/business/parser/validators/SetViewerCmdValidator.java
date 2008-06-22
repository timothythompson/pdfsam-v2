/*
 * Created on 06-Mar-2008
 * Copyright (C) 2008 by Andrea Vacondio.
 *
 *
 * This library is provided under dual licenses.
 * You may choose the terms of the Lesser General Public License version 2.1 or the General Public License version 2
 * License at your discretion.
 * 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * 
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation; 
 * either version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the Free Software Foundation, Inc., 
 *  59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.pdfsam.console.business.parser.validators;

import java.io.File;
import java.util.Iterator;

import jcmdline.BooleanParam;
import jcmdline.CmdLineHandler;
import jcmdline.FileParam;
import jcmdline.PdfFileParam;
import jcmdline.StringParam;
import jcmdline.dto.PdfFile;

import org.pdfsam.console.business.dto.commands.AbstractParsedCommand;
import org.pdfsam.console.business.dto.commands.SetViewerParsedCommand;
import org.pdfsam.console.business.parser.validators.interfaces.AbstractCmdValidator;
import org.pdfsam.console.exceptions.console.ConsoleException;
import org.pdfsam.console.exceptions.console.ParseException;
import org.pdfsam.console.utils.FileUtility;

import com.lowagie.text.pdf.PdfWriter;
/**
 * CmdValidator for the setviewer command
 * @author Andrea Vacondio
 */
public class SetViewerCmdValidator extends AbstractCmdValidator {

	public AbstractParsedCommand validateArguments(CmdLineHandler cmdLineHandler) throws ConsoleException {
			
			SetViewerParsedCommand parsedCommandDTO = new SetViewerParsedCommand();
			
			if(cmdLineHandler != null){
				//-o
				FileParam oOption = (FileParam) cmdLineHandler.getOption("o");
				if ((oOption.isSet())){
		            File outFile = oOption.getFile();
		            if (outFile.isDirectory()){
		            	parsedCommandDTO.setOutputFile(outFile);	
		    		}           
		            else{
		            	throw new ParseException(ParseException.ERR_OUT_NOT_DIR);
		            }
		        }else{
		        	throw new ParseException(ParseException.ERR_NO_O);
		        }
				
				//-p
		        StringParam pOption = (StringParam) cmdLineHandler.getOption("p");
		        if(pOption.isSet()){
		        	parsedCommandDTO.setOutputFilesPrefix(pOption.getValue());
		        }
		        
				//-mode
		        StringParam mOption = (StringParam) cmdLineHandler.getOption(SetViewerParsedCommand.M_ARG);
		        parsedCommandDTO.setMode(getMode(mOption.getValue()));		     
		        
				//-layout
		        StringParam lOption = (StringParam) cmdLineHandler.getOption(SetViewerParsedCommand.L_ARG);
		        parsedCommandDTO.setLayout(getLayout(lOption.getValue()));	       
		        
				//-nfsmode
		        StringParam nfsmOption = (StringParam) cmdLineHandler.getOption(SetViewerParsedCommand.NFSM_ARG);		      
		        parsedCommandDTO.setNfsmode(getNFSMode(nfsmOption.getValue()));
		        
				//-direction
		        StringParam dOption = (StringParam) cmdLineHandler.getOption(SetViewerParsedCommand.D_ARG);
		        parsedCommandDTO.setDirection(getDirection(dOption.getValue()));
		        
				 //-f
		        PdfFileParam fOption = (PdfFileParam) cmdLineHandler.getOption("f");
		        if(fOption.isSet()){
					//validate file extensions
		        	for(Iterator fIterator = fOption.getPdfFiles().iterator(); fIterator.hasNext();){
		        		PdfFile currentFile = (PdfFile) fIterator.next();
		        		if (!((currentFile.getFile().getName().toLowerCase().endsWith(PDF_EXTENSION)) && (currentFile.getFile().getName().length()>PDF_EXTENSION.length()))){
		        			throw new ParseException(ParseException.ERR_OUT_NOT_PDF, new String[]{currentFile.getFile().getPath()});
		        		}
		        	}
		        	parsedCommandDTO.setInputFileList(FileUtility.getPdfFiles(fOption.getPdfFiles()));
		        }
		        
		        //-hidemenu
		        parsedCommandDTO.setHideMenu(((BooleanParam) cmdLineHandler.getOption(SetViewerParsedCommand.HIDEMENU_ARG)).isTrue());
		        //-hidetoolbar
		        parsedCommandDTO.setHideToolBar(((BooleanParam) cmdLineHandler.getOption(SetViewerParsedCommand.HIDETOOLBAR_ARG)).isTrue());
		        //-hide window ui
		        parsedCommandDTO.setHideWindowUI(((BooleanParam) cmdLineHandler.getOption(SetViewerParsedCommand.HIDEWINDOWUI_ARG)).isTrue());
		        //-fit window
		        parsedCommandDTO.setFitWindow(((BooleanParam) cmdLineHandler.getOption(SetViewerParsedCommand.FITWINDOW_ARG)).isTrue());
		        //-center window
		        parsedCommandDTO.setCenterWindow(((BooleanParam) cmdLineHandler.getOption(SetViewerParsedCommand.CENTERWINDOW_ARG)).isTrue());
		        //-display doc title
		        parsedCommandDTO.setDisplayDocTitle(((BooleanParam) cmdLineHandler.getOption(SetViewerParsedCommand.DOCTITLE_ARG)).isTrue());
		        //-noprintscaling
		        parsedCommandDTO.setNoPrintScaling(((BooleanParam) cmdLineHandler.getOption(SetViewerParsedCommand.NOPRINTSCALING_ARG)).isTrue());
		       
			}else{
				throw new ConsoleException(ConsoleException.CMD_LINE_HANDLER_NULL);
			}				
			return parsedCommandDTO;
	}

	
	/**
	 * @param direction 
	 * @return The direction to iText 
	 */
	private int getDirection(String direction){
		int retVal = PdfWriter.DirectionL2R;
		if(SetViewerParsedCommand.D_R2L.equals(direction)){
			retVal = PdfWriter.DirectionR2L;
		}
		return retVal;
	}
	
	/**
	 * 
	 * @param mode
	 * @return The mode to iText
	 */
	private int getMode(String mode){
		int retVal = PdfWriter.PageModeUseNone;
		if(SetViewerParsedCommand.M_ATTACHMENTS.equals(mode)){
			retVal = PdfWriter.PageModeUseAttachments;
		}else if(SetViewerParsedCommand.M_FULLSCREEN.equals(mode)){
			retVal = PdfWriter.PageModeFullScreen;
		}else if(SetViewerParsedCommand.M_OCONTENT.equals(mode)){
			retVal = PdfWriter.PageModeUseOC;
		}else if(SetViewerParsedCommand.M_OUTLINES.equals(mode)){
			retVal = PdfWriter.PageModeUseOutlines;
		}else if(SetViewerParsedCommand.M_THUMBS.equals(mode)){
			retVal = PdfWriter.PageModeUseThumbs;
		}
		return retVal;
	}
	
	/**
	 * 
	 * @param nfsmode
	 * @return the non full screen mode to iText
	 */
	private int getNFSMode(String nfsmode){
		int retVal = PdfWriter.NonFullScreenPageModeUseNone;
		if(SetViewerParsedCommand.NFSM_OCONTENT.equals(nfsmode)){
			retVal = PdfWriter.NonFullScreenPageModeUseOC;
		}else if(SetViewerParsedCommand.NFSM_OUTLINES.equals(nfsmode)){
			retVal = PdfWriter.NonFullScreenPageModeUseOutlines;
		}else if(SetViewerParsedCommand.NFSM_THUMBS.equals(nfsmode)){
			retVal = PdfWriter.NonFullScreenPageModeUseThumbs;
		}
		return retVal;
	}
	
	/**
	 * 
	 * @param layout
	 * @return the layout to iText
	 */
	private int getLayout(String layout){
		int retVal = PdfWriter.PageLayoutOneColumn;
		if(SetViewerParsedCommand.L_SINGLEPAGE.equals(layout)){
			retVal = PdfWriter.PageLayoutSinglePage;
		}else if(SetViewerParsedCommand.L_TWOCOLUMNLEFT.equals(layout)){
			retVal = PdfWriter.PageLayoutTwoColumnLeft;
		}else if(SetViewerParsedCommand.L_TWOCOLUMNRIGHT.equals(layout)){
			retVal = PdfWriter.PageLayoutTwoColumnRight;
		}else if(SetViewerParsedCommand.L_TWOPAGELEFT.equals(layout)){
			retVal = PdfWriter.PageLayoutTwoPageLeft;
		}else if(SetViewerParsedCommand.L_TWOPAGERIGHT.equals(layout)){
			retVal = PdfWriter.PageLayoutTwoPageRight;
		}
		return retVal;
	}
}
