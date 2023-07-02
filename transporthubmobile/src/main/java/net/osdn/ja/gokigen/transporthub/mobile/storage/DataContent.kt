package net.osdn.ja.gokigen.transporthub.mobile.storage


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "contents", indices = [Index(value = ["hash_value"], unique = true)])
data class DataContent(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "sub_title") val subTitle: String?,
    @ColumnInfo(name = "hash_value") val hashValue: String?,
    @ColumnInfo(name = "note") val note: String?,
    @ColumnInfo(name = "is_attachedFile") val isAttachedFile: Boolean,
    @ColumnInfo(name = "file_name") val fileName: String?,
    @ColumnInfo(name = "received_date") val receivedDate: Date?,
    @ColumnInfo(name = "send_date") val sendDate: Date?,
    @ColumnInfo(name = "checked") val checked: Boolean,
    @ColumnInfo(name = "shared_date") val sharedDate: Date?,
)
{
    companion object {
        fun create(title: String?, hash: String?, data: String?) : DataContent {
            val currentDate = Date()
            return DataContent(0, title, null, hash, data, false, null, currentDate, null, false, null)
        }
    }
}
