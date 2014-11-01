package com.adfhomebrew.ncbi.model;

import com.adfhomebrew.ncbi.model.common.NIHAppModule;
import com.adfhomebrew.ncbi.restclient.EFetch;

import com.adfhomebrew.ncbi.restclient.EUtilClient;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.ViewLinkImpl;
import oracle.jbo.server.ViewObjectImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Thu Oct 02 22:30:28 EDT 2014
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class NIHAppModuleImpl extends ApplicationModuleImpl implements NIHAppModule {
    /**
     * This is the default constructor (do not remove).
     */
    public NIHAppModuleImpl() {
    }

    public void syncEFetchData(String[] targets, String sessionId) {

        Row row = getNihNuucoreSessionView1().getCurrentRow();
        row.setAttribute("AppSessionId", sessionId);
        EUtilClient client = new EUtilClient();

        Map<String, List> efetchMap = client.eFetchTitleByGiMap(client.esearchGIByLocusID(targets));

        for (Iterator iter = efetchMap.keySet().iterator(); iter.hasNext();) {
            List efetchList = efetchMap.get(iter.next());

            for (Iterator<EFetch> efetchListIter = efetchList.iterator(); efetchListIter.hasNext();) {
                EFetch efetch = efetchListIter.next();
                persistGi(efetch.getLocusId(), efetch.getGi(), sessionId, efetch.getDefinition());
            }
            persistEFetchList(efetchList, sessionId);
        }


        for (int i = 0; i < targets.length; i++) {

            String target = targets[i];
            if (target != null && !target.trim().equals("")) {
                String gi = client.esearchGIByLocusID(target);

                String definition = client.esummaryTitleByGi(gi);

                persistGi(target, gi, sessionId, definition);
                List<EFetch> efetchList = client.eFetchTitleByGi(gi);
                persistEFetchList(efetchList, sessionId);
            }
        }

        try {
            getDBTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        getNihNuccoreRnaView1().executeQuery();
        getNihPubmedEntryView1().executeQuery();
        getNihMPubmedRnaView1().executeQuery();
    }

    public void printRow(Row row) {
        System.out.println("Printing row...");
        String[] attributes = row.getAttributeNames();
        String print = row.getClass().toString();
        for (int i = 0; i < attributes.length; i++) {
            String attributeName = attributes[i];
            print = print + attributeName + ":" + row.getAttribute(i) + ", ";
        }
        System.out.println("Printing done...");
    }

    private void persistGi(String locusId, String gi, String sessionId, String definition) {

        ViewObject vo = getNihNuccoreRnaView1();
        Row row = vo.createRow();
        row.setAttribute("LocusId", locusId);
        row.setAttribute("GiId", gi);
        row.setAttribute("AppSessionId", sessionId);
        row.setAttribute("Definition", definition);
        vo.insertRow(row);

    }

    private void persistEFetchList(List<EFetch> efetchList, String sessionId) {

        ViewObject pubMedEntryVo = getNihPubmedEntryView1();
        ViewObject mPubmedRnaVo = getNihMPubmedRnaView1();

        for (Iterator<EFetch> iter = efetchList.iterator(); iter.hasNext();) {
            EFetch efetch = iter.next();
            //check if it exists
            String pubMedId = efetch.getPubmedId();

            Row pubMedEntryRow = pubMedEntryVo.createRow();
            pubMedEntryRow.setAttribute("PubmedId", pubMedId);
            pubMedEntryRow.setAttribute("Title", efetch.getTitle());
            pubMedEntryRow.setAttribute("AppSessionId", sessionId);
            pubMedEntryRow.setAttribute("Definition", efetch.getDefinition());
            pubMedEntryRow.setAttribute("Organism", efetch.getOrganism());
            pubMedEntryVo.insertRow(pubMedEntryRow);

            Row pubmedRnaRow = mPubmedRnaVo.createRow();
            pubmedRnaRow.setAttribute("LocusId", efetch.getLocusId());
            pubmedRnaRow.setAttribute("PubmedId", pubMedId);
            pubmedRnaRow.setAttribute("AppSessionId", sessionId);

        }
    }


    /**
     * Container's getter for NihNuucoreSessionView1.
     * @return NihNuucoreSessionView1
     */
    public ViewObjectImpl getNihNuucoreSessionView1() {
        return (ViewObjectImpl) findViewObject("NihNuucoreSessionView1");
    }

    /**
     * Container's getter for PerSessionLocusId1.
     * @return PerSessionLocusId1
     */
    public ViewObjectImpl getPerSessionLocusId1() {
        return (ViewObjectImpl) findViewObject("PerSessionLocusId1");
    }

    /**
     * Container's getter for AllSessionToPerSessionVl1.
     * @return AllSessionToPerSessionVl1
     */
    public ViewLinkImpl getAllSessionToPerSessionVl1() {
        return (ViewLinkImpl) findViewLink("AllSessionToPerSessionVl1");
    }

    /**
     * Container's getter for PubMedEntryChildVo2.
     * @return PubMedEntryChildVo2
     */
    public ViewObjectImpl getPubMedEntryChildVo2() {
        return (ViewObjectImpl) findViewObject("PubMedEntryChildVo2");
    }

    /**
     * Container's getter for PerSessionToPubMedEntryChildVl1.
     * @return PerSessionToPubMedEntryChildVl1
     */
    public ViewLinkImpl getPerSessionToPubMedEntryChildVl1() {
        return (ViewLinkImpl) findViewLink("PerSessionToPubMedEntryChildVl1");
    }

    /**
     * Container's getter for NihMPubmedRnaView1.
     * @return NihMPubmedRnaView1
     */
    public ViewObjectImpl getNihMPubmedRnaView1() {
        return (ViewObjectImpl) findViewObject("NihMPubmedRnaView1");
    }

    /**
     * Container's getter for NihNuccoreRnaView1.
     * @return NihNuccoreRnaView1
     */
    public ViewObjectImpl getNihNuccoreRnaView1() {
        return (ViewObjectImpl) findViewObject("NihNuccoreRnaView1");
    }

    /**
     * Container's getter for NihPubmedEntryView1.
     * @return NihPubmedEntryView1
     */
    public ViewObjectImpl getNihPubmedEntryView1() {
        return (ViewObjectImpl) findViewObject("NihPubmedEntryView1");
    }
}

