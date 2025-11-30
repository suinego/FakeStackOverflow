package com.example.fakestackoverflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fakestackoverflow.ui.questions.QuestionFragment
import com.example.fakestackoverflow.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, QuestionFragment())
                .commit()
        }
    }
}