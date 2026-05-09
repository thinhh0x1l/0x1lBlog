<template>
  <div class="fixed-nav" id="navR">
    <nav  ref="navRef" class="text-white" style="background-color: rgb(27, 28, 29); border: none;">
      <div class="container  ">
        <div class="flex-nav">
            <router-link class="no-underline nav-line " to="/">
              <h3 class="m-blue"
                  style="margin: 0; padding: 0.900007143em ;font-size: 1.28571429rem;">{{ blogName }}
              </h3>
            </router-link>
            <router-link :class="[mobileMenuOpen? 'm-mobile-show':'m-mobile-hide',
            $route.name==='home'?'active':'']"
                class=" nav-line  no-underline p-3
                t-m-bold transition-colors
                transition-duration-200" to="/home">
              <font-awesome-icon icon="home" class="mr-2" />Trang chủ
            </router-link>
          <div @click="toggleMenu"
              :class="[mobileMenuOpen ? 'm-mobile-show' : 'm-mobile-hide',
                $route.name === 'category' ? 'active' : '',
                'nav-line no-underline t-m-bold transition-colors transition-duration-200 p-3 cursor-pointer'
              ]"
          >
              <font-awesome-icon icon="lightbulb" class="mr-2"/>
              Phân loại
              <Menu ref="menu" :model="categoryItems"
                    :pt="{root: { class: 'custom-menu-nav' }}"
                      @mouseleave="menu?.hide()"
                    :popup="true"  />
              <i class="pi pi-angle-down ml-1"/>
          </div>
          <router-link :class="[mobileMenuOpen? 'm-mobile-show':'m-mobile-hide',
              $route.name==='archives'?'active':'']"
                       class=" nav-line  no-underline t-m-bold
                  transition-colors transition-duration-200 p-3" to="/archives">
            <font-awesome-icon icon="archive" class="mr-2 pi" />Lưu trữ
          </router-link>
            <router-link :class="[mobileMenuOpen? 'm-mobile-show':'m-mobile-hide',
                $route.name==='moments'?'active':'']"
                class=" nav-line  no-underline t-m-bold
                 transition-colors transition-duration-200 p-3" to="/moments">
              <font-awesome-icon icon="comment-dots" class="mr-2" />Khoảng khắc
            </router-link>
            <router-link :class="[mobileMenuOpen? 'm-mobile-show':'m-mobile-hide',
               $route.name==='about'?'active':'']"
                class=" nav-line no-underline t-m-bold
                transition-colors transition-duration-200 p-3" to="/about">
              <font-awesome-icon icon="info-circle" class="mr-2" />Về tôi
            </router-link>

          <div class=" search-container" :class="mobileMenuOpen ? 'm-mobile-show' : 'm-mobile-hide'"
               >
            <div >
              <div class="m-search">
                <div class="card flex justify-center">
                  <AutoComplete
                      v-model="queryString"
                      :suggestions="suggestions"
                      @complete="debounceSearch"
                      optionLabel="title"
                      placeholder="Search..."
                      :forceSelection="false"
                      :loading="loading"
                      @item-select="handleSelect"
                  >
                    <template #option="slotProps">
                      <div v-if="slotProps.option.loading" class="p-2 text-center">
                        <i class="pi pi-spin pi-spinner mr-2"></i>
                        Đang tìm kiếm...
                      </div>

                      <div v-else>
                         <div class="title" >{{ slotProps.option.title }}</div>
                        <span class="content">{{ slotProps.option.content }}</span>
                      </div>
                    </template>
                  </AutoComplete>
                </div>

                <font-awesome-icon icon="search" class="search icon t-m-bold"></font-awesome-icon>

                <!-- Suggestions dropdown -->

              </div>
            </div>
          </div>
        </div>
        <Button
            icon="pi pi-bars"
            class="p-button-text p-2 m-2 m-right-top md:hidden text-white"
            style=""
            @click="toggle"
        />
      </div>
    </nav>
  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, ref} from 'vue'
import {useRouter} from "vue-router";
import type {Category} from "@/types/categoryType.ts";
import type {ApiResponse} from "@/plugins/axios2";
import {fGetCategoryList} from "@/api/category";
import {fSearchBlog} from "@/api/blog";
import type { AutoCompleteCompleteEvent } from 'primevue/autocomplete';

const router = useRouter()
const menu = ref()
const props = defineProps<{
  blogName: string
}>()
const mobileMenuOpen = ref(false);
const toggle = () => {
  mobileMenuOpen.value = !mobileMenuOpen.value;
};
document.addEventListener('click', (e: Event) => {
  const el = document.getElementById('navR')
  const target = e.target as Node
  if(!el?.contains(target))
    mobileMenuOpen.value=false;
})

const toggleMenu = (event: any) => {
  menu.value.toggle(event)
}
const categoryList = ref<Category[]>([])
const categoryRoute = (name: string) => {
  router.push({
    name: "category",
    params: { name }
  })
}
const categoryItems = computed(() =>
    categoryList.value.map(category => ({
      label: category.name,
      command: () => categoryRoute(category.slug)
    }))
)

const getCategoryList = async () => {
  try{
    const res: ApiResponse<Category[]> = await fGetCategoryList();
    if(res.code === 200){
      categoryList.value = res.data
      console.log(categoryList.value)
    }

  }catch (e){
    console.log('error')
  }
}
const queryString = ref<string>('')
const suggestions = ref<any>([])
let timer: any = null

