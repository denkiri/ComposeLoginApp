package com.example.loginapp.models
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity
data class Profile(
    @PrimaryKey(autoGenerate = false)
    val member_id: Int,
    val date_created: String,
    val email: String,
    val token: String
)