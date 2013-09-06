/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Judy
 */
@Entity
@Table(name = "upload_files_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UploadFilesHistory.findAll", query = "SELECT u FROM UploadFilesHistory u"),
    @NamedQuery(name = "UploadFilesHistory.findById", query = "SELECT u FROM UploadFilesHistory u WHERE u.id = :id"),
    @NamedQuery(name = "UploadFilesHistory.findByFileName", query = "SELECT u FROM UploadFilesHistory u WHERE u.fileName = :fileName"),
    @NamedQuery(name = "UploadFilesHistory.findByFileDate", query = "SELECT u FROM UploadFilesHistory u WHERE u.fileDate = :fileDate"),
    @NamedQuery(name = "UploadFilesHistory.findByUploadDate", query = "SELECT u FROM UploadFilesHistory u WHERE u.uploadDate = :uploadDate"),
    @NamedQuery(name = "UploadFilesHistory.findByUploadUser", query = "SELECT u FROM UploadFilesHistory u WHERE u.uploadUser = :uploadUser")})
public class UploadFilesHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(max = 255)
    @Column(name = "file_name", length = 255)
    private String fileName;
    @Column(name = "file_date")
    @Temporal(TemporalType.DATE)
    private Date fileDate;
    @Column(name = "upload_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;
    @Size(max = 255)
    @Column(name = "upload_user", length = 255)
    private String uploadUser;

    public UploadFilesHistory() {
    }

    public UploadFilesHistory(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getFileDate() {
        return fileDate;
    }

    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UploadFilesHistory)) {
            return false;
        }
        UploadFilesHistory other = (UploadFilesHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.autopay.crm.model.UploadFilesHistory[ id=" + id + " ]";
    }
    
}
