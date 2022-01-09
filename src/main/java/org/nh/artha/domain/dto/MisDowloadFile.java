package org.nh.artha.domain.dto;

import java.util.Arrays;

public class MisDowloadFile {
    private String contentType;
    private byte[] content;
    private long contentLength;
    private String fileName;

    public String getContentType() {

        return contentType;
    }

    public void setContentType(String contentType) {

        this.contentType = contentType;
    }

    public byte[] getContent() {

        return Arrays.copyOf(content, content.length);
    }

    public void setContent(byte[] content) {
        this.setContentLength(content.length);
        this.content = Arrays.copyOf(content, content.length);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {

        this.fileName = fileName;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MisDowloadFile that = (MisDowloadFile) o;

        if (contentLength != that.contentLength) return false;
        if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null) return false;
        if (!Arrays.equals(content, that.content)) return false;
        return fileName != null ? fileName.equals(that.fileName) : that.fileName == null;
    }

    @Override
    public int hashCode() {
        int result = contentType != null ? contentType.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(content);
        result = 31 * result + (int) (contentLength ^ (contentLength >>> 32));
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MisDownloadFile{" +
            "contentType='" + contentType + '\'' +
            ", content=" + Arrays.toString(content) +
            ", contentLength=" + contentLength +
            ", fileName='" + fileName + '\'' +
            '}';
    }

}
