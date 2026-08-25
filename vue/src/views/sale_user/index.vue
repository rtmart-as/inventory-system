<template>
  <el-main>
    <el-form :model="searchParm" :inline="true" size="default">
      <el-form-item>
        <el-date-picker
            v-model="searchParm.startTime"
            type="month"
            value-format="YYYY-MM"
            placeholder="请选择开始时间"
        />
      </el-form-item>
      <el-form-item>
        <el-date-picker
            v-model="searchParm.endTime"
            type="month"
            value-format="YYYY-MM"
            placeholder="请选择结束时间"
        />
      </el-form-item>
      <el-form-item>
        <el-button icon="Search" @click="searchBtn">搜索</el-button>
        <el-button icon="Close" type="danger" plain @click="resetBtn"
        >重置</el-button
        >
        <el-button icon="Plus" type="success" @click="exportBtn"
        >导出</el-button
        >
      </el-form-item>
    </el-form>
    <el-table :data="tableList" border stripe>
      <el-table-column prop="nickName" label="姓名"></el-table-column>
      <el-table-column prop="salePrice" label="销售金额"></el-table-column>
      <el-table-column prop="saleMonth" label="月份"></el-table-column>
    </el-table>
  </el-main>
</template>

<script setup lang="ts">
import {getEverySaleTotalApi} from '@/api/sale_order/index'
import { onMounted, reactive, ref } from 'vue';
import {userStore} from '@/stores/user/index'
const ustore = userStore()
//列表参数
const searchParm = reactive({
  currentPage:1,
  pageSize:10,
  startTime:'',
  endTime:'',
  userId:'',
  total:0
})
//定义表格数据
const tableList = ref([])
const getSaleTotal = async()=>{
  searchParm.userId = ustore.getUserId
  let res = await getEverySaleTotalApi(searchParm)
  if(res && res.code == 200){
    tableList.value = res.data.records;
  }
}
//搜索按钮
const searchBtn = () => {
  getSaleTotal();
};
//重置按钮
const resetBtn = () => {
  searchParm.currentPage = 1;
  searchParm.startTime = "";
  searchParm.endTime = "";
  getSaleTotal();
};
//导出
const exportBtn = () => {
  const abtn = document.createElement("a");
  abtn.href =
      import.meta.env.VITE_BASE_API +
      "/api/saleOrder/exportStaff?token=" +
      ustore.getToken +
      "&currentPage=" +
      searchParm.currentPage +
      "&pageSize=" +
      searchParm.pageSize +
      "&startTime=" +
      searchParm.startTime +
      "&endTime=" +
      searchParm.endTime +
      "&userId=" + ustore.getUserId
  abtn.click();
};
onMounted(()=>{
  getSaleTotal()
})
</script>

<style scoped>

</style>
