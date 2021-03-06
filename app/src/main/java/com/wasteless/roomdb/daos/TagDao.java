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
    @Query("select * from tag_assoc")
    List<TagAssociation> getAll();

    @Query("select * from tags where tag like :name")
    List<Tag> getTagsByName(String name);

    @Insert(entity = Tag.class, onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertAll(Tag... tag);

    @Update(entity = Tag.class, onConflict = OnConflictStrategy.IGNORE)
    int updateAll(Tag... tag);

    @Delete
    void delete(Tag tag);

    // tag_assoc queries
    @Query("select tag from tag_assoc where transactionId = :transactionId")
    List<String> getAllTagsOf(Long transactionId);

    @Query("select transactionId from tag_assoc where tag =:tagName")
    List<Long> getAllTransactionIdsOf(String tagName);

    @Insert(entity = TagAssociation.class, onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertAllTagAssociation(TagAssociation... tagAssociations);

    @Update(entity = TagAssociation.class, onConflict = OnConflictStrategy.IGNORE)
    int updateAllTagAssociations(TagAssociation... tagAssociations);
}
