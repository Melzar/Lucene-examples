package net.codelab.core.entity.dto;

import java.util.Map;

/**
 * Created by Melzarek on 21/11/13.
 */
public class Course {

    private String reg_num;
    private String subj;
    private String crse;
    private String sect;
    private String title;
    private String units;
    private String instructor;
    private String days;
    private String start_time;
    private String end_time;
    private String building;
    private String room;

    public Course()
    {
        subj = "";
        sect = "";
        title = "";
        instructor = "";
        days = "";
        start_time = "";
        end_time = "";
        building = "";
        room = "";
    }

    public String getReg_num() {
        return reg_num;
    }

    public void setReg_num(String reg_num) {
        this.reg_num = reg_num;
    }

    public String getSubj() {
        return subj;
    }

    public void setSubj(String subj) {
        this.subj = subj;
    }

    public String getCrse() {
        return crse;
    }

    public void setCrse(String crse) {
        this.crse = crse;
    }

    public String getSect() {
        return sect;
    }

    public void setSect(String sect) {
        this.sect = sect;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
