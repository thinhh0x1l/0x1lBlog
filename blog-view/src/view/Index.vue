<template>
  <div class="site">
    <Nav :blogName="blogName"/>

    <div class="main">
      <div class="py-6">

        <div class="container mx-auto" style="max-width: 1450px;">
          <div class="flex flex-row flex-nowrap ">

            <div class="hidden md:block flex-none sticky-sidebar-wrapper" style="width: 18.75% !important;">
              <div class="sticky-sidebar">
                <Introduction
                    v-show="$route.name!=='blog'"
                    v-if="!showIntro"
                />
                <div class="toc-wrapper">
                  <Toc
                      ref="tocComponent"
                      :contentSelector="'.blog-content'"
                      headingSelector="h1, h2, h3"
                      :scrollOffset="80"
                      v-show="showE"
                      v-if="showPlatform"
                  />
                </div>
              </div>
            </div>
            <div class="flex-1 min-w-0 main-content"
                 :class="{'overlay':showIntro}"
                 style="width: 62.5% !important"
            >
              <router-view v-slot="{ Component }">
                <keep-alive :include="['Home']">
                  <component :is="Component" />
                </keep-alive>
              </router-view>
            </div>

            <div class="hidden md:block flex-none" style="width: 18.75% !important;">
              <Tags :tag-list="tags"/>
            </div>

            <div class="bat"></div>
          </div>

        </div>
      </div>
      <button
          @click="scrollToTop"
          class="scroll-top-btn"
      >
        <img src="/src/assets/img/plane-top.png"  alt="dfds">
      </button>
    </div>

    <font-awesome-icon

      :icon="['fa',showIntro?'circle-arrow-left':'circle-arrow-right']"
      style="color: rgb(43, 47, 51); z-index: 40; top:300px; width: auto; height:  30px"
      class="md:hidden fixed left-4 px-3 py-2"
      @click="showIntro = !showIntro"
    />
    <div
        v-if="showIntro"
        class="fixed text-white px-3 "
        style="z-index: 10; top: 75px;"
    >
      <div style=" width: 40%">
        <Introduction />
      </div>
    </div>
    <font-awesome-icon
        v-show="showE"
        :icon="['fa',showToc?'book-open':'book']"
        style="color: rgb(43, 47, 51); z-index: 40; top:400px; width: auto; height: 30px"
        class="md:hidden fixed left-4 px-3 py-2"
        @click="showToc = !showToc;"
    />
    <div
        v-if="!showPlatform"
        v-show="showToc"
        class="fixed text-white px-3 "
        style="z-index: 10; top: 400px;"
    >
      <div class="toc-wrapper">
        <Toc
            ref="tocComponent"
            :contentSelector="'.blog-content'"
            headingSelector="h1, h2, h3"
            :scrollOffset="80"
            v-show="showToc"
        />
      </div>
    </div>
    <Footer :siteInfo="siteInfo" :badges="badges" :newBlogList="newBlogList" :hitokoto="hitokoto"/>
  </div>

</template>

<script setup lang="ts">
import Nav from "@/components/index/Nav.vue";
import {computed, nextTick, onMounted, ref, watch} from "vue";
import Footer from "@/components/index/Footer.vue";
import {getHitokoto, getSite, translateUrl} from "@/api/index.js";
import {useAppStore} from "@/store";
import {useRoute} from "vue-router";
import Introduction from "@/components/sidebar/Introduction.vue";
import {storeToRefs} from "pinia";
import {useScrollToTop} from '@/util/ScrollToTop.js'
import Tags from "@/components/sidebar/Tags.vue";
import {getTags} from '@/api/tags'
import type {ApiResponse} from "@/plugins/axios2";
import type {Tag, TagSlug} from "@/types/tagType";
import Toc from "@/components/sidebar/Toc.vue";
import {useBlogDetailStore} from "@/store/blogDetailStore";
import {useWindowSize} from "@vueuse/core";

const tags = ref<TagSlug[]>([])
const loading = ref(false)
const error = ref<string|null>(null)

const {scrollToTop} = useScrollToTop()

const route = useRoute()
const store = useAppStore()
const blogDetailStore = useBlogDetailStore()
const {isBlogRenderCompleted} = storeToRefs(blogDetailStore)
const {siteInfo} = storeToRefs(store)

const badges = ref([])
const newBlogList = ref([])
const hitokoto = ref<Record<string, any>>({})
const blogName = computed(() => siteInfo.value?.blogName || 'Thinh0x1l\'')

const showIntro = ref(false)
const showToc= ref(true)
const { width } = useWindowSize()

watch(() => width.value, (w) => {
  if(w > 768){
    showIntro.value = false
    showPlatform.value = true
  }else{
    showPlatform.value = false
    showToc.value = true
  }
})

