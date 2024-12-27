package com.example.lb4.classes;

import java.util.Date;

public class Note {
    private String Title;
    private String Description;
    private int Priority;
    private Date CreateDate;
    private Date DueDate;
    private int DueHour;
    private String PhotoPath;

public Note(String Title, String Description, int Priority, Date CreateDate, Date DueDate, int DueHour, String PhotoPath) {
    this.Title = Title;
    this.Description = Description;
    this.Priority = Priority;
    this.CreateDate = CreateDate;
    this.DueDate = DueDate;
    this.PhotoPath = PhotoPath;
    this.DueHour = DueHour;
    }
    public String GetTitle(){
        return Title;
    }
    public void SetTitle(String Title){
        this.Title = Title;
    }
    public String GeDescription(){
        return Description;
    }
    public void SetDescription(String Description){
        this.Description = Description;
    }
    public int GetPriority(){
        return Priority;
    }
    public void SetPriority(int Priority){
        this.Priority = Priority;
    }
    public Date GetDate(){
        return CreateDate;
    }
    public void SetDate(Date CreateDate){
        this.CreateDate = CreateDate;
    }
    public Date GetDueDate() {
        return DueDate;
    }
    public void SetDueDate(Date DueDate) {
        this.DueDate = DueDate;
    }
    public int GetDueHour() {
        return DueHour;
    }

    public void SetDueHour(int DueHour) {
        this.DueHour = DueHour;
    }
    public String GetPhotoPath(){
        return PhotoPath;
    }
    public void SetPhotoPath(String PhotoPath){
        this.PhotoPath = PhotoPath;
    }
}
