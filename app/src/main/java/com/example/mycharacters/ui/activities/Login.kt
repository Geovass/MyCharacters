package com.example.mycharacters.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mycharacters.R
import com.example.mycharacters.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private var email: String = ""
    private var contra: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        if(firebaseAuth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


        with(binding){
            loginButton.setOnClickListener {
                if(!validaCampos()) return@setOnClickListener

                autenticaUsuario(email, contra)
            }

            registerButton.setOnClickListener {
                if(!validaCampos()) return@setOnClickListener

                registraUsuario(email, contra)
            }

            tvForgotPassword.setOnClickListener {
                val resetEmail = EditText(it.context)
                resetEmail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

                AlertDialog.Builder(it.context)
                    .setTitle(getString(R.string.reset_password))
                    .setMessage(getString(R.string.enterEmailReset))
                    .setView(resetEmail)
                    .setPositiveButton(getString(R.string.enviar)){ _, _ ->
                        val mail = resetEmail.text.toString()
                        if(mail.isNotEmpty()){
                            firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                                Toast.makeText(this@Login,
                                    getString(R.string.emailSentSuccess), Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                Toast.makeText(this@Login,
                                    getString(R.string.emailSentFailure), Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this@Login,
                                getString(R.string.pleaseEnterEmail), Toast.LENGTH_SHORT).show()
                        }
                    }.setNegativeButton(getString(R.string.cancel)){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
    }

    private fun validaCampos(): Boolean{
        email = binding.etEmail.text.toString().trim()
        contra = binding.etPassword.text.toString().trim()

        if(email.isEmpty()){
            binding.etEmail.error = getString(R.string.emailRequired)
            binding.etEmail.requestFocus()
            return false
        }

        if(contra.isEmpty() || contra.length < 6){
            binding.etPassword.error = getString(R.string.passwordLengthRequired)
            binding.etPassword.requestFocus()
            return false
        }

        return true
    }

    private fun manejaErrores(task: Task<AuthResult>){
        var errorCode = ""

        try{
            errorCode = (task.exception as FirebaseAuthException).errorCode
        }catch(e: Exception){
            e.printStackTrace()
        }

        when(errorCode){
            getString(R.string.error_invalid_email) -> {
                Toast.makeText(this, getString(R.string.invalidEmail), Toast.LENGTH_SHORT).show()
                binding.etEmail.error = getString(R.string.invalidEmail)
                binding.etEmail.requestFocus()
            }
            getString(R.string.error_wrong_password) -> {
                Toast.makeText(this, getString(R.string.invalidPassword), Toast.LENGTH_SHORT).show()
                binding.etPassword.error = getString(R.string.invalidPassword)
                binding.etPassword.requestFocus()
                binding.etPassword.setText("")

            }
            getString(R.string.account_exists) -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                Toast.makeText(this, getString(R.string.existingAccount), Toast.LENGTH_SHORT).show()
            }
            getString(R.string.error_email) -> {
                Toast.makeText(this, getString(R.string.emailAlreadyUsed), Toast.LENGTH_LONG).show()
                binding.etEmail.error = getString(R.string.emailAlreadyUsed)
                binding.etEmail.requestFocus()
            }
            getString(R.string.error_user_token_expired) -> {
                Toast.makeText(this, getString(R.string.expired_session), Toast.LENGTH_LONG).show()
            }
            getString(R.string.error_user_not_found) -> {
                Toast.makeText(this, getString(R.string.inexistent_user), Toast.LENGTH_LONG).show()
            }
            getString(R.string.error_password) -> {
                Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_LONG).show()
                binding.etPassword.error = getString(R.string.password_length)
                binding.etPassword.requestFocus()
            }
            getString(R.string.no_network) -> {
                Toast.makeText(this, getString(R.string.noRed), Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this,
                    getString(R.string.authentication_failure), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun autenticaUsuario(usr: String, psw: String){
        binding.pbConexion.visibility = View.VISIBLE

        firebaseAuth.signInWithEmailAndPassword(usr, psw).addOnCompleteListener { authResult ->
            binding.pbConexion.visibility = View.GONE

            if(authResult.isSuccessful){
                Toast.makeText(this,
                    getString(R.string.autenticacion_exitosa), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                manejaErrores(authResult)
            }
        }
    }

    private fun registraUsuario(usr: String, psw: String){
        binding.pbConexion.visibility = View.VISIBLE

        firebaseAuth.createUserWithEmailAndPassword(usr, psw).addOnCompleteListener { authResult ->

            if(authResult.isSuccessful){

                val userFb = firebaseAuth.currentUser

                userFb?.sendEmailVerification()?.addOnSuccessListener {
                    Toast.makeText(this, getString(R.string.email_sent), Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener {
                    Toast.makeText(this,
                        getString(R.string.email_not_sent), Toast.LENGTH_SHORT).show()
                }

                Toast.makeText(this, getString(R.string.usuario_creado), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                manejaErrores(authResult)
            }


        }
    }
}