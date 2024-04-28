package com.cmpe.cosmos.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cmpe.cosmos.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user")
        private val USER_ID = intPreferencesKey("user_id")
        private val USERNAME = stringPreferencesKey("username")
        private val MEMBERSHIP = stringPreferencesKey("membership")
        private val REWARDS = intPreferencesKey("rewards")
    }

    fun getUser(): Flow<UserModel> = context.dataStore.data.map { preferences ->
        UserModel(
            userId = preferences[USER_ID] ?: 1,
            username = preferences[USERNAME] ?: "",
            membership = preferences[MEMBERSHIP] ?: "",
            rewards = preferences[REWARDS] ?: 0
        )
    }

    suspend fun saveUser(user: UserModel) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = user.userId
            preferences[USERNAME] = user.username
            preferences[MEMBERSHIP] = user.membership
            preferences[REWARDS] = user.rewards
        }
    }

    suspend fun saveMembershipType(type: String) {
        context.dataStore.edit { preferences ->
            preferences[MEMBERSHIP] = type
        }
    }
}