// Functions
const getHitokotoData = async () => {
  try {
    const res = await getHitokoto()
    hitokoto.value = res
    const trans1: Record<string, any> = await translateUrl(hitokoto.value.hitokoto)
    const trans2: Record<string, any> = await translateUrl(hitokoto.value.from)
    hitokoto.value.hitokoto = trans1[0][0][0]
    hitokoto.value.from = trans2[0][0][0]
  } catch (error) {
    console.error('Lấy hitokoto thất bại:', error)
    hitokoto.value = {
      hitokoto: 'Hãy viết code bằng cả trái tim',
      from: 'Lập trình viên'
    }
  }
}

const site = async () => {
  try {
    const res:Record<string, any> = await getSite();
    if(res.code === 200){
      badges.value = res.data.badges
      newBlogList.value = res.data.newBlogList

      store.saveIntroduction(res.data.introduction)
      store.saveSiteInfo(res.data.siteInfo)
      watch(
          [() => route.meta?.title , () => store.webTitleSuffix],
          ([title, suffix]) => {
            document.title = `${title || ''} | ${suffix}`;
          },
          { immediate: true }
      )
    }
  } catch (e) {
    console.error(e)
  }
}
const fetchTags = async () => {
  loading.value = true
  error.value = null
  try {
    const response: ApiResponse<TagSlug[]> = await getTags()
    if(response.code === 200){
      tags.value = response.data ?? []
    }
  }catch (err: any){
  }finally {
    loading.value = false
  }
}

const tocComponent = ref<InstanceType<typeof Toc>|null>(null)
const showE = ref(window.innerWidth>768)
const showPlatform = ref(window.innerWidth>768)// 1:pc, 0: mobile
watch(
    () => isBlogRenderCompleted.value,
    async (ready) => {
      showE.value = false
      const toc = tocComponent.value
      if (!toc) return
      if (!ready) {
        toc.cleanup()
        return
      }
      toc.refreshToc()
      await nextTick()
      showE.value = true
      const hash = route.hash
      if (hash) {
        const id = hash.slice(1)
        toc.scrollToHeading(id)
      }
    },
)
onMounted(() => {
  site()
  getHitokotoData()
  fetchTags()
})

</script>

<style scoped>
/* Reset các thuộc tính có thể ảnh hưởng đến sticky */
.site, .main, .container, .flex {
  position: static !important;
  overflow: visible !important;
}

.site {
  display: flex;
  min-height: 100vh;
  flex-direction: column;
}

.main {
  margin-top: 40px;
  flex: 1;
  position: relative;
}
.main-content{
  padding-left: 1rem;
  padding-right: 1rem;
  padding-bottom: 1rem;
}
@media (max-width: 768px) {
  .main-content{
    padding-right: max((100vw - 421px)/25, 0px) !important;
    padding-left: max((100vw - 421px)/25, 0px) !important;
  }
}
.scroll-top-btn img{
  width: 56px;
  height: 56px;
}
.scroll-top-btn {
  background: none;
  position: fixed;
  bottom: 2.5rem;
  right: 2.5rem;
  border: none;
  cursor: pointer;
  z-index: 1000;
}

/* Style cho TOC wrapper */
.toc-wrapper {
  position: sticky;
  top: 80px;
  max-height: calc(100vh - 100px);
  overflow-y: auto;
  padding: 0.5rem;
  border-left: 1px solid #e9ecef;
}



@media (max-width: 768px) {
  .blog-container {
    padding-right: max((100vw - 421px)/25, 0px) !important;
    padding-left: max((100vw - 421px)/25, 0px) !important;
  }
  .comment-container {
    padding-right: max((100vw - 421px)/25, 2px) !important;
    padding-left: max((100vw - 421px)/25, 2px) !important;
  }
}

.header {
  border: none;
  margin: 0 1rem;
  top: 13px;
  padding: 0 0;
  font-size: 1.71428571rem;
  font-family: Lato, 'Helvetica Neue', Arial, Helvetica, sans-serif;
  font-weight: 700;
  line-height: 1.28571429em;
  text-transform: none;
  color: rgba(0, 0, 0, .87);
}
.scroll-top-btn:hover {
  transform: translateY(-3px);
  filter: brightness(1.1);
}
/* Sticky sidebar wrapper */
.sticky-sidebar-wrapper {
  align-self: flex-start;
  position: sticky;
  top: 68px;
  height: 100%;
}
.overlay {
  position: relative;
}

.overlay::after {
  content: "";
  position: absolute;
  inset: 0;
  background: rgba(0,0,0,0.4); /* mức độ tối */
  pointer-events: none;
}
/*.sticky-sidebar {
  position: sticky;n dev
  top: 10px;
  height: fit-content;
  overflow-y: auto;
}*/

</style>