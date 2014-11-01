package com.adfhomebrew.ncbi.restclient;

public class ESummary {
    private String id;
    private String title;
    private String caption;
    private String extra;
    private String createDate;
    private String updateDate;
    private String flags;
    private String length;
    private String taxId;
    private String status;
    private String replacedBy;
    private String comment;
    private String gi;

    public ESummary(String id) {
        this.id = id;
    }
    
    public void setGi(String gi) {
        this.gi = gi;
    }

    public String getGi() {
        return gi;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getExtra() {
        return extra;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public String getFlags() {
        return flags;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getLength() {
        return length;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setReplacedBy(String replacedBy) {
        this.replacedBy = replacedBy;
    }

    public String getReplacedBy() {
        return replacedBy;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ESummary id: " + id);
        sb.append("\n\t  Caption: " + caption);
        sb.append("\n\t  Title: " + title);
        sb.append("\n\t  Extra: " + extra);
        sb.append("\n\t  Gi: " + gi);
        sb.append("\n\t  CreateDate: " + createDate);
        sb.append("\n\t  UpdateDate: " + updateDate);
        sb.append("\n\t  Flags: " + flags);
        sb.append("\n\t  TaxId: " + taxId);
        sb.append("\n\t  Length: " + length);
        sb.append("\n\t  Status: " + status);
        sb.append("\n\t  ReplacedBy: " + replacedBy);
        sb.append("\n\t  Comment: " + comment);
        sb.append("\n\t  Length: " + length);

        return sb.toString();
    }


}
