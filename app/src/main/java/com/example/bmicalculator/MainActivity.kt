package com.example.bmicalculator

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.slider.Slider

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val button = findViewById<AppCompatButton>(R.id.calcButton)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val bmiAnswer = findViewById<TextView>(R.id.answer)
        val bmiCategory = findViewById<TextView>(R.id.bmiCategory)

        button.setOnClickListener {
            val weight = findViewById<Slider>(R.id.weightSlider).value
            val height = findViewById<Slider>(R.id.heightSlider).value



            val heightSquared = height * height

            // Calculate BMI
            val bmi = weight / heightSquared

            // Show the ProgressBar while calculating BMI
            progressBar.visibility = View.VISIBLE
            bmiAnswer.visibility = View.INVISIBLE
            bmiCategory.visibility = View.INVISIBLE

            // Simulate a delay (e.g., network request or complex calculation)
            Thread {
                try {
                    Thread.sleep(1500) // Simulate processing time
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                // Update the UI on the main thread
                runOnUiThread {
                    // Hide ProgressBar
                    progressBar.visibility = View.GONE

                    // Display the BMI result
                    bmiAnswer.text = String.format("%.2f", bmi)
                    bmiAnswer.visibility = View.VISIBLE


                    val (category, categoryColor) = getBMICategoryAndColor(bmi)

                    // Update the BMI category and set the color
                    bmiCategory.text = category
                    bmiCategory.setTextColor(categoryColor)
                    bmiCategory.visibility = View.VISIBLE
                }
            }.start()
        }

        // Handle edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Determine BMI category and corresponding color
    private fun getBMICategoryAndColor(bmi: Float): Pair<String, Int> {
        return when {
            bmi < 18.5 -> "Underweight" to resources.getColor(R.color.underweight)
            bmi in 18.5..24.9 -> "Normal weight" to resources.getColor(R.color.normal_weight)
            bmi in 25.0..29.9 -> "Overweight" to resources.getColor(R.color.overweight)
            else -> "Obesity" to resources.getColor(R.color.obesity)
        }
    }
}
