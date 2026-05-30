import {useGuestStore} from "@/store/guessStore.js";

import {getGuestToken} from "@/services/bridge/bridge.js";
import {createGuestToken} from "@/api/index.js";
import {createBridgeFrame } from "@/services/bridge/createBridgeFrame.js";

export async function initGuestToken() {
    if (window.self !== window.top) {
        return
    }
    const guestStore =
        useGuestStore();

    if (guestStore.isTokenExist)
        return;

    // tạo iframe bridge
    await createBridgeFrame();

    try {
        // hỏi FE khác
        const token = await getGuestToken();

        if (token) {
            guestStore.setToken(token);
            console.log("Lấy token từ FE khác thành công");
            return;
        }else{
            console.warn("Lấy token từ FE khác không thành công");
        }
    } catch (e) {
        console.error("Lỗi:FE khác không có token",e);
    }

    // fallback BE
    console.log("Yêu cầu backend cấp token");
    try{
        const res = await createGuestToken()
        console.log(res)
    }catch (e){
        console.error(e)
    }


}