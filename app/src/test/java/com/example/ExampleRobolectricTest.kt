package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.example.ui.navigation.TrendyDestination

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Trendy", appName)
  }

  @Test
  fun `verify navigation destinations non null`() {
    val items = TrendyDestination.bottomNavItems
    assertEquals(4, items.size)
    items.forEach { dest ->
      assertNotNull("Destination must not be null", dest)
      assertNotNull("Route must not be null", dest.route)
      assertNotNull("Title must not be null", dest.title)
    }
  }

  @Test
  fun `verify developer attribution information`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val title = context.getString(R.string.developer_info_title)
    val devName = context.getString(R.string.developer_name)
    val phone = context.getString(R.string.developer_phone)
    val email = context.getString(R.string.developer_email)
    val rawPhone = context.getString(R.string.developer_phone_raw)
    val rawEmail = context.getString(R.string.developer_email_raw)

    assertEquals("Developer Information", title)
    assertEquals("Developed by Arbab Rizvi", devName)
    assertEquals("📞 030383631699", phone)
    assertEquals("✉️ arbabrixvi@gmail.com", email)
    assertEquals("030383631699", rawPhone)
    assertEquals("arbabrixvi@gmail.com", rawEmail)
  }
}
