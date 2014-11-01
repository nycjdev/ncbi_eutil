package com.adfhomebrew.ncbi.view;

import com.adfhomebrew.ncbi.restclient.EUtilClient;

import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.model.BindingContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import org.apache.myfaces.trinidad.event.ReturnEvent;

public class NUCCoreSearchBean {
    
    EUtilClient client = new EUtilClient();
    private RichInputText searchField;
    private RichPopup newpopup;


    public NUCCoreSearchBean() {
      
    }


    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public void refresh(ActionEvent actionEvent) {

        String targets = (String) searchField.getValue();
        if (!targets.equals(null) || !targets.trim().equals("")) {
            String lines[] = targets.split("\\r?\\n");
            OperationBinding ob = getBindings().getOperationBinding("syncEFetchData");
            ob.getParamsMap().put("targets", lines);
            ob.execute();
        }

    }

    public void setSearchField(RichInputText searchField) {
        this.searchField = searchField;
    }

    public RichInputText getSearchField() {
        return searchField;
    }

    public void newReturn(ReturnEvent returnEvent) {
        // Add event code here...
        newpopup.hide();
    }

    public void setNewpopup(RichPopup newpopup) {
        this.newpopup = newpopup;
    }

    public RichPopup getNewpopup() {
        return newpopup;
    }
}
