package by.iapsit.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.iapsit.core.model.PackageInfo

@Entity(tableName = "packages")
internal data class PackageEntity(
    @PrimaryKey @ColumnInfo(name = "package_name") val packageName: String,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean,
)

internal fun PackageInfo.toEntity() = PackageEntity(packageName, isFavourite)

internal fun PackageEntity.toModel() = PackageInfo(packageName, isFavourite)

internal fun List<PackageEntity>.toModel() = map { it.toModel() }