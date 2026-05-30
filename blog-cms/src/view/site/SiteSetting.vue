<template>
  <div>
    <!-- Breadcrumb navigation -->
    <Breadcrumb parent-title="Quản lý hệ thống" />

    <el-card>
        <el-row :gutter="24">
        <!-- Cài đặt cơ bản -->
          <el-col :span="12">
            <el-card>
              <template #header>
                <div style="justify-content: space-between;display: flex">
                  <span>Cài đặt cơ bản</span>
                  <el-switch  v-model="e1"></el-switch>
                </div>
              </template>
              <el-form label-position="right" label-width="100px">
                <el-form-item
                    :label="item.nameVn"
                    v-for="item in typeMap.type1"
                    :key="item.id"
                >
                  <el-input :type="e1?'textarea':'text'" v-model="item.value" />
                </el-form-item>
              </el-form>
            </el-card>
          </el-col>

        <!-- Thông tin thẻ cá nhân -->
          <el-col :span="12">
            <el-card>
              <template #header>
                <div style="justify-content: space-between;display: flex">
                  <span>Thông tin thẻ cá nhân</span>
                  <el-switch v-model="e2"></el-switch>
                </div>
              </template>
              <el-form label-position="right" label-width="100px">
                <el-form-item
                    :label="item.nameVn"
                    v-for="item in typeMap.type3"
                    :key="item.id"
                    style="position: relative !important;"
                >
                  <el-input
                      :type="e2?'textarea':'text'" v-model="item.value"
                      :style="{ width: item.nameEn === 'favorite' ? '85%' : '100%' }"
                      :autosize="{ minRows: 1, maxRows: 50 }"
                  />
                  <el-button v-if="item.nameEn === 'favorite'"
                             type="danger" :icon="Delete" @click="deleteFavorite(item)"
                             style="width: 10% !important; margin: 0 auto !important;"

                  >Xóa</el-button>
                </el-form-item>
                <el-button type="primary" :icon="Plus" @click="addFavorite">Thêm đam mê</el-button>
              </el-form>
            </el-card>
          </el-col>
      </el-row>

      <!-- Phần huy hiệu chân trang -->
      <el-row style="margin-top: 20px">
        <el-card>
          <template #header>
            <span>Huy hiệu chân trang</span>
          </template>

          <!-- Danh sách huy hiệu -->
          <div v-for="badge in typeMap.type2" :key="badge.id" class="badge-item">
            <el-form :inline="true" :model="badge">
              <el-form-item label="Tiêu đề">
                <el-input v-model="badge.value.title" placeholder="Nhập tiêu đề" />
              </el-form-item>
              <el-form-item label="URL">
                <el-input v-model="badge.value.url" placeholder="Nhập URL" />
              </el-form-item>
              <el-form-item label="Chủ đề">
                <el-input v-model="badge.value.subject" placeholder="Nhập chủ đề" />
              </el-form-item>
              <el-form-item label="Giá trị">
                <el-input v-model="badge.value.value" placeholder="Nhập giá trị" />
              </el-form-item>
              <el-form-item label="Màu sắc">
                <el-color-picker v-model="badge.value.color" color-format="hex" />
                <el-input
                    v-model="badge.value.color"
                    placeholder="Nhập mã màu"
                    style="margin-left: 10px; width: 120px"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="danger" :icon="Delete" @click="deleteBadge(badge)">
                  Xóa
                </el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- Nút thêm huy hiệu mới -->
          <el-button type="primary" :icon="Plus" @click="addBadge">
            Thêm huy hiệu
          </el-button>
        </el-card>
      </el-row>

      <!-- Nút lưu -->
      <div class="save-button-container">
        <el-button type="primary" :icon="Check"  @click="submit" >
          Lưu cài đặt
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { cloneDeep } from 'lodash-es'
import { Delete, Plus, Check } from '@element-plus/icons-vue'
import Breadcrumb from "@/components/Breadcrumb.vue"
import {getSiteSettingData, update,} from "@/api/siteSetting"
import {useToast} from "@/plugins/primevueConfig/primePluginVue.js";

const toast = useToast()