const debounceSearch = (event: AutoCompleteCompleteEvent) => {
  clearTimeout(timer)
  const query = event.query
  if (
      !query ||
      query.trim() === '' ||
      /[%_\[#*]/.test(query) ||
      query.trim().length > 20
  ) {
    suggestions.value = [] // thay callback([])
    return
  }
  loading.value = true
  suggestions.value = [{ title: 'Đang tìm kiếm...', loading: true }]
  timer = setTimeout(() => {
    search(event)
  }, 500) // nên 300–500ms
}

const loading = ref(false)
const search = async (event: AutoCompleteCompleteEvent) => {
  const query = event.query
  try {
    const res: any = await fSearchBlog(query)
    if (res.code / 100 === 2) {
      let data = res.data
      if (!data.length) {
        data = [{ title: 'Không tìm thấy kết quả phù hợp' }]
      }
      suggestions.value = data // thay callback
      console.log(suggestions.value)
    }
  } catch (e) {
    suggestions.value = []
  }finally {
    loading.value = false
  }
}

const handleSelect = (event: any) => {
  const blog = event.value
  if (blog?.loading) return
  if (blog?.id) {
    queryString.value = ''
    router.push(`/blog/${blog.id}`)
  }
}
onMounted(() => {
  getCategoryList()
})
</script>

<style scoped>

.fixed-nav {
  position: fixed;
  top: -1px;
  left: 0;
  right: 0;
  z-index: 1000;
  width: 100%;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
}
.t-m-bold{
  color: rgba(255, 255, 255, 0.8) !important;
  font-weight: 400;
}

.t-m-bold:hover{
  background: #021e30 !important;
  color: #a5cfff !important;
}
.container {
  max-width: 1400px;
  margin-left: auto;
  margin-right: auto;
}

.fixed-nav.scrolled {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  background-color: rgba(27, 28, 29, 0.95) !important;
  backdrop-filter: blur(100 px);
}
.nav-line{
  align-items: center;
  position: relative;
}

.nav-line:before {
  content: '';
  position: absolute;
  width: 1px;
  background: rgba(255, 255, 255, .08);
  top: 0;
  right: 0;
  height: 100%;
}

.search-container{
  margin-left: auto;
  border-left: 1px solid rgba(255, 255, 255, .08);
  align-items: center;
  position: relative;
}

@media (min-width: 768px) {
  .nav-line{
    display: flex !important;
  }
  .flex-nav{
    display: flex;
  }
  .search-container{
    display: flex !important;
  }
}
@media (max-width: 767px) {
  .nav-line:before {
    top: auto;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 1px;
  }
  .flex-nav{
    display: flex; flex-direction: column;
  }
  .search-container{
    margin-left: 0;
    border-left: none!important;
    border-right: none!important;
  }
}
.md\:hidden {
  z-index: 1001;
}

.m-search {
  position: relative;
  min-width: 220px;
  margin: 0;
  padding: 0 !important;
}

/* Input field */
.search-input {
  width: 100%;
  color: rgba(255, 255, 255, .9);
  border: 0 !important;
  background-color: inherit;
  padding: 0.67857143em 2.1em 0.67857143em 1em;
  font-size: 14px;
  font-family: inherit;
  border-radius: 4px;
  outline: none;
  box-sizing: border-box;
  transition: all 0.3s ease;
}

/* Search icon */
.m-search .search.icon {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: rgba(255, 255, 255, .9) !important;
  pointer-events: none;
  font-style: normal;
  font-size: 16px;
}

/* Suggestions dropdown */
.m-search-item {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  min-width: 350px !important;
  max-height: 300px;
  overflow-y: auto;
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border: 1px solid #e4e7ed;
  z-index: 1000;
  margin-top: 2px;
  padding: 5px 0;
}

/* Suggestion item */
.suggestion-item {
  padding: 8px 10px !important;
  cursor: pointer;
  line-height: normal !important;
  transition: background-color 0.3s;
}

.suggestion-item .title {
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  color: rgba(0, 0, 0, 0.87);
  font-size: 14px;
  margin-bottom: 4px;
  font-weight: 500;
}

.suggestion-item .content {
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  font-size: 12px;
  color: rgba(0, 0, 0, .70);
  line-height: 1.4;
}

.p-button-text:hover{
  background: #505050!important;
}

.active{
  background: #023d67 !important;
  color: #93c5fd!important;
}
.title {
  line-height: normal !important;
  padding: 8px 10px !important;
  text-overflow: ellipsis !important;
  overflow: hidden !important;
  color: rgba(0, 0, 0, 0.87) !important;
}
.m-search-item {
  min-width: 350px !important;
}
.content {
  line-height: normal !important;
  padding: 8px 10px !important;
  text-overflow: ellipsis !important;
  font-size: 12px !important;
  color: rgba(0, 0, 0, .70) !important;
}
.m-search.loading .search.icon::after {
  content: "";
  position: absolute;
  top: 50%;
  left: 50%;
  width: 12px;
  height: 12px;
  border: 2px solid transparent;
  border-top-color: rgba(255, 255, 255, .9);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

</style>