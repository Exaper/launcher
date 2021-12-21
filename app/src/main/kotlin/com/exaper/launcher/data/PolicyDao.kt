package com.exaper.launcher.data

import androidx.room.*
import com.exaper.launcher.api.data.DenyPolicy
import kotlinx.coroutines.flow.Flow

@Dao
interface PolicyDao {
    @Query("SELECT * FROM deny_policy")
    fun getDenyList(): Flow<List<DenyPolicy>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDenyList(list: List<DenyPolicy>)

    @Query("DELETE FROM deny_policy")
    suspend fun clearDenyList()

    @Transaction
    suspend fun replaceDenyListWith(newList: List<DenyPolicy>){
        clearDenyList()
        insertDenyList(newList)
    }
}