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

import dagger.Module

@Module(includes = [ViewModelModule::class])
class AppModule {

    /*@Singleton
    @Provides
    fun providePostsService(): PodcastService {
        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BASIC
        val httpClient = OkHttpClient.Builder()
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging)  // <-- this is the important line!


        return Retrofit.Builder()
                .baseUrl("https://podcast.3pies.es/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .client(httpClient.build())
                .build()
                .create(PodcastService::class.java)
    }*/


/*
    @Singleton
    @Provides
    fun providePostsDb(app: Application): PostsDb {
        return Room
                .databaseBuilder(app, PostsDb::class.java, "posts.db")
                .fallbackToDestructiveMigration()
                .build()
    }
*/

}
