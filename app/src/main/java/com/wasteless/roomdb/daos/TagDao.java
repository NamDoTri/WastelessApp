package com.wasteless.roomdb.daos;

import com.wasteless.roomdb.entities.*;

import androidx.room.Dao;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TagDao {
    @Query("select * from tags")
    List<Tag> getAll();

    @Query("select * from tags where tag like :name")
    List<Tag> getTagsByName(String name);

    @Insert(entity = Tag.class, onConflict = OnConflictStrategy.IGNORE)
    Long insertAll(Tag... tag);

    @Update(entity = Tag.class, onConflict = OnConflictStrategy.IGNORE)
    Long updateAll(Tag... tag);

    @Delete
    void delete(Tag tag);

    // tag_assoc queries
    @Query("select * from tag_assoc where transactionId = :transactionId")
    List<String> getAllTagsOf(Long transactionId);

    @Query("select * from tag_assoc where tag =:tagName")
    List<Long> getAllTransactionIdsOf(String tagName);

    //TODO: experiment with specifying different entities for insert and update
    @Query("insert into tag_assoc(transactionId, tag) values (:transactionId, :tagName)")
    void insertTagAssociation(Long transactionId, String tagName);

    @Query("delete from tag_assoc where transactionId = :transactionId and tag = :tagName")
    void deleteTagAssociation(Long transactionId, String tagName);
}
