/*
 * Created on 18-Mar-2009
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
package org.pdfsam.guiclient.utils;

import org.pdfsam.console.business.dto.commands.EncryptParsedCommand;
/**
 * Support to the encryption
 * @author Andrea Vacondio
 *
 */
public class EncryptionUtility {

	
    public final static String RC4_40 = "RC4-40b";
	public final static String RC4_128 = "RC4-128b";
	public final static String AES_128 = "AES-128b";
	
    
	/**
	*@return Console parameter for the selected encryption algorithm from the JComboBox
	*/
	public static String getEncAlgorithm(String algorithm){
		String retval = EncryptParsedCommand.E_RC4_40;
		if(algorithm != null){
			if(algorithm.equals(RC4_40)){
				retval = EncryptParsedCommand.E_RC4_40;
			}else if(algorithm.equals(RC4_128)){
				retval = EncryptParsedCommand.E_RC4_128;
			}else if(algorithm.equals(AES_128)){
				retval = EncryptParsedCommand.E_AES_128;
			}
		}
		return retval;			
	}
}
