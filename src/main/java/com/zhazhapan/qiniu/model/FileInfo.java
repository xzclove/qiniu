/**
 *
 */
package com.zhazhapan.qiniu.model;

/**
 * @author pantao
 */
public class FileInfo implements Comparable<FileInfo>{

    private String name;

    private String type;

    private String size;

    private String time;

    private long putTime;

    public FileInfo() {

    }

    public FileInfo(String name, String type, String size, String time) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.time = time;
    }

    public long getPutTime() {
        return putTime;
    }

    public void setPutTime(long putTime) {
        this.putTime = putTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int compareTo(FileInfo r) {
        if (this == r) {
            return 0;
        }
        if (this.getPutTime() == r.getPutTime()) {
            return 0;
        }
        if (this.getPutTime() > r.getPutTime()) {
            return -1;
        }
        if (this.getPutTime() < r.getPutTime()) {
            return 1;
        }
        return 0;
    }
}
