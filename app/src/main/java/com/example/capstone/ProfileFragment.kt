package com.example.capstone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        // Mengakses komponen-komponen UI secara langsung
        val profileImage: ImageView = rootView.findViewById(R.id.profile_image)
        val usernameTextView: TextView = rootView.findViewById(R.id.tv_username)
        val emailTextView: TextView = rootView.findViewById(R.id.tv_email)
        val languageTextView: TextView = rootView.findViewById(R.id.tv_language)
        val modeTextView: TextView = rootView.findViewById(R.id.tv_mode)
        val logoutButton: ImageButton = rootView.findViewById(R.id.btn_logout)

        // Menetapkan data profil (ini hanya contoh, Anda bisa mengganti dengan data nyata)
        profileImage.setImageResource(R.drawable.ic_avatar)
        usernameTextView.text = "Username"
        emailTextView.text = "example@gmail.com"
        languageTextView.text = "Bahasa Indonesia"
        modeTextView.text = "Terang"

        // Menangani logout button
        logoutButton.setOnClickListener {
            // Tulis logika untuk logout di sini
        }

        return rootView
    }
}
