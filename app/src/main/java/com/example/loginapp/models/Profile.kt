package com.example.loginapp.models
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val church_id: Int,
    val date_created: String,
    val first_name: String,
    val gender: String,
    val id_number: String,
    val member_id: Int,
    val mobile_number: String,
    val password: String,
    val registration_id: Int,
    val second_name: String,
    val status: Int,
    val surname: String,
    val token: String
)