package ru.dinarastepina.myapplication.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.scichart.charting.visuals.SciChartSurface
import ru.dinarastepina.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var vb: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            SciChartSurface.setRuntimeLicenseKey("J44eFPvLml4yzvpXb9tZwDv6tEKJev90nfg2kE4YzvmEJBjIfFgr7n6G07u2lTOX2/nFLcP50xIaZCS9PZtNrDznaztk9NAnMHMDb0jUDDQ0uOyt8w+td/obYJURexm9PxqUEjJbFvyWuSoDUqnFY1Da1J9IbVAdxL1ZEfmCGj37z5oIYlgkXaZ2Whxf9GuyMtaaKi9smxOkWwVEFHGMmPZOT/UNjISpU9u7LTscNmt0FukPgZOAArDnwp1LQFBRbwOosJJvjCqpkZPhHbi5GSF23GDJ9a2fEqQm0RXloV8JJLtQcfwINLPX9HnSH8TjXw1eiDFwKJfKV/mGWkUQJHqtAueq2qcU2AkOI0lkG7unAGb/Fg/UdhfiylHhXLZttYzEwLzQ5og4VZDPglgHEURbS4hwdqdfALWhsOGpKsyOAMaWpXDdNchqOSzrn2N9hQcqOS0IAmHmzOLJKtOEhLxKNDhqH8wN0aEvhTYIoHwON7WWTckrG0NtS6NNA0Endh272amADxzGhKG9W5jQo62BR2MTQbec9nkmElJXVy4qqhRsexCg/SA6P1mEzVbzvIlhzYOuaKUm1qFz96s=");
        } catch (e: Exception) {
            Log.e("SciChart", "Error when setting the license", e)
        }

        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)
    }
}