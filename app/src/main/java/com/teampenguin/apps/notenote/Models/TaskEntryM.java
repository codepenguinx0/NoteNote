package com.teampenguin.apps.notenote.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.teampenguin.apps.notenote.Utils.Utils;

import java.util.Date;

@Entity(tableName = "task_entries")
public class TaskEntryM implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "task_title")
    private String taskTitle;

    @ColumnInfo(name = "create_date")
    private String createDate;

    @ColumnInfo(name = "alert_time")
    private String deadline;

    @ColumnInfo(name = "is_alerted")
    private int isAlerted;

    @ColumnInfo(name = "is_done")
    private int isDone;

    @ColumnInfo(name = "is_active")
    private int isActive;

    public TaskEntryM()
    {
        taskTitle = "";
        createDate = Utils.convertDateToString(new Date());
        deadline = "";
        isAlerted = 0;
        isDone = 0;
        isActive = 1;
    }

    protected TaskEntryM(Parcel in) {
        id = in.readInt();
        taskTitle = in.readString();
        createDate = in.readString();
        deadline = in.readString();
        isAlerted = in.readInt();
        isDone = in.readInt();
        isActive = in.readInt();
    }

    public static final Creator<TaskEntryM> CREATOR = new Creator<TaskEntryM>() {
        @Override
        public TaskEntryM createFromParcel(Parcel in) {
            return new TaskEntryM(in);
        }

        @Override
        public TaskEntryM[] newArray(int size) {
            return new TaskEntryM[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getIsAlerted() {
        return isAlerted;
    }

    public void setIsAlerted(int isAlerted) {
        this.isAlerted = isAlerted;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(taskTitle);
        dest.writeString(createDate);
        dest.writeString(deadline);
        dest.writeInt(isAlerted);
        dest.writeInt(isDone);
        dest.writeInt(isActive);
    }
}
