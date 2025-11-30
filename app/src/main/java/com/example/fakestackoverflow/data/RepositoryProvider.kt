package com.example.fakestackoverflow.data

import android.content.Context
import com.example.fakestackoverflow.data.local.AppDatabase
import com.example.fakestackoverflow.data.remote.NetworkModule
import com.example.fakestackoverflow.data.repository.StackRepository

object RepositoryProvider {
    fun provideRepository(context: Context): StackRepository {
        val db = AppDatabase.getInstance(context)

        val api = NetworkModule.provideStackApi()
        val questionDao = db.questionDao()
        val answerDao = db.answerDao()

        return StackRepository(
            context = context.applicationContext,
            api = api,
            questionDao = questionDao,
            answerDao = answerDao
        )
    }
}
