package com.example.studentnest.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.studentnest.data.AppDatabase
import com.example.studentnest.databinding.ActivityLoginBinding
import com.example.studentnest.ui.listings.ListingsActivity
import com.example.studentnest.ui.register.RegisterActivity
import com.example.studentnest.util.SessionManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        // Skip straight to Listings if a session already exists
        if (session.isLoggedIn()) {
            goToListings()
            return
        }

        binding.btnLogin.setOnClickListener { attemptLogin() }
        binding.tvGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun attemptLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            binding.tilEmail.error = if (email.isEmpty()) "Email required" else null
            binding.tilPassword.error = if (password.isEmpty()) "Password required" else null
            return
        }
        binding.tilEmail.error = null
        binding.tilPassword.error = null

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val student = db.studentDao().login(email, password)
            if (student != null) {
                session.saveSession(student.studentId, student.fullName)
                goToListings()
            } else {
                binding.tilPassword.error = "Invalid email or password"
            }
        }
    }

    private fun goToListings() {
        startActivity(Intent(this, ListingsActivity::class.java))
        finish()
    }
}
