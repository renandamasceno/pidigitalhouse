package com.renan.digitalspace.favorite.adapter

import com.google.android.material.card.MaterialCardView
import com.renan.digitalspace.favorite.entity.FavoriteEntity

interface IFavorite {
    fun changedFavorite(position: Int, favorite: FavoriteEntity, cardView: MaterialCardView)
}