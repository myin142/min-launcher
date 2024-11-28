package io.github.myin.phone.data.calendar

import android.content.Context
import android.util.Log
import androidx.credentials.*
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch


class GoogleLogin(private val activityContext: Context) {
    companion object {
        const val TAG = "GoogleLogin"
        const val WEB_CLIENT_ID = "175140229326-h4suagepu4l1eidjpedmb6fmoir06931.apps.googleusercontent.com"
        const val ANDROID_CLIENT_ID = "175140229326-i7mt89tjbri85bgci2nml6nmunvsdhr0.apps.googleusercontent.com"
    }

    private val loginOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId(WEB_CLIENT_ID)
        .setAutoSelectEnabled(true)
        .setNonce("SomeRandomStringForTestingThatShouldBeReplacedAtSomePointInTheFutureSoItIsMoreSecureThanThisShit")
        .build()

    private val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(WEB_CLIENT_ID)
        .setNonce("")
        .build()

    var responseJson: String? = null

    fun login(activity: LifecycleOwner) {
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(loginOption)
            .build()

        val credentialManager: CredentialManager = CredentialManager.create(activityContext)

        activity.lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = activityContext,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                handleFailure(e)
            }
        }
    }

    private fun handleFailure(e: Exception) {
        e.printStackTrace();
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        val credential = result.credential
        Log.i(TAG, "Received credential: $credential")

        when (credential) {

            // Passkey credential
            is PublicKeyCredential -> {
                // Share responseJson such as a GetCredentialResponse on your server to
                // validate and authenticate
                responseJson = credential.authenticationResponseJson
            }

            // Password credential
            is PasswordCredential -> {
                // Send ID and password to your server to validate and authenticate.
                val username = credential.id
                val password = credential.password
            }

            // GoogleIdToken credential
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract the ID to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        // You can use the members of googleIdTokenCredential directly for UX
                        // purposes, but don't use them to store or control access to user
                        // data. For that you first need to validate the token:
                        // pass googleIdTokenCredential.getIdToken() to the backend server.
                        // TODO: maybe verify if needed
//                        GoogleIdTokenVerifier verifier = Builder (transport, jsonFactory) // Specify the CLIENT_ID of the app that accesses the backend:
//                        .setAudience(listOf(WEB_CLIENT_ID)) // Or, if multiple clients access the backend:
//                            .build()
//                        GoogleIdToken idToken = verifier . verify (idTokenString);
                        // To get a stable account identifier (e.g. for storing user data),
                        // use the subject ID:
//                        idToken.getPayload().getSubject()
                        println(googleIdTokenCredential.displayName)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }
}
