/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trespies.posts.di

import android.app.Application
import androidx.room.Room
import com.trespies.posts.api.PostService
import com.trespies.posts.api.clients.PostsRestClient
import com.trespies.posts.db.PostsDB
import com.trespies.posts.db.PostsDao
import com.trespies.posts.services.Configuration
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun providePostsService(configuration: Configuration): PostService {
        return PostsRestClient(configuration).client
    }

    @Singleton
    @Provides
    fun providePostsDB(app: Application, configuration: Configuration): PostsDB {
        return Room
                .databaseBuilder(app, PostsDB::class.java, configuration.databaseName)
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun providesPostsDAO(db: PostsDB) : PostsDao {
        return db.postsDAO()
    }

    @Singleton
    @Provides
    fun providesConfiguration(app: Application) : Configuration {
        return Configuration(app)
    }


}
