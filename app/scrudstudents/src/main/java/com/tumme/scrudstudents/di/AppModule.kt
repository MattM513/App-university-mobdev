package com.tumme.scrudstudents.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tumme.scrudstudents.data.local.AppDatabase
import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.SubscribeDao
import com.tumme.scrudstudents.data.local.dao.TeacherDao
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        teacherDaoProvider: Provider<TeacherDao>
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "scrud-db")
            .fallbackToDestructiveMigration()
            .addCallback(DatabaseCallback(teacherDaoProvider))
            .build()
    }

    private class DatabaseCallback(
        private val teacherDaoProvider: Provider<TeacherDao>
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                teacherDaoProvider.get().insert(
                    TeacherEntity(
                        idTeacher = 1,
                        lastName = "Default",
                        firstName = "Teacher",
                        email = "teacher@default.com",
                        password = "password"
                    )
                )
            }
        }
    }

    @Provides fun provideStudentDao(db: AppDatabase): StudentDao = db.studentDao()
    @Provides fun provideCourseDao(db: AppDatabase): CourseDao = db.courseDao()
    @Provides fun provideSubscribeDao(db: AppDatabase): SubscribeDao = db.subscribeDao()

    @Provides fun provideTeacherDao(db: AppDatabase): TeacherDao = db.teacherDao()

    @Provides
    @Singleton
    fun provideRepository(
        studentDao: StudentDao,
        courseDao: CourseDao,
        subscribeDao: SubscribeDao,
        teacherDao: TeacherDao
    ): SCRUDRepository =
        SCRUDRepository(studentDao, courseDao, subscribeDao, teacherDao)
}