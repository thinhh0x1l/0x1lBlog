import {useGuestStore} from "@/store/guessStore.js";

export default {
    install(app) {
        app.mixin({
            mounted() {
                const guestStore
                    = useGuestStore()
                window.addEventListener('beforeunload', () => {
                    if (!localStorage.getItem(guestStore.TOKEN_KEY)) {
                        guestStore.setToken(guestStore.guestToken)
                        console.log('guest_token_backup ' + guestStore.guestToken)
                    }
                })
            }
        })
    }
}