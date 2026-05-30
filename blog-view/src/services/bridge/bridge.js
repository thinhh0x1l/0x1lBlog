const BRIDGE_ORIGIN =
    "http://localhost:5173";

export function getGuestToken() {

    return new Promise((resolve, reject) => {

        // iframe bridge
        const iframe = document.getElementById("bridge-frame");

        if (!iframe) {
            reject("Không tìm thấy iframe");
            return;
        }

        // timeout
        const timeout =
            setTimeout(() => {
                window.removeEventListener("message", listener);
                reject("Lấy token timeout");
            }, 5000);

        // nhận message
        function listener(event) {
            // sai origin
            if (event.origin !== BRIDGE_ORIGIN)
                return;

            const data = event.data;

            // nhận token
            if (data.type === "TOKEN_RESPONSE") {
                clearTimeout(timeout);
                window.removeEventListener("message", listener);
                resolve(data.token);
            }
        }

        // lắng nghe message
        window.addEventListener("message", listener);

        // gửi yêu cầu lấy token
        iframe.contentWindow
            ?.postMessage(
                {
                    type: "GET_TOKEN"
                },
                BRIDGE_ORIGIN
            );
        }
    );
}