package autotest.dto.custData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClientData{

	@SerializedName("clientData")
	private List<ClientDataItem> clientData;

	public void setClientData(List<ClientDataItem> clientData){
		this.clientData = clientData;
	}

	public List<ClientDataItem> getClientData(){
		return clientData;
	}

	@Override
 	public String toString(){
		return 
			"ClientData{" + 
			"clientData = '" + clientData + '\'' + 
			"}";
		}
}