// Dữ liệu
const deleteIds = ref([]);
const typeMap = ref({
  type1: [], // Cài đặt cơ bản
  type2: [], // Huy hiệu chân trang
  type3: []  // Thông tin thẻ cá nhân
});
const e1 = ref(false)
const e2 = ref(false)
// Hàm xử lý
const getList = async () => {
  try {
    const res = await getSiteSettingData()
    if (res.code === 200) {
      res.data.type2.forEach(item => {
        try {
          if (typeof item.value === 'string')
            item.value = JSON.parse(item.value)
        } catch (error) {
          item.value = {}
        }
      })
      typeMap.value = res.data
      toast.success(res.msg || 'Tải dữ liệu thành công')
    } else {
      toast.info(res.msg || 'Tải dữ liệu thất bại')
    }
  } catch (error) {
    console.error('Lỗi khi tải dữ liệu:', error)
    toast.error(error)
  }
}



const addBadge = () => {
  typeMap.value.type2.push({
    key: Date.now(),
    nameEn: "badge",
    nameVn: "Huy hiệu",
    type: 2,
    value: {
      color: '#409EFF',
      subject: '',
      title: '',
      url: '',
      value: ''
    }
  })
  toast.info('Đã thêm huy hiệu mới')
  console.log(typeMap.value)
}

const addFavorite = () => {
  typeMap.value.type3.push({
    key: Date.now(),
    nameEn: 'favorite',
    nameVn: 'Đam mê',
    type: 3,
    value: "{\"title\":\"---\",\"content\":\"---\"}",
  })
}

const deleteFavorite = (favorite) => {
  let arr = typeMap.value.type3
  if(favorite.id){
    deleteIds.value.push(favorite.id);
    for(let i = 0 ; i < arr.length ; i++)
      if(arr[i].id === favorite.id){
        arr.splice(i,1);
        break;
      }
    console.log(deleteIds.value)
  }else{
    for(let i = 0 ; i < arr.length ; i++)
      if(arr[i].key === favorite.key){
        arr.splice(i,1);
        break;
      }
  }
}

const deleteBadge = (badge) => {
  let arr = typeMap.value.type2;
  if(badge.id){
    deleteIds.value.push(badge.id)
    for(let i = 0; i < arr.length;i++)
      if(arr[i].id === badge.id){
        arr.splice(i,1);
        break;
      }
    console.log(deleteIds.value)
  }else {
    for(let i = 0; i < arr.length;i++)
      if(arr[i].key === badge.key){
        arr.splice(i,1);
        break;
      }
  }
}

const submit = async () => {
  const result = cloneDeep(typeMap.value)
  result.type2.forEach(item => item.value = JSON.stringify(item.value))
  let updateArr = []
  updateArr.push(...result.type1)
  updateArr.push(...result.type2)
  updateArr.push(...result.type3)
  console.log(updateArr)
  try{
    const res = await update(updateArr, deleteIds.value);
    if(res.code === 200){
      deleteIds.value = []
      await getList()
      toast.success(res.msg)
    }else
      toast.info(res.msg)
  }catch (e){
    console.log(e.response.data)
    toast.error(e.response.data.msg)
  }
}

const removeBadge = (id) => {
  const index = typeMap.value.type2.findIndex(item => item.id === id)
  if (index !== -1) {
    typeMap.value.type2.splice(index, 1)
    toast.success('Đã xóa huy hiệu')
  }
}



// Lifecycle hook
onMounted(() => {
  getList()
})
</script>

<style scoped>

.badge-item {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 16px;
  border: 1px solid #e9ecef;
  transition: all 0.3s ease;
}

.badge-item:hover {
  background: #f1f3f5;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.badge-item + .badge-item {
  margin-top: 16px;
}

.save-button-container {
  text-align: right;
  margin-top: 30px;
  padding: 20px 0;
  border-top: 1px solid #e9ecef;
}

/* Responsive */
@media (max-width: 768px) {
  .el-col {
    width: 100%;
    margin-bottom: 20px;
  }

  .badge-item .el-form--inline {
    display: flex;
    flex-direction: column;
  }

  .badge-item .el-form-item {
    margin-right: 0;
    margin-bottom: 12px;
  }
}

/* Animation cho các hành động */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
