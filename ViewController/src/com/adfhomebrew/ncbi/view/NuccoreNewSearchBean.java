package com.adfhomebrew.ncbi.view;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.model.BindingContext;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

public class NuccoreNewSearchBean {
    private RichInputText searchField;
    String sessionId = null;

    public NuccoreNewSearchBean() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null) {
            ExternalContext ec = fc.getExternalContext();
            if (ec != null) {
                HttpSession session = (HttpSession) ec.getSession(false);
                if (session != null) {
                    this.sessionId = session.getId();
                }
            }
        }
    }


    public void setSearchField(RichInputText searchField) {
        this.searchField = searchField;
    }

    public RichInputText getSearchField() {
        return searchField;
    }

    public void sync(ActionEvent actionEvent) {

        
        if (searchField.getValue()!=null) {
            String targets = (String) searchField.getValue();
            String lines[] = targets.split("\\r?\\n");
            OperationBinding ob = getBindings().getOperationBinding("syncEFetchData");
            ob.getParamsMap().put("targets", lines);
            ob.getParamsMap().put("sessionId", this.sessionId);

            ob.execute();
        }
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public void showMessage(String s) {
        FacesMessage msg = new FacesMessage(s);
        msg.setSeverity(FacesMessage.SEVERITY_WARN);
        FacesContext fctx = FacesContext.getCurrentInstance();
        fctx.addMessage(null, msg);
    }

}
