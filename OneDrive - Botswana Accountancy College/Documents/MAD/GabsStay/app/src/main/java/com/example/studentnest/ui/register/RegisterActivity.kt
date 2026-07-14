package com.example.studentnest.ui.register

import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.studentnest.data.AppDatabase
import com.example.studentnest.data.Student
import com.example.studentnest.databinding.ActivityRegisterBinding
import com.example.studentnest.ui.listings.ListingsActivity
import com.example.studentnest.util.SessionManager
import kotlinx.coroutines.launch
import android.content.Intent

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        binding.btnRegister.setOnClickListener { attemptRegister() }
    }

    private fun attemptRegister() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val isProvider = binding.switchProvider.isChecked

        var valid = true
        if (name.isEmpty()) {
            binding.tilName.error = "Name required"; valid = false
        } else binding.tilName.error = null

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Enter a valid email"; valid = false
        } else binding.tilEmail.error = null

        if (password.length < 6) {
            binding.tilPassword.error = "Minimum 6 characters"; valid = false
        } else binding.tilPassword.error = null

        if (!valid) return

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val existing = db.studentDao().findByEmail(email)
            if (existing != null) {
                binding.tilEmail.error = "An account with this email already exists"
                return@launch
            }
            val role = if (isProvider) "PROVIDER" else "STUDENT"
            val newId = db.studentDao().register(
                Student(fullName = name, email = email, password = password, role = role)
            )
            session.saveSession(newId.toInt(), name)
            startActivity(Intent(this@RegisterActivity, ListingsActivity::class.java))
            finish()
        }
    }
}
