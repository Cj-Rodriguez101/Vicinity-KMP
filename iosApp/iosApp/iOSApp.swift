import ComposeApp
import FirebaseCore
import FirebaseStorage
import SwiftUI

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    handleDeepLink(url: url)
                }
        }
    }

    private func handleDeepLink(url: URL) {
        print("📱 iOS received deep link: \(url)")

        if url.scheme == "auth" {
            print("🔐 Auth callback detected on iOS")
            handleAuthCallback(url: url.absoluteString)
        } else if url.scheme == "vicinity" {  // or your chosen scheme
            print("🔗 Forwarding to KMP: \(url)")
            ExternalUriHandler.shared.onNewUri(uri: url.absoluteString)
        }
    }

    //claude vibe coding ios
    private func handleAuthCallback(url: String) {
        print("🔗 Handling auth callback: \(url)")

        guard let urlComponents = URLComponents(string: url),
            let fragment = urlComponents.fragment ?? urlComponents.query
        else {
            print("❌ No tokens found in URL")
            return
        }

        let params = parseURLParams(fragment)
        print("📋 Available parameters: \(params)")

        guard let accessToken = params["access_token"],
            let refreshToken = params["refresh_token"] ?? params["refresh"]
        else {
            print("❌ Missing required tokens")
            return
        }

        Task {
            do {
                try await AuthHelper.shared.setAuthSession(
                    accessToken: accessToken,
                    refreshToken: refreshToken,
                    providerToken: params["provider_token"],
                    expiresIn: params["expires_in"],
                    expiresAt: params["expires_at"]
                )
                print("✅ Auth session set successfully")
            } catch {
                print("❌ Failed to set auth session: \(error)")
            }
        }
    }

    private func parseURLParams(_ paramString: String) -> [String: String] {
        var params: [String: String] = [:]
        let pairs = paramString.components(separatedBy: "&")

        for pair in pairs {
            let keyValue = pair.components(separatedBy: "=")
            if keyValue.count == 2 {
                let key = keyValue[0]
                let value = keyValue[1].removingPercentEncoding ?? keyValue[1]
                params[key] = value
            }
        }

        return params
    }
}

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        FirebaseApp.configure()
        return true
    }
}