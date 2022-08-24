package by.iapsit.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.iapsit.data.db.entities.PackageEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface PackagesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPackage(packageEntity: PackageEntity)

    @Query("SELECT * FROM packages WHERE is_favourite = :isFavourite AND package_name IN (SELECT DISTINCT package_name FROM notifications WHERE is_deleted = 0)")
    fun getPackages(isFavourite: Boolean): Flow<List<PackageEntity>>

    @Query("UPDATE packages SET is_favourite = :isFavourite WHERE package_name = :packageName")
    suspend fun updateFavouritePackage(packageName: String, isFavourite: Boolean)

    @Query("DELETE FROM packages WHERE package_name NOT IN (SELECT DISTINCT package_name FROM notifications WHERE is_deleted = 0)")
    suspend fun deleteEmptyPackages()
}