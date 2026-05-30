// const BRIDGE_ORIGIN = import.meta.env.VITE_BRIDGE_FE;
//
// export function getGuestToken() {
//
//     return new Promise((resolve, reject) => {
//
//         // iframe bridge
//         const iframe = document.getElementById("bridge-frame");
//
//         if (!iframe) {
//             reject("Không tìm thấy iframe");
//             return;
//         }
//         console.log("origin: "+origin)
//         console.log("BRIDGE_ORIGIN: "+BRIDGE_ORIGIN)
//         // timeout
//         const timeout =
//             setTimeout(() => {
//                 window.removeEventListener("message", listener);
//                 reject("Lấy token timeout");
//             }, 5000);
//
//         // nhận message
//         function listener(event) {
//             // sai origin
//             console.log(event.origin)
//             if (normalize(event.origin) !==
//                 normalize(BRIDGE_ORIGIN))
//                 return;
//
//             const data = event.data;
//
//             // nhận token
//             if (data.type === "TOKEN_RESPONSE") {
//                 clearTimeout(timeout);
//                 window.removeEventListener("message", listener);
//                 resolve(data.token);
//             }
//         }
//         const normalize = (url) =>
//             url.replace(/\/$/, "").trim()
//         // lắng nghe message
//         window.addEventListener("message", listener);
//
//         // gửi yêu cầu lấy token
//         iframe.contentWindow
//             ?.postMessage(
//                 {
//                     type: "GET_TOKEN"
//                 },
//                 BRIDGE_ORIGIN
//             );
//         }
//     );
// }
const BRIDGE_ORIGIN = import.meta.env.VITE_BRIDGE_FE;

// Định nghĩa normalize TRƯỚC khi dùng
const normalize = (url) => {
    if (!url) return '';
    return url.replace(/\/$/, "").trim().toLowerCase();
};

export function getGuestToken() {
    return new Promise((resolve, reject) => {
        // Kiểm tra BRIDGE_ORIGIN
        if (!BRIDGE_ORIGIN) {
            console.error("❌ VITE_BRIDGE_FE is not defined in .env");
            reject("VITE_BRIDGE_FE không được cấu hình");
            return;
        }

        // Tìm iframe
        const iframe = document.getElementById("bridge-frame");
        if (!iframe) {
            console.error("❌ Không tìm thấy iframe với id='bridge-frame'");
            reject("Không tìm thấy iframe bridge");
            return;
        }

        // Log thông tin debug
        console.log("=== DEBUG GET GUEST TOKEN ===");
        console.log("📍 Current origin:", window.location.origin);
        console.log("🔗 BRIDGE_ORIGIN from env:", BRIDGE_ORIGIN);
        console.log("🔗 Normalized BRIDGE_ORIGIN:", normalize(BRIDGE_ORIGIN));
        console.log("🖼️ Iframe src:", iframe.src);
        console.log("📦 Iframe contentWindow:", iframe.contentWindow);

        // Timeout handle
        let timeoutId;

        // Listener function
        function listener(event) {
            console.log("📨 Received message:", {
                origin: event.origin,
                normalizedOrigin: normalize(event.origin),
                data: event.data,
                targetOrigin: normalize(BRIDGE_ORIGIN)
            });

            // Kiểm tra origin với nhiều cách
            const normalizedEventOrigin = normalize(event.origin);
            const normalizedBridgeOrigin = normalize(BRIDGE_ORIGIN);

            // Thử so sánh linh hoạt hơn
            const isOriginMatch =
                normalizedEventOrigin === normalizedBridgeOrigin ||
                normalizedEventOrigin.includes('0x1l-blog.vercel.app') ||
                normalizedBridgeOrigin.includes('0x1l-blog.vercel.app');

            if (!isOriginMatch) {
                console.warn("⚠️ Origin không khớp:", {
                    received: normalizedEventOrigin,
                    expected: normalizedBridgeOrigin
                });
                return;
            }

            console.log("✅ Origin matched!");

            const data = event.data;
            console.log("📦 Message data:", data);

            // Nhận token response
            if (data && data.type === "TOKEN_RESPONSE") {
                console.log("🎉 Nhận được token response:", data.token);
                clearTimeout(timeoutId);
                window.removeEventListener("message", listener);

                if (data.token) {
                    resolve(data.token);
                } else {
                    reject("Token rỗng từ bridge");
                }
            } else if (data && data.type === "ERROR") {
                console.error("❌ Nhận error từ bridge:", data.error);
                clearTimeout(timeoutId);
                window.removeEventListener("message", listener);
                reject(data.error || "Lỗi từ bridge");
            }
        }

        // Đăng ký listener
        window.addEventListener("message", listener);

        // Set timeout
        timeoutId = setTimeout(() => {
            console.error("⏰ Timeout sau 5 giây - không nhận được phản hồi");
            window.removeEventListener("message", listener);
            reject("Timeout: Không nhận được token từ bridge sau 5 giây");
        }, 5000);

        // Gửi message đến iframe
        try {
            console.log("📤 Gửi message GET_TOKEN đến iframe");
            iframe.contentWindow?.postMessage(
                {
                    type: "GET_TOKEN",
                    timestamp: Date.now()
                },
                BRIDGE_ORIGIN
            );
            console.log("✅ Đã gửi message thành công");
        } catch (error) {
            console.error("❌ Lỗi khi gửi message:", error);
            clearTimeout(timeoutId);
            window.removeEventListener("message", listener);
            reject("Không thể gửi message đến iframe: " + error.message);
        }
    });
}