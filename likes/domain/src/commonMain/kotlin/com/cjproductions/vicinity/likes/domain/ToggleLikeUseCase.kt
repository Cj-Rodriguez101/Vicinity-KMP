package com.cjproductions.vicinity.likes.domain

import com.cjproductions.vicinity.core.domain.util.EmptyResult
import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.core.domain.util.asEmptyDataResult
import com.cjproductions.vicinity.likes.domain.DatabaseError.Unknown
import com.cjproductions.vicinity.likes.domain.model.Like
import com.cjproductions.vicinity.support.user.UserRepository
import kotlinx.coroutines.flow.firstOrNull

class ToggleLikeUseCase(
    private val likesRepository: LikesRepository,
    private val userRepository: UserRepository,
) {
    suspend fun invoke(
        normalizedTitle: String,
        title: String,
        category: String,
        isLiked: Boolean,
        image: String? = null,
        startDateTime: Double? = null,
        endDateTime: Double? = null,
    ): EmptyResult<DatabaseError> {
        val userId =
            userRepository.user.firstOrNull()?.id
                ?: return Result.Error(Unknown("UNKNOWN_ERROR")).asEmptyDataResult()

        return if (isLiked) {
            likesRepository.deleteLike(
                like = Like(
                    userId = userId,
                    normalizedTitle = normalizedTitle,
                    title = title,
                    category = category,
                    startDateTime = startDateTime,
                    endDateTime = endDateTime,
                )
            )
        } else {
            likesRepository.insertLike(
                like = Like(
                    userId = userId,
                    normalizedTitle = normalizedTitle,
                    title = title,
                    category = category,
                    image = image,
                    startDateTime = startDateTime,
                    endDateTime = endDateTime,
                )
            )
        }
    }
}