/*
 * Created on 21-oct-2007
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
package org.pdfsam.console.business.pdf;

import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;
import org.pdfsam.console.business.dto.commands.AbstractParsedCommand;
import org.pdfsam.console.business.dto.commands.ConcatParsedCommand;
import org.pdfsam.console.business.dto.commands.EncryptParsedCommand;
import org.pdfsam.console.business.dto.commands.MixParsedCommand;
import org.pdfsam.console.business.dto.commands.SplitParsedCommand;
import org.pdfsam.console.business.dto.commands.UnpackParsedCommand;
import org.pdfsam.console.business.pdf.handlers.AlternateMixCmdExecutor;
import org.pdfsam.console.business.pdf.handlers.ConcatCmdExecutor;
import org.pdfsam.console.business.pdf.handlers.EncryptCmdExecutor;
import org.pdfsam.console.business.pdf.handlers.SplitCmdExecutor;
import org.pdfsam.console.business.pdf.handlers.UnpackCmdExecutor;
import org.pdfsam.console.business.pdf.handlers.interfaces.AbstractCmdExecutor;
import org.pdfsam.console.exceptions.console.ConsoleException;
import org.pdfsam.console.utils.TimeUtility;
/**
 * Manager for the commands execution 
 * @author Andrea Vacondio
 *
 */
public class CmdExecuteManager extends Observable implements Observer{

	private final Logger log = Logger.getLogger(CmdExecuteManager.class.getPackage().getName());
	
	private AbstractCmdExecutor cmdExecutor = null;
	
	/**
	 * Executes the input parsed command
	 * @param parsedCommand
	 * @throws ConsoleException
	 */
	public void execute(AbstractParsedCommand parsedCommand) throws ConsoleException{
		if(parsedCommand != null){
			long start = System.currentTimeMillis();
			cmdExecutor = getExecutor(parsedCommand);
	
			if(cmdExecutor != null){
				cmdExecutor.addObserver(this);
				cmdExecutor.execute(parsedCommand);
				log.info("Command '"+parsedCommand.getCommand()+"' executed in "+TimeUtility.format(System.currentTimeMillis()-start));
			}else{
				throw new ConsoleException(ConsoleException.CMD_LINE_EXECUTOR_NULL, new String[]{""+parsedCommand.getCommand()});
			}
		}else{
			throw new ConsoleException(ConsoleException.CMD_LINE_NULL);
		}
	}	
	 /**
	  * forward the WorkDoneDataModel to the observers
	  */
	public void update(Observable arg0, Object arg1) {
		setChanged();
		notifyObservers(arg1);
	}

	/**
	 * @param parsedCommand
	 * @return an istance of the proper executor for the parsed command
	 */	
	private AbstractCmdExecutor getExecutor(AbstractParsedCommand parsedCommand){
		AbstractCmdExecutor retVal;
		if(MixParsedCommand.COMMAND_MIX.equals(parsedCommand.getCommand())){
			retVal = new AlternateMixCmdExecutor();
		}else if(SplitParsedCommand.COMMAND_SPLIT.equals(parsedCommand.getCommand())){
			retVal = new SplitCmdExecutor();
		}else if(EncryptParsedCommand.COMMAND_ECRYPT.equals(parsedCommand.getCommand())){
			retVal = new EncryptCmdExecutor();
		}else if(ConcatParsedCommand.COMMAND_CONCAT.equals(parsedCommand.getCommand())){
			retVal = new ConcatCmdExecutor();
		}else if(UnpackParsedCommand.COMMAND_UNPACK.equals(parsedCommand.getCommand())){
			retVal = new UnpackCmdExecutor();
		}else {
			retVal = null;
		}
		return retVal;
	}
}
