package com.example.talimlectures.domain.mapper

import com.example.talimlectures.data.model.DatabaseCategoryModel
import com.example.talimlectures.domain.dto.CategoryModel

fun CategoryModel.convertToDatabaseCategoryModel(): DatabaseCategoryModel {
    return DatabaseCategoryModel(
        categoryId = this.categoryId!!,
        categoryName = this.categoryName,
        uniqueId = this.uniqueId!!
    )
}
fun DatabaseCategoryModel.convertToCategoryModel():CategoryModel{
    return CategoryModel(
        categoryId = this.categoryId,
        categoryName = this.categoryName,
        uniqueId = this.uniqueId
    )
}