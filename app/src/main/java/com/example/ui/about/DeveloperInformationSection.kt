package com.example.ui.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.TrendyNeonCyan
import com.example.ui.theme.TrendySurfaceCard
import com.example.ui.theme.TrendyTextPrimary
import com.example.ui.theme.TrendyTextSecondary

/**
 * Developer Information attribution section.
 *
 * Displays developer attribution for the Trendy application.
 * All phone and email interactions trigger native Android dialer and email client
 * intents strictly on the local device without sending or collecting data.
 */
@Composable
fun DeveloperInformationSection(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .testTag("developer_information_section"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top divider
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            thickness = 1.dp,
            color = TrendySurfaceCard
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Section Title
        Text(
            text = stringResource(id = R.string.developer_info_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TrendyTextPrimary,
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag("developer_info_title")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Developer Name
        Text(
            text = stringResource(id = R.string.developer_name),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = TrendyTextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag("developer_name")
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tappable Phone Row
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = TrendySurfaceCard.copy(alpha = 0.6f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    launchPhoneDialer(context, "030383631699")
                }
                .testTag("developer_phone_action")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "📞 030383631699",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = TrendyTextPrimary,
                    modifier = Modifier.testTag("developer_phone_text")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tappable Email Row
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = TrendySurfaceCard.copy(alpha = 0.6f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    launchEmailClient(context, "arbabrixvi@gmail.com")
                }
                .testTag("developer_email_action")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "✉️ arbabrixvi@gmail.com",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = TrendyNeonCyan,
                    modifier = Modifier.testTag("developer_email_text")
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom divider
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            thickness = 1.dp,
            color = TrendySurfaceCard
        )
    }
}

/**
 * Initiates the Android dialer with the specified number.
 * Uses ACTION_DIAL so Android's normal user interaction/confirmation occurs,
 * requiring zero dangerous runtime permissions.
 */
private fun launchPhoneDialer(context: Context, phoneNumber: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Unable to open phone dialer", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Opens the user's default email client addressed to the developer.
 */
private fun launchEmailClient(context: Context, emailAddress: String) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$emailAddress")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(Intent.createChooser(intent, "Send Email"))
    } catch (e: Exception) {
        Toast.makeText(context, "Unable to open email client", Toast.LENGTH_SHORT).show()
    }
}
