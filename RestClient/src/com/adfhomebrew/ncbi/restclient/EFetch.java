package com.adfhomebrew.ncbi.restclient;

public class EFetch {
    private final String PUBMED_URL = "http://www.ncbi.nlm.nih.gov/pubmed/";

    private String title, pubmed, locusId, gi, pubmedId, definition, organism;

    public EFetch(String locusId, String gi, String title, String pubmedId, String definition, String organism) {
        this.locusId = locusId;
        this.gi = gi;
        this.title = title;
        this.pubmedId = pubmedId;
        this.definition = definition;
        this.organism = organism;
    }

    public String getPubmed() {
        pubmed = PUBMED_URL + pubmedId;
        return pubmed;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getDefinition() {
        return definition;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    public String getOrganism() {
        return organism;
    }

    public void setPubmedId(String pubmedId) {
        this.pubmedId = pubmedId;
    }

    public String getPubmedId() {
        return pubmedId;
    }


    public void setPubmed(String pubmed) {
        this.pubmed = pubmed;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setLocusId(String id) {
        this.locusId = id;
    }

    public String getLocusId() {
        return locusId;
    }

    public void setGi(String gi) {
        this.gi = gi;
    }

    public String getGi() {
        return gi;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("EFetch locus ID: " + locusId + " - gi: " + gi);
        sb.append("\n\t Title: " + title);
        sb.append("\n\t Pubmed: " + pubmed);
        sb.append("\n\t Organism: " + organism);
        sb.append("\n\t Definition: " + definition);
        return sb.toString();
    }
}
