package com.example.ziyara.utils

object AppStrings {
    // General
    fun getSettings(lang: String) = if (lang == "AR") "الإعدادات" else "Settings"
    fun getFree(lang: String) = if (lang == "AR") "مجاني" else "Free"
    fun getCancel(lang: String) = if (lang == "AR") "إلغاء" else "Cancel"

    // Home Screen
    fun getZiyara(lang: String) = "Ziyara"
    fun getDiscoverEgypt(lang: String) = if (lang == "AR") "اكتشف مصر" else "Discover Egypt"
    fun getSearchPlaceholder(lang: String) = if (lang == "AR") "ابحث..." else "Search..."
    fun getFeaturedTitle(lang: String) = if (lang == "AR") "وجهة مميزة" else "FEATURED DESTINATION"
    fun getExploreButton(lang: String) = if (lang == "AR") "استكشف ›" else "Explore ›"
    fun getExploreByType(lang: String) = if (lang == "AR") "استكشف حسب النوع" else "Explore by Type"

    // Favorites Screen
    fun getFavoritesTitle(lang: String) = if (lang == "AR") "المفضلة" else "Favorite Places"
    fun getNoFavorites(lang: String) = if (lang == "AR") "لا توجد أماكن مفضلة بعد." else "No favorite places yet."
    fun getClearAll(lang: String) = if (lang == "AR") "مسح الكل" else "Clear All"
    fun getConfirmClear(lang: String) = if (lang == "AR") "هل أنت متأكد من مسح جميع الأماكن؟" else "Are you sure you want to remove all places from your favorites?"

    // Sort Options
    fun getDefaultSort(lang: String) = if (lang == "AR") "الافتراضي" else "Default"
    fun getPriceLowHigh(lang: String) = if (lang == "AR") "السعر: من الأقل" else "Price: Low to High"
    fun getPriceHighLow(lang: String) = if (lang == "AR") "السعر: من الأعلى" else "Price: High to Low"
    fun getRatingLowHigh(lang: String) = if (lang == "AR") "التقييم: من الأقل" else "Rating: Low to High"
    fun getRatingHighLow(lang: String) = if (lang == "AR") "التقييم: من الأعلى" else "Rating: High to Low"
}