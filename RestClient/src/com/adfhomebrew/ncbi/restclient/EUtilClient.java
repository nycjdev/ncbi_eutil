package com.adfhomebrew.ncbi.restclient;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.UnsupportedEncodingException;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class EUtilClient {

    private static final String EUTIL_API_ESEARCH_URL = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?";
    private static final String EUTIL_API_ESUMMARY_URL = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?";
    private static final String EUTIL_API_EFETCH_URL = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?";
    private static final String CONST_PARAMETERS = "db=nuccore&retmode=xml&tool=adfeutils&email=fangx%40mskcc.org";


    public EUtilClient() {

    }

    public static void main(String[] args) {
        EUtilClient util = new EUtilClient();
        //util.getEFetchTestData();
        util.getESummaryInBatchTest();
    }

    public void getESummaryInBatchTest() {
        //esearchGIByLocusID,
        //esummaryGIByLocusID
        String[] locusIDs = { "NM_024212", "NM_024213" };

        eFetchTitleByGiMap(esearchGIByLocusID(locusIDs));


    }

    public List<ESummary> getESummaryTestData() {
        System.out.println("getESummaryTestData");
        String locusID = "NM_024212";
        String gi = esearchGIByLocusID(locusID);
        return esummaryGIByLocusID(gi);
    }

    public void sendTest() {
        System.out.println("sendTest");
        String locusID = "NM_024212";
        String gi = esearchGIByLocusID(locusID);
        System.out.println("gi: " + gi);
    }


    public List<EFetch> getEFetchTestData() {
        System.out.println("getEFetchTestData data");

        String gi = "148747442";
        return eFetchTitleByGi(gi);
    }

    public Map<String, List> eFetchTitleByGiMap(List giList) {

        String term = "&id="; // + encode(gi);

        if (giList.size() > 0) {
            for (int i = 0; i < giList.size(); i++) {
                if (giList.size() - 1 == i) {
                    term = term + giList.get(i);
                } else {
                    term = term + giList.get(i) + ",";
                }
            }
        }

        String parameters = CONST_PARAMETERS + term;
        String resturl = EUTIL_API_EFETCH_URL + parameters;
        System.out.println("url: " + resturl);
        Document doc = parseDOM(sendGet(resturl));

        NodeList gbseqList = doc.getElementsByTagName("GBSeq");

        Map mapOfGIToEFetchList = new HashMap();

        for (int i = 0; i < gbseqList.getLength(); i++) {

            Node gbseq = gbseqList.item(i);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = null;

            try {
                builder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            Document gbseqDoc = builder.newDocument();
            Node importedNode = gbseqDoc.importNode(gbseq, true);
            gbseqDoc.appendChild(importedNode);

            NodeList referencesList = gbseqDoc.getElementsByTagName("GBReference");
            String locusid = gbseqDoc.getElementsByTagName("GBSeq_locus").item(0).getTextContent();
            String definition = gbseqDoc.getElementsByTagName("GBSeq_definition").item(0).getTextContent();
            String organism = gbseqDoc.getElementsByTagName("GBSeq_source").item(0).getTextContent();
            String giWhole = gbseqDoc.getElementsByTagName("GBSeqid").item(1).getTextContent();
            String gi = giWhole.substring(3, giWhole.length());

            List efetchList = new ArrayList();

            for (int j = 0; j < referencesList.getLength(); j++) {
                //GBReference_title
                //GBReference_pubmed
                Element ele = (Element) referencesList.item(i);
                String title = ele.getElementsByTagName("GBReference_title").item(0).getTextContent();
                String pubmed = ele.getElementsByTagName("GBReference_pubmed").item(0).getTextContent();

                EFetch efetch = new EFetch(locusid, gi, title, pubmed, definition, organism);

                efetchList.add(efetch);
            }
            mapOfGIToEFetchList.put(gi, efetchList);

        }
        return mapOfGIToEFetchList;

    }

    public List<EFetch> eFetchTitleByGi(String gi) {

        String term = "&id=" + gi; // + encode(gi);
        String parameters = CONST_PARAMETERS + term;
        String resturl = EUTIL_API_EFETCH_URL + parameters;
        System.out.println("url: " + resturl);
        Document doc = parseDOM(sendGet(resturl));


        NodeList docSumList = doc.getElementsByTagName("GBReference");
        String locusid = doc.getElementsByTagName("GBSeq_locus").item(0).getTextContent();
        String definition = doc.getElementsByTagName("GBSeq_definition").item(0).getTextContent();
        String organism = doc.getElementsByTagName("GBSeq_source").item(0).getTextContent();

        List efetchList = new ArrayList();


        for (int i = 0; i < docSumList.getLength(); i++) {
            //GBReference_title
            //GBReference_pubmed
            Element ele = (Element) docSumList.item(i);
            String title = ele.getElementsByTagName("GBReference_title").item(0).getTextContent();
            String pubmed = ele.getElementsByTagName("GBReference_pubmed").item(0).getTextContent();


            EFetch efetch = new EFetch(locusid, gi, title, pubmed, definition, organism);

            efetchList.add(efetch);

        }

        return efetchList;
    }


    public List<EFetch> eFetchTitleByGi(List giList) {

        String term = "&id=";

        if (giList.size() > 0) {
            for (int i = 0; i < giList.size(); i++) {
                if (giList.size() - 1 == i) {
                    term = term + giList.get(i);
                } else {
                    term = term + giList.get(i) + ",";
                }
            }
        }

        String parameters = CONST_PARAMETERS + term;
        String resturl = EUTIL_API_EFETCH_URL + parameters;
        System.out.println("url: " + resturl);
        Document doc = parseDOM(sendGet(resturl));

        NodeList docSumList = doc.getElementsByTagName("GBReference");
        String locusid = doc.getElementsByTagName("GBSeq_locus").item(0).getTextContent();
        String definition = doc.getElementsByTagName("GBSeq_definition").item(0).getTextContent();
        String organism = doc.getElementsByTagName("GBSeq_source").item(0).getTextContent();
        String gi = doc.getElementsByTagName("GBSeqid").item(1).getTextContent();
        System.out.println("gi: " + gi);

        List efetchList = new ArrayList();


        for (int i = 0; i < docSumList.getLength(); i++) {
            //GBReference_title
            //GBReference_pubmed
            Element ele = (Element) docSumList.item(i);
            String title = ele.getElementsByTagName("GBReference_title").item(0).getTextContent();
            String pubmed = ele.getElementsByTagName("GBReference_pubmed").item(0).getTextContent();


            EFetch efetch = new EFetch(locusid, gi, title, pubmed, definition, organism);

            efetchList.add(efetch);

        }

        return efetchList;
    }

    public String esummaryTitleByGi(String gi) {

        List<ESummary> esummaryList = esummaryGIByLocusID(gi);
        System.out.println("esummaryList size: " + esummaryList.size());
        return esummaryList.get(0).getTitle();
    }

    public List<ESummary> esummaryGIByLocusID(List giList) {

        String term = "&id=";

        if (giList.size() > 0) {

            for (int i = 0; i < giList.size(); i++) {
                if (giList.size() - 1 == i) {
                    term = term + giList.get(i);
                } else {
                    term = term + giList.get(i) + ",";
                }
            }
        }

        String parameters = CONST_PARAMETERS + term;
        String resturl = EUTIL_API_ESUMMARY_URL + parameters;

        Document doc = parseDOM(sendGet(resturl));

        NodeList docSumList = doc.getElementsByTagName("DocSum");

        List esummaryList = new ArrayList();


        for (int i = 0; i < docSumList.getLength(); i++) {

            Element ele = (Element) docSumList.item(i);
            String id = ele.getElementsByTagName("Id").item(0).getTextContent();
            ESummary esummary = new ESummary(id);
            esummarySetElementItems(esummary, ele.getElementsByTagName("Item"));
            System.out.println(esummary);
            esummaryList.add(esummary);
        }

        return esummaryList;
    }

    public List<ESummary> esummaryGIByLocusID(String gi) {

        String term = "&id=" + gi;
        String parameters = CONST_PARAMETERS + term;
        String resturl = EUTIL_API_ESUMMARY_URL + parameters;

        Document doc = parseDOM(sendGet(resturl));

        NodeList docSumList = doc.getElementsByTagName("DocSum");

        List esummaryList = new ArrayList();


        for (int i = 0; i < docSumList.getLength(); i++) {

            Element ele = (Element) docSumList.item(i);
            String id = ele.getElementsByTagName("Id").item(0).getTextContent();
            ESummary esummary = new ESummary(id);
            esummarySetElementItems(esummary, ele.getElementsByTagName("Item"));
            esummaryList.add(esummary);

        }

        return esummaryList;
    }

    public void esummarySetElementItems(ESummary esummary, NodeList e) {
        for (int i = 0; i < e.getLength(); i++) {
            Element element = (Element) e.item(i);
            String attributeName = element.getAttribute("Name");

            switch (attributeName) {

            case "Caption":
                esummary.setCaption(element.getTextContent());
            case "Title":
                esummary.setTitle(element.getTextContent());
            case "Extra":
                esummary.setExtra(element.getTextContent());
            case "Gi":
                esummary.setGi(element.getTextContent());
            case "CreateDate":
                esummary.setCreateDate(element.getTextContent());
            case "UpdateDate":
                esummary.setUpdateDate(element.getTextContent());
            case "Flags":
                esummary.setFlags(element.getTextContent());
            case "TaxId":
                esummary.setTaxId(element.getTextContent());
            case "Length":
                esummary.setLength(element.getTextContent());
            case "Status":
                esummary.setStatus(element.getTextContent());
            case "ReplacedBy":
                esummary.setReplacedBy(element.getTextContent());
            case "Comment":
                esummary.setComment(element.getTextContent());
            }
        }

    }


    public String esearchGIByLocusID(String locusID) {

        String term = "&term=" + locusID;
        String parameters = CONST_PARAMETERS + term;
        String resturl = EUTIL_API_ESEARCH_URL + parameters;

        Document doc = parseDOM(sendGet(resturl));
        NodeList nList = doc.getElementsByTagName("Id");
        return nList.item(0).getTextContent();
    }

    public List esearchGIByLocusID(String[] locusIDArray) {
        List giList = new ArrayList();
        String term = "&term=";

        if (locusIDArray.length > 0) {

            for (int i = 0; i < locusIDArray.length; i++) {
                if (locusIDArray.length - 1 == i) {
                    term = term + locusIDArray[i];
                } else {
                    term = term + locusIDArray[i] + ",";
                }
            }
        }

        String parameters = CONST_PARAMETERS + term;
        String resturl = EUTIL_API_ESEARCH_URL + parameters;

        Document doc = parseDOM(sendGet(resturl));
        NodeList nList = doc.getElementsByTagName("Id");

        for (int i = 0; i < nList.getLength(); i++) {
            String gi = nList.item(i).getTextContent();
            System.out.println("gi: "+gi);
            giList.add(gi);
        }
        return giList;
    }


    public Document parseDOM(String xmlStr) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            return dBuilder.parse(new InputSource(new ByteArrayInputStream(xmlStr.getBytes("utf-8"))));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sendGet(String uri) {
        oneSecondDelay();
        System.out.println("getting: " + uri);
        HttpURLConnection connection = null;
        URL url = null;
        try {

            url = new URL(uri);

        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException");
            e.printStackTrace();
        }
        StringBuffer repsonse = new StringBuffer();
        try {

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            InputStream xmlresponse = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader((xmlresponse)));
            String output;
            while ((output = br.readLine()) != null) {
                repsonse.append(output);
            }
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        System.out.println("repsonse.toString(): " + repsonse.toString());
        return repsonse.toString();
    }

    public static void oneSecondDelay() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    public String encode(String s) {

        try {
            return java.net.URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }*/ //glassfish bug, encode throws error
}
