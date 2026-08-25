<template>
  <menu-logo></menu-logo>
  <el-menu
      :default-active="defaultActive"
      class="el-menu-vertical-demo"
      :collapse="isCollapse"
      @open="handleOpen"
      @close="handleClose"
      background-color="#304156"
      unique-opened
      router
  >
    <menu-item :menuList="menuList"></menu-item>
  </el-menu>
</template>
<script setup lang="ts">
import { ref, reactive, computed} from "vue";
import {menuStore} from '@/stores/menu/index.ts'
import { useRoute } from 'vue-router';
import MenuItem from "@/layout/menu/MenuItem.vue";
import MenuLogo from "@/layout/menu/MenuLogo.vue";
//获取store
const store = menuStore()
//获取状态
const isCollapse = computed(()=>{
  return store.getCollapse
})
const route = useRoute()
//定义响应式数据
// const isCollapse = ref(false)
//当前激活的菜单:当前激活的菜单
const defaultActive = computed(()=>{
  const {path} = route
  return path
})
const menuList = computed(()=>{
  return store.getMenu
})
const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
</script>
<style scoped lang="scss">
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 230px;
  min-height: 400px;
}
.el-menu {
  border-right: none;
}
:deep(.el-sub-menu .el-sub-menu__title){
  color: #f4f4f5 !important;
}
:deep(.el-menu .el-menu-item){
  color: #bfcbd9;
}
/* 菜单点中文字的颜色 */
:deep(.el-menu-item.is-active){
  color: #409eff !important;
}
/* 当前打开菜单的所有子菜单颜色 */
:deep(.is-opened .el-menu-item){
  background-color: #1f2d3d !important;
}
/* 鼠标移动菜单的颜色 */
:deep(.el-menu-item:hover){
  background-color: #001528 !important;
}
</style>
