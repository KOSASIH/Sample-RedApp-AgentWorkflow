package com.sabre.tn.redapp.example.showcase.listeners;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;

import com.sabre.edge.cf.common.service.ContextStatusAdvisor;
import com.sabre.edge.cf.emu.data.EmulatorCommand;
import com.sabre.edge.cf.emu.data.requests.EmulatorCommandRequest;
import com.sabre.edge.cf.emu.data.responses.EmulatorCommandResponse;
import com.sabre.edge.cf.model.IRequest;
import com.sabre.edge.cf.model.IService;
import com.sabre.edge.cf.model.IServiceContext;
import com.sabre.edge.cf.model.ServiceStatus;
import com.sabre.edge.cf.model.element.ServiceContext;
import com.sabre.edge.platform.core.sso.base.IAgentProfileService;
import com.sabre.edge.platform.core.ui.threading.UiThreadInvoker;
import com.sabre.tn.redapp.example.showcase.Activator;
import com.sabre.tn.redapp.example.showcase.preferences.PreferenceConstants;
import com.sabre.tn.redapp.example.showcase.uiparts.CoreServicesHelper;
import com.sabre.tn.redapp.example.showcase.uiparts.OpenThingsHelper;


public class CommandModificationService implements IService {

	@Override
	public void process(IServiceContext context) {
		
		IPreferenceStore st = Activator.getDefault().getPreferenceStore();
		
		
		boolean shouldModify=st.getBoolean(PreferenceConstants.P_MODIFY_RCVDFROM);
		
		
		if(shouldModify){

			IRequest rq = context.getRequest();
			
			if(rq instanceof EmulatorCommandRequest){
				EmulatorCommand cmd = ((EmulatorCommandRequest) rq).getEmulatorCommand();
				EmulatorCommandResponse cmdResp=null;
			
				//show an pop-up indication the the command can be modified, let the user decide
				int resDialog = OpenThingsHelper.showDialog("The command you typed is about to be modified, please confirm the operation by clicking Ok");
				
				
				if(resDialog==Dialog.OK){

					//will use agent profile services to append user name to the received from (6) command
					IAgentProfileService svc =  CoreServicesHelper.getAgentProfile();
					
					String strCmdToAppend = svc.getFirstName() + " " + svc.getLastName();
					EmulatorCommand cmdModified = new EmulatorCommand(cmd.getCommand().concat(strCmdToAppend.toUpperCase()));
					
					cmdResp = new EmulatorCommandResponse(cmdModified);
	
					((ServiceContext)context).setResponse(cmdResp);
					
					
					context.setStatus(ServiceStatus.SUCCESS);
					
	
				}
			
			}
			
		
		}
		
	}

}
