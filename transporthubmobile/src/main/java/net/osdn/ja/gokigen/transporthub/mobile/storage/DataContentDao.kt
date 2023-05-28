package net.osdn.ja.gokigen.transporthub.mobile.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DataContentDao
{
    @Query("SELECT * FROM contents")
    fun getAll(): List<DataContent>

    @Query("SELECT * FROM contents WHERE id IN (:ids)")
    fun getAllByIds(ids: IntArray): List<DataContent>

    @Query("SELECT * FROM contents WHERE id = :id LIMIT 1")
    fun findById(id: Int): DataContent?
    @Query("SELECT * FROM contents WHERE hash_value = :hash")
    fun findByHash(hash: String): List<DataContent>

    @Query("SELECT * FROM contents WHERE title LIKE :mainTitle AND " + "sub_title LIKE :subTitle LIMIT 1")
    fun findByTitle(mainTitle: String, subTitle: String): DataContent?

    @Insert
    fun insertAll(vararg contents: DataContent)

    @Delete
    fun delete(content: DataContent)

}
