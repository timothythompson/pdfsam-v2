/*
 * Created on 17-Oct-2007
 * Copyright (C) 2006 by Andrea Vacondio.
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
package org.pdfsam.console.business.parser.validators;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;

import jcmdline.CmdLineHandler;
import jcmdline.FileParam;
import jcmdline.StringParam;

import org.pdfsam.console.business.dto.commands.AbstractParsedCommand;
import org.pdfsam.console.business.dto.commands.EncryptParsedCommand;
import org.pdfsam.console.business.parser.validators.interfaces.AbstractCmdValidator;
import org.pdfsam.console.exceptions.console.ConsoleException;
import org.pdfsam.console.exceptions.console.ParseException;

import com.lowagie.text.pdf.PdfWriter;
/**
 * CmdValidator for the encrypt command
 * @author Andrea Vacondio
 */
public class EncryptCmdValidator extends AbstractCmdValidator {

	protected AbstractParsedCommand validateArguments(CmdLineHandler cmdLineHandler) throws ConsoleException {
		
		EncryptParsedCommand parsedCommandDTO = new EncryptParsedCommand();
		
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
	        
	        //-apwd
	        StringParam apwdOption = (StringParam) cmdLineHandler.getOption("apwd");
	        if(apwdOption.isSet()){
	        	parsedCommandDTO.setOwnerPwd(apwdOption.getValue());
	        }
	        
	        //-upwd
	        StringParam upwdOption = (StringParam) cmdLineHandler.getOption("upwd");
	        if(upwdOption.isSet()){
	        	parsedCommandDTO.setUserPwd(upwdOption.getValue());
	        }
	        
	        //-etype
	        StringParam etypeOption = (StringParam) cmdLineHandler.getOption("etype");
	        if(etypeOption.isSet()){
	        	parsedCommandDTO.setEncryptionType(etypeOption.getValue());
	        }
	        
	        //-f
	        FileParam fOption = (FileParam) cmdLineHandler.getOption("f");
	        if(fOption.isSet()){
				//validate file extensions
	        	for(Iterator fIterator = fOption.getFiles().iterator(); fIterator.hasNext();){
	        		File currentFile = (File) fIterator.next();
	        		if (!((currentFile.getName().toLowerCase().endsWith(PDF_EXTENSION)) && (currentFile.getName().length()>PDF_EXTENSION.length()))){
	        			throw new ParseException(ParseException.ERR_OUT_NOT_PDF, new String[]{currentFile.getPath()});
	        		}
	        	}
	        	parsedCommandDTO.setInputFileList((File[]) fOption.getFiles().toArray(new File[0]));
	        }
	        
	        //-allow
	        StringParam allowOption = (StringParam) cmdLineHandler.getOption("allow");
	        if(allowOption.isSet()){
	        	Hashtable permissionsMap = getPermissionsMap(parsedCommandDTO.getEncryptionType());
	        	int permissions = 0;
	        	if(!permissionsMap.isEmpty()){
		        	for(Iterator permIterator = allowOption.getValues().iterator(); permIterator.hasNext();){
		        		String currentPermission = (String) permIterator.next();
		        		Object value = permissionsMap.get(currentPermission);
		        		if(value != null){
		        			permissions |= ((Integer)value).intValue();
				    	}
		        	}
	        	}
	        	permissionsMap = null;
	        	parsedCommandDTO.setPermissions(permissions);
	        }
		}else{
			throw new ParseException(ParseException.CMD_LINE_HANDLER_NULL);
		}
		return parsedCommandDTO;	
	}

	/**
	 * @param encryptionType encryption algorithm
	 * @return The permissions map based on the chosen encryption
	 */
	private Hashtable getPermissionsMap(String encryptionType){
		Hashtable retMap = new Hashtable(12);
		if(EncryptParsedCommand.E_RC4_40.equals(encryptionType)){
			retMap.put(EncryptParsedCommand.E_PRINT,new Integer(PdfWriter.AllowPrinting));
			retMap.put(EncryptParsedCommand.E_MODIFY,new Integer(PdfWriter.AllowModifyContents));
			retMap.put(EncryptParsedCommand.E_COPY,new Integer(PdfWriter.AllowCopy));
			retMap.put(EncryptParsedCommand.E_ANNOTATION,new Integer(PdfWriter.AllowModifyAnnotations));
		}else{
			retMap.put(EncryptParsedCommand.E_PRINT,new Integer(PdfWriter.AllowPrinting));
			retMap.put(EncryptParsedCommand.E_MODIFY,new Integer(PdfWriter.AllowModifyContents));
			retMap.put(EncryptParsedCommand.E_COPY,new Integer(PdfWriter.AllowCopy));
			retMap.put(EncryptParsedCommand.E_ANNOTATION,new Integer(PdfWriter.AllowModifyAnnotations));
			retMap.put(EncryptParsedCommand.E_FILL,new Integer(PdfWriter.AllowFillIn));
			retMap.put(EncryptParsedCommand.E_SCREEN,new Integer(PdfWriter.AllowScreenReaders));
			retMap.put(EncryptParsedCommand.E_ASSEMBLY,new Integer(PdfWriter.AllowAssembly));
			retMap.put(EncryptParsedCommand.E_DPRINT,new Integer(PdfWriter.AllowDegradedPrinting));
		}
		
		return retMap;
	}
}