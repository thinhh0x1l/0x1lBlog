import {defineStore} from "pinia";
import defaultSettings from '@/settings.js'
import { useDark, useToggle } from '@vueuse/core'

export const useSettingsStore = defineStore('settings',() =>{

    const storageSetting = JSON.parse(
        localStorage.getItem('layout-setting')|| '{}'
    )
    const {
        sideTheme,
        showSettings,
        navType,
        tagsView,
        tagsViewPersist,
        tagsIcon,
        tagsViewStyle,
        fixedHeader,
        sidebarLogo,
        dynamicTitle,
        footerVisible,
        footerContent
    } = defaultSettings

    const title = ref('')
    const isThemeDark = ref(true)


    const theme = ref(
        storageSetting.theme ?? '#409EFF'
    )

    const sideThemeState = ref(
        storageSetting.sideTheme ?? sideTheme
    )

    const navTypeState = ref(
        storageSetting.navType ?? navType
    )

    const tagsViewState = ref(
        storageSetting.tagsView ?? tagsView
    )

    const tagsViewPersistState = ref(
        storageSetting.tagsViewPersist ?? tagsViewPersist
    )

    const tagsIconState = ref(
        storageSetting.tagsIcon ?? tagsIcon
    )

    const tagsViewStyleState = ref(
        storageSetting.tagsViewStyle ?? tagsViewStyle
    )

    const fixedHeaderState = ref(
        storageSetting.fixedHeader ?? fixedHeader
    )

    const sidebarLogoState = ref(
        storageSetting.sidebarLogo ?? sidebarLogo
    )

    const dynamicTitleState = ref(
        storageSetting.dynamicTitle ?? dynamicTitle
    )

    const footerVisibleState = ref(
        storageSetting.footerVisible ?? footerVisible
    )

    const footerContentState = ref(
        footerContent
    )
    const isDark = useDark()

    const toggleDark = useToggle(isDark)

    function setTitle(newTitle) {
        title.value = newTitle
        // useDynamicTitle()
    }

    function updateSetting(key, value) {
        if (!(key in settings)) return

        settings[key].value = value
    }

    function toggleTheme() {
        toggleDark()

        // nextTick(() => {
        //     handleThemeStyle(theme.value)
        // })
    }
    const settings = {
        theme,
        sideTheme: sideThemeState,
        navType: navTypeState,
        tagsView: tagsViewState,
        tagsViewPersist: tagsViewPersistState,
        tagsIcon: tagsIconState,
        tagsViewStyle: tagsViewStyleState,
        fixedHeader: fixedHeaderState,
        sidebarLogo: sidebarLogoState,
        dynamicTitle: dynamicTitleState,
        footerVisible: footerVisibleState
    }

    return {
        title,
        isThemeDark,
        theme,
        sideTheme: sideThemeState,

        showSettings,

        navType: navTypeState,

        tagsView: tagsViewState,
        tagsViewPersist: tagsViewPersistState,
        tagsIcon: tagsIconState,
        tagsViewStyle: tagsViewStyleState,

        fixedHeader: fixedHeaderState,
        sidebarLogo: sidebarLogoState,

        dynamicTitle: dynamicTitleState,

        footerVisible: footerVisibleState,
        footerContent: footerContentState,

        isDark,

        setTitle,
        updateSetting,
        toggleTheme
    }
})