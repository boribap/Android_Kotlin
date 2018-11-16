package io.github.boribab.button_event

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Countup()
    }

    fun Countup() {
        var btn = btn_count

        btn.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                btn.setText((++counter).toString())
            }
        })
    }
}
