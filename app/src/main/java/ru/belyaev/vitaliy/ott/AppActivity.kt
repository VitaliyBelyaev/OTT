package ru.belyaev.vitaliy.ott

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, MainFragment())
            .commit()
    }
}
