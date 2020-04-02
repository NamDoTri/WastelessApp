package com.wasteless.roomdb.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "tag_assoc",
        foreignKeys = {@ForeignKey(entity = Transaction.class,
                                    parentColumns = "transactionId",
                                    childColumns = "transactionId",
                                    onDelete = ForeignKey.CASCADE,
                                    onUpdate = ForeignKey.CASCADE),
                       @ForeignKey(entity = Tag.class,
                                    parentColumns = "tag",
                                    childColumns = "tag",
                                    onDelete = ForeignKey.CASCADE,
                                    onUpdate = ForeignKey.CASCADE)})
public class TagAssociation {
    @PrimaryKey(autoGenerate = true)
    public Long index;

    @ColumnInfo(name = "transactionId")
    public Long transactionId;

    @ColumnInfo(name = "tag")
    public String tag;

    public TagAssociation(Long transactionId, String tag){
        this.transactionId = transactionId;
        this.tag = tag;
    }
